/**
 * Copyright (C) 2010-2011 TVH Group NV. <kalman.tiboldi@tvh.com>
 *
 * This file is part of the MyStroBe project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 package net.mystrobe.client;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.DAORequest;
import net.mystrobe.client.connector.DSRequest;
import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IDAORequest;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.IDSRequest;
import net.mystrobe.client.connector.IDSResponse;
import net.mystrobe.client.connector.IDaoRowList;
import net.mystrobe.client.connector.DAORequest.StartRowMarker;
import net.mystrobe.client.connector.messages.IConnectorResponseMessages;
import net.mystrobe.client.connector.quarixbackend.json.Message;
import net.mystrobe.client.connector.quarixbackend.json.ResponseOption;
import net.mystrobe.client.connector.transaction.IDSRequestTransactionManager;
import net.mystrobe.client.connector.transaction.IDSTransactionManager;
import net.mystrobe.client.connector.transaction.IDSTransactionable;
import net.mystrobe.client.util.DataBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Data source for data beans implementation.</br>
 * 
 * <p>
 * Acts like a data source for data beans. Uses an internal data buffer that
 * handles the fetch data, add/delete/update operations. Data is
 * fetched/synchronized with the back end storage through an application
 * connector.
 * </p>
 * 
 * <p>
 * Implements {@link IConnectorResponseMessages} for connector/connection
 * response messages. Main usage is error handling.
 * </p>
 * 
 * <p>
 * {@link IStateSouce} is used for cursor and data update state callbacks for
 * the UI components that have to update their visibility according to current
 * data operation/cursor state.
 * </p>
 * 
 * @author TVH Group NV
 * 
 * @param <T>
 *            Data source bean type.
 */
public abstract class DataSourceAdaptor<T extends IDataBean> implements
		IDataSource<T>, IDSTransactionable<T>, IStateSource,
		IConnectorResponseMessages, LogEnabled, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2117336399480629777L;

	protected Logger logger = null;

	protected IAppConnector appConnector = null;
	protected T currentData = null;

	protected int cursorPosition = -1;
	protected int cursorPreviousPosition = -1;
	protected IDAOResponse<T> lastDAOResponse = null;
	protected IDAORequest<T> lastDAORequest = null;

	protected IDAOSchema<T> schema = null;
	protected boolean hasLastRow = false;
	protected boolean hasFirstRow = false;
	protected CursorStates cursorState = CursorStates.NoRecordAvailable;
	protected UpdateStates updateState = UpdateStates.UpdateComplete;
	protected boolean isDisabled = false;
	
	boolean offlineMode = false; 
	
	/**
	 * Default data set schema attached to the data object.
	 * 
	 * All data operations will go to this data set unless other data set is set
	 * in the constructors.
	 */
	protected IDSSchema defaultDSSchema = null;

	/**
	 * Data set schema to be used when fetching/saving data.
	 * 
	 * Should only be set when different data set will be used from default one.
	 */
	protected IDSSchema dsSchema = null;
	
	protected transient WeakReference<IDSTransactionManager> transactionManager = null;
	protected IDSRequestTransactionManager requestTransactionManager = null; 
	
	protected boolean isTransactionRequestMainObject = false;
	protected boolean createNewDAORequest = true;
	protected boolean isInternalTransaction = false;

	/**
	 * Data buffer default size
	 */
	protected int dataBufferSize = 100;

	/**
	 * Data buffers
	 */
	protected IDaoRowList<T> dataBuffer = new IDaoRowList<T>();

	/**
	 * Data listeners
	 */
	protected Collection<IDataListener<T>> dataListeners = new ArrayList<IDataListener<T>>();

	/**
	 * Last retrieved row ids
	 */
	protected String lastNextFetchedRowId = null;
	protected String lastPreviousFetchedRowId = null;

	protected Collection<IStateListener> stateListeners = new ArrayList<IStateListener>();

	protected Class<T> dataTypeClass = null;

	public static enum AppendPosition {
		BEGINING, END, REPLACE
	};

	public IDSSchema getDSSchema() {
		if (dsSchema != null) {
			return dsSchema;
		}

		if (defaultDSSchema == null) {
			throw new WicketDSRuntimeException(
					"No data set schema set for data object.");
		}

		return defaultDSSchema;
	}

	public boolean isLocked() {
		return isDisabled || (this.updateState != UpdateStates.UpdateComplete);
	}

	protected boolean canMove(int rowPosition) {
		if (dataBuffer == null || this.dataBuffer.size() == 0) {
			publishCursorState(CursorStates.NoRecordAvailable);
			return false;
		}

		if (isLocked() || rowPosition < 0
				|| rowPosition >= this.dataBuffer.size()) {
			return false;
		}

		return true;
	}

	/**
	 * Create new DAO request using previous request data.
	 * 
	 * @param command
	 *            Dao request command.
	 * @param startRowId
	 *            Fetch data start row id.
	 */
	protected void createNewDAORequest(String command, String startRowId) {

		if (this.lastDAORequest != null) {
			this.lastDAORequest = this.lastDAORequest.createDAORequest(command, startRowId);
		} else {
			if (command == null || startRowId == null) {
				this.lastDAORequest = DAORequest.FetchFirst(getSchema().getDAOId());
			} else {
				this.lastDAORequest = new DAORequest<T>(getSchema().getDAOId(), command, startRowId);
			}
		}
	}

	/**
	 * Create new DAO request using previous request data.
	 * 
	 * @param command
	 *            Dao request command.
	 * @param startRowId
	 *            Fetch data start row id.
	 * @param batchSize
	 *            Batch size.
	 * @param isPrefetch
	 *            Pre fetched flag.
	 */
	protected void createNewDAORequest(String command, String startRowId, long batchSize, boolean isPrefetch) {

		createNewDAORequest(command, startRowId);

		this.lastDAORequest.setBatchSize(batchSize);
		this.lastDAORequest.setPrefetch(isPrefetch);
	}

	/**
	 * Request data from the data set.<br/>
	 * 
	 * Make a data request to the data set and process response according to
	 * <tt>appendPosition</tt> parameter.
	 * 
	 * @param command
	 *            Data request command.
	 * @param startRowId
	 *            Dao row use as fetch data start point.
	 * @param batchSize
	 *            Batch size. Used as direction. Negative values will fetch data
	 *            before start row.
	 * @param isPrefetch
	 *            Pre fetch flag.
	 * @param appendPosition
	 *            New fetched data buffer append position.
	 */
	protected void requestData(String command, String startRowId,
			long batchSize, boolean isPrefetch, AppendPosition appendPosition) {
		trace("Data request. Start row id:" + startRowId + ", batch size:"
				+ batchSize + " appens position: " + appendPosition);

		if (batchSize == 0 && this.dataBufferSize < Integer.MAX_VALUE) {
			this.dataBufferSize = Integer.MAX_VALUE;
		}

		if (this.requestTransactionManager != null) {
			if (isTransactionRequestMainObject) {
				try {
					createNewDAORequest(command, startRowId, batchSize, isPrefetch);
					createNewDAORequest = false;
					this.requestTransactionManager.dataRequest(appendPosition);
				} finally {
					createNewDAORequest = true;
				}
			}
		} else {
			
			createNewDAORequest(command, startRowId, batchSize, isPrefetch);
			
			IDSRequest lastDSRequest = new DSRequest(this.lastDAORequest);
	
			processResponse(getAppConnector().dataRequest(getDSSchema(),
					lastDSRequest), appendPosition);
			
			if (this.lastDAOResponse.hasMessageType(MessageType.Error)) {
				Message errorMessage = null;
				for (Message message : this.lastDAOResponse.getMessages()) {
					if (MessageType.Error.equals(message.getMessageType()) ) {
						errorMessage = message;
						break;
					}
				}
				throw new WicketDSRuntimeException(errorMessage);
			}
		}
	}

	/**
	 * Resize cache to meet data buffer size restriction.</br>
	 * 
	 * When buffer size exceeds <tt>bufferedDataSize</tt>, excess data is
	 * removed from the buffer. Removal policy is to clear data situated at the
	 * other end of the last appended data(i.e. clear data at start of the
	 * buffer if last data was added t the end of the buffer).
	 * <p/>
	 * 
	 * method returns data that is removed from the buffer.
	 * 
	 * @param appendPosition
	 *            Last buffer data appended position.
	 * @return Flushed data.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected List<T> flushDataBuffer(AppendPosition appendPosition)
			throws InstantiationException, IllegalAccessException {
		trace("Flush data buffer, append position:  " + appendPosition);

		if (this.dataBuffer.size() <= dataBufferSize) {
			publishOnDataBufferChanged(null, null, appendPosition);
			return null;
		}

		int excessDataSize = this.dataBuffer.size() - dataBufferSize;
		int startPosition = 0;

		switch (appendPosition) {

		case BEGINING:
		case REPLACE:
			startPosition = dataBuffer.size() - excessDataSize;
			this.hasLastRow = false;
			break;
		case END:
			this.hasFirstRow = false;
			break;

		}

		List<T> flushedData = DataBeanUtil.copyDataList(this.dataBuffer
				.subList(startPosition, startPosition + excessDataSize),
				getSchema().getIDataTypeClass());

		// remove excess data from cache
		this.dataBuffer.subList(startPosition, startPosition + excessDataSize)
				.clear();

		this.lastNextFetchedRowId = dataBuffer.get(dataBuffer.size() - 1)
				.getRowId();
		this.lastPreviousFetchedRowId = dataBuffer.get(0).getRowId();

		if (flushedData != null && !flushedData.isEmpty()) {

			// build map of removed row id's
			Map<String, T> removedRowsMap = new HashMap<String, T>();
			for (T dataBean : flushedData) {
				removedRowsMap.put(dataBean.getRowId(), dataBean);
			}

			publishOnDataBufferChanged(flushedData, removedRowsMap,
					appendPosition);
		}

		return flushedData;
	}

	/**
	 * Method fetches first data chunk and repositions cursor to first position.
	 * 
	 * Used for first access data initialization or data navigation reset.
	 */
	public void resetDataBuffer() {
		if (isLocked()) {
			return;
		}

		requestData(DAOCommands.sendRows.name(), StartRowMarker.first.name(), getSchema().getBatchSize(), false, AppendPosition.REPLACE);
		
		cursorPosition = -1;
		cursorPreviousPosition = -1;
		
		this.currentData = null;

		moveToRow(0, false);
	}

	/**
	 * Moves to <tt>rowPosition</tt> if possible and updates cursor state.
	 * 
	 * @param rowPosition
	 *            Row position in data buffer to move to.
	 * @param updatePreviousPosition
	 *            Flag to set previous position index to current
	 * @return true if can move to <tt>rowPosition</tt>
	 */
	protected boolean moveToRow(int rowPosition, boolean updatePreviousPosition) {
		trace(" Move to row : " + rowPosition);
		if (this.cursorPosition == rowPosition)
			return true;

		if (!canMove(rowPosition))
			return false;

		if (updatePreviousPosition && this.cursorPosition > 0
				&& this.cursorPosition < dataBuffer.size()) {
			this.cursorPreviousPosition = cursorPosition;
		}

		this.cursorPosition = rowPosition;

		if (this.dataBuffer.size() == 1 && rowPosition == 0 && hasLastRow()
				&& hasFirstRow()) {
			publishCursorState(CursorStates.OnlyRecordAvailable);
		} else if (rowPosition == 0 && hasFirstRow()) {
			publishCursorState(CursorStates.FirstRecord);
		} else if (rowPosition == this.dataBuffer.size() - 1 && hasLastRow()) {
			publishCursorState(CursorStates.LastRecord);
		} else {
			publishCursorState(CursorStates.NotFirstOrLast);
		}

		publishDataAvailable(this.dataBuffer.get(this.cursorPosition).getRowData());

		return true;
	}

	/**
	 * Resize cache so that it does not exceed
	 */
	protected void clearBufferCache() {

	}

	/**
	 * Process data set response.<br/>
	 * 
	 * Add data rows to data buffer, either by appending or recreate the buffer
	 * with the new data.
	 * 
	 * @param response
	 *            Data set request response.
	 * @param replace
	 *            Data buffer caching strategy.
	 */
	private void processResponse(AppendPosition replace) {

		if (replace == AppendPosition.REPLACE) {

			this.dataBuffer = new IDaoRowList<T>(this.lastDAOResponse
					.getDAORows());
			this.cursorPosition = -1;

			this.hasFirstRow = this.lastDAOResponse.hasFirstRow();
			this.hasLastRow = this.lastDAOResponse.hasLastRow();
			/*
			 * DBG
			 */
			// Iterator it = this.dataBuffer.iterator();
			// while( it.hasNext() )
			// getLog().info("Process response data object " +
			// getSchema().getDAOId() + " item " +
			// ((IDAORow)it.next()).getRowData().getRowId() );

		} else if (replace == AppendPosition.END) {

			// TODO: After create and 'next' check that new record(s) isn't
			// added twice to the data buffer
			// Maybe a tree set could be used for data buffer to avoid checking
			// below
			for (IDAORow<T> daoRow : this.lastDAOResponse.getDAORows()) {
				if (!dataBuffer.contains(daoRow)) {
					this.dataBuffer.add(daoRow);
				}
			}

			this.hasLastRow = this.lastDAOResponse.hasLastRow();

		} else if (replace == AppendPosition.BEGINING) {

			IDaoRowList<T> buffer = new IDaoRowList<T>(this.lastDAOResponse
					.getDAORows());

			// avoid duplicates
			for (IDAORow<T> daoRow : this.dataBuffer) {
				if (!buffer.contains(daoRow)) {
					buffer.add(daoRow);
				}
			}
			this.hasFirstRow = this.lastDAOResponse.hasFirstRow();
			this.dataBuffer = buffer;
		}

		if (dataBuffer != null && !dataBuffer.isEmpty()) {
			this.lastNextFetchedRowId = dataBuffer.get(dataBuffer.size() - 1)
					.getRowId();
			this.lastPreviousFetchedRowId = dataBuffer.get(0).getRowId();
		} else {
			this.lastNextFetchedRowId = null;
			this.lastPreviousFetchedRowId = null;
		}

		if (replace == AppendPosition.REPLACE) {

			// notify buffer listeners that old buffer was replaced, cached data
			// was cleared
			// build map of removed row id's
			publishOnDataBufferReplaced();

		} else {

			// check data buffer size does not exceed maximum allowed cache size
			try {
				flushDataBuffer(replace);
			} catch (InstantiationException e) {
				getLog().error("Unable to resize data buffer cache");
			} catch (IllegalAccessException e) {
				getLog().error("Unable to resize data buffer cache");
			}
		}
	}

	/**
	 * Process data set response.<br/>
	 * 
	 * Add data rows to data buffer, either by appending or recreate the buffer
	 * with the new data.
	 * 
	 * @param response
	 *            Data set request response.
	 * @param appendPosition
	 *            Data buffer caching strategy.
	 */
	private void processResponse(IDSResponse response, AppendPosition appendPosition) {
		if (response == null) {
			trace("received null response");
			return;
		}

		trace("Process response"
				+ response.getDAOResponse(getSchema().getDAOId(),
						getSchema().getIDataTypeClass()).getDAORows());

		this.lastDAOResponse = response.getDAOResponse(getSchema().getDAOId(),
				getSchema().getIDataTypeClass());

		//TODO For the future all request should go through transaction manager
		// Dataset level messages will be available in the transaction manager
		if (response.getDSMessages() != null
				&& !response.getDSMessages().isEmpty()) {

			if (this.lastDAOResponse.getMessages() != null) {
				this.lastDAOResponse.getMessages().addAll(
						response.getDSMessages());
			} else {
				this.lastDAOResponse.setMessages(response.getDSMessages());
			}
		}

		processResponse(appendPosition);
	}

	public IAppConnector getAppConnector() {
		if (this.appConnector != null)
			return this.appConnector;

		throw new IllegalStateException("Data object has no app connector set");
	}

	public void setAppConnector(IAppConnector connector) {
		this.appConnector = connector;
	}

	public UpdateStates getUpdateState() {
		return this.updateState;
	}

	public Logger getLog() {
		if (logger != null) {
			return logger;
		}

		this.logger = LoggerFactory.getLogger(getClass().getName());

		return this.logger;
	}

	public void setLog(Logger log) {
		logger = log;
	}

	/**
	 * Returns true if this instance has the first record of the set available
	 * in the buffer, false otherwise.
	 * 
	 * @return true if this instance has the first record of the set available
	 *         in the buffer, false otherwise.
	 */
	public boolean hasFirstRow() {
		return this.hasFirstRow;
	}

	/**
	 * Returns true if this instance has the last record of the set available in
	 * the buffer, false otherwise.
	 * 
	 * @return true if this instance has the last record of the set available in
	 *         the buffer, false otherwise.
	 */
	public boolean hasLastRow() {
		return this.hasLastRow;
	}
	
	public boolean hasAllData() {
		return this.hasLastRow && this.hasFirstRow;
	}

	public IDAOSchema<T> getSchema() {
		return this.schema;
	}

	protected void setSchema(IDAOSchema<T> schema) {
		this.schema = schema;
		this.dataBufferSize = Math.max(this.dataBufferSize, (int) (schema
				.getBatchSize() > 0 ? (schema.getBatchSize() * 2)
				: Integer.MAX_VALUE));
	}

	protected void publishCursorState(CursorStates newCursorState) {
		this.cursorState = newCursorState;
		Iterator<IStateListener> stateListenerIterator = this
				.getStateListeners().iterator();

		while (stateListenerIterator.hasNext()) {
			try {
				stateListenerIterator.next()
						.cursorState(this, this.cursorState);
			} catch (Exception ex) {
				getLog().warn(ex.getMessage(), ex);
			}
		}
	}

	protected void publishDataAvailable(T data) {
		this.currentData = data;
		trace(" publishing dataAvailable for " + data.getRowId());

		Iterator<IDataListener<T>> dataListenerIterator = this
				.getDataListeners().iterator();

		while (dataListenerIterator.hasNext()) {
			try {
				dataListenerIterator.next().dataAvailable(data);
			} catch (Exception ex) {
				getLog().warn(ex.getMessage(), ex);
			}
		}
	}

	public void cursorState(IStateSource source, CursorStates cursorState) {
		// RP: FIXME: Should we do something here?
	}

	public void addStateListener(IStateListener listener) {
		this.stateListeners.add(listener);
	}

	public Collection<IStateListener> getStateListeners() {
		return this.stateListeners;
	}

	public void removeStateListener(IStateListener listener) {
		if (this.stateListeners.contains(listener)) {
			this.stateListeners.remove(listener);
		} else {
			getLog()
					.warn(
							"removeStateListener was called on data object "
									+ this.getSchema().getDAOId()
									+ " but the listener is not between the existing listeners");
		}
	}

	public List<IDAOMessage> getMessages() {
		if (this.lastDAOResponse == null )
			return new ArrayList<IDAOMessage>();
		
		if (this.lastDAOResponse.getMessages() == null || 
				this.lastDAOResponse.getMessages().isEmpty())
			return new ArrayList<IDAOMessage>();
		
		return new ArrayList<IDAOMessage>(this.lastDAOResponse.getMessages());
	}

	public boolean hasMessageType(MessageType messageType) {
		if (this.lastDAOResponse == null)
			return false;
		return this.lastDAOResponse.hasMessageType(messageType);
	}

	public List<IDAOMessage> getMessages(MessageType messageType) {
		if (this.lastDAOResponse == null)
			return null;

		List<IDAOMessage> result = new ArrayList<IDAOMessage>();

		Collection<IDAOMessage> messages = getMessages();

		if (messages != null) {
			for (IDAOMessage message : getMessages()) {
				if (messageType.equals(message.getMessageType())) {
					result.add(message);
				}
			}
		}
		return result;
	}

	public List<IDAOMessage> getMessages(String columnName) {

		if (this.lastDAOResponse == null || columnName == null)
			return null;

		List<IDAOMessage> result = new ArrayList<IDAOMessage>();
		Collection<? extends IDAOMessage> messages = getMessages();
		if (messages != null) {
			for (IDAOMessage message : getMessages()) {
				if (columnName.equals(message.getColumn())) {
					result.add(message);
				}
			}
		}
		return result;
	}

	public CursorStates getCursorState() {
		return this.cursorState;
	}

	public T getData() {
		if (cursorState.equals(CursorStates.NoRecordAvailable)) {
			return null;
		}
		return this.currentData;
	}
	
	public String getResponseOptionValue(String optionName) {
		String optionValue = null;
		if (this.lastDAOResponse != null &&
				this.lastDAOResponse.getResponseOptions() != null) {
			
			if( this.lastDAOResponse.getResponseOptions().contains(new ResponseOption(optionName, null)) ) {
				Iterator<ResponseOption> optionsIterator =  this.lastDAOResponse.getResponseOptions().iterator();
				while(optionsIterator.hasNext()) {
					ResponseOption option = optionsIterator.next();
					if (optionName.equals(option.getName())) {
						optionValue = option.getValue();
						break;
					}
				}
			}
				
		}
		return optionValue;
	}
	

	public Collection<IDataListener<T>> getDataListeners() {
		return Collections.unmodifiableCollection(this.dataListeners);
	}

	public boolean removeDataListener(IDataListener<T> dataListener) {
		if (this.dataListeners.contains(dataListener)) {
			return this.dataListeners.remove(dataListener);
		}
		return false;
	}

	public void addDataListener(IDataListener<T> dataListener) {
		if (dataListener != null) {
			this.dataListeners.add(dataListener);
		}
	}

	/**
	 * Notify all buffer listeners of new buffer content and append operation.
	 * 
	 * @param appendPosition
	 *            append position.
	 */
	protected void publishOnDataBufferReplaced() {

	}

	/**
	 * Notify all buffer listeners of new buffer content and append operation.
	 * 
	 * @param appendPosition
	 *            append position.
	 */
	protected void publishOnDataBufferChanged(List<T> removedData,
			Map<String, T> removedRowsMap, AppendPosition appendPosition) {
		trace("Publish data buffer changed. Removed data:" + removedRowsMap);
	}

	protected void trace(String message) {
		getLog().trace(
				"DataObject " + getSchema().getDAOId() + " hash " + hashCode()
						+ message + " data buffer size is: "
						+ this.dataBuffer.size() + " currsor position is "
						+ this.cursorPosition);

	}

	/**
	 * Process data request response.
	 */
	@Override
	public void processDataResponse(IDAOResponse daoResponse, AppendPosition appendPosition) {
		
		if (daoResponse == null) {
			//added as result of service calls when no info is received for entity
			if (this.lastDAOResponse != null) {
				this.lastDAOResponse.getMessages().clear();
			}
			
			this.dataBuffer.clear();
			this.cursorPosition = -1;

			this.hasFirstRow = true;
			this.hasLastRow = true;
			
			this.lastNextFetchedRowId = null;
			this.lastPreviousFetchedRowId = null;
		
		} else { 
		
			this.lastDAOResponse = daoResponse;
		
			processResponse(appendPosition != null && isTransactionRequestMainObject ? appendPosition : AppendPosition.REPLACE);
		

			if (AppendPosition.REPLACE.equals(appendPosition) || !isTransactionRequestMainObject) {
				
				cursorPosition = -1;
				cursorPreviousPosition = -1;
				
				currentData = null;
				
				moveToRow(0, false);
				
			}
		}
	}

	@Override
	public IDAORequest<T> getDataRequest() {
		if (this.isTransactionRequestMainObject) {
			if (this.lastDAORequest == null || createNewDAORequest) {
				createNewDAORequest(DAOCommands.sendRows.name(), StartRowMarker.first.name(), this.schema.getBatchSize(), false );
			}
		} else {
			//when current data object is not the main transaction object then use standard request
			createNewDAORequest(DAOCommands.sendRows.name(), StartRowMarker.first.name(), this.schema.getBatchSize(), false );
		}
		
		if (this.schema.getBatchSize() == 0 && this.dataBufferSize < Integer.MAX_VALUE) {
			this.dataBufferSize = Integer.MAX_VALUE;
		}
		return this.lastDAORequest;
	}
	
	/**
	 * Get dao request main object flag.  
	 */
	public boolean getDataRequestMainObject() {
		return isTransactionRequestMainObject; 
	}
	
	public void setDataRequestMainObject(boolean dataRequestMainObject) {
		this.isTransactionRequestMainObject = dataRequestMainObject;
	}
	
	protected boolean isAutoCommit() {
		return (this.transactionManager == null || this.transactionManager.get() == null );
	}
	
	public void acceptRequestTransaction(IDSRequestTransactionManager dsRequestTransactionManager, boolean mainDataRequestObject) {
		this.isTransactionRequestMainObject = mainDataRequestObject;
		this.requestTransactionManager = dsRequestTransactionManager;
	}

}

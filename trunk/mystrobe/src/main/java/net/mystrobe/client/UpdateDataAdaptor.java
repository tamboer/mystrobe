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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.IDAORequest;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.IDaoRowList;
import net.mystrobe.client.connector.RowState;
import net.mystrobe.client.connector.DAORequest.StartRowMarker;
import net.mystrobe.client.connector.transaction.DSTransactionManager;
import net.mystrobe.client.connector.transaction.IDSTransactionManager;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.impl.DAORow;
import net.mystrobe.client.util.DataBeanUtil;

/**
 * @author TVH Group NV
 */
public abstract class UpdateDataAdaptor<T extends IDataBean> extends DataTableNavigationAdaptor<T> implements IUpdateListener<T>, IStateCallback,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8986179455911819198L;

	protected IUpdateSource<T> updateSource = null;

	/**
	 * Data buffers.
	 * 
	 * Currently each data operation is executed as it comes.
	 * 
	 * No buffering is performed for batch updates.
	 */
	protected IDaoRowList<T> bufferDeletedRows = new IDaoRowList<T>();
	protected IDaoRowList<T> bufferChangedRows = new IDaoRowList<T>();
	protected IDaoRowList<T> bufferAddedRows = new IDaoRowList<T>();

	protected boolean updateDataCommitSuccess = true;

	protected static enum SynchronizeDataType {
		ADD, UPDATE, DELETE
	};

	protected static int nextAppendingRowIdNumber = 0;

	protected synchronized String getNextAppendingRowId() {
		return "appending" + nextAppendingRowIdNumber++;
	}

	/**
	 * Create a new record and add it to the data buffer.
	 * 
	 * @param copyData
	 *            Flag whether to copy current record data to the new created
	 *            record.
	 */
	public void createData(boolean copyData) {
		if (isLocked()) {
			getLog().warn("DataObject [" + this.getSchema().getDAOId() + "] is locked unable to deleteData");
			return;
		}

		if (this.getSchema().isReadOnly())
			throw new RuntimeException("Data object is readonly can not delete data");

		if (this.dataTypeClass == null) {
			this.dataTypeClass = getSchema().getIDataTypeClass();
		}

		if (this.dataTypeClass == null) {
			throw new IllegalStateException("No IDataType class generated for: " + getDSSchema().getId() + ":" + getSchema().getDAOId());
		}

		T rowData = null;
		T beforeImageData = null;
		try {
			rowData = this.dataTypeClass.newInstance();
			beforeImageData = this.dataTypeClass.newInstance();
		} catch (InstantiationException ex) {
			getLog().error("Can not instantiate bean class " + this.dataTypeClass.getName(), ex);
		} catch (IllegalAccessException ex) {
			getLog().error("Can not instantiate bean class " + this.dataTypeClass.getName(), ex);
		}

		IDAORow<T> daoRow = new DAORow<T>();
		daoRow.setRowData(rowData);
		daoRow.setBeforeImage(beforeImageData);
		daoRow.setRowState(RowState.New);
		daoRow.setRowId(this.getNextAppendingRowId());

		if (copyData) {
			// when copy data
			daoRow.copyDataToRowData(this.currentData);
			daoRow.copyDataToBeforeImageRowData(this.currentData);
		}

		this.dataBuffer.add(daoRow);
		moveToRow(this.dataBuffer.size() - 1, true);
	}

	public void cancelCRUDOpertaion() {

		if (this.cursorPosition < 0 || this.dataBuffer.isEmpty()) {
			return;
		}

		IDAORow<T> row = this.dataBuffer.get(this.cursorPosition);

		// change update state to complete
		this.updateState = UpdateStates.UpdateComplete;

		switch (row.getRowState()) {
			case New:
				bufferAddedRows.remove(row);
				this.dataBuffer.remove(row);
				moveToAvailablePosition();
				break;
	
			case Updated:
				bufferChangedRows.remove(row);
				resetData();
				break;
		}

	}

	public void resetData() {

		IDAORow<T> currentRow = this.dataBuffer.get(this.cursorPosition);
		currentRow.copyDataToRowData(currentRow.getBeforeImage());

		publishDataAvailable(this.currentData);
	}

	public void setDataInitialValues(Map<String, Object> initialValuesMap) {
		DataBeanUtil.setBeanFieldValues(this.getData(), initialValuesMap);
	}

	public void deleteData() throws WicketDSBLException {
		deleteData(this.currentData);
	}

	public void deleteData(T dataType) throws WicketDSBLException {

		if (this.dataBuffer == null)
			throw new NoSuchElementException(" This data object contains no record with rowId " + dataType.getRowId());

		IDAORow<T> row = this.dataBuffer.getRow(dataType.getRowId());
		if (row == null) {
			throw new NoSuchElementException(" This data object contains no record with rowId " + dataType.getRowId());
		}

		// if current position is removed move to previous position if available
		// or
		// first,
		// or publish no record available
		deleteDataRow(dataType);
	}

	protected void moveToAvailablePosition() {

		if (canMove(this.cursorPreviousPosition)) {
			moveToRow(this.cursorPreviousPosition, false);
			return;

		}

		// try to move to first position
		if (this.cursorPosition == 0) {
			this.cursorPosition = -1;
		}

		if (!this.moveToRow(0, false)) {
			this.cursorState = CursorStates.NoRecordAvailable;

			// publish new cursor state
			publishCursorState(this.cursorState);

			// clear data fields
			T dataBean = null;

			try {
				dataBean = getSchema().getIDataTypeClass().newInstance();
			} catch (InstantiationException e) {
				getLog().error("Could not instantiate data bean of class:" + getSchema().getIDataTypeClass().getName());
			} catch (IllegalAccessException e) {
				getLog().error("Could not instantiate data bean of class:" + getSchema().getIDataTypeClass().getName());
			}

			publishDataAvailable(dataBean);
		}
	}

	public void deleteDataRow(IDataBean dataType) throws WicketDSBLException {
		if (isLocked())
			getLog().warn("DataObject [" + this.getSchema().getDAOId() + "] is locked unable to deleteData");

		if (this.getSchema().isReadOnly())
			throw new RuntimeException("Data object is readonly can not delete data");

		if (dataType == null || dataType.getRowId() == null)
			throw new NullPointerException("[dataType] is null or has as getRowId a null value ");

		IDAORow<T> row;
		if (this.dataBuffer == null || (row = this.dataBuffer.getRow(dataType.getRowId())) == null)
			throw new NoSuchElementException(" This data object contains no record with rowId " + dataType.getRowId());

		row.setRowState(RowState.Deleted);

		if (row.getBeforeImage() == null)
			row.setBeforeImage(row.getRowData());
		row.setRowData(null);

		if (bufferDeletedRows.getRow(dataType.getRowId()) == null) {
			bufferDeletedRows.add(row);
			row.setConsideredForUpdate(true);
		}

		if (!isAutoCommit()) {
			this.dataBuffer.remove(row);

			// check current row is deleted
			boolean currentRowRemoved = this.currentData != null && dataType.getRowId().equals(this.currentData.getRowId());

			if (currentRowRemoved) {
				moveToAvailablePosition();
			}
		} else {
			try {
				// call update data to remove row
				updateDataRequest();
			
			} catch (WicketDSBLException e) {
				//clear rows in 'to delete' buffer, to avoid sending record for deletion
				// on a consequent update data / delete data call 
				bufferDeletedRows.clear();
				throw e;
			}
		}
	}

	public void updateData() throws WicketDSBLException {
		updateData(this.currentData);
	}

	public void updateData(IDataBean dataType) throws WicketDSBLException {

		if (dataType == null)
			dataType = getData();

		if (dataType == null) {
			getLog().error(
					"DataObject " + getSchema().getDAOId() + " hash " + hashCode() + " dataType is null " + " data buffer size is: "
							+ this.dataBuffer.size() + " currsor position is " + this.cursorPosition);
			throw new IllegalStateException("dataType in updateData can not be null");
		}
		getLog().debug("Update data in dao " + dataType);

		IDAORow<T> daoRow = null;

		if (dataType.getRowId() == null) {
			daoRow = this.dataBuffer.get(this.cursorPosition);
		} else {
			daoRow = this.dataBuffer.getRow(dataType.getRowId());
		}

		if (!daoRow.isConsideredForUpdate() && !offlineMode) {
			switch (daoRow.getRowState()) {
			case New:
				bufferAddedRows.add(daoRow);
				daoRow.setConsideredForUpdate(true);
				getLog().info("updateData row is an new row, row position in the appendedRows ");
				break;

			default:
				daoRow.setRowState(RowState.Updated);
				daoRow.setConsideredForUpdate(true);
				bufferChangedRows.add(daoRow);
				getLog().info("updateData row is an updated row, row position in the changedRows " + bufferChangedRows.indexOf(daoRow));

				break;
			}
		}

		// make back end call to update data
		updateDataRequest();
	}

	/**
	 * Update data data object request.<br/>
	 * 
	 * Performs a data update request.
	 * 
	 * @param addedRows
	 *            New rows.
	 * @param changedRows
	 *            Changed rows.
	 * @param deletedRows
	 *            Removed rows.
	 * @param batchSize
	 *            Batch size.
	 * @param isPrefetch
	 *            Pre fetched flag.
	 * 
	 * @return Command dao result response.
	 */
	protected void updateDataRequest() throws WicketDSBLException {

		if (logger.isTraceEnabled()) {
			logger.trace("Update data request");
		}

		if (!this.getSchema().isAutosync())
			return;

		if (!isAutoCommit() || offlineMode) {
			return;
		}

		try {
			isInternalTransaction = true;
			IDSTransactionManager tempTransaction = new DSTransactionManager(getDSSchema(), this.appConnector);
			tempTransaction.addTransactionParticipant(this);

			if (logger.isTraceEnabled()) {
				logger.trace("Transaction manager commit");
			}

			tempTransaction.commit();
		} finally {
			isInternalTransaction = false;
		}
	}

	/**
	 * Update buffer changes with back end info.
	 * 
	 */
	protected void synchronizeData() {

		Collection<IDAORow<T>> rows = this.lastDAOResponse.getDAORows();

		if (isAutoCommit()) {

			if (!this.bufferDeletedRows.isEmpty()) {

				IDAORow<T> daoRow = bufferDeletedRows.get(0);

				this.dataBuffer.remove(daoRow);

				// check current row is deleted
				boolean currentRowRemoved = this.currentData != null && daoRow.getRowId().equals(this.currentData.getRowId());

				if (currentRowRemoved) {
					moveToAvailablePosition();
				}

			} else {

				// update current new/changed row
				if (rows != null && rows.size() == 1) {
					logger.debug("Synchronize data response rows:" + rows);
					IDAORow<T> receivedRow = rows.iterator().next();

					// daoRow.copyDataToBeforeImageRowData(daoRow.getRowData());

					// synchronize current data with BL submit result
					IDAORow<T> currentRowData = this.dataBuffer.get(this.cursorPosition);
					currentRowData.copyDataToBeforeImageRowData(receivedRow.getRowData());
					currentRowData.copyDataToRowData(receivedRow.getRowData());
					currentRowData.setRowState(receivedRow.getRowState());
					currentRowData.setConsideredForUpdate(false);

					if (currentRowData.getRowId() == null) {
						currentRowData.setRowId(receivedRow.getRowId());
						currentRowData.getRowData().setRowid(receivedRow.getRowId());
						currentRowData.getBeforeImage().setRowid(receivedRow.getRowId());
					}

					this.currentData = currentRowData.getRowData();

					publishDataAvailable(this.currentData);
				}

				publishDataAvailable(this.currentData);
			}

		} else {

			boolean publishData = false;

			if (!this.bufferChangedRows.isEmpty()) {
				// find each row in the data buffer
				Iterator<IDAORow<T>> receivedRowsIterator = rows.iterator();

				while (receivedRowsIterator.hasNext()) {

					IDAORow<T> receivedRowData = receivedRowsIterator.next();
					int rowPosition = this.dataBuffer.getRowPosition(receivedRowData.getRowId());

					if (rowPosition >= 0) {
						// copy received row data over current buffer data
						IDAORow<T> currentRowData = this.dataBuffer.get(rowPosition);

						currentRowData.copyDataToBeforeImageRowData(receivedRowData.getRowData());
						currentRowData.copyDataToRowData(receivedRowData.getRowData());
						currentRowData.setRowState(receivedRowData.getRowState());
						currentRowData.setConsideredForUpdate(false);
						receivedRowsIterator.remove();
					}
				}
				this.currentData = this.dataBuffer.get(this.cursorPosition).getRowData();
				publishData = true;
			}

			if (!this.bufferAddedRows.isEmpty()) {

				// remove all new added rows from buffer
				for (IDAORow<T> addedRow : this.bufferAddedRows) {
					this.dataBuffer.remove(addedRow);
				}

				// append new received rows
				for (IDAORow<T> receivedRowData : rows) {
					receivedRowData.copyDataToBeforeImageRowData(receivedRowData.getRowData());
					this.dataBuffer.add(receivedRowData);
				}

				this.currentData = this.dataBuffer.get(this.cursorPosition).getRowData();
				publishData = true;
			}

			// nothing to do for deleted rows
			if (publishData) {
				publishDataAvailable(this.currentData);

				// TODO: Should we consider navigators also??
			}
		}

		// clear data synchronization buffers
		bufferDeletedRows.clear();
		bufferAddedRows.clear();
		bufferChangedRows.clear();
	}

	public void updateState(IStateSource updateStateListener, UpdateStates updateState) {
		getLog().debug("Update state:" + updateState);
		this.updateState = updateState;

		if (navigationSources != null && !navigationSources.isEmpty()) {
			for (INavigationSource navigationSource : navigationSources) {
				navigationSource.updateState(this, updateState);
			}
		}
	}

	@Override
	public IDAORequest<T> getTransactionCommitData() {

		// initialize new request
		createNewDAORequest(DAOCommands.submitCommit.name(), StartRowMarker.commit.name());

		this.lastDAORequest.setSkipRow(false);

		if (bufferDeletedRows != null && !bufferDeletedRows.isEmpty()) {
			for (IDAORow<T> deletedRow : bufferDeletedRows) {
				this.lastDAORequest.addRow(deletedRow);
			}
		}

		if (this.getSchema().isSendChangesOnly()) { // we have to send
													// everything
			// not only the changes but the
			// added and changed rows will
			// be already there
			if (bufferChangedRows != null && !bufferChangedRows.isEmpty()) {
				for (IDAORow<T> changedRow : bufferChangedRows) {
					this.lastDAORequest.addRow(changedRow);
				}
			}

			if (bufferAddedRows != null && !bufferAddedRows.isEmpty()) {
				for (IDAORow<T> addedRow : bufferAddedRows) {
					this.lastDAORequest.addRow(addedRow);
				}
			}
		}

		return this.lastDAORequest;
	}

	@Override
	public String getDaoId() {
		return this.getSchema().getDAOId();
	}

	@Override
	public Class<T> getDataBeanClass() {
		return this.getSchema().getIDataTypeClass();
	}

	@Override
	public void unSuccesfullCommitCallback(IDAOResponse daoResponse) {

		this.updateDataCommitSuccess = false;

		if (daoResponse == null) {
			return;
		}

		this.lastDAOResponse = daoResponse;

		if (this.updateSource != null) {
			updateSource.onUpdateError();
		}
	}

	@Override
	public void startTransaction() {

		// clear any previous tranzaction data
		bufferDeletedRows.clear();
		bufferAddedRows.clear();
		bufferChangedRows.clear();

		if (isAutoCommit()) {
			return;
		}

		// TODO We can make a copy of current data buffer to use when rollback
	}

	@Override
	public void rollback() {

		// TODO: We could replace all buffer data with data saved in
		// startTransaction

		if (!this.bufferAddedRows.isEmpty()) {
			for (IDAORow<T> addedRow : this.bufferAddedRows) {
				this.dataBuffer.remove(addedRow);
			}
		}

		if (!this.bufferChangedRows.isEmpty()) {
			for (IDAORow<T> changedRow : this.bufferChangedRows) {
				// int position =
				// this.dataBuffer.getRowPosition(changedRow.getRowId());
				changedRow.copyDataToRowData(changedRow.getBeforeImage());
				changedRow.setConsideredForUpdate(false);
			}
		}

		// Normally we should not have any delete operations in transaction..
		if (!this.bufferDeletedRows.isEmpty()) {
			for (IDAORow<T> deletedRow : this.bufferDeletedRows) {
				// TODO: we have to know position of rows before being deleted
				// in order
				// to
				// properly restore the data buffer. Chronological changes
				// history might
				// be of help as well
				this.dataBuffer.add(deletedRow);
				deletedRow.setConsideredForUpdate(false);
			}
		}

		bufferDeletedRows.clear();
		bufferAddedRows.clear();
		bufferChangedRows.clear();
	}

	@Override
	public void succesfullCommitCallback(IDAOResponse daoResponse) {

		this.updateDataCommitSuccess = true;

		if (daoResponse == null) {
			return;
		}

		this.lastDAOResponse = daoResponse;

		synchronizeData();

		bufferDeletedRows.clear();
		bufferAddedRows.clear();
		bufferChangedRows.clear();
	}

	public boolean getUpdateDateCommitSuccess() {
		return this.updateDataCommitSuccess;
	}

	@Override
	public void accept(IDSTransactionManager transactionManager) {
		if (!isInternalTransaction) {
			// when external transaction manager set autocommit flag to false
			this.transactionManager = new WeakReference<IDSTransactionManager>(transactionManager);
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Internal transaction manager");
			}
		}
	}
	
	public void setUpdateSource(IUpdateSource<T> updateSource) {
		this.updateSource = updateSource;
	}

}

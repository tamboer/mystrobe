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
 package net.mystrobe.client.dynamic.navigation;

import java.util.List;
import java.util.Map;

import net.mystrobe.client.DataSourceAdaptor.AppendPosition;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataBufferListener;
import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.config.MyStrobeCoreSettingsProvider;
import net.mystrobe.client.navigator.IDataTableNavigatorListener;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationDirection;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationState;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;
import net.mystrobe.client.ui.UICssResourceReference;
import net.mystrobe.client.ui.config.MyStrobeWebSettingsProvider;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Data table navigation base component.</br>
 * 
 * Default implementation of simple navigation over a data object buffer.
 * 
 * Implements {@link IDataBufferListener} to reset navigation according to buffer changes.</br>
 * 
 * Implements {@link IDataTableNavigatorSource} to send data fetch request and to be 
 *  able to synchronize on data whenever data 'changes'.  
 * 
 * 
 * @author TVH Group NV
 *
 * @param <T> Data object type.
 */
public abstract class AbstractDataTableNavigationPanel<T extends IDataBean> extends Panel implements IDataTableNavigatorSource<T> {

	private static final long serialVersionUID = 2759882956599172869L;
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractDataTableNavigationPanel.class);

	/**
	 * Navigator state
	 */
	protected DataTableNavigationState navigationState = DataTableNavigationState.FirstPage;
	
	/**
	 * Current page number
	 */
	protected int currentPageNumber = 1;
	
	/**
	 * Navigator page size
	 */
	protected int pageSize = 20;
	
	/**
	 * Navigator listener
	 */
	protected IDataTableNavigatorListener<T> navigationListener;
	
	/**
	 * Last and first data row id's. 
	 */
	protected String lastDataRowId, firstDataRowId;
	
	
	protected String reloadPageStartRowId = null;

	protected boolean reloadPage = false;
	
	
	public AbstractDataTableNavigationPanel(String id) {
		super(id);
		setOutputMarkupId(true);
	}
	
	public AbstractDataTableNavigationPanel(String id, final int pageSize) {
		super(id);
		
		if (pageSize <=0 ) {
			throw new IllegalArgumentException("Page size has to be a positive value.");
		}
		
		this.pageSize = pageSize;
		setOutputMarkupId(true);
	}

	public AbstractDataTableNavigationPanel(String id, IModel<?> model, final int pageSize) {
		super(id, model);
		this.pageSize = pageSize;
		setOutputMarkupId(true);
	}

	public void setDataTableNavigationListener(IDataTableNavigatorListener<T> navigatorListener) {
		this.navigationListener = navigatorListener;
		
	}

	public void onNewDataReceived(List<T> removedData,  Map<String, T> removedRowsMap, 
			List<T> newDataBuffer, AppendPosition appendPosition, boolean hasFirstRow, boolean hasLastRow, int positionInBuffer) {
		
	}
	
	public void onDataDeleted(T removedRow, List<T> dataBuffer, boolean hasFirstRow,
			boolean hasLastRow, int removedRowBufferPosition) {
		
		reloadPage = true;
		
		if (!this.navigationListener.isDataBufferEnabled()) {
			
			//TODO remove page from navigation set ??!!
			if (removedRow.getRowId().equals(firstDataRowId) && 
					removedRow.getRowId().equals(lastDataRowId)) {
				throw new IllegalStateException("Navigator can not reposition on previous page when data cache is not enabled !!!!");
			}
			
			if (removedRow.getRowId().equals(firstDataRowId)) {
				firstDataRowId = null;
			}
			
		} else {
		
			if (!removedRow.getRowId().equals(firstDataRowId)) {
				
				int firstRowPosition = findRowPositionInList(firstDataRowId, dataBuffer);
				
				if (removedRowBufferPosition <= firstRowPosition ) {
					reloadPageStartRowId = firstDataRowId;
					
					//shift first page position
					firstRowPosition++;
					
					firstDataRowId = dataBuffer.get(firstRowPosition).getRowId();
					lastDataRowId = dataBuffer.get(Math.min(firstRowPosition + pageSize - 1, dataBuffer.size()-1 )).getRowId();
				
				} else if ((firstRowPosition + pageSize - 1 ) >= removedRowBufferPosition) {
					
					//reset last row id
					reloadPageStartRowId = firstRowPosition - 1 >= 0 ? 
							dataBuffer.get(firstRowPosition - 1).getRowId() : null;
					
					lastDataRowId = dataBuffer.get(Math.min(firstRowPosition + pageSize - 1, dataBuffer.size()-1 )).getRowId();
				
				} else {
					reloadPageStartRowId = null;
					reloadPage = false;
				}
			
			} else if (!removedRow.getRowId().equals(lastDataRowId)){
				
				int firstRowPosition = removedRowBufferPosition;
				
				firstDataRowId = dataBuffer.get(firstRowPosition).getRowId();
				lastDataRowId = dataBuffer.get(Math.min(firstRowPosition + pageSize - 1, dataBuffer.size()-1 )).getRowId();
				
				reloadPageStartRowId = ( (firstRowPosition - 1) >=0 ) ? dataBuffer.get(firstRowPosition - 1).getRowId() : null;
			
			} else {
				//last record on last page removed
				if (currentPageNumber == 1) {
					
					reloadPageStartRowId = null;
					
					firstDataRowId = null;
					lastDataRowId = null;
				} else {
					currentPageNumber--;
					
					lastDataRowId = dataBuffer.get(dataBuffer.size() - 1).getRowId();
					firstDataRowId = dataBuffer.get(dataBuffer.size() - pageSize ).getRowId();
					
					reloadPageStartRowId = (dataBuffer.size() - pageSize - 1) >= 0 ?
							dataBuffer.get( dataBuffer.size() - pageSize - 1).getRowId() : null; 
				}
			}
		}
	}

	@Override
	public void onDataReset(List<T> newDataBuffer, Map<String, T> bufferRowIdsMap,
					boolean hasFirstRow, boolean hasLastRow) {
		
		this.currentPageNumber = 1;
		this.firstDataRowId = null;
		this.lastDataRowId = null;
		
		this.navigationListener.nextPageData(this, null, pageSize, 1,  this.navigationListener.isDataBufferEnabled());
	}
	
	public void updateNavigatorState(DataTableNavigationState navigationState,
			DataTableNavigationDirection navigationDirection,
			String firstRowId, String lastRowId) {
		
		this.lastDataRowId = lastRowId;
		this.firstDataRowId = firstRowId;
		this.navigationState = navigationState;
		
		if (DataTableNavigationState.FirstPage.equals(navigationState) || 
				DataTableNavigationState.FirstAndLastPage.equals(navigationState)) {
			this.currentPageNumber = 1;
		}
	}
	
	@Override
	public void updateNavigatorDataChanged(DataTableNavigationDirection navigationDirection, String firstRowId, 
			int newPageSize, int newPageNumber) {
		
		if (this.firstDataRowId == null) {
			//on initialization fetch first data
			this.navigationListener.nextPageData(this, null, pageSize, 1, true);
			return;
		}
		
		PageNavigation pageToNavigate = this.navigationListener.findNavigatorRepositionInfo(firstRowId, this.firstDataRowId, 
				currentPageNumber, pageSize, navigationDirection, newPageSize, newPageNumber); 
		
		if (pageToNavigate.getPageNumber() < currentPageNumber ) {
			this.currentPageNumber = pageToNavigate.getPageNumber();
			this.navigationListener.previousPageData(this, pageToNavigate.getNextPageFirstRowId(), pageSize,
					this.currentPageNumber, true);
		} else if (pageToNavigate.getPageNumber() > currentPageNumber) {
			this.currentPageNumber = pageToNavigate.getPageNumber();
			this.navigationListener.nextPageData(this, pageToNavigate.getPreviousPageLastRowId(), pageSize,
					this.currentPageNumber, true);
		}
	}
	
	public int getPageNumber() {
		return this.currentPageNumber;
	}
	
	/**
	 * Return navigator page size
	 * 
	 * @return Navigator page size.
	 */
	public int getPageSize() {
		return this.pageSize;
	}
	
	protected int findRowPositionInList(String rowId, List<T> rows) {
		
		int position = 0;
		
		for (T row : rows) {
			if (rowId.equals(row.getRowId())) {
				break;
			}
			position++;
		}
		if (!(position < rows.size())) {
			throw new WicketDSRuntimeException("Can not find navigator page rowId in data buffer.");
		}
		
		return position;
	}
	
	public void goToPageWithRecord(String newRecordId) {
		currentPageNumber = 1;
		
		this.navigationState = DataTableNavigationState.FirstPage; 
		this.firstDataRowId = null;
		this.lastDataRowId = null;
		
		while(!this.navigationListener.nextPageDataForRecord(this, this.lastDataRowId, 
				this.pageSize, this.currentPageNumber, false, newRecordId) ) {
			
			if (DataTableNavigationState.LastPage.equals(this.navigationState) ||
					DataTableNavigationState.FirstAndLastPage.equals(this.navigationState)) {
				break;
			}
			
			currentPageNumber++;
		}
	}
	
	@Override
	public void reloadCurrentPage() {
		if (reloadPage) {
			
			if (this.navigationListener.isDataBufferEnabled()) {
				this.navigationListener.nextPageData(this, reloadPageStartRowId, pageSize, this.currentPageNumber, true);
			} else {
				//TODO: not supported
				if (firstDataRowId != null) {
					this.navigationListener.nextPageData(this, firstDataRowId, pageSize, this.currentPageNumber, true);
				} else {
					this.navigationListener.previousPageData(this, lastDataRowId, pageSize, this.currentPageNumber, true);
				}
			}
			
		}
		
		if (this.firstDataRowId != null) {
			this.navigationListener.moveToRow(this.firstDataRowId);
		}
	}
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	 }
	
	public boolean getUsePageSizeForBatchSize() {
		return MyStrobeWebSettingsProvider.getInstance(getApplication()).
				getUseUIComponentSizeForDataObjectBatchSize();	
	}
}

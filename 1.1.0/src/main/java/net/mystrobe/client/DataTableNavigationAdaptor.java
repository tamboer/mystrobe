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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.IDaoRowList;
import net.mystrobe.client.dynamic.navigation.PageNavigation;
import net.mystrobe.client.navigator.IDataTableNavigatorListener;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;

/**
 * @author TVH Group NV
 */
public abstract class DataTableNavigationAdaptor<T extends IDataBean> extends DataNavigationAdaptor<T> implements IDataTableNavigatorListener<T>, IDataTableDataSource<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1407528685239008041L;

	/**
	 * Data table navigation listeners 
	 */
	protected List<IDataTableNavigatorSource<T>> tableNavigationSources = new ArrayList<IDataTableNavigatorSource<T>>(1);
	
	/**
	 * Data table data listeners
	 */
	protected List<IDataTableDataListener<T>>   tableDataListeners = new ArrayList<IDataTableDataListener<T>>(1);
	
	/**
	 * Create list of data from data buffer <tt>startPos</tt> to <tt>endPos</tt>
	 * 
	 * @param startPos Start position in data buffer.
	 * @param endPos End position in data buffer.
	 * @return Data buffer sub list of IDataBean elements.
	 */
	protected List<T> buildDataList(int startPos, int endPos) {
		if (startPos >= endPos || startPos < 0) {
			return null;
		}
		
		List<T> results = new ArrayList<T>(endPos - startPos);
		for (int i = startPos ; i < Math.min(endPos, this.dataBuffer.size()) ; i++) {
			results.add(this.dataBuffer.get(i).getRowData());
		}
		
		return Collections.unmodifiableList(results);
	}
	
	/**
	 * Update navigation state 
	 * 
	 * @param startPos Start position. 
	 * @param endPos End position.
	 * @return 
	 */
	protected IDataTableNavigatorListener.DataTableNavigationState findDataNavigationState(int startPos, int endPos) {
		
		boolean firstDataPage = false;
		boolean lastDataPage = false; 
		
		if (startPos == 0 && this.hasFirstRow) {
			firstDataPage = true;
		}
		
		if ( (endPos >= (this.dataBuffer.size() - 1)) && this.hasLastRow) {
			lastDataPage = true;
		}
		
		if (!firstDataPage && !lastDataPage) {
			return IDataTableNavigatorListener.DataTableNavigationState.NotFirstOrLast;
		} else if (firstDataPage && lastDataPage) {
			return IDataTableNavigatorListener.DataTableNavigationState.FirstAndLastPage;
		} else if (firstDataPage) {
			return IDataTableNavigatorListener.DataTableNavigationState.FirstPage;
		} else {
			return IDataTableNavigatorListener.DataTableNavigationState.LastPage;
		}
	}
	
	@Override
	public boolean nextPageData(IDataTableNavigatorSource<T> navigationSource,  String rowId, int pageSize,  int pageNumber, boolean repositionRequest) throws IllegalArgumentException {
		return nextPageDataForRecord(navigationSource,rowId,pageSize,pageNumber, repositionRequest, null);
	}
	
	@Override
	public boolean nextPageDataForRecord(IDataTableNavigatorSource<T> navigationSource, String rowId, int pageSize, int pageNumber,
			boolean repositionRequest, String recordRowId)	throws IllegalArgumentException {
		
		getLog().debug("Next page data, rowId: " + rowId + ", pageSize: " + pageSize);
		
		List<T> dataList = null;
		int currentDataBufferPosition = -1;
		if (rowId != null) {
			currentDataBufferPosition = this.dataBuffer.getRowPosition(rowId);
			if (currentDataBufferPosition < 0 && this.isDataBufferEnabled) {
				throw new IllegalArgumentException("Can not find row id:" + rowId );
			}
		}
		 
		int startPos = this.isDataBufferEnabled ? currentDataBufferPosition + 1 : this.dataBuffer.size();
		int endPos = startPos + pageSize;
		
		if (this.isDataBufferEnabled && (this.canMove(endPos-1) || this.hasLastRow)) {
			dataList = buildDataList(startPos, endPos);
		} else {
			
			do {
				//request next data 
				String startRowId = this.isDataBufferEnabled ? this.lastNextFetchedRowId : rowId;
				long batchSize = this.isDataBufferEnabled ? this.getSchema().getBatchSize() : pageSize;
				requestData(DAOCommands.sendRows.name(), startRowId, batchSize, false, AppendPosition.END, true);
		        
		        startPos = this.isDataBufferEnabled ? this.dataBuffer.getRowPosition(rowId) + 1 : 0;
		        endPos = startPos + pageSize;
		    
			} while (!(this.canMove(endPos-1) || this.hasLastRow));
	        
			dataList = buildDataList(startPos, endPos);
		}
		
		//publish data 
		publishTableDataAvailable(dataList, pageSize);
		
		//update navigator
		String firstRowId = dataList != null && !dataList.isEmpty() ? dataList.get(0).getRowId() : null;
		String lastRowId = dataList != null && !dataList.isEmpty() ? dataList.get(dataList.size() - 1).getRowId() : null;
		
		if (navigationSource != null) {
			navigationSource.updateNavigatorState(findDataNavigationState(startPos, endPos - 1), DataTableNavigationDirection.Next,
				firstRowId,	lastRowId);
		}
		
		//info all other navigator(s) sources of change
		if (!repositionRequest && this.isDataBufferEnabled) {
			updateDataNavigationChanged(navigationSource, DataTableNavigationDirection.Next, firstRowId, pageSize, pageNumber);
		}
		
		if (recordRowId != null) {
			boolean recordFound = false;
			if (dataList != null && !dataList.isEmpty()) {
				for (T record : dataList) {
					if (recordRowId.equals(record.getRowId())) {
						recordFound = true;
						break;
					}
				}
			}
			return recordFound; 
		} else {
			return true;
		}
	}
	
	
	public boolean previousPageData(IDataTableNavigatorSource<T> navigationSource, String rowId,
			int pageSize, int pageNumber, boolean repositionRequest) throws IllegalArgumentException {
		
		if (getLog().isDebugEnabled()) {
			getLog().debug("Previous page data, rowId: " + rowId + ", pageSize: " + pageSize);
		}
		
		int currentDataBufferPosition = dataBuffer.getRowPosition(rowId);
		
		if (currentDataBufferPosition < 0 && this.isDataBufferEnabled) {
			throw new IllegalArgumentException("Can not find row id:" + rowId );
		}
		
		List<T> dataList = null;
		
		//start buffer position
		int startPos = currentDataBufferPosition - pageSize;
		
		if ( this.isDataBufferEnabled && ( canMove(startPos) || hasFirstRow )) {
			startPos = Math.max(0, startPos);
			dataList = buildDataList(startPos, startPos + pageSize);
		} else {
			
			do {
				
				//request previous data
				String startRowId = this.isDataBufferEnabled ? this.lastPreviousFetchedRowId : rowId;
				long batchSize = this.isDataBufferEnabled ? this.getSchema().getBatchSize() : pageSize;
				requestData(DAOCommands.sendRows.name(), startRowId, batchSize * -1, false, AppendPosition.BEGINING, true);
				
				//find current position in the data buffer as it might have changed
				currentDataBufferPosition = this.isDataBufferEnabled ? dataBuffer.getRowPosition(rowId) : dataBuffer.size();
				startPos = currentDataBufferPosition - pageSize;
		    } while (!canMove(startPos) && !hasFirstRow);
			
			startPos = Math.max(0, startPos);
			dataList = buildDataList(startPos, startPos + pageSize);
		}
		
		//publish data
		publishTableDataAvailable(dataList, pageSize);
		
		//update navigator
		String firstRowId = dataList != null && !dataList.isEmpty() ? dataList.get(0).getRowId() : null;
		String lastRowId = dataList != null && !dataList.isEmpty() ? dataList.get(dataList.size() - 1).getRowId() : null;
		
		if (navigationSource != null) {
			navigationSource.updateNavigatorState(findDataNavigationState(startPos, startPos + pageSize - 1), DataTableNavigationDirection.Previous,
				firstRowId , lastRowId);
		}
		
		//info all other navigator(s) sources of change
		if (!repositionRequest && this.isDataBufferEnabled ) {
			updateDataNavigationChanged(navigationSource, DataTableNavigationDirection.Previous, firstRowId, pageSize, pageNumber);
		}
		
		return true;
	}
	
	
	public void addDataTableNavigationSource(IDataTableNavigatorSource<T> source) {
		
		if (!this.tableNavigationSources.isEmpty() && !this.isDataBufferEnabled ){
			throw new IllegalStateException("Data source can not support multiple navigators.\n" +
					"Enable data caching before setting navigators !!!");
		}
		
		this.tableNavigationSources.add(source);
		
		if (!this.isDataBufferEnabled) {
			//set batch size as navigator page size when cache is not used
			this.schema.setBatchSize(source.getPageSize());
		}
		
		if (this.lastDAOResponse != null || (offlineMode && this.dataBuffer.size() > 0  )  ){
			source.onDataReset(this.dataBuffer.getDataList(), null, hasFirstRow, hasLastRow);
		}
	}
	
	public void removeDataTableNavigationSource(IDataTableNavigatorSource<T> source) {
		this.tableNavigationSources.remove(source);
	}
	
	/**
	 * Inform all navigators on the data change.<br/>
	 * 
	 * Method is called as navigator next/previous page request.
	 * All navigators can reposition themselves on the same data</p>. 
	 * 
	 * @param navigatorSource Navigator source that generated the request.
	 * @param navigationDirection Next/Previous
	 * @param firstRowId First row id in the <tt>navigatorSource</tt> requested page. 
	 */
	protected void updateDataNavigationChanged(IDataTableNavigatorSource<T> navigatorSource, 
			DataTableNavigationDirection navigationDirection, String firstRowId, int pageSize, int pageNumber) {
		for (IDataTableNavigatorSource<T> source : this.tableNavigationSources) {
			if (!source.equals(navigatorSource)) {
				source.updateNavigatorDataChanged(navigationDirection, firstRowId, pageSize, pageNumber);
			}
		}
	}
	
	
	/**
	 * Find reposition page when current position is not available in the data buffer. 
	 * 
	 * @param repositionRowId Reposition row identifier.
	 * @param pageSize Navigator page size.
	 * @param navigationDirection Last navigation direction.
	 * @param repositionDataBuffer Reposition buffer.
	 * @param newPageSize Repositioning navigator page size.
	 * @param newPageNumber Repositioning navigator page number.
	 * 
	 * @return Page to reposition on.
	 */
	protected PageNavigation findRespositionPage(String repositionRowId, int pageSize,
			DataTableNavigationDirection navigationDirection, IDaoRowList<T> repositionDataBuffer, int newPageSize, int newPageNumber) {
		
		PageNavigation resultPage = null;
		
		int repositionRowRelativePosition = repositionDataBuffer.getRowPosition(repositionRowId);
		int repositionRowAbsolutePosition = newPageSize * (newPageNumber - 1);
		
		int pageToPositionOn = (repositionRowAbsolutePosition / pageSize ) + 1;
		int repositionRowPositionInPage = repositionRowAbsolutePosition % pageSize; 
		
		if (DataTableNavigationDirection.Next.equals(navigationDirection)) {
			String previousPageLastRowId = null;
			if (repositionRowRelativePosition - repositionRowPositionInPage >= 0) {
				previousPageLastRowId = repositionDataBuffer.get(repositionRowRelativePosition - repositionRowPositionInPage - 1).getRowId();
			} else {
				//go to next page
				previousPageLastRowId =  repositionDataBuffer.get(repositionRowRelativePosition + (pageSize - repositionRowPositionInPage - 1)).getRowId();
				pageToPositionOn++;
			}
			resultPage = new PageNavigation(pageToPositionOn, null, previousPageLastRowId);
		} else {
			String nextPageFirstRowId = null;
			
			if ((repositionRowRelativePosition + (pageSize - repositionRowPositionInPage) ) < repositionDataBuffer.size()) {
				nextPageFirstRowId = repositionDataBuffer.get(repositionRowRelativePosition + (pageSize - repositionRowPositionInPage) ).getRowId();
			} else {
				nextPageFirstRowId = repositionDataBuffer.get(repositionRowRelativePosition - repositionRowPositionInPage).getRowId();
				pageToPositionOn--;
			}
			resultPage = new PageNavigation(pageToPositionOn, nextPageFirstRowId, null);
		}
		return resultPage;
	}
	
	/**
	 * Find reposition page when current position is available in the data buffer. 
	 * 
	 * @param currentStartPosition Navigator current row position.
	 * @param currentPageNumber Navigator current page number.
	 * @param pageSize Navigator page size.
	 * @param repositionRowPosition Reposition row position.
	 * @param navigationDirection Last navigation direction.
	 * @param repositionDataBuffer Reposition buffer.
	 * 
	 * @return  Page to reposition on.
	 */
	protected PageNavigation findRepositionPage(int currentStartPosition, int currentPageNumber,  int pageSize,
			int repositionRowPosition, DataTableNavigationDirection navigationDirection, IDaoRowList<T> repositionDataBuffer) {
		
		PageNavigation resultPage;
		
		if (repositionRowPosition < currentStartPosition) {
			int pagesToNavigate = (currentStartPosition - repositionRowPosition - 1 ) / pageSize;
			pagesToNavigate++;
			
			String nextPageFirstRowId = repositionDataBuffer.get(currentStartPosition - ((pagesToNavigate - 1 )* pageSize) ).getRowId();
			resultPage = new PageNavigation(currentPageNumber - pagesToNavigate, nextPageFirstRowId, null);
			
		} else {
			int pagesToNavigate = (repositionRowPosition - currentStartPosition) / pageSize;
			String previousPageLastRowId = repositionDataBuffer.get(pagesToNavigate * pageSize + currentStartPosition - 1).getRowId();
			resultPage = new PageNavigation(currentPageNumber + pagesToNavigate, null, previousPageLastRowId);
		}
		
		return resultPage;
	}
	
	public PageNavigation findNavigatorRepositionInfo(String repositionRowId,
			String currentStartRowId, int currentPageNumber, int pageSize,
			DataTableNavigationDirection navigationDirection, int newPageSize, int newPageNumber) throws IllegalArgumentException {
		
		PageNavigation resultPage;
		int currentStartPosition = this.dataBuffer.getRowPosition(currentStartRowId);
		if (currentStartPosition < 0) {
			return findRespositionPage(repositionRowId, pageSize, navigationDirection, this.dataBuffer, newPageSize, newPageNumber);
		}
		
		int repositionRowPosition = this.dataBuffer.getRowPosition(repositionRowId);
		if (repositionRowPosition < 0 ) {
			throw new IllegalArgumentException("Can not find row id in the data buffer.");
		}
		
		getLog().debug("Navigator reposition info, start position " + currentStartPosition + 
				", reposition:"  + repositionRowPosition + " current page number: " + currentPageNumber +
				" page size " + pageSize);
		
		if (currentStartPosition <= repositionRowPosition &&
				repositionRowPosition < (currentStartPosition + pageSize)) {
			//no reposition is required
			resultPage = new PageNavigation(currentPageNumber, null, null);
			
		} else {
			resultPage = findRepositionPage(currentStartPosition, currentPageNumber, pageSize, repositionRowPosition, 
					navigationDirection, this.dataBuffer);
		}
		
		getLog().debug("Navigator reposition result: " + resultPage);
		
		return resultPage;
	}

	protected void moveToRow(int rowBufferPosition) {
		moveToRow(rowBufferPosition, false);
	}


	/**
	 * Method will move cursor to new row position if 
	 *  new row is in the dataBuffer. Additional rows will not be
	 *  fetched to search for the row.   
	 */
	public boolean moveToRow(String rowId) {
		int bufferRowPosition = this.dataBuffer.getRowPosition(rowId);
		
		if (bufferRowPosition >= 0 ) {
			moveToRow(bufferRowPosition, true);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Resets navigator to first position on first page.
	 */
	public boolean resetDataTableNavigation(IDataTableNavigatorSource<T> navigationSource, int pageSize) {
		resetDataBuffer();
		nextPageData(navigationSource, null, pageSize, 1,  false);
		return true;
	}
	
	/**
	 * Update listeners with new data.
	 * 
	 * @param listData List of data beans to be sent to the data table listeners.
	 */
	protected void publishTableDataAvailable(List<T> listData, int navigatorSize) {
		for (IDataTableDataListener<T> dataListener : tableDataListeners) {
			dataListener.dataAvailable(listData, navigatorSize);
		}
	}

	public void addDataTableDataListener(IDataTableDataListener<T> dataListener) {
		tableDataListeners.add(dataListener);
	}
	
	public void removeDataTableDataListener(IDataTableDataListener<T> dataListener) {
		tableDataListeners.remove(dataListener);
	}
	
	 protected void repositionToNewRecord(String newRecordRowId) {
    	if (this.tableNavigationSources!= null &&
    			this.tableNavigationSources.size() >=1) {
    		this.tableNavigationSources.get(0).goToPageWithRecord(newRecordRowId);
    	}
    }
}

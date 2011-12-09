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

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataBufferListener;
import net.mystrobe.client.DataSourceAdaptor.AppendPosition;
import net.mystrobe.client.navigator.IDataTableNavigatorListener;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationDirection;
import net.mystrobe.client.navigator.IDataTableNavigatorListener.DataTableNavigationState;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;


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
	
	
	public AbstractDataTableNavigationPanel(String id, final int pageSize) {
		super(id);
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

	
	public void onDataBufferChanged(List<T> removedData,  Map<String, T> removedRowsMap, 
			List<T> newDataBuffer, AppendPosition appendPosition, boolean hasFirstRow, boolean hasLastRow) {

		//Navigators are syncronized on visible data - code below is not needed
//		switch(appendPosition) {
//			case BEGINING:
//			
//				//reposition buffer when current last row is removed from the data buffer 
//				if (removedRowsMap != null && removedRowsMap.containsKey(this.lastDataRowId)) {
//					int position = 0;
//					for (IDataBean dataBean : removedData) {
//						if (dataBean.getRowId().equals(this.lastDataRowId)) {
//							break;
//						}
//						position++;
//					}
//					
//					//compute nr. of pages to move back
//					int pagesToMove = ( (int) position/pageSize) + 1;
//					
//					//find position in the data buffer of new pages - previous page last row id to use in the get next page request  
//					int previousPageLastRowPosition = newDataBuffer.size() - 1;
//					if (position % pageSize > 0) {
//						previousPageLastRowPosition -= (pageSize - (position % pageSize));
//					}
//					
//					String previousPageLastRowId = newDataBuffer.get(previousPageLastRowPosition - pageSize).getRowId();
//					
//					//reset pages
//					this.currentPageNumber = this.currentPageNumber - pagesToMove;
//					
//					//get data
//					this.navigationListener.nextPageData(this, previousPageLastRowId, this.pageSize, true);
//				}
//				break;
//			
//			case END:
//				if (removedRowsMap != null && removedRowsMap.containsKey(this.firstDataRowId)) {
//					int position = 0;
//					for (IDataBean dataBean : removedData) {
//						if (dataBean.getRowId().equals(this.firstDataRowId)) {
//							break;
//						}
//						position++;
//					}
//					
//					int pagesToMove =((int) (removedData.size() - position - 1) / this.pageSize) + 1;
//					
//					//find position in the data buffer of new pages - next page first row id to use in the get previous page request  
//					int previousPageLastRowPosition = pageSize;
//					if ((removedData.size() - position) % this.pageSize > 0) {
//						previousPageLastRowPosition += this.pageSize - ((removedData.size() - position) % this.pageSize);
//					}
//					
//					String nextPageFirstRowId = newDataBuffer.get(previousPageLastRowPosition).getRowId();
//					
//					//reset pages
//					this.currentPageNumber = this.currentPageNumber + pagesToMove;
//					
//					//get data
//					this.navigationListener.previousPageData(this, nextPageFirstRowId, this.pageSize, true);
//				}
//				break;
//		}
	}

	public void onDataBufferReplaced(List<T> newDataBuffer, Map<String, T> bufferRowIdsMap,
					boolean hasFirstRow, boolean hasLastRow) {
		
		this.currentPageNumber = 1;
		this.firstDataRowId = null;
		this.lastDataRowId = null;
		
		this.navigationListener.nextPageData(this, null, pageSize, 1,  true);
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
			this.navigationListener.previousPageData(this, pageToNavigate.getNextPageFirstRowId(), pageSize, this.currentPageNumber, true);
		} else if (pageToNavigate.getPageNumber() > currentPageNumber) {
			this.currentPageNumber = pageToNavigate.getPageNumber();
			this.navigationListener.nextPageData(this, pageToNavigate.getPreviousPageLastRowId(), pageSize, this.currentPageNumber, true);
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
}

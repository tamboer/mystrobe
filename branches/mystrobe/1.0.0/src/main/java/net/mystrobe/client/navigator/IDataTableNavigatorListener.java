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
 package net.mystrobe.client.navigator;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataBufferSource;
import net.mystrobe.client.IDataSelectListener;
import net.mystrobe.client.LinkListener;
import net.mystrobe.client.connector.messages.IConnectorResponseMessages;
import net.mystrobe.client.dynamic.navigation.PageNavigation;

/**
 * @author TVH Group NV
 */
public interface IDataTableNavigatorListener<T extends IDataBean> extends IConnectorResponseMessages, IDataBufferSource<T>, IDataSelectListener, LinkListener {
	
	/**
	 * Data tabel navigation states.
	 */
	public enum DataTableNavigationState {
		 FirstPage, LastPage, FirstAndLastPage, NotFirstOrLast;
	}
	
	/**
	 * Data tabel navigation states.
	 */
	public enum DataTableNavigationDirection {
		 Next, Previous;
	}
	
	/**
	 * Get next page data beans.<br/>
	 * 
	 * Method retrieves next <tt>pageSize</tt> rows after of <tt>previousRowId</tt>. 
	 * 
	 * @param source Navigation source making data request.
	 * @param rowId Row from which to fetch data. 
	 * @param size Data chunk size.
	 * @param repositionRequest True when the method is called for navigator repositioning,
	 * 				and not a user next/previous request. When false other navigators are not informed on
	 * 				the navigation data changes.     
	 * 
	 * @throws IllegalArgumentException if <tt>previousRowId</tt> can not be found.
	 */
	public boolean nextPageData(IDataTableNavigatorSource<T> navigationSource, String previousRowId, int pageSize, int pageNumber, boolean repositionRequest) throws IllegalArgumentException;

	/**
	 * Get previous page data beans.<br/>
	 * 
	 * Method retrieves previous <tt>pageSize</tt> rows in front of <tt>nextRowId</tt>.
	 * 
	 * @param source Navigation source making data request.
	 * @param rowId Row from which to fetch data. 
	 * @param size Data chunk size.
	 * @param repositionRequest True when the method is called for navigator repositioning,
	 * 				and not a user next/previous request. When false other navigators are not informed on
	 * 				the navigation data changes.     
	 * 
	 * @throws IllegalArgumentException if <tt>nextRowId</tt> can not be found. 
	 */
	public boolean previousPageData(IDataTableNavigatorSource<T> navigationSource, String nextRowId, int pageSize, int pageNumber, boolean repositionRequest) throws IllegalArgumentException;
	
	/**
	 * Resets navigator to first position on first page.
	 */
	public boolean resetDataTableNavigation(IDataTableNavigatorSource<T> navigationSource, int pageSize);
	
	/**
	 * Find navigator reposition page data so that <tt>repositionRowId</tt> 
	 *  will be in the page data.  
	 * 
	 * @param repositionRowId Row to use to look for reposition page. 
	 * @param currentStartRowId Current navigator page data start row id. 
	 * @param currentPageNumber Current navigator page number.
	 * @param pageSize Navigator page size.
	 * @param navigationDirection Next/Previous.
	 * 
	 * @return Page to navigate to info.
	 */
	public PageNavigation findNavigatorRepositionInfo(String repositionRowId, String currentStartRowId,
			int currentPageNumber, int pageSize, DataTableNavigationDirection navigationDirection, int newPageSize, int newPageNumber) throws IllegalArgumentException;
	
	/**
	 * Set data table navigation source.
	 * 
	 * @param source Data table navigation source.
	 */
	public void addDataTableNavigationSource(IDataTableNavigatorSource<T> source);
	
	/**
	 * Set data table navigation source.
	 * 
	 * @param source Data table navigation source.
	 */
	public void removeDataTableNavigationSource(IDataTableNavigatorSource<T> source);
}


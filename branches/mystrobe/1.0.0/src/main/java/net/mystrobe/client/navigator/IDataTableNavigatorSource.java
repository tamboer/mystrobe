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
import net.mystrobe.client.IDataBufferListener;
import net.mystrobe.client.LinkSource;

/**
 * @author TVH Group NV
 */
public interface IDataTableNavigatorSource<T extends IDataBean> extends LinkSource, IDataBufferListener<T> {
	
	/**
	 * Call back to update navigation source with navigation direction,
	 *  state and current page data first and last row id's. <br/>
	 *  
	 * Call back is used on the navigation source after a  call to next/previous page requests.
	 * Only caller navigation source will be updated.<p/> 
	 * 
	 * @param navigationState Navigator state. First/last pages. 
	 * @param navigationDirection Can be next or previous.
	 * @param firstRowId Current page data first row id.
	 * @param lastRowId Current page data last row id.
	 */
	public void updateNavigatorState(IDataTableNavigatorListener.DataTableNavigationState navigationState,
					IDataTableNavigatorListener.DataTableNavigationDirection navigationDirection, 
					String firstRowId, String lastRowId);
	
	
	/**
	 * Call back used to inform navigation sources that a next/previous page
	 *  was requested by a different navigator. Each navigator can/has to synchronize
	 *  on the new data.     
	 * 
	 * @param navigationDirection Can be next or previous.
	 * @param firstRowId First row id on the last next/previous page navigation requests. 
	 */
	public void updateNavigatorDataChanged(IDataTableNavigatorListener.DataTableNavigationDirection navigationDirection, 
					String firstRowId, int pageSize, int pageNumber);
	

	/**
	 * Set navigation listener.
	 * 
	 * @param navigatorListener Navigation listener instance.
	 */
	public void setDataTableNavigationListener(IDataTableNavigatorListener<T> navigatorListener);
	
	/**
	 * Return current navigation page number
	 * 
	 * @return Current navigation page number.
	 */
	public int getPageNumber();
	
	/**
	 * Return current navigation page number
	 * 
	 * @return Current navigation page number.
	 */
	public int getPageSize();
	
}

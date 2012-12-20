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

import java.util.List;

/**
 * @author TVH Group NV
 */
public interface IDataTableDataListener<T extends IDataBean> extends LinkListener {
	
	/**
	 * Data available call back method.
	 * 
	 * When multiple navigators are used method will be called for each 
	 *  navigation request. Implementations should consider data if it matches
	 *  viewer data size and discarded otherwise.   
	 * 
	 * @param navigatorSize Size of the data navigator source
	 * 							that generated the call back. 
	 * @param listData List of data to be displayed.
	 */
	public void dataAvailable(List<T> listData, int navigatorSize); 
	
	/**
	 * Set data table data source.
	 * 
	 * @param source
	 */
	public void setDataTableDataSource(IDataTableDataSource<T> source);
	
	/**
	 * Get current(runtime) table row count.</br>
	 * 
	 * It will return current table view data size.
	 * It doesn't have to match the table size normally
	 *  set in the constructors.</p>
	 *  
	 * @return Table row count.
	 */
	public int getTableDataSize();
	
	/**
	 * Get current table row count.
	 * 
	 * @return Table size.
	 */
	public int getSize();
}

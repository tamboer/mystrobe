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

/**
 * @author TVH Group NV
 */
public interface IDataTableDataSource<T extends IDataBean> extends IDataSelectListener, IDataBufferSource<T>, LinkListener {
	
	/**
	 * Add table data listener to table data source. 
	 * 
	 * @param dataListener Data listener to be updated with data.
	 */
	public void addDataTableDataListener(IDataTableDataListener<T> dataListener);
	
	/**
	 * Remove data listener from table data source. 
	 * 
	 * @param dataListener Data listener to be removed.
	 */
	public void removeDataTableDataListener(IDataTableDataListener<T> dataListener);
}

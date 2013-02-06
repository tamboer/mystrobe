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
import java.util.Map;

import net.mystrobe.client.DataSourceAdaptor.AppendPosition;


/**
 * Data buffer listener interface.
 * 
 * Components that need to be notified  whenever the data buffer is updated
 *  should implement this interface and register themselves with {@link ComponentLinker} 
 *  to the corresponding {@link IDataBufferSource}. 
 *  
 * @author TVH Group NV
 */
public interface IDataBufferListener<T extends IDataBean> {
	

	/**
	 * Data buffer before changes call back method.<br/>
	 * 
	 * Method is called when ever data is removed from data buffer to make
	 *  room for new fetched data.<br/>
	 * 
	 * Implementing classes should take appropriate actions.
	 * 
	 * @param removedData removed data. 
	 * @param removedRowsMap removed row ids map data. 
	 * @param bufferData all buffer data.
	 * @param appendPosition Specify whether data was added at beginning, end or 
	 * 				completely replaced in the. data buffer.
	 * @param hasFirstRow Buffer contains first row
	 * @param hasLastRow Buffer contains last row
	 * @param deletedData Buffer changed as result of delete operation
	 */
	public void onNewDataReceived(List<T> removedData, Map<String, T> removedRowsMap, List<T> newDataBuffer, 
			AppendPosition appendPosition, boolean hasFirstRow, boolean hasLastRow);
	
	
	
	/**
	 * Data deleted call back method.<br/>
	 * 
	 * Method is called when ever data is deleted from current buffer.<br/>
	 * 
	 * Implementing classes should take appropriate actions.
	 * 
	 * @param removedRow removed data. 
	 * @param dataBuffer all buffer data.
	 * @param hasFirstRow Buffer contains first row
	 * @param hasLastRow Buffer contains last row
	 * @param deletedData Buffer changed as result of delete operation
	 */
	public void onDataDeleted(T removedRow, List<T> dataBuffer, 
			boolean hasFirstRow, boolean hasLastRow, int removedRowBufferPosition);
	
	
	/**
	 * Data buffer replaced call back method.<br/>
	 * 
	 * Method is called when data buffer is completely replaced with new data. 
	 * 
	 * Implementing classes should take appropriate actions.
	 * 
	 * @param bufferRowsMap map of buffer data row ids. 
	 * @param newDataBuffer - all buffer data.
	 * @param appendPosition Specify whether data was added at beginning, end or 
	 * 				completely replaced in the. data buffer.
	 */
	public void onDataReset(List<T> newDataBuffer, Map<String, T> bufferRowIdsMap,
			boolean hasFirstRow, boolean hasLastRow);
}

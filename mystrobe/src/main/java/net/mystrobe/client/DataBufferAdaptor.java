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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data buffer source implementation.</br>
 * 
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type.
 */

public abstract class DataBufferAdaptor<T extends IDataBean> extends FilteredDataAdaptor<T> implements IDataBufferSource<T>, Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4861021212674536062L;
	
	
	protected List<IDataBufferListener<T>> dataBufferListeners = new ArrayList<IDataBufferListener<T>>();


	public void addDataBufferListener(IDataBufferListener<T> dataBufferListener) {
		this.dataBufferListeners.add(dataBufferListener);
		
	}

	public void removeDataBufferListener(IDataBufferListener<T> dataBufferListener) {
		this.dataBufferListeners.remove(dataBufferListener);
	}
	
	/**
	 * Notify all buffer listeners of new buffer content and append operation.
	 * 
	 * @param appendPosition append position.
	 */
	@Override
	protected void publishOnDataBufferChanged(List<T> removedData,  Map<String, T> removedRowsMap, 
			AppendPosition appendPosition) {
		
		super.publishOnDataBufferChanged(removedData, removedRowsMap, appendPosition);
		
		for (IDataBufferListener<T> dataBufferListener : this.dataBufferListeners) {
			dataBufferListener.onNewDataReceived(removedData, removedRowsMap, 
					this.dataBuffer.getDataList(), appendPosition, this.hasFirstRow, this.hasLastRow);
		}
	}
	
	/**
	 * Notify all buffer listeners of new buffer content and append operation.
	 * 
	 * @param appendPosition append position.
	 */
	@Override
	protected void publishOnDataBufferReplaced() {
		
		super.publishOnDataBufferReplaced();
		
		Map<String, T> dataRowIdsMap = new HashMap<String, T>();
		for (T dataBean : dataBuffer.getDataList()) {
			dataRowIdsMap.put(dataBean.getRowId(), dataBean);
		}
		
		for (IDataBufferListener<T> dataBufferListener : this.dataBufferListeners) {
			dataBufferListener.onDataReset(this.dataBuffer.getDataList(), dataRowIdsMap,
					this.hasFirstRow, this.hasLastRow());
		}
	}
	
	protected void publishDataRowRemoved(T removedRow, int removedRowPosition) {
		
		for (IDataBufferListener<T> dataBufferListener : this.dataBufferListeners) {
			dataBufferListener.onDataDeleted(removedRow, this.dataBuffer.getDataList(), 
					this.hasFirstRow, this.hasLastRow, removedRowPosition);
		}
	}

	public List<T> getDataBuffer() {
		return this.dataBuffer.getDataList();
	}
}

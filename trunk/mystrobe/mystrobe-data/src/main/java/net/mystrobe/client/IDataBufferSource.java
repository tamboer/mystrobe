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
 * Data buffer source interface.<br/>
 * 
 * Handles data buffer changes internally and updates
 *  all listeners on changes to the data buffer.
 * 
 * @author TVH Group NV 
 */
public interface IDataBufferSource<T extends IDataBean> extends IDataSource<T> {
	
	/**
	 * Add data buffer listener.
	 * 
	 * @param dataBufferListener
	 */
	public void addDataBufferListener( IDataBufferListener<T> dataBufferListener);
	
	/**
	 * Remove data buffer listener
	 * 
	 * @param dataBufferListener.
	 */
	public void removeDataBufferListener( IDataBufferListener<T> dataBufferListener);
	
	/**
	 * Returns all records in the data buffer.
	 */
	public List<T> getDataBuffer();
	
}

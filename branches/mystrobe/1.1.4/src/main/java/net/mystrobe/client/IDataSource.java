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

import java.util.Collection;

/**
 * A data source is an entity that provides data (data store, data objects) to a
 * data listener component (usually fields, special controls that must be data
 * aware).
 * 
 * @author TVH Group NV
 */
public interface IDataSource<T extends IDataBean> extends LinkSource {

	/**
	 * Adds a data listener to this source.
	 * 
	 * Each data listener that uses data from this data source has to be first added
	 * to this source trough this method.
	 * 
	 * @param dataListener    A data listener to this source.
	 * 
	 */
	public void addDataListener(IDataListener<T> dataListener);

	/**
	 * Returns the currently selected data record, or null if none is available.
	 */
	public T getData();
	
	/**
	 * Get response option value.
	 * 
	 * @return String representing response option value
	 */
	public String getResponseOptionValue(String optionName);
	
	/**
	 * Clears current data cache and fetches chunk of data from first position.
	 */
	public void resetDataBuffer();

	/**
	 * Clears current data cache and fetches chunk of data from first position.
	 */
	public void clearDataBuffer();

	/**
	 * Returns a collections containing all the data listener to this source.
	 */
	public Collection<IDataListener<T>> getDataListeners();

	/**
	 * Removes a data listener that uses data from this data source.
	 * 
	 * @param dataListener
	 */
	public boolean  removeDataListener(IDataListener<T> dataListener);
	
	/**
	 * Check all data was fetched.
	 * 
	 * @return True if object contains all data.
	 */
	public boolean hasAllData();
	
	/**
	 * Reposition data buffer to new added record.
	 * 
	 * @param repositionOnNewAddedRecord
	 */
	public void setRepositionOnNewAddedRecord(boolean repositionOnNewAddedRecord);
	
	/**
	 * Data source uses a data buffer to cache data
	 * 
	 * @return true/false
	 */
	public boolean isDataBufferEnabled();

	/**
	 * Change data caching startegy
	 * 
	 * @param cacheData
	 */
	public void setCacheData(boolean cacheData);
	
	/**
	 * Get data object meta data information.
	 * @return Data object meta data class instance 
	 */
	public IDAOSchema<T> getSchema();
	
	/**
	 * Get data object meta data information.
	 * @return Data object meta data class instance 
	 */
	public int getBatchSize();
	
	/**
	 * Set data object batch size.
	 * Method will automatically set the freeze batch flag to true. 
	 *  
	 * @param batchSize
	 */
	public void setBatchSize( int batchSize);
	
	/**
	 * Let UI components change batch size according to their view size (default).
	 * When set to true batch size will not be updated by the UI components.
	 * 
	 * @param freezeBatchSize
	 */
	public void setLockBatchSize(boolean lockBatchSize);
	
	/**
	 * Batch size setting cleared to be overridden by subsequent setting(s)
	 */
	public void clearBatchSize();
	
	/**
	 * Get all records,  do not consider batching 
	 */
	public void fetchAllRecords();

}
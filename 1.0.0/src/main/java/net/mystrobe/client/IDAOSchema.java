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
import java.util.Collection;
import java.util.Map;


/**
 * Metadata about an data object / data store .
 *
 * @author TVH Group NV
 */
public interface IDAOSchema<T extends IDataBean>  extends Serializable {

	/**
	 * How many rows to request at a time.
	 */
	public long getBatchSize();

	/**
	 * An immutable collection containing the columns of data this data store / data
	 * object holds.
	 */
	public Collection<String> getColumnNames();

	/**
	 * Method to request additional information about an column.
	 * 
	 * @param columnName    The column name whose metadata wants to be retrieved.
	 */
	public Map<SchemaColumnProperties, String> getColumnProperties(String columnName);

	/**
	 * The id for the data object that will be used.
	 */
	public String getDAOId();

	/**
	 * When the current row or the top/bottom row  is this close to the begining/end
	 * of the batch, fetch the previous/next batch
	 */
	public int getMargin();


	/**
	 * If true, immediately commit every change to the server
	 */
	public boolean isAutosync();

	/**
	 * If set to true in the data objects initializeObject() method the first batch of
	 * data sent in the intialValues will be loaded and the data object will be
	 * started by automatically invoking fetchFirst() (and subsequently generating
	 * QueryPosition and DataAvailable events). If set to false the data object will
	 * not load it&rsquo;s initial batch of data and will not be started waiting for
	 * an external fetchX call to be started (useful for slave data objects, or data
	 * objects linked to lookup fields whose data field does not have a data object,
	 * ex: report filter forms).
	 */
	public boolean isOpenOnInit();

	/**
	 * Returns if the actual backend data object identified by the RelURL is an actual
	 * data store or data object 
	 */
	public boolean isReadOnly();

	/**
	 * Whenever the backend expects to receive all the data from the client or only
	 * the changed one.
	 */
	public boolean isSendChangesOnly();

	/**
	 * Send the current filters with every request? if not (default), then the server
	 * must keep track of the filters using the session and/or the dao-id
	 */
	public boolean isSetFilterEveryTime();


    /**
     * Returns the Java class that is responsible for representing this schema.
     * It is used by the DAOResponse to actualy instantiate and fill the proper Java data
     * type and fill it with data from the backend protocol.
     */
    public Class<T> getIDataTypeClass();


    /**
     * Returns true if this schema is dynamicaly generated, false otherwise.
     * Could be used by a caching mechanism to decide if the schema can be cached or not.
     * @return true if this schema is dynamicaly generated, false otherwise
     */
    public boolean isDynamic();
    
    /**
	 * Set request batch size.
	 */
	public void setBatchSize(long batchSize);
}
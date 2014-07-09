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
 package net.mystrobe.client.connector;

import java.util.List;
import java.util.Set;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.SortState;

/**
 * Defines an object to provide the client request to the actual backend server
 * data object implementation.
 * 
 * @author TVH Group NV
 */
public interface IDAORequest<T extends IDataBean> {


	/**
	 * Adds a filtering operation / parameter to the request for data.
	 * 
	 * @param filterParam    The filtering parameter to be added to the filtering rules
	 */
	public void addFilterParameter(IFilterParameter filterParam);

	/**
	 * Adds a new row of data to be sent to the backend that could be a
	 * new/modified/deleted row or an unmodified row if the backend declared the
	 * SendChangesEveryTime property.
	 * 
	 * @param row
	 */
	public void addRow(IDAORow<T> row);

	/**
	 * Returns how many rows to request at a time.
	 */
	public long getBatchSize();

	/**
	 * The comand to be send to the backend instructing the type of transaction
	 * required: "sendRows" for fetching records, "submitCommit" for a CRUD operation
	 * or a custom command that makes sence to the actual backend dao implementation.
	 */
	public String getCommand();

	/**
	 * The data object id that will uniquily identify in session the backend data
	 * object instance.
	 */
	public String getDAOId();

	/**
	 * Returns the Set of filter parameters that have been added to this request.
	 */
	public Set<IFilterParameter> getFilters();

	/**
	 * Returns the list of rows that have been added to this request to be sent to the
	 * backend that could be a new/modified/deleted row or an unmodified row if the
	 * backend declared the SendChangesEveryTime property.
	 */
	public List<IDAORow<T>> getRows();

	/**
	 * Returns the sort state that might have been specified for this request, or null
	 * if none was set.
	 */
	public SortState getSortState();

	/**
	 * Returns the start row it value set for this request. It can be the actual row
	 * id with wich to start or the static strings "first" or "last" to actualy start
	 * with the first or last record
	 */
	public String getStartRowId();

	/**
	 * Returns true if this request is a prefetch (nonmandadory buffering request) or
	 * false othervise
	 */
	public boolean isPrefetch();

	/**
	 * How many rows to request at a time.
	 * 
	 * @param batchSize    How many rows to request at a time.
	 */
	public void setBatchSize(long batchSize);

	/**
	 * The comand to be send to the backend instructing the type of transaction
	 * required: "sendRows" for fetching records, "submitCommit" for a CRUD operation
	 * or a custom command that makes sence to the actual backend dao implementation.
	 * 
	 * @param command     "sendRows" for fetching records, "submitCommit" for a CRUD
	 * operation or a custom command that makes sence to the actual backend dao
	 * implementation.
	 */
	public void setCommand(String command);

	/**
	 * The data object id that will uniquily identify in session the backend data
	 * object instance.
	 * 
	 * @param daoId    The string identifying the actual data object instance on the
	 * backend
	 */
	public void setDAOId(String daoId);

	/**
	 * Set if this request is a prefetch (nonmandadory buffering request) or false
	 * othervise
	 * 
	 * @param isPrefetch    true if this request is a prefetch (nonmandadory buffering
	 * request) or false othervise
	 */
	public void setPrefetch(boolean isPrefetch);

	/**
	 * Sets the sort state for this request.
	 * 
	 * @param sortState    The sort state for this request
	 */
	public void setSortState(SortState sortState);

	/**
	 * Sets the start row id of an sendRows or prefetch data request.
	 * 
	 * @param startRowId    It can be the actual row id with wich to start or the
	 * static strings "first" or "last" to actualy start with the first or last record
	 */
	public void setStartRowId(String startRowId);


    public boolean isSkipRow();

    public void setSkipRow(boolean isSkipRow);
    
    /**
     * Create a new DAO request instance using
     *  command and start row from parameters and 
     *  all other settings like sort/filters 
     *  from current request instance. 
     * 
     * @param command DAO command.
     * @param startRowId Start row identifier.
     * @return New IDAORequest implementation. 
     */
    public IDAORequest<T> createDAORequest(String command, String startRowId);

}
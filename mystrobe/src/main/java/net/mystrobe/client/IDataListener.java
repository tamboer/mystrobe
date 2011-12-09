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

import java.util.Set;

/**
 * A data listener is an entity that uses data provided by a data source  (data
 * store, data objects).  Usually fields, special controls that must be data avare.
 * 
 * @author TVH Group NV
 */
public interface IDataListener<T extends IDataBean> extends LinkListener  {
	
	/**
	 * Event published after a new row is selected (navigation or initialization) in a
	 * data store containing the new record that is selected.
	 * 
	 * @param modelInstance    The new record that is selected.
	 * 
	 */
	public void dataAvailable(T modelInstance);

	/**
	 * Retrieve data link columns, values and data compare operators.
	 * 
	 * @return Set of data link parameters.
	 */
	public Set<DataLinkParameters> getDataLinkParameters();
    
    public void setDataLinkParameters(Set<DataLinkParameters> dataLinkParametersSet);
    
    /**
     * Remove data link parameters
     */
    public void clearDataLinkParameters();

    /**
     * Adds a new data link parameter. 
     * 
     * @param dataLinkParameter data link parameter.
     */
    public void addDataLinkParameter(DataLinkParameters dataLinkParameter);
    
    /**
	 * Returns the data source this listener listens to data events. 
	 * Normally a data store or data object of some kind
	 */
	public IDataSource<T> getDataSource();

	/**
	 * Sets the data source this listener listens to data events.
	 * Normally a data store or data object of some kind
	 * 
	 * @param dataSource
	 */
	public void setDataSource(IDataSource<T> dataSource);
    
}
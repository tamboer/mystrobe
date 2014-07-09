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
import java.util.Set;

import net.mystrobe.client.connector.messages.IConnectorResponseMessages;


/**
 * A filter listener is an entity whose records can be filtered by a filter source.
 * Usually extended by an IDataObject whose records can be filtered by a filter
 * source, usually implemented by a filter view, control that can collect data
 * from fields and apply them as a set of filter parameters.
 * 
 * @author TVH Group NV
 */
public interface IFilterListener extends LinkListener, IConnectorResponseMessages {

	/**
	 * Returns the last set of filter parameters that where applied to this filter
	 * listener.
	 */
	public Set<IFilterParameter> getFilters();

	/**
	 * Returns the filter source that this entity listens to in order to receive
	 * filtration parameters.
	 */
	public IFilterSource getFilterSource();

	/**
	 * Setter for the published set of filter parameters that are to be applied to
	 * this filter listener.
	 * 
	 * @param filters    the published set of filter parameters that are to be applied
	 * to this filter listener.
	 */
	public void addFilters(Set<IFilterParameter> filters);

	/**
	 * Add new filtering parameter.
	 * 
	 * @param filterParameter Parameter to e removed
	 */
	public void addFilter(IFilterParameter filterParameter);

	/**
	 * Remove filtering parameter.
	 * 
	 * @param filters
	 */
	public void removeFilter(IFilterParameter filterParameter);

	/**
	 * Remove all filters. 
	 */
	public void clearFilters();
	
	/**
	 * Sets the filter source that this entity listens to in order to receive
	 * filtrastion parameters.
	 * 
	 * @param filterView    the filter source that this entity listens to in order to
	 * receive filtrastion parameters.
	 */
	public void setFilterSource(IFilterSource filterView);
	
	/**
	 * Method sets query name for business logic processing.
	 * 
	 * @param queryNameValue
	 */
	public void setQueryName(String queryNameValue);
	
	/**
	 * Method sets option name and value for business logic processing.
	 * 
	 * @param optionName Option name
	 * @param optionValue Option value
	 */
	public void addOption(String optionName, Serializable optionValue);
	
	/**
	 * Method remove option from set of options sent to the business logic.
	 * 
	 * @param optionName Option name
	 */
	public void removeOption(String optionName);
	
	/**
	 * Update filters for a column.<br/>
	 * 
	 * All existing column filters are removed and new filter is set.
	 * 
	 * @param filterParameter
	 */
	public void updateColumnFilter(IFilterParameter filterParameter);
	
	/**
	 * Remove all filters for a column.<br/>
	 * 
	 * @param columnName
	 */
	public void removeColumnFilters(String  columnName) ;
	
	/**
	 * Remove the query name from the BL options.  
	 */
	public void removeQueryName();
}
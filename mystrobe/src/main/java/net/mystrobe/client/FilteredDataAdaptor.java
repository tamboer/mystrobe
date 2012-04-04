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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.mystrobe.client.impl.FilterParameter;

import org.apache.wicket.util.lang.PropertyResolver;


/**
 * Adds filtering options on the data source. </br> 
 * 
 * Two options to filter a data source care available:
 * <li>Specifying filter columns, filter values and filter operator</li>
 * <li>Link data source to another one through a foreign key column</li>      
 * 
 * <p>
 * In order to link data sources one must specify the link parameters(one or more columns)
 *  and the linked data source has to be a {@link IDataListener} of the 'parent' data source.</p> 
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type
 */
public abstract class FilteredDataAdaptor<T extends IDataBean> extends SortedDataAdaptor<T> implements IFilterListener , IDataListener, Serializable {
	
	private static final long serialVersionUID = 6437517819973464901L;
	
	/**
	 * Constant value to be sent to the BL to identify a query name 
	 */
	protected static final String QUERY_NAME = "QueryName";
	
	/**
	 * Constant value to be used when sending options to the BL.
	 * 
	 * Delimiter is used to distinguish from normal column filters and options.  
	 */
	protected static final String OPTION_FILTER_DELIMITER = "_";
	
	protected Map<DataLinkParameters, Object> dataLinkParameters = new HashMap<DataLinkParameters, Object>();
    protected IFilterSource        filterSource      = null;
    protected IDataSource dataSource = null;
    protected Set<IFilterParameter> filters  = new HashSet<IFilterParameter>();
    protected Set<IFilterParameter> options  = new HashSet<IFilterParameter>();
	
	public Set<IFilterParameter> getFilters() {
        return this.filters;
    }

	public IFilterSource getFilterSource() {
        return filterSource;
    }

    private void appendFilters(Set<IFilterParameter> dataFilters) {
		getLog().debug("Set filters :" + dataFilters);
    	
    	// Check if the fk filter is somehow in the filter parameters, filtering from fk takes precendence over
		// filtering from filter view, so if the filtered column is in the filters, replace its filter parameter
		// with the one from the fk       
		
    	if( !this.dataLinkParameters.isEmpty() && dataFilters != null && !dataFilters.isEmpty() ) {
		     Iterator<IFilterParameter> filterIterator = dataFilters.iterator();
		     while( filterIterator.hasNext() ) {
		    	 IFilterParameter filterParam = filterIterator.next();

		    	 for (DataLinkParameters dataLinkParameter : this.dataLinkParameters.keySet()) {
		    		 if( filterParam.getColumn().equals(dataLinkParameter.getMappedListenerColumnName()) ) {
		    			 filterIterator.remove();
		    		 }
		         }
		     }
		}
    	
    	this.filters.addAll(dataFilters);
    	
    	setFilters();
    }
    
    public void addFilter(IFilterParameter filterParameter) {
    	Set<IFilterParameter> params = new HashSet<IFilterParameter>(1);
    	params.add(filterParameter);
    	this.appendFilters(params);
    }
    
    public void addFilters(Set<IFilterParameter> filterParameters) {
    	this.appendFilters(filterParameters);
    }
    
    /**
     * Apply existing filters.
     * 
     * Method is to be used with query names or option filters
     *  when no column filters so we do not make data request for each new option/query. 
     * 
     */
    protected void setFilters() {
    	
    	Set<IFilterParameter> dataRequestFilters = new HashSet<IFilterParameter>(this.filters.size() + this.dataLinkParameters.size() + this.options.size());
        dataRequestFilters.addAll(this.filters);
    	
    	if( !this.dataLinkParameters.isEmpty() ) {
    		for (DataLinkParameters dataLinkParameter : this.dataLinkParameters.keySet()) {
    			Object columnFilterValue =  this.dataLinkParameters.get(dataLinkParameter); 
    			
    			if (columnFilterValue != null) {
    				dataRequestFilters.add(new FilterParameter(dataLinkParameter.getMappedListenerColumnName(), FilterOperator.EQ, (Serializable) columnFilterValue));
    			}
	         }
    	}
    	
    	if (!this.options.isEmpty()) {
    		for (IFilterParameter filterParameter : this.options) {
    			dataRequestFilters.add(new FilterParameter(OPTION_FILTER_DELIMITER + filterParameter.getColumn(), FilterOperator.EQ, filterParameter.getValue()));
    		}
    	}
    	
    	if (this.lastDAORequest == null) {
    		createNewDAORequest(null, null);
    	}
    	
    	this.lastDAORequest.getFilters().clear();
		
		//set filters
		if (dataRequestFilters != null && !dataRequestFilters.isEmpty()) {
			for (IFilterParameter filterParam : dataRequestFilters) {
				this.lastDAORequest.addFilterParameter(filterParam);
			}
		}
    }
    
    public void removeColumnFilters(String  columnName) {
    	
    	for (Iterator<IFilterParameter> filterParamsIterator = this.filters.iterator(); filterParamsIterator.hasNext();) {
    		IFilterParameter filterParameter = filterParamsIterator.next();
    		
    		if (filterParameter.getColumn().equals(columnName)) {
    			filterParamsIterator.remove();
    		}
    	}
    	
    	setFilters();
    }
    
    public void updateColumnFilter(IFilterParameter filterParameter) {
    	
    	removeColumnFilters(filterParameter.getColumn());
    	
    	this.filters.add(filterParameter);
    	
    	setFilters();
    }
    
    
    @Override
	public void clearFilters() {
		this.filters.clear();
		setFilters();
	}

	@Override
	public void removeFilter(IFilterParameter filterParameter) {
		this.filters.remove(filterParameter);
		setFilters();
	}
    
    public void setFilterSource(IFilterSource filterView) {
    	filterSource = filterView;
    }
    
    public void dataAvailable(IDataBean data) {

    	boolean fkFilterValueChanged = false;
        
    	if (!this.dataLinkParameters.isEmpty()) {
        
    		for (DataLinkParameters dataLinkParameter : this.dataLinkParameters.keySet()) {
    			
    			Object newDataLinkColumnValue = PropertyResolver.getValue(dataLinkParameter.getMappedSourceColumnName(), data);
    			Object oldDataLinkColumnValue = this.dataLinkParameters.get(dataLinkParameter);
    			
    			getLog().info("DataAvailable received, mapping " + dataLinkParameter.getMappedListenerColumnName() + ":" + dataLinkParameter.mappedSourceColumnName + " =" + newDataLinkColumnValue);
    			
    			if (oldDataLinkColumnValue == null && newDataLinkColumnValue == null ) {
    				continue;
    			}
    			
    			if ((newDataLinkColumnValue != null && !newDataLinkColumnValue.equals(oldDataLinkColumnValue)) || 
    					(oldDataLinkColumnValue != null && !oldDataLinkColumnValue.equals(newDataLinkColumnValue))) {
    				
    				this.dataLinkParameters.put(dataLinkParameter, newDataLinkColumnValue);
    				fkFilterValueChanged = true;
    			}
    		}
        }
    	
    	if (fkFilterValueChanged) {
    		setFilters();
    		resetDataBuffer();
    	}
    }

	public Set<DataLinkParameters> getDataLinkParameters() {
		return this.dataLinkParameters.keySet();
	}

	public void addDataLinkParameter(DataLinkParameters dataLinkParameter) {
		
		if (dataLinkParameter == null ||
				dataLinkParameter.getMappedListenerColumnName() == null ||
				dataLinkParameter.getMappedSourceColumnName() == null) {
			throw new IllegalArgumentException("Inavlid data link parameter. Mapped column names can not be null.");
		}
		
		this.dataLinkParameters.put(dataLinkParameter, null);
		
		setFilters();
	}

	public void setDataLinkParameters(Set dataLinkParametersSet) {
		if (dataLinkParametersSet != null && !dataLinkParametersSet.isEmpty()) {
			Set<DataLinkParameters> linkParameters = (Set<DataLinkParameters>) dataLinkParametersSet; 
			for (DataLinkParameters dataLinkParameter : linkParameters ) {
				addDataLinkParameter(dataLinkParameter);
			}
		}
		
		setFilters();
	}
	
	@Override
	public void addOption(String optionName, Serializable optionValue) {
		if (optionName == null || optionValue == null) {
			throw new WicketDSRuntimeException("Option name and value can not be null."); 
		}
		
		if (QUERY_NAME.equals(optionName)) {
			throw new WicketDSRuntimeException("Invalid option name.");
		}
		
		storeOption(optionName, optionValue);
	}
	
	@Override
	public void removeOption(String optionName) {
		if (optionName == null) {
			throw new WicketDSRuntimeException("Option name can not be null."); 
		}
		
		if (QUERY_NAME.equals(optionName)) {
			throw new WicketDSRuntimeException("Invalid option name.");
		}
		deleteOption(optionName);
	}
	
	private void storeOption(String optionName, Serializable optionValue) {
		
		for (Iterator<IFilterParameter> optionsIterator = this.options.iterator(); optionsIterator.hasNext();) {
			IFilterParameter option = optionsIterator.next();
			
			//replace old value if option was already added
			if (option.getColumn().equals(optionName)) {
				optionsIterator.remove();
			}
		}
		
		IFilterParameter filterParameter = new FilterParameter(optionName, FilterOperator.EQ, optionValue);
		options.add(filterParameter);
	
		setFilters();
	}
	
	private void deleteOption(String optionName) {
		for (Iterator<IFilterParameter> optionsIterator = this.options.iterator(); optionsIterator.hasNext();) {
			IFilterParameter option = optionsIterator.next();
			if (option.getColumn().equals(optionName)) {
				optionsIterator.remove();
			}
		}
		setFilters();
	}
	
	@Override
	public void setQueryName(String queryNameValue) {
		if (queryNameValue == null) {
			throw new WicketDSRuntimeException("Query name can not be null");
		}
		storeOption(QUERY_NAME, queryNameValue);
	}
	
	@Override
	public void removeQueryName() {
		deleteOption(QUERY_NAME);
	}

	public IDataSource getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(IDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
     * Remove data link parameters
     */
    public void clearDataLinkParameters() {
    	this.dataLinkParameters.clear();
    }
}

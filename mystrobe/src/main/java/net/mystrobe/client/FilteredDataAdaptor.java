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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.filter.SearchFilter;
import net.mystrobe.client.impl.FilterParameter;
import net.mystrobe.client.util.DataBeanUtil;
import net.mystrobe.client.util.StringToJavaNativeUtil;
import net.mystrobe.client.util.StringUtil;


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
	
    public FilteredDataAdaptor(IAppConnector appConnector) {
		super(appConnector);
	}
    
    public FilteredDataAdaptor(IConfig config, String appName) {
		super(config, appName);
	}
    
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
    
    public void dataAvailable(IDataBean data, CursorStates cursorState) {

    	boolean fkFilterValueChanged = false;
        
    	if (!this.dataLinkParameters.isEmpty() &&
    			!CursorStates.NoRecordAvailable.equals(cursorState) && data != null ) {
        
    		for (DataLinkParameters dataLinkParameter : this.dataLinkParameters.keySet()) {
    			
    			Object newDataLinkColumnValue = DataBeanUtil.getFieldValue(data, dataLinkParameter.getMappedSourceColumnName(), null);
    			
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
    	
    	if (CursorStates.NoRecordAvailable.equals(cursorState)) {
    		setFilters();
    		clearDataBuffer();
    	} else {
    		
    		if (fkFilterValueChanged) {
    			setFilters();	
    		}
    		
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
			throw new WicketDSRuntimeException("Option name [" + optionName + "] and value [" + optionValue + "] can not be null."); 
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
    
    /**
     * Remove all filters that should not be applied while repositioning on records. 
     * 
     * Method used after new data is added(for now) to clear filters 
     *  before getting fresh data until record is found.  
     */
    protected void updateFiltersForRecordReposition() {
		
    	applySearchBeanFilterSettings();
    	
    	//remove remaining filters marked as to be removed on repositioning 
		for (Iterator<IFilterParameter> parametersIterator = this.filters.iterator(); parametersIterator.hasNext();) {
			IFilterParameter parameter = parametersIterator.next();
			if (parameter.isRemoveOnBufferRecordReposition()) {
				parametersIterator.remove();
			}
		}
		
		//reset filters on the data request 
		setFilters();
	}
    
    /**
     * Method provides UI search fields that can be used to 
     *  clear filters when repositioning.<br/> 
     * 
     * Annotations are used to map fields to  filters  
     * 
     * @return Search bean details.
     */
    protected Serializable getSearchBean() {
    	return null;
    }
    
    /**
     * Method used when repositioning to reset filters 
     *  according to search bean fields annotations.
     *  
     * Filters can be removed or reset to default values. 
     */
    protected void applySearchBeanFilterSettings() {
    	Serializable searchBean = getSearchBean();
    	
    	if (searchBean == null) {
    		return;
    	}
    	
		for (Field field : searchBean.getClass().getDeclaredFields()) {
			
			if (field.isAnnotationPresent(SearchFilter.class)) {
				
				SearchFilter searchFilter = field.getAnnotation(SearchFilter.class);
	
				try {
			    	String fieldName = field.getName();
			    	
			    	StringBuilder methodName =  new StringBuilder(fieldName.substring(0, 1).toUpperCase()).
							append( fieldName.substring(1));
			    	
			    	String setterMethodName = "set" + methodName.toString();
			    	String getterMethodName = "get" + methodName.toString();
			    	
			    	Method setterMethod = searchBean.getClass().getMethod(setterMethodName, field.getType() );
			    	Method getterMethod = searchBean.getClass().getMethod(getterMethodName);

			    	Serializable value = (Serializable) getterMethod.invoke(searchBean);
			    	
			    	boolean matchesOperator = false;
			    	
			    	FilterOperator filterOperator = FilterOperator.getFilterOperator(searchFilter.operator());
			    	if (filterOperator == null) {
			    		filterOperator = FilterOperator.EQ;
			    	} else if (FilterOperator.MATCHES.equals(filterOperator)) {
			    		matchesOperator = true;
			    		value =  value != null ? "*" + value.toString() + "*" : null;
			    	}
			    	
			    	IFilterParameter filterParameter = new FilterParameter(searchFilter.name(), filterOperator, value);
			    	
			    	if (searchFilter.remove()) {
						
			    		this.filters.remove(filterParameter);
						removeColumnFilters(filterParameter.getColumn());
						setterMethod.invoke(searchBean, new Object [] {null});
					
			    	} else {
						
						String defaultStringValue = searchFilter.defaultValue();
						Class<?> type = field.getType();
						Serializable defaultValue = null;
						
						//if matches field type must be string
						if (matchesOperator) {
							if (!(type.equals(String.class))) {
								throw new Exception("Matches opertor is supported only for String fields."); 
							}
						}
						
						if (StringUtil.isNullOrEmpty(defaultStringValue) ||
								defaultStringValue.equalsIgnoreCase("null")){
							defaultValue = null;
						} else if (type.equals(String.class)) {
							defaultValue = defaultStringValue;
						} else if(type.equals(Integer.class)) {
							defaultValue = Integer.valueOf(defaultStringValue);
						} else if(type.equals(BigInteger.class)) {
							defaultValue = new BigInteger(defaultStringValue);
						} else if(type.equals(BigDecimal.class)) {
							
							int pos = defaultStringValue.indexOf(".");
					        int scale = defaultStringValue.substring(pos+1).length();
							
					        BigDecimal result = new BigDecimal(Double.valueOf(defaultStringValue)).
					        	setScale(scale > StringToJavaNativeUtil.SCALE ? scale : StringToJavaNativeUtil.SCALE, BigDecimal.ROUND_HALF_UP);
							defaultValue = result;
						
						} else if(type.equals(boolean.class) || (type.equals(Boolean.class))) {
							defaultValue = Boolean.valueOf(defaultStringValue);
						} else if(type.equals(Date.class)) {
							defaultValue = new SimpleDateFormat(searchFilter.format()).parse(defaultStringValue);
						} else if(type.equals(Time.class)) {
							Date date = new SimpleDateFormat(searchFilter.format()).parse(defaultStringValue);
							defaultValue = new Timestamp(date.getTime());
						} else {
							throw new Exception("Search bean reset class type not supported: " + type.getName());
						}
						
						//set filter value
						this.filters.remove(filterParameter);
						removeColumnFilters(filterParameter.getColumn());
						
						//null values not supported as filter values
						if (defaultValue != null) {
							filterParameter.setValue(matchesOperator ? 
									"*" + defaultValue.toString() + "*" : defaultValue );
							this.filters.add(filterParameter);
						}
						
						setterMethod.invoke(searchBean, new Object [] {defaultValue});
					}
					
				} catch (Exception e) {
					getLog().error("Can not apply search bean filter settings for field: " + field.getName(), e);
				}
			}
		}
    }
}

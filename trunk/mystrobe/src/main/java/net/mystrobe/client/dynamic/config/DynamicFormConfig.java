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
 package net.mystrobe.client.dynamic.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.dynamic.config.IDynamicFormFieldConfig.Property;
import net.mystrobe.client.util.StringUtil;


/**
 * Dynamic form configuration implementation. 
 * 
 * 
 * @author TVH Group NV
 */
public class DynamicFormConfig<T extends IDataBean> implements IDynamicFormConfig<T>, Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Column properties map
	 */
	protected Map<String, Map<Property, Object>> columnPropertiesMap = new HashMap<String, Map<Property,Object>>();
	
	/**
	 * Number of form fields to display on a row
	 */
	protected int formFieldsOnRowCount = 2;
	
	/**
	 * Form fields sortable flag. 
	 */
	protected boolean sortable = false;
	
	/**
	 * Form fields sortable flag. 
	 */
	protected boolean localizeFormLabels = false;
	
	protected Map<String, Integer> visibleColumnsMap = null; 
	
	
	/**
	 * Default constructor
	 */
	public DynamicFormConfig() {
		
	}
	
	/**
	 * Creates form config using db table metadata.
	 * 
	 * @param daoSchema Db column schema.
	 */
	public DynamicFormConfig(IDAOSchema<T> daoSchema) {
		buildConfigurationFromDAOSchema(daoSchema);
	}
	
	/**
	 * Creates form config using db table metadata.
	 * 
	 * @param daoSchema Db column schema.
	 */
	public DynamicFormConfig(IDAOSchema<T> daoSchema, String [] visibleColumns, boolean sortable) {
		this.sortable = sortable;
		buildVisibleColumnMap(visibleColumns);
		buildConfigurationFromDAOSchema(daoSchema);
	}
	
	/**
	 * Creates form config using db table metadata.
	 * 
	 * @param daoSchema Db column schema.
	 */
	public DynamicFormConfig(IDAOSchema<T> daoSchema, String [] visibleColumns, boolean sortable, boolean localizableLabels) {
		this.sortable = sortable;
		this.localizeFormLabels = localizableLabels;
		buildVisibleColumnMap(visibleColumns);
		buildConfigurationFromDAOSchema(daoSchema);
	}
	
	/**
	 * Creates form config using db table metadata.
	 * 
	 * @param daoSchema Db column schema.
	 */
	public DynamicFormConfig(IDAOSchema<T> daoSchema, boolean localizeFormLabels) {
		this.localizeFormLabels = localizeFormLabels;
		buildConfigurationFromDAOSchema(daoSchema);
	}
	
	
	
	public int getNumberOfFieldsOnRow() {
		return formFieldsOnRowCount;
	}

	
	public void setNumberOfFieldsOnRow(int fieldsOnARow) {
		formFieldsOnRowCount = fieldsOnARow;
	}
	
	
	public boolean hasSortableInputFields() {
		return sortable;
	}
	
	public Map<Property, Object> getColumnProperties(String columnName) {
		return columnPropertiesMap.get(columnName);
	}

	public void setColumnProperty(Object columnId, IDynamicFormFieldConfig.Property property,
			Object value) {
		setColumnProperty(columnId.toString(), property, value);
	}
	
	public void setColumnProperty(String columnName, Property property,
			Object value) {
	
		Map<Property, Object> columnProperties = null;
		
		if (columnPropertiesMap.containsKey(columnName)) {
			 columnProperties = columnPropertiesMap.get(columnName);
		}
		
		if (columnProperties == null) {
			columnProperties = new HashMap<Property, Object>();
		}
		columnProperties.put(property, value);
	}

	public List<String> getVisibleColumnNames() {
		
		List<Map<Property,Object>> columnPropertiesList = new ArrayList<Map<Property,Object>>();
		
		for (Map<Property,Object> columnProperties : columnPropertiesMap.values()  ) {
			if (!columnProperties.containsKey(Property.Visible) ||
					(Boolean)(columnProperties.get(Property.Visible))) {
				columnPropertiesList.add(columnProperties);
			}
		}
		
		if (sortable) {
			Collections.sort(columnPropertiesList, new DynamicFormFieldConfigComparator());
		}
		
		List<String> columnNames = new ArrayList<String>(columnPropertiesList.size());
		
		for (Map<Property,Object> columnProperties : columnPropertiesList) {
			columnNames.add((String)columnProperties.get(Property.Name));
		}
		
		return columnNames;
	}
	
	protected void buildVisibleColumnMap(String [] visibleColumns) {
		this.visibleColumnsMap = new HashMap<String, Integer>(visibleColumns.length);
		
		int index = 1;
		for (String columnName : visibleColumns ) {
			this.visibleColumnsMap.put(columnName, index++);
		}
	}
	
	/**
	 * Create form field configuration from dao schema meta data.
	 * 
	 * @param daoSchema
	 */
	protected void buildConfigurationFromDAOSchema(IDAOSchema<T> daoSchema) {
		
		if (daoSchema == null) {
			return;
		}
		
		for (String columnName : daoSchema.getColumnNames()) {
			
			if ( IDataBean.ROW_ID_FIELD_NAME.equalsIgnoreCase(columnName) || 
					IDataBean.ROW_STATE_FIELD_NAME.equals(columnName)  ) {
				continue;
			}
			
			Map<SchemaColumnProperties, String> schemaColumnProperties = daoSchema.getColumnProperties(columnName);
			Map<IDynamicFormFieldConfig.Property, Object> configColumnProperties;

			if (columnPropertiesMap.containsKey(columnName)) {
				configColumnProperties = columnPropertiesMap.get(columnName);
			} else {
				configColumnProperties = new HashMap<IDynamicFormFieldConfig.Property, Object>(schemaColumnProperties.size());
			}
			
			configColumnProperties.put(IDynamicFormFieldConfig.Property.Name, columnName);
			
			for (SchemaColumnProperties schemaPropertyKey : schemaColumnProperties.keySet() ) {
				
				String schemaPropertyValue = schemaColumnProperties.get(schemaPropertyKey).trim();
				
				switch (schemaPropertyKey) {
					case Label:
						if (!this.localizeFormLabels) {
							String label = StringUtil.isNullOrEmpty(schemaPropertyValue) ? columnName : schemaPropertyValue;	
							configColumnProperties.put(IDynamicFormFieldConfig.Property.Label, label);
						} 
					break;
					
					case Required:
						boolean required = StringUtil.isNullOrEmpty(schemaPropertyValue) ||
							StringUtil.TRUE.equalsIgnoreCase(schemaPropertyValue) ||
							columnName.endsWith("id") || columnName.endsWith("Id");
						configColumnProperties.put(IDynamicFormFieldConfig.Property.Required, required);
					break;
						
					case Type:
						IDynamicFormFieldConfig.FieldType fieldType;
						if ("logical".equals(schemaPropertyValue)) {
							fieldType = IDynamicFormFieldConfig.FieldType.CheckBox;
						} else {
							fieldType = IDynamicFormFieldConfig.FieldType.TextField;
						}
						configColumnProperties.put(IDynamicFormFieldConfig.Property.Type, fieldType);
					break;
					
					case Format:
						if (!StringUtil.isNullOrEmpty(schemaPropertyValue)) {
							configColumnProperties.put(IDynamicFormFieldConfig.Property.Format, schemaPropertyValue);
						}
					break;
					
					case Sortable:
						boolean sortable = !StringUtil.isNullOrEmpty(schemaPropertyValue) && StringUtil.TRUE.equalsIgnoreCase(schemaPropertyValue);
						configColumnProperties.put(IDynamicFormFieldConfig.Property.Sortable, sortable);
					break;
					
					case ReadOnly:
						boolean visible = StringUtil.isNullOrEmpty(schemaPropertyValue) || StringUtil.FALSE.equalsIgnoreCase(schemaPropertyValue);
						configColumnProperties.put(IDynamicFormFieldConfig.Property.Visible, visible);
					break;
				}
			}
			
			if (this.localizeFormLabels) {
				StringBuilder localizedKeyBuilder = new StringBuilder(daoSchema.getDAOId());
				localizedKeyBuilder.append(".").append(columnName).append(".").append("Label");
				
				configColumnProperties.put(IDynamicFormFieldConfig.Property.Label, localizedKeyBuilder.toString());
			}
			
			if (this.visibleColumnsMap != null) {
				configColumnProperties.put(IDynamicFormFieldConfig.Property.Visible, this.visibleColumnsMap.containsKey(columnName) );
				
				if (this.sortable) {
					configColumnProperties.put(IDynamicFormFieldConfig.Property.SortValue, this.visibleColumnsMap.get(columnName) );
				}
			}
			
			columnPropertiesMap.put(columnName, configColumnProperties);
		}
	}

	public boolean getLocalizableFormLabels() {
		return localizeFormLabels;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	
}

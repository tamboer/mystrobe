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
 package net.mystrobe.client.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.SchemaColumnProperties;
import net.mystrobe.client.config.CoreConfigUtil;
import net.mystrobe.client.config.MyStrobeCoreSettingsProvider;
import net.mystrobe.client.connector.quarixbackend.Globals;
import net.mystrobe.client.connector.quarixbackend.XMLUtil;


/**
 * Default IDAOSchema implementation
 * 
 * @author TVH Group NV
 */
public class DAOSchema<T extends IDataBean> implements IDAOSchema<T> {

	protected String daoId;
	protected Class<T> iDataTypeClass = null;
	protected long batchSize = 50;
	protected int margin = 5;
	protected boolean isAutosync = true;
	protected boolean isOpenOnInit = false;
	protected boolean isReadOnly = false;
	protected boolean isDynamic = false;
	protected boolean isSendChangesOnly = true;
	protected boolean isSetFilterEveryTime = true;

	protected Map<String, Map<SchemaColumnProperties, String>> properties = null;

	public DAOSchema() {
		
 	}
	
	protected void assignValues() {
		String generatedAppName = CoreConfigUtil.getGeneratedAppNameForClass(this.getClass());
		this.batchSize =  MyStrobeCoreSettingsProvider.getInstance().getBatchSize(generatedAppName);
	}
	
	public long getBatchSize() {
		return this.batchSize;
	}

	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}

	public Collection<String> getColumnNames() {
		if (this.properties == null)
			return null;
		return this.properties.keySet();
	}

	public Map<SchemaColumnProperties, String> getColumnProperties(
			String columnName) {
		if (this.properties == null || !this.properties.containsKey(columnName))
			return null;
		return this.properties.get(columnName);
	}

	public void setPropertiesMap(
			Map<String, Map<SchemaColumnProperties, String>> properties) {
		this.properties = properties;
	}

	public String getDAOId() {
		return this.daoId;
	}

	public void setDAOId(String daoId) {
		this.daoId = daoId;
	}

	public int getMargin() {
		return this.margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public boolean isAutosync() {
		return this.isAutosync;
	}

	public void setAutosync(boolean isAutoSync) {
		this.isAutosync = isAutoSync;
	}

	public boolean isOpenOnInit() {
		return this.isOpenOnInit;
	}

	public void setOpenOnInit(boolean isOpenOnInit) {
		this.isOpenOnInit = isOpenOnInit;
	}

	public boolean isReadOnly() {
		return this.isReadOnly;
	}

	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public boolean isSendChangesOnly() {
		return this.isSendChangesOnly;
	}

	public void setSendChangesOnly(boolean isSendChangesOnly) {
		this.isSendChangesOnly = isSendChangesOnly;
	}

	public boolean isSetFilterEveryTime() {
		return this.isSetFilterEveryTime;
	}

	public void setFilterEveryTime(boolean isSetFilterEveryTime) {
		this.isSetFilterEveryTime = isSetFilterEveryTime;
	}

	public Class<T> getIDataTypeClass() {
		return this.iDataTypeClass;
	}

	public void setIDataTypeClass(Class<T> iDataTypeClass) {
		this.iDataTypeClass = iDataTypeClass;
	}

	public boolean isDynamic() {
		return this.isDynamic;
	}

	public void setIsDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}
	
	/**
	 * Read  schema properties from xml<br/>.
	 * 
	 * Used at generation time to read settings from response.  
	 * 
	 * @param properties BL settings  
	 * @param table BL table info 
	 */
	public void initDAOSchemaSettingsFromXML(final Element properties, Element table) {
		
		final String id = table.getAttribute(Globals.ATTRIBUTE_ID);
		this.daoId = id;
		
		Object obj = XMLUtil.getProperty(properties,
				Globals.PROP_BATCHSIZE, Long.class);
		if (obj != null)
			this.batchSize = (Long) obj;

		obj = XMLUtil.getProperty(properties, Globals.PROP_MARGIN,
				Integer.class);
		if (obj != null)
			this.margin = (Integer) obj;

		obj = XMLUtil.getProperty(properties, Globals.PROP_AUTOSYNC,
				Boolean.class);
		if (obj != null)
			this.isAutosync = (Boolean) obj;

		obj = XMLUtil.getProperty(properties, Globals.PROP_OPENONINIT,
				Boolean.class);
		if (obj != null)
			this.isOpenOnInit= (Boolean) obj;

		obj = XMLUtil.getProperty(properties, Globals.PROP_READONLY,
				Boolean.class);
		if (obj != null)
			this.isReadOnly = (Boolean) obj;

		obj = XMLUtil.getProperty(properties, Globals.PROP_SENDCHANGESONLY,
				Boolean.class);
		if (obj != null)
			this.isSendChangesOnly = (Boolean) obj;

		obj = XMLUtil.getProperty(properties,
				Globals.PROP_SENDFILTEREVERYTIME, Boolean.class);
		if (obj != null)
			this.isSetFilterEveryTime = (Boolean) obj;
		
		obj = XMLUtil.getProperty(properties, Globals.PROP_DYNAMIC,
				Boolean.class);
		if (obj != null)
			this.isDynamic = (Boolean) obj;

		String expression = "child::"
				+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_COLUMNNAMES)
				+ "/child::"
				+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_COL);

		List<Element> cols = XMLUtil.xpathQuery(expression, table);
		String columnName = null;
		String columnExpression = null;
		Map<SchemaColumnProperties, String> columnProperties;
		LinkedHashMap<String, Map<SchemaColumnProperties, String>> daoProperties = new LinkedHashMap<String, Map<SchemaColumnProperties, String>>();

		for (Element col : cols) {
			columnName = col.getAttribute(Globals.ATTRIBUTE_NAME);

			if (columnName != null && !columnName.isEmpty()) {
				columnProperties = new HashMap<SchemaColumnProperties, String>();

				columnExpression = "child::"
						+ XMLUtil
								.getLowerCaseExpr(Globals.ELEMENT_COLUMNNAMES)
						+ "/child::"
						+ XMLUtil.getLowerCaseExpr(Globals.ELEMENT_COL)
						+ "[@" + Globals.ATTRIBUTE_NAME + "='" + columnName
						+ "']";

				List<Element> columnProps = XMLUtil.xpathQuery(
						columnExpression, table);
				if (!columnProps.isEmpty()) {
					Element columnProp = columnProps.get(0);

					//set column properties
					columnProperties
							.put(SchemaColumnProperties.DefaultValue,
									columnProp
											.getAttribute(Globals.ATTRIBUTE_DEFAULTVALUE));
					columnProperties
							.put(SchemaColumnProperties.Format, columnProp
									.getAttribute(Globals.ATTRIBUTE_FORMAT));
					columnProperties.put(SchemaColumnProperties.Label,
							columnProp
									.getAttribute(Globals.ATTRIBUTE_LABEL));
					columnProperties
							.put(SchemaColumnProperties.ReadOnly,
									columnProp
											.getAttribute(Globals.ATTRIBUTE_READONLY));
					columnProperties
							.put(SchemaColumnProperties.Required,
									columnProp
											.getAttribute(Globals.ATTRIBUTE_REQUIRED));
					columnProperties
							.put(SchemaColumnProperties.Sortable,
									columnProp
											.getAttribute(Globals.ATTRIBUTE_SORTABLE));
					columnProperties
							.put(SchemaColumnProperties.Type, columnProp
									.getAttribute(Globals.ATTRIBUTE_TYPE));
					columnProperties
							.put(SchemaColumnProperties.ViewAs, columnProp
									.getAttribute(Globals.ATTRIBUTE_VIEWAS));
					columnProperties
							.put(SchemaColumnProperties.Tooltip,
									columnProp
											.getAttribute(Globals.ATTRIBUTE_TOOLTIP));
				}

				daoProperties.put(columnName, columnProperties);
			}
		}
		setPropertiesMap(daoProperties);
	}
}

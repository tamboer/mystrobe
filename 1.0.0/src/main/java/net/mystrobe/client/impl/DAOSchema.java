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
import java.util.Map;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.SchemaColumnProperties;


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
}

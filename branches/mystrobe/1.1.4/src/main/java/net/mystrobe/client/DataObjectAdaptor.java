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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

import net.mystrobe.client.connector.IAppConnector;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.impl.DAOSchema;


//~--- non-JDK imports --------------------------------------------------------


/**
 * The DataObjectAdaptor is a default implementation for an IDataObject
 * 
 * @author TVH Group NV
 */
public class DataObjectAdaptor<T extends IDataBean> extends UpdateDataAdaptor<T> implements IDataObject<T>, Serializable {

	private static final long serialVersionUID = 2999847020307247019L;

	/**
	 * Constructs a regular data object instance connected to the provided app connector
	 * @param connector The app connector to which this data object will be connected
	 */
	@Deprecated
	public DataObjectAdaptor(IAppConnector connector) {
		super(connector);
        if (connector != null) /*needed for OfflineDataObject*/
            initializeSchemasFromGenericType(true);
	}
	
	public DataObjectAdaptor(IConfig config, String appName) {
		super(config, appName);
        if (config != null && appName != null) /*needed for OfflineDataObject*/
            initializeSchemasFromGenericType(true);
	}
	
	public DataObjectAdaptor(IConfig config, String appName, int batchSize) {
		super(config, appName);
        
		if (config != null && appName != null) /*needed for OfflineDataObject*/
            initializeSchemasFromGenericType(false);
        
		this.batchSize = batchSize;
		this.lockBatchSize = true;
	}
	
	@Deprecated
	public DataObjectAdaptor(IAppConnector connector, IDSSchema dsSchema, IDAOSchema<T> daoSchema) {
		super(connector);
		this.dsSchema = dsSchema;
		this.schema = daoSchema;
		
		this.batchSize = (int) this.schema.getBatchSize();
	}
	
	public DataObjectAdaptor(IConfig config, String appName, IDSSchema dsSchema, IDAOSchema<T> daoSchema) {
		super(config, appName);
		this.dsSchema = dsSchema;
		this.schema = daoSchema;
		
		this.batchSize = (int) this.schema.getBatchSize();
	}
	
	public DataObjectAdaptor(IConfig config, String appName, IDSSchema dsSchema, IDAOSchema<T> daoSchema, int batchSize) {
		super(config, appName);
		this.dsSchema = dsSchema;
		this.schema = daoSchema;
		
		this.batchSize = batchSize;
		this.lockBatchSize = true;
	}
	
	private void initializeSchemasFromGenericType(boolean initBatchSize) {
		try {
			 
			ParameterizedType superclass = (ParameterizedType)
	  	        getClass().getGenericSuperclass();
	
			Class<? extends IDataBean> beanClass = (Class<? extends IDataBean>) superclass.getActualTypeArguments()[0];
	 
			Class<? extends IDSSchema> dsSchemaClass = (Class<IDSSchema>) beanClass.getMethod("getDSSchemaClass").invoke(null, new Object [] {});
			Class<? extends DAOSchema<T>> daoSchemaClass = (Class<? extends DAOSchema<T>>) beanClass.getMethod("getDAOSchemaClass").invoke(null, new Object [] {});
		
			this.dsSchema = dsSchemaClass.newInstance();
			this.schema = daoSchemaClass.newInstance();
			
			if (initBatchSize) {
				this.batchSize = (int) this.schema.getBatchSize();
			}
			
			
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException | ClassCastException e) {
			
			logger.error("Can not instantiate schema and ds schema for data object.\n" +
					"Must specify data bean generic when using this constructor.", e);
			
			throw new WicketDSRuntimeException(e);
		}
	}
}

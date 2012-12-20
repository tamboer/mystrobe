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

import net.mystrobe.client.connector.IAppConnector;


//~--- non-JDK imports --------------------------------------------------------


/**
 * The DataObjectAdaptor is a default implementation for an IDataObject
 * 
 * @author TVH Group NV
 */
public abstract class DataObjectAdaptor<T extends IDataBean> extends UpdateDataAdaptor<T> implements IDataObject<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2999847020307247019L;


	/**
	 * Standard constructor
	 */
	public DataObjectAdaptor() {
		super();
		assignValues();
	}
	
	/**
	 * DSSchema Constructor
	 */
	public DataObjectAdaptor(IDSSchema dsSchema) {
		super();
		assignValues();
		this.dsSchema = dsSchema;
	}
	
	/**
	 * Constructs a regular data object instance connected to the provided app connector
	 * @param connector The app connector to which this data object will be connected
	 */
	public DataObjectAdaptor(IAppConnector connector) {
		super();
		assignValues();
		setAppConnector(connector);
	}
	
	/**
	 * Constructs a regular data object instance connected to the provided app connector
	 * @param connector The app connector to which this data object will be connected
	 * 
	 */
	public DataObjectAdaptor(IAppConnector connector, IDSSchema dsSchema) {
		super();
		assignValues();
		setAppConnector(connector);
		this.dsSchema = dsSchema;
	}
	
	protected abstract void assignValues(); 
}

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


import java.io.Serializable;
import java.util.Enumeration;

import org.slf4j.Logger;


/**
 * Basic implementation for an immutable configuration element.
 * 
 * @author TVH Group NV
 */
public interface IConfig extends Serializable {

	String APP_SERVER_URL = "APP_SERVER_URL"; 
	String CONNECTOR = "CONNECTOR"; 
	String USER = "USER"; 
	String PASSWORD = "PASSWORD"; 
	String DATABEAN_PACKAGES = "DATABEAN_PACKAGES";
	String APP_DATABEAN_PACKAGES = "APP_DATABEAN_PACKAGES";
	

	/**
	 * Returns a list of the config keys participating in this config instance.
	 */
	public Enumeration<?> entries();

	/**
	 * Retrieve the requested config element value.
	 * 
	 * @param propertyName    The name of the config element whose values is needed.
	 */
	public String getValue(String propertyName);

	
	public Logger getLogger();
}
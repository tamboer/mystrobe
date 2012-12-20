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

import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An default IConfig implementation
 * 
 * @author TVH Group NV
 */
public class Config implements IConfig {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(Config.class);

	private Properties properties;
	
	
	public Config() {
		properties = new Properties();
		
		//TODO this values need to be loaded from property file 
                // properties.setProperty(APP_SERVER_URL, "AppServerDC://localhost:3511");
                //properties.setProperty(APP_SERVER_URL, "AppServerDC://c3-acc.yonder.local:30007/c3-xp");
                //properties.setProperty(APP_SERVER_URL, "AppServerDC://192.168.1.25:3511");
		
		String appServerURL = System.getProperty(APP_SERVER_URL);
		
		properties.setProperty(APP_SERVER_URL, appServerURL != null ? appServerURL : "AppServer://niko:5162/TVH_DEV_PRODSEARCH");
		properties.setProperty(CONNECTOR, "net.mystrobe.client.connector.quarixbackend.dispatcher.QuarixOODispatcher");
		properties.setProperty(USER, "");
		properties.setProperty(PASSWORD, "");
	}

	/**
	 * Returns a list of the config keys participating in this config instance.
	 */
	public Enumeration<?> entries() {
		return properties.propertyNames();
	}

	/**
	 * Retrieve the requested config element value.
	 * 
	 * @param propertyName
	 *            The name of the config element whose values is needed.
	 */
	public String getValue(String propertyName) {
		return properties.getProperty(propertyName);
	}


    public void setValue(String propertyName, String propertyValue) {
        this.properties.setProperty(propertyName, propertyValue);
    }

        
	public Logger getLogger() {
		return log;
	}

}
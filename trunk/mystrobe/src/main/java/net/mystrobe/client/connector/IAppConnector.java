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


import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataSet;

/**
 * The interface describing the connection to the actual application hosted on a
 * supposedly SAAS server.
 * 
 * @author TVH Group NV
 */
public interface IAppConnector {


	/**
	 * Makes a data request with the request details encoded in the request parameter
	 * to the backend application server and returns the response containing data and
	 * / or messages resulted.
	 * 
	 * @param request    The request details for the backend application server.
	 */
	public IDSResponse dataRequest(IDSSchema dsSchema, IDSRequest request);
	
	
	/**
	 * Makes a data request with the request details encoded in the request parameter
	 * to the backend application server and returns the response containing data and
	 * / or messages resulted.
	 * 
	 * @param request    The request details for the backend application server.
	 */
	public IDSResponse dataRequest(String datasetURN, IDSRequest request);

	/**
	 * Returns a detailed schema of the data set requested.
	 * 
	 * @param dataSetURN    The dataset path / class name to be requested, ex: server.
	 * clients
	 */
	public IDSSchema getSchema(String dataSetURN);

	/**
	 * Create a new instance of the requested data set.
	 * 
	 * @param dataSetURN    The dataset path / class name to be requested, ex: server.
	 * clients
	 */
	public IDataSet instantiateDataSet(String dataSetURN);


    /**
     * Returns the application server localization properties
     * @return an instance of the application server localization properties
     */
    public LocalizationProperties getLocalizationProperties();
    
    /**
     * Connector application name.
     * 
     * @return Application name.
     */
    public String getAppName() ;
    
    /**
     * Connector configuration.
     * 
     * @return Connector configuration.
     */
    public IConfig getConfig() ;

}
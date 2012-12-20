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

/**
 * Interface for a data set server connection. It is expected to be implemented by
 * the actual connector which is backend dependent. The connections parameters
 * required to the actual connection are expected to be passed in the constructor
 * trough a key / value set of properties.
 * 
 * @author TVH Group NV
 */
public interface IServerConnector {


	/**
	 * Once a server connection established the actual application connector the
	 * server exposes can be requested using a simple string as the key, string
	 * representing the application name as hosted by the backend server.
	 * 
	 * @param appName    The Application name as hosted by the backend server.
	 */
	public IAppConnector getAppConnector(String appName);

}
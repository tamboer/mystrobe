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

import net.mystrobe.client.connector.quarixbackend.AppServerImpl;
import net.mystrobe.client.connector.quarixbackend.api.IAppServer;


/**
 * An server connector implementation that knows how to exchange data with
 * applications hosted on a Quarix for Progress server.
 * 
 * @author TVH Group NV
 */
public class QuarixServerConnector implements IServerConnector {

	private IConfig config;

	/**
	 * The constructor for the server connection.
	 * 
	 * @param config
	 *            The connections parameters required to the actual connection
	 *            are expected to be passed in the constructor trough a key /
	 *            value set of properties represented by the IConfig interface
	 */
	public QuarixServerConnector(IConfig config) {
		this.config = config;
	}

	@Override
	public void finalize() throws Throwable {

	}

	/**
	 * Once a server connection established the actual application connector the
	 * server exposes can be requested using a simple string as the key, string
	 * representing the application name as hosted by the backend server.
	 * 
	 * @param appName
	 *            The Application name as hosted by the backend server.
	 */
	public IAppConnector getAppConnector(String appName) {
		IAppServer appServer = new AppServerImpl();
		appServer.setLog(config.getLogger());
		appServer.setUrl(config.getValue(IConfig.APP_SERVER_URL));
		appServer.setConnectorClassName(config.getValue(IConfig.CONNECTOR));
		appServer.setUserName(config.getValue(IConfig.USER));
		appServer.setPassword("", config.getValue(IConfig.PASSWORD));

		return new QuarixAppConnector(appServer, appName, config);
	}

}
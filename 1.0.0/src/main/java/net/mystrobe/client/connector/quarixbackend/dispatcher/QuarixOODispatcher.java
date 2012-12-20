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
 package net.mystrobe.client.connector.quarixbackend.dispatcher;

import java.sql.ResultSet;

import net.mystrobe.client.connector.quarixbackend.api.IAppServer;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcher;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherRequestParameters;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherResponseParameters;
import net.quarix.connector.MainController;
import net.quarix.connector.QuarixProgressOOConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.open4gl.Memptr;
import com.progress.open4gl.MemptrHolder;
import com.progress.open4gl.ResultSetHolder;
import com.progress.open4gl.RunTimeProperties;

/**
 * @author TVH Group NV
 */
public class QuarixOODispatcher implements IDispatcher {

public static final int REQ_TYPE_POS = 1;
	public static final int REQ_NAME_POS = 2;
	public static final int REQ_VALUE_POS = 3;

	public static final int RES_TYPE_POS = 1;
	public static final int RES_NAME_POS = 2;
	public static final int RES_VALUE_POS = 3;


	protected IAppServer appServer = null;
	protected Logger log = LoggerFactory.getLogger(QuarixOODispatcher.class);


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcher#initialize(net.quarix.api.AppServer)
	 */
	public boolean initialize(IAppServer appServer) {
		this.appServer = appServer;
		//RunTimeProperties.setWaitIfBusy();
		//if( appServer.getSessionModel() != -1 ) {
		    //RunTimeProperties.setSessionModel(appServer.getSessionModel());
		//}
                      RunTimeProperties.setSessionModel(0);
		return true;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcher#isInitialized()
	 */
	public boolean isInitialized() {
		return this.appServer != null;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcher#process(net.quarix.api.IDispatcherRequestParameters)
	 */
	public IDispatcherResponseParameters process(IDispatcherRequestParameters requestParameters) {

		DispatcherResponseParameters responseParameters = new DispatcherResponseParameters();
		QuarixProgressOOConnector connector = null;
		MainController controller = null;
		responseParameters.setLog(this.getLog());

		try {
			ResultSetHolder resultSetHolder = new ResultSetHolder();
			MemptrHolder responseMemptrHolder = new MemptrHolder();
			QuarixOOInputParameter progressRequestParameters = new QuarixOOInputParameter(requestParameters);

			connector = createConnector();
			if( connector == null ) throw new IllegalStateException("Unable to create connector");

			controller = connector.createAO_MainController();
			if( controller == null ) throw new IllegalStateException("Unable to create controller");

			controller.processRequest(  progressRequestParameters
					, requestParameters.getMemptrValue().length == 0 ? null :  new Memptr(requestParameters.getMemptrValue())
					, resultSetHolder
					, responseMemptrHolder);

			ResultSet response = resultSetHolder.getResultSetValue();
			String name, value;
			int type;
			while( response.next() ) {
				type  = response.getInt(RES_TYPE_POS);
				name = response.getString(RES_NAME_POS);
				value = response.getString(RES_VALUE_POS);
				responseParameters.addParameter(type, name , value );
			}

			if( !responseMemptrHolder.isNull() ) responseParameters.setMemptrValue(responseMemptrHolder.getMemptrValue().getBytes());
			if( resultSetHolder != null) resultSetHolder.getResultSetValue().close();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			try{  if ( controller != null) controller._release(); } catch (Exception e){ e.printStackTrace(); }
            try{  if ( connector != null) connector._release(); } catch (Exception e){ e.printStackTrace(); }

		}
		return responseParameters;
	}


	/**
	 * @return
	 */
    protected QuarixProgressOOConnector createConnector() throws Exception {
        if (getLog().isTraceEnabled()) {
            getLog().trace( "Connecting to app server " + appServer.getUrl()
                            + " with username " + appServer.getUserName()
                            + " reusing connection "
                            + appServer.getReuseConnection()
                            + " using connector " + this.getClass().getName());
         }

        return new QuarixProgressOOConnector( appServer.getUrl(), appServer.getUserName(), appServer.getPassword(), appServer.getInfo());
    }


	/* (non-Javadoc)
	 * @see net.quarix.api.LogEnabled#getLog()
	 */
	public Logger getLog() {
		return log;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.LogEnabled#setLog(org.apache.commons.logging.Log)
	 */
	public void setLog(Logger log) {
		this.log = log;
	}
}

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
 package net.mystrobe.client.connector.quarixbackend;


import net.mystrobe.client.connector.quarixbackend.api.IAppServer;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author TVH Group NV
 * @version $Revision: 1.7 $ $Date: 2009/01/13 06:12:17 $
 */
public class AppServerImpl implements IAppServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5976207426484962788L;
	protected String url;
	protected String info;
	protected String userName;
	protected String password;
	protected boolean reuseConnection = false;
	protected int sessionModel = -1;
	protected transient Logger log = LoggerFactory.getLogger(AppServerImpl.class);
	protected String   connectorClassName = "net.quarix.QuarixOODispatcher";
	protected transient IDispatcher dispatcher = null;


	/**
	 *
	 */
	public AppServerImpl() {
		super();
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.Initializable#initialize()
	 */
	public boolean initialize() {
		setConnectorClassName(this.connectorClassName);
		return true;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.AppServer#getPassword()
	 */
	public String getPassword() {
		return password;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.AppServer#getUserName()
	 */
	public String getUserName() {
		return userName;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.AppServer#setPassword(java.lang.String, java.lang.String)
	 */
	public void setPassword(String type, String pass) {
		this.password = pass;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.AppServer#setUserName(java.lang.String)
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.LogEnabled#getLog()
	 */
	public Logger getLog() {
		if( log != null ) return log;
		return log = LoggerFactory.getLogger(AppServerImpl.class);
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.LogEnabled#setLog(org.apache.commons.logging.Log)
	 */
	public void setLog(Logger log) {
		this.log = log;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.AppServer#getInfo()
	 */
	public String getInfo() {
		return info;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.AppServer#getUrl()
	 */
	public String getUrl() {
		return url;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.AppServer#setInfo(java.lang.String)
	 */
	public void setInfo(String info) {
		this.info = info;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.AppServer#setUrl(java.lang.String)
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.AppServer#getReuseConnection()
	 */
	public String getReuseConnection() {
		return "" + reuseConnection;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.AppServer#setReuseConnection(java.lang.String)
	 */
	public void setReuseConnection(String reuseConnection) {
		if( reuseConnection.equalsIgnoreCase("true") ) {
			this.reuseConnection = true;
		} else {
			this.reuseConnection = false;
		}
	}


	/**
     * @param reuseConnection the reuseConnection to set
     */
    public void setReuseConnection(boolean reuseConnection) {
        this.reuseConnection = reuseConnection;
    }



    /**
     * @return the sessionModel
     */
    public int getSessionModel() {
        return sessionModel;
    }


    /**
     * @param sessionModel the sessionModel to set
     */
    public void setSessionModel(int sessionModel) {
        this.sessionModel = sessionModel;
    }


    /**
     * @param sessionModel the sessionModel to set
     */
    public void setSessionModel(String sessionModel) {

        try {
            this.sessionModel = Integer.valueOf(sessionModel);
        } catch (Throwable t) {
            getLog().error(t.getMessage());
            this.sessionModel = 1;
        }
    }

    /* (non-Javadoc)
	 * @see net.quarix.api.AppServer#getDispatcher()
	 */
	public IDispatcher getDispatcher() {
		if( this.dispatcher != null ) {
			if( !this.dispatcher.isInitialized() ) this.dispatcher.initialize(this);
		} else {
			initialize();
			if( this.dispatcher == null ) {
				getLog().error("No dispatcher instance available"); 
				throw new RuntimeException("No dispatcher instance available");
			}
		}

		if( !this.dispatcher.isInitialized() ) {
			getLog().error("Unable to initialize dispatcher");
			throw new RuntimeException("Unable to initialize dispatcher");
		}
		return dispatcher;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.AppServer#isReusingConnection()
	 */
	public boolean isReusingConnection() {
		return reuseConnection;
	}


	/* (non-Javadoc)
	 * @see com.nethrom.projapi.api.AppServer#setConnectorClassName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public void setConnectorClassName(String connectorClassName) {
		this.connectorClassName = connectorClassName;

		if( getLog().isTraceEnabled()) {
			getLog().trace("Using connector " + connectorClassName);
		}

		try {
			Class connectorClass = Class.forName(this.connectorClassName);
			if( IDispatcher.class.isAssignableFrom(connectorClass) ) {
				this.dispatcher = (IDispatcher) connectorClass.newInstance();
				if( this.dispatcher != null )  {
					this.dispatcher.setLog(this.getLog());
					this.dispatcher.initialize(this);
				}
			} else {
				getLog().error("The connector instance speciffied in the config file: " + this.connectorClassName + " is not a valid connector class (IDispatcher)");
				throw new RuntimeException("The connector instance speciffied in the config file: " + this.connectorClassName + " is not a valid connector class (IDispatcher)");
			}
		} catch (ClassNotFoundException cnex) {
			getLog().error( "Unable to find connector class: " + this.connectorClassName, cnex);
                        throw new RuntimeException("Unable to find connector class: " + this.connectorClassName, cnex);
		} catch (Exception e) {
			getLog().error( "Unable to instantiate connector instance " + this.connectorClassName, e);
                        throw new RuntimeException("Unable to instantiate connector instance " + this.connectorClassName, e);
		}
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.AppServer#getConnectorClassName()
	 */
	public String getConnectorClassName() {
		return this.connectorClassName;
	}

}

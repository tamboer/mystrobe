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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.mystrobe.client.connector.quarixbackend.api.IDispatcherResponseParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class DispatcherResponseParameters implements IDispatcherResponseParameters {

	protected byte[] memptrValue = new byte[0];
	protected HashMap<String, String> brokerParametersHash = new HashMap<String, String>();
	protected HashMap<String, String> headerParametersHash = new HashMap<String, String>();
	protected Logger log = LoggerFactory.getLogger(DispatcherResponseParameters.class);

	
	/**
	 * @param paramType
	 * @param paramName
	 * @param paramValue
	 */
	public void addParameter(int paramType, String paramName, String paramValue) {
		switch (paramType) {
		case RESP_TYPE_BROKER: addBrokerParameter(paramName, paramValue); break;
		case RESP_TYPE_HEADER: addHeaderParameter(paramName, paramValue); break;
		default: break;
		}
	}
	

	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#addBrokerParameter(java.lang.String, java.lang.String)
	 */
	public void addBrokerParameter(String paramName, String paramValue) {
		this.brokerParametersHash.put(paramName, paramValue);
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#addHeaderParameter(java.lang.String, java.lang.String)
	 */
	public void addHeaderParameter(String paramName, String paramValue) {
		this.headerParametersHash.put(paramName, paramValue);		
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getBrokerParameter(java.lang.String)
	 */
	public String getBrokerParameter(String paramName) {
		return this.brokerParametersHash.get(paramName);
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getBrokerParameterNames()
	 */
	public Set<String> getBrokerParameterNames() {
		return this.brokerParametersHash.keySet();
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getBrokerParameterMap()
	 */
	public Map<String, String> getBrokerParameterMap() {
		return brokerParametersHash;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getHeaderParameter(java.lang.String)
	 */
	public String getHeaderParameter(String paramName) {
		return this.headerParametersHash.get(paramName);
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getHeaderParameterNames()
	 */
	public Set<String> getHeaderParameterNames() {
		return this.headerParametersHash.keySet();
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getMemptrValue()
	 */
	public byte[] getMemptrValue() {
		return this.memptrValue;
	}

	
	/**
	 * @param memptrValue
	 */
	public void setMemptrValue(byte[] memptrValue) {
		if( memptrValue == null ) memptrValue = new byte[0];
		this.memptrValue = memptrValue;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#hasBrokerParameterName(java.lang.String)
	 */
	public boolean hasBrokerParameterName(String paramName) {
		return this.brokerParametersHash.containsKey(paramName);
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#hasHeaderParameterName(java.lang.String)
	 */
	public boolean hasHeaderParameterName(String paramName) {
		return this.headerParametersHash.containsKey(paramName);
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


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherResponseParameters#getDebugString()
	 */
	public String getDebugString() {
		StringBuffer response = new StringBuffer();
		String paramName = null;
		Iterator<String> nameIterator = null;
		
		response.append("\n MEMPTR VALUE\n");
		paramName = new String(getMemptrValue());
		response.append(paramName.length() > 50 ? paramName.substring(0, 50) : paramName);
		if( paramName.length() > 70 ) {
			response.append("[...]");
			response.append(paramName.substring(paramName.length() -20));
		}		
		
		response.append("\n BROKER PARAMETERS");
		nameIterator = this.brokerParametersHash.keySet().iterator();
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=" + this.brokerParametersHash.get(paramName));
		}

		response.append("\n HEADER PARAMETERS");
		nameIterator = this.headerParametersHash.keySet().iterator();
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=" + this.headerParametersHash.get(paramName));
		}


		return response.toString();	
	}
}

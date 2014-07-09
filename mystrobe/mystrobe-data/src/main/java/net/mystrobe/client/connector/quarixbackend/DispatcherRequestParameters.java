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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.connector.quarixbackend.api.DispatcherParameter;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherRequestParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author TVH Group NV
 */
public class DispatcherRequestParameters implements IDispatcherRequestParameters {

	protected byte[] memptrValue = new byte[0];
	protected HashMap<String, String> cgiParametersHash = new HashMap<String, String>();
	protected HashMap<String, String> headerParametersHash = new HashMap<String, String>();
	protected HashMap<String, String> sessionParametersHash = new HashMap<String, String>();
	protected HashMap<String, String[]> inputParametersHash = new HashMap<String, String[]>();
	protected ArrayList<DispatcherParameter> allList = null;
	protected boolean allListIsDirty = true;
	protected Logger log = LoggerFactory.getLogger(DispatcherRequestParameters.class);
	
	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#addCGIParameter(java.lang.String, java.lang.String)
	 */
	public void addCGIParameter(String paramName, String paramValue) {
		this.cgiParametersHash.put(paramName, paramValue);
		this.allListIsDirty = true;
	}
	

	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#addHeaderParameter(java.lang.String, java.lang.String)
	 */
	public void addHeaderParameter(String paramName, String paramValue) {
		this.headerParametersHash.put(paramName, paramValue);
		this.allListIsDirty = true;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#setInputParameter(java.lang.String, java.lang.String[])
	 */
	public void setInputParameter(String paramName, String[] paramValues) {
		this.inputParametersHash.put(paramName, paramValues);
		this.allListIsDirty = true;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#addInputParameter(java.lang.String, java.lang.String)
	 */
	public void addInputParameter(String paramName, String paramValue) {
		if( this.inputParametersHash.containsKey(paramName)) {
			List<String> params = Arrays.asList(this.inputParametersHash.get(paramName));
			params.add(paramValue);
			this.inputParametersHash.put(paramName, (String[])params.toArray());
		} else {
			this.inputParametersHash.put(paramName, new String[]{paramValue});
		}		
		this.allListIsDirty = true;
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#addSessionParameter(java.lang.String, java.lang.String)
	 */
	public void addSessionParameter(String paramName, String paramValue) {
		this.sessionParametersHash.put(paramName, paramValue);
		this.allListIsDirty = true;
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getAll()
	 */
	public List<DispatcherParameter> getAll() {
		if( this.allListIsDirty ) {
			this.allList = new ArrayList<DispatcherParameter>();
			
			Iterator<String> iterator = this.headerParametersHash.keySet().iterator();
			String paramName = null;
			while( iterator.hasNext() ) {
				paramName = iterator.next();
				this.allList.add(new DispatcherParameter(IDispatcherRequestParameters.REQ_TYPE_HEADERS, paramName, this.headerParametersHash.get(paramName) ));
			}

			iterator = this.cgiParametersHash.keySet().iterator();
			while( iterator.hasNext() ) {
				paramName = iterator.next();
				this.allList.add(new DispatcherParameter(IDispatcherRequestParameters.REQ_TYPE_CGI, paramName, this.cgiParametersHash.get(paramName) ));
			}

			iterator = this.sessionParametersHash.keySet().iterator();
			while( iterator.hasNext() ) {
				paramName = iterator.next();
				this.allList.add(new DispatcherParameter(IDispatcherRequestParameters.REQ_TYPE_SESSION, paramName, this.sessionParametersHash.get(paramName) ));
			}

			iterator = this.inputParametersHash.keySet().iterator();
			String[] params = null;
			int idx, idxEnd;
			while( iterator.hasNext() ) {
				paramName = iterator.next();
				params = this.inputParametersHash.get(paramName);
				idxEnd = params.length;
				for( idx=0; idx < idxEnd; idx++)  this.allList.add(new DispatcherParameter(IDispatcherRequestParameters.REQ_TYPE_INPUT, paramName, params[idx] ));
			}
			
		}		
		return this.allList;
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getCGIParameter(java.lang.String)
	 */
	public String getCGIParameter(String paramName) {
		return cgiParametersHash.get(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getCGIParametersName()
	 */
	public Set<String> getCGIParametersName() {
		return cgiParametersHash.keySet();
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getHeaderParameter(java.lang.String)
	 */
	public String getHeaderParameter(String paramName) {
		return this.headerParametersHash.get(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getHeaderParametersName()
	 */
	public Set<String> getHeaderParametersName() {
		return headerParametersHash.keySet();
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getFirstInputParameter(java.lang.String)
	 */
	public String getFirstInputParameter(String paramName) {
		return this.inputParametersHash.get(paramName)[0];
	}
	

	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getInputParameters(java.lang.String)
	 */
	public String[] getInputParameters(String paramName) {
		return this.inputParametersHash.get(paramName);
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getInputParametersName()
	 */
	public Set<String> getInputParametersName() {
		return inputParametersHash.keySet();
	}
	

	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getMemptrValue()
	 */
	public byte[] getMemptrValue() {
		return this.memptrValue;
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getSessionParameter(java.lang.String)
	 */
	public String getSessionParameter(String paramName) {
		return this.sessionParametersHash.get(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#getSessionParametersName()
	 */
	public Set<String> getSessionParametersName() {
		return sessionParametersHash.keySet();
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#hasCGIParameter(java.lang.String)
	 */
	public boolean hasCGIParameter(String paramName) {
		return this.cgiParametersHash.containsKey(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#hasHeaderParameter(java.lang.String)
	 */
	public boolean hasHeaderParameter(String paramName) {
		return this.headerParametersHash.containsKey(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#hasInputParameter(java.lang.String)
	 */
	public boolean hasInputParameter(String paramName) {
		return this.inputParametersHash.containsKey(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#hasSessionParameter(java.lang.String)
	 */
	public boolean hasSessionParameter(String paramName) {
		return this.sessionParametersHash.containsKey(paramName);
	}

	
	/* (non-Javadoc)
	 * @see net.quarix.api.IDispatcherRequestParameters#setMemptrValue(byte[])
	 */
	public void setMemptrValue(byte[] memptrValue) {
		if( memptrValue == null ) memptrValue = new byte[0];
		this.memptrValue = memptrValue;
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
	 * @see net.quarix.api.IDispatcherRequestParameters#getDebugString()
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
		
		response.append("\n INPUT PARAMETERS");
		nameIterator = this.inputParametersHash.keySet().iterator();
		String[] paramNameArr = null;
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=[");
			paramNameArr = this.inputParametersHash.get(paramName);
			for( int idx = 0; idx < paramNameArr.length; idx++) {
				response.append(paramNameArr[idx] + ",");
			}
			response.append("]");
		}

		response.append("\n HEADER PARAMETERS");
		nameIterator = this.headerParametersHash.keySet().iterator();
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=" + this.headerParametersHash.get(paramName));
		}

		response.append("\n CGI PARAMETERS");
		nameIterator = this.cgiParametersHash.keySet().iterator();
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=" + this.cgiParametersHash.get(paramName));
		}
		
		response.append("\n SESSION PARAMETERS");
		nameIterator = this.sessionParametersHash.keySet().iterator();
		while( nameIterator.hasNext()) {
			paramName = nameIterator.next();
			response.append("\n\t" + paramName + "=" + this.sessionParametersHash.get(paramName));
		}
		
		return response.toString();
	}	
}

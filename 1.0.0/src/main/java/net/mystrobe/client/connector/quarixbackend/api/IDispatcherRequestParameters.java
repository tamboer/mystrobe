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
 package net.mystrobe.client.connector.quarixbackend.api;

import java.util.List;
import java.util.Set;

/**
 * @author TVH Group NV
 */
public interface IDispatcherRequestParameters extends LogEnabled {
	
    static final int REQ_TYPE_INPUT = 1;
    static final int REQ_TYPE_HEADERS = 2;
    static final int REQ_TYPE_CGI = 3;
    static final int REQ_TYPE_SESSION = 4;
    
    
	void addInputParameter(String paramName, String paramValue);
	void setInputParameter(String paramName, String[] paramValues);
	boolean hasInputParameter(String paramName);
	String getFirstInputParameter(String paramName);
	String[] getInputParameters(String paramName);
	Set<String> getInputParametersName();
	
	void addSessionParameter(String paramName, String paramValue);
	boolean hasSessionParameter(String paramName);
	String getSessionParameter(String paramName);
	Set<String> getSessionParametersName();
	
	void addCGIParameter(String paramName, String paramValue);
	boolean hasCGIParameter(String paramName);
	String getCGIParameter(String paramName);
	Set<String> getCGIParametersName();
	
	void addHeaderParameter(String paramName, String paramValue);
	boolean hasHeaderParameter(String paramName);
	String getHeaderParameter(String paramName);
	Set<String> getHeaderParametersName();
	
	List<DispatcherParameter> getAll();
	
	void setMemptrValue(byte[] memptrValue);
	
	byte[] getMemptrValue();
	
	String getDebugString();
}

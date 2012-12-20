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

import java.util.Map;
import java.util.Set;

/**
 * @author TVH Group NV
 */
public interface IDispatcherResponseParameters extends LogEnabled {
	
    //public static final int RESP_TYPE_OUTPUT = 1;
    public static final int RESP_TYPE_HEADER = 2;
    public static final int RESP_TYPE_COOKIE = 3;
    public static final int RESP_TYPE_BROKER = 4;	

	Set<String> getHeaderParameterNames();
	boolean hasHeaderParameterName(String paramName);
	void addHeaderParameter(String paramName, String paramValue);
	String getHeaderParameter(String paramName);


	
	Set<String> getBrokerParameterNames();
	Map<String,String> getBrokerParameterMap();
	boolean hasBrokerParameterName(String paramName);
	String getBrokerParameter(String paramName);
	void addBrokerParameter(String paramName, String paramValue);
	
	byte[] getMemptrValue();
		
	String getDebugString();
}

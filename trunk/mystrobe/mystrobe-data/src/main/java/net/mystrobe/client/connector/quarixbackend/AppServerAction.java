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

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.config.MyStrobeCoreSettingsProvider;
import net.mystrobe.client.connector.LocalizationProperties;
import net.mystrobe.client.connector.quarixbackend.api.IAppServer;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherRequestParameters;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherResponseParameters;
import net.mystrobe.client.connector.quarixbackend.json.DSResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 *
 * @author TVH Group NV
 * @version $Revision: 1.17 $ $Date: 2010/03/11 13:58:48 $
 */
public class AppServerAction {

	private static ConcurrentMap<String, LocalizationProperties> localizationPropertiesCachedMap = new ConcurrentHashMap<String, LocalizationProperties>();
    
	protected String appName;
	
    protected IAppServer appServer;
    
    protected transient IDispatcherResponseParameters dispatcherResponse;
    protected transient IDispatcherRequestParameters dispatcherRequest = new DispatcherRequestParameters();
    protected transient Logger log = LoggerFactory.getLogger(AppServerAction.class);
    public static final String TIME_LOGGING_NAME = "TimeLogging";
    protected transient Logger timeLog = LoggerFactory.getLogger(TIME_LOGGING_NAME);
//    protected transient LocalizationProperties localizationProperties = null;
    
    /**
     * @param context
     * @param routeParameters
     * @param request
     * @param response
     */
    public AppServerAction(IAppServer appServer, String appName) {
        this.appServer = appServer;
        this.appName = appName;
        this.dispatcherRequest.setLog(this.getLog());
    }
    
    
    public void process(String daoClassName, String method, String xml) {
        long start = System.currentTimeMillis(), progressStart = 0, progressEnd = 0, end = 0;

        if( getLog().isTraceEnabled()) {
        	getLog().trace("\n" + xml + "\n");
        }

        try {
            Monitor monitor = MonitorFactory.start("MyStrobe_OpenEdge " + daoClassName);
            progressStart = System.currentTimeMillis();

            loadDispatcherRequest(daoClassName, method, xml);

            if( getLog().isTraceEnabled()) {
                getLog().trace("REQUEST PARAMETERS SENT TO PROGRESS" + dispatcherRequest.getDebugString());
            }
            
            dispatcherResponse = appServer.getDispatcher().process(dispatcherRequest);
            progressEnd = System.currentTimeMillis();
            monitor.stop();
            loadDispatcherResponse(dispatcherResponse);

            if( getLog().isTraceEnabled() ) {
                getLog().trace("RESPONSE PARAMETERS SENT BY PROGRESS" + dispatcherResponse.getDebugString());
                getLog().trace("MEMPTR VALUE :\n" + new String(dispatcherResponse.getMemptrValue()));
            }

        } catch (Exception ex) {
            getLog().error( "Error in processing", ex);
            if (dispatcherRequest != null) {
                getLog().error("Error in processing, server parameters are:" + dispatcherRequest.getDebugString());
            }
            if (dispatcherResponse != null) {
                getLog().error("Error in processing, server response is:" + dispatcherResponse.getDebugString());
            }
            throw new RuntimeException(ex);
        }
        end = System.currentTimeMillis();
        getLog().trace("Request served in " + (end - start) + " ms progress time " + (progressEnd - progressStart) + " ms");
        timeLog.info("MyStrobe_OpenEdge " + daoClassName + " took [" + (progressEnd - progressStart) + "] milis");
    }

    public Document getResponseXML() {
        Document ret = null;
        InputStream inputStream = new ByteArrayInputStream(dispatcherResponse.getMemptrValue());
        InputSource is = new InputSource(inputStream);
        is.setEncoding("UTF-8");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            ret = docFactory.newDocumentBuilder().parse(is);
        } catch (Exception e) {
            getLog().error(e.getMessage());
            throw new WicketDSRuntimeException("Can not parse response xml." ,e);
        } finally {
        	try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        return ret;
    }

    public DSResponse getResponseJSON() {
        DSResponse ret = null;

        //check for HTTP error codes in the response header parameters
        if (dispatcherResponse.getHeaderParameterNames().contains("STATUS-CODE") ) {
        	validateHTTPResponse(); 
        }
         
        String json;
		try {
			json = new String(dispatcherResponse.getMemptrValue(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new WicketDSRuntimeException("Can not read response", e);
		}
        if (json != null && json.length() > 0) {

            /*
            Marshaller<DSResponse> m = TwoLattes.createMarshaller(DSResponse.class);
            ret = m.unmarshall(Json.fromString(json));
             */

            ObjectMapper mapper = new ObjectMapper();
            try {
                ret = mapper.readValue(json, DSResponse.class);
                ret.setLocalizationProperties(getLocalizationProperties());
//                if ( ret.getLocalizationProperties() == null ) {
//                	ret.setLocalizationProperties((LocalizationProperties) localizationProperties.clone());
//                } else {
//                	this.localizationProperties = ret.getLocalizationProperties();
//                }
            } catch (Exception ex) {
                log.error(null, ex);
            }
        }

        return ret;
    }
    
    public LocalizationProperties getLocalizationResponseJSON() {
    	LocalizationProperties ret = null;

        //check for HTTP error codes in the response header parameters
        if (dispatcherResponse.getHeaderParameterNames().contains("STATUS-CODE") ) {
        	validateHTTPResponse(); 
        }
         
        String json = null;
		try {
			json = new String(dispatcherResponse.getMemptrValue(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new WicketDSRuntimeException("Can not read response memptr value");
		}
        if (json != null && json.length() > 0) {

            /*
            Marshaller<DSResponse> m = TwoLattes.createMarshaller(DSResponse.class);
            ret = m.unmarshall(Json.fromString(json));
             */

            ObjectMapper mapper = new ObjectMapper();
            try {
                ret = mapper.readValue(json, LocalizationProperties.class);
                if ( ret != null ) {
                	String key = this.appServer.getUrl() + "_" + this.appName;
                	localizationPropertiesCachedMap.put(key, ret);
                }
            } catch (Exception ex) {
                log.error(null, ex);
            }
        }
        return ret;
    }
    
    private void validateHTTPResponse() {
    	
    	String statusCodeParamValue = dispatcherResponse.getHeaderParameter("STATUS-CODE");
    	String httpCode = statusCodeParamValue.split(" ")[1];
    	
    	if (httpCode != null && (httpCode.startsWith("5") || httpCode.startsWith("4")) ) {
    		StringBuilder errorMessage = new StringBuilder("HTTP Error status code received:").append(httpCode).append(". ");
    		String quarixMessage = dispatcherResponse.getHeaderParameter("X-Quarix-Server-Error-Msg");
    		errorMessage.append(quarixMessage != null ? quarixMessage : statusCodeParamValue);
    		
    		//throw runtime exception on when HTTP error status code is received
    		throw new RuntimeException( errorMessage.toString() );
    	}
    }


    /* (non-Javadoc)
     * @see net.quarix.api.LogEnabled#setLog(org.apache.commons.logging.Log)
     */
    public void setLog(Logger log) {
        this.log = log;
    }


    /* (non-Javadoc)
     * @see net.quarix.api.LogEnabled#getLog()
     */
    public Logger getLog() {
        return log;
    }

    /**
     * Creates and loads the input parameters required at the Progress side
     * @return The input parameters initialized with the proper values to be sent to the app server
     * @throws TransformerException
     */
    protected void loadDispatcherRequest(String daoClassName, String method, String xml) throws TransformerException {

        // not used at this time
        //dispatcherRequest.setInputParameter("User", new String[] {"ion"});
        //dispatcherRequest.setInputParameter("PASSWORD", new String[] {"ion"});
        if (xml != null) {
            dispatcherRequest.setInputParameter("xml", new String[]{xml});
        }

        dispatcherRequest.addCGIParameter("CHECK_ATTACHMENTS", String.valueOf(false));
        dispatcherRequest.addCGIParameter("CHECK_BODY_DATA", String.valueOf(false));

        //faking the session
        long now = System.currentTimeMillis();
        long creationTime = now - 5000;
        long maxInactivInterval = 300000;
        String sessionID;
        if (MyStrobeCoreSettingsProvider.getInstance().canProvideBLSessionIDs()) {
        	sessionID = MyStrobeCoreSettingsProvider.getInstance().getBLSessionID();
        } else {
	        sessionID = appName + "." + daoClassName + "." + now;
	    }

        // faking the locale
        Locale usedLocale = Locale.getDefault();
        if (usedLocale != null) {
            dispatcherRequest.addCGIParameter(Globals.LOCALE_COUNTRY_CODE, usedLocale.getCountry());
        }

        dispatcherRequest.addCGIParameter(Globals.LOCALE_PREFFERED_LOCALES, usedLocale.getCountry());
        
        String pathInfo = appName + "/" + daoClassName.replace(".", "/") + "/" + method;
        
        dispatcherRequest.addCGIParameter("PATH_INFO", pathInfo );
        dispatcherRequest.addCGIParameter("SESSION_CREATION_TIME", "" + creationTime);
        dispatcherRequest.addCGIParameter("SESSION_ID", "" + sessionID);
        dispatcherRequest.addCGIParameter("SESSION_LAST_ACCESSED_TIME", "" + creationTime);
        dispatcherRequest.addCGIParameter("SESSION_MAX_INACTIVE_INTERVAL", "" + maxInactivInterval);

        Calendar cal = Calendar.getInstance();
        long gmtOffset = (cal.get(java.util.Calendar.ZONE_OFFSET) + cal.get(java.util.Calendar.DST_OFFSET)) / (60 * 1000 * 60);
        dispatcherRequest.addCGIParameter("UTC_OFFSET", "" + gmtOffset);

        dispatcherRequest.addSessionParameter("APP_NAME", appName);
        dispatcherRequest.addSessionParameter("LOCALE_LANG", "en");
        dispatcherRequest.addSessionParameter("USER_THEME", "default");
    }


    protected void loadDispatcherResponse(IDispatcherResponseParameters dispatcherResponse) {
//        if( dispatcherResponse == null ) return;
//        
//        if (localizationProperties == null) {
//        	localizationProperties = new LocalizationProperties();
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_LANG")) {
//            localizationProperties.setLanguageCode(dispatcherResponse.getBrokerParameter("LOCALE_LANG"));
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_DATE_FORMAT")){
//            localizationProperties.setDateFormat(dispatcherResponse.getBrokerParameter("LOCALE_DATE_FORMAT"));
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_LOGIC_FORMAT")){
//            localizationProperties.setLogicalFormat(dispatcherResponse.getBrokerParameter("LOCALE_LOGIC_FORMAT"));
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_NUM_DEC")){
//            String value = dispatcherResponse.getBrokerParameter("LOCALE_NUM_DEC");
//            if( value != null && value.length() >= 1) localizationProperties.setNumericalDecimalPoint(value.charAt(0));
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_NUM_FORMAT")){
//            localizationProperties.setNumericalFormat(dispatcherResponse.getBrokerParameter("LOCALE_NUM_FORMAT"));
//        }
//
//        if( dispatcherResponse.hasBrokerParameterName("LOCALE_NUM_SEP")){
//            String value = dispatcherResponse.getBrokerParameter("LOCALE_NUM_SEP");
//            if( value != null && value.length() >= 1) localizationProperties.setNumericalSeparator(value.charAt(0));
//        }
//        
//        setAppServerLocalizationProperties(appServer.getUrl(), localizationProperties);
    }
    
    public LocalizationProperties getLocalizationProperties() {
        LocalizationProperties result = getCachedLocalizationProperties(appServer.getUrl(), appName);
    	if (result == null) {
    		synchronized (AppServerAction.class) {
    			if (getCachedLocalizationProperties(appServer.getUrl(), appName) == null) {
    				process(Globals.LOCALIZATION_URN, Globals.METHOD_DATA, null);
    				getLocalizationResponseJSON();
    			}
			}
    		result = getCachedLocalizationProperties(appServer.getUrl(), appName);
    	} 
        return result != null ? result : new LocalizationProperties();
    }

    public static void setAppServerLocalizationProperties(String appServerUrl, String appName, LocalizationProperties localizationProps) {
    	String key = appServerUrl + "_" + appName;
    	localizationPropertiesCachedMap.putIfAbsent(key, localizationProps);
    }
    
    public static LocalizationProperties getCachedLocalizationProperties(String appServerUrl, String appName) {
    	String key = appServerUrl + "_" + appName;
    	return localizationPropertiesCachedMap.get(key);
    }
    
    public static void clearCachedLocalizationProperties() {
    	synchronized (localizationPropertiesCachedMap) {
    		localizationPropertiesCachedMap.clear();
		}
    }
}

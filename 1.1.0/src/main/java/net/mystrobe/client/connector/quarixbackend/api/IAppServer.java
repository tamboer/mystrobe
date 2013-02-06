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

import java.io.Serializable;




/**
 *
 * @author TVH Group NV
 * @version $Revision: 1.3 $ $Date: 2009/01/08 11:19:16 $
 */
public interface IAppServer extends Initializable, LogEnabled, Serializable {

    
    /**
     * @param url
     */
    void setUrl(String url);
    
    
    /**
     * @return
     */
    String getUrl();
        
    
    /**
     * @param userName
     */
    void setUserName(String userName);
    
    
    /**
     * @return
     */
    String getUserName();
    
        
    /**
     * @param type
     * @param pass
     */
    void setPassword(String type, String pass);
    
        
    /**
     * @return
     */
    String getPassword();
    
    
    
    /**
     * @param info
     */
    void setInfo(String info);
    
        
    /**
     * @return
     */
    String getInfo();    
        
    
    IDispatcher getDispatcher();
    
    
    void setReuseConnection(String reuseConnection);
    
    String getReuseConnection();
    
    boolean isReusingConnection();
    
    void setConnectorClassName(String ConnectorClassName);
    
    String getConnectorClassName();


    void setSessionModel(int sessionModel);
    
    int getSessionModel();
}

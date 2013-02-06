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

import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.IDataBean;

/**
 * @author TVH Group NV
 */
public class DSRequest implements IDSRequest, Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String,IDAORequest<? extends IDataBean>> requestPool = new HashMap<String,IDAORequest<? extends IDataBean>>();

	private String responseFormat;
	
	private String actionCommand;

    public DSRequest() {
        
    }

    public DSRequest( IDAORequest<? extends IDataBean> request ) {
        this();
        this.setActionCommand(request.getCommand());
        if( request != null && request.getDAOId() != null ) requestPool.put(request.getDAOId(), request);
    }

	public void addDAORequest(IDAORequest<? extends IDataBean> request) {
		requestPool.put(request.getDAOId(), request);
		
		//backwards compatibility
		if (this.actionCommand == null) {
			this.actionCommand = request.getCommand();
		}
		
	}

	public <T extends IDataBean> IDAORequest<T> getDAORequest(String daoId) {
		return (IDAORequest<T> ) requestPool.get(daoId);
	}

	public Enumeration<String> getDAORequestIds() {
		return Collections.enumeration(requestPool.keySet());
	}

	public String getResponeFormat() {
		return responseFormat;
	}

	public void removeDAORequest(String daoId) {
		requestPool.remove(daoId);
	}

	public void setResponeFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setActionCommand(String command) {
		this.actionCommand = command;
	}
}

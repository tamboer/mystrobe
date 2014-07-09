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
 package net.mystrobe.client.connector.quarixbackend.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSSchema;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.WicketDSRuntimeException;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.IDSResponse;
import net.mystrobe.client.connector.LocalizationProperties;
import net.mystrobe.client.connector.quarixbackend.StringListDAORow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class DSResponse implements IDSResponse, Serializable {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(DSResponse.class);
    
	private LocalizationProperties localizationProperties;

	private List<DAOResponse<? extends IDataBean>> tables;

	private List<Message> messages;

	private IDSSchema dsSchema;

	public <T extends IDataBean> IDAOResponse<T> getDAOResponse(String tableId, Class<T> dataBeanClass) {
		DAOResponse<T> result = null;
		for (DAOResponse<?> rsp : tables) {
			if (rsp.getId().equals(tableId)) {
				result = (DAOResponse<T>) rsp;
				if (result.getDAORows() == null) {
					
					result.setDAORows(getDAORows(tableId, result.getRows(), dataBeanClass));
				}
				result.setLocalizationProperties(localizationProperties);
				return result;
			}
		}
		return result;
	}

	public Enumeration<String> getDAOResponseIds() {
		List<String> ret = new ArrayList<String>();
		for (DAOResponse<?> rsp : tables) {
			ret.add(rsp.getId());
		}
		return Collections.enumeration(ret);
	}

    
	public List<Message> getDSMessages() {
		if (messages != null ) {
			return Collections.unmodifiableList(messages);
		}
		
		return null;
	}

    
    public boolean hasMessageType(MessageType messageType) {      
        if(messages != null) {
            Iterator<Message>messagesIterator = messages.iterator();
            while( messagesIterator.hasNext() ) {
                if( messagesIterator.next().getMessageType().equals( messageType )) return true;
            }
        }
        return false;
    }


	public void setDSSchema(IDSSchema dsSchema) {
		this.dsSchema = dsSchema;
	}

	private <T extends IDataBean> Collection<IDAORow<T>> getDAORows(final String daoId, List<List<Object>> rows, Class<T> dataBeanClass) {
		List<IDAORow<T>> ret = new ArrayList<IDAORow<T>>();

		if( dsSchema == null ) {
			logger.debug("DS Schema is null ");
		}
		else {
			logger.debug("DS Schema " + dsSchema.getId()+ " is not null class " + dsSchema.getClass());
		}
		
		if( dsSchema.getDataObjectSchema(daoId) == null) {
			logger.debug("DAO with id " + daoId + " of schema " + dsSchema.getId() + " is null");
		} else {
			logger.debug("DAO is " + dsSchema.getDataObjectSchema(daoId).getDAOId() + " class " + dsSchema.getDataObjectSchema(daoId).getClass().getName());
		}

		logger.debug( "DAO URN: " + (dsSchema.getDataObjectSchema(daoId)).getIDataTypeClass());
		
		if (dataBeanClass == null) {
			logger.error("No IDataType class generated for: " + dsSchema.getId() + ":" + daoId);
			throw new WicketDSRuntimeException("No IDataType class generated for: " + dsSchema.getId() + ":" + daoId);			
		}
		
		if( rows != null )  {
			for (final List<Object> row : rows) {
				ret.add( new StringListDAORow<T>((IDAOSchema<T>)dsSchema.getDataObjectSchema(daoId), dataBeanClass, row, getLocalizationProperties()));
			}
		}

		return ret;
	}

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<DAOResponse<? extends IDataBean>> getTables() {
        return tables;
    }

    public void setTables(List<DAOResponse<? extends IDataBean>> tables) {
        this.tables = tables;
    }

    public LocalizationProperties getLocalizationProperties() {
       return this.localizationProperties;
    }

    public void setLocalizationProperties(LocalizationProperties localizationProperties) {
        this.localizationProperties = localizationProperties;
    }
}

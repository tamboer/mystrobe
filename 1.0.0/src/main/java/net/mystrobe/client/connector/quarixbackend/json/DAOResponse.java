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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.connector.IDAOResponse;
import net.mystrobe.client.connector.IDAORow;
import net.mystrobe.client.connector.LocalizationProperties;

/**
 * @author TVH Group NV
 */
public class DAOResponse<T extends IDataBean> implements IDAOResponse<T>, Serializable {

	
	private static final long serialVersionUID = 1L;

	private String name;

	private String id;

	private Info info;

	private List<Message> messages;
	
	private List<List<Object>> rows;

	private Collection<IDAORow<T>> daoRows;
	
	//private Collection<IDAORow<T>> beforeImage;
	
    protected LocalizationProperties localizationProperties ;

    public Info getInfo() {
		return info;
	}

	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public List<List<Object>> getRows() {
		return rows;
	}

	
	public List<Message> getMessages() {
		return messages;
	}

    public boolean hasMessageType(MessageType messageType) {
        if( messages == null ) return false;
        
        Iterator<Message>messagesIterator = messages.iterator();
        while( messagesIterator.hasNext() ) {
            if( messagesIterator.next().getMessageType().equals( messageType )) return true;
        }
        return false;
    }


	public void setDAORows(Collection<IDAORow<T>> daoRows) {
		this.daoRows = daoRows;
	}
	
	public Collection<IDAORow<T>> getDAORows() {
		return daoRows;
	}


	public boolean hasChangesOnly() {
        if( info != null ) return info.changesOnly();
        return true;
	}


	public boolean hasFirstRow() {
		if( info != null ) return info.hasFirstRow();
        return false;
	}


	public boolean hasLastRow() {
		if( info != null ) return info.hasLastRow();
        return false;
	}


	public int selectIndex() {
		String pos = info.getNewPosition();
		int ret = 0;
		try {
			ret = Integer.parseInt(pos);
		} catch (NumberFormatException ignored) {
		}
		return ret;
	}


	public String selectRowId() {
		String pos = info.getNewPosition();
		String ret = null;
		try {
			Integer.parseInt(pos);
		} catch (NumberFormatException ignored) {
			ret = pos;
		}
		return ret;
	}

    public Collection<IDAORow<T>> getDaoRows() {
        return daoRows;
    }

    public void setDaoRows(Collection<IDAORow<T>> daoRows) {
        this.daoRows = daoRows;
    }


    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }
    
    /**
	 * @return the beforeImage
	 */
//	public Collection<IDAORow<T>> getBeforeImage() {
//		return beforeImage;
//	}
//
//	/**
//	 * @param beforeImage the beforeImage to set
//	 */
//	public void setBeforeImage(Collection<IDAORow<T>> beforeImage) {
//		this.beforeImage = beforeImage;
//	}

	public LocalizationProperties getLocalizationProperties() {
       return this.localizationProperties;
    }

    public void setLocalizationProperties(LocalizationProperties localizationProperties) {
        this.localizationProperties = localizationProperties;
    }


	@Override
	public Set<ResponseOption> getResponseOptions() {
		if( info != null ) return info.getOptions();
		return null;
	}
}
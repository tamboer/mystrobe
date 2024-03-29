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

import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.MessageType;

/**
 * @author TVH Group NV
 */
public class Message implements IDAOMessage, Serializable {

	private static final long serialVersionUID = 1L;

	private int type;
	
	private int code;

	private String column;
	
	private String recordId;

	private String msg;
	
	private String file;
		
	private String method;
	
	private String line;
	
	private String tag;
	
	public Message() {

    }

	public String getColumn() {
		return column;
	}

	public String getMessage() {
		return msg;
	}

	public MessageType getMessageType() {
		
		MessageType ret = MessageType.Info;
		
		switch (type) {
			case 0:
				ret = MessageType.Error;
				break;
			case 1:
				ret =  MessageType.Error;
				break;
			case 2:
				ret =  MessageType.Warning;
				break;
			case 3:
				ret =  MessageType.Info;
				break;
			case 4:
				//Unknown, but is not implemented
				break;
		}
		
		return ret;
	}

    public void setColumn(String column) {
        this.column = column;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(int type) {
        this.type = type;
    }


	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}


	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}


	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}


	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}


	/**
	 * @return the line
	 */
	public String getLine() {
		return line;
	}


	/**
	 * @param line the line to set
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * @return the code
	 */
	@Override
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
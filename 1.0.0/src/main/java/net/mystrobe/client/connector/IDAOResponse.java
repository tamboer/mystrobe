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


import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.connector.quarixbackend.json.Message;
import net.mystrobe.client.connector.quarixbackend.json.ResponseOption;


/**
 * Defines an object that encapsulates all the details of an data response
 * resulted from the backend data object responding to an request.
 * 
 * @author TVH Group NV
 */
public interface IDAOResponse<T extends IDataBean> {


	/**
	 * Returns a list of message returned by the backend data object as a result of an
	 * trasaction. Might be null.
	 */
	public List<Message> getMessages();
	
	
	public void setMessages(List<Message> messages);

    
    public boolean hasMessageType(MessageType messageType);

	/**
	 * Returns a list of rows returned from the backend data object. Each row
	 * containing an data instance together with an row state
	 * (Unmodified/Deleted/Updated/New) and possibly with an before image if we are
	 * deep inside an trasaction.
	 */
	public Collection<IDAORow<T>> getDAORows();

	/**
	 * Returns true if the response contains only the changed rows, false othervise.
	 * If the response contains all the rows the data store implementation will have
	 * to drop the equivalent batch and refresh it with the current one.
	 */
	public boolean hasChangesOnly();

	/**
	 * Returns true if the batch received contains the first record of data (is the
	 * first record), false othervise.
	 */
	public boolean hasFirstRow();

	/**
	 * Returns true if the batch received contains the last row of data (last record),
	 * false otherwise.
	 */
	public boolean hasLastRow();

	/**
	 * Returns an integer signaling the position of the record from the current batch
	 * that will have to be selected, or -1 if none should be selected.
	 */
	public int selectIndex();

	/**
	 * Returns the rowId of the record from the batch that should be selected, or null
	 * if none should.
	 */
	public String selectRowId();

   /**
    * Returns the application server localization properties
    * @return an instance of the application server localization properties
    */
    public LocalizationProperties getLocalizationProperties();

    
    /**
     * Return business logic response options.
     *  
     * @return List of response options.
     */
    public Set<ResponseOption> getResponseOptions();
}
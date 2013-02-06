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
 package net.mystrobe.client;

/**
 * A simple class holding an information message sent from the backend to the
 * client.
 * 
 * @author TVH Group NV
 */
public interface IDAOMessage {

	/**
	 * The data object column to which this message is linked. It might be null
	 * signalizing the the message is general data object or data set related and not
	 * column related.
	 */
	public String getColumn();

	/**
	 * The actual message
	 */
	public String getMessage();

	/**
	 * The type of this message which can be: Info, Warning or Error.
	 */
	public MessageType getMessageType();
	
	/**
	 * The message code. Used ti distinguish between multiple messages of same type.
	 */
	public int getCode();
	
	/**
	 * Get message record identifier. 
	 * 
	 * @return Record identifier.
	 */
	public String getRecordId();
	
	/**
	 * Return message tag.
	 * 
	 * @return Message additional information.
	 */
	public String getTag();

}
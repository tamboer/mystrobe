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
 package net.mystrobe.client.error;

import java.util.Collection;
import java.util.List;

import net.mystrobe.client.IDAOMessage;
import net.mystrobe.client.MessageType;
import net.mystrobe.client.connector.messages.IConnectorResponseMessages;
import net.mystrobe.client.connector.quarixbackend.NamingHelper;
import net.mystrobe.client.dynamic.panel.DynamicFormComponentPanel;

import org.apache.wicket.Component;


/**
 * @author TVH Group NV
 */
public class DefaultErrorHandler {
	
	
	/**
	 * Default error handling method.<br/>
	 * 
	 * Reports all error messages to <tt>errorReportingComponent</tt>.
	 * 
	 * Uses column names in the error messages to mark form components as invalid. 
	 * 
	 * @param connectorResponseMessages Connector response messages.
	 * @param errorReportingComponent Component to handle error display
	 * @param formFields Dynamic form fields. Can also be null.
	 * @return False when no error messages are present.
	 */
	public static boolean handleErrors(IConnectorResponseMessages connectorResponseMessages, 
				Component errorReportingComponent, List<DynamicFormComponentPanel<?>> formFields) {
		
		if (!connectorResponseMessages.hasMessageType(MessageType.Error)) {
			return false;
		}
		
		Collection<IDAOMessage> daoMessages = connectorResponseMessages.getMessages(MessageType.Error);
		
		for (IDAOMessage daoMessage : daoMessages) {
			if (daoMessage.getColumn() != null) {
				
				String columnName = NamingHelper.getFieldName(daoMessage.getColumn());
				errorReportingComponent.error("Error on column : " + columnName 
						+ ". Message : "  + daoMessage.getMessage());
				
				if (formFields != null) {
					for (DynamicFormComponentPanel<?> fieldPanel : formFields) {
						if (columnName.equalsIgnoreCase(fieldPanel.getInputPropertyName())) {
							fieldPanel.markAsNotValid();
							break;
						}
					}
				}
			} else {
				errorReportingComponent.error(daoMessage.getMessage());
			}
		}
		
		return true;
	}
}

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
 package net.mystrobe.client.dynamic.panel;


import java.io.Serializable;

import net.mystrobe.client.IDataBean;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;



/**
 * Panel class that renders an input field.
 * Used to dynamically generate form text fields.
 * 
 * Should be enhanced to support additional property settings such
 *  as type, field length & others.
 * 
 * @author TVH Group NV
 */
public class TextFieldPanel<T extends Serializable> extends DynamicFormComponentPanel<T> {

	private static final long serialVersionUID = -3401492928135987240L;

	private static final String TEXT_FIELD_ID = "textField_id"; 
	
	private static final String TEXT_FIELD_LABEL_ID = "textField_label";
	
	private TextField<T> textField;
	
	public String getInputPropertyName() {
		return propertyName;
	}

	public TextFieldPanel(String id, IModel<T> model, String propertyName, IModel<String> labelModel, boolean required, boolean readOnly, int maxLength) {
		super(id, model, propertyName, required, readOnly);
		
		textField = new TextField<T>(TEXT_FIELD_ID, model);
		textField.setRequired(required);
		
		FormComponentLabel label = new DynamicFormComponentLabel(TEXT_FIELD_LABEL_ID, textField, required);
        label.setDefaultModel(labelModel);
		add(label);
		
		textField.setOutputMarkupId(true);
		textField.setLabel(labelModel);
		textField.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(textField);
	}
	
	public FormComponent<T> getFormComponent() {
		return textField;
	}
}

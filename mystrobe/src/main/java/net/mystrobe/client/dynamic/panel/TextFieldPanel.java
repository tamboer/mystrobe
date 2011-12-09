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


import net.mystrobe.client.IDataBean;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
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
public class TextFieldPanel<T> extends DynamicFormComponentPanel{

	private static final long serialVersionUID = -3401492928135987240L;

	private static final String TEXT_FIELD_ID = "textField_id"; 
	
	private static final String TEXT_FIELD_LABEL_ID = "textField_label";
	
	private String propertyName;
	
	private TextField<T> textField;
	
	public String getInputPropertyName() {
		return propertyName;
	}

	public TextFieldPanel(String id, IModel<T> model, String propertyName, IModel<String> label, boolean required, int maxLength) {
		super(id, model);
		
		this.propertyName = propertyName;
		
		add(new Label(TEXT_FIELD_LABEL_ID, label));
		
		textField = new TextField<T>(TEXT_FIELD_ID, model);
		textField.setRequired(required);
		textField.setOutputMarkupId(true);
		textField.setLabel(label);
		
		textField.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(textField);
	}
	
	public FormComponent<T> getFormComponent() {
		return textField;
	}
	
	public void setFormComponentModelObject(IDataBean dataBean) {
		textField.setModel(new PropertyModel<T>(dataBean, propertyName));
	}

}

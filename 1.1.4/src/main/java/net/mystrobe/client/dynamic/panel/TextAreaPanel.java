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
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author TVH Group NV
 */
public class TextAreaPanel<T extends Serializable> extends DynamicFormComponentPanel<T> {

	private static final long serialVersionUID = -100494669933196950L;

	private static final String TEXT_AREA_ID = "textArea_id"; 
	
	private static final String TEXT_AREA_LABEL_ID = "textArea_label";
	
	private TextArea<T> textArea;
	
	public TextAreaPanel(String id, IModel<T> model, String propertyName, IModel<String> labelModel, boolean required, boolean readOnly, int maxLength) {
		
		super(id, model, propertyName, required, readOnly);
		
		textArea = new TextArea<T>(TEXT_AREA_ID, model);
		textArea.setRequired(required);
		textArea.setOutputMarkupId(true);
		textArea.setLabel(labelModel);
		
		textArea.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(textArea);
		
		FormComponentLabel label = new DynamicFormComponentLabel(TEXT_AREA_LABEL_ID, textArea, required);
        label.setDefaultModel(labelModel);
		add(label);
	}


	public FormComponent<T> getFormComponent() {
		return textArea;
	}
	
	public void setFormComponentModelObject(IDataBean dataBean) {
		textArea.setModel(new PropertyModel<T>(dataBean, propertyName));
	}
	
	public String getInputPropertyName() {
		return propertyName;
	}

}

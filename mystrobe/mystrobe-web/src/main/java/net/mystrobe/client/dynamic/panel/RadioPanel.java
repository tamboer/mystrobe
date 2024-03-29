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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.dynamic.config.FieldValueOptionsRenderer;
import net.mystrobe.client.dynamic.config.IFieldValue;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


/**
 * Radio panel creates a radio button foprm input field.
 * 
 * Used to dynamically generate radio input fields for the dynamic form. 
 * 
 * @author TVH Group NV
 */
public class RadioPanel<T extends Serializable> extends DynamicFormComponentPanel<T> {

	private static final long serialVersionUID = -6022662995120166158L;
	
	/**
	 * Radio panel html markup constants
	 */
	private static final String RADIO_CHOICE_ID = "radioChoice_id"; 
	private static final String RADIO_CHOICE_LABEL_ID = "radioChoice_label";
	
	private RadioChoice<T> radioChoice;
	
	public RadioPanel(String id, IModel<T> model, String propertyName, IModel<String> labelModel, Collection<IFieldValue<T>> options, boolean displayInLine, boolean required, boolean readOnly) {
		super(id, model, propertyName, required, readOnly);
		
		Map<T, IFieldValue<T>> optionsMap = new HashMap<T, IFieldValue<T>>(options.size());
		for (IFieldValue<T> fieldValue : options) {
			optionsMap.put(fieldValue.getValue(), fieldValue);
		}
		
		radioChoice = new RadioChoice<T>(RADIO_CHOICE_ID, model,  new ArrayList<T>(optionsMap.keySet()) , new FieldValueOptionsRenderer<T>(optionsMap));
		radioChoice.setLabel(labelModel);
		radioChoice.setOutputMarkupId(true);
		radioChoice.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(radioChoice);
		
		FormComponentLabel label = new DynamicFormComponentLabel(RADIO_CHOICE_LABEL_ID, radioChoice, required);
        label.setDefaultModel(labelModel);
		add(label);
	}
	
	public FormComponent<T> getFormComponent() {
		return radioChoice;
	}

	public String getInputPropertyName() {
		return propertyName;
	}

	public void setFormComponentModelObject(IDataBean dataBean) {
		radioChoice.setModel(new PropertyModel<T>(dataBean, propertyName));
	} 
}

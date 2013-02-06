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
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.ui.UICssResourceReference;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author TVH Group NV
 */
public class AutoCompleteTextFieldPanel<T extends Serializable, S extends IDataBean> extends DynamicFormComponentPanel<T> {

	private static final long serialVersionUID = -1869714905060029368L;

	private static final String TEXT_FIELD_ID = "textField_id"; 
	
	private static final String TEXT_FIELD_LABEL_ID = "textField_label";
	
	public DataObjectAutoCompleteTextField<S,T> autoCompleteTextField;
	
	public AutoCompleteTextFieldPanel(String id, IModel<T> model, String propertyName, IModel<String> labelModel, boolean required, boolean readOnly,
			IDataObject<S> autoCompleteDataSource, String autoCompleteFilterColumnName) {
	
		super(id, model, propertyName, required, readOnly);
	
		autoCompleteTextField = new DataObjectAutoCompleteTextField<S,T>(TEXT_FIELD_ID, model, 
				autoCompleteDataSource, autoCompleteFilterColumnName);
		autoCompleteTextField.setRequired(required);
		autoCompleteTextField.setOutputMarkupId(true);
		autoCompleteTextField.setLabel(labelModel);
		
		autoCompleteTextField.add(FIELD_NOT_VALID_BEHAVIOR);
		
		add(autoCompleteTextField);	
		
		FormComponentLabel label = new DynamicFormComponentLabel(TEXT_FIELD_LABEL_ID, autoCompleteTextField, required);
        label.setDefaultModel(labelModel);
		add(label);
	}

	public FormComponent<T> getFormComponent() {
		return autoCompleteTextField;
	}

	public String getInputPropertyName() {
		return propertyName;
	}

	public void setFormComponentModelObject(IDataBean dataBean) {
		autoCompleteTextField.setModel(new PropertyModel<T>(dataBean, this.propertyName));
	}
}

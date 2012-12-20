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

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


/**
 * Panel class that renders a checkbox field.
 * Used to dynamically generate form checkbox fields.
 * 
 * 
 * @author TVH Group NV
 *
 */
public class CheckBoxPanel extends DynamicFormComponentPanel  {

	private static final long serialVersionUID = -776109128438026167L;

	private final String CHECK_BOX_ID = "checkBox_id";
	
	private final String CHECK_BOX_LABEL_ID = "checkBox_label";
	
	private CheckBox checkBox;
	
	public CheckBoxPanel(String id, IModel<Boolean> model, String propertyName, IModel<String> labelModel, boolean required, boolean readOnly) {
		super(id, model, propertyName, required, readOnly);
		
		checkBox = new CheckBox(CHECK_BOX_ID, model); 
		checkBox.setOutputMarkupId(true);
		checkBox.setLabel(labelModel);
		checkBox.add(FIELD_NOT_VALID_BEHAVIOR);
		add(checkBox);
		
		FormComponentLabel label = new DynamicFormComponentLabel(CHECK_BOX_LABEL_ID, checkBox, required);
        label.setDefaultModel(labelModel);
		add(label);
	}


	public String getInputPropertyName() {
		return propertyName;
	}
	
	public FormComponent<Boolean> getFormComponent() {
		return checkBox;
	}
	
	public void setFormComponentModelObject(IDataBean dataBean) {
		checkBox.setModel(new PropertyModel<Boolean>(dataBean, propertyName));
	}
	

}

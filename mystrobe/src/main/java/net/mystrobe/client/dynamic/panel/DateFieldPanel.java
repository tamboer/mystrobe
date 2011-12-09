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

import java.util.Date;

import net.mystrobe.client.IDataBean;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author TVH Group NV
 */
public class DateFieldPanel extends DynamicFormComponentPanel{

	private static final long serialVersionUID = -3401492928135987240L;

	private static final String DATE_FIELD_ID = "dateField_id"; 
	
	private static final String DATE_FIELD_LABEL_ID = "dateField_label";
	
	private String propertyName;
	
	private DateTextField dateTextField;
	
	private DatePickerVisibilityAware datePicker;
	
	public DateFieldPanel(String id, IModel<Date> model, String propertyName, IModel<String> label, boolean required, String dateFormat) {
		super(id, model);
		
		this.propertyName = propertyName;
		
		add(new Label(DATE_FIELD_LABEL_ID, label));
		 
		if (dateFormat == null) {
			dateTextField = new DateTextField(DATE_FIELD_ID, model);
		} else {
			dateTextField = new DateTextField(DATE_FIELD_ID, model, dateFormat);
		}
		
		dateTextField.setRequired(required);
		dateTextField.setOutputMarkupId(true);
		dateTextField.setLabel(label);
		dateTextField.add(FIELD_NOT_VALID_BEHAVIOR);
		
		datePicker = new DatePickerVisibilityAware();
		dateTextField.add(datePicker);
		
		add(dateTextField);
	}
	
	public FormComponent<?> getFormComponent() {
		return dateTextField;
	}

	public String getInputPropertyName() {
		return propertyName;
	}

	public void setFormComponentModelObject(IDataBean dataBean) {
		dateTextField.setModel(new PropertyModel<Date>(dataBean, propertyName));
	}

	@Override
	public String getDisableFormFieldJavaScript() {
		return datePicker.getDoNotDisplayJavaScript();
	}

	private class DatePickerVisibilityAware extends DatePicker {
		
		private static final long serialVersionUID = -3744342446586580608L;

		public String getDoNotDisplayJavaScript() {
			
			StringBuilder javaScript = new StringBuilder(); 
			javaScript.append("if (document.getElementById('").append(getIconId()).append("')!= null) { ");
			javaScript.append("document.getElementById('").append(getIconId()).append("').style.display='none';");
			javaScript.append("}");
			
			return javaScript.toString();
		}
		
	}
	
	
}

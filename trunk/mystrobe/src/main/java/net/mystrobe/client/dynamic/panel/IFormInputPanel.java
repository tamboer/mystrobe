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

import org.apache.wicket.markup.html.form.FormComponent;


/**
 * Dynamic form input field panel class.</br>
 * 
 * All dynamic for input fields (text field, text area, checkbox, radio etc) 
 * have to implement this interface.</br>  
 * 
 * <p>
 * Contains general form action manipulation methods that are required  
 *  by all form fields. Methods are called by disable/enable operations,
 *  form data is changed, field is not valid etc.
 *  </p>   
 *  
 * @author TVH Group NV
 */
public interface IFormInputPanel {
	
	/**
	 * Model property name attached to form input.  
	 * 
	 * @return Model property name.
	 */
	public String getInputPropertyName();
	
	/**
	 * Retrieves actual form component contained by the panel.   
	 * 
	 * @return Form input filed contained by the panel.
	 */
	public FormComponent<?> getFormComponent();
	
	/**
	 * Method updates the form component model object.
	 * 
	 * @param dataBean New model object.
	 */
	public void setFormComponentModelObject(IDataBean dataBean);
	
	/**
	 * Enables current for component.
	 */
	public void enableFormFieldPanel();
	
	/**
	 * Disables current form component
	 */
	public void disableFormFieldPanel();
	
	
	/**
	 * Additional java script code to execute when disabling component.
	 * 
	 * @return Java script text.
	 */
	public String getDisableFormFieldJavaScript();
	
	/**
	 * Additional java script code to execute when enabling component.
	 * 
	 * @return Java script text.
	 */
	public String getEnableFormFieldJavaScript();
	
	
	/**
	 * Display form component field as invalid.<br/>
	 * 
	 * Used when form validation succeeds but back end validation fails for 
	 *  form field contained by the panel. 
	 */
	public void markAsNotValid();
	
}

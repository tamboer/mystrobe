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

import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Base wicket component class inherited by all dynamic form input fields.</br>
 * 
 * @author TVH Group NV
 *
 */
public abstract class DynamicFormComponentPanel extends Panel implements IFormInputPanel{

	private static final long serialVersionUID = 534266307888283782L;
	
	protected static final String INVALID_STYLE_CLASS = "invalid";

	protected static final String STYLE_CLASS_ATTRIBUTE_NAME = "class";
	
	/**
	 * Meta data used to signal component that back end validation failed.
	 * 
	 * @author TVH Group NV
	 *
	 */
	protected static class ErrorKey extends MetaDataKey<String> {

		private static final long serialVersionUID = -5973581697886335097L;
		
		public static ErrorKey ERROR_KEY = new ErrorKey();
	}
	
	/**
	 * Behavior used by form field input components when not valid.
	 */
	protected final Behavior FIELD_NOT_VALID_BEHAVIOR = new Behavior() {
		
		private static final long serialVersionUID = -9060204060870131886L;

		@Override
		public void onComponentTag(final Component component, final ComponentTag tag)
		{
			if ( component instanceof FormComponent<?>) {
				
				boolean backEndError = component.getMetaData(ErrorKey.ERROR_KEY) != null;
				if (backEndError) {
					component.setMetaData(ErrorKey.ERROR_KEY, null);
				}
				
				if (!((FormComponent<?>)component).isValid() || backEndError) {
					tag.addBehavior(new AttributeAppender(STYLE_CLASS_ATTRIBUTE_NAME, true, Model.of(INVALID_STYLE_CLASS), " "));
				}
			}
		}
	}; 
	
	public DynamicFormComponentPanel(String id) {
		super(id);
	}

	public DynamicFormComponentPanel(String id, IModel<?> model) {
		super(id, model);
	}
	
	public void disableFormFieldPanel() {
		getFormComponent().setEnabled(false);
	}

	
	public void enableFormFieldPanel() {
		getFormComponent().setEnabled(true);
	}

	public String getDisableFormFieldJavaScript() {
		return StringUtil.EMPTY_STRING;
	}

	public String getEnableFormFieldJavaScript() {
		return StringUtil.EMPTY_STRING;
	}
	
	public void markAsNotValid() {
		getFormComponent().setMetaData(ErrorKey.ERROR_KEY, getInputPropertyName());
	}
	
}

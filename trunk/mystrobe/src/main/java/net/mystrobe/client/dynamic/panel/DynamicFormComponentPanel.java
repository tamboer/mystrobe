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
import net.mystrobe.client.ui.UICssResourceReference;
import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


/**
 * Base wicket component class inherited by all dynamic form input fields.</br>
 * 
 * @author TVH Group NV
 *
 */
public abstract class DynamicFormComponentPanel<T extends Serializable> extends FormComponentPanel<T> implements IFormInputPanel<T> {

	private static final long serialVersionUID = 534266307888283782L;
	
	public static final String INVALID_STYLE_CLASS = "invalid";

	public static final String REQUIRED_STYLE_CLASS = "required";

	public static final String STYLE_CLASS_ATTRIBUTE_NAME = "class";
	
	protected String propertyName;
	
	protected boolean required, readOnly; 
	
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
		public void onComponentTag(Component component, ComponentTag tag) {
		
			FormComponent<?> formComponent = null; 
			
			if ( component instanceof FormComponent<?>) {
				formComponent = (FormComponent<?>)component;
			} else {
				formComponent = component.findParent(DynamicFormComponentPanel.class);
			}
			
			boolean backEndError = formComponent.getMetaData(ErrorKey.ERROR_KEY) != null;
			if (backEndError) {
				formComponent.setMetaData(ErrorKey.ERROR_KEY, null);
			}
			
			if (!formComponent.isValid() || backEndError) {
				tag.append(STYLE_CLASS_ATTRIBUTE_NAME, INVALID_STYLE_CLASS, " ");
			}
		}
	};
	
	public DynamicFormComponentPanel(String id, IModel<T> model, String propertyName, boolean required, boolean readOnly) {
		super(id, model);
		this.propertyName = propertyName;
		this.readOnly = readOnly;
		this.required = required;
	}
	
	public DynamicFormComponentPanel(String id, IModel<T> model) {
		super(id, model);
	}
	
	public void disableFormFieldPanel() {
		if (getFormComponent() != null ) {
			getFormComponent().setEnabled(false);
		}
	}

	
	public void enableFormFieldPanel() {
		if (!readOnly && getFormComponent() != null) {
			getFormComponent().setEnabled(true);
		} 
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
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	}
	
	@Override
	protected void convertInput() {
		if (getFormComponent() != null) {
			T input = getFormComponent().getConvertedInput();
			if(input == null)
				setModelObject(null);
			setConvertedInput(input);
		} else {
			super.convertInput();
		}
	}
	
	@Override
	public void setFormComponentModelObject(IDataBean dataBean) {
		IModel<T> newModel = PropertyModel.<T>of(dataBean, this.propertyName); 
		setDefaultModel(newModel);
		if (getFormComponent() != null && getFormComponent() != this) {
			getFormComponent().setDefaultModel(newModel);
		}
	}
}

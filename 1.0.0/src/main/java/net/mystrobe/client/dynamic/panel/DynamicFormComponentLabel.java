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

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.LabeledWebMarkupContainer;

public class DynamicFormComponentLabel extends FormComponentLabel {

	private boolean required; 
	
	public DynamicFormComponentLabel(String id, LabeledWebMarkupContainer component, boolean required ) {
		super(id, component);
		this.required = required;
		setEscapeModelStrings(this.required);
	}
	
	@Override
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if (!this.required) {
			StringBuilder stringBuilder = new StringBuilder(getDefaultModelObjectAsString()); 
			replaceComponentTagBody(markupStream, openTag, stringBuilder.toString());
		} else {
			StringBuilder stringBuilder = new StringBuilder(getDefaultModelObjectAsString()); 
			stringBuilder.append("<span class='" + DynamicFormComponentPanel.REQUIRED_STYLE_CLASS +"'>*</span>");
			replaceComponentTagBody(markupStream, openTag, stringBuilder.toString());
		}
	}
}

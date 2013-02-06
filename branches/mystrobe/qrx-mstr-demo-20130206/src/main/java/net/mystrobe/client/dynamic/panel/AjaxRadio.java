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

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * Ajax onClick aware radio
 * 
 * @author TVH Group NV
 *
 * @param <T> 
 */
public class AjaxRadio<T> extends Radio<T> {

	private static final long serialVersionUID = -3173738484991546963L;

	public AjaxRadio(String id, IModel<T> model, RadioGroup<T> group) {
		super(id, model, group);
	
		addAjaxBehavior(group);
		
		setOutputMarkupId(true);
	}
	
	private void addAjaxBehavior(final RadioGroup<T> group) {
		
		add(new AjaxEventBehavior("onclick"){
			
			private static final long serialVersionUID = 1L;

			protected void onEvent(AjaxRequestTarget target) {
				group.processInput();
				onAjaxEvent(target);
			}
			
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				
				attributes.getDynamicExtraParameters().add("Wicket.Form.serializeElement(document.getElementById('"
					    + AjaxRadio.this.getMarkupId() + "'));");
			}
		});
	}
	
	protected void onAjaxEvent(AjaxRequestTarget target) {
		
	}
}

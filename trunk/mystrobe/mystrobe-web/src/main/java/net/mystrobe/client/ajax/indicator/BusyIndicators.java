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
 package net.mystrobe.client.ajax.indicator;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author TVH Group NV
 */
public class BusyIndicators {
	
/**
 * Class for ajax components which will display the busy indicator when clicked.
 * 	
 */
public static final String DEFAULT_BUSY_INDICATOR = "wrk_indicator";
	
	/**
	 * Ajax link busy indicator.
 	 * 
	 */
	public static abstract class AjaxBusyIndicatorLink<T> extends AjaxLink<T> implements IAjaxIndicatorAware{
		
		private static final long serialVersionUID = -5856979071084965125L;

		public AjaxBusyIndicatorLink(String id) {
			super(id);
		}
	
		public AjaxBusyIndicatorLink(String id, IModel<T> model) {
			super(id, model);
		}

		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}
	}
	
	/**
	 * Ajax button busy indicator class
	 * 
	 */
	public static abstract class AjaxBusyIndicatorButton extends AjaxButton implements IAjaxIndicatorAware{
		
		private static final long serialVersionUID = -6061641517382588985L;

		public AjaxBusyIndicatorButton(String id, Form<?> form) {
			super(id, form);
		}

		public AjaxBusyIndicatorButton(String id, IModel<String> model,Form<?> form) {
			super(id, model, form);
		}

		public AjaxBusyIndicatorButton(String id, IModel<String> model) {
			super(id, model);
		}

		public AjaxBusyIndicatorButton(String id) {
			super(id);
		}

		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}
		
	}
	
	/**
	 * Busy indicator check box.	
	 *
	 */
	public static abstract class AjaxBusyIndicatorCkeckBox extends AjaxCheckBox implements IAjaxIndicatorAware{

		private static final long serialVersionUID = -25710786858446888L;

		public AjaxBusyIndicatorCkeckBox (String id, IModel<Boolean> model) {
			super(id, model);
		}

		public AjaxBusyIndicatorCkeckBox (String id) {
			super(id);
		}
		
		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}
	}
	
	/**
	 * Busy indicator text field.	
	 *
	 */	
	public static class TexFieldBusyIndicator<T> extends TextField<T> implements IAjaxIndicatorAware{
		
		private static final long serialVersionUID = 908447821266064690L;

		public TexFieldBusyIndicator(String id, Class<T> type) {
			super(id, type);
		}

		public TexFieldBusyIndicator(String id, IModel<T> model,Class<T> type) {
			super(id, model, type);
		}

		public TexFieldBusyIndicator(String id, IModel<T> model) {
			super(id, model);
		}

		public TexFieldBusyIndicator(String id) {
			super(id);
		}

		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}
	}
	
	/**
	 * Busy indicator drop down.	
	 *
	 */
	public static abstract class DropDownBusyIndicator<T> extends DropDownChoice<T> implements IAjaxIndicatorAware{

		private static final long serialVersionUID = -7653545043008965384L;

		public DropDownBusyIndicator(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer){
			super(id, model, choices, renderer);
			init();
		}

		private void init(){
			add(new AjaxFormComponentUpdatingBehavior("onchange"){

				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					DropDownBusyIndicator.this.onSelectionUpdate(target);
				}
			});
		}
		
		public abstract void onSelectionUpdate(AjaxRequestTarget target);
		
		public String getAjaxIndicatorMarkupId() {
			return DEFAULT_BUSY_INDICATOR;
		}	
	}
}

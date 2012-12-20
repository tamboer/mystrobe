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

import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Busy indicator panel.</p>
 * 
 * In order to use the busy indicators defined in {@link BusyIndicators} you 
 *  have to add this panel to the page/panel where it will be used. </p>
 *  
 * Including panel in the page it will make all ajax calls 
 * 	in the page use this busy indicator </p>.  
 *  
 * @author TVH Group NV
 */
public class AjaxBusyIndicator extends Panel {

	private static final long serialVersionUID = 7370030735741642604L;

	private static String CSS_Resource = "busyIndicator.css";
	private static String JS_Resource = "busyIndicator.js";
	
	public AjaxBusyIndicator(String id) {
		super(id);
	}
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(WicketEventJQueryResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(WicketAjaxJQueryResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AjaxBusyIndicator.class, JS_Resource)));
		response.render(CssHeaderItem.forReference(new CssResourceReference(AjaxBusyIndicator.class, CSS_Resource)));
	 }
}

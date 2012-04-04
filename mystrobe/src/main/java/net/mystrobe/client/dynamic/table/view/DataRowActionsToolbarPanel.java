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
 package net.mystrobe.client.dynamic.table.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.dynamic.table.view.DataRowActionsToolbarColumn.DataRecordAction;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;



/**
 * Panel to display action icons.</br>
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type.
 */
public abstract class DataRowActionsToolbarPanel<T extends IDataBean> extends Panel {

	private static final long serialVersionUID = 708271910833781574L;
	
	private static final String DELETE_LINK_MARKUP_ID = "deleteRecordLinkId";
	
	private static final String ADD_LINK_MARKUP_ID = "addRecordLinkId";		
	
	private static final String VIEW_LINK_MARKUP_ID = "viewRecordLinkId";
	
	private static final String GO_LINK_MARKUP_ID = "goRecordLinkId";
	
	private static final String EDIT_LINK_MARKUP_ID = "editRecordLinkId";
	
	protected AjaxCancelEventBubbleCallDecorator cancelEventBubbleDecorator = new AjaxCancelEventBubbleCallDecorator();
	
	/**
	 * Constructor
	 * 
	 * @param id Panel id.
	 * @param model Data bean model.
	 * @param dataRecordActions Available actions.
	 */
	public DataRowActionsToolbarPanel(String id, IModel<T> model, DataRecordAction[] dataRecordActions) {
		super(id, model);
		setOutputMarkupId(true);
		
		Set<DataRecordAction> dataRecordActionsSet = new HashSet<DataRecordAction>(Arrays.asList(dataRecordActions));
		
		StopEventPropagationAjaxLink<T> addRecordLink = createActionAjaxLink(ADD_LINK_MARKUP_ID, DataRecordAction.Add, model);
		addRecordLink.setVisible(dataRecordActionsSet.contains(DataRecordAction.Add));
		add(addRecordLink);
		
		StopEventPropagationAjaxLink<T> editRecordLink = createActionAjaxLink(EDIT_LINK_MARKUP_ID, DataRecordAction.Edit, model);
		editRecordLink.setVisible(dataRecordActionsSet.contains(DataRecordAction.Edit));
		add(editRecordLink);

		StopEventPropagationAjaxLink<T> goRecordLink = createActionAjaxLink(GO_LINK_MARKUP_ID, DataRecordAction.Go, model);
		goRecordLink.setVisible(dataRecordActionsSet.contains(DataRecordAction.Go));
		add(goRecordLink);
		
		StopEventPropagationAjaxLink<T> deleteRecordLink = createActionAjaxLink(DELETE_LINK_MARKUP_ID, DataRecordAction.Delete, model);
		deleteRecordLink.setVisible(dataRecordActionsSet.contains(DataRecordAction.Delete));
		add(deleteRecordLink);
		
		StopEventPropagationAjaxLink<T> viewRecordLink = createActionAjaxLink(VIEW_LINK_MARKUP_ID, DataRecordAction.View, model);
		viewRecordLink.setVisible(dataRecordActionsSet.contains(DataRecordAction.View));
		add(viewRecordLink);
	}
	
	protected StopEventPropagationAjaxLink<T> createActionAjaxLink(String linkMarkupId, final DataRecordAction dataRecordAction, IModel<T> model) {
		
		return new StopEventPropagationAjaxLink<T>(linkMarkupId, model) {
			
			private static final long serialVersionUID = -1191898864712404159L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onActionClick(dataRecordAction, target, getModel());
			}
		};
	}
	
	public abstract class StopEventPropagationAjaxLink<M extends IDataBean> extends AjaxFallbackLink<M> {

		private static final long serialVersionUID = 8852735326063398683L;

		public StopEventPropagationAjaxLink(String id) {
			super(id);
		}

		public StopEventPropagationAjaxLink(String id, IModel<M> model) {
			super(id, model);
		}

		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator() {
			return cancelEventBubbleDecorator;
		}
	}
	
	public class AjaxCancelEventBubbleCallDecorator extends AjaxCallDecorator {
		
		private static final long serialVersionUID = -7256200628313699983L;

		public CharSequence decorateScript(Component c, CharSequence script) {
		    return (new StringBuilder()).append("var e = arguments[0] || window.event; if(e.stopPropagation) {e.stopPropagation();}else{e.cancelBubble = true;}").append(script).toString();
		}
	}
	
	/**
	 * Method called when action links are clicked.</br>
	 * 
	 * Instances have to override this method in order to take
	 *  appropriate actions when links are clicked. 
	 * 
	 * @param action Data record action(delete, update, save ..) 
	 * @param target Ajax request target.
	 * @param model Data bean model object.
	 */
	public abstract void onActionClick(DataRecordAction action, AjaxRequestTarget target, IModel<T> model); 

}

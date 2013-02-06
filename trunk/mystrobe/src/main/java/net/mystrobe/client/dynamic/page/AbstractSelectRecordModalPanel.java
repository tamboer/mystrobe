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
 package net.mystrobe.client.dynamic.page;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.WicketDSBLException;
import net.mystrobe.client.ui.UICssResourceReference;
import net.mystrobe.client.util.StringUtil;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author TVH Group NV
 */
public abstract class AbstractSelectRecordModalPanel<T extends IDataBean> extends Panel implements ISelectRecordComponent {

	private static final long serialVersionUID = -445587064054510496L;

	protected IDataObject<T> dataObject = null;
	
	protected ModalWindow modalWindow;
	
	protected static String WINDOW_TITLE_RESOURCE_KEY = "component.SelectRecordModalPanel.title"; 
	
	public AbstractSelectRecordModalPanel(String id, ModalWindow modalWindow) {
		super(id);
		this.modalWindow = modalWindow;
		String titleKey = WINDOW_TITLE_RESOURCE_KEY; 
		this.modalWindow.setTitle(  new ResourceModel(titleKey, 
				StringUtil.buildDefaultResourceValue(titleKey)));
	}
	
	public AbstractSelectRecordModalPanel(String id, ModalWindow modalWindow, IDataObject<T> dataObject) {
		this(id, modalWindow);
		this.dataObject = dataObject;
		
		String titleKey = WINDOW_TITLE_RESOURCE_KEY + "." + dataObject.getSchema().getDAOId(); 
		this.modalWindow.setTitle(  new ResourceModel(titleKey,
				StringUtil.buildDefaultResourceValue(titleKey)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mystrobe.client.dynamic.page.ISelectRecordComponent#isSelected()
	 */
	public abstract boolean isSelected();

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.mystrobe.client.dynamic.page.ISelectRecordComponent#initializePageComponents()
	 */
	public abstract void initializePageComponents() throws WicketDSBLException; 
	
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	}
}

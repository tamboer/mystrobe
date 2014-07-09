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

import java.util.Map;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.dynamic.config.IDynamicFormConfig;

import org.apache.wicket.Page;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.PageCreator;

/**
 * @author TVH Group NV
 */
public class EditRecordModalPageCreator<T extends IDataBean> implements PageCreator{
	
	private static final long serialVersionUID = 1L;

	private ModalWindowUpdateMode windowUpdateMode = ModalWindowUpdateMode.View;
	
	private ModalWindow modalWindow;
	
	private IDataObject<T> dataObjectAdaptor;
	
	private IDynamicFormConfig<T> tableColumnsConfig;
	
	private Map<String, Object> initRecordValuesMap;
	
	public EditRecordModalPageCreator(ModalWindow modalWindow, IDataObject<T> dataObjectAdaptor, IDynamicFormConfig<T> tableColumnsConfig) {
		super();
		this.modalWindow = modalWindow;
		this.dataObjectAdaptor = dataObjectAdaptor;
		this.tableColumnsConfig = tableColumnsConfig;
	}

	public Page createPage() {
		return new EditRecordModalPage<T>(dataObjectAdaptor, modalWindow, tableColumnsConfig, windowUpdateMode, initRecordValuesMap);
	}

	/**
	 * @return the windowUpdateMode
	 */
	public ModalWindowUpdateMode getWindowUpdateMode() {
		return windowUpdateMode;
	}

	/**
	 * @param windowUpdateMode the windowUpdateMode to set
	 */
	public void setWindowUpdateMode(ModalWindowUpdateMode windowUpdateMode) {
		this.windowUpdateMode = windowUpdateMode;
	}

	/**
	 * @return the initRecordValuesMap
	 */
	public Map<String, Object> getInitRecordValuesMap() {
		return initRecordValuesMap;
	}

	/**
	 * @param initRecordValuesMap the initRecordValuesMap to set
	 */
	public void setInitRecordValuesMap(Map<String, Object> initRecordValuesMap) {
		this.initRecordValuesMap = initRecordValuesMap;
	}
}; 

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
package net.mystrobe.client.ui.config;

public interface IMyStrobeSettings {

	/**
	 * Form components builder class name.
	 * 
	 * Specify different builder than default mystrobe should be used to create form components
	 * 
	 * @return Builder class name
	 */
	public String getDynamicFormComponentsBuilderClassName();
	
	/**
	 * Highlight table selected rows.
	 * 
	 * @return true/false.
	 */
	public boolean isSelectedTableRowCssEnabled();
	
	/**
	 * Css style sheet definition to use for table selected row if enabled
	 * 
	 * @return Css style sheet name
	 */
	public String getSelectedTableRowCssSelector();
	
	/**
	 * Number of visible navigation pages.
	 * 
	 * Property is used by the navigator component. 
	 * 
	 * @return true/false.
	 */
	public int getPageableNavigatorVisiblePagesCount();
	
	/**
	 * Grid view and navigator page size.
	 * 
	 * Property is used by the gird and navigator components. 
	 * 
	 * @return true/false.
	 */
	public int getPageSize();
	
	/**
	 * When no page size is set for navigators use linked data object batch size. 
	 * 
	 * @return true/false.
	 */
	public boolean getUseUIComponentSizeForDataObjectBatchSize();
	
}

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
 package net.mystrobe.client.dynamic.config;

/**
 * 
 * Dynamic form/table fields/columns configuration properties constants.<br/>
 * 
 * @author TVH Group NV.
 */
public class IDynamicFormFieldConfig {
	
	/**
	 * Form/table field/column properties. 
	 * 
	 * @author TVH Group NV
	 */
	public enum Property {
		Name("name"),
		Label("label"),
		
		// Text field, radio,  others ..		
		Type("type"),
		Required("required"),
		Visible("visible"),
		Width("width"),
		Format("format"),
		Sortable("sortable"),
		ReadOnly("readOnly"),
		
		//Page display sort order value of input field		
		SortValue("sortValue"),
		
		//Possible list of values - Can be used for radio or drop down
		ValuesList("valuesList"),
		
		SelectableFieldValue("SelectableFieldValue"),
		
		SelectableModalWindowPanelClass("SelectableModalWindowPanelClass"),
		
		LinkedDataObject("autoCompleteDataObject"),

		LinkedColumnName("autoCompleteColumnName"),
		
		VisibleColumnName("visibleColumnName"),
		
		DescriptionColumn("descriptionColumn"),

		SelectRecordTableConfig("selectRecordTableConfig");
		
		private String name;
		
		private Property (String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	/**
	 * Form field types 
	 * 
	 * 
	 * @author TVH Group NV
	 *
	 */
	public enum FieldType {
		TextField("textField"),
		AutoCompleteTextField("autoCompleteTextField"),
		CheckBox("checkBox"),
		Radio("radio"),
		TextArea("textarea"),
		DropDown("dropDown"),
		DateField("dateField");
		
		private String name;
		
		private FieldType (String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}

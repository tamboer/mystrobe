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
 package net.mystrobe.client;

import java.io.Serializable;


/**
 * Represents a single filtration rule that will be applied on the backend data
 * object.
 *
 * @author TVH Group NV
 */
public interface IFilterParameter {


	/**
	 * Returns the column on which the rule will be applied.
	 */
	public String getColumn();

	/**
	 * Returns the operator that will be applied between the column and the value.
	 */
	public FilterOperator getOperator();

	/**
	 * Returns the the value for which the operation will be executed
	 */
	public Serializable getValue();

	/**
	 * Setter for the column on which the rule should be applied.
	 * 
	 * @param column    the column on which the rule should be applied.
	 */
	public void setColumn(String column);

	/**
	 * Setter for the operator that will be applied between the column and the value.
	 * 
	 * @param filterOperator    the operator that will be applied between the column
	 * and the value.
	 */
	public void setOperator(FilterOperator filterOperator);

	/**
	 * Setter for the value for which the operation will be executed
	 * 
	 * @param filterValue
	 */
	public void setValue(Serializable filterValue);
	
	/**
	 * Filter value format.
	 * 
	 * Can be used to specify formatting options. 
	 */
	public void setFormat(String format);

	/**
	 * Retrieve filter format value.
	 * 
	 */
	public String getFormat();
	
	/**
	 * Flag on whether filter should be removed when buffer repositioning occurs.
	 * 
	 * @return flag seting.
	 */
	public boolean isRemoveOnBufferRecordReposition();

	public void setRemoveOnBufferRecordReposition(boolean clearOnBufferRecordReposition);

}
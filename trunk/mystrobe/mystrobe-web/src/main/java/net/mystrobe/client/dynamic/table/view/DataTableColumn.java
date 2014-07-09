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

import java.io.Serializable;

/**
 * Data table column info class. 
 * 
 * Contains column display data and position.
 * Used to specify additional column to be included in the data table.
 *
 * @author TVH Group NV
 */
public class DataTableColumn <T,S> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Column position
	 */
	private int columnPosition;
	
	/**
	 * Column render info.
	 */
	private IMyStrobeColumn<T,S> columnInfo;

	public DataTableColumn(int columnPosition,
			IMyStrobeColumn<T,S> columnInfo) {
		this.columnPosition = columnPosition;
		this.columnInfo = columnInfo;
	}

	/**
	 * @return the columnPosition
	 */
	public int getColumnPosition() {
		return columnPosition;
	}

	/**
	 * @return the columnInfo
	 */
	public IMyStrobeColumn<T,S> getColumnInfo() {
		return columnInfo;
	}
}
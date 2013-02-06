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
 * Simple class to transmit and mark the curently sorted column and the order of
 * the sort for that column.
 * 
 * @author TVH Group NV
 */
public class SortState implements Serializable {
	
	public enum Sort {
		  Ascending
		, Descending
		, None
	}

	private static final long serialVersionUID = 1L;
	
	protected String column;
    protected Sort sortOrder = Sort.Ascending;

    /**
     * Standard constructor
     */
    public SortState() {

    }

    public SortState( String sortColumn, Sort sortOrder ) {
        this.column = sortColumn;
        this.sortOrder = sortOrder;
    }

    public SortState( Object sortColumn, Sort sortOrder ) {
    	this( sortColumn.toString(), sortOrder);
    }
    
    /**
     * Returns the column on which the data resulted from the request should be sorted.
     */
    public String getSortColumn() {
        return this.column;
    }

    /**
     * Returns the sort order of the sorted column
     */
    public Sort getSortOrder() {
        return this.sortOrder;
    }

    /**
     * Sets the column on which the data resulted from the request should be sorted.
     *
     * @param sortColumn
     */
    public void setSortColumn(String sortColumn) {
        this.column = sortColumn;
    }

    /**
     * Setter for the sort order of the sorted column
     *
     * @param sortOrder    The sort order of the sorted column: Ascending, Descending
     */
    public void setSortOrder(Sort sortOrder) {
        this.sortOrder = sortOrder;
    }

	@Override
	public String toString() {
		return "SortState [column=" + column + ", sortOrder=" + sortOrder + "]";
	}
}

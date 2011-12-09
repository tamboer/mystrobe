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

import net.mystrobe.client.SortState.Sort;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;



/**
 * @author TVH Group NV
 */
public class SortSource implements ISortSource, ISortState{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ISortListener sortListener = null;

    protected SortState sortState = null;
    

    public ISortListener getSortListener() {
        if( sortListener != null ) return sortListener;
        throw new IllegalStateException( "The sort listener has now sort state (getSortListener().getSortState())");
    }

    
    public void setSortListener(ISortListener dataObject) {
        if( dataObject == null ) throw 	new IllegalArgumentException("argument [dataObject] cannot be null");
        this.sortListener = dataObject;
    }

    
    public void setPropertySortOrder(String column, SortOrder order) {
        if( column == null ) throw 	new IllegalArgumentException("argument [column] cannot be null");
        if( SortOrder.NONE.equals(order) ) return;

        if( sortState == null ) {
            sortState = getSortListener().getSortState();
            if( sortState == null ) throw new IllegalStateException( "The sort listener has now sort state (getSortListener().getSortState())");
        }
        sortState.setSortColumn(column);
        sortState.setSortOrder( order == SortOrder.ASCENDING ? Sort.Ascending : Sort.Descending );

        getSortListener().setSortState(sortState);
    }

    
    
    public SortOrder getPropertySortOrder(String column) {
        if( column == null ) return SortOrder.NONE;
        
        if( sortState == null ) {
            sortState = getSortListener().getSortState();
            if( sortState == null ) throw new IllegalStateException( "The sort listener has now sort state (getSortListener().getSortState())");
        }

        if( column.equals(sortState.getSortColumn() )) {
            return ( Sort.Ascending.equals( sortState.getSortOrder() ) ? SortOrder.ASCENDING : SortOrder.DESCENDING );
        }

        return SortOrder.NONE;
    }

}

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
 package net.mystrobe.client.connector.quarixbackend.json;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.FilterOperator;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.impl.FilterParameter;

import org.codehaus.jackson.annotate.JsonIgnore;



/**
 * @author TVH Group NV
 */
public class Info implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean hasFirstRow;

	private boolean hasLastRow;

	private String newPosition;

	private boolean changesOnly;
	
	private Set<IFilterParameter> filters;
	
	private List<String> sort;
	
	private Set<ResponseOption> options;
	
	public boolean hasFirstRow() {
		return hasFirstRow;
	}

	public boolean hasLastRow() {
		return hasLastRow;
	}

	public String getNewPosition() {
		return newPosition;
	}

	public boolean changesOnly() {
		return changesOnly;
	}

    public boolean isChangesOnly() {
        return changesOnly;
    }

    public void setChangesOnly(boolean changesOnly) {
        this.changesOnly = changesOnly;
    }

    public boolean isHasFirstRow() {
        return hasFirstRow;
    }

    public void setHasFirstRow(boolean hasFirstRow) {
        this.hasFirstRow = hasFirstRow;
    }

    public boolean isHasLastRow() {
        return hasLastRow;
    }

    public void setHasLastRow(boolean hasLastRow) {
        this.hasLastRow = hasLastRow;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }

    @JsonIgnore
    public void setFilter(List<String> args) {
        if( args == null ) this.filters = null;
        int len = args.size()/3;
        IFilterParameter filterParam;
        this.filters = new HashSet<IFilterParameter>();
        for(int cnt =0; cnt < len ; cnt++ ) {
            filterParam = new FilterParameter(args.get(cnt+1), FilterOperator.valueOf(args.get(cnt).toUpperCase()), args.get(cnt+2));
            this.filters.add(filterParam);
        }       
    }

    /**
     * @param filters the filters to set
     */  
    @JsonIgnore
    public void setFilters(Set<IFilterParameter> filters) {
        this.filters = filters;
    }

    /**
     * @return the filters
     */
    public Set<IFilterParameter> getFilters() {
        return filters;
    }

	/**
	 * @return the sort
	 */
	public List<String> getSort() {
		return sort;
	}

	/**
	 * @param sort the sort to set
	 */
	public void setSort(List<String> sort) {
		this.sort = sort;
	}
    
	/**
	 * @return the options
	 */
	public Set<ResponseOption> getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(Set<ResponseOption> options) {
		this.options = options;
	}
}
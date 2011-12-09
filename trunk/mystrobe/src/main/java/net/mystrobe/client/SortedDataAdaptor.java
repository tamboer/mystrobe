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
 * Extends the {@link DataSourceAdaptor} and adds sorting capabilities. 
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type.
 */
public abstract class SortedDataAdaptor<T extends IDataBean> extends DataSourceAdaptor<T> implements ISortListener, Serializable {
	
	private static final long serialVersionUID = -6700019706255565604L;
	
	protected SortState            sortState         = new SortState();
	protected ISortSource          sortSource        = null;
	
	public ISortSource getSortSource() {
        return this.sortSource;
    }

	public SortState getSortState() {
        return this.sortState;
    }

	public void setSortSource(ISortSource dataBrowse) {
		this.sortSource = dataBrowse;
    }

	public void setSortState(SortState sortState) {
		
		getLog().debug("Update sort state:" + sortState);
		
        this.sortState = sortState;
        
        if (this.lastDAORequest == null) {
        	createNewDAORequest(null, null);
        }
        
        this.lastDAORequest.setSortState(sortState);
    }
	
	public void applySortState(SortState sortState) {
		setSortState(sortState);
		resetDataBuffer();
	}
}

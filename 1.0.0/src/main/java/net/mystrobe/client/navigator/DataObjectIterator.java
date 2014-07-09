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
 package net.mystrobe.client.navigator;


import java.util.Iterator;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataObject;
import net.mystrobe.client.connector.transaction.WicketDSBLException;

import org.apache.wicket.WicketRuntimeException;


/**
 * @author TVH Group NV
 */
public class DataObjectIterator<T extends IDataBean> implements Iterator<T> {

	
	protected IDataObject<T> dao; 
	protected long cnt = 0;
	
	/**
	 * 
	 */
	public DataObjectIterator(IDataObject<T> dataObject) {
		dao  = dataObject;
	}

	public boolean hasNext() {
		switch ( dao.getCursorState() ) {
		
		case NoRecordAvailable: 		
		case LastRecord:
			return false;
			
			
		case OnlyRecordAvailable:
			return cnt == 0;
		}
		
		return true;

	}

	public T next() {
		if (cnt == 0)
			dao.fetchFirst();
		else
			dao.fetchNext();
		cnt++;
		return dao.getData();
	}

	public void remove() {
		try {
			dao.deleteData();
		} catch (WicketDSBLException e) {
			throw new WicketRuntimeException("Can not delete data.", e);
		}		
	}	
	
}

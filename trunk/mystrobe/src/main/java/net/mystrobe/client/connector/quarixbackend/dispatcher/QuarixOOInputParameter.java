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
 package net.mystrobe.client.connector.quarixbackend.dispatcher;

import java.sql.SQLException;
import java.util.List;

import net.mystrobe.client.connector.quarixbackend.api.DispatcherParameter;
import net.mystrobe.client.connector.quarixbackend.api.IDispatcherRequestParameters;

import com.progress.open4gl.ProBlob;

/**
 * @author TVH Group NV
 */
public class QuarixOOInputParameter extends AbstractInputParameters {

	protected List<DispatcherParameter> params = null;
	
	public QuarixOOInputParameter (IDispatcherRequestParameters reqParams) {
		this.params = reqParams.getAll();
		this.length = this.params.size();
	}
	
	
	/* (non-Javadoc)
	 * @see net.quarix.AbstractInputParameters#getObject(int)
	 */
	@Override
	public Object getObject(int position) throws SQLException {
		DispatcherParameter paramLine = params.get(this.currentRow);
	     switch (position) {
	     	case QuarixOODispatcher.REQ_TYPE_POS: return Integer.valueOf(paramLine.ParameterType); 
        	case QuarixOODispatcher.REQ_NAME_POS: return paramLine.ParameterName;
        	case QuarixOODispatcher.REQ_VALUE_POS:
        		if( paramLine.ParameterValue != null ) return new ProBlob(paramLine.ParameterValue.getBytes());
        		else return null;
        	default: return  null;            
        }
	}

//    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	
}

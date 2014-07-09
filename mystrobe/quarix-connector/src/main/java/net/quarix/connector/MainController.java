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
 package net.quarix.connector;

import com.progress.open4gl.dynamicapi.ResultSetMetaData;
import com.progress.open4gl.dynamicapi.ResultSetSchema;
import com.progress.open4gl.dynamicapi.Session;
import com.progress.open4gl.dynamicapi.PersistentProc;
import com.progress.open4gl.dynamicapi.ParameterSet;
import com.progress.open4gl.javaproxy.*;
import com.progress.open4gl.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.ResultSet;

//
// MainController
//
public class MainController implements SDOFactory
{

    protected MainControllerImpl m_MainControllerImpl;

    //---- Constructor
    public MainController(ProObject appObj) throws Open4GLException
    {
        m_MainControllerImpl = new MainControllerImpl(appObj);
    }

    public void _release() throws Open4GLException, SystemErrorException
    {
        m_MainControllerImpl._release();
    }

    //---- Get Connection Id
    public Object _getConnectionId() throws Open4GLException
    {
        return (m_MainControllerImpl._getConnectionId());
    }

    //---- Get Request Id
    public Object _getRequestId() throws Open4GLException
    {
        return (m_MainControllerImpl._getRequestId());
    }

    //---- Get SSL Subject Name
    public Object _getSSLSubjectName() throws Open4GLException
    {
        return (m_MainControllerImpl._getSSLSubjectName());
    }

    //---- Is there an open output temp-table?
    public boolean _isStreaming() throws Open4GLException
    {
        return (m_MainControllerImpl._isStreaming());
    }

    //---- Stop any outstanding request from any object that shares this connection.
    public void _cancelAllRequests() throws Open4GLException
    {
        m_MainControllerImpl._cancelAllRequests();
    }

    //---- Return the last Return-Value from a Progress procedure
    public String _getProcReturnString() throws Open4GLException
    {
        return (m_MainControllerImpl._getProcReturnString());
    }

    //---- Create an SDO ResultSet object - There are 3 overloaded variations
    public SDOResultSet _createSDOResultSet(String procName)
        throws Open4GLException, ProSQLException
    {
        return (m_MainControllerImpl._createSDOResultSet(procName, null, null, null));
    }

    public SDOResultSet _createSDOResultSet(String procName,
                                            String whereClause,String sortBy)
        throws Open4GLException, ProSQLException
    {
        return (m_MainControllerImpl._createSDOResultSet(procName, whereClause, sortBy, null));
    }

    public SDOResultSet _createSDOResultSet(String procName,
                                          String whereClause,
                                          String sortBy,
                                          SDOParameters params)
        throws Open4GLException, ProSQLException
    {
        return (m_MainControllerImpl._createSDOResultSet(procName, whereClause, sortBy, params));
    }

    // Create the ProcObject that knows how to talk to SDO's.
    public SDOInterface _createSDOProcObject(String procName)
        throws Open4GLException
    {
        return (m_MainControllerImpl._createSDOProcObject(procName));
    }

	/**
	*	
	*	
	*	Schema of input result set; Parameter 1
	*		Field:fieldType integer (java.lang.Integer)
	*		Field:fieldName character (java.lang.String)
	*		Field:fieldValue blob (java.sql.Blob)
	*	Schema of output result set; Parameter 3
	*		Field:fieldType integer (java.lang.Integer)
	*		Field:fieldName character (java.lang.String)
	*		Field:fieldValue character (java.lang.String)
	*/
	public void processRequest(java.sql.ResultSet ttRequest, Memptr pstrRequest, ResultSetHolder ttResponse, MemptrHolder pstrResponse)
		throws Open4GLException, RunTime4GLException, SystemErrorException
	{
		m_MainControllerImpl.processRequest( ttRequest,  pstrRequest,  ttResponse,  pstrResponse);
	}



}

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

import com.progress.open4gl.*;
import com.progress.common.ehnlog.IAppLogger;
import com.progress.common.ehnlog.LogUtils;
import com.progress.open4gl.dynamicapi.IPoolProps;
import com.progress.open4gl.javaproxy.Connection;
import com.progress.message.jcMsg;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.ResultSet;
import java.io.IOException;

//
// QuarixProgressOOConnector
//
/**
*    
*
*    @author TVH Group NV
*    @version 0.1
*/
public class QuarixProgressOOConnector implements SDOFactory
{
    // "This proxy version is not compatible with the current
    // version of the dynamic API."
    protected static final long m_wrongProxyVer = jcMsg.jcMSG079;

    private   static final int  PROXY_VER = 5;

    protected QuarixProgressOOConnectorImpl m_QuarixProgressOOConnectorImpl;

    //---- Constructors
    public QuarixProgressOOConnector(Connection connection)
        throws Open4GLException,
               ConnectException,
               SystemErrorException,
               IOException
    {
        /* we must do this here before we attempt to create the appobject */
        if (RunTimeProperties.getDynamicApiVersion() != PROXY_VER)
            throw new Open4GLException(m_wrongProxyVer, null);

        String urlString = connection.getUrl();
        if (urlString == null || urlString.compareTo("") == 0)
            connection.setUrl("net.quarix.connector.QuarixProgressOOCnnector");

        m_QuarixProgressOOConnectorImpl = new QuarixProgressOOConnectorImpl(
                                  "net.quarix.connector.QuarixProgressOOCnnector",
                                  connection,
                                  RunTimeProperties.tracer);
    }

    public QuarixProgressOOConnector(String urlString,
                        String userId,
                        String password,
                        String appServerInfo)
        throws Open4GLException,
               ConnectException,
               SystemErrorException,
               IOException
    {
        Connection connection;

        /* we must do this here before we attempt to create the appobject */
        if (RunTimeProperties.getDynamicApiVersion() != PROXY_VER)
            throw new Open4GLException(m_wrongProxyVer, null);

        connection = new Connection(urlString,
                                    userId,
                                    password,
                                    appServerInfo);

        m_QuarixProgressOOConnectorImpl = new QuarixProgressOOConnectorImpl(
                                  "net.quarix.connector.QuarixProgressOOCnnector",
                                  connection,
                                  RunTimeProperties.tracer);

        /* release the connection since the connection object */
        /* is being destroyed.  the user can't do this        */
        connection.releaseConnection();
    }

    public QuarixProgressOOConnector(String userId,
                        String password,
                        String appServerInfo)
        throws Open4GLException,
               ConnectException,
               SystemErrorException,
               IOException
    {
        Connection connection;

        /* we must do this here before we attempt to create the appobject */
        if (RunTimeProperties.getDynamicApiVersion() != PROXY_VER)
            throw new Open4GLException(m_wrongProxyVer, null);

        connection = new Connection("net.quarix.connector.QuarixProgressOOCnnector",
                                    userId,
                                    password,
                                    appServerInfo);

        m_QuarixProgressOOConnectorImpl = new QuarixProgressOOConnectorImpl(
                                  "net.quarix.connector.QuarixProgressOOCnnector",
                                  connection,
                                  RunTimeProperties.tracer);

        /* release the connection since the connection object */
        /* is being destroyed.  the user can't do this        */
        connection.releaseConnection();
    }

    public QuarixProgressOOConnector()
        throws Open4GLException,
               ConnectException,
               SystemErrorException,
               IOException
    {
        Connection connection;

        /* we must do this here before we attempt to create the appobject */
        if (RunTimeProperties.getDynamicApiVersion() != PROXY_VER)
            throw new Open4GLException(m_wrongProxyVer, null);

        connection = new Connection("net.quarix.connector.QuarixProgressOOCnnector",
                                    null,
                                    null,
                                    null);

        m_QuarixProgressOOConnectorImpl = new QuarixProgressOOConnectorImpl(
                                  "net.quarix.connector.QuarixProgressOOCnnector",
                                  connection,
                                  RunTimeProperties.tracer);

        /* release the connection since the connection object */
        /* is being destroyed.  the user can't do this        */
        connection.releaseConnection();
    }

    public void _release() throws Open4GLException, SystemErrorException
    {
        m_QuarixProgressOOConnectorImpl._release();
    }

    //---- Get Connection Id
    public Object _getConnectionId() throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._getConnectionId());
    }

    //---- Get Request Id
    public Object _getRequestId() throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._getRequestId());
    }

    //---- Get SSL Subject Name
    public Object _getSSLSubjectName() throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._getSSLSubjectName());
    }

    //---- Is there an open output temp-table?
    public boolean _isStreaming() throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._isStreaming());
    }

    //---- Stop any outstanding request from any object that shares this connection.
    public void _cancelAllRequests() throws Open4GLException
    {
        m_QuarixProgressOOConnectorImpl._cancelAllRequests();
    }

    //---- Return the last Return-Value from a Progress procedure
    public String _getProcReturnString() throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._getProcReturnString());
    }

    //---- Create an SDO ResultSet object - There are 3 overloaded variations
    public SDOResultSet _createSDOResultSet(String procName)
        throws Open4GLException, ProSQLException
    {
        return (m_QuarixProgressOOConnectorImpl._createSDOResultSet(procName, null, null, null));
    }

    public SDOResultSet _createSDOResultSet(String procName,
                                            String whereClause,String sortBy)
        throws Open4GLException, ProSQLException
    {
        return (m_QuarixProgressOOConnectorImpl._createSDOResultSet(procName, whereClause, sortBy, null));
    }

    public SDOResultSet _createSDOResultSet(String procName,
                                          String whereClause,
                                          String sortBy,
                                          SDOParameters params)
        throws Open4GLException, ProSQLException
    {
        return (m_QuarixProgressOOConnectorImpl._createSDOResultSet(procName, whereClause, sortBy, params));
    }

    // Create the ProcObject that knows how to talk to SDO's.
    public SDOInterface _createSDOProcObject(String procName)
        throws Open4GLException
    {
        return (m_QuarixProgressOOConnectorImpl._createSDOProcObject(procName));
    }

	/**
	*	
	*	%TblDesc%
	*/
	public MainController createAO_MainController()
		throws Open4GLException, RunTime4GLException, SystemErrorException
	{
		return new MainController(m_QuarixProgressOOConnectorImpl);
	}



}

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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

import net.mystrobe.client.connector.quarixbackend.Globals;
import net.mystrobe.client.connector.quarixbackend.api.LogEnabled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progress.open4gl.InputResultSet;

/**
 *
 * @author TVH Group NV
 * @version $Revision: 1.4 $ $Date: 2009/01/13 06:12:04 $
 */
public abstract class AbstractInputParameters extends InputResultSet implements LogEnabled {

    public static final int REQ_TYPE_INPUT = 1;
    public static final int REQ_TYPE_HEADERS = 2;
    public static final int REQ_TYPE_CGI = 3;
    public static final int REQ_TYPE_SESSION = 4;
    
    
    protected int length = 0;
    protected int currentRow = -1;
    protected Logger log = LoggerFactory.getLogger(AbstractInputParameters.class);
    
    
    /**
     * 
     */
    public AbstractInputParameters() {
        super();
    }    
    

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#getObject(int)
     */
    public abstract Object getObject(int position) throws SQLException;
    

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#next()
     */
    public boolean next() throws SQLException {
        currentRow++;
        return currentRow < length;
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#getURL(int)
     */
    public URL getURL(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#getURL(java.lang.String)
     */
    public URL getURL(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateArray(int, java.sql.Array)
     */
    public void updateArray(int arg0, Array arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateArray(java.lang.String, java.sql.Array)
     */
    public void updateArray(String arg0, Array arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateAsciiStream(int, java.io.InputStream, int)
     */
    public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBigDecimal(int, java.math.BigDecimal)
     */
    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBinaryStream(int, java.io.InputStream, int)
     */
    public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
     */
    public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBlob(int, java.sql.Blob)
     */
    public void updateBlob(int arg0, Blob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBlob(java.lang.String, java.sql.Blob)
     */
    public void updateBlob(String arg0, Blob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBoolean(int, boolean)
     */
    public void updateBoolean(int arg0, boolean arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBoolean(java.lang.String, boolean)
     */
    public void updateBoolean(String arg0, boolean arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateByte(int, byte)
     */
    public void updateByte(int arg0, byte arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateByte(java.lang.String, byte)
     */
    public void updateByte(String arg0, byte arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBytes(int, byte[])
     */
    public void updateBytes(int arg0, byte[] arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateBytes(java.lang.String, byte[])
     */
    public void updateBytes(String arg0, byte[] arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateCharacterStream(int, java.io.Reader, int)
     */
    public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
     */
    public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateClob(int, java.sql.Clob)
     */
    public void updateClob(int arg0, Clob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateClob(java.lang.String, java.sql.Clob)
     */
    public void updateClob(String arg0, Clob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateDate(int, java.sql.Date)
     */
    public void updateDate(int arg0, Date arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateDate(java.lang.String, java.sql.Date)
     */
    public void updateDate(String arg0, Date arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateDouble(int, double)
     */
    public void updateDouble(int arg0, double arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateDouble(java.lang.String, double)
     */
    public void updateDouble(String arg0, double arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateFloat(int, float)
     */
    public void updateFloat(int arg0, float arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateFloat(java.lang.String, float)
     */
    public void updateFloat(String arg0, float arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateInt(int, int)
     */
    public void updateInt(int arg0, int arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateInt(java.lang.String, int)
     */
    public void updateInt(String arg0, int arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateLong(int, long)
     */
    public void updateLong(int arg0, long arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateLong(java.lang.String, long)
     */
    public void updateLong(String arg0, long arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateNull(int)
     */
    public void updateNull(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateNull(java.lang.String)
     */
    public void updateNull(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateObject(int, java.lang.Object, int)
     */
    public void updateObject(int arg0, Object arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateObject(int, java.lang.Object)
     */
    public void updateObject(int arg0, Object arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateObject(java.lang.String, java.lang.Object, int)
     */
    public void updateObject(String arg0, Object arg1, int arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateObject(java.lang.String, java.lang.Object)
     */
    public void updateObject(String arg0, Object arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateRef(int, java.sql.Ref)
     */
    public void updateRef(int arg0, Ref arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    /* (non-Javadoc)
     * @see com.progress.open4gl.InputResultSet#updateRef(java.lang.String, java.sql.Ref)
     */
    public void updateRef(String arg0, Ref arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public int getHoldability() throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public NClob getNClob(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public NClob getNClob(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public String getNString(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public String getNString(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public RowId getRowId(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public RowId getRowId(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public SQLXML getSQLXML(int arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public SQLXML getSQLXML(String arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public boolean isClosed() throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateClob(int arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateClob(String arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(int arg0, NClob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(String arg0, NClob arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(int arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(String arg0, Reader arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNString(int arg0, String arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateNString(String arg0, String arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateRowId(int arg0, RowId arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateRowId(String arg0, RowId arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }

    public <T> T unwrap(Class<T> arg0) throws SQLException {
        throw new IllegalAccessError("Not Implemented");
    }
        

	/* (non-Javadoc)
	 * @see net.quarix.api.LogEnabled#getLog()
	 */
	public Logger getLog() {
		return log;
	}


	/* (non-Javadoc)
	 * @see net.quarix.api.LogEnabled#setLog(org.apache.commons.logging.Log)
	 */
	public void setLog(Logger log) {
		this.log = log;		
	}    
}

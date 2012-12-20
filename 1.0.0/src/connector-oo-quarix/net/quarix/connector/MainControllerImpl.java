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

import com.progress.open4gl.Memptr;
import com.progress.open4gl.MemptrHolder;
import com.progress.open4gl.Open4GLException;
import com.progress.open4gl.ResultSetHolder;
import com.progress.open4gl.RunTime4GLException;
import com.progress.open4gl.SystemErrorException;
import com.progress.open4gl.dynamicapi.MetaSchema;
import com.progress.open4gl.dynamicapi.ParameterSet;
import com.progress.open4gl.dynamicapi.ResultSet;
import com.progress.open4gl.dynamicapi.ResultSetMetaData;
import com.progress.open4gl.dynamicapi.RqContext;
import com.progress.open4gl.javaproxy.ProObject;
import com.progress.open4gl.javaproxy.SubAppObject;

public final class MainControllerImpl extends SubAppObject
{

    public MainControllerImpl(ProObject proobject)
        throws Open4GLException
    {
        super(proobject);
    }

    public void processRequest(java.sql.ResultSet resultset, Memptr memptr, ResultSetHolder resultsetholder, MemptrHolder memptrholder)
        throws Open4GLException, RunTime4GLException, SystemErrorException
    {
        RqContext rqcontext = null;
        Object obj = null;
        if(!isSessionAvailable())
            throw new Open4GLException(0x6a63000000001c5cL, null);
        ParameterSet parameterset = new ParameterSet(4);
        parameterset.setResultSetParameter(1, resultset, 1);
        parameterset.setMemptrParameter(2, memptr, 1);
        parameterset.setResultSetParameter(3, null, 2);
        parameterset.setMemptrParameter(4, null, 2);
        MetaSchema metaschema = new MetaSchema();
        metaschema.addResultSetSchema(processRequest_MetaData1, 1, 1);
        metaschema.addResultSetSchema(processRequest_MetaData2, 3, 2);
        rqcontext = runProcedure("com/quarix/bin/dispatcher.p", parameterset, metaschema);
        Object obj1 = parameterset.getOutputParameter(3);
        resultsetholder.setValue(obj1);
        obj1 = parameterset.getOutputParameter(4);
        memptrholder.setValue(obj1);
        if(rqcontext != null)
            if(!rqcontext._isStreaming())
            {
                rqcontext._release();
            } else
            {
                ResultSet resultset1 = (ResultSet)resultsetholder.getResultSetValue();
                if(resultset1 != null)
                    resultset1.setRqContext(rqcontext);
            }
    }

    static ResultSetMetaData processRequest_MetaData1;
    static ResultSetMetaData processRequest_MetaData2;

    static 
    {
        processRequest_MetaData1 = new ResultSetMetaData(0, 3);
        processRequest_MetaData1.setFieldDesc(1, "fieldType", 0, 4);
        processRequest_MetaData1.setFieldDesc(2, "fieldName", 0, 1);
        processRequest_MetaData1.setFieldDesc(3, "fieldValue", 0, 18);
        processRequest_MetaData2 = new ResultSetMetaData(0, 3);
        processRequest_MetaData2.setFieldDesc(1, "fieldType", 0, 4);
        processRequest_MetaData2.setFieldDesc(2, "fieldName", 0, 1);
        processRequest_MetaData2.setFieldDesc(3, "fieldValue", 0, 1);
    }
}
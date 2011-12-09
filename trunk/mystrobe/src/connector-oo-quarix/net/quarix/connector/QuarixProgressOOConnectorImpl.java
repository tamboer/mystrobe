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

import com.progress.common.ehnlog.IAppLogger;
import com.progress.open4gl.*;
import com.progress.open4gl.dynamicapi.IPoolProps;
import com.progress.open4gl.javaproxy.AppObject;

public final class QuarixProgressOOConnectorImpl extends AppObject
{

    public QuarixProgressOOConnectorImpl(String s, IPoolProps ipoolprops, IAppLogger iapplogger)
        throws Open4GLException, ConnectException, SystemErrorException
    {
        super(s, ipoolprops, iapplogger, null);
    }
}
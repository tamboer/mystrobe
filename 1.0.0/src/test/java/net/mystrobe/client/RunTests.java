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


import net.mystrobe.client.connector.DAOSchemaTest;
import net.mystrobe.client.connector.DSSchemaTest;
import net.mystrobe.client.connector.DataObjectTest;
import net.mystrobe.client.connector.DataRequestTest;
import net.mystrobe.client.connector.FilterDataTest;
import net.mystrobe.client.connector.SortDataTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @author TVH Group NV
 */

@RunWith(Suite.class)
@Suite.SuiteClasses ( { //DataStoreTest.class
                         TestStringToJavaNativeUtil.class
                        , DataRequestTest.class
                        , DSSchemaTest.class
                        , DAOSchemaTest.class
                        , DataRequestTest.class
                        , DataObjectTest.class
                        , FilterDataTest.class
                        , SortDataTest.class
                        //, DAORequestCrudTest.class
                        //, CRUDDataObjectTest.class
                    } )
public class RunTests {


}
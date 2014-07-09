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


import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataSource;

/**
 * @author TVH Group NV
 */
public class TestPerson implements IDataBean {
    protected String      countryCode = "us";
    protected IDataSource dataSource  = null;
    protected String      firstName   = "";
    protected int         id          = -1;
    protected String      language    = "en";
    protected String      lastName    = "";

    public TestPerson() {}

    public TestPerson(int id, String firstName, String lastName, String language, String countryCode) {
        this.id          = id;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.language    = language;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public IDataSource getDataSource() {
        return this.dataSource;
    }

    public String getRowId() {
        return "" + getId();
    }

    public void detach() {}

	@Override
	public void setRowid(String rowId) {
		// TODO Auto-generated method stub
		
	}

   
}


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
 package net.mystrobe.client.connector;

import java.io.Serializable;

/**
 * @author TVH Group NV
 */
public class LocalizationProperties implements Cloneable, Serializable {


    private static final long serialVersionUID = 1899891432452219166L;
	
	protected String dateFormat = "mdy";
    protected String languageCode = "en";
    protected String logicalFormat = "true/false";
    protected char numericalDecimalPoint = '.';
    protected String numericalFormat = null;
    protected char numericalSeparator = ',';
    
    protected String formatDate = null;
    protected String formatDateTime = null;
    protected String formatDateTimeTz = null;
    

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLogicalFormat() {
        return logicalFormat;
    }

    public void setLogicalFormat(String logicalFormat) {
        this.logicalFormat = logicalFormat;
    }

    public char getNumericalDecimalPoint() {
        return numericalDecimalPoint;
    }

    public void setNumericalDecimalPoint(char numericalDecimalPoint) {
        this.numericalDecimalPoint = numericalDecimalPoint;
    }

    public String getNumericalFormat() {
        return numericalFormat;
    }

    public void setNumericalFormat(String numericalFormat) {
        this.numericalFormat = numericalFormat;
    }

    public char getNumericalSeparator() {
        return numericalSeparator;
    }

    public void setNumericalSeparator(char numericalSeparator) {
        this.numericalSeparator = numericalSeparator;
    }
    
    /**
	 * @return the formatDate
	 */
	public String getFormatDate() {
		return formatDate;
	}

	/**
	 * @param formatDate the formatDate to set
	 */
	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}

	/**
	 * @return the formatDateTime
	 */
	public String getFormatDateTime() {
		return formatDateTime;
	}

	/**
	 * @param formatDateTime the formatDateTime to set
	 */
	public void setFormatDateTime(String formatDateTime) {
		this.formatDateTime = formatDateTime;
	}

	/**
	 * @return the formatDateTimeTz
	 */
	public String getFormatDateTimeTz() {
		return formatDateTimeTz;
	}

	/**
	 * @param formatDateTimeTz the formatDateTimeTz to set
	 */
	public void setFormatDateTimeTz(String formatDateTimeTz) {
		this.formatDateTimeTz = formatDateTimeTz;
	}

	@Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    
}

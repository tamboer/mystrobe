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

import java.io.Serializable;



/**
 * Parameter class holding extra information about a Data link between two objects.
 * 
 * @author TVH Group NV
 */
public class DataLinkParameters implements ILinkParameters, Serializable {

    
    private static final long serialVersionUID = -1373853269521345651L;
	
	protected String mappedListenerColumnName = null;
    protected String mappedSourceColumnName = null;

    public DataLinkParameters( String mappedColumnName) {
        this.mappedSourceColumnName = mappedColumnName;
        this.mappedListenerColumnName = mappedColumnName;
    }


    public DataLinkParameters( String mappedSourceColumnName, String mappedListenerColumnName ) {
        this.mappedSourceColumnName = mappedSourceColumnName;
        this.mappedListenerColumnName = mappedListenerColumnName;
    }

    
    public String getMappedListenerColumnName() {
        return this.mappedListenerColumnName;
    }


    public String getMappedSourceColumnName() {
        return this.mappedSourceColumnName;
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((mappedListenerColumnName == null) ? 0
						: mappedListenerColumnName.hashCode());
		result = prime
				* result
				+ ((mappedSourceColumnName == null) ? 0
						: mappedSourceColumnName.hashCode());
		return result;
	}


	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataLinkParameters other = (DataLinkParameters) obj;
		if (mappedListenerColumnName == null) {
			if (other.mappedListenerColumnName != null)
				return false;
		} else if (!mappedListenerColumnName
				.equals(other.mappedListenerColumnName))
			return false;
		if (mappedSourceColumnName == null) {
			if (other.mappedSourceColumnName != null)
				return false;
		} else if (!mappedSourceColumnName.equals(other.mappedSourceColumnName))
			return false;
		return true;
	}

    
}

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
import java.util.Enumeration;

/**
 * Metadata about an data object / data store .
 *
 * @author TVH Group NV
 */
public interface IDSSchema extends Serializable {

	public String getId();
	/**
	 * How many rows to request at a time.
	 */
	public long getBatchSize();

	
	public Enumeration<String> getDataObjectIds();
	
	public int getDataObjectsCount();

	public IDAOSchema<?> getDataObjectSchema(String dataObjectId);
	
	/**
	 * When the current row or the top/bottom row  is this close to the begining/end
	 * of the batch, fetch the previous/next batch
	 */
	public int getMargin();

	public Enumeration<IDSRelation> getRelations();
	
	public String getURN();
	
	public boolean hasAtomicChanges();
	
	public boolean hasFilteredChildren();

    /**
     * Returns true if this schema is dynamicaly generated, false otherwise.
     * Could be used by a caching mechanism to decide if the schema can be cached or not.
     * @return true if this schema is dynamicaly generated, false otherwise
     */
    public boolean isDynamic();
    
    public boolean isReadOnly(); 
}
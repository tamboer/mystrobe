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
 package net.mystrobe.client.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mystrobe.client.IDAOSchema;
import net.mystrobe.client.IDSRelation;
import net.mystrobe.client.IDSSchema;


/**
 * @author TVH Group NV
 */
public abstract class DSSchema implements IDSSchema {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5611737103628595887L;
	
	protected String id;
	protected long batchSize;
	protected int margin;
	protected String urn;
	protected boolean hasAtomicChanges;
	protected boolean hasFilteredChildren;
	protected boolean isDynamic;
	protected boolean readOnly;
	
	protected transient Enumeration<String> dataObjectIds;
	protected Map<String, IDAOSchema<?>> daoMap = new HashMap<String, IDAOSchema<?>>();

	private transient Enumeration<IDSRelation> dsRelations;

	public long getBatchSize() {
		return batchSize;
	}

	public Enumeration<String> getDataObjectIds() {
		if( dataObjectIds != null ) return dataObjectIds;
		return dataObjectIds = Collections.enumeration(daoMap.keySet());
	}
	
	public int getDataObjectsCount() {
		return daoMap.size();
	}

	public IDAOSchema<?> getDataObjectSchema(String dataObjectId) {
		return daoMap.get(dataObjectId);
	}

	public String getId() {
		return id;
	}

	public int getMargin() {
		return margin;
	}

	public Enumeration<IDSRelation> getRelations() {
		return dsRelations;
	}

	public String getURN() {
		return urn;
	}

	public boolean hasAtomicChanges() {
		return hasAtomicChanges;
	}

	public boolean hasFilteredChildren() {
		return hasFilteredChildren;
	}

	public boolean isDynamic() {
		return isDynamic;
	}

	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	protected void loadRelations(String[] relations) {
		List<IDSRelation> list = new ArrayList<IDSRelation>();
		for (String relation : relations) {
			final String[] fields = relation.split(",");
			if (fields.length == 4) {
				list.add(new IDSRelation() {

					public String getParentDAOId() {
						return fields[3];
					}

					public String getParentColumnName() {
						return fields[2];
					}

					public String getChildDAOId() {
						return fields[1];
					}

					public String getChildColumnName() {
						return fields[0];
					}
				});
			}
		}
		dsRelations = Collections.enumeration(list);
	}
}
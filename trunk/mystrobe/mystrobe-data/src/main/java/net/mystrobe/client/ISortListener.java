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

import net.mystrobe.client.connector.messages.IConnectorResponseMessages;


/**
 * An entity whose records can be sorted by a sour source. Usually extended by an
 * IDataObject and implemented by a data object, the sort events are usually
 * published by a databrowse where the user can sort one column of data.
 * 
 * @author TVH Group NV
 */
public interface ISortListener extends LinkListener, IConnectorResponseMessages {


	/**
	 * Returns the sourt source that publishes the sort events to this entity.
	 */
	public ISortSource getSortSource();

	/**
	 * Returns the sort state that was last published to this entity, or null if there
	 * is none.
	 */
	public SortState getSortState();

	/**
	 * Sets the sort source that publishes the sort events to this entity.
	 * 
	 * @param dataBrowse    the sort source that publishes the sort events to this
	 * entity.
	 */
	public void setSortSource(ISortSource dataBrowse);

	/**
	 * Sets the sort state that was published to this entity, or null if there is none.
	 * 
	 * 
	 * @param sortState    the sort state that was published to this entity, or null
	 * if there is none.
	 */
	public void setSortState(SortState sortState);
	
	/**
	 * Sets the sort state that was published to this entity, or null if there is none.
	 * 
	 * Makes call to BL to refresh data according to new sort state.
	 * 
	 * @param sortState    the sort state that was published to this entity, or null
	 * if there is none.
	 */
	public void applySortState(SortState sortState);

}
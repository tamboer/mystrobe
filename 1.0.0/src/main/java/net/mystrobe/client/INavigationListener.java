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

import java.util.Collection;

import net.mystrobe.client.connector.messages.IConnectorResponseMessages;


/**
 * A navigation listener is an entity whose records can be navigated by an
 * navigation sour ce. Usually extended by an IDataStore and implemented by a data
 * object an is navigated by an navigation toolbar or data browse.
 * 
 * @author TVH Group NV
 */
public interface INavigationListener extends LinkListener, IStateSource, IConnectorResponseMessages {

	/**
	 * Add a navigation source to this listener.
	 * 
	 * Each navigation source that can navigate this listener has to be added first
	 * trough this method to the navigation listener.
	 * 
	 * @param navigationSource  The navigation source instance, usually a navigation
	 * 				tool bar or data browse.
	 */
	public void addNavigationSource(INavigationSource navigationSource);
	
	/**
	 * Remove navigation source from navigation listener.
	 * 
	 * @param navigationSource Navigation source to be removed from the listener.
	 */
	public void removeNavigationSource(INavigationSource navigationSource);

	/**
	 * Returns true if it manages to reposition the cursor to the first row in the
	 * data store, false otherwise.
	 */
	public boolean fetchFirst();

	/**
	 * Returns true if it manages to reposition the cursor to the last row in the data
	 * store, false otherwise.
	 */
	public boolean fetchLast();

	/**
	 * Returns true if it manages to reposition the cursor to the next row in the data
	 * store, false otherwise.
	 */
	public boolean fetchNext();

	/**
	 * Returns true if it manages to reposition the cursor to the previous row in the
	 * data store, false otherwise.
	 */
	public boolean fetchPrev();

	/**
	 * Returns  information about the positioning of the current record in the data
	 * source. Possible values are: 'NoRecordAvailable', 'OnlyRecordAvailable',
	 * 'FirstRecord', 'LastRecord', 'NotFirstOrLast'
	 */
	public CursorStates getCursorState();

	/**
	 * Returns a collection of the navigation sources (navigation toolgroups, data
	 * browsers) that can publish navigation events to this listener.
	 */
	public Collection<INavigationSource> getNavigationSources();
}
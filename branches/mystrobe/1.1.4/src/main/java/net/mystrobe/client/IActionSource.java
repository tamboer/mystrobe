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

/**
 * An action source is an entity that can publish state, action or value
 * notifications to a number of action listeners. It is the mechanism used for
 * instance to propagate value changes from fields (user entered values) to data
 * views, filter view or data objects, or the mechanisms trough which actions
 * performed by tool buttons or buttons are propagated to filter views or data
 * views.
 * 
 * @author TVH Group NV
 */
public interface IActionSource extends LinkSource {

	/**
	 * Setter for an action listener to be added to this source. The action source
	 * will publish action events to this listener.
	 * 
	 * @param actionListener    The action listener to be added to this source. The
	 * action source will publish action events to this listener.
	 */
	public void addActionListener(IActionListener actionListener);

	/**
	 * Returns all the action listener that this action source will publish action
	 * events to.
	 */
	public Collection<IActionListener> getActionListener();

	/**
	 * Removes an action listener from this action source.
	 * 
	 * @param actionListener    The action listener to be removed from this action
	 * source.
	 */
	public void removeActionListener(IActionListener actionListener);

}
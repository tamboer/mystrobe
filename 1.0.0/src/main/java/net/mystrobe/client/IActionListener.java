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
 * An action listener is an entity that can receive state, action or value
 * notifications from a number of action sources. It is the mechanism used for
 * instance to propagate value changes from fields (user entered values) to data
 * views, filter view or data objects, or the mechanisms trough which actions
 * performed by tool buttons or buttons are propagated to filter views or data
 * views.
 * 
 * @author TVH Group NV
 */
public interface IActionListener extends LinkListener {

	/**
	 * Event notifing this entity that an action has been performed (tool button was
	 * pressed, ...).
	 * 
	 * @param actionSource    The action source that published this event
	 * @param actionName    The action that has been performed or it is desired
	 */
    public void actionPerformed(IActionSource actionSource, String actionName);

	/**
	 * Setter for an action source to be added to this listener. The action source
	 * will publish action events to his listener.
	 * 
	 * @param actionSource    An action source to be added to this listener. The
	 * action source will publish action events to his listener.
	 */
	public void addActionSource(IActionSource actionSource);

	/**
	 * Event notifying this entity that an source has its value changed / modified.
	 * For example fields publish this event when their value changes or not to notify
	 * an data or filter view about the state of the values (are changes, should the
	 * save / cancel buttons be enabled, or in case there are no changes leave the
	 * save and cancel button disabled, the user can leave the transaction any time
	 * whitout consecvences).
	 * 
	 * @param actionSource    The source of this event.
	 * @param isModified    True if the value of the action source is altered, false
	 * otherwise.
	 */
	public void dataModified(IActionSource actionSource, boolean isModified);

	/**
	 * Returns a collection of an action sources that can publish action events to his
	 * listener.
	 */
	public Collection<IActionSource> getActionSource();

	/**
	 * Removes an action source that can publish action events to his listener.
	 * 
	 * @param actionSource    The action source that can publish action events to his
	 * listener.
	 */
	public void removeActionSource(IActionSource actionSource);

	/**
	 * Event notifying this entity that a source has its state changed / modified. For
	 * example a container can publis information about a new page beeing selected, or
	 * an data view about the state of the transaction beeing changed.
	 * 
	 * @param actionSource    The source of this event.
	 * @param stateName    The name of the state (ex: pageSelected, updateState)
	 * @param stateValue    The value of the state (ex: 0, UpdateBegin)
	 * @param oldStateValue    The old value of the state if aplicable, null otherwise.
	 */
	public void stateChanged(IActionSource actionSource, String stateName, String stateValue, String oldStateValue);

	/**
	 * Event notifying this entity that a source has its value changed / modified.
	 * 
	 * @param actionSource    The source of this event.
	 * @param newValue    The current value of this source.
	 * @param oldValue    The old value of this source
	 * @param columnName    A column name if it is associated with this source.
	 */
	public void valueChanged(IActionSource actionSource, Object newValue, Object oldValue, String columnName);

}
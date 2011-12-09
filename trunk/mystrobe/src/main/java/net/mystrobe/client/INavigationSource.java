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

/**
 * A navigation source is an entity that can publish navigation events (fetchFirst,
 * fetchNext, fetchPrev, fetchLast) to an navigation listener. Usually implemented
 * by an navigation toolbar or data browse.
 * 
 * @author TVH Group NV
 */
public interface INavigationSource extends LinkSource, IStateCallback {

	/**
	 * Returns the navigation listener this source publishes navigation events to.
	 */
	public INavigationListener getNavigationListener();

	/**
	 * Sets the navigation listener this source publishes navigation events to.
	 * 
	 * @param navigationListener    Sets the navigation listener this source publishes
	 * navigation events to.
	 */
	public void setNavigationListener(INavigationListener navigationListener);

}
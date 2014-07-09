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
 * A filter source is an entity that can apply filtering rules to a filter
 * listener. Usually extended by a filter view it collects data from fields and
 * applies them as filtration rules to a filter listener, usually implemented by a
 * data object.
 * 
 * @author TVH Group NV
 */
public interface IFilterSource extends LinkSource {


	/**
	 * Returns the filter listener to which this entity publishes filtration rules
	 */
	public IFilterListener getFilterListener();

	/**
	 * Sets the filter listener to which this entity publishes filtration rules
	 * 
	 * @param dataObject    the filter listener to which this entity publishes
	 * filtration rules
	 */
	public void setFilterListener(IFilterListener dataObject);

}
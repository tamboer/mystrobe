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
 * An interface describing a controller for the creation and application of
 * filtering rules to a  data object / filter listener.
 * 
 * @author TVH Group NV
 */
public interface IFilterView<T extends IDataBean> extends IDataListener<T>, IActionListener, IFilterSource {

    /**
	 * Collect all the filtering rules from its action sources (valueChanged) and
	 * apply them to the filter listener (data object)
	 */
	public void applyFilter();

	/**
	 * Clear / reset all the values from its action sources (valueChanged) and apply
	 * them to the filter listener (data object)
	 */
	public void clearFilter();

}
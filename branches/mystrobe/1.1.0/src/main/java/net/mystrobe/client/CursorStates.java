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
 * Enumeration of the cursor states indicated by a data source or navigation
 * listeners regarding the positioning in its records sets.  Used by the data
 * listener and navigation source to be notified about the new row state and
 * positioning.
 * 
 * @author TVH Group NV
 */
public enum CursorStates {
	/**
	 * Cursor state indicating that the data store has now rows (data) available so
	 * there is no row positioning. Usually the components are instructed to disable
	 * the navigation or transactions on existing records (update/delete).
	 */
	NoRecordAvailable,
	/**
	 * Cursor state indicating that the data store has only one record / row available.
	 * Usually navigation will be disabled in this situation.
	 */
	OnlyRecordAvailable,
	/**
	 * Cursor state indicating that the cursor / row selected is the first one in the
	 * data store.
	 */
	FirstRecord,
	/**
	 * Cursor state indicating that the cursor / row selected is the last one in the
	 * data store.
	 */
	LastRecord,
	/**
	 * Cursor state indicating that the cursor / row selected is somewhere in the
	 * middle of the data / rows  in the data store.
	 */
	NotFirstOrLast
}
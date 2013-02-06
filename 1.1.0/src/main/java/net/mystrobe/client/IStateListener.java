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
 * @author TVH Group NV
 */
public interface IStateListener {

    /**
	 * Event published after a new row is selected (navigation or initialization) in a
	 * data store containing the row state of the new selected row position. Possible
	 * values are: 'NoRecordAvailable', 'OnlyRecordAvailable', 'FirstRecord',
	 * 'LastRecord', 'NotFirstOrLast'
	 *
     * @param source
	 * @param cursorState    The row state of the new selected row position. Possible
	 * values are: 'NoRecordAvailable', 'OnlyRecordAvailable', 'FirstRecord',
	 * 'LastRecord', 'NotFirstOrLast'
	 */
    public void cursorState(IStateSource source, CursorStates cursorState);


    /**
     * Callback event updating the state of an CRUD operation
     *
     * @param source
     * @param updateState
     */
    public void updateState(IStateSource source, UpdateStates updateState);

}

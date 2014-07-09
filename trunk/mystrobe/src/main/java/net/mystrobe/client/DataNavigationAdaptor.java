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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.mystrobe.client.connector.DAOCommands;
import net.mystrobe.client.connector.IConfig;
import net.mystrobe.client.connector.DAORequest.StartRowMarker;
import net.mystrobe.client.connector.IAppConnector;


/**
 * Data navigation listener implementation.
 * 
 * @author TVH Group NV
 *
 * @param <T> Data bean type.
 */
public abstract class DataNavigationAdaptor<T extends IDataBean> extends DataBufferAdaptor<T> implements INavigationListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4265921764229187653L;
	
	
	/**
	 * Navigation source
	 */
	protected List<INavigationSource> navigationSources = new ArrayList<INavigationSource>();
	
	
	public DataNavigationAdaptor(IAppConnector appConnector) {
		super(appConnector);
	}
	
	public  DataNavigationAdaptor(IConfig config, String appName) {
		super(config, appName);
	}
	
	/**
	 * Invoke navigation sources state callback.
	 * 
	 * @param newCursorState Navigation cursor state.
	 */
	@Override
	protected void publishCursorState(CursorStates newCursorState) {
        super.publishCursorState(newCursorState);

        for (INavigationSource navigationSource: navigationSources) {  
        	navigationSource.cursorState(this, this.cursorState);
        }
	 }
	

	public boolean fetchFirst() {
        
        if (isLocked()) {
            return false;
        }

        if (hasFirstRow()) {
            return moveToRow(0, true);
        }
        
        //request data and replace current buffer
        requestData(DAOCommands.sendRows.name(), StartRowMarker.first.name(), this.batchSize,
        		false, AppendPosition.REPLACE, true, 0);
        
        if( this.hasFirstRow() ) { 
        	return this.moveToRow(0, true);
        }
        
        return false;
    }

	public boolean fetchLast() {
        if (isLocked()) {
            return false;
        }

        if ( hasLastRow() ) {
            return moveToRow( this.dataBuffer.size() -1, true);
        }

        //request data and replace current buffer
        requestData(DAOCommands.sendRows.name(), StartRowMarker.last.name(),  this.batchSize * -1,
        		false, AppendPosition.REPLACE, true, - 1);
        
        if( hasLastRow() ) return moveToRow( this.dataBuffer.size() - 1, true);
        return false;
    }

    
    public boolean fetchNext() {
        if( isLocked() ) return false;

        if( canMove( this.cursorPosition + 1) ) {
        	return moveToRow(this.cursorPosition + 1, true); 
        } else {
        	if( hasLastRow() ) 
        		return false;
        }
        //request next data and append it to the end of thge buffer
        requestData(DAOCommands.sendRows.name(), this.lastNextFetchedRowId, this.batchSize,
        		false, AppendPosition.END, true, -1);

        if( this.canMove( this.cursorPosition + 1) ) {
        	return moveToRow(this.cursorPosition + 1, true);
        }
        
        return false;
    }


    public boolean fetchPrev() {
        if( isLocked() ) return false;

        if( canMove( this.cursorPosition - 1) ) return moveToRow(this.cursorPosition - 1, true);
        else if( this.hasFirstRow() ) return false;

        int oldSize = this.dataBuffer.size();
        
        //request data and append it at the beginning of the data buffer
        requestData(DAOCommands.sendRows.name(), this.lastPreviousFetchedRowId, this.batchSize * -1,
        		false, AppendPosition.BEGINING, true, -1);
        
        int newSize = this.dataBuffer.size();

        if( oldSize < newSize  &&
        		canMove( newSize - oldSize + this.cursorPosition - 1) ) {
        	return moveToRow(newSize - oldSize + this.cursorPosition - 1, true);
        }

        return false;
    }

    public void addNavigationSource(INavigationSource navigationSource) {
       this.navigationSources.add(navigationSource); 
    }
    
    public void removeNavigationSource(INavigationSource navigationSource) {
       this.navigationSources.remove(navigationSource); 
    }

    public List<INavigationSource> getNavigationSources() {
        return Collections.unmodifiableList(this.navigationSources);
    }
}

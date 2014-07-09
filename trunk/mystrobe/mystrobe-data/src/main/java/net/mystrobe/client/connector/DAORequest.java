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
 package net.mystrobe.client.connector;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IFilterParameter;
import net.mystrobe.client.SortState;

/**
 * Defines an object to provide the client request to the actual backend server
 * data object implementation.
 * 
 * @author TVH Group NV
 */
public class DAORequest<T extends IDataBean> implements IDAORequest<T>, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum StartRowMarker { first, last, commit };
    
    protected HashSet<IFilterParameter> filters = new HashSet<IFilterParameter>();

    protected ArrayList<IDAORow<T>> rows = new ArrayList<IDAORow<T>>();

    /**
     *
     */
    protected SortState sortState = null;

    protected String daoId = null;

    protected String command = DAOCommands.sendRows.name();

    protected long batchSize = 50;
    
    protected String startRowId = StartRowMarker.first.name() ;

    protected boolean isPrefetch = false;

    protected boolean skipRow = true;

    
    public DAORequest() {

    }


    public DAORequest(String daoId, String command, String startRowId ) {
        this.daoId = daoId;
        if( command != null ) this.command = command ;
        if( startRowId != null) this.startRowId = startRowId;
        this.isPrefetch = false;
    }


    public DAORequest(String daoId, String command, String startRowId, long batchSize, boolean isPrefetch ) {
        this(daoId, command, startRowId);
        this.batchSize = batchSize;
        this.isPrefetch = isPrefetch;
    }

    
    public void addFilterParameter(IFilterParameter filterParam) {
        if( filterParam == null ) return;
        this.filters.add(filterParam);
    }

    
    public Set<IFilterParameter> getFilters() {
        return this.filters;
    }
    

    public void addRow(IDAORow<T> row) {
        if( row != null ) this.rows.add(row);
    }

    
    public long getBatchSize() {
        return this.batchSize;
    }


    public String getCommand() {
        return this.command;
    }
        

    public String getDAOId() {
        return this.daoId;
    }


    public List<IDAORow<T>> getRows() {
        return this.rows;
    }
    

    public SortState getSortState() {
        return this.sortState;
    }

    
    public String getStartRowId() {
        return this.startRowId;
    }


    public boolean isPrefetch() {
        return this.isPrefetch;
    }

    
    public void setBatchSize(long batchSize) {
        this.batchSize = batchSize;
    }


    public void setCommand(String command) {
        if( this.command != null ) this.command = command;
    }

    
    public void setDAOId(String daoId) {
        this.daoId = daoId;
    }

    
    public void setPrefetch(boolean isPrefetch) {
        this.isPrefetch = isPrefetch;
    }

    
    public void setSortState(SortState sortState) {
        this.sortState = sortState;
    }
    

    public void setStartRowId(String startRowId) {
        this.startRowId = startRowId;
    }

    public DAORequest<T> createDAORequest(String command, String startRowId) {
    	
    	DAORequest<T> newDaoRequest = new DAORequest<T>(this.daoId, command, startRowId);
    	
    	newDaoRequest.getFilters().addAll(this.getFilters());
    	newDaoRequest.setSortState(this.getSortState());
    	newDaoRequest.setPrefetch(this.isPrefetch);
    	newDaoRequest.setSkipRow(this.skipRow);
    	
    	return newDaoRequest;
    }
    
    public static <S extends IDataBean> DAORequest<S> FetchFirst( String daoId ) {
        return new DAORequest<S>(daoId, DAOCommands.sendRows.name(), StartRowMarker.first.name() );
    }

    public static <S extends IDataBean> DAORequest<S> FetchLast( String daoId ) {
        return new DAORequest<S>(daoId, DAOCommands.sendRows.name(), StartRowMarker.last.name(),  -50, false);
    }

    public static <S extends IDataBean> DAORequest<S> SubmitCommit( String daoId ) {
        return new DAORequest<S>(daoId, DAOCommands.submitCommit.name(), StartRowMarker.commit.name(),  50, false);
    }

    public boolean isSkipRow() {
        return this.skipRow;
    }

    public void setSkipRow(boolean isSkipRow) {
        this.skipRow = isSkipRow;
    }


}

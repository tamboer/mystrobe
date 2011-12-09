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

import java.util.Set;

import net.mystrobe.client.navigator.IDataTableNavigatorListener;
import net.mystrobe.client.navigator.IDataTableNavigatorSource;



/**
 * @author TVH Group NV
 */
public class ComponentLinker {

    public static  void bindState( IStateSource linkSource, IStateListener linkListener) {
        linkSource.addStateListener(linkListener);
    }

    public static  void unbindState( IStateSource linkSource, IStateListener linkListener) {
        linkSource.removeStateListener(linkListener);
    }


    public static  void bindNavigation( INavigationSource linkSource, INavigationListener linkListener) {
        linkSource.setNavigationListener(linkListener);
        linkListener.addNavigationSource(linkSource);
        bindState(linkListener, linkSource);
    }

    public static <T extends IDataBean> void  bindData( IDataSource<T> linkSource, IDataListener<T> linkListener, Set<DataLinkParameters> parameters) {
        linkSource.addDataListener(linkListener);
        linkListener.setDataSource(linkSource);
        linkListener.setDataLinkParameters(parameters);
    }

    
    public static <T extends IDataBean>  void unbindData( IDataSource<T> linkSource, IDataListener<T> linkListener) {
        linkSource.removeDataListener(linkListener);
        linkListener.clearDataLinkParameters();
        if( linkListener.getDataSource() == linkSource) 
        	linkListener.setDataSource(null);
    }

    public static  void bindFilter( IFilterSource linkSource, IFilterListener linkListener) {
        linkSource.setFilterListener(linkListener);
        linkListener.setFilterSource(linkSource);
    }

    public static  void unbindFilter( IFilterSource linkSource, IFilterListener linkListener) {
        if( linkSource.getFilterListener() == linkListener) linkSource.setFilterListener(null);
        if( linkListener.getFilterSource() == linkSource) linkListener.setFilterSource(null);
    }


    public static  void bindSort( ISortSource linkSource, ISortListener linkListener) {
        linkSource.setSortListener(linkListener);
        linkListener.setSortSource(linkSource);
    }


    public static  void unbindSort( ISortSource linkSource, ISortListener linkListener) {
        if( linkSource.getSortListener() == linkListener) linkSource.setSortListener(null);
        if( linkListener.getSortSource() == linkSource) linkListener.setSortSource(null);
    }


    public static  void bindUpdateUI( IUpdateUIActionSource linkSource, IUpdateUIActionListener linkListener) {
        linkSource.setUpdateActionListener(linkListener);
        bindState(linkListener, linkSource);
    }


    public static  void unbindUpdateUI( IUpdateUIActionSource linkSource, IUpdateUIActionListener linkListener) {
        linkSource.setUpdateActionListener(null);
        unbindState(linkListener, linkSource);
    }


    public static <T extends IDataBean> void bindUpdate( IUpdateSource<T> linkSource, IUpdateListener<T> linkListener) {
        linkSource.setUpdateListener(linkListener);
        bindState(linkListener, linkSource);
    }

    public static <T extends IDataBean> void unbindUpdate( IUpdateSource<T> linkSource, IUpdateListener<T> linkListener) {
        if( linkSource.getUpdateListener() == linkListener) linkSource.setUpdateListener(null);
        bindState(linkListener, linkSource);
    }


    public static  void bindAction( IActionSource linkSource, IActionListener linkListener) {
        linkSource.addActionListener(linkListener);
        linkListener.addActionSource(linkSource);
    }


    public static  void unbindAction( IActionSource linkSource, IActionListener linkListener) {        
        linkSource. removeActionListener(linkListener);
        linkListener.removeActionSource(linkSource);
    }
    
    public static <T extends IDataBean> void bindDataTableNavigation( IDataTableNavigatorSource<T> linkSource, IDataTableNavigatorListener<T> linkListener) {
        linkSource.setDataTableNavigationListener(linkListener);
        linkListener.addDataTableNavigationSource(linkSource);
        
        linkListener.addDataBufferListener(linkSource);
    }
    
    public static <T extends IDataBean> void bindDataTableData( IDataTableDataSource<T> linkSource, IDataTableDataListener<T> listener) {
        linkSource.addDataTableDataListener(listener);
        listener.setDataTableDataSource(linkSource);
    }

}

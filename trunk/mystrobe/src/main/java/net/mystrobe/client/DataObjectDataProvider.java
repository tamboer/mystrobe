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

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;



/**
 * @author TVH Group NV
 */
public class DataObjectDataProvider implements IDataProvider<IDataBean> {
	private static final long serialVersionUID = 1L;
	
	protected IDataObject dataObject;
    protected int size = 0;


    public DataObjectDataProvider(IDataObject dataObject) {
        this.dataObject = dataObject;
        
        if( this.dataObject != null ) this.size = (int) this.dataObject.getSchema().getBatchSize();
    }


    public Iterator<? extends IDataBean> iterator(int start, int size) {
        ArrayList<IDataBean> result = new ArrayList<IDataBean>();
        int idx = 0;

//        dataObject.fetchFirst();
//        if( dataObject.getCursorState() != CursorStates.FirstRecord &&  dataObject.getCursorState() != CursorStates.OnlyRecordAvailable ) return result.iterator();
//
//        for( idx=0; idx < start; idx++ ) {
//            dataObject.fetchNext();
//            if(  dataObject.getCursorState() != CursorStates.NotFirstOrLast ) return result.iterator();
//        }
//        
//        for( idx=0; idx < size; idx++ ) {
//            result.add(dataObject.getData());
//            dataObject.fetchNext();
//            if(  dataObject.getCursorState() == CursorStates.LastRecord
//                    || dataObject.getCursorState() == CursorStates.NoRecordAvailable ) return result.iterator();
//        }

        this.size = start + (int)( idx < size  ? idx : this.dataObject.getSchema().getBatchSize() );
        return result.iterator();
    }

    
    public int size() {
        return size;
    }

    public IModel<IDataBean> model(IDataBean t) {
        return new Model<IDataBean>(t);
    }

    public void detach() {
        
    }

}

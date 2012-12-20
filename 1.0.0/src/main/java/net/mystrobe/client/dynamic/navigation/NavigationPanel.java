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
 package net.mystrobe.client.dynamic.navigation;

import net.mystrobe.client.CursorStates;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.INavigationListener;
import net.mystrobe.client.INavigationSource;
import net.mystrobe.client.IStateCallback;
import net.mystrobe.client.IStateSource;
import net.mystrobe.client.UpdateStates;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author TVH Group NV
 */
public class NavigationPanel extends Panel implements INavigationSource, IStateCallback {

    private static final long serialVersionUID = -2561240497275910451L;
	
	protected INavigationListener navigationListener;
    protected UpdateStates updateState = UpdateStates.UpdateComplete;
    protected CursorStates cursorState = CursorStates.NoRecordAvailable;

    public static enum Commands {First, Previous, Next, Last };

    protected Button first = null;
    protected Button previous = null;
    protected Button next = null;
    protected Button last = null;

    public NavigationPanel(String id, Form<IDataBean> form) {
        super(id, form.getModel());
        initialize(form);
    }

    private void initialize(Form<IDataBean> form) {
        setOutputMarkupId(true);
        
        first = new Button("navPanel_fetchFirst"){
            
			private static final long serialVersionUID = 5315469481510106892L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;                
                publishComand(Commands.First);
            }
        };
        first.setDefaultFormProcessing(false);
        add(first);

        previous = new Button("navPanel_fetchPrevious"){
            
			private static final long serialVersionUID = 3571126860057319034L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                publishComand(Commands.Previous);
            }
        };
        previous.setDefaultFormProcessing(false);
        add(previous);

        next = new Button("navPanel_fetchNext"){
            
			private static final long serialVersionUID = -7126791926265944263L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                publishComand(Commands.Next);
            }
        };
        next.setDefaultFormProcessing(false);
        add(next);

        last = new Button("navPanel_fetchLast"){
            
			private static final long serialVersionUID = -8557552915336771030L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                publishComand(Commands.Last);
            }
        };
        last.setDefaultFormProcessing(false);
        add(last);
    }

    public INavigationListener getNavigationListener() {
        return this.navigationListener;
    }

    public void setNavigationListener(INavigationListener navigationListener) {
        this.navigationListener = navigationListener;
    }


    protected void publishComand(Commands command ) {
        if( getNavigationListener() == null ) return ;
        
        switch( command ) {
            case First: getNavigationListener().fetchFirst(); break;
            case Previous: getNavigationListener().fetchPrev(); break;
            case Next: getNavigationListener().fetchNext(); break;
            case Last: getNavigationListener().fetchLast(); break;
        }
    }

    public void cursorState(IStateSource navigationListener, CursorStates cursorState) {
        
        this.cursorState = cursorState;
        if( updateState.isInTransaction() ) cursorState = CursorStates.NoRecordAvailable;

        switch( cursorState ) {

            case FirstRecord: 
                first.setEnabled(false);
                previous.setEnabled(false);
                next.setEnabled(true);
                last.setEnabled(true);
                break;

            case NoRecordAvailable:
                first.setEnabled(false);
                previous.setEnabled(false);
                next.setEnabled(false);
                last.setEnabled(false);
                break;

            case OnlyRecordAvailable:
                first.setEnabled(false);
                previous.setEnabled(false);
                next.setEnabled(false);
                last.setEnabled(false);
                break;

            case LastRecord:
                first.setEnabled(true);
                previous.setEnabled(true);
                next.setEnabled(false);
                last.setEnabled(false);
                break;

            case NotFirstOrLast:
                first.setEnabled(true);
                previous.setEnabled(true);
                next.setEnabled(true);
                last.setEnabled(true);
                break;
        }        
    }

    public void updateState(IStateSource navigationListener, UpdateStates updateState) {
        this.updateState = updateState;
        CursorStates oldCursorStates = this.cursorState;
        if( updateState.isInTransaction() ) {
            cursorState(getNavigationListener(), CursorStates.NoRecordAvailable);
            this.cursorState = oldCursorStates;
        } else {
            cursorState(getNavigationListener(), this.cursorState);
        }
         
    }
}
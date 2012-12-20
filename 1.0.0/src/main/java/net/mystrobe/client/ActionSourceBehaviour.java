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
import java.util.Collection;
import java.util.List;
import java.util.Iterator;

import net.mystrobe.client.dynamic.panel.DynamicFormDataViewPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author TVH Group NV
 */
public class ActionSourceBehaviour  extends OnChangeAjaxBehavior implements IActionSource {

	private static final Logger logger = LoggerFactory.getLogger(DynamicFormDataViewPanel.class);
	
    protected List<IActionListener> actionListeners = new ArrayList<IActionListener>();
    protected String columnName = null;
    protected String oldValue = null;
    protected String initialValue = null;
    protected boolean dataModifiedPublished = false;

    

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        boolean notify = false;
        
        if( getComponent() instanceof FormComponent ) {
            
            String value = ((FormComponent)getComponent()).getInput();

            if( !value.equals(oldValue) ) {
                triggerValueChanged(oldValue, value);                
                oldValue = value;
                notify = true;
            }            

            if( !value.equals(initialValue) ) {
                if( !dataModifiedPublished ) {
                    dataModifiedPublished = true;
                    triggerDataModified( true );
                    notify = true;
                }
            } else if( dataModifiedPublished ) {
                dataModifiedPublished = false;
                triggerDataModified(false);
                notify = true;
            }
        }

        if( notify ) {
            Iterator<Component> components = getUpdateableComponents().iterator();
            Component comp;
            while( components.hasNext() ) {
                comp = components.next();
                target.addComponent(comp);
                System.err.println("Adding component to be rerendere " + comp.getMarkupId());
            }
        }
    }

    

    protected List<Component> getUpdateableComponents() {
        List<Component> components = new ArrayList<Component>();
        Iterator<IActionListener> listenersIterator = actionListeners.iterator();

        IActionListener listener;
        while( listenersIterator.hasNext() ) {
            listener = listenersIterator.next();
            if( listener instanceof Component ) {
                components.add( (Component) listener );
            }
            if( listener instanceof IUpdateUIActionListener ) {
                Iterator <IStateListener>  sources = ((IUpdateUIActionListener)listener).getStateListeners().iterator();
                while( sources.hasNext() ) {
                    IStateListener actionSource = sources.next();
                    if( actionSource instanceof Component ) components.add( (Component) actionSource );
                }                
            }
        }
        return components;
    }

    
    public void modelChanged() {
        if( getComponent() instanceof FormComponent ) {
            this.initialValue = this.oldValue = ((FormComponent)getComponent()).getDefaultModelObjectAsString();
            System.err.println("ActionSourceBehaviour model changed" + this.initialValue);
        }        
    }



    protected void triggerValueChanged(Object oldValue, Object newValue) {
        System.err.println("ActionSourceBehaviour valueChanged oldValue=" + oldValue + " new value =" + newValue);
        Iterator<IActionListener> listenersIterator = actionListeners.iterator();
        while( listenersIterator.hasNext() ) {
            listenersIterator.next().valueChanged(this, oldValue, newValue, columnName);
        }        
    }

    
    protected void triggerDataModified(boolean isModified) {
        System.err.println("ActionSourceBehaviour dataChanged=" + isModified);
        Iterator<IActionListener> listenersIterator = actionListeners.iterator();
        while( listenersIterator.hasNext() ) {
            listenersIterator.next().dataModified(this, isModified);
        }
    }

    
    public void addActionListener(IActionListener actionListener) {
        if( actionListener == null ) return;
        if( !actionListeners.contains( actionListener) ) actionListeners.add(actionListener);
    }

    public Collection<IActionListener> getActionListener() {
        return actionListeners;
    }

    public void removeActionListener(IActionListener actionListener) {
        if( actionListener == null || !actionListeners.contains(actionListener) ) return;
        actionListeners.remove(actionListener);
    }

}

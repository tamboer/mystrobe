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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.mystrobe.client.CursorStates;
import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IStateSource;
import net.mystrobe.client.IUpdateUIActionListener;
import net.mystrobe.client.IUpdateUIActionSource;
import net.mystrobe.client.UpdateStates;
import net.mystrobe.client.ui.UICssResourceReference;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * CRUD operations panel.</br>
 * 
 * Visible action buttons can be configured through a set of actions constructor parameter.</br>. 
 * 
 * Actions can be customized by overriding appropriate action methods(onDelete, onAdd etc) </br>.  
 * 
 * <p>
 * UI component displays action buttons for CRUD operations.
 * Acts as a {@link IUpdateUIActionSource} for form  
 *  components (components that listen for user CRUD operations).</p>
 * 
 * <p>
 * Component has to be linked to {@link IStateSource} components usually a {@link IDataObject} and/or {@link IUpdateUIActionListener}
 *  to receive state/cursor changes and properly update buttons enable/disabled status. </p> 
 * 
 * @author TVH Group NV
 *
 */
public class CRUDOperationsPanel extends Panel implements IUpdateUIActionSource {

    private static final long serialVersionUID = 6009973376230185804L;

    private static final Logger logger = LoggerFactory.getLogger(CRUDOperationsPanel.class);
    
	public static enum Commands { Add, Copy, Delete, Edit, Save, Cancel, Reset };

    public static enum CRUDButton {Add, Copy, Delete, Edit, Save, Cancel, Reset};
    
    public static final Set<CRUDButton> ALL = new HashSet<CRUDButton>(Arrays.asList(new CRUDButton [] {CRUDButton.Add, CRUDButton.Copy,
    		CRUDButton.Delete, CRUDButton.Edit, CRUDButton.Save, CRUDButton.Cancel, CRUDButton.Reset}));
    
    public static final Set<CRUDButton> EDIT = new HashSet<CRUDButton>(Arrays.asList(new CRUDButton [] {CRUDButton.Save, 
    			CRUDButton.Cancel, CRUDButton.Reset}));
    
    public static final Set<CRUDButton> CRUD = new HashSet<CRUDButton>(Arrays.asList(new CRUDButton [] {CRUDButton.Add, 
			CRUDButton.Edit, CRUDButton.Delete}));
    
    public static enum ButtonState { InitialTableIO
            ,Update
            ,ModalUpdate
            ,ModalUpdateModified
            ,DeleteOnly
            ,AddOnly
            ,UpdateOnly
            ,DisableTableIO };

    private Button btAdd = null
            , btCopy = null
            , btDelete = null
            , btEdit = null
            , btSave = null
            , btCancel = null
            , btReset = null;


    protected IUpdateUIActionListener actionListener = null;
    
    protected ButtonState currentButtonState = null;
    
    protected UpdateStates currentUpdateState = UpdateStates.UpdateComplete;
    
    protected CursorStates currentCursorStates = CursorStates.NoRecordAvailable;
  
    public CRUDOperationsPanel(String id, Form<IDataBean> form) {
    	 super(id, form.getModel());
    	 initialize(form, ALL);
    }
    
    public CRUDOperationsPanel(String id, Form<IDataBean> form, Set<CRUDButton> crudButtons) {
        super(id, form.getModel());
        
        if (crudButtons == null ||  crudButtons.isEmpty() ) {
        	throw new IllegalArgumentException("Buttons set can not be null or empty.");
        }
        
        initialize(form, crudButtons);
    }

    private void initialize(Form<IDataBean> form, Set<CRUDButton> buttons) {
        setOutputMarkupId(true);
        
        btAdd = new Button("updatePanel_addRecord"){
            
			private static final long serialVersionUID = -244003584318770481L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                
                publishComand(Commands.Add);
                
                onAdd();
            }
        };
        btAdd.setDefaultFormProcessing(false);
        btAdd.setOutputMarkupId(true);
        btAdd.setVisible(buttons.contains(CRUDButton.Add));
        add(btAdd);

        btCopy = new Button("updatePanel_copyRecord"){
            
			private static final long serialVersionUID = 6730551261477538588L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                
                publishComand(Commands.Copy);
                
                //execute additional actions
                onCopy();
            }
			
			
        };
        btCopy.setDefaultFormProcessing(false);
        btCopy.setOutputMarkupId(true);
        btCopy.setVisible(buttons.contains(CRUDButton.Copy));
        add(btCopy);

        btDelete = new Button("updatePanel_deleteRecord"){
           
			private static final long serialVersionUID = 797462197525522215L;

			@Override
            public void onSubmit() {
                
				if( !this.isEnabled() ) return;
                
                publishComand(Commands.Delete);
            }
        };
        btDelete.setDefaultFormProcessing(false);
        btDelete.setOutputMarkupId(true);
        btDelete.setVisible(buttons.contains(CRUDButton.Delete));
        add(btDelete);

        btEdit = new Button("updatePanel_editRecord"){
            
			private static final long serialVersionUID = -174066646453826190L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                
                publishComand(Commands.Edit);
                
                //execute additional actions
                onEdit();
            }
        };
        btEdit.setDefaultFormProcessing(false);
        btEdit.setOutputMarkupId(true);
        btEdit.setVisible(buttons.contains(CRUDButton.Edit));
        add(btEdit);

        btSave = new Button("updatePanel_saveRecord"){
            
			private static final long serialVersionUID = 6647366130896931647L;

			@Override
            public void onSubmit() {
                
				if( !this.isEnabled() ) return;
            	
				publishComand(Commands.Save);
                	
            	//execute additional actions
                onSave();
        	}
        };
        btSave.setOutputMarkupId(true);
        btSave.setVisible(buttons.contains(CRUDButton.Save));
        add(btSave);

        btCancel = new Button("updatePanel_cancelRecord"){
            
			private static final long serialVersionUID = 8403410656140978030L;

			@Override
            public void onSubmit() {
                if( !this.isEnabled() ) return;
                
                publishComand(Commands.Cancel);
            
                //execute additional actions
                onCancel();
			}
		};
		
        btCancel.setDefaultFormProcessing(false);
        btCancel.setOutputMarkupId(true);
        btCancel.setVisible(buttons.contains(CRUDButton.Cancel));
        add(btCancel);

        btReset = new Button("updatePanel_resetRecord"){
            
			private static final long serialVersionUID = -6258763748312644996L;

			@Override
            public void onSubmit() {
                
				if( !this.isEnabled() ) return;
                
				publishComand(Commands.Reset);
            }
        };
        btReset.setDefaultFormProcessing(false);
        btReset.setOutputMarkupId(true);
        btReset.setVisible(buttons.contains(CRUDButton.Reset));
        add(btReset);
    }
    
    /**
     * Method to be overridden by inheritance classes
     *  to add additional behavior to cancel button (i.e. close page)
     */
    protected void onCancel() {
    	
    }
    
    /**
     * Method to be overridden by inheritance classes
     *  to add additional behavior to save button (i.e. close page)
     */
    protected void onSave() {
    	
    }
    
    /**
     * Method to be overridden by inheritance classes
     *  to add additional behavior to add button (i.e. close page)
     */
    protected void onAdd() {
    	
    }
    
    /**
     * Method to be overridden by inheritance classes
     *  to add additional behavior to edit button (i.e. close page)
     */
    protected void onEdit() {
    	
    }
    
    /**
     * Method to be overridden by inheritance classes
     *  to add additional behavior to copy button (i.e. close page)
     */
    protected void onCopy() {
    	
    }
    
    protected void publishComand(Commands command ) {
        if( getUpdateActionListener() == null ) return ;

        logger.debug("Update action panel publishing: " + command.name());

        switch( command ) {
            case Add: getUpdateActionListener().addRecord(); break;
            case Copy: getUpdateActionListener().copyRecord(); break;
            case Delete: getUpdateActionListener().deleteRecord(); break;
            case Edit: getUpdateActionListener().editRecord(); break;
            case Save: getUpdateActionListener().saveRecord(); break;
            case Cancel: getUpdateActionListener().cancelRecord(); break;
            case Reset: getUpdateActionListener().resetRecord(); break;
        }
    }

    public void cursorState(IStateSource updateUIActionListener, CursorStates cursorState) {

        if (!this.currentCursorStates.equals(cursorState)) {
        	this.currentCursorStates = cursorState;

            if( UpdateStates.UpdateBegin.equals(this.currentUpdateState) || UpdateStates.Update.equals(this.currentUpdateState)) {
                this.updateState(getUpdateActionListener(), currentUpdateState);
            } else {
                switch( cursorState ) {

                    case NoRecordAvailable:
                        setButtons( ButtonState.AddOnly);
                        break;

                    default:
                        setButtons( ButtonState.InitialTableIO );
                        break;
                }

            }
        }
    }

    public void updateState(IStateSource updatateUIActionListener, UpdateStates updateState) {
    	logger.debug("Update Toolgroup got update state " + updateState);
        
        this.currentUpdateState = updateState;

        switch( updateState ) {
            
            case UpdateBegin: 
                    setButtons(ButtonState.ModalUpdate);
                    break;
                    
            case Update:
                    setButtons( ButtonState.ModalUpdateModified );
                    break;
                    
            case UpdateEnd:
            case UpdateComplete:
                    setButtons( CursorStates.NoRecordAvailable.equals( this.currentCursorStates) ? ButtonState.AddOnly : ButtonState.InitialTableIO );
                    break;

            default:
                setButtons( ButtonState.DisableTableIO );
                break;
        }
    }

    public IUpdateUIActionListener getUpdateActionListener() {
        return this.actionListener;
    }

    public void setUpdateActionListener(IUpdateUIActionListener updateActionListener) {
        this.actionListener = updateActionListener;
    }
    
    public void addButtonBehavior(CRUDButton button, Behavior behavior ) {
    	 switch( button ) {
	         case Add: btAdd.add(behavior); break;
	         case Copy: btCopy.add(behavior);  break;
	         case Delete: btDelete.add(behavior); break;
	         case Edit: btEdit.add(behavior); break;
	         case Save: btSave.add(behavior); break;
	         case Cancel: btCancel.add(behavior); break;
	         case Reset: btReset.add(behavior); break;
    	 }
    	
    }
    
    protected void setButtons(ButtonState buttonState) {
        if( buttonState.equals(currentButtonState) ) return;

        currentButtonState = buttonState;
        switch( buttonState ) {
            case InitialTableIO:
                    this.btAdd.setEnabled(true);
                    this.btCopy.setEnabled(true);
                    this.btEdit.setEnabled(true);
                    this.btDelete.setEnabled(true);
                    this.btSave.setEnabled(false );
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;

            case Update :
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(true);
                    this.btReset.setEnabled(true);
                    this.btCancel.setEnabled(false);
                    break;

            case ModalUpdate :
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(true);
                    this.btReset.setEnabled(true);
                    this.btCancel.setEnabled(true);
                    break;

            case ModalUpdateModified :
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(true);
                    this.btReset.setEnabled(true);
                    this.btCancel.setEnabled(true);
                    break;

            case DeleteOnly :
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(true);
                    this.btSave.setEnabled(false);
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;

            case AddOnly :
                    this.btAdd.setEnabled(true);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(false);
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;

            case UpdateOnly :
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(true);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(false);
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;


            case DisableTableIO:
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(false);
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;

            default:
                    this.btAdd.setEnabled(false);
                    this.btCopy.setEnabled(false);
                    this.btEdit.setEnabled(false);
                    this.btDelete.setEnabled(false);
                    this.btSave.setEnabled(false);
                    this.btReset.setEnabled(false);
                    this.btCancel.setEnabled(false);
                    break;
            }
        }
    
    public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(UICssResourceReference.get()));
	 }
}



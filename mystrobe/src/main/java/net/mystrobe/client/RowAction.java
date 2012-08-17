package net.mystrobe.client;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author annba
 */
public class RowAction {

    public static final RowAction SELECT = new RowAction();
    public static final RowAction EDIT = new RowAction();
    public static final RowAction DELETE = new RowAction(); 
    public static final RowAction NEW = new RowAction();
    public static final RowAction PRINT = new RowAction();
    public static final RowAction OPEN = new RowAction();
    
    protected RowAction(){}

    public static List<RowAction> getFullActions() {
        /*
         * All actions, except some (f.e. select), because these actions are
         * defined by the object that called the panel/page
         */
        return Arrays.asList(EDIT, DELETE, NEW, PRINT);
    }

    public static List<RowAction> getReadActions() {
        return Arrays.asList(PRINT);
    }

    public static boolean isActionAllowed(List<RowAction> actions, RowAction allowedAction) {
        if(actions != null) {
            return actions.contains(allowedAction);
        }
        return false;
    }

    public enum RowActionRequestType {

        GET,
        AJAX
    }
}
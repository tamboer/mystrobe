/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mystrobe.client;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author annba
 */
public enum RowAction {

    SELECT, EDIT, DELETE, NEW, PRINT,
    CHANGE_AFFILIATE_SEARCHBOX,
    SELECT_COMPANY_DEFAULT_ORDER_CARRIER,
    SHOW_ORDER_PDF_LINK,
    OPEN, SWITCH, SET_DEFAULT,
    CHECK_FREE_TRANSPORT,
    COMPLAINT, CHECKBOX;

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
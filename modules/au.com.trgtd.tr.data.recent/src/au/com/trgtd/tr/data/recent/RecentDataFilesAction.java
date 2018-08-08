/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.data.recent;

import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 * Action for the recent files menu.
 *
 * @author Jeremy Moore
 */
public final class RecentDataFilesAction extends CallableSystemAction { 
    
    private RecentDataFileMenu menu;        
    
    @Override
    public JMenuItem getMenuPresenter() {
        if (menu == null) {
            menu = new RecentDataFileMenu();            
        }
        return menu;        
    }

    void refreshMenu() {
        if (menu == null) {
            menu = new RecentDataFileMenu();            
        }
        menu.initialize();            
    }

    public String getName() {
        return "";
    }
    
    public void performAction() {
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}

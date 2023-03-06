/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

    @Override
    public String getName() {
        return "";
    }
    
    @Override
    public void performAction() {
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}

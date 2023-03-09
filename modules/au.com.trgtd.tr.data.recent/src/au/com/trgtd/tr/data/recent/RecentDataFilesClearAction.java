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

import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;

/**
 * Action to clear the recent files menu.
 *
 * @author Jeremy Moore
 */
public final class RecentDataFilesClearAction extends CallableSystemAction {
        
    @Override
    public void performAction() {
        Prefs.setPaths(null, 0);
        
        RecentDataFilesAction action = SystemAction.get(RecentDataFilesAction.class);
        if (action != null) {
            action.refreshMenu();
        }
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(RecentDataFilesClearAction.class, "CTL_RecentDataFilesClearAction");
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

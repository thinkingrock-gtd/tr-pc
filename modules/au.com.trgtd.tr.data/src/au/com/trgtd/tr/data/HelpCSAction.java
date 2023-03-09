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
package au.com.trgtd.tr.data;

import au.com.trgtd.tr.resource.Resource;
import org.netbeans.api.javahelp.Help;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Action for context sensitive help.
 *
 * @author Jeremy Moore
 */
public final class HelpCSAction extends CallableSystemAction {
    
    public HelpCSAction() {
        super();
    }
    
    @Override
    protected String iconResource() {
        return Resource.CSHelp;
    }

    /** Perform the action to show help for the active screen. */
    @Override
    public void performAction() {
        TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
        if (tc == null) return;

        HelpCtx helpCtx = tc.getHelpCtx();
        if (helpCtx == null) return;
        
        Help help = Lookup.getDefault().lookup(Help.class);
        if (help == null) return;
        
        help.showHelp(helpCtx);
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(HelpCSAction.class, "CTL_HelpCSAction"); // No I18N
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

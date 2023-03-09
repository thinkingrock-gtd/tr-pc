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

import org.netbeans.api.javahelp.Help;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * Action for help contents.
 *
 * @author Jeremy Moore
 */
public final class HelpAction extends CallableSystemAction {

    public HelpAction() {
    }
    
    /** Perform the action to show help for the active screen. */
    @Override
    public void performAction() {
        Help help = Lookup.getDefault().lookup(Help.class);
        if (help == null) {
            return;
        }
        help.showHelp(HelpCtx.DEFAULT_HELP);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(HelpAction.class, "CTL_HelpAction"); // No I18N
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

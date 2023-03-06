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
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.resource.Icons;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.project.Project;

/**
 * Action to toggle hide/show completed items.
 *
 * @author Jeremy Moore
 */
public class ToggleHideDoneAction extends CookieAction {
    
    public ToggleHideDoneAction() {
        setIcon(Icons.ShowDone);
    }
    
    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ToggleHideDoneAction");
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { ToggleHideDoneCookie.class, Project.class };
    }
    
    @Override
    public int mode() {
        return MODE_ALL;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public void performAction(Node[] nodes) {
        for (Node node : nodes) {
            ToggleHideDoneCookie cookie = node.getCookie(ToggleHideDoneCookie.class);
            if (cookie != null) {
                cookie.toggleHideDone();
            }
        }
    }
    
}


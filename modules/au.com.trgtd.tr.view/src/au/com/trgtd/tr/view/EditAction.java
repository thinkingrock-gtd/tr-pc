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

import au.com.trgtd.tr.resource.Resource;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * Action to edit.
 *
 * @author Jeremy Moore
 */
public class EditAction extends CookieAction {
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_EditAction");
    }

    @Override
    protected String iconResource() {
        return Resource.Edit;
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { EditCookie.class };
    }
    
    @Override
    public int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes.length != 1) {
            return false;
        }
        EditCookie cookie = activatedNodes[0].getCookie(EditCookie.class);
        return cookie != null && cookie.canEdit();
    }

    @Override
    public void performAction(Node[] nodes) {
        if (nodes.length == 1) {
            EditCookie cookie = nodes[0].getCookie(EditCookie.class);
            if (cookie != null) {
                cookie.edit();
            }
        }
    }
    
}


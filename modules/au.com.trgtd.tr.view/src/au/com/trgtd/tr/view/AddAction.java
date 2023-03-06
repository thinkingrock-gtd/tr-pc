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
 * Action to add items.
 *
 * @author Jeremy Moore
 */
public class AddAction extends CookieAction {

    @Override
    protected String iconResource() {
        return Resource.Add;
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(AddAction.class, "CTL_AddAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[] { AddCookie.class };
    }

    @Override
    protected void performAction(Node[] nodes) {
        AddCookie cookie = nodes[0].getCookie(AddCookie.class);
        if (cookie != null) {
            cookie.add();
        }
    }

}


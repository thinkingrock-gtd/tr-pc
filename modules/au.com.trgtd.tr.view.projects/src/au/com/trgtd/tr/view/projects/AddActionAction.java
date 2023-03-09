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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.resource.Icons;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * System action to add a TR action.
 *
 * @author Jeremy Moore
 */
public class AddActionAction extends CookieAction {
    
    private final Lookup.Result result;

    public AddActionAction() {
        super();
        setIcon(Icons.ActionAdd);
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            setEnabled(!result.allInstances().isEmpty());
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    /** Gets the initial action name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddActionAction");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    protected Class[] cookieClasses() {
        return new Class[] { Project.class, Action.class, AddActionCookie.class };
    }

    @Override
    protected boolean enable(Node[] nodes) {
        if (nodes.length == 1) {
            if (nodes[0] instanceof ActionNode actionNode) {
                return actionNode.canAddAction();
            }
            if (nodes[0] instanceof ProjectNode projectNode) {
                return projectNode.canAddAction();
            }
        }
        return false;
    }

    protected void performAction(Node[] nodes) {
        AddActionCookie cookie = nodes[0].getCookie(AddActionCookie.class);
        if (cookie != null) {
            cookie.addAction();
        }
    }


//    @Override
//    protected int mode() {
//        return MODE_ONE;
//    }
//
//    @Override
//    protected Class<?>[] cookieClasses() {
//        return new Class[] {AddActionCookie.class};
//    }
//
//    @Override
//    protected void performAction(Node[] nodes) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public String getName() {
//        return "Some Action Name";
//    }
//
//    @Override
//    public HelpCtx getHelpCtx() {
//        return HelpCtx.DEFAULT_HELP;
//    }
//

}



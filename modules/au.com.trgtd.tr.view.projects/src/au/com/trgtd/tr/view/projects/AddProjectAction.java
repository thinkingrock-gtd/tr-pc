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
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * System action to add a TR project.
 *
 * @author Jeremy Moore
 */
//////public class AddProjectAction extends CallableSystemAction {
public class AddProjectAction extends CookieAction {
    
    /** Constructs a new instance. */
    public AddProjectAction() {
        setIcon(Icons.ProjectAdd);
    }
    
    /** Gets the display name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddProjectAction");
    }
    
    /** Gets help context. */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
//////    public void performAction() {
//////        AddProjectCookie cookie = getAddProjectCookie();
//////        if (cookie != null) {
//////            cookie.addProject();
//////        }
//////    }
    
//////    public boolean isEnabled() {
//////        return getAddProjectCookie() != null;
//////    }
    
//////    private synchronized AddProjectCookie getAddProjectCookie() {
//////
//////        for (Object object : WindowManager.getDefault().getRegistry().getOpened()) {
//////
//////            TopComponent tc = (TopComponent)object;
//////
//////            if (tc.isShowing() && tc instanceof ExplorerManager.Provider) {
//////
//////                ExplorerManager.Provider emp = (ExplorerManager.Provider)tc;
//////
//////                for (Node node : emp.getExplorerManager().getSelectedNodes()) {
//////
//////                    if (node instanceof AddProjectCookie) {
//////
//////                        AddProjectCookie cookie = (AddProjectCookie)node;
//////
//////                        return cookie.canAddProject() ? cookie : null;
//////                    }
//////                }
//////            }
//////        }
//////
//////        return null;
//////    }
    
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    protected Class[] cookieClasses() {
        return new Class[] { Project.class, Action.class, AddProjectCookie.class };
    }
    
    @Override
    protected boolean enable(Node[] nodes) {
        if (nodes.length == 1) {
            if (nodes[0] instanceof ActionNode actionNode) {
                return actionNode.canAddProject();
            }
            if (nodes[0] instanceof ProjectNode projectNode) {
                return projectNode.canAddProject();
            }
        }
        return false;
    }
    
    protected void performAction(Node[] nodes) {
        AddProjectCookie cookie = nodes[0].getCookie(AddProjectCookie.class);
        if (cookie != null) {
            cookie.addProject();
        }
    }
    
}


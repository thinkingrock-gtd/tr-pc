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
            if (nodes[0] instanceof ActionNode) {
                return ((ActionNode)nodes[0]).canAddProject();
            }
            if (nodes[0] instanceof ProjectNode) {
                return ((ProjectNode)nodes[0]).canAddProject();
            }
        }
        return false;
    }
    
    protected void performAction(Node[] nodes) {
        AddProjectCookie cookie =
                (AddProjectCookie)nodes[0].getCookie(AddProjectCookie.class);
        if (cookie != null) {
            cookie.addProject();
        }
    }
    
}


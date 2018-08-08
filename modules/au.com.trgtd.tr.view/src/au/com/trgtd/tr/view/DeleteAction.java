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
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.resource.Resource;
import java.awt.Frame;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Action to delete.
 *
 * @author Jeremy Moore
 */
public class DeleteAction extends CookieAction {

    public DeleteAction() {
        super();
    }

    @Override
    protected String iconResource() {
        return Resource.Delete;
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_DeleteAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[] { Action.class, Project.class, DeleteCookie.class };
    }

    @Override
    public int mode() {
        return CookieAction.MODE_ALL;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (activatedNodes.length == 0) {
            return false;
        }
        for (Node node : activatedNodes) {
            DeleteCookie cookie = node.getCookie(DeleteCookie.class);
            if (cookie == null || !cookie.canDelete()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void performAction(Node[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return;
        }
        String t;
        String m;
        if (nodes.length == 1) {
            DeleteCookie cookie = (DeleteCookie) nodes[0].getCookie(DeleteCookie.class);
            m = NbBundle.getMessage(getClass(), "confirm.single.delete.message", cookie.toString());
            t = NbBundle.getMessage(getClass(), "confirm.single.delete.title");
        } else {
            m = NbBundle.getMessage(getClass(), "confirm.multiple.delete.message", nodes.length );
            t = NbBundle.getMessage(getClass(), "confirm.multiple.delete.title");
        }
        Frame f = WindowManager.getDefault().getMainWindow();
        int r = JOptionPane.showConfirmDialog(f, m, t, JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }
        for (Node node : nodes) {
            DeleteCookie cookie = (DeleteCookie) node.getCookie(DeleteCookie.class);
            if (cookie != null) {
                cookie.delete();
            }
        }
    }
}


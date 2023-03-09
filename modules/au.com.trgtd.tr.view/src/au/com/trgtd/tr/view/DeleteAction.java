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
            DeleteCookie cookie = nodes[0].getCookie(DeleteCookie.class);
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
            DeleteCookie cookie = node.getCookie(DeleteCookie.class);
            if (cookie != null) {
                cookie.delete();
            }
        }
    }
}


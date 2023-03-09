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
package au.com.trgtd.tr.view.someday;

import au.com.trgtd.tr.resource.Icons;
import java.awt.Component;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.future.Future;

/**
 * Action to delete future items.
 *
 * @author Jeremy Moore
 */
public class FutureDeleteAction extends CookieAction {
    
    public FutureDeleteAction() {
        super();
        setIcon(Icons.Delete);
    }
    
    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_FutureDeleteAction");
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Future.class };
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
        
        if (nodes == null || nodes.length == 0) return;
        
        String title = NbBundle.getMessage(FutureDeleteAction.class, "confirm.deletion");
        String message;
        if (nodes.length == 1) {
            message = NbBundle.getMessage(FutureDeleteAction.class, "confirm.deletion.message.1", nodes[0].getName());
        } else {
            message = NbBundle.getMessage(FutureDeleteAction.class, "confirm.deletion.message.2", nodes.length);
        }
        Component p = WindowManager.getDefault().getMainWindow();
        int opt = JOptionPane.showConfirmDialog(p, message, title, JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) {
            return;
        }
        
        for (Node node : nodes) {
            FutureDeleteCookie cookie = node.getCookie(FutureDeleteCookie.class);
            if (cookie != null) {
                cookie.deleteFuture();
            }
        }
    }
    
}


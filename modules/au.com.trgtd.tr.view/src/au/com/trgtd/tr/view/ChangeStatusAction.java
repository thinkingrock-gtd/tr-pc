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
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.action.Action;
import tr.model.action.ActionState;

/**
 * Action to change status.
 *
 * @author Jeremy Moore
 */
public class ChangeStatusAction extends CookieAction {

    public ChangeStatusAction() {
        setIcon(Icons.StatusChange);
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ChangeStatusAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[] {Action.class, ChangeStatusCookie.class};
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
        if (nodes == null || nodes.length == 0) {
            return;
        }

        Frame frame = WindowManager.getDefault().getMainWindow();

        final JDialog jd = new JDialog(frame, getName(), true);

        String msg;
        if (nodes.length == 1) {
            msg = NbBundle.getMessage(getClass(), "status.change.message.single", nodes[0].toString());
        } else {
            msg = NbBundle.getMessage(getClass(), "status.change.message.plural", nodes.length);
        }
        ChangeStatusPanel panel = new ChangeStatusPanel(msg);

        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);

        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, (PropertyChangeEvent evt) -> {
            jd.dispose();
        });

        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();

        Rectangle r = Utilities.findCenterBounds(jd.getSize());
        jd.setLocation(r.x, r.y);
        jd.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer integer) {
            if (integer.intValue() == JOptionPane.OK_OPTION) {
                changeStatus(nodes, panel.getStatus());
            }
        }
    }

    private void changeStatus(Node[] nodes, ActionState state) {
        for (Node node : nodes) {
            ChangeStatusCookie cookie = node.getCookie(ChangeStatusCookie.class);
            if (cookie != null) {
                cookie.changeStatus(state.copy());
            }
        }
    }
}


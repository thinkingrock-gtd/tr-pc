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

import au.com.trgtd.tr.resource.Icons;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jd.dispose();
            }
        });

        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();

        Rectangle r = Utilities.findCenterBounds(jd.getSize());
        jd.setLocation(r.x, r.y);
        jd.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer) {
            if (((Integer)value).intValue() == JOptionPane.OK_OPTION) {
                changeStatus(nodes, panel.getStatus());
            }
        }
    }

    private void changeStatus(Node[] nodes, ActionState state) {
        for (Node node : nodes) {
            ChangeStatusCookie cookie = (ChangeStatusCookie) node.getCookie(ChangeStatusCookie.class);
            if (cookie != null) {
                cookie.changeStatus(state.copy());
            }
        }
    }
}


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
package au.com.trgtd.tr.view.goals;

import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JDialog.*;
import static javax.swing.JOptionPane.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.goals.ctrl.LevelCtrl;


/**
 * Action to change level.
 *
 * @author Jeremy Moore
 */
public class ChangeLevelAction extends CookieAction {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/goals/resource/LevelChange.png";

    public ChangeLevelAction() {
        setIcon(ImageUtilities.loadImageIcon(ICON_PATH, true));
    }

    /** Gets the display name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ChangeLevelAction");
    }

    /** Gets help context. */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    public Class[] cookieClasses() {
        return new Class[] {ChangeLevelCookie.class};
    }

    public int mode() {
        return MODE_ALL;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    public void performAction(Node[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return;
        }

        Frame frame = WindowManager.getDefault().getMainWindow();

        final JDialog dialog = new JDialog(frame, getName(), true);

        String msg;
        if (nodes.length == 1) {
            msg = NbBundle.getMessage(getClass(), "level.change.message.single", nodes[0].toString());
        } else {
            msg = NbBundle.getMessage(getClass(), "level.change.message.plural", nodes.length);
        }

        ChangeLevelPanel panel = new ChangeLevelPanel(msg);

        JOptionPane op = new JOptionPane(panel, INFORMATION_MESSAGE, OK_CANCEL_OPTION, null, null, null);
        op.addPropertyChangeListener(VALUE_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                dialog.dispose();
            }
        });

        dialog.getContentPane().add(op, "Center" );
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();

        Rectangle r = Utilities.findCenterBounds(dialog.getSize());
        dialog.setLocation(r.x, r.y);
        dialog.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer) {
            if (((Integer)value).intValue() == OK_OPTION) {
                changeLevel(nodes, panel.getLevel());
            }
        }
    }

    private void changeLevel(Node[] nodes, LevelCtrl levelCtrl) {
        if (levelCtrl == null) {
            return;
        }
        for (Node node : nodes) {
            ChangeLevelCookie cookie = node.getCookie(ChangeLevelCookie.class);
            if (cookie != null) {
                cookie.changeLevel(levelCtrl);
            }
        }
    }

}


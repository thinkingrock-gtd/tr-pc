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
package au.com.trgtd.tr.view.goals;

import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
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
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ChangeLevelAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class<ChangeLevelCookie>[] cookieClasses() {
        return new Class[] {ChangeLevelCookie.class};
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

        final JDialog dialog = new JDialog(frame, getName(), true);

        String msg;
        if (nodes.length == 1) {
            msg = NbBundle.getMessage(getClass(), "level.change.message.single", nodes[0].toString());
        } else {
            msg = NbBundle.getMessage(getClass(), "level.change.message.plural", nodes.length);
        }

        ChangeLevelPanel panel = new ChangeLevelPanel(msg);

        JOptionPane op = new JOptionPane(panel, INFORMATION_MESSAGE, OK_CANCEL_OPTION, null, null, null);
        op.addPropertyChangeListener(VALUE_PROPERTY, (PropertyChangeEvent evt) -> {
            dialog.dispose();
        });

        dialog.getContentPane().add(op, "Center" );
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();

        Rectangle r = Utilities.findCenterBounds(dialog.getSize());
        dialog.setLocation(r.x, r.y);
        dialog.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer integer) {
            if (integer.intValue() == OK_OPTION) {
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


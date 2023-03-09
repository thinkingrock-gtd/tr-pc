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

import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import au.com.trgtd.tr.resource.Icons;
import java.awt.Frame;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.action.Action;
import tr.model.future.Future;
import tr.model.information.Information;

/**
 * Action to reprocess an item.
 *
 * @author Jeremy Moore
 */
public class ReprocessAction extends CookieAction {

    public ReprocessAction() {
        setIcon(Icons.Reprocess);
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ReprocessAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[]{Action.class, Information.class, Future.class};
    }

    @Override
    public int mode() {
//      return MODE_EXACTLY_ONE;
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

        // not allowed if there are any recurrent actions
        boolean anyRecurrentActions = false;
        for (Node node : nodes) {
            ReprocessCookie cookie = node.getCookie(ReprocessCookie.class);
            if (cookie != null) {
                if (cookie.isRecurrent()) {
                    anyRecurrentActions = true;
                    break;
                }
            }
        }
        if (anyRecurrentActions) {
            String t = NbBundle.getMessage(getClass(), "reprocess.err.title");
            String m = NbBundle.getMessage(getClass(), "reprocess.err.msg.1") + "\n"
                     + NbBundle.getMessage(getClass(), "reprocess.err.msg.2");
            JOptionPane.showMessageDialog(frame, m, t, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // User confirmation and reprocess now or later options
        String t;
        String m;
        if (nodes.length == 1) {
            m = NbBundle.getMessage(getClass(), "confirm.reprocess.message");
            t = NbBundle.getMessage(getClass(), "confirm.reprocess.title");
        } else {
            m = NbBundle.getMessage(getClass(), "confirm.multiple.reprocess.message", nodes.length);
            t = NbBundle.getMessage(getClass(), "confirm.multiple.reprocess.title");
        }

        String REPROCESS_CANCEL = NbBundle.getMessage(getClass(), "reprocess.cancel");
        String REPROCESS_NOW = NbBundle.getMessage(getClass(), "reprocess.now");
        String REPROCESS_LATER = NbBundle.getMessage(getClass(), "reprocess.later");

        Object[] options = {REPROCESS_CANCEL, REPROCESS_NOW, REPROCESS_LATER};
        int n = JOptionPane.showOptionDialog(frame,
                m,
                t,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[2]); //default button title

        if (n == 0) { // cancel
            return;
        }

        if (n == 1) { // reprocess now
            WindowUtils.closeWindows();
        }

        for (Node node : nodes) {
            ReprocessCookie cookie = node.getCookie(ReprocessCookie.class);
            if (cookie != null) {
                cookie.reprocess();
            }
        }

        if (n == 1) { // reprocess now
            ProcessThoughtsStarter s = ProcessThoughtsStarterLookup.starter;
            if (s != null) {
                s.invoke();
            }
        }
    }
}


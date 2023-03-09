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
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.thought.Thought;

/**
 * Action to process a thought.
 *
 * @author Jeremy Moore
 */
public class ProcessAction extends CookieAction {

    public ProcessAction() {
        setIcon(Icons.ProcessThoughts);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Gets the cookie classes.
     * @return The Thought class.
     */
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Thought.class };
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

        WindowUtils.closeWindows();

        for (Node node : nodes) {
            ProcessCookie cookie = node.getCookie(ProcessCookie.class);
            if (cookie != null) {
                cookie.process();
            }
        }

        ProcessThoughtsStarter s = ProcessThoughtsStarterLookup.starter;
        if (s != null) {
            s.invoke();
        }
    }
}


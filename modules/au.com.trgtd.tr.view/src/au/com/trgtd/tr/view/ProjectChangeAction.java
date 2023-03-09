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
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.action.Action;
import tr.model.project.Project;
import au.com.trgtd.tr.view.project.chooser.ProjectChooser;
import au.com.trgtd.tr.view.project.chooser.ProjectChooserDialog;

/**
 * Action to change project.
 *
 * @author Jeremy Moore
 */
public class ProjectChangeAction extends CookieAction {

    public ProjectChangeAction() {
        setIcon(Icons.ProjectChange);
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProjectChangeAction");
    }

    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[]{Action.class};
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
            String t = NbBundle.getMessage(getClass(), "project.change.warn.title");
            String m = NbBundle.getMessage(getClass(), "project.change.warn.msg.1") + "\n"
                     + NbBundle.getMessage(getClass(), "project.change.warn.msg.2");
            String CONTINUE = NbBundle.getMessage(getClass(), "continue");
            String CANCEL = NbBundle.getMessage(getClass(), "cancel");
            Object[] options = {CONTINUE, CANCEL};
            int n = JOptionPane.showOptionDialog(frame,
                    m,
                    t,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,           //do not use a custom Icon
                    options,        //the titles of buttons
                    options[1]);    //default button title
            if (n != 0) { 
                return;
            }
        }

        // user choose project
        if (projectChooser == null) {
            projectChooser = new ProjectChooser(frame);
        }
        ProjectChooserDialog dialog = projectChooser.getDialog();
        Project project = dialog.select(null, null);
        if (dialog.cancelled() || project == null) {
            return;
        }

        changeProject(nodes, project);
    }

    private void changeProject(Node[] nodes, Project project) {
        if (nodes == null) {
            return;
        }
        for (Node node : nodes) {
            ProjectChangeCookie cookie = node.getCookie(ProjectChangeCookie.class);
            if (cookie != null) {
                cookie.changeProject(project);
            }
        }
    }
    private ProjectChooser projectChooser;
}


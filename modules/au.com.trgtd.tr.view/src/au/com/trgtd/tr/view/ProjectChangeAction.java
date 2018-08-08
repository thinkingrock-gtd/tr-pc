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
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProjectChangeAction");
    }

    /** Gets help context. */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    public Class[] cookieClasses() {
        return new Class[]{Action.class};
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

        // not allowed if there are any recurrent actions
        boolean anyRecurrentActions = false;
        for (Node node : nodes) {
            ReprocessCookie cookie = (ReprocessCookie) node.getCookie(ReprocessCookie.class);
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
            ProjectChangeCookie cookie = (ProjectChangeCookie) node.getCookie(ProjectChangeCookie.class);
            if (cookie != null) {
                cookie.changeProject(project);
            }
        }
    }
    private ProjectChooser projectChooser;
}


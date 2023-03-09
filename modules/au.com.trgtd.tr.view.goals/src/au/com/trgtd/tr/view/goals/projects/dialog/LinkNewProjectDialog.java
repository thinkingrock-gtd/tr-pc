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
package au.com.trgtd.tr.view.goals.projects.dialog;

import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import au.com.trgtd.tr.view.project.ProjectPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.project.Project;

/**
 * Dialog for creating a project.
 *
 * @author Jeremy Moore
 */
public class LinkNewProjectDialog extends JDialog {

//  private static enum Result {OK, CANCEL};
    private static enum Mode {CREATE, MODIFY};
    private Mode mMode;
    private ProjectPanel mPanel;
    private Project mProject;
    private GoalCtrl mGoal;
    private JButton mOkButton;
    private JButton mCancelButton;
//  private Result mResult;

    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public LinkNewProjectDialog() {
        super(WindowManager.getDefault().getMainWindow(), true);
        construct();
    }

    private void construct() {
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        mCancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        mCancelButton.addActionListener(cancelListener);
        ActionListener okListener = (ActionEvent e) -> {
            ok();
        };
        mOkButton = new JButton(NbBundle.getMessage(getClass(), "ok"));
        mOkButton.addActionListener(okListener);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(mCancelButton);
        buttons.add(mOkButton);
        
        mPanel = new ProjectPanel();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(mPanel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800,600));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    private void setMode(Mode mode) {
        mMode = mode;
        if (mMode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create"));
            mOkButton.setText(NbBundle.getMessage(getClass(), "add"));
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify"));
            mOkButton.setText(NbBundle.getMessage(getClass(), "ok"));
        }
    }

    /* Handle OK. */
    private void ok() {
//      mResult = Result.OK;
        Data data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            data.getRootProjects().add(mProject);
            mGoal.insertGoalProject(mProject.getID());
        }
        setVisible(false);
        dispose();
    }

    /* Handle Cancel. */
    private void cancel() {
//      mResult = Result.CANCEL;
        setVisible(false);
        dispose();
    }

    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(mOkButton);
        setVisible(true);
    }

    /** 
     * Shows the dialog in create mode. 
     * @param goal The goal to link the new project to.
     */
    public void showCreateDialog(GoalCtrl goal) {
        if (goal == null) {
            throw new IllegalArgumentException("Goal can not be null.");
        }
        mGoal = goal;
        mProject = createProject();
        if (mProject == null) {
            return;
        }
        mPanel.initModel(mProject);
        setMode(Mode.CREATE);
        showDialog();
    }
    
    private Project createProject() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return null;
        }
        Project p = new Project(data);
        p.setDescription(NbBundle.getMessage(getClass(), "new.project"));
        p.setSequencing(ProjectsPrefs.isSequencing());
        p.setSeqIncludeProjects(ProjectsPrefs.getAutoSeqIncludeSubprojects());
        p.setSeqIncludeScheduled(ProjectsPrefs.getAutoSeqIncludeScheduled());
        p.setSeqIncludeDelegated(ProjectsPrefs.getAutoSeqIncludeDelegated());
        return p;
    }

}

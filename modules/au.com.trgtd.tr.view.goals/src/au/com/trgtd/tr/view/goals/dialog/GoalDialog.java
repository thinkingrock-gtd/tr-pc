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
package au.com.trgtd.tr.view.goals.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Toolkit;
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
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

/**
 * Dialog for creating and modifying goals.
 *
 * @author Jeremy Moore
 */
public class GoalDialog extends JDialog {

    public static enum Result {OK, CANCEL};
    private static enum Mode {CREATE, MODIFY};
    private final GoalsCtrl goalsCtrl;
    private final LevelsCtrl levelsCtrl;
    private Mode mode;
    private GoalDialogPanel panel;
    private GoalCtrl supergoalCtrl;
    private GoalCtrl goalCtrl;
    private JButton okButton;
    private JButton cancelButton;
    private Result result;

    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public GoalDialog() {
        super(WindowManager.getDefault().getMainWindow(), true);
        goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
        construct();
    }

    private void construct() {
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener(cancelListener);
        ActionListener okListener = (ActionEvent e) -> {
            ok();
        };
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok"));
        okButton.addActionListener(okListener);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(okButton);
        panel = new GoalDialogPanel(goalsCtrl, levelsCtrl, okButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800,600));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create"));
            okButton.setText(NbBundle.getMessage(getClass(), "add"));
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify"));
            okButton.setText(NbBundle.getMessage(getClass(), "ok"));
        }
    }

    /* Handle OK. */
    private void ok() {
        if (!panel.isValidInput()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (mode == Mode.MODIFY) {
            goalCtrl.setLevel(panel.getLevel());
            goalCtrl.setTopic(panel.getTopic());
            goalCtrl.setDescr(panel.getDescr());
            goalCtrl.setVision(panel.getVision());
            goalCtrl.setAccountability(panel.getAccountability());
            goalCtrl.setRewards(panel.getRewards());
            goalCtrl.setObstacles(panel.getObstacles());
            goalCtrl.setSupport(panel.getSupport());
            goalCtrl.setBrainstorming(panel.getBrainstorming());
            goalCtrl.setNotes(panel.getNotes());
            goalCtrl.setStartDate(panel.getStart());
            goalCtrl.setEndDate(panel.getEnd());
            goalCtrl.setAchievedDate(panel.getAchieved());
            goalCtrl.update();
            result = Result.OK;
            setVisible(false);
            dispose();
            return;
        }

        goalCtrl = supergoalCtrl.insert(
                panel.getLevelID(),
                panel.getTopicID(),
                panel.getDescr(),
                panel.getVision(),
                panel.getAccountability(),
                panel.getRewards(),
                panel.getObstacles(),
                panel.getSupport(),
                panel.getBrainstorming(),
                panel.getNotes(),
                panel.getStart(),
                panel.getEnd(),
                panel.getAchieved());

        // initialise panel for next goal entry
        panel.setDescr("");
        panel.setVision("");
        panel.setAccountability("");
        panel.setRewards("");
        panel.setObstacles("");
        panel.setSupport("");
        panel.setBrainstorming("");
        panel.setNotes("");
        panel.setStart(null);
        panel.setEnd(null);
        panel.setCreated(null);
        panel.setAchieved(null);
        panel.focus();
    }

    /* Handle Cancel. */
    private void cancel() {
        result = Result.CANCEL;
        setVisible(false);
        dispose();
    }

    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }

    /** Shows the dialog in create mode. */
    public void showCreateDialog(GoalCtrl supergoalCtrl) {
        this.supergoalCtrl = supergoalCtrl;
        panel.setLevel(supergoalCtrl.getLevel());
        panel.setTopic(supergoalCtrl.getTopic());
        panel.setDescr("");
        panel.setVision("");
        panel.setAccountability("");
        panel.setRewards("");
        panel.setObstacles("");
        panel.setSupport("");
        panel.setBrainstorming("");
        panel.setNotes("");
        panel.setStart(null);
        panel.setEnd(null);
        panel.setCreated(null);
        panel.setAchieved(null);
        setMode(Mode.CREATE);
        showDialog();
    }

    /** Shows the dialog in edit mode. */
    public Result showModifyDialog(GoalCtrl goal) {
        if (goal == null) {
            return Result.CANCEL;
        }
        this.goalCtrl = goal;
        panel.setLevel(goal.getLevel());
        panel.setTopic(goal.getTopic());
        panel.setDescr(goal.getDescr());
        panel.setVision(goal.getVision());
        panel.setAccountability(goal.getAccountability());
        panel.setRewards(goal.getRewards());
        panel.setObstacles(goal.getObstacles());
        panel.setSupport(goal.getSupport());
        panel.setBrainstorming(goal.getBrainstorming());
        panel.setNotes(goal.getNotes());
        panel.setStart(goal.getStartDate());
        panel.setEnd(goal.getEndDate());
        panel.setCreated(goal.getCreatedDate());
        panel.setAchieved(goal.getAchievedDate());
        setMode(Mode.MODIFY);
        showDialog();
        return result;
    }

}

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
package au.com.trgtd.tr.view.goals.levels.dialog;

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
import tr.model.goals.ctrl.GoalIcons;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

/**
 * Dialog for creating and modifying goal levels.
 *
 * @author Jeremy Moore
 */
public class LevelDialog extends JDialog {

    public static enum Result {
        OK, CANCEL
    };

    private static enum Mode {
        CREATE, MODIFY
    };

    private final LevelsCtrl levelsCtrl;
    private Mode mode;
    private LevelDialogPanel panel;
    private LevelCtrl levelCtrl;
    private JButton okButton;
    private JButton cancelButton;
    private Result result;

    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public LevelDialog() {
        super(WindowManager.getDefault().getMainWindow(), true);
        levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
        construct();
    }

    private void construct() {
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel")); // NOI18N
        cancelButton.addActionListener(cancelListener);
        ActionListener okListener = (ActionEvent e) -> {
            ok();
        };
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok")); // NOI18N
        okButton.addActionListener(okListener);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(okButton);
        panel = new LevelDialogPanel(okButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(600, 400));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create")); // NOI18N
            okButton.setText(NbBundle.getMessage(getClass(), "add"));  // NOI18N
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify")); // NOI18N
            okButton.setText(NbBundle.getMessage(getClass(), "ok"));  // NOI18N
        }
    }

    /* Handle OK. */
    private void ok() {
        if (!panel.isValidInput()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            levelCtrl.setDescr(panel.getDescr());
            levelCtrl.setGoalsHaveProjects(panel.getHasProjects());
            levelCtrl.setGoalsHaveStartDate(panel.getHasStartDate());
            levelCtrl.setGoalsHaveEndDate(panel.getHasEndDate());
            levelCtrl.setGoalsHaveAccountability(panel.getHasAccountability());
            levelCtrl.setGoalsHaveBrainstorming(panel.getHasBrainstorming());
            levelCtrl.setGoalsHaveObstacles(panel.getHasObstacles());
            levelCtrl.setGoalsHaveRewards(panel.getHasRewards());
            levelCtrl.setGoalsHaveSupport(panel.getHasSupport());
            levelCtrl.setGoalsHaveVision(panel.getHasVision());
            levelCtrl.setGoalsIconPath(panel.getGoalsIconPath());
            levelsCtrl.updateLevel(levelCtrl);
            result = Result.OK;
            setVisible(false);
            dispose();
            return;
        }

        levelsCtrl.insertLevel(
                panel.getDescr(),
                panel.getHasProjects(),
                panel.getHasStartDate(),
                panel.getHasEndDate(),
                panel.getHasVision(),
                panel.getHasAccountability(),
                panel.getHasRewards(),
                panel.getHasObstacles(),
                panel.getHasSupport(),
                panel.getHasVision(),
                panel.getGoalsIconPath());

        panel.focus();
        panel.setDescr("");
        panel.setHasProjects(false);
        panel.setHasStartDate(false);
        panel.setHasEndDate(false);
        panel.setHasAccountability(false);
        panel.setHasBrainstorming(false);
        panel.setHasObstacles(false);
        panel.setHasRewards(false);
        panel.setHasSupport(false);
        panel.setHasVision(false);
        panel.setGoalIcon(GoalIcons.getDefault().getDefaultGoalIcon());
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
    public void showCreateDialog() {
        panel.setLevel(null);
        panel.setDescr("");
        panel.setHasProjects(false);
        panel.setHasStartDate(false);
        panel.setHasEndDate(false);
        panel.setHasAccountability(false);
        panel.setHasBrainstorming(false);
        panel.setHasObstacles(false);
        panel.setHasRewards(false);
        panel.setHasSupport(false);
        panel.setHasVision(false);
        panel.setGoalIcon(GoalIcons.getDefault().getDefaultGoalIcon());
        setMode(Mode.CREATE);
        showDialog();
    }

    public Result showModifyDialog(LevelCtrl level) {
        if (level == null) {
            return Result.CANCEL;
        }
        this.levelCtrl = level;
        panel.setLevel(level);
        panel.setDescr(level.getDescr());
        panel.setHasProjects(level.isGoalsHaveProjects());
        panel.setHasStartDate(level.isGoalsHaveStartDate());
        panel.setHasEndDate(level.isGoalsHaveEndDate());
        panel.setHasAccountability(level.isGoalsHaveAccountability());
        panel.setHasBrainstorming(level.isGoalsHaveBrainstorming());
        panel.setHasObstacles(level.isGoalsHaveObstacles());
        panel.setHasRewards(level.isGoalsHaveRewards());
        panel.setHasSupport(level.isGoalsHaveSupport());
        panel.setHasVision(level.isGoalsHaveVision());
        panel.setGoalIcon(level.getGoalsIcon());
        setMode(Mode.MODIFY);
        showDialog();
        return result;
    }

}

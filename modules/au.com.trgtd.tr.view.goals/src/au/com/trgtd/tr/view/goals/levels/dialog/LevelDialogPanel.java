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

import au.com.trgtd.tr.view.goals.levels.combo.GoalIconsComboBox;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalIcon;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.goals.ctrl.LevelCtrl;

/**
 * Goal level add/edit dialog panel.
 *
 * @author Jeremy Moore
 */
public class LevelDialogPanel extends JPanel {

    /** 
     * Constructs a new instance.
     * @param okButton 
     */
    public LevelDialogPanel(JButton okButton) {
        this.okButton = okButton;
        initView();
    }

    private void initView() {
        descrLabel = new JLabel(NbBundle.getMessage(CLASS, "descr"));
        descrField = new JTextField();
        descrVerifier = new DescrVerifier();
        descrField.setInputVerifier(descrVerifier);
        goalsIconLabel = new JLabel(NbBundle.getMessage(CLASS, "goals.icon"));
        goalsIconCombo = new GoalIconsComboBox();
        goalsHaveLabel = new JLabel(NbBundle.getMessage(CLASS, "goals.have"));
        projectsCheck = new JCheckBox(NbBundle.getMessage(CLASS, "projects"));
        projectsCheck.addActionListener((ActionEvent e) -> {
            if (projectsCheck.isSelected() || levelCtrl == null) {
                return;
            }
            GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
            if (goalsCtrl == null) {
                return;
            }
            if (goalsCtrl.existGoalProjects(levelCtrl)) {
                // show confirm dialog to remove all linked projects
                String msg = NbBundle.getMessage(getClass(), "msg.confirm.remove.projects");
                String ttl = NbBundle.getMessage(getClass(), "ttl.confirm.remove.projects");
                NotifyDescriptor descriptor = new NotifyDescriptor.Confirmation(msg, ttl, NotifyDescriptor.OK_CANCEL_OPTION);
                if (NotifyDescriptor.OK_OPTION == DialogDisplayer.getDefault().notify(descriptor)) {
                    goalsCtrl.removeProjects(levelCtrl);
                } else {
                    projectsCheck.setSelected(true);
                }
            }
        });
        startDateCheck = new JCheckBox(NbBundle.getMessage(CLASS, "start.date"));
        endDateCheck = new JCheckBox(NbBundle.getMessage(CLASS, "end.date"));
        accountabilityCheck = new JCheckBox(NbBundle.getMessage(CLASS, "accountability"));
        brainstormingCheck = new JCheckBox(NbBundle.getMessage(CLASS, "brainstorming"));
        obstaclesCheck = new JCheckBox(NbBundle.getMessage(CLASS, "obstacles"));
        rewardsCheck = new JCheckBox(NbBundle.getMessage(CLASS, "rewards"));
        supportCheck = new JCheckBox(NbBundle.getMessage(CLASS, "support"));
        visionCheck = new JCheckBox(NbBundle.getMessage(CLASS, "vision"));
        messageLabel = new JLabel();
        messageLabel.setForeground(UIManager.getDefaults().getColor("nb.errorForeground"));

        setLayout(new MigLayout("", "4[]2[grow]4", "4[]4[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]32[]4"));
        setPreferredSize(new Dimension(400, 140));
        add(descrLabel, "align left");
        add(descrField, "align left, growx, wrap");
        add(goalsIconLabel, "align left");
        add(goalsIconCombo, "align left, wrap");
        add(goalsHaveLabel, "align left");
        add(projectsCheck, "align left, gap left 10, wrap");
        add(startDateCheck, "skip, align left, gap left 10, wrap");
        add(endDateCheck, "skip, align left, gap left 10, wrap");
        add(accountabilityCheck, "skip, align left, gap left 10, wrap");
        add(brainstormingCheck, "skip, align left, gap left 10, wrap");
        add(obstaclesCheck, "skip, align left, gap left 10, wrap");
        add(rewardsCheck, "skip, align left, gap left 10, wrap");
        add(supportCheck, "skip, align left, gap left 10, wrap");
        add(visionCheck, "skip, align left, gap left 10, wrap");
        add(messageLabel, "skip, align left, growx, wrap");
    }

    public void setLevel(LevelCtrl levelCtrl) {
        this.levelCtrl = levelCtrl;
    }

    public void setDescr(String name) {
        descrField.setText(name);
    }

    public String getDescr() {
        return descrField.getText();
    }

    public boolean getHasProjects() {
        return projectsCheck.isSelected();
    }

    public void setHasProjects(boolean b) {
        projectsCheck.setSelected(b);
    }

    public boolean getHasStartDate() {
        return startDateCheck.isSelected();
    }

    public void setHasStartDate(boolean b) {
        startDateCheck.setSelected(b);
    }

    public boolean getHasEndDate() {
        return endDateCheck.isSelected();
    }

    public void setHasEndDate(boolean b) {
        endDateCheck.setSelected(b);
    }

    public boolean getHasAccountability() {
        return accountabilityCheck.isSelected();
    }

    public void setHasAccountability(boolean b) {
        accountabilityCheck.setSelected(b);
    }

    public boolean getHasBrainstorming() {
        return brainstormingCheck.isSelected();
    }

    public void setHasBrainstorming(boolean b) {
        brainstormingCheck.setSelected(b);
    }

    public boolean getHasObstacles() {
        return obstaclesCheck.isSelected();
    }

    public void setHasObstacles(boolean b) {
        obstaclesCheck.setSelected(b);
    }

    public boolean getHasRewards() {
        return rewardsCheck.isSelected();
    }

    public void setHasRewards(boolean b) {
        rewardsCheck.setSelected(b);
    }

    public boolean getHasSupport() {
        return supportCheck.isSelected();
    }

    public void setHasSupport(boolean b) {
        supportCheck.setSelected(b);
    }

    public boolean getHasVision() {
        return visionCheck.isSelected();
    }

    public void setHasVision(boolean b) {
        visionCheck.setSelected(b);
    }

    public String getGoalsIconPath() {
        GoalIcon goalIcon = getGoalsIcon();
        return goalIcon == null ? "" : goalIcon.path;
    }

    public GoalIcon getGoalsIcon() {
        Object selected = goalsIconCombo.getSelectedItem();
        if (selected instanceof GoalIcon goalIcon) {
            return goalIcon;
        }
        return null;
    }

    public void setGoalIcon(GoalIcon icon) {
        goalsIconCombo.setSelectedItem(icon);
    }

    public void focus() {
        requestFocusInWindow();
        descrField.requestFocusInWindow();
    }

    private class DescrVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextComponent) input).getText().trim();
            boolean valid = text.trim().length() > 0;
            okButton.setEnabled(valid);
            messageLabel.setText(valid ? "" : "Description must be entered.");
            return valid;
        }
    }

    public boolean isValidInput() {
        if (!descrVerifier.verify(descrField)) {
            return false;
        }
        if (goalsIconCombo.getSelectedIndex() == -1) {
            return false;
        }
        return true;
    }

    private final static Class CLASS = LevelDialogPanel.class;
    private final JButton okButton;
    private LevelCtrl levelCtrl;
    private JTextField descrField;
    private JLabel descrLabel;
    private JLabel goalsIconLabel;
    private GoalIconsComboBox goalsIconCombo;
    private JLabel goalsHaveLabel;
    private JCheckBox projectsCheck;
    private JCheckBox startDateCheck;
    private JCheckBox endDateCheck;
    private JCheckBox accountabilityCheck;
    private JCheckBox brainstormingCheck;
    private JCheckBox obstaclesCheck;
    private JCheckBox rewardsCheck;
    private JCheckBox supportCheck;
    private JCheckBox visionCheck;
    private JLabel messageLabel;
    private DescrVerifier descrVerifier;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

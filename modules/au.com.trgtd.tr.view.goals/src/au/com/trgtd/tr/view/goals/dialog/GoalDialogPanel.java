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

import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.mig.MTextArea;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.goals.levels.combo.LevelsComboBox;
import au.com.trgtd.tr.view.notes.NotesEditorPanel;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.topic.Topic;

/**
 * Goal add/edit dialog panel.
 *
 * @author Jeremy Moore
 */
public class GoalDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public GoalDialogPanel(GoalsCtrl goalsCtrl, LevelsCtrl levelsCtrl, JButton selectButton) {
        this.selectButton = selectButton;
        initFields();
        initLayout();
    }

    private void initFields() {
        descrLabel = new TRLabel(NbBundle.getMessage(CLASS, "descr"));
        descrField = new JTextField();
        descrVerifier = new DescrVerifier();
        descrField.setInputVerifier(descrVerifier);
        levelLabel = new TRLabel(NbBundle.getMessage(CLASS, "level"));
        levelCombo = new LevelsComboBox();
        levelCombo.addActionListener((ActionEvent e) -> {
            initLayout();
            levelCombo.requestFocusInWindow();
        });
        levelVerifier = new LevelVerifier();
        levelCombo.setInputVerifier(levelVerifier);
        topicLabel = new TRLabel(NbBundle.getMessage(CLASS, "topic"));
        topicCombo = new TopicsComboBox();
        topicCombo.setSelectedItem(Topic.getDefault());
        topicVerifier = new TopicVerifier();
        topicCombo.setInputVerifier(topicVerifier);
        
        achievedLabel = new TRLabel(NbBundle.getMessage(CLASS, "achieved"));
        achievedCheck = new JCheckBox("");
        achievedCheck.addActionListener((ActionEvent e) -> {
            achievedDate.setDate(achievedCheck.isSelected() ? new Date() : null);
        });
        achievedDate = new DateField();
        achievedDate.addPropertyChangeListener("value", (PropertyChangeEvent evt) -> {
            achievedCheck.setSelected(achievedDate.getDate() != null);
        });
        createdLabel = new TRLabel(NbBundle.getMessage(CLASS, "created"));
        createdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        createdDate = new DateField();
        createdDate.setEnabled(false);
        startLabel = new TRLabel(NbBundle.getMessage(CLASS, "start.date"));
        startDate = new DateField();
        endLabel = new TRLabel(NbBundle.getMessage(CLASS, "end.date"));
        endLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        endDate = new DateField();
        visionLabel = new TRLabel(NbBundle.getMessage(CLASS, "vision"));
        visionField = new MTextArea();
        accountLabel = new TRLabel(NbBundle.getMessage(CLASS, "accountability"));
        accountField = new MTextArea();
        rewardsLabel = new TRLabel(NbBundle.getMessage(CLASS, "rewards"));
        rewardsField = new MTextArea();
        obstaclesLabel = new TRLabel(NbBundle.getMessage(CLASS, "obstacles"));
        obstaclesField = new MTextArea();
        supportLabel = new TRLabel(NbBundle.getMessage(CLASS, "support"));
        supportField = new MTextArea();
        brainstormLabel = new TRLabel(NbBundle.getMessage(CLASS, "brainstorming"));
        brainstormField = new MTextArea();
        notesLabel = new TRLabel(NbBundle.getMessage(CLASS, "notes"));
        notesField = new NotesEditorPanel();
        notesField.setPreferredSize(new Dimension(400, 64));

        FontMetrics fm = endLabel.getFontMetrics(endLabel.getFont());
        int w1 = fm.stringWidth(endLabel.getText());
        int w2 = fm.stringWidth(createdLabel.getText());
        Dimension d = new Dimension(Math.max(w1, w2), endLabel.getHeight());
        endLabel.setPreferredSize(d);
        createdLabel.setPreferredSize(d);
    }

    private void initLayout() {

        LevelCtrl level = getLevel();

        removeAll();


        if (ViewUtils.isAquaLaF()) {
            setLayout(new MigLayout("", "4[]2[grow]2[]2[]4", "4[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]"));
        } else {
            setLayout(new MigLayout("", "4[]2[grow]2[]2[]4",
                    "4[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]"));
        }

        add(descrLabel, "align right");
        add(descrField, "align left, growx, wrap");

        add(levelLabel, "align right");
        add(levelCombo, "align left, spanx, split 3");
        add(topicLabel, "align left");
        add(topicCombo, "align left, wrap");

        ViewUtils.addSeparator(this);
        boolean lastLineWasASeparator = true;

        if (level == null || (level.isGoalsHaveStartDate() && level.isGoalsHaveEndDate())) {
            add(startLabel, "align right");
            add(startDate, "align left, spanx, split 3");
            add(endLabel, "align right");
            add(endDate, "align left, wrap");
            ViewUtils.addSeparator(this);
        } else if (level.isGoalsHaveStartDate()) {
            add(startLabel, "align right");
            add(startDate, "align left, wrap");
            lastLineWasASeparator = false;
        } else if (level.isGoalsHaveEndDate()) {
            add(endLabel, "align right");
            add(endDate, "align left, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveVision()) {
            add(visionLabel, "align right");
            add(visionField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveAccountability()) {
            add(accountLabel, "align right");
            add(accountField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveRewards()) {
            add(rewardsLabel, "align right");
            add(rewardsField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveObstacles()) {
            add(obstaclesLabel, "align right");
            add(obstaclesField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveSupport()) {
            add(supportLabel, "align right");
            add(supportField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (level == null || level.isGoalsHaveBrainstorming()) {
            add(brainstormLabel, "align right");
            add(brainstormField, "align left, gap left 3, gap right 3, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }

        if (!lastLineWasASeparator) {
            ViewUtils.addSeparator(this);
        }

        add(achievedLabel, "align right, split 2");
        add(achievedCheck, "align left");
        add(achievedDate, "align left, , spanx, split 3");
        add(createdLabel, "align right");
        add(createdDate, "align left, wrap");

        ViewUtils.addSeparator(this);

        if (ViewUtils.isAquaLaF()) {
            add(notesLabel, "align right top, gaptop 4, growy 0");
            add(notesField, "align left, gapleft 3, gapright 3, span, push, grow");
        } else {
            add(notesLabel, "align right top, growy 0");
            add(notesField, "align left, span, push, grow");
        }

        revalidate();
        repaint();
    }

    private void verify() {
        this.descrVerifier.verify(descrField);
        this.levelVerifier.verify(levelCombo);
        this.topicVerifier.verify(topicCombo);
    }

    public void setDescr(String name) {
        descrField.setText(name);
        verify();
    }

    public String getDescr() {
        return descrField.getText();
    }

    public void setAchieved(Date date) {
        achievedCheck.setSelected(date != null);
        achievedDate.setDate(date);
    }

    public Date getAchieved() {
        return achievedDate.getDate();
    }

    public void setCreated(Date date) {
        createdDate.setDate(date);
    }

    public void setLevel(LevelCtrl level) {
        levelCombo.setSelectedItem(level);
        verify();
        initLayout();
    }

    public LevelCtrl getLevel() {
        return (LevelCtrl) levelCombo.getSelectedItem();
    }

    public Integer getLevelID() {
        return getLevel() != null ? getLevel().getID() : null;
    }

    public void setTopic(Topic topic) {
        topicCombo.setSelectedItem(topic == null ? Topic.getDefault() : topic);
        verify();
    }

    public Topic getTopic() {
        return (Topic)topicCombo.getSelectedItem();
    }

    public Integer getTopicID() {
        return getTopic() != null ? getTopic().getID() : null;
    }

    public void setNotes(String notes) {
        notesField.setText(notes);
    }

    public String getNotes() {
        return notesField.getText();
    }

    public void focus() {
        requestFocusInWindow();
        descrField.requestFocusInWindow();
    }

    private class DescrVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            boolean valid = ((JTextComponent) input).getText().trim().length() > 0;
            selectButton.setEnabled(valid);
            return valid;
        }
    }

    private class LevelVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            boolean valid = ((JComboBox) input).getSelectedIndex() > -1;
            selectButton.setEnabled(valid);
            return valid;
        }
    }

    private class TopicVerifier extends InputVerifier {
        public boolean verify(JComponent input) {
            boolean valid = ((JComboBox) input).getSelectedIndex() > -1;
            selectButton.setEnabled(valid);
            return valid;
        }
    }

    public boolean isValidInput() {
        return descrField.getText().trim().length() > 0 
                && getLevel() != null
                && getTopic() != null;
    }

    public String getAccountability() {
        return accountField.getText();
    }

    public void setAccountability(String s) {
        accountField.setText(s);
    }

    public String getBrainstorming() {
        return brainstormField.getText();
    }

    public void setBrainstorming(String s) {
        brainstormField.setText(s);
    }

    public String getObstacles() {
        return obstaclesField.getText();
    }

    public void setObstacles(String s) {
        obstaclesField.setText(s);
    }

    public String getRewards() {
        return rewardsField.getText();
    }

    public void setRewards(String s) {
        rewardsField.setText(s);
    }

    public String getSupport() {
        return supportField.getText();
    }

    public void setSupport(String s) {
        supportField.setText(s);
    }

    public String getVision() {
        return visionField.getText();
    }

    public void setVision(String s) {
        visionField.setText(s);
    }

    public void setStart(Date date) {
        startDate.setDate(date);
    }

    public Date getStart() {
        return startDate.getDate();
    }

    public void setEnd(Date date) {
        endDate.setDate(date);
    }

    public Date getEnd() {
        return endDate.getDate();
    }
    private final static Class CLASS = GoalDialogPanel.class;
    private final JButton selectButton;
    private TRLabel descrLabel;
    private JTextField descrField;
    private DescrVerifier descrVerifier;
    private TRLabel levelLabel;
    private LevelsComboBox levelCombo;
    private LevelVerifier levelVerifier;
    private TRLabel topicLabel;
    private TopicsComboBox topicCombo;
    private TopicVerifier topicVerifier;
    private TRLabel achievedLabel;
    private JCheckBox achievedCheck;
    private DateField achievedDate;
    private TRLabel createdLabel;
    private DateField createdDate;
    private TRLabel visionLabel;
    private MTextArea visionField;
    private TRLabel accountLabel;
    private MTextArea accountField;
    private TRLabel rewardsLabel;
    private MTextArea rewardsField;
    private TRLabel obstaclesLabel;
    private MTextArea obstaclesField;
    private TRLabel supportLabel;
    private MTextArea supportField;
    private TRLabel brainstormLabel;
    private MTextArea brainstormField;
    private TRLabel notesLabel;
    private NotesEditorPanel notesField;
    private TRLabel startLabel;
    private DateField startDate;
    private TRLabel endLabel;
    private DateField endDate;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

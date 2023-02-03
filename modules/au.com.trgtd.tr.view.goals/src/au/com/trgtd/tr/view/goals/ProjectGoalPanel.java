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

import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.ui.view.NotesView;
import au.com.trgtd.tr.view.ui.view.TopicView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.LevelCtrl;

/**
 * Project goal view panel.
 *
 * @author Jeremy Moore
 */
//public class ProjectGoalPanel extends JPanel {
public class ProjectGoalPanel extends JScrollPane {

    /** Constructs a new instance. */
    public ProjectGoalPanel() {
        this(new JPanel());
    }
    
    private ProjectGoalPanel(JPanel panel) {
        super(panel);
        this.panel = panel;
        initFields();
        initLayout();
    }

    private void initFields() {              
        descrLabel = new Label(NbBundle.getMessage(CLASS, "descr"));
        descrView = new TextFieldView();

        levelLabel = new Label(NbBundle.getMessage(CLASS, "level"));
        levelView = new TextFieldView();

        topicLabel = new Label(NbBundle.getMessage(CLASS, "topic"));
        topicView = new TopicView();

        achievedLabel = new Label(NbBundle.getMessage(CLASS, "achieved"));
        achievedCheck = new JCheckBox("");
        achievedCheck.setEnabled(false);
        achievedDate = new TextFieldView();

        createdLabel = new Label(NbBundle.getMessage(CLASS, "created"));
        createdDate = new TextFieldView();

        startLabel = new Label(NbBundle.getMessage(CLASS, "start.date"));
        startDate = new TextFieldView();

        endLabel = new Label(NbBundle.getMessage(CLASS, "end.date"));
        endDate = new TextFieldView();

        visionLabel = new Label(NbBundle.getMessage(CLASS, "vision"));
        visionView = new TextAreaView();

        accountLabel = new Label(NbBundle.getMessage(CLASS, "accountability"));
        accountField = new TextAreaView();

        rewardsLabel = new Label(NbBundle.getMessage(CLASS, "rewards"));
        rewardsField = new TextAreaView();

        obstaclesLabel = new Label(NbBundle.getMessage(CLASS, "obstacles"));
        obstaclesField = new TextAreaView();

        supportLabel = new Label(NbBundle.getMessage(CLASS, "support"));
        supportField = new TextAreaView();

        brainstormLabel = new Label(NbBundle.getMessage(CLASS, "brainstorming"));
        brainstormField = new TextAreaView();

        notesLabel = new Label(NbBundle.getMessage(CLASS, "notes"));
        notesField = new NotesView();
    }

    private void initLayout() {
        panel.removeAll();
        panel.setOpaque(true);
        panel.setBackground(ViewUtils.COLOR_PANEL_BG);

        if (ViewUtils.isAquaLaF()) {
            panel.setLayout(new MigLayout("", "4[]4[]4[]4[]4[grow]", "4[]4[]4[]4[]4[]4[]4[]4[]4[]4[]4[]4[]4[]"));
        } else {
            panel.setLayout(new MigLayout("", "4[]4[]4[]4[]4[grow]", "4[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]2[top]"));
        }

        panel.add(descrLabel, "align right");
        panel.add(descrView, "align left, spanx, growx 100");        
        panel.add(levelLabel, "align right");
        panel.add(levelView, "align left");
        panel.add(topicLabel, "align right");
        panel.add(topicView, "align left, wrap");

        ViewUtils.addSeparator(panel);
        boolean lastLineWasASeparator = true;

        if (levelCtrl == null || (levelCtrl.isGoalsHaveStartDate() && levelCtrl.isGoalsHaveEndDate())) {
            panel.add(startLabel, "align right");
            panel.add(startDate, "align left, growx 0");
            panel.add(endLabel, "align right");
            panel.add(endDate, "align left, wrap");
            ViewUtils.addSeparator(panel);
        } else if (levelCtrl.isGoalsHaveStartDate()) {
            panel.add(startLabel, "align right");
            panel.add(startDate, "align left, wrap");
            lastLineWasASeparator = false;
        } else if (levelCtrl.isGoalsHaveEndDate()) {
            panel.add(endLabel, "align right");
            panel.add(endDate, "align left, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveVision()) {
            panel.add(visionLabel, "align right");
            panel.add(visionView, "align left, spanx, growx, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveAccountability()) {
            panel.add(accountLabel, "align right");
            panel.add(accountField, "align left, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveRewards()) {
            panel.add(rewardsLabel, "align right");
            panel.add(rewardsField, "align left, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveObstacles()) {
            panel.add(obstaclesLabel, "align right");
            panel.add(obstaclesField, "align left, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveSupport()) {
            panel.add(supportLabel, "align right");
            panel.add(supportField, "align left, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }
        if (levelCtrl == null || levelCtrl.isGoalsHaveBrainstorming()) {
            panel.add(brainstormLabel, "align right");
            panel.add(brainstormField, "align left, spanx, growx 100, growy 0, wrap");
            lastLineWasASeparator = false;
        }

        if (!lastLineWasASeparator) {
            ViewUtils.addSeparator(panel);
        }

        panel.add(achievedLabel, "align right, split 2");
        panel.add(achievedCheck, "align left");
        panel.add(achievedDate, "align left, growx 0");
        panel.add(createdLabel, "align right");
        panel.add(createdDate, "align left, wrap");

        ViewUtils.addSeparator(panel);

        if (ViewUtils.isAquaLaF()) {
            panel.add(notesLabel, "align right top, growy 0");
            panel.add(notesField, "align left, span, push, grow");
        } else {
            panel.add(notesLabel, "align right top, growy 0");
            panel.add(notesField, "align left, span, push, grow");
        }

        panel.revalidate();
        panel.repaint();
    }
    
    private String format(Date date) {
        return date == null ? " " : DatesPrefs.formatMedium(date);
    }

    public GoalCtrl getModel() {
        return goalCtrl;
    }

    public void initModel(GoalCtrl newGoalCtrl) {

        // remove listeners to current model
        if (goalCtrl != null) {
            goalCtrl.removePropertyChangeListener(changeListener);
        }
        if (levelCtrl != null) {
            levelCtrl.removePropertyChangeListener(changeListener);
        }

        if (newGoalCtrl == null) {
            goalCtrl = null;
            levelCtrl = null;
            clearFields();
        } else {
            goalCtrl = newGoalCtrl;
            goalCtrl.addPropertyChangeListener(changeListener);
            levelCtrl = goalCtrl.getLevel();
            if (levelCtrl != null) {
                levelCtrl.addPropertyChangeListener(changeListener);
            }
            setFields();
        }
    }

    private void clearFields() {
        descrView.setText("");
        levelView.setText("");
        topicView.setTopic(null);
        achievedCheck.setSelected(false);
        achievedDate.setText("");
        createdDate.setText("");
        visionView.setText("");
        accountField.setText("");
        rewardsField.setText("");
        obstaclesField.setText("");
        supportField.setText("");
        brainstormField.setText("");
        notesField.setNotable(null);
        startDate.setText("");
        endDate.setText("");
    }

    private void setFields() {
        descrView.setText(goalCtrl.getDescr());
        LevelCtrl level = goalCtrl.getLevel();
        levelView.setText(level == null ? "" : level.getDescr());
        topicView.setTopic(goalCtrl.getTopic());
        achievedCheck.setSelected(goalCtrl.isAchieved());
        achievedDate.setText(format(goalCtrl.getAchievedDate()));
        createdDate.setText(format(goalCtrl.getCreatedDate()));
        visionView.setText(goalCtrl.getVision());
        accountField.setText(goalCtrl.getAccountability());
        rewardsField.setText(goalCtrl.getRewards());
        obstaclesField.setText(goalCtrl.getObstacles());
        supportField.setText(goalCtrl.getSupport());
        brainstormField.setText(goalCtrl.getBrainstorming());
        notesField.setNotable(goalCtrl);
        startDate.setText(format(goalCtrl.getStartDate()));
        endDate.setText(format(goalCtrl.getEndDate()));
        
        // force scroll to top
        SwingUtilities.invokeLater(() -> {
            getViewport().setViewPosition(new Point(0, 0));
        });
    }

    private final PropertyChangeListener changeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            initModel(goalCtrl);
        }
    };

    private final static Class CLASS = ProjectGoalPanel.class;
    private final static Color COLOR_PANEL_BG = ViewUtils.COLOR_PANEL_BG;
    private final static Color COLOR_FIELD_BG = ViewUtils.lighter(COLOR_PANEL_BG, 5);
    private final static Color COLOR_FIELD_FG = Color.BLACK;
    private final static Color COLOR_LABEL_FG = Color.DARK_GRAY;
    
    private JPanel panel;
    private GoalCtrl goalCtrl;
    private LevelCtrl levelCtrl;
    private Label descrLabel;
    private TextFieldView descrView;
    private Label levelLabel;
    private TextFieldView levelView;
    private Label topicLabel;
    private TopicView topicView;
    private Label achievedLabel;
    private JCheckBox achievedCheck;
    private TextFieldView achievedDate;
    private Label createdLabel;
    private TextFieldView createdDate;
    private Label visionLabel;
    private TextAreaView visionView;
    private Label accountLabel;
    private TextAreaView accountField;
    private Label rewardsLabel;
    private TextAreaView rewardsField;
    private Label obstaclesLabel;
    private TextAreaView obstaclesField;
    private Label supportLabel;
    private TextAreaView supportField;
    private Label brainstormLabel;
    private TextAreaView brainstormField;
    private Label notesLabel;
    private NotesView notesField;
    private Label startLabel;
    private TextFieldView startDate;
    private Label endLabel;
    private TextFieldView endDate;

    private class Label extends TRLabel {
        public Label(String string) {
            super(string);
            setOpaque(false);
            setForeground(COLOR_LABEL_FG);
        }
    }

    private class TextAreaView extends JTextArea {
        public TextAreaView() {
            setOpaque(true);
            setBackground(ViewUtils.lighter(COLOR_PANEL_BG, 5));
            setBorder(new EmptyBorder(1,4,1,4));
            setEditable(false);
        }
    }

    private class TextFieldView extends JTextField {
        public TextFieldView() {
            super();
            setBorder(new EmptyBorder(1,4,1,4));
            setOpaque(true);
            setBackground(COLOR_FIELD_BG);
            setForeground(COLOR_FIELD_FG);
            setEditable(false);
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension d = super.getMinimumSize();
            d.setSize(110, d.height);
            return d;
        }

    }

}

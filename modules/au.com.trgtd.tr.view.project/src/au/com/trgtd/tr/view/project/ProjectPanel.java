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
package au.com.trgtd.tr.view.project;

import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import au.com.trgtd.tr.swing.TRCheckBox;
import au.com.trgtd.tr.swing.mig.MTextArea;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.topic.Topic;
import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.TRTextField;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.document.LazyDocumentListener;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.criteria.PriorityComboBoxModel;
import au.com.trgtd.tr.view.notes.NotesViewField;
import au.com.trgtd.tr.view.notes.NotesViewLabel;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import au.com.trgtd.tr.view.topics.TopicsComboBoxModel;
import java.awt.Point;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.*;
import tr.model.action.Action;

/**
 * Project panel.
 *
 * @author Jeremy Moore
 */
public final class ProjectPanel extends JPanel { // implements Observer {

    /** Constructs a new instance. */
    public ProjectPanel() {
        super();
        initPanel();
        initModel(null);
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            dataChanged();
        });
        dataChanged();
    }

    private void dataChanged() {
        Data data = this.getData();
        if (data == null) {
            return;
        }
        if (observerCriterion == null) {
            observerCriterion = (Observable observable, Object arguement) -> {
                showHideCriteria();
            };
        }
        getData().getPriorityCriterion().addObserver(observerCriterion);
    }

    private Data getData() {
        return DataLookup.instance().lookup(Data.class);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        addListeners();
    }

    @Override
    public void removeNotify() {
        removeListeners();
        super.removeNotify();
    }

    /** Start editing the current project if possible. */
    public void edit() {
        descriptionText.requestFocusInWindow();
    }

    private ComboBoxModel<Value> getPriorityComboBoxModel() {
        return new PriorityComboBoxModel();
    }

    private void descriptionTextFocusGained(FocusEvent evt) {
        if (descriptionText.getText().equals(DEFAULT_DESCRIPTION)) {
            descriptionText.setSelectionStart(0);
            descriptionText.setSelectionEnd(DEFAULT_DESCRIPTION.length());
        }
    }

    private void topicComboActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        TRComboBox combo = (TRComboBox) evt.getSource();

        project.setTopic((Topic) combo.getSelectedItem());
    }

    private void priorityComboActionPerformed(java.awt.event.ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        project.setPriority((Value) priorityCombo.getSelectedItem());
    }

    private void sequencingCheckBoxActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        project.setSequencing(sequencingCheckBox.isSelected());
        seqIncludeLabel.setEnabled(sequencingCheckBox.isSelected());
        seqProjectsCheckBox.setEnabled(sequencingCheckBox.isSelected());
        seqScheduledCheckBox.setEnabled(sequencingCheckBox.isSelected());
        seqDelegatedCheckBox.setEnabled(sequencingCheckBox.isSelected());
    }

    private void seqProjectsCheckBoxActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        project.setSeqIncludeProjects(seqProjectsCheckBox.isSelected());
    }

    private void seqDelegatedCheckBoxActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        project.setSeqIncludeDelegated(seqDelegatedCheckBox.isSelected());
    }

    private void seqScheduledCheckBoxActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        project.setSeqIncludeScheduled(seqScheduledCheckBox.isSelected());
    }

    
    private void completedCheckBoxActionPerformed(ActionEvent evt) {
        if (updating || project == null) {
            return;
        }
        if (doneCheckBox.isSelected() && !project.canSetDone(true)) {
            doneCheckBox.setSelected(false);
            // CHANGED - Mantis: 0001958 - Force project done after confirm. 
//          String t = "";
//          String m = org.openide.util.NbBundle.getMessage(ProjectPanel.class, "message.can.not.set.done");
//          Component p = WindowManager.getDefault().getMainWindow();
//          JOptionPane.showMessageDialog(p, m, t, JOptionPane.WARNING_MESSAGE);
            Component p = WindowManager.getDefault().getMainWindow();
            String t = NbBundle.getMessage(ProjectPanel.class, "ttl.confirm.force.done");
            String m = NbBundle.getMessage(ProjectPanel.class, "msg.confirm.force.done");
            String y = UIManager.getString("OptionPane.yesButtonText");
            String n = UIManager.getString("OptionPane.noButtonText");
            Object[] opts = {y, n};            
            int rslt = JOptionPane.showOptionDialog(p, m, t, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, opts, opts[1]);
            if (rslt == 0) {
                forceDone(project, new Date());
            }
            // END CHANGE            
        } else {
            project.setDone(doneCheckBox.isSelected());
            doneDateField.setDate(project.getDoneDate());
        }
        doneDateField.setEnabled(project.isDone());
    }

    // ADDED for Mantis ID: 0001958
    private void forceDone(Project p, Date d) {
        // recursively call for all project children
        for (Project sp : p.getChildren(Project.class)) {
            forceDone(sp, d);
        }
        // set all action children done
        for (Action a : p.getChildren(Action.class)) {
            a.setDoneDate(d);
        }
        // set project done
        p.setDoneDate(d);
    }
    // END ADDED
    
    private void changedCreatedDate() {
        if (updating || project == null) {
            return;
        }
        project.setCreated(createdDateField.getDate());
    }

    private void changedStartDate() {
        if (updating || project == null) {
            return;
        }
        project.setStartDate(startDateField.getDate());
    }

    private void changedDueDate() {
        if (updating || project == null) {
            return;
        }
        project.setDueDate(dueDateField.getDate());
    }

    private void changedCompletedDate() {
        if (updating || project == null) {
            return;
        }
        project.setDoneDate(doneDateField.getDate());
    }

    private void addListeners() {

        prefsChangeListener = (PreferenceChangeEvent arg0) -> {
            changedPrefs();
        };
        ProjectsPrefs.getPrefs().addPreferenceChangeListener(prefsChangeListener);

        docListenerDescription = new LazyDocumentListener() {
            public void update() {
                changedDescription();
            }
        };
        descriptionText.getDocument().addDocumentListener(docListenerDescription);

        actionListenerPriorityCombo = (ActionEvent evt) -> {
            priorityComboActionPerformed(evt);
        };
        priorityCombo.addActionListener(actionListenerPriorityCombo);

        docListenerPurpose = new LazyDocumentListener() {
            public void update() {
                changedPurpose();
            }
        };
        purposeTextArea.getDocument().addDocumentListener(docListenerPurpose);

        docListenerBrainstorm = new LazyDocumentListener() {
            public void update() {
                changedBrainstorm();
            }
        };
        brainstormTextArea.getDocument().addDocumentListener(docListenerBrainstorm);

        docListenerOrganise = new LazyDocumentListener() {
            public void update() {
                changedOrganise();
            }
        };
        organiseTextArea.getDocument().addDocumentListener(docListenerOrganise);

        docListenerVision = new LazyDocumentListener() {
            public void update() {
                changedVision();
            }
        };
        visionTextArea.getDocument().addDocumentListener(docListenerVision);

        focusAdapterDescription = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                descriptionTextFocusGained(evt);
            }
        };
        descriptionText.addFocusListener(focusAdapterDescription);

        actionListenerTopic = (ActionEvent evt) -> {
            topicComboActionPerformed(evt);
        };
        topicCombo.addActionListener(actionListenerTopic);

        actionListenerSequencingCheckBox = (ActionEvent evt) -> {
            sequencingCheckBoxActionPerformed(evt);
        };
        sequencingCheckBox.addActionListener(actionListenerSequencingCheckBox);

        actionListenerSeqProjectsCheckBox = (ActionEvent evt) -> {
            seqProjectsCheckBoxActionPerformed(evt);
        };
        seqProjectsCheckBox.addActionListener(actionListenerSeqProjectsCheckBox);

        actionListenerSeqScheduledCheckBox = (ActionEvent evt) -> {
            seqScheduledCheckBoxActionPerformed(evt);
        };
        seqScheduledCheckBox.addActionListener(actionListenerSeqScheduledCheckBox);

        actionListenerSeqDelegatedCheckBox = (ActionEvent evt) -> {
            seqDelegatedCheckBoxActionPerformed(evt);
        };
        seqDelegatedCheckBox.addActionListener(actionListenerSeqDelegatedCheckBox);

        createdDatePropertyChangeListener = (PropertyChangeEvent e) -> {
            changedCreatedDate();
        };
        createdDateField.addPropertyChangeListener("value", createdDatePropertyChangeListener);

        startDatePropertyChangeListener = (PropertyChangeEvent e) -> {
            changedStartDate();
        };
        startDateField.addPropertyChangeListener("value", startDatePropertyChangeListener);

        dueDatePropertyChangeListener = (PropertyChangeEvent e) -> {
            changedDueDate();
        };
        dueDateField.addPropertyChangeListener("value", dueDatePropertyChangeListener);

        actionListenerDoneCheckBox = (ActionEvent evt) -> {
            completedCheckBoxActionPerformed(evt);
        };
        doneCheckBox.addActionListener(actionListenerDoneCheckBox);

        doneDatePropertyChangeListener = (PropertyChangeEvent e) -> {
            changedCompletedDate();
        };
        doneDateField.addPropertyChangeListener("value", doneDatePropertyChangeListener);
    }

    private void removeListeners() {
        ProjectsPrefs.getPrefs().removePreferenceChangeListener(prefsChangeListener);
        prefsChangeListener = null;

        descriptionText.getDocument().removeDocumentListener(docListenerDescription);
        docListenerDescription = null;

        purposeTextArea.getDocument().removeDocumentListener(docListenerPurpose);
        docListenerPurpose = null;

        brainstormTextArea.getDocument().removeDocumentListener(docListenerBrainstorm);
        docListenerBrainstorm = null;

        organiseTextArea.getDocument().removeDocumentListener(docListenerOrganise);
        docListenerOrganise = null;

        visionTextArea.getDocument().removeDocumentListener(docListenerVision);
        docListenerVision = null;

        descriptionText.removeFocusListener(focusAdapterDescription);
        focusAdapterDescription = null;

        topicCombo.removeActionListener(actionListenerTopic);
        actionListenerTopic = null;

        sequencingCheckBox.removeActionListener(actionListenerSequencingCheckBox);
        actionListenerSequencingCheckBox = null;

        seqProjectsCheckBox.removeActionListener(actionListenerSeqProjectsCheckBox);
        actionListenerSeqProjectsCheckBox = null;

        seqScheduledCheckBox.removeActionListener(actionListenerSeqScheduledCheckBox);
        actionListenerSeqScheduledCheckBox = null;

        seqDelegatedCheckBox.removeActionListener(actionListenerSeqDelegatedCheckBox);
        actionListenerSeqDelegatedCheckBox = null;

        createdDateField.removePropertyChangeListener(createdDatePropertyChangeListener);
        createdDatePropertyChangeListener = null;

        startDateField.removePropertyChangeListener(startDatePropertyChangeListener);
        startDatePropertyChangeListener = null;

        dueDateField.removePropertyChangeListener(dueDatePropertyChangeListener);
        dueDatePropertyChangeListener = null;

        doneCheckBox.removeActionListener(actionListenerDoneCheckBox);
        actionListenerDoneCheckBox = null;

        doneDateField.removePropertyChangeListener(doneDatePropertyChangeListener);
        doneDatePropertyChangeListener = null;
        priorityCombo.removeActionListener(actionListenerPriorityCombo);
        actionListenerPriorityCombo = null;
    }

    /**
     * Gets the initialized data model.
     * @return The current project or null if there is none.
     */
    public Project getModel() {
        return project;
    }

    /**
     * Initializes the project data model and view.
     * @param project The project data model.
     */
    public void initModel(Project project) {
        if (Utils.equal(this.project, project)) {
            return;
        }
        if (this.project != null) {
            this.project.removePropertyChangeListener(Project.PROP_DONE, doneListener);
            this.project.removePropertyChangeListener(Project.PROP_DESCR, descrListener);
            this.project.removePropertyChangeListener(Project.PROP_TOPIC, topicListener);
        }
        this.project = project;
        if (this.project != null) {
            this.project.addPropertyChangeListener(Project.PROP_DONE, doneListener);
            this.project.addPropertyChangeListener(Project.PROP_DESCR, descrListener);
            this.project.addPropertyChangeListener(Project.PROP_TOPIC, topicListener);
        }
        initView();
    }

    /* Initialises the view form field values for the data model. */
    private void initView() {
        updating = true;
        if (project == null) {
            descriptionText.setText("");
            topicCombo.setSelectedItem(null);
            priorityCombo.setSelectedItem(null);
            purposeTextArea.setText("");
            visionTextArea.setText("");
            brainstormTextArea.setText("");
            organiseTextArea.setText("");
            notesViewLabel.setNotable(null);
            notesViewField.setNotable(null);
            sequencingCheckBox.setSelected(false);
            seqProjectsCheckBox.setSelected(false);
            seqScheduledCheckBox.setSelected(false);
            seqDelegatedCheckBox.setSelected(false);
            startDateField.setDate(null);
            dueDateField.setDate(null);
            createdDateField.setDate(null);
            doneCheckBox.setSelected(false);
            doneDateField.setDate(null);
            setEnabled(false);
        } else {
            descriptionText.setText(project.getDescription());
            topicCombo.setSelectedItem(project.getTopic());
            priorityCombo.setSelectedItem(project.getPriority());
            purposeTextArea.setText(project.getPurpose());
            visionTextArea.setText(project.getVision());
            brainstormTextArea.setText(project.getBrainstorming());
            organiseTextArea.setText(project.getOrganising());
            notesViewLabel.setNotable(project);
            notesViewField.setNotable(project);
            boolean sequence = ProjectsPrefs.isSequencing();
            sequencingCheckBox.setVisible(sequence);
            sequencingCheckBox.setSelected(project.isSequencing());
            seqIncludeLabel.setVisible(sequence);
            seqProjectsCheckBox.setVisible(sequence);
            seqProjectsCheckBox.setSelected(project.isSeqIncludeProjects());
            seqScheduledCheckBox.setVisible(sequence);
            seqScheduledCheckBox.setSelected(project.isSeqIncludeScheduled());
            seqDelegatedCheckBox.setVisible(sequence);
            seqDelegatedCheckBox.setSelected(project.isSeqIncludeDelegated());
            startDateField.setDate(project.getStartDate());
            dueDateField.setDate(project.getDueDate());
            createdDateField.setDate(project.getCreated());
            doneCheckBox.setSelected(project.isDone());
            doneDateField.setDate(project.getDoneDate());
            setEnabled(true);
        }

        // scroll to top
        SwingUtilities.invokeLater(() -> {
            scrollpane.getViewport().setViewPosition(new Point(0,0));
        });

        updating = false;
    }

    /** Override to enable/disable fields. */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        descriptionLabel.setEnabled(enabled);
        descriptionText.setEnabled(enabled);
        topicLabel.setEnabled(enabled);
        topicCombo.setEnabled(enabled);
        purposeLabel.setEnabled(enabled);
        purposeTextArea.setEnabled(enabled);
        visionLabel.setEnabled(enabled);
        visionTextArea.setEnabled(enabled);
        brainstormLabel.setEnabled(enabled);
        brainstormTextArea.setEnabled(enabled);
        organiseLabel.setEnabled(enabled);
        organiseTextArea.setEnabled(enabled);
        notesViewLabel.setEnabled(enabled);
        notesViewField.setEnabled(enabled);
        createdDateLabel.setEnabled(enabled);
        createdDateField.setEnabled(enabled && ProjectsPrefs.isEditCreateDate());
        startDateLabel.setEnabled(enabled);
        startDateField.setEnabled(enabled);
        startDateLabel.setVisible(ProjectsPrefs.isIncludeStartDate());
        startDateField.setVisible(ProjectsPrefs.isIncludeStartDate());
        dueDateLabel.setEnabled(enabled);
        dueDateField.setEnabled(enabled);
        dueDateLabel.setVisible(ProjectsPrefs.isIncludeDueDate());
        dueDateField.setVisible(ProjectsPrefs.isIncludeDueDate());
        sequencingCheckBox.setEnabled(enabled);
        seqIncludeLabel.setEnabled(enabled && project.isSequencing());
        seqProjectsCheckBox.setEnabled(enabled && project.isSequencing());
        seqScheduledCheckBox.setEnabled(enabled && project.isSequencing());
        seqDelegatedCheckBox.setEnabled(enabled && project.isSequencing());
        doneCheckBox.setEnabled(enabled);
        doneDateField.setEnabled(enabled && project.canSetDone(true));
        priorityLabel.setEnabled(enabled);
        priorityCombo.setEnabled(enabled);
        showHideCriteria();
        changedPrefs();
    }

    private void showHideCriteria() {
        Data data = getData();
        priorityLabel.setVisible(data.getPriorityCriterion().isUse());
        priorityCombo.setVisible(data.getPriorityCriterion().isUse());
    }

    public void focus() {
        descriptionText.requestFocus();
    }

    private void changedPrefs() {
//        removeAll();
//        JPanel panel = createPanel();
//        add(panel, BorderLayout.CENTER);
//        panel.revalidate();
//        panel.repaint();
        scrollpane.setViewportView(createPanel());
    }

    private void changedDescription() {
        if (updating || project == null) {
            return;
        }
        project.setDescription(descriptionText.getText());
    }

    private void changedPurpose() {
        if (updating || project == null) {
            return;
        }
        project.setPurpose(purposeTextArea.getText());
    }

    private void changedVision() {
        if (updating || project == null) {
            return;
        }
        project.setVision(visionTextArea.getText());
    }

    private void changedBrainstorm() {
        if (updating || project == null) {
            return;
        }
        project.setBrainstorming(brainstormTextArea.getText());
    }

    private void changedOrganise() {
        if (updating || project == null) {
            return;
        }
        project.setOrganising(organiseTextArea.getText());
    }

    /** Action description changes. */
    private final PropertyChangeListener descrListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (descriptionText.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!descriptionText.getText().equals(project.getDescription())) {
                descriptionText.setText(project.getDescription());
            }
        }
    };

    /** Project topic listener. */
    private final PropertyChangeListener topicListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (topicCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(project.getTopic(), topicCombo.getSelectedItem())) {
                topicCombo.setSelectedItem(project.getTopic());
            }

        }
    };

    /** Project done listener. */
    private final PropertyChangeListener doneListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (doneCheckBox.isSelected() != project.isDone()) {
                doneCheckBox.setSelected(project.isDone());
                doneDateField.setDate(project.getDoneDate());
                doneDateField.setEnabled(project.canSetDone(true));
            }
        }
    };

    private String getMsg(String key) {
        return NbBundle.getMessage(ProjectPanel.class, key);
    }

    private void initPanel() {
        descriptionLabel = new TRLabel();
        descriptionLabel.setText(getMsg("ProjectPanel.description"));
        descriptionText = new TRTextField();
        topicLabel = new TRLabel();
        topicLabel.setText(getMsg("ProjectPanel.topic"));
        topicsModel = new TopicsComboBoxModel(false);
        topicCombo = new TopicsComboBox(topicsModel);
        priorityLabel = new TRLabel();
        priorityLabel.setText(getMsg("LBL_Priority"));
        priorityCombo = new TRComboBox<>();
        priorityCombo.setModel(getPriorityComboBoxModel());
        sequencingCheckBox = new TRCheckBox();
        sequencingCheckBox.setHorizontalTextPosition(TRCheckBox.LEFT);
        sequencingCheckBox.setText(getMsg("ProjectPanel.sequencing"));
        seqIncludeLabel = new JLabel(getMsg("ProjectPanel.seq.include"));
        seqProjectsCheckBox = new TRCheckBox();
        seqProjectsCheckBox.setHorizontalTextPosition(TRCheckBox.LEFT);
        seqProjectsCheckBox.setText(getMsg("ProjectPanel.seq.subprojects"));
        seqDelegatedCheckBox = new TRCheckBox();
        seqDelegatedCheckBox.setHorizontalTextPosition(TRCheckBox.LEFT);
        seqDelegatedCheckBox.setText(getMsg("ProjectPanel.seq.delegated"));
        seqScheduledCheckBox = new TRCheckBox();
        seqScheduledCheckBox.setHorizontalTextPosition(TRCheckBox.LEFT);
        seqScheduledCheckBox.setText(getMsg("ProjectPanel.seq.scheduled"));
        dueDateLabel = new TRLabel();
        dueDateLabel.setText(getMsg("ProjectPanel.due"));
        dueDateField = new DateField();
        startDateLabel = new TRLabel();
        startDateLabel.setText(getMsg("ProjectPanel.start"));
        startDateField = new DateField();
        purposeLabel = new TRLabel();
        purposeLabel.setText(getMsg("ProjectPanel.purpose"));
        purposeTextArea = new MTextArea();
        visionLabel = new TRLabel();
        visionLabel.setText(getMsg("ProjectPanel.vision"));
        visionTextArea = new MTextArea();
        brainstormLabel = new TRLabel();
        brainstormLabel.setText(getMsg("ProjectPanel.brainstorming"));
        brainstormTextArea = new MTextArea();
        organiseLabel = new TRLabel();
        organiseLabel.setText(getMsg("ProjectPanel.organising"));
        organiseTextArea = new MTextArea();
        notesViewLabel = new NotesViewLabel();
        notesViewField = new NotesViewField();
        doneCheckBox = new TRCheckBox();
        doneCheckBox.setHorizontalTextPosition(TRCheckBox.LEFT);
        doneCheckBox.setText(getMsg("ProjectPanel.completed"));
        doneDateField = new DateField();
        createdDateLabel = new TRLabel();
        createdDateLabel.setText(getMsg("ProjectPanel.created"));
        createdDateField = new DateField();

        scrollpane = new JScrollPane();
        scrollpane.setBorder(null);
        scrollpane.setViewportView(createPanel());

        setLayout(new BorderLayout());
        add(scrollpane, BorderLayout.CENTER);
    }

    private MigLayout layoutA1 = new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]4[]4[]2", "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2");
    private MigLayout layoutA2 = new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]4[]4[]2", "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2");

    private MigLayout layoutM1 = new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]4[]4[]2", "2[top]2[top]2[center]2[top]2[center]2[top]2[top]2[top]2[top]2[center]2[center]2[top]2[grow]2");
    private MigLayout layoutM2 = new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]4[]4[]2", "2[top]2[top]2[center]2[top]2[top]2[top]2[top]2[center]2[center]2[top]2[grow]2");

    private JPanel createPanel() {

        JComponent[][] includedOptionalComponents = new JComponent[3][2];
        int includedOptionalComponentsCount = 0;
        if (ProjectsPrefs.isIncludeDueDate()) {
            includedOptionalComponents[includedOptionalComponentsCount][0] = dueDateLabel;
            includedOptionalComponents[includedOptionalComponentsCount][1] = dueDateField;
            includedOptionalComponentsCount++;
        }
        if (ProjectsPrefs.isIncludeStartDate()) {
            includedOptionalComponents[includedOptionalComponentsCount][0] = startDateLabel;
            includedOptionalComponents[includedOptionalComponentsCount][1] = startDateField;
            includedOptionalComponentsCount++;
        }
        if (ProjectsPrefs.isSequencing()) {
            JPanel seqOptionsPanel = new JPanel(new MigLayout(getMIGCons(), "2[]2[]2[]2[]0", "0[]0"));
            seqOptionsPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
            seqOptionsPanel.add(seqIncludeLabel, "align left");
            seqOptionsPanel.add(seqProjectsCheckBox, "align left");
            seqOptionsPanel.add(seqScheduledCheckBox, "align left");
            seqOptionsPanel.add(seqDelegatedCheckBox, "align left");
            includedOptionalComponents[includedOptionalComponentsCount][0] = sequencingCheckBox;
            includedOptionalComponents[includedOptionalComponentsCount][1] = seqOptionsPanel;
            includedOptionalComponentsCount++;
        }

        JPanel panel;
        if (includedOptionalComponentsCount > 0) {
            panel = ViewUtils.isAquaLaF() ? new JPanel(layoutA1) : new JPanel(layoutM1);
        } else {
            panel = ViewUtils.isAquaLaF() ? new JPanel(layoutA2) : new JPanel(layoutM2);
        }

        panel.setBackground(ViewUtils.COLOR_PANEL_BG);
        panel.add(descriptionLabel, "align right");
        panel.add(descriptionText, "span, growx 100, wrap");
        panel.add(topicLabel, "align right");
        panel.add(topicCombo, "split 3, align left");
        panel.add(priorityLabel, "align left");
        panel.add(priorityCombo, "align left, wrap");

        ViewUtils.addSeparator(panel);

        if (includedOptionalComponentsCount > 0) {
            for (int i = 0; i < includedOptionalComponentsCount; i++) {
                includedOptionalComponents[i][0].setVisible(true);
                includedOptionalComponents[i][0].setEnabled(true);
                includedOptionalComponents[i][1].setVisible(true);
                includedOptionalComponents[i][1].setEnabled(true);
            }
            if (includedOptionalComponentsCount == 1) {
                panel.add(includedOptionalComponents[0][0], "align right");
                panel.add(includedOptionalComponents[0][1], "align left, wrap");
            } else if (includedOptionalComponentsCount == 2) {
                panel.add(includedOptionalComponents[0][0], "align right");
                panel.add(includedOptionalComponents[0][1], "align left, split 3");
                panel.add(includedOptionalComponents[1][0], "align left");
                panel.add(includedOptionalComponents[1][1], "align left, wrap");
            } else if (includedOptionalComponentsCount == 3) {
                panel.add(includedOptionalComponents[0][0], "align right");
                panel.add(includedOptionalComponents[0][1], "align left, split 5");
                panel.add(includedOptionalComponents[1][0], "align left");
                panel.add(includedOptionalComponents[1][1], "align left");
                panel.add(includedOptionalComponents[2][0], "align left");
                panel.add(includedOptionalComponents[2][1], "align left, wrap");
            }
            ViewUtils.addSeparator(panel);
        }

        if (ViewUtils.isAquaLaF()) {
            panel.add(purposeLabel, "align right");
            panel.add(purposeTextArea, "gapleft 3, gapright 3, span, growx 100, growy 0, wrap");
            panel.add(visionLabel, "align right");
            panel.add(visionTextArea, "gapleft 3, gapright 3, span, growx 100, growy 0, wrap");
            panel.add(brainstormLabel, "align right");
            panel.add(brainstormTextArea, "gapleft 3, gapright 3, span, growx 100, growy 0, wrap");
            panel.add(organiseLabel, "align right");
            panel.add(organiseTextArea, "gapleft 3, gapright 3, span, growx 100, growy 0, wrap");
        } else {
            panel.add(purposeLabel, "align right");
            panel.add(purposeTextArea, "span, growx 100, growy 0, wrap");
            panel.add(visionLabel, "align right");
            panel.add(visionTextArea, "span, growx 100, growy 0, wrap");
            panel.add(brainstormLabel, "align right");
            panel.add(brainstormTextArea, "span, growx 100, growy 0, wrap");
            panel.add(organiseLabel, "align right");
            panel.add(organiseTextArea, "span, growx 100, growy 0, wrap");
        }

        ViewUtils.addSeparator(panel);

        panel.add(doneCheckBox, "align right");
        panel.add(doneDateField, "align left, split 3");
        panel.add(createdDateLabel, "align left");
        panel.add(createdDateField, "align left, wrap");

        ViewUtils.addSeparator(panel);

        if (ViewUtils.isAquaLaF()) {
            panel.add(notesViewLabel, "align right top, gaptop 4, growy 0");
            panel.add(notesViewField, "span, gapleft 3, gapright 3, align left, grow");
        } else {
            panel.add(notesViewLabel, "align right top, growy 0");
            panel.add(notesViewField, "span, align left, grow");
        }


        return panel;
    }

    private String getMIGCons() {
        return DEBUG_MIG_LAYOUT ? "debug" : "";
    }

    private String getMIGCons(String c) {
        assert (c != null && c.trim().length() > 0);

        return DEBUG_MIG_LAYOUT ? "debug, " + c : c;
    }

    private static final boolean DEBUG_MIG_LAYOUT = false;

    private static final Logger LOG = Logger.getLogger("tr.view.project");
    private final static String DEFAULT_DESCRIPTION = NbBundle.getMessage(ProjectPanel.class, "new.project");
    private TRLabel descriptionLabel;
    private TRTextField descriptionText;
    private TRLabel topicLabel;
    private TRComboBox<Topic> topicCombo;
    private TRLabel priorityLabel;
    private TRComboBox<Value> priorityCombo;
    private TRCheckBox sequencingCheckBox;
    private JLabel seqIncludeLabel;
    private TRCheckBox seqProjectsCheckBox;
    private TRCheckBox seqScheduledCheckBox;
    private TRCheckBox seqDelegatedCheckBox;
    private TRLabel dueDateLabel;
    private DateField dueDateField;
    private TRLabel startDateLabel;
    private DateField startDateField;
    private TRLabel purposeLabel;
    private MTextArea purposeTextArea;
    private TRLabel visionLabel;
    private MTextArea visionTextArea;
    private TRLabel brainstormLabel;
    private MTextArea brainstormTextArea;
    private TRLabel organiseLabel;
    private MTextArea organiseTextArea;
    private NotesViewLabel notesViewLabel;
    private NotesViewField notesViewField;
    private TRCheckBox doneCheckBox;
    private DateField doneDateField;
    private TRLabel createdDateLabel;
    private DateField createdDateField;
    private Lookup.Result result;
    private Project project;
    private ComboBoxModel<Topic> topicsModel;
    private boolean updating;
    private DocumentListener docListenerDescription;
    private DocumentListener docListenerPurpose;
    private DocumentListener docListenerVision;
    private DocumentListener docListenerBrainstorm;
    private DocumentListener docListenerOrganise;
    private FocusAdapter focusAdapterDescription;
    private ActionListener actionListenerTopic;
    private ActionListener actionListenerPriorityCombo;
    private ActionListener actionListenerSequencingCheckBox;
    private ActionListener actionListenerSeqProjectsCheckBox;
    private ActionListener actionListenerSeqDelegatedCheckBox;
    private ActionListener actionListenerSeqScheduledCheckBox;
    private ActionListener actionListenerDoneCheckBox;
    private PropertyChangeListener createdDatePropertyChangeListener;
    private PropertyChangeListener startDatePropertyChangeListener;
    private PropertyChangeListener dueDatePropertyChangeListener;
    private PropertyChangeListener doneDatePropertyChangeListener;
    private Observer observerCriterion;
    private PreferenceChangeListener prefsChangeListener;
    private JScrollPane scrollpane;
}

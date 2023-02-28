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
package au.com.trgtd.tr.view.cal.dialog;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.swing.*;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.document.LazyDocumentListener;
import au.com.trgtd.tr.swing.mig.MTextArea;
import au.com.trgtd.tr.swing.time.HourSpinner;
import au.com.trgtd.tr.swing.time.MinuteSpinner;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.actn.StatusComboBoxModel;
import au.com.trgtd.tr.view.actn.StatusEnum;
import au.com.trgtd.tr.view.actors.ActorsComboBox;
import au.com.trgtd.tr.view.contexts.ContextsComboBox;
import au.com.trgtd.tr.view.contexts.ContextsComboBoxModel;
import au.com.trgtd.tr.view.criteria.EnergyComboBoxModel;
import au.com.trgtd.tr.view.criteria.PriorityComboBoxModel;
import au.com.trgtd.tr.view.criteria.TimeComboBoxModel;
import au.com.trgtd.tr.view.notes.NotesViewField;
import au.com.trgtd.tr.view.notes.NotesViewLabel;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import au.com.trgtd.tr.view.topics.TopicsComboBoxModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Notable;
import tr.model.action.ActionState;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.topic.Topic;

/**
 * Action panel for editing. Assumes that the panel is used in a modal dialog
 * and so does not listen for data model updates.
 *
 * @author Jeremy Moore
 */
public final class ActionEditPanel extends JPanel {

    private final ActionCtlr ctlr;

    /**
     * Constructs a new instance.
     *
     * @param ctlr The action controller.
     */
    public ActionEditPanel(ActionCtlr ctlr) {
        super(new BorderLayout());
        this.ctlr = ctlr;
        initPanel();
        initValues();
        changedStatus();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        addInternalListeners();
    }

    @Override
    public void removeNotify() {
        removeInternalListeners();
        super.removeNotify();
    }

    /*
     * Initialises the view form field values.
     */
    private void initValues() {

        projectField.setText(ctlr.getProjectPath());
        
        descrLabel.setIcon(ctlr.getIcon());
        descrText.setText(ctlr.getDescr());
        
        thoughtField.setText(ctlr.getThought());
        successText.setText(ctlr.getSuccess());
        topicCombo.setSelectedItem(ctlr.getTopic());
        contextCombo.setSelectedItem(ctlr.getContext());

        statusCombo.setSelectedItem(null);

        switch (ctlr.getStateType()) {
            case INACTIVE:
                statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                break;
            case DOASAP:
                statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                break;
            case SCHEDULED:
                statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                scheduledDateField.setDate(ctlr.getSchdDate());
                if (ctlr.getSchdDate() == null) {
                    scheduledHourSpinner.setValue(new Integer(0));
                    scheduledMinuteSpinner.setValue(new Integer(0));
                    durationHourSpinner.setValue(new Integer(0));
                    durationMinuteSpinner.setValue(new Integer(0));
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(ctlr.getSchdDate());
                    scheduledHourSpinner.setVal(c.get(Calendar.HOUR_OF_DAY));
                    scheduledMinuteSpinner.setVal(c.get(Calendar.MINUTE));
                    durationHourSpinner.setVal(ctlr.getSchdDurHrs());
                    durationMinuteSpinner.setVal(ctlr.getSchdDurMins());
                }
                break;
            case DELEGATED:
                statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                Integer id = ctlr.getDelegateID();
                String name = ctlr.getDelegateName();
                followupField.setDate(ctlr.getDelegateDate());
                if (id != null) {
                    delegateCombo.setSelectedItem(ActorUtils.instance().getActor(id));
                } else if (name != null) {
                    delegateCombo.setSelectedItem(name);
                } else {
                    delegateCombo.selectDefault();
                }
                break;
        }

        timeCombo.setSelectedItem(ctlr.getTime());
        energyCombo.setSelectedItem(ctlr.getEnergy());
        priorityCombo.setSelectedItem(ctlr.getPriority());
        
        
//        notesViewLabel.setNotable(ctlr);
//        notesViewField.setNotable(ctlr);
        
        notesViewLabel.setNotable(notable);
        notesViewField.setNotable(notable);
        
        
        
        
        createdDateField.setDate(ctlr.getCreatedDate());
        startDateField.setDate(ctlr.getStartDate());
        dueDateField.setDate(ctlr.getDueDate());
        doneCheckBox.setSelected(ctlr.isDone());
        doneDateField.setDate(ctlr.getDoneDate());
        setEnabled(true);

        // force scroll to top
        SwingUtilities.invokeLater(() -> {
            scrollpane.getViewport().setViewPosition(new Point(0, 0));
        });
    }

    /**
     * Override to enable/disable fields.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        descrLabel.setEnabled(enabled);
        descrText.setEnabled(enabled);
        successText.setEnabled(enabled);
        topicLabel.setEnabled(enabled);
        topicCombo.setEnabled(enabled);
        contextLabel.setEnabled(enabled);
        contextCombo.setEnabled(enabled);

        
        statusLabel.setEnabled(enabled);
        statusCombo.setEnabled(enabled);
        delegatedToLabel.setEnabled(enabled);
        delegateCombo.setEnabled(enabled);
//        emailButton.setEnabled(enabled);
        emailButton.setEnabled(false);
        followupLabel.setEnabled(enabled);
        followupField.setEnabled(enabled);
        scheduledLabel.setEnabled(enabled);
        scheduledDateField.setEnabled(enabled);
        scheduledHourSpinner.setEnabled(enabled);
        scheduledMinuteSpinner.setEnabled(enabled);
        durationLabel.setEnabled(enabled);
        durationHourSpinner.setEnabled(enabled);
        durationMinuteSpinner.setEnabled(enabled);
        notesViewLabel.setEnabled(enabled);
        notesViewField.setEnabled(enabled);
        createdDateLabel.setEnabled(enabled);
        createdDateField.setEnabled(enabled && ActionPrefs.isEditCreateDate());
        startDateLabel.setEnabled(enabled);
        startDateField.setEnabled(enabled);
        dueDateLabel.setEnabled(enabled);
        dueDateField.setEnabled(enabled);
        doneCheckBox.setEnabled(enabled);
        doneDateField.setEnabled(enabled);
        timeLabel.setEnabled(enabled);
        timeCombo.setEnabled(enabled);
        energyLabel.setEnabled(enabled);
        energyCombo.setEnabled(enabled);
        priorityLabel.setEnabled(enabled);
        priorityCombo.setEnabled(enabled);
    }

    private void changedDurationMinutes() {
        ctlr.setSchdDurMins(durationMinuteSpinner.getVal());
    }

    private void changedDurationHours() {
        ctlr.setSchdDurHours(durationHourSpinner.getVal());
    }

    private void changedScheduledDate(Date date) {
        ctlr.setSchdDate(date);
    }

    private void changedPriority() {
        ctlr.setPriority((Value) priorityCombo.getSelectedItem());
    }

    private void changedTime() {
        ctlr.setTime((Value) timeCombo.getSelectedItem());
    }

    private void changedEnergy() {
        ctlr.setEnergy((Value)energyCombo.getSelectedItem());
    }

    private void changedStatus() {
        StatusEnum status = (StatusEnum) statusCombo.getSelectedItem();
        if (status == null) {
            return;
        }
        
        switch (status) {
            case INACTIVE:
                ctlr.setStateType(ActionState.Type.INACTIVE);
                setStatusInactive();
                break;
            case DO_ASAP:
                ctlr.setStateType(ActionState.Type.DOASAP);
                setStatusDoASAP();
                break;
            case SCHEDULED:
                ctlr.setStateType(ActionState.Type.SCHEDULED);
                setStatusScheduled();
                break;
            case DELEGATED:
                ctlr.setStateType(ActionState.Type.DELEGATED);
                setStatusDelegated();
                break;
        }

        descrLabel.setIcon(ctlr.getIcon());
//        descrLabel.invalidate();
//        descrLabel.repaint();
        
        setEnabled(true);
    }

    private void changedDelegate(Object selected) {
        if (selected == null) {
            ctlr.setDelegateID(null);
            ctlr.setDelegateName(null);
            return;
        }
        if (selected instanceof String string) {
            ctlr.setDelegateName(string);
            return;
        }
        if (selected instanceof Actor actor) {
            ctlr.setDelegateID(actor.getID());
        }
    }

    private void emailActionPerformed(ActionEvent evt) {
//        String addr = ctlr.getDelegateEmail();
//        if (Utilities.getOperatingSystem() == Utilities.OS_LINUX && !addr.contains("@")) {
//            JOptionPane.showMessageDialog(null, getMsg("ERR_EmailRequired"), "", JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//        String subj = ctlr.getDescr();
//        String body = ctlr.getNotes();
//        Email.email(addr, subj, body, ActionPrefs.getEmailEncoding());
//        Date today = Calendar.getInstance().getTime();
//        if (ActionPrefs.isNoteDelegateEmail()) {
//            ctlr.addToNotes(getMsg("MSG_EmailSent", Constants.DATE_TIME_FORMAT_FIXED.format(today), addr));
//        }
    }
    
    private void changedScheduledHour() {
        ctlr.setSchdHour(scheduledHourSpinner.getVal());
    }

    private void changedScheduledMinute() {
        ctlr.setSchdMin(scheduledMinuteSpinner.getVal());
    }

    private void changedDoneDate(Date date) {
        ctlr.setDoneDate(doneDateField.getDate());
    }

    private void changedDone() {
        if (doneCheckBox.isSelected()) {
            Date doneDate = ctlr.getDoneDate();
            if (doneDate == null) {
                doneDate = new Date();
                ctlr.setDoneDate(doneDate);
                doneDateField.setDate(doneDate);
            }
        } else {
            ctlr.setDoneDate(null);
            doneDateField.setDate(null);
        }
    }

    private void changedCreatedDate(Date date) {
        ctlr.setCreatedDate(date);
    }

    private void changedStartDate(Date date) {
        ctlr.setStartDate(date);
    }

    private void changedDueDate(Date date) {
        ctlr.setDueDate(dueDateField.getDate());
    }

    private void changedContext(java.awt.event.ActionEvent evt) {
        TRComboBox<Context> combo = (TRComboBox<Context>) evt.getSource();
        ctlr.setContext((Context) combo.getSelectedItem());
    }

    private void changedTopic(java.awt.event.ActionEvent evt) {
        TRComboBox<Topic> combo = (TRComboBox<Topic>) evt.getSource();
        ctlr.setTopic((Topic) combo.getSelectedItem());
    }

    private static String getMsg(String key) {
        return NbBundle.getMessage(ActionEditPanel.class, key);
    }

    private static String getMsg(String key, String p1, String p2) {
        return NbBundle.getMessage(ActionEditPanel.class, key, p1, p2);
    }

    private ComboBoxModel<Value> getTimeComboBoxModel() {
        return new TimeComboBoxModel();
    }

    private ComboBoxModel<Value> getEnergyComboBoxModel() {
        return new EnergyComboBoxModel();
    }

    private ComboBoxModel<Value> getPriorityComboBoxModel() {
        return new PriorityComboBoxModel();
    }

    private void addInternalListeners() {
        docListenerDescr = new LazyDocumentListener() {
            @Override
            public void update() {
                ctlr.setDescr(descrText.getText());
            }
        };
        descrText.getDocument().addDocumentListener(docListenerDescr);

        docListenerSuccess = new LazyDocumentListener() {
            @Override
            public void update() {
                ctlr.setSuccess(successText.getText());
            }
        };
        successText.getDocument().addDocumentListener(docListenerSuccess);

        actionListenerTopic = (ActionEvent evt) -> {
            changedTopic(evt);
        };
        topicCombo.addActionListener(actionListenerTopic);

        actionListenerContext = (ActionEvent evt) -> {
            changedContext(evt);
        };
        contextCombo.addActionListener(actionListenerContext);

        // Scheduled
        propertyListenerScheduledDate = (PropertyChangeEvent e) -> {
            changedScheduledDate((Date) e.getNewValue());
        };
        scheduledDateField.addPropertyChangeListener("value", propertyListenerScheduledDate);

        changeListenerScheduledHour = (ChangeEvent evt) -> {
            changedScheduledHour();
        };

        scheduledHourSpinner.addChangeListener(changeListenerScheduledHour);

        changeListenerScheduledMinute = (ChangeEvent evt) -> {
            changedScheduledMinute();
        };
        scheduledMinuteSpinner.addChangeListener(changeListenerScheduledMinute);

        changeListenerDurationHour = (ChangeEvent evt) -> {
            changedDurationHours();
        };
        durationHourSpinner.addChangeListener(changeListenerDurationHour);

        changeListenerDurationMinute = (ChangeEvent evt) -> {
            changedDurationMinutes();
        };
        durationMinuteSpinner.addChangeListener(changeListenerDurationMinute);

//        actionListenerRecurrence = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                recurrenceActionPerformed(evt);
//            }
//        };
//        recurrenceButton.addActionListener(actionListenerRecurrence);

        propertyListenerDelegateCombo = (PropertyChangeEvent e) -> {
            changedDelegate(e.getNewValue());
        };
        delegateCombo.addPropertyChangeListener(ActorsComboBox.PROP_SELECTED, propertyListenerDelegateCombo);

        actionListenerEmail = (ActionEvent evt) -> {
            emailActionPerformed(evt);
        };
        emailButton.addActionListener(actionListenerEmail);

        actionListenerStatusCombo = (ActionEvent evt) -> {
            changedStatus();
        };
        statusCombo.addActionListener(actionListenerStatusCombo);

        actionListenerPriorityCombo = (ActionEvent evt) -> {
            ctlr.setPriority((Value)priorityCombo.getSelectedItem());
        };
        priorityCombo.addActionListener(actionListenerPriorityCombo);

        actionListenerEnergyCombo = (ActionEvent evt) -> {
            ctlr.setEnergy((Value)energyCombo.getSelectedItem());
        };
        energyCombo.addActionListener(actionListenerEnergyCombo);

        actionListenerTimeCombo = (ActionEvent evt) -> {
            ctlr.setTime((Value)timeCombo.getSelectedItem());
        };
        timeCombo.addActionListener(actionListenerTimeCombo);

        propertyListenerFollowupDate = (PropertyChangeEvent e) -> {
            ctlr.setDelegateDate((Date) e.getNewValue());
        };
        followupField.addPropertyChangeListener("value", propertyListenerFollowupDate);

        propertyListenerCreatedDate = (PropertyChangeEvent e) -> {
            changedCreatedDate((Date) e.getNewValue());
        };
        createdDateField.addPropertyChangeListener("value", propertyListenerCreatedDate);

        propertyListenerStartDate = (PropertyChangeEvent e) -> {
            changedStartDate((Date) e.getNewValue());
        };
        startDateField.addPropertyChangeListener("value", propertyListenerStartDate);

        propertyListenerDueDate = (PropertyChangeEvent e) -> {
            changedDueDate((Date) e.getNewValue());
        };
        dueDateField.addPropertyChangeListener("value", propertyListenerDueDate);

        // Done CheckBox
        doneCheckBoxActionListener = (ActionEvent evt) -> {
            changedDone();
        };
        doneCheckBox.addActionListener(doneCheckBoxActionListener);

        propertyListenerDoneDate = (PropertyChangeEvent e) -> {
            changedDoneDate((Date) e.getNewValue());
        };
        doneDateField.addPropertyChangeListener("value", propertyListenerDoneDate);
    }

    private void removeInternalListeners() {
        descrText.getDocument().removeDocumentListener(docListenerDescr);
        docListenerDescr = null;
        successText.getDocument().removeDocumentListener(docListenerSuccess);
        docListenerSuccess = null;
        contextCombo.removeActionListener(actionListenerContext);
        actionListenerContext = null;
        topicCombo.removeActionListener(actionListenerTopic);
        actionListenerTopic = null;
        statusCombo.removeActionListener(actionListenerStatusCombo);
        actionListenerStatusCombo = null;
        scheduledDateField.removePropertyChangeListener(propertyListenerScheduledDate);
        propertyListenerScheduledDate = null;
        scheduledHourSpinner.removeChangeListener(changeListenerScheduledHour);
        changeListenerScheduledHour = null;
        scheduledMinuteSpinner.removeChangeListener(changeListenerScheduledMinute);
        changeListenerScheduledMinute = null;
        durationHourSpinner.removeChangeListener(changeListenerDurationHour);
        changeListenerDurationHour = null;
        durationMinuteSpinner.removeChangeListener(changeListenerDurationMinute);
        changeListenerDurationMinute = null;
//        recurrenceButton.removeActionListener(actionListenerRecurrence);
//        actionListenerRecurrence = null;
        delegateCombo.removePropertyChangeListener(ActorsComboBox.PROP_SELECTED, propertyListenerDelegateCombo);
        propertyListenerDelegateCombo = null;
        emailButton.removeActionListener(actionListenerEmail);
        actionListenerEmail = null;
        followupField.removePropertyChangeListener(propertyListenerFollowupDate);
        propertyListenerFollowupDate = null;
        timeCombo.removeActionListener(actionListenerTimeCombo);
        actionListenerTimeCombo = null;
        energyCombo.removeActionListener(actionListenerEnergyCombo);
        actionListenerEnergyCombo = null;
        priorityCombo.removeActionListener(actionListenerPriorityCombo);
        actionListenerPriorityCombo = null;
        createdDateField.removePropertyChangeListener(propertyListenerCreatedDate);
        propertyListenerCreatedDate = null;
        startDateField.removePropertyChangeListener(propertyListenerStartDate);
        propertyListenerStartDate = null;
        dueDateField.removePropertyChangeListener(propertyListenerDueDate);
        propertyListenerDueDate = null;
        doneCheckBox.removeActionListener(doneCheckBoxActionListener);
        doneCheckBoxActionListener = null;
        doneDateField.removePropertyChangeListener(propertyListenerDoneDate);
        propertyListenerDoneDate = null;
    }

    private void initPanel() {
        projectLabel = new TRLabel();
        projectField = new TRTextField();
        
        scheduledLabel = new TRLabel();
        scheduledHourSpinner = new HourSpinner();
        scheduledMinuteSpinner = new MinuteSpinner();
        durationLabel = new TRLabel();
        durationHourSpinner = new HourSpinner();
        durationMinuteSpinner = new MinuteSpinner();
        scheduledDateField = new DateField();
        delegatedToLabel = new TRLabel();
        delegateCombo = new ActorsComboBox();
        emailButton = new TRButton();
        followupLabel = new TRLabel();
        followupField = new DateField();
        descrLabel = new TRLabel();
        descrText = new TRTextField();
        
        thoughtLabel = new TRLabel();
        thoughtField = new TRTextField();
        
        successLabel = new TRLabel();
        successText = new MTextArea();
        contextLabel = new TRLabel();
        contextsModel = new ContextsComboBoxModel(false);
        contextCombo = new ContextsComboBox(contextsModel);
        topicLabel = new TRLabel();
        topicsModel = new TopicsComboBoxModel(false);
        topicCombo = new TopicsComboBox(topicsModel);
        statusLabel = new TRLabel();
        statusCombo = new TRComboBox<>();
        priorityLabel = new TRLabel();
        energyLabel = new TRLabel();
        energyCombo = new TRComboBox<>();
        timeLabel = new TRLabel();
        timeCombo = new TRComboBox<>();
        priorityCombo = new TRComboBox<>();
        notesViewLabel = new NotesViewLabel();
        notesViewField = new NotesViewField();
        createdDateLabel = new TRLabel();
        startDateLabel = new TRLabel();
        dueDateLabel = new TRLabel();
        doneCheckBox = new TRCheckBox();
        createdDateField = new DateField();
        startDateField = new DateField();
        dueDateField = new DateField();
        doneDateField = new DateField();

        scheduledLabel.setText(getMsg("LBL_ScheduledFor")); // NOI18N
        scheduledHourSpinner.setMaximumSize(new Dimension(46, 23));
        scheduledHourSpinner.setMinimumSize(new Dimension(46, 23));
        scheduledHourSpinner.setPreferredSize(new Dimension(46, 23));
        scheduledMinuteSpinner.setMaximumSize(new Dimension(46, 23));
        scheduledMinuteSpinner.setMinimumSize(new Dimension(46, 23));
        scheduledMinuteSpinner.setPreferredSize(new Dimension(46, 23));

        durationLabel.setText(getMsg("LBL_Duration")); // NOI18N
        durationHourSpinner.setMaximumSize(new Dimension(46, 23));
        durationHourSpinner.setMinimumSize(new Dimension(46, 23));
        durationHourSpinner.setPreferredSize(new Dimension(46, 23));
        durationMinuteSpinner.setMaximumSize(new Dimension(46, 23));
        durationMinuteSpinner.setMinimumSize(new Dimension(46, 23));
        durationMinuteSpinner.setPreferredSize(new Dimension(46, 23));

        scheduledDateField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scheduledDateField.setMaximumSize(new Dimension(120, 23));
        scheduledDateField.setMinimumSize(new Dimension(120, 23));
        scheduledDateField.setPreferredSize(new Dimension(120, 23));
        delegatedToLabel.setText(getMsg("LBL_DelegatedTo")); // NOI18N
        emailButton.setText(getMsg("LBL_Email"));
        followupLabel.setText(getMsg("LBL_FollowUp")); // NOI18N
        followupField.setMaximumSize(new Dimension(110, 23));
        followupField.setMinimumSize(new Dimension(110, 23));
        followupField.setPreferredSize(new Dimension(110, 23));

        
        projectLabel.setText(getMsg("LBL_Project")); // NOI18N
        projectField.setEnabled(false);
        
        descrLabel.setText(getMsg("LBL_Description")); // NOI18N
        
        thoughtLabel.setText(getMsg("LBL_Thought")); // NOI18N
        thoughtField.setEnabled(false);
//      thoughtField.setBackground(UIManager.getDefaults().getColor("Panel.background"));
//      thoughtField.setBorder(null);

        successLabel.setText(NbBundle.getMessage(ActionEditPanel.class, "LBL_Success")); // NOI18N

        contextLabel.setText(getMsg("LBL_Context")); // NOI18N
        contextCombo.setMaximumRowCount(Constants.COMBO_MAX_ROWS);

        topicLabel.setText(getMsg("LBL_Topic")); // NOI18N

        statusLabel.setText(getMsg("LBL_Status")); // NOI18N
        statusCombo.setModel(new StatusComboBoxModel());

        timeLabel.setText(getMsg("LBL_SimpleTime")); // NOI18N
        timeCombo.setModel(getTimeComboBoxModel());

        energyLabel.setText(getMsg("LBL_Energy")); // NOI18N
        energyCombo.setModel(getEnergyComboBoxModel());

        priorityLabel.setText(getMsg("LBL_Priority")); // NOI18N
        priorityCombo.setModel(getPriorityComboBoxModel());

        createdDateLabel.setText(getMsg("LBL_Created")); // NOI18N

        startDateLabel.setText(NbBundle.getMessage(ActionEditPanel.class, "LBL_Start")); // NOI18N

        dueDateLabel.setText(NbBundle.getMessage(ActionEditPanel.class, "LBL_Due")); // NOI18N

        doneCheckBox.setText(getMsg("LBL_Done")); // NOI18N
        doneCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);

        scrollpane = new JScrollPane();
        scrollpane.setBorder(null);
        scrollpane.setViewportView(createPanel());

        add(scrollpane, BorderLayout.CENTER);
    }

    private JPanel createPanel() {

        JPanel panel;

        if (ViewUtils.isAquaLaF()) {
            if (ActionPrefs.isShowSuccess()) {
                panel = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2",
                        "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2"));
            } else {
                panel = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2",
                        "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2"));
            }
        } else {
            if (ActionPrefs.isShowSuccess()) {
                panel = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2",
                        "2[top]2[top]2[top]2[top]2[top]2[center]2[top]2[center]2[top]2[center]2[top]2[center]2[top, grow]2"));
            } else {
                panel = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2",
                        "2[top]2[top]2[top]2[top]2[center]2[top]2[center]2[top]2[center]2[top]2[center]2[top, grow]2"));
            }
        }

        panel.setBackground(ViewUtils.COLOR_PANEL_BG);

        panel.add(descrLabel, "align right");
        panel.add(descrText, "span, growx, wrap");

        panel.add(projectLabel, "align right");
        panel.add(projectField, "span, growx, wrap");
        
        panel.add(thoughtLabel, "align right");
        panel.add(thoughtField, "span, growx, wrap");

        if (ActionPrefs.isShowSuccess()) {
            if (ViewUtils.isAquaLaF()) {
                panel.add(successLabel, "align right top");
                panel.add(successText, "gapleft 3, gapright 3, growx, span, wrap");
            } else {
                panel.add(successLabel, "align right top");
                panel.add(successText, "growx, span, wrap");
            }
        }

        panel.add(topicLabel, "align right");
        panel.add(topicCombo, "align left, wrap");

        ViewUtils.addSeparator(panel);

        JComponent[][] comps = new JComponent[4][2];
        int compsIndex = 0;
        comps[compsIndex][0] = contextLabel;
        comps[compsIndex][1] = contextCombo;
                
        Data data = DataLookup.instance().lookup(Data.class);        
        if (data.getTimeCriterion().isUse()) {
            compsIndex++;
            comps[compsIndex][0] = timeLabel;
            comps[compsIndex][1] = timeCombo;
        }
        if (data.getEnergyCriterion().isUse()) {
            compsIndex++;
            comps[compsIndex][0] = energyLabel;
            comps[compsIndex][1] = energyCombo;
        }
        if (data.getPriorityCriterion().isUse()) {
            compsIndex++;
            comps[compsIndex][0] = priorityLabel;
            comps[compsIndex][1] = priorityCombo;
        }
        if (compsIndex == 0) {
            panel.add(comps[0][0], "align right");
            panel.add(comps[0][1], "align left, wrap");
        } else if (compsIndex == 1) {
            panel.add(comps[0][0], "align right");
            panel.add(comps[0][1], "align left, split 3");
            panel.add(comps[1][0], "align right");
            panel.add(comps[1][1], "align left, wrap");
        } else if (compsIndex == 2) {
            panel.add(comps[0][0], "align right");
            panel.add(comps[0][1], "align left, split 5");
            panel.add(comps[1][0], "align right");
            panel.add(comps[1][1], "align left");
            panel.add(comps[2][0], "align right");
            panel.add(comps[2][1], "align left, wrap");
        } else if (compsIndex == 3) {
            panel.add(comps[0][0], "align right");
            panel.add(comps[0][1], "align left, split 7");
            panel.add(comps[1][0], "align right");
            panel.add(comps[1][1], "align left");
            panel.add(comps[2][0], "align right");
            panel.add(comps[2][1], "align left");
            panel.add(comps[3][0], "align right");
            panel.add(comps[3][1], "align left, wrap");
        }

        ViewUtils.addSeparator(panel);

        if (ViewUtils.isAquaLaF()) {
            panel.add(statusLabel, "align right top, gaptop 4");
            panel.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
        } else {
            panel.add(statusLabel, "align right top");
            panel.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
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
    
    private MigLayout statusLayoutA1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]0");
    private MigLayout statusLayoutA2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]2[]0");
    private MigLayout statusLayoutM1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]0");
    private MigLayout statusLayoutM2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]2[top]0");
    
    private enum Rows {
        One, Two
    };

    private JPanel getStatusPanel(Rows rows) {
        if (statusPanel == null) {
            statusPanel = new JPanel();
            statusPanel.setOpaque(false);
            statusPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            statusPanel.removeAll();
        }
        if (ViewUtils.isAquaLaF()) {
            statusPanel.setLayout(rows == Rows.One ? statusLayoutA1 : statusLayoutA2);
        } else {
            statusPanel.setLayout(rows == Rows.One ? statusLayoutM1 : statusLayoutM2);
        }
        return statusPanel;
    }

    private void setStatusDelegated() {
        boolean focus = statusCombo.hasFocus();
        JPanel panel = getStatusPanel(Rows.Two);
        panel.add(statusCombo, "span, split");
        if (ActionPrefs.isShowDueDate()) {
            panel.add(dueDateLabel, "");
            panel.add(dueDateField, "");
        }
        if (ActionPrefs.isShowStartDate()) {
            panel.add(startDateLabel, "");
            panel.add(startDateField, "");
        }
        panel.add(fillerLabel1, "wrap");

        panel.add(delegatedToLabel, "gapleft 4, span, split 5, h 24!");
        if (ViewUtils.isAquaLaF()) {
            if (delegateCombo.isEditable()) {
                panel.add(delegateCombo, "h 23!");
            } else {
                panel.add(delegateCombo, "h 27!");
            }
        } else {
            panel.add(delegateCombo, "");
        }
        panel.add(emailButton, "gapleft 2");
        panel.add(followupLabel, "h 24!");
        panel.add(followupField, "");

        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusDoASAP() {
        boolean focus = statusCombo.hasFocus();

        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "span, split");
        if (ActionPrefs.isShowDueDate()) {
            panel.add(dueDateLabel, "");
            panel.add(dueDateField, "");
        }
        if (ActionPrefs.isShowStartDate()) {
            panel.add(startDateLabel, "");
            panel.add(startDateField, "");
        }
        panel.add(fillerLabel2, "wrap");

        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusInactive() {
        boolean focus = statusCombo.hasFocus();

        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "span, split");
        if (ActionPrefs.isShowDueDate()) {
            panel.add(dueDateLabel, "");
            panel.add(dueDateField, "");
        }
        if (ActionPrefs.isShowStartDate()) {
            panel.add(startDateLabel, "");
            panel.add(startDateField, "");
        }
        panel.add(fillerLabel3, "wrap");

        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusScheduled() {
        boolean focus = statusCombo.hasFocus();

        JPanel panel = getStatusPanel(Rows.Two);
        panel.add(statusCombo, "span, split 7");
        panel.add(scheduledDateField, "gapleft 2");
        panel.add(scheduledHourSpinner, "gapleft 0");
        panel.add(scheduledMinuteSpinner, "gapleft 0");
        panel.add(durationLabel, "gapleft 4");
        panel.add(durationHourSpinner, "gapleft 2");
        panel.add(durationMinuteSpinner, "gapleft 0, wrap");

//        panel.add(recurrenceLabel, "gapleft 4, split 2");
//        panel.add(recurrenceButton, "gapleft 2");

        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private String getMIGCons() {
        return DEBUG_MIG_LAYOUT ? "debug" : "";
    }

    private String getMIGCons(String c) {
        assert (c != null && c.trim().length() > 0);

        return DEBUG_MIG_LAYOUT ? "debug, " + c : c;
    }
    
    private final Notable notable = new Notable() {        
        private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);        
        @Override
        public String getNotes() {
            return ctlr.getNotes();
        }
        @Override
        public void setNotes(String newNotes) {
            String oldNotes = ctlr.getNotes();            
            ctlr.setNotes(newNotes);
            pcs.firePropertyChange(PROP_NOTES, oldNotes, newNotes);            
        }
        @Override
        public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
            pcs.addPropertyChangeListener(property, listener);
        }
        @Override
        public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(property, listener);
        }
    };
    
    
//  private static final Logger LOG = Logger.getLogger("tr.view.actn");
    private static final boolean DEBUG_MIG_LAYOUT = false;
    private static final TRLabel fillerLabel1 = new TRLabel("");
    private static final TRLabel fillerLabel2 = new TRLabel("");
    private static final TRLabel fillerLabel3 = new TRLabel("");
    
    private ComboBoxModel<Topic> topicsModel;
    private ComboBoxModel<Context> contextsModel;
    private DocumentListener docListenerDescr;
    private DocumentListener docListenerSuccess;
    private ActionListener actionListenerTopic;
    private ActionListener actionListenerContext;
    private ActionListener actionListenerEmail;
    private ActionListener actionListenerStatusCombo;
    private ActionListener actionListenerPriorityCombo;
    private ActionListener actionListenerEnergyCombo;
    private ActionListener actionListenerTimeCombo;
    private ChangeListener changeListenerScheduledHour;
    private ChangeListener changeListenerScheduledMinute;
    private ChangeListener changeListenerDurationHour;
    private ChangeListener changeListenerDurationMinute;
    private ActionListener doneCheckBoxActionListener;
    private PropertyChangeListener propertyListenerFollowupDate;
    private PropertyChangeListener propertyListenerCreatedDate;
    private PropertyChangeListener propertyListenerStartDate;
    private PropertyChangeListener propertyListenerDueDate;
    private PropertyChangeListener propertyListenerDoneDate;
    private PropertyChangeListener propertyListenerScheduledDate;
    private PropertyChangeListener propertyListenerDelegateCombo;
    
    private TRLabel projectLabel;
    private TRTextField projectField;    
    private TRLabel descrLabel;
    private TRTextField descrText;
    private TRLabel thoughtLabel;
    private TRTextField thoughtField;
    private TRLabel successLabel;
    private MTextArea successText;
    private TRLabel contextLabel;
    private TRComboBox<Context> contextCombo;
    private TRComboBox<Topic> topicCombo;
    private TRLabel topicLabel;
    private DateField createdDateField;
    private TRLabel createdDateLabel;
    private TRLabel delegatedToLabel;
    private TRCheckBox doneCheckBox;
    private DateField doneDateField;
    private DateField dueDateField;
    private TRLabel dueDateLabel;
    private HourSpinner durationHourSpinner;
    private TRLabel durationLabel;
    private MinuteSpinner durationMinuteSpinner;
    private TRButton emailButton;
    private TRComboBox<Value> energyCombo;
    private TRLabel energyLabel;
    private DateField followupField;
    private TRLabel followupLabel;
    private NotesViewLabel notesViewLabel;
    private NotesViewField notesViewField;
    private TRComboBox<Value> priorityCombo;
    private TRLabel priorityLabel;
    private DateField scheduledDateField;
    private HourSpinner scheduledHourSpinner;
    private TRLabel scheduledLabel;
    private MinuteSpinner scheduledMinuteSpinner;
    private DateField startDateField;
    private TRLabel startDateLabel;
    private TRComboBox<StatusEnum> statusCombo;
    private TRLabel statusLabel;
    private TRComboBox<Value> timeCombo;
    private TRLabel timeLabel;
    private JPanel statusPanel;
    private ActorsComboBox delegateCombo;
    private JScrollPane scrollpane;
}

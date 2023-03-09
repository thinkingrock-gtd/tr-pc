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
package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.email.prefs.EmailPrefs;
import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import au.com.trgtd.tr.runtime.Email;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.swing.*;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.mig.MTextArea;
import au.com.trgtd.tr.swing.time.HourSpinner;
import au.com.trgtd.tr.swing.time.MinuteSpinner;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.actn.ActionPanel;
import au.com.trgtd.tr.view.actn.StatusComboBoxModel;
import au.com.trgtd.tr.view.actn.StatusEnum;
import au.com.trgtd.tr.view.actn.recurrence.NewRecurrenceWizard;
import au.com.trgtd.tr.view.actn.recurrence.modify.ModifyRecurrenceWizard;
import au.com.trgtd.tr.view.actors.ActorsComboBox;
import au.com.trgtd.tr.view.contexts.ContextsComboBox;
import au.com.trgtd.tr.view.contexts.ContextsComboBoxModel;
import au.com.trgtd.tr.view.criteria.CriterionComboBoxModel;
import au.com.trgtd.tr.view.goals.chooser.GoalChooserDialog;
import au.com.trgtd.tr.view.goals.chooser.GoalChooserDialog.Result;
import au.com.trgtd.tr.view.goals.levels.combo.LevelsComboBox;
import au.com.trgtd.tr.view.notes.NotesEditorPanel;
import au.com.trgtd.tr.view.project.chooser.ProjectChooser;
import au.com.trgtd.tr.view.project.chooser.ProjectChooserDialog;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import au.com.trgtd.tr.view.topics.TopicsComboBoxModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.*;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Criterion;
import tr.model.criteria.Value;
import tr.model.future.Future;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.util.Manager;
import tr.model.util.delegation.DelegationData;
import tr.model.util.delegation.DelegationUtils;
import tr.model.util.delegation.SMTPSender;

/**
 * Process panel.
 *
 * @author Jeremy Moore
 * @version 2.0
 */
public final class ProcessPanel extends JPanel implements Observer, ProcessCookie, ProcessAddCookie, DeleteCookie {

    /** Creates new form panelProcess
     * @param processNodeProvider */
    public ProcessPanel(ProcessNodeProvider processNodeProvider) {
        this.processNodeProvider = processNodeProvider;
        processNode = new ProcessNode(this, this, this);
        processNodeDelegationResponse = new ProcessNode(this, this);
        data = DataLookup.instance().lookup(Data.class);
        projects = data.getRootProjects();
        singleActions = data.getRootActions();
        referenceManager = data.getInformationManager();
        somedayManager = data.getFutureManager();
        thoughtManager = data.getThoughtManager();
        thoughtManager.addObserver(this);
        initPanel();
        initModel();
    }

    private void enableFields(boolean b) {
        thoughtLabel.setEnabled(b);
        thoughtText.setEnabled(b);
        topicLabel.setEnabled(b);
        topicCombo.setEnabled(b);

        actionableYesRadioButton.setEnabled(b);
        actionableNoRadioButton.setEnabled(b);

        // Not Actionable
        deleteRadioButton.setEnabled(b);
        somedayRadioButton.setEnabled(b);
        referenceRadioButton.setEnabled(b);
        tickleDateLabel.setEnabled(b);
        tickleDateField.setEnabled(b);
        goalRadioButton.setEnabled(b);
        goalLevelLabel.setEnabled(b);
        goalLevelCombo.setEnabled(b);
        superGoalLabel.setEnabled(b);
        supergoalButton.setEnabled(b);
        superGoalText.setEnabled(b);

        notesLabel.setEnabled(b);
        notesField.setEnabled(b);
        notesLabelNA.setEnabled(b);
        notesFieldNA.setEnabled(b);

        // Actionable
        actionableLabel.setEnabled(b);
        successLabel.setEnabled(b);
        successText.setEnabled(b);
        actionLabel.setEnabled(b);
        descriptionLabel.setEnabled(b);
        descrField.setEnabled(b);
        contextLabel.setEnabled(b);
        contextCombo.setEnabled(b);
        timeLabel.setEnabled(b);
        timeCombo.setEnabled(b);
        energyLabel.setEnabled(b);
        energyCombo.setEnabled(b);
        priorityLabel.setEnabled(b);
        priorityCombo.setEnabled(b);

        // Status
        statusCombo.setEnabled(b);
        statusLabel.setEnabled(b);
        dueDateLabel.setEnabled(b);
        dueDateField.setEnabled(b);
        startDateLabel.setEnabled(b);
        startDateField.setEnabled(b);

        // Scheduled
        scheduledDateField.setEnabled(b);
        scheduledHourSpinner.setEnabled(b);
        scheduledMinuteSpinner.setEnabled(b);
        durationLabel.setEnabled(b);
        durationHourSpinner.setEnabled(b);
        durationMinuteSpinner.setEnabled(b);
        recurrenceButton.setEnabled(b);
        recurrenceLabel.setEnabled(b);

        // Delegated
        delegatedToLabel.setEnabled(b);
        delegateCombo.setEnabled(b);
        emailButton.setEnabled(b);
        followupLabel.setEnabled(b);
        followupField.setEnabled(b);

        // done and created
        doneCheckBox.setEnabled(b);
        doneDateField.setEnabled(b);
        createdDateLabel.setEnabled(b);
        createdDateField.setEnabled(b && ActionPrefs.isEditCreateDate());

        // project
        projectLabel.setEnabled(b);

        newProjectCheck.setEnabled(b);
        newProjectField.setEnabled(b && newProjectCheck.isSelected());
        newProjectField.setEditable(b && newProjectCheck.isSelected());
        
        addToProjectCheck.setEnabled(b);
        addToProjectButton.setEnabled(b && addToProjectCheck.isSelected());
        addToProjectField.setEnabled(b && addToProjectCheck.isSelected());
        addToProjectField.setEditable(false);        
    }

    private void initFieldValues() {
        if (thought == null) {
            thoughtLabel.setText(getMsg("LBL_Thought"));
            thoughtText.setText("");
            thoughtText.setForeground(Color.black);
            thoughtText.setBackground(Color.white);
            topicCombo.setSelectedItem(null);
            descrField.setText("");
            newProjectField.setText("");
        } else {
            thoughtLabel.setText(getMsg("LBL_ThoughtN", countUnprocessedThoughts()));
            thoughtText.setText(thought.getDescription());
            thoughtText.setForeground(thought.getTopic().getForeground());
            thoughtText.setBackground(thought.getTopic().getBackground());
            topicCombo.setSelectedItem(thought.getTopic());
            descrField.setText(thought.getDescription());
            newProjectField.setText(thought.getDescription());
        }

        
        panelMain.removeAll();
        if (delegationResponse) {
            panelMain.add(getPanelDelegationResponse(), BorderLayout.CENTER);            
            delegationResponseActionPanel.initModel(delegationResponseAction);
            delegationResponseActionPanel.setEnabled(false);
            delegationOriginalActionPanel.initModel(delegationOriginalActionCopy);            
            String path = Services.instance.getPath(delegationOriginalActionCopy);
            delegationProjectPath.setText(path);
            return;
        }
        panelMain.add(panelNormal, BorderLayout.CENTER);                
      
        
        actionableYesRadioButton.setSelected(false);
        actionableNoRadioButton.setSelected(false);

        // reset all status values
        dueDateField.setDate(null);
        startDateField.setDate(null);
        scheduledDateField.setDate(null);
        
        scheduledHourSpinner.setVal(ActionPrefs.getSchdTimeHr());
        scheduledMinuteSpinner.setVal(ActionPrefs.getSchdTimeMn());
        durationHourSpinner.setVal(ActionPrefs.getSchdDurHrs());
        durationMinuteSpinner.setVal(ActionPrefs.getSchdDurMns());
        
        recurrenceButton.setText(LBL_CREATE);
        delegateCombo.selectDefault();
        followupField.setDate(null);

        if (action == null) {
            // Not Actionable
            deleteRadioButton.setSelected(false);
            somedayRadioButton.setSelected(true);
            referenceRadioButton.setSelected(false);
            tickleDateField.setDate(null);
            goalRadioButton.setSelected(false);
            goalLevelCombo.setSelectedItem(null);
            superGoalText.setText("");
            // Actionable
            successText.setText("");
            contextCombo.setSelectedItem(null);
            timeCombo.setSelectedItem(null);
            energyCombo.setSelectedItem(null);
            priorityCombo.setSelectedItem(null);
            // Status
            switch (ActionPrefs.getNewActionState()) {
                case DOASAP:
                    statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                    break;
                case DELEGATED:
                    statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                    break;
                case INACTIVE:
                    statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                    break;
                case SCHEDULED:
                    statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                    break;
            }
            notesField.setText("");
            notesFieldNA.setText("");

            // done and created
            doneCheckBox.setSelected(false);
            doneDateField.setDate(null);
            createdDateField.setDate(Calendar.getInstance().getTime());
        } else {
            // Not Actionable
            deleteRadioButton.setSelected(false);
            somedayRadioButton.setSelected(true);
            referenceRadioButton.setSelected(false);
            tickleDateField.setDate(null);
            goalRadioButton.setSelected(false);
            goalLevelCombo.setSelectedItem(null);
            superGoalText.setText("");
            notesField.setText(action.getNotes());
            notesFieldNA.setText(action.getNotes());

            // Actionable
            successText.setText(action.getSuccess());
            if (action.getDescription().trim().length() > 0) {
                descrField.setText(action.getDescription());
            }
            contextCombo.setSelectedItem(action.getContext());
            timeCombo.setSelectedItem(action.getTime());
            energyCombo.setSelectedItem(action.getEnergy());
            priorityCombo.setSelectedItem(action.getPriority());

            // Status
            switch (action.getState().getType()) {
                case DELEGATED: {
                    statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                    ActionStateDelegated s = (ActionStateDelegated) action.getState();
                    delegateCombo.setIncludeInactive(action.isDone());
                    if (s.hasDelegateValue()) {
                        delegateCombo.setSelectedItem(ActorUtils.instance().getActor(s.getActorID()));
                    } else if (s.hasToValue()) {
                        delegateCombo.setSelectedItem(s.getTo());
                    } else {
                        delegateCombo.selectDefault();
                    }
                    followupField.setDate(s.getDate());
                    break;
                }
                case DOASAP: {
                    statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                    break;
                }
                case INACTIVE: {
                    statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                    break;
                }
                case SCHEDULED: {
                    statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                    ActionStateScheduled s = (ActionStateScheduled) action.getState();
                    scheduledDateField.setDate(s.getDate());
                    scheduledHourSpinner.setVal(s.getSchdHour());
                    scheduledMinuteSpinner.setVal(s.getSchdMinute());
                    durationHourSpinner.setVal(s.getDurationHours());
                    durationMinuteSpinner.setVal(s.getDurationMinutes());
                    recurrenceButton.setText(LBL_CREATE);
                    break;
                }
            }
            dueDateField.setDate(action.getDueDate());
            startDateField.setDate(action.getStartDate());

            // done and created
            doneCheckBox.setSelected(false);
            doneDateField.setDate(null);
            createdDateField.setDate(action.getCreated());

            newProjectField.setText(descrField.getText());
        }                
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
    
    private ActionListener actionListenerThoughtTopic;
    private FocusListener focusListenerThoughtText;
    private ActionListener actionListenerDelete;
    private ActionListener actionListenerSomeday;
    private ActionListener actionListenerReference;
    private ActionListener actionListenerGoal;
    private ActionListener supergoalButtonListener;
    private ActionListener actionListenerEmail;
    private PropertyChangeListener doneDatePropertyChangeListener;
    private PropertyChangeListener scheduledDateListener;
    private ActionListener doneCheckBoxActionListener;
    private ItemListener statusListener;
    private ActionListener recurrenceButtonListener;
    private ActionListener addToProjectButtonListener;
    private ActionListener newProjectCheckBoxListener;
    
    private ActionListener addToProjectCheckBoxListener;
    
    private FocusListener newProjectTextFocusListener;
    private ActionListener actionableYesListener;
    private ActionListener actionableNoListener;
    private PreferenceChangeListener prefsChangeListener;
    private Observer criteriaObserver;

    private void addListeners() {
        actionListenerThoughtTopic = (ActionEvent evt) -> {
            changedTopic();
        };
        topicCombo.addActionListener(actionListenerThoughtTopic);

        focusListenerThoughtText = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                changedDescription();
            }
        };
        thoughtText.addFocusListener(focusListenerThoughtText);

        actionableYesListener = (ActionEvent arg0) -> {
            changedToActionable();
        };
        actionableYesRadioButton.addActionListener(actionableYesListener);

        actionableNoListener = (ActionEvent arg0) -> {
            changedToNotActionable();
        };
        actionableNoRadioButton.addActionListener(actionableNoListener);

        actionListenerEmail = (ActionEvent evt) -> {
            emailActionPerformed();
        };
        emailButton.addActionListener(actionListenerEmail);

        actionListenerDelete = (ActionEvent evt) -> {
            enableDisableNotActionable();
        };
        deleteRadioButton.addActionListener(actionListenerDelete);

        actionListenerSomeday = (ActionEvent evt) -> {
            enableDisableNotActionable();
        };
        somedayRadioButton.addActionListener(actionListenerSomeday);

        actionListenerReference = (ActionEvent evt) -> {
            enableDisableNotActionable();
        };
        referenceRadioButton.addActionListener(actionListenerReference);

        actionListenerGoal = (ActionEvent evt) -> {
            enableDisableNotActionable();
        };
        goalRadioButton.addActionListener(actionListenerGoal);

        // Done CheckBox
        doneCheckBoxActionListener = (ActionEvent evt) -> {
            doneCheckBoxChanged();
        };
        doneCheckBox.addActionListener(doneCheckBoxActionListener);

        // Done Date
        doneDatePropertyChangeListener = (PropertyChangeEvent e) -> {
            changedDoneDate();
        };
        doneDateField.addPropertyChangeListener("value", doneDatePropertyChangeListener);

        // status
        statusListener = (ItemEvent e) -> {
            changedStatus();
        };
        statusCombo.addItemListener(statusListener);

        recurrenceButtonListener = (ActionEvent e) -> {
            recurrenceButtonActionPerformed(e);
        };
        recurrenceButton.addActionListener(recurrenceButtonListener);

        scheduledDateListener = (PropertyChangeEvent arg0) -> {
            changedScheduledDate();
        };
        scheduledDateField.addPropertyChangeListener(scheduledDateListener);

        newProjectCheckBoxListener = (ActionEvent e) -> {
            newProjectActionPerformed(e);
        };
        newProjectCheck.addActionListener(newProjectCheckBoxListener);

        newProjectTextFocusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                newProjectTextFocusGained(e);
            }
        };
        newProjectField.addFocusListener(newProjectTextFocusListener);
        
        addToProjectCheckBoxListener = (ActionEvent e) -> {
            addToProjectActionPerformed(e);
        };
        addToProjectCheck.addActionListener(addToProjectCheckBoxListener);
                
        addToProjectButtonListener = (ActionEvent e) -> {
            addToProjectButtonActionPerformed(e);
        };
        addToProjectButton.addActionListener(addToProjectButtonListener);
        
        supergoalButtonListener = (ActionEvent e) -> {
            supergoalButtonActionPerformed(e);
        };
        supergoalButton.addActionListener(supergoalButtonListener);

        prefsChangeListener = (PreferenceChangeEvent e) -> {
            changedPrefs(e);
        };
        ActionPrefs.getPrefs().addPreferenceChangeListener(prefsChangeListener);

        criteriaObserver = (Observable observable, Object arguement) -> {
            changedCriteria();
        };

        data.getTimeCriterion().addObserver(criteriaObserver);
        data.getEnergyCriterion().addObserver(criteriaObserver);
        data.getPriorityCriterion().addObserver(criteriaObserver);
    }

    private void removeListeners() {
        topicCombo.removeActionListener(actionListenerThoughtTopic);
        actionListenerThoughtTopic = null;
        thoughtText.removeFocusListener(focusListenerThoughtText);
        focusListenerThoughtText = null;
        actionableYesRadioButton.removeActionListener(actionableYesListener);
        actionableYesListener = null;
        actionableNoRadioButton.removeActionListener(actionableNoListener);
        actionableNoListener = null;
        emailButton.removeActionListener(actionListenerEmail);
        actionListenerEmail = null;
        deleteRadioButton.removeActionListener(actionListenerDelete);
        actionListenerDelete = null;
        somedayRadioButton.removeActionListener(actionListenerSomeday);
        actionListenerSomeday = null;
        referenceRadioButton.removeActionListener(actionListenerReference);
        actionListenerReference = null;
        goalRadioButton.removeActionListener(actionListenerGoal);
        supergoalButton.removeActionListener(supergoalButtonListener);
        supergoalButtonListener = null;
        actionListenerGoal = null;
        doneCheckBox.removeActionListener(doneCheckBoxActionListener);
        doneCheckBoxActionListener = null;
        doneDateField.removePropertyChangeListener(doneDatePropertyChangeListener);
        doneDatePropertyChangeListener = null;
        recurrenceButton.removeActionListener(recurrenceButtonListener);
        recurrenceButtonListener = null;
        scheduledDateField.removePropertyChangeListener(scheduledDateListener);
        scheduledDateListener = null;
        newProjectCheck.removeActionListener(newProjectCheckBoxListener);
        newProjectCheckBoxListener = null;
        newProjectField.removeFocusListener(newProjectTextFocusListener);
        newProjectTextFocusListener = null;
        addToProjectButton.removeActionListener(addToProjectButtonListener);
        addToProjectButtonListener = null;
        ActionPrefs.getPrefs().removePreferenceChangeListener(prefsChangeListener);
        prefsChangeListener = null;
        data.getTimeCriterion().removeObserver(criteriaObserver);
        data.getEnergyCriterion().removeObserver(criteriaObserver);
        data.getPriorityCriterion().removeObserver(criteriaObserver);
        criteriaObserver = null;
    }

    private void changedDescription() {
        if (thought != null) {
            thought.setDescription(thoughtText.getText());
        }
    }

    private void changedTopic() {
        if (thought != null) {
            thought.setTopic((Topic) topicCombo.getSelectedItem());
        }
    }

    private void changedToActionable() {
        panelChoice.removeAll();
        panelChoice.add(getPanelActionable(), BorderLayout.CENTER);
        panelChoice.revalidate();
        panelChoice.repaint();
        if (ActionPrefs.isShowSuccess()) {
            successText.requestFocusInWindow();
        } else {
            descrField.requestFocusInWindow();
        }
        // transfer notes
        if (!notesField.getText().equals(notesFieldNA.getText())) {
            notesField.setText(notesFieldNA.getText());
        }

        SystemAction.get(ProcessAddAction.class).setEnabled(thought != null);
    }

    private void changedToNotActionable() {
        panelChoice.removeAll();
        panelChoice.add(getPanelNotActionable(), BorderLayout.CENTER);
        panelChoice.revalidate();
        panelChoice.repaint();
        enableDisableNotActionable();
        if (deleteRadioButton.isSelected()) {
            deleteRadioButton.requestFocusInWindow();
        } else if (somedayRadioButton.isSelected()) {
            somedayRadioButton.requestFocusInWindow();
        } else if (referenceRadioButton.isSelected()) {
            referenceRadioButton.requestFocusInWindow();
        } else {
            goalRadioButton.requestFocusInWindow();
        }
        // transfer notes
        if (!notesFieldNA.getText().equals(notesField.getText())) {
            notesFieldNA.setText(notesField.getText());
        }

        SystemAction.get(ProcessAddAction.class).setEnabled(false);
    }
   
    private void emailActionPerformed() {
        Object object = delegateCombo.getSelectedItem();
        if (object == null) {
            return;
        }

        String addr;
        String name;
        boolean team = false;
        
        if (object instanceof String str) {
            addr = str;
            name = addr;
        } else {
            Actor actor = (Actor)object;
            addr = actor.getEmail().length() > 0 ? actor.getEmail() : actor.getName();
            name = actor.getName();
            team = actor.isTeam();
        }
        if (addr == null) {
            return;
        }
        addr = addr.trim();
        if (addr.length() == 0) {
            return;
        }
        if (name == null || name.trim().length() == 0) {
            name = addr;
        }
        if (Utilities.getOperatingSystem() == Utilities.OS_LINUX && !addr.contains("@")) {
            JOptionPane.showMessageDialog(null, getMsg("ERR_EmailRequired"), "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String subject = descrField.getText();
        String message = notesField.getText();

        // Set fields to action so that we can make call to get delegate data
        action.setTime((Value) timeCombo.getSelectedItem());
        action.setPriority((Value) priorityCombo.getSelectedItem());
        action.setTopic((Topic) topicCombo.getSelectedItem());
        action.setSuccess(successText.getText());
        action.setDueDate(dueDateField.getDate());
        action.setStartDate(startDateField.getDate());
                
        if (team) {
            DelegationData dd = DelegationUtils.getSendDelegateData(action);
            if (dd != null) {
                message += "\n\n" + dd.serialize();            
            }
        }
        
        sendEmail(addr, subject, message, team);                
        
        Date today = Calendar.getInstance().getTime();        
        if (ActionPrefs.isNoteDelegateEmail()) {
            addToNotes(getMsg("MSG_EmailSent", Constants.DATE_TIME_FORMAT_FIXED.format(today), name.trim()));
        }
    }

    private void sendEmail(String to, String subject, String body, boolean team) {
        if (!to.contains("@")) {
            sendEmailIndirect(to, subject, body);                                
        }
        switch (EmailPrefs.getDelegateEmailMethod()) {                
            case DIRECT_ALL:
                sendEmailDirect(to, subject, body);
                break;
            case DIRECT_TEAM:
                if (team) {
                    sendEmailDirect(to, subject, body);
                } else {
                    sendEmailIndirect(to, subject, body);                    
                }
                break;
            case EMAIL_CLIENT:                
            default:          
                sendEmailIndirect(to, subject, body);                
        }        
    }
    
    // Send email directly using SMTP
    private void sendEmailDirect(String to, String subject, String body) {                
        if (SMTPSender.isValidSMTPPreferences()) {
            SMTPSender.send(to, subject, body);
        } else {
            sendEmailIndirect(to, subject, body);                                        
        }
    }

    // Send email indirectly using email client application.
    private void sendEmailIndirect(String to, String subject, String body) {        
        Email.email(to, subject, body, ActionPrefs.getEmailEncoding());
    }
    
    private void changedScheduledDate() {
        recurrenceButton.setEnabled(scheduledDateField.getDate() != null);
    }

    private void changedStatus() {
        StatusEnum status = (StatusEnum) statusCombo.getSelectedItem();
        if (status == null) {
            SwingUtilities.invokeLater(() -> {
                statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
            });
            setStatusDoASAP();
            return;
        }
        switch (status) {
            case DELEGATED:
                setStatusDelegated();
                return;
            case DO_ASAP:
                setStatusDoASAP();
                return;
            case INACTIVE:
                setStatusInactive();
                return;
            case SCHEDULED:
                setStatusScheduled();
                recurrenceButton.setEnabled(scheduledDateField.getDate() != null);
        }
    }

    private void changedDoneDate() {
        doneCheckBox.setSelected(doneDateField.getDate() != null);
    }

    private void doneCheckBoxChanged() {
        if (doneCheckBox.isSelected()) {
            if (doneDateField.getDate() == null) {
                doneDateField.setDate(Calendar.getInstance().getTime());
            }
        } else { // not done
            doneDateField.setDate(null);
        }
    }

    private void changedPrefs(PreferenceChangeEvent e) {
        String key = e.getKey();
        if (key.equals(ActionPrefs.KEY_EDIT_CREATE_DATE)) {
            createdDateField.setEnabled(thought != null && ActionPrefs.isEditCreateDate());
            return;
        }
        if (key.equals(ActionPrefs.KEY_NOTE_DELEGATE_EMAIL)) {
            // no need to do anything
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_DUE)) {
            // force rebuild of status panel
            if (actionableYesRadioButton.isSelected()) {
                changedStatus();
            }
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_START)) {
            // force rebuild of status panel
            if (actionableYesRadioButton.isSelected()) {
                changedStatus();
            }
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_SUCCESS)) {
            // force rebuild of actionable panel
            panelActionable = null;
            if (actionableYesRadioButton.isSelected()) {
                changedToActionable();
            }
        }
    }

    // in case one or more criteria have change use.
    private void changedCriteria() {
        panelActionable = null;
        if (actionableYesRadioButton.isSelected()) {
            changedToActionable();
        }
    }

    private void addToNotes(String text) {
        String notes = notesField.getText().trim();
        if (notes.contains(text)) {
            return;
        }
        if (notes.length() == 0) {
            notesField.setText(text);
        } else {
            notesField.setText(notes + "\n" + text);
        }
    }

    private void enableDisableNotActionable() {
        tickleDateLabel.setEnabled(somedayRadioButton.isSelected());
        tickleDateField.setEnabled(somedayRadioButton.isSelected());
        if (deleteRadioButton.isSelected()) {
            notesLabelNA.setEnabled(false);
            notesFieldNA.setEnabled(false);
            notesFieldNA.setEditable(false);
        } else {
            notesLabelNA.setEnabled(true);
            notesFieldNA.setEnabled(true);
        }
        goalLevelLabel.setEnabled(goalRadioButton.isSelected());
        goalLevelCombo.setEnabled(goalRadioButton.isSelected());
        superGoalLabel.setEnabled(goalRadioButton.isSelected());
        supergoalButton.setEnabled(goalRadioButton.isSelected());
        superGoalText.setEnabled(goalRadioButton.isSelected());
        superGoalText.setEditable(false);
    }

    private Thought getNextUnprocessedThought() {
        for (Thought t : thoughtManager.list()) {
            if (!t.isProcessed()) {
                return t;
            }
        }
        return null;
    }

    private int countUnprocessedThoughts() {
        int c = 0;
        for (Thought t : thoughtManager.list()) {
            if (!t.isProcessed()) {
                c++;
            }
        }
        return c;
    }
    
    private final Observer thoughtObserver = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            assert (observable instanceof Thought);
            if (arg == Thought.Change.DESCRIPTION) {
                if (!thoughtText.getText().equals(thought.getDescription())) {
                    thoughtText.setText(thought.getDescription());
                }
            } else if (arg == Thought.Change.TOPIC) {
                topicCombo.setSelectedItem(thought.getTopic());
                thoughtText.setForeground(thought.getTopic().getForeground());
                thoughtText.setBackground(thought.getTopic().getBackground());
            }
        }
    };

    /* Initialise the model for processing the current thought. */
    public synchronized void initModel() {
        
        newProjectCheck.setSelected(false);        
        newProjectField.setEnabled(false);
        
        addToProjectCheck.setSelected(false);
        addToProjectButton.setEnabled(false);
        addToProjectField.setEnabled(false);        
        
        if (thought != null) {
            thought.removeObserver(this);
        }
        thought = getNextUnprocessedThought();

        if (thought == null) {
            action = null;
            enableFields(false);
            processNodeProvider.provide(null);
            delegationResponse = false;                       
        } else {
            if (thought.getAction() != null) {
                action = thought.getAction();
            } else {
                action = new Action(data);
                action.setDescription(thought.getDescription());
                action.setNotes(thought.getNotes());
                action.setTopic(thought.getTopic());
                switch (ActionPrefs.getNewActionState()) {
                    case DELEGATED:
                        action.setState(new ActionStateDelegated());
                        break;
                    case DOASAP:
                        action.setState(new ActionStateASAP());
                        break;
                    case INACTIVE:
                        action.setState(new ActionStateInactive());
                        break;
                    case SCHEDULED:
                        action.setState(new ActionStateScheduled());
                        break;
                }
            }
            thought.addObserver(thoughtObserver);
            recurrenceProject = null;
            enableFields(true);
            
            delegationResponse = thought.isDelegationResponse();
            if (delegationResponse) {
                delegationOriginalAction = Services.instance.getAction(thought.getDelegationActionID());   
                if (delegationOriginalAction == null) {
                    // Probably original action has been deleted so treat as normal thought. 
                    processNodeProvider.provide(processNode);                                    
                } else {
                    delegationOriginalActionCopy = delegationOriginalAction.copy();                
                    delegationOriginalActionCopy.disableDelegationResponse = true;                
                    delegationResponseAction = action;
                    Thought t = new Thought(data.getNextID());
                    t.setDescription(delegationOriginalAction.getDescription());
                    t.setTopic(delegationOriginalAction.getTopic());
                    t.setDelegationActionID(delegationOriginalAction.getID());
                    t.setProcessed(true);                
                    delegationResponseAction.setThought(t);                
                    if (thought.isDelegationDone()) {
                        delegationResponseAction.setDoneDate(thought.getDelegationDone());                    
                    }                
                    processNodeProvider.provide(processNodeDelegationResponse);                                
                }
            } else {
                processNodeProvider.provide(processNode);                
            }
        }
        
        initFieldValues();

        actionableYesRadioButton.setSelected(true);

        changedToActionable();
        changedStatus();

        actionableYesRadioButton.requestFocusInWindow();
    }
    
    /**
     * Determines whether processing is valid.
     * @return null if processing is valid, otherwise a message containing the
     * problems.
     */
    public String validateProcessing() {

        StringBuilder sb = new StringBuilder();

        if (actionableNoRadioButton.isSelected()) {
            if (!deleteRadioButton.isSelected() && thoughtText.getText().trim().equals("")) {
                sb.append(getMsg("no.thought.description")).append("\n");
            }
            if (goalRadioButton.isSelected()) {
                if (!(goalLevelCombo.getSelectedItem() instanceof LevelCtrl)) {
                    sb.append(getMsg("no.goal.level")).append("\n");
                }
            }
        } else {
            if (descrField.getText().trim().length() == 0) {
                sb.append(getMsg("no.action.description")).append("\n");
            }
            if (statusCombo.getSelectedItem() == StatusEnum.SCHEDULED) {
                if (scheduledDateField.getDate() == null) {
                    sb.append(getMsg("no.scheduled.date")).append("\n");
                }
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /** Process the current item. */
    @Override
    public void process() {
        if (delegationResponse) {
            processDelegationResponse();
        } else {
            _process(true);            
        }
    }

    /**
     * Process the thought.
     * @param finalise If true, set thought as processed and load next thought. 
     */
    private void _process(boolean finalise) {
        if (thought == null || action == null) {
            return;
        }

        String problems = validateProcessing();
        if (problems != null) {
            String t = getMsg("dialog.problems.title");
            String m = getMsg("dialog.problems.message") + "\n\n" + problems + "\n";
            JOptionPane.showMessageDialog(null, m, t, JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (actionableNoRadioButton.isSelected()) {
            if (deleteRadioButton.isSelected()) {
                thoughtManager.remove(thought);
            } else if (referenceRadioButton.isSelected()) {
                Information reference = new Information(data.getNextID());
                reference.setTopic((Topic) topicCombo.getSelectedItem());
                reference.setDescription(thoughtText.getText());
                reference.setNotes(notesFieldNA.getText());
                referenceManager.add(reference);
            } else if (somedayRadioButton.isSelected()) {
                Future someday = new Future(data.getNextID());
                someday.setThought(thought);
                someday.setDescription(thoughtText.getText());
                someday.setTopic((Topic) topicCombo.getSelectedItem());
                someday.setTickle(tickleDateField.getDate());
                someday.setNotes(notesFieldNA.getText());
                somedayManager.add(someday);
            } else if (goalRadioButton.isSelected()) {
                Topic topic = (Topic) topicCombo.getSelectedItem();
                if (topic == null) {
                    topic = Topic.getDefault();
                }
                LevelCtrl levelCtrl = (LevelCtrl) goalLevelCombo.getSelectedItem();
                if (supergoal == null) {
                    supergoal = GoalsCtrlLookup.getGoalsCtrl().getRootGoal();
                }
                supergoal.insert(levelCtrl.getID(), topic.getID(), thought.getDescription(),
                        "", "", "", "", "", "", notesFieldNA.getText(), null, null, null);
            }
        } else { // actionable
            action.setThought(thought);
            action.setSuccess(successText.getText());
            action.setDescription(descrField.getText());
            action.setTopic((Topic) topicCombo.getSelectedItem());
            action.setContext((Context) contextCombo.getSelectedItem());
            action.setTime((Value) timeCombo.getSelectedItem());
            action.setEnergy((Value) energyCombo.getSelectedItem());
            action.setPriority((Value) priorityCombo.getSelectedItem());
            action.setNotes(notesField.getText());
            action.setDone(doneCheckBox.isSelected());
            action.setDoneDate(doneDateField.getDate());
            action.setCreated(createdDateField.getDate());
            StatusEnum status = (StatusEnum) statusCombo.getSelectedItem();
            switch (status) {
                case DELEGATED:
                    ActionStateDelegated stateDelegated = new ActionStateDelegated();
                    stateDelegated.setDate(followupField.getDate());
                    Object delegate = delegateCombo.getSelectedItem();
                    if (delegate instanceof String str) {
                        stateDelegated.setTo(str);
                    } else if (delegate instanceof Actor actor) {
                        stateDelegated.setActorID(actor.getID());
                    }
                    action.setState(stateDelegated);
                    action.setDueDate(dueDateField.getDate());
                    action.setStartDate(startDateField.getDate());
                    break;

                case DO_ASAP:
                    action.setState(new ActionStateASAP());
                    action.setDueDate(dueDateField.getDate());
                    action.setStartDate(startDateField.getDate());
                    break;
                case INACTIVE:
                    action.setState(new ActionStateInactive());
                    action.setDueDate(dueDateField.getDate());
                    action.setStartDate(startDateField.getDate());
                    break;
                case SCHEDULED:
                    if (!action.isStateScheduled()) {
                        action.setState(new ActionStateScheduled());
                    }
                    ActionStateScheduled stateScheduled = (ActionStateScheduled) action.getState();
                    stateScheduled.setDate(scheduledDateField.getDate());
                    stateScheduled.setSchdHour(scheduledHourSpinner.getVal());
                    stateScheduled.setSchdMinute(scheduledMinuteSpinner.getVal());                    
                    stateScheduled.setDurationHours(durationHourSpinner.getVal());
                    stateScheduled.setDurationMins(durationMinuteSpinner.getVal());
                    action.setDueDate(null);
                    action.setStartDate(null);
                    break;
            }

            if (newProjectCheck.isSelected()) {
                Project project = new Project(data);
                project.setThought(thought);
                String desc = newProjectField.getText().trim();
                project.setDescription(desc.length() > 0 ? desc : thought.getDescription());
                project.setTopic(action.getTopic());
                project.setDone(false);
                project.setVision(action.getSuccess());
                project.setNotes(notesField.getText());
                project.add(action);

                // add to selected project or projects root
                if (addToProjectCheck.isSelected() && null != addToProject) {
                    project.setSequencing(addToProject.isSequencing());
                    project.setSeqIncludeProjects(addToProject.isSeqIncludeProjects());
                    project.setSeqIncludeScheduled(addToProject.isSeqIncludeScheduled());
                    project.setSeqIncludeDelegated(addToProject.isSeqIncludeDelegated());
                    project.setPriority(addToProject.getPriority());
                    addToProject.add(project);                    
                } else {
                    project.setSequencing(ProjectsPrefs.isSequencing());
                    project.setSeqIncludeProjects(ProjectsPrefs.getAutoSeqIncludeSubprojects());
                    project.setSeqIncludeScheduled(ProjectsPrefs.getAutoSeqIncludeScheduled());
                    project.setSeqIncludeDelegated(ProjectsPrefs.getAutoSeqIncludeDelegated());
                    projects.add(project);                    
                }

            } else { // do not create new project
                // add action to selected project or as a single action
                if (addToProjectCheck.isSelected() && null != addToProject) {
                    addToProject.add(action);
                } else {
                    singleActions.add(action);
                }
            }

            // If no action priority set to parent project priority
            if (action.getPriority() == null) {
                Project parent = (Project) action.getParent();
                if (parent != null) {
                    action.setPriority(parent.getPriority());                    
                }
            }

            // recurrence: need to transfer any actions created by recurrence
            if (action.isStateScheduled()) {
                Recurrence recurrence = action.getRecurrence();
                if (recurrence != null) {
                    if (recurrenceProject != null) {
                        Project parent = (Project) action.getParent();
                        if (parent != null) {
                            for (Action a : recurrenceProject.getChildren(Action.class)) {
                                if (a.getID() != action.getID()) {
                                    parent.add(a);
                                }
                            }
                        }
                    }
                    // set correct recurrence project
                    recurrence.setProject((Project) action.getParent());
                }
            }
        }

        if (finalise) {
            thought.setProcessed(true);
            initModel();
        }
    }
    
    private void processDelegationResponse() {
        // update original delegated action with any changes
        delegationOriginalAction.setContext(delegationOriginalActionCopy.getContext());
        delegationOriginalAction.setDescription(delegationOriginalActionCopy.getDescription());
        delegationOriginalAction.setSuccess(delegationOriginalActionCopy.getSuccess());
        delegationOriginalAction.setNotes(delegationOriginalActionCopy.getNotes());
        delegationOriginalAction.setTime(delegationOriginalActionCopy.getTime());
        delegationOriginalAction.setEnergy(delegationOriginalActionCopy.getEnergy());
        delegationOriginalAction.setPriority(delegationOriginalActionCopy.getPriority());
        delegationOriginalAction.setTopic(delegationOriginalActionCopy.getTopic());
        delegationOriginalAction.setRecurrence(delegationOriginalActionCopy.getRecurrence());
        delegationOriginalAction.setStartDate(delegationOriginalActionCopy.getStartDate());
        delegationOriginalAction.setDueDate(delegationOriginalActionCopy.getDueDate());
        delegationOriginalAction.setState(delegationOriginalActionCopy.getState());
        delegationOriginalAction.setDoneDate(delegationOriginalActionCopy.getDoneDate());
        
        // Add new delegation response action to project after original delegated 
        // action and any previous responses.
        Project parent = (Project)delegationOriginalAction.getParent();
        List<Item> items = parent.getChildren();       
        int index = items.indexOf(delegationOriginalAction);
        if (index >= 0) {                  
            int posn = index + 1;
            // move past are any previous responses            
            for (int i = posn; i < items.size(); i++) {
                if (isDelegatedActionResponse(items.get(i), delegationOriginalAction.getID())) {
                    posn++;
                } else {
                    break;
                }
            }
            parent.add(posn, delegationResponseAction);            
        }        
        
        thought.setProcessed(true);
        initModel();
    }

    /**
     * Determine if an item is a delegated action response. 
     * @param item The item to examine.
     * @param delegatedActionID The ID of the original delegated action.
     */
    private boolean isDelegatedActionResponse(Item item, int delegatedActionID) {
        if (item instanceof Action actn) {
            Thought itemThought = actn.getThought();
            if (itemThought != null) {
                if (Objects.equals(itemThought.getDelegationActionID(), delegatedActionID)) {
                    return true;
                }                
            }
        }
        return false;
    }
    
    public void takeFocus() {
        actionableYesRadioButton.requestFocusInWindow();
    }

    /** Handle thought manager change
     * @param arg */
    @Override
    public void update(Observable observable, Object arg) {
        if (observable == thoughtManager) {
            if (thought == null) {
                initModel();
            }
        }
    }

    private void initPanel() {
        initFields();
        initShortcuts();
        setLayout(new BorderLayout());
        add(createPanel(), BorderLayout.CENTER);
    }

    private void initShortcuts() {
        KeyAdapter deleteKeyAdapter = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume();
                    delete();
                }
            }
        };
        actionableYesRadioButton.addKeyListener(deleteKeyAdapter);
        actionableNoRadioButton.addKeyListener(deleteKeyAdapter);
    }

    private void initFields() {
        thoughtLabel = new TRLabel(getMsg("LBL_Thought"));
        thoughtText = new TRTextField();

        topicLabel = new TRLabel(LBL_TOPIC);
        topicCombo = new TopicsComboBox(new TopicsComboBoxModel(false));

        actionableYesRadioButton = new TRRadioButton(LBL_YES);
        actionableNoRadioButton = new TRRadioButton(LBL_NO);

        actionableButtonGroup = new ButtonGroup();
        actionableButtonGroup.add(actionableNoRadioButton);
        actionableButtonGroup.add(actionableYesRadioButton);

        // NOT ACTIONABLE
//      deleteLabel.setIcon(Icons.Trash);
//      deleteLabel.setText(getMsg(LBL_DELETE));
        deleteRadioButton = new TRRadioButton();
        deleteRadioButton.setText(LBL_DELETE);

//      futureLabel.setIcon(Icons.SomedayMaybe);
//      futureLabel.setText(getMsg(LBL_SOMEDAY));
        somedayRadioButton = new TRRadioButton();
        somedayRadioButton.setText(LBL_SOMEDAY);
        tickleDateLabel = new TRLabel(LBL_TICKLE_DATE);
        tickleDateField = new DateField();

//      informationLabel.setIcon(Icons.Reference);
//      informationLabel.setText(getMsg(LBL_REFERENCE));
        referenceRadioButton = new TRRadioButton();
        referenceRadioButton.setText(LBL_REFERENCE);

        goalRadioButton = new TRRadioButton(LBL_GOAL);
        goalLevelLabel = new TRLabel(LBL_GOAL_LEVEL);
        goalLevelCombo = new LevelsComboBox();
        superGoalLabel = new TRLabel(LBL_SUPERGOAL);
        supergoalButton = new TRButton("...");
        supergoalButton.setPreferredSize(SIZE_PROJECT_BUTTON);
        supergoalButton.setMinimumSize(SIZE_PROJECT_BUTTON);
        supergoalButton.setMaximumSize(SIZE_PROJECT_BUTTON);
        superGoalText = new TRTextField();

        notesLabelNA = new TRLabel(LBL_NOTES);
//      notesFieldNA = new HyperEditorPane();
//      notesFieldNA = new NotesEditorPanel();
        notesFieldNA = new NotesEditorPanel();
        notesFieldNA.setMinimumSize(SIZE_NOTES_EDITOR);
        notesFieldNA.setPreferredSize(SIZE_NOTES_EDITOR);

        notesLabel = new TRLabel(LBL_NOTES);
//      notesField = new HyperEditorPane();
        notesField = new NotesEditorPanel();
        notesField.setMinimumSize(SIZE_NOTES_EDITOR);
        notesField.setPreferredSize(SIZE_NOTES_EDITOR);

        // ACTIONABLE
        actionableLabel = new TRLabel(LBL_ACTIONABLE);
        successLabel = new TRLabel(LBL_SUCCESS);
        successText = new MTextArea();
        actionLabel = new TRLabel(LBL_NEXT_ACTION);
        descriptionLabel = new TRLabel(LBL_DESCRIPTION);
        descrField = new TRTextField();
        contextLabel = new TRLabel(LBL_CONTEXT);
        contextCombo = new ContextsComboBox(new ContextsComboBoxModel(false));
        timeLabel = new TRLabel(LBL_TIME);
        timeCombo = new TRComboBox<>(getTimeComboBoxModel());
        energyLabel = new TRLabel(LBL_ENERGY);
        energyCombo = new TRComboBox<>(getEnergyComboBoxModel());
        priorityLabel = new TRLabel(LBL_PRIORITY);
        priorityCombo = new TRComboBox<>(getPriorityComboBoxModel());
        statusLabel = new TRLabel(LBL_STATUS);
        statusCombo = new TRComboBox<>(new StatusComboBoxModel());
        recurrenceLabel = new TRLabel(LBL_RECURRENCE);
        recurrenceButton = new TRButton(LBL_CREATE);
        dueDateLabel = new TRLabel(LBL_DUE_DATE);
        dueDateField = new DateField();
        startDateLabel = new TRLabel(LBL_START_DATE);
        startDateField = new DateField();
        notActionableButtonGroup = new ButtonGroup();
        notActionableButtonGroup.add(deleteRadioButton);
        notActionableButtonGroup.add(somedayRadioButton);
        notActionableButtonGroup.add(referenceRadioButton);
        notActionableButtonGroup.add(goalRadioButton);
        scheduledHourSpinner = new HourSpinner();
        scheduledMinuteSpinner = new MinuteSpinner();
        durationLabel = new TRLabel(LBL_DURATION);
        durationHourSpinner = new HourSpinner();
        durationMinuteSpinner = new MinuteSpinner();
        scheduledDateField = new DateField();
        delegatedToLabel = new TRLabel(LBL_DELEGATE);
        delegateCombo = new ActorsComboBox();
        emailButton = new TRButton(LBL_EMAIL);
        followupLabel = new TRLabel(LBL_FOLLOWUP);
        followupField = new DateField();
        doneCheckBox = new TRCheckBox(LBL_DONE);
        doneCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
        doneDateField = new DateField();
        createdDateLabel = new TRLabel(LBL_CREATED);
        createdDateField = new DateField();
        projectLabel = new TRLabel(LBL_PROJECT);
        
        newProjectCheck = new TRCheckBox(LBL_NEW_PROJECT);
//        newProjectCheck.setHorizontalTextPosition(SwingConstants.LEADING);
        newProjectField = new TRTextField();
        addToProjectField = new TRTextField();
//        addToProjectLabel = new TRLabel(LBL_ADD_TO_PROJECT);
          addToProjectCheck = new TRCheckBox(LBL_ADD_TO_PROJECT);        
        addToProjectButton = new TRButton("...");
        addToProjectButton.setPreferredSize(SIZE_PROJECT_BUTTON);
        addToProjectButton.setMinimumSize(SIZE_PROJECT_BUTTON);
        addToProjectButton.setMaximumSize(SIZE_PROJECT_BUTTON);
    }

//    private JPanel createPanel() {
//
//        JPanel panel;
//
//        if (ViewUtils.isAquaLaF()) {
//            panel = new JPanel(new MigLayout(getMIGCons(), "[][grow]", "4[]2[]2[]2[]2[grow]0"));
//        } else {
//            panel = new JPanel(new MigLayout(getMIGCons(), "[][grow]", "4[center]2[top]2[center]2[center]2[grow]0"));
//        }
//
//        panel.setBackground(ViewUtils.COLOR_PANEL_BG);
//
//        ViewUtils.addSeparator(panel, thoughtLabel);
//
//        panel.add(thoughtText, "align left, span, growx 100, split 3");
//        panel.add(topicLabel, "align left");
//        panel.add(topicCombo, "align left, wrap");
//
//        ViewUtils.addSeparator(panel, actionableLabel);
//
//        panel.add(actionableYesRadioButton, "align left, split 2");
//        panel.add(actionableNoRadioButton, "align left, wrap");
//
//        panelChoice = new JPanel(new BorderLayout());
//        panelChoice.setBackground(ViewUtils.COLOR_PANEL_BG);
//
//        panel.add(panelChoice, "span, grow");
//
//        return panel;
//    }
    private JPanel createPanel() {

        panelThought = new JPanel();
        panelThought.setBackground(ViewUtils.COLOR_PANEL_BG);        
        panelThought.setLayout(new MigLayout(getMIGCons(), "[][grow]", "4[]2"));
        ViewUtils.addSeparator(panelThought, thoughtLabel);
        panelThought.add(thoughtText, "align left, span, growx 100, split 3");
        panelThought.add(topicLabel, "align left");
        panelThought.add(topicCombo, "align left, wrap");
                                
        panelNormal = new JPanel();
        panelNormal.setBackground(ViewUtils.COLOR_PANEL_BG);        
        if (ViewUtils.isAquaLaF()) {
            panelNormal.setLayout(new MigLayout(getMIGCons(), "[][grow]", "2[]2[]2[grow]0"));
        } else {
            panelNormal.setLayout(new MigLayout(getMIGCons(), "[][grow]", "2[top]2[center]2[grow]0"));
        }       
        ViewUtils.addSeparator(panelNormal, actionableLabel);
        panelNormal.add(actionableYesRadioButton, "align left, split 2");
        panelNormal.add(actionableNoRadioButton, "align left, wrap");
        panelChoice = new JPanel(new BorderLayout());
        panelChoice.setBackground(ViewUtils.COLOR_PANEL_BG);        
        panelNormal.add(panelChoice, "span, grow");

        panelMain = new JPanel(new BorderLayout());
        panelMain.setBackground(ViewUtils.COLOR_PANEL_BG);        
        panelMain.add(panelNormal, BorderLayout.CENTER);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ViewUtils.COLOR_PANEL_BG);        
        panel.add(panelThought, BorderLayout.NORTH);
        panel.add(panelMain, BorderLayout.CENTER);
        return panel;
    }

    private JPanel getPanelNotActionable() {
        if (panelNotActionable == null) {
            if (ViewUtils.isAquaLaF()) {
                panelNotActionable = new JPanel(new MigLayout(getMIGCons(),
                        "0[]12[]4[]4[]4[]2[grow]0", "2[]2[]2[]2[]2[]2[]0[grow]4"));
                ViewUtils.addSeparator(panelNotActionable, LBL_NOT_ACTIONABLE);
                panelNotActionable.add(deleteRadioButton, "align left, wrap");
                panelNotActionable.add(somedayRadioButton, "align left");
                panelNotActionable.add(tickleDateLabel, "align left");
                panelNotActionable.add(tickleDateField, "align left, wrap");
                panelNotActionable.add(referenceRadioButton, "align left, wrap");
                panelNotActionable.add(goalRadioButton, "align left");
                panelNotActionable.add(goalLevelLabel, "align right");
                panelNotActionable.add(goalLevelCombo, "align left");
                panelNotActionable.add(superGoalLabel, "align left");
                panelNotActionable.add(supergoalButton, "align left");
                panelNotActionable.add(superGoalText, "align  left, growx, wrap");
                ViewUtils.addSeparator(panelNotActionable);
                panelNotActionable.add(notesLabelNA, "spanx, split 2, align right top, gaptop 10, growx 0, growy 0");
                panelNotActionable.add(notesFieldNA, "gapleft 3, gapright 3, grow");
            } else {
                panelNotActionable = new JPanel(new MigLayout(getMIGCons(),
                        "0[]12[]4[]4[]4[]2[grow]0", "2[top]2[top]2[top]2[top]2[top]2[top]0[grow]4"));
                ViewUtils.addSeparator(panelNotActionable, LBL_NOT_ACTIONABLE);
                panelNotActionable.add(deleteRadioButton, "ax left,  wrap");
                panelNotActionable.add(somedayRadioButton, "ax left,  sgy g1");
                panelNotActionable.add(tickleDateLabel, "ax left,  sgy g1");
                panelNotActionable.add(tickleDateField, "ax left,  sgy g1, wrap");
                panelNotActionable.add(referenceRadioButton, "ax left,  wrap");
                panelNotActionable.add(goalRadioButton, "ax left,  sgy g2");
                panelNotActionable.add(goalLevelLabel, "ax right, sgy g2");
                panelNotActionable.add(goalLevelCombo, "ax left,  sgy g2");
                panelNotActionable.add(superGoalLabel, "ax left,  sgy g2");
                panelNotActionable.add(supergoalButton, "ax left,  sgy g2");
                panelNotActionable.add(superGoalText, "ax left,  sgy g2, growx, wrap");
                ViewUtils.addSeparator(panelNotActionable);
                panelNotActionable.add(notesLabelNA, "spanx, split 2, ax right, ay top, growx 0, growy 0");
                panelNotActionable.add(notesFieldNA, "gapleft 3, gapright 3, grow");
            }
            panelNotActionable.setBackground(ViewUtils.COLOR_PANEL_BG);
        }
        return panelNotActionable;
    }

    private JPanel getPanelActionable() {
        if (panelActionable == null) {
            if (ActionPrefs.isShowSuccess()) {
                panelActionable = new JPanel(new MigLayout(getMIGCons(), "0[][grow]0", "0[]2[]2[]2[grow]2[]2[]2[]2"));
                ViewUtils.addSeparator(panelActionable, successLabel);
                panelActionable.add(successText, "span, growx, wrap");
            } else {
                panelActionable = new JPanel(new MigLayout(getMIGCons(), "0[][grow]0", "0[]2[grow]2[]2[]2[]2"));
            }

            panelActionable.setBackground(ViewUtils.COLOR_PANEL_BG);

            ViewUtils.addSeparator(panelActionable, actionLabel);

            panelActionable.add(getPanelAction(), "span, grow, wrap");

            ViewUtils.addSeparator(panelActionable, projectLabel);

            panelActionable.add(newProjectCheck, "align left, grow 0");
            panelActionable.add(newProjectField, "align left, growx 100, growy 0, wrap");

            panelActionable.add(addToProjectCheck, "split 2, gapright 6, align left, growx 0, growy 0");
            panelActionable.add(addToProjectButton, "gapright 6, align left, grow 0");
            panelActionable.add(addToProjectField, "align left, growx 100, growy 0, wrap");
        }
        return panelActionable;
    }

    private String getMIGCons() {
        return DEBUG_MIG_LAYOUT ? "debug" : "";
    }

    private String getMIGCons(String c) {
        assert (c != null && c.trim().length() > 0);

        return DEBUG_MIG_LAYOUT ? "debug, " + c : c;
    }
    private final MigLayout statusLayoutA1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]0");
    private final MigLayout statusLayoutA2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]2:2:2[]0");
    private final MigLayout statusLayoutM1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]0");
    private final MigLayout statusLayoutM2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]2[top]0");

    @Override
    public boolean canDelete() {
        return thought != null;
    }

    @Override
    public void delete() {
        if (canDelete()) {
            thought.setProcessed(true);
            thoughtManager.remove(thought);
            initModel();
        }
    }

    @Override
    public boolean canProcessAdd() {
        return !delegationResponse && actionableYesRadioButton.isSelected();
    }

    @Override
    public void processAdd() {
        if (!canProcessAdd()) {
            return;
        }

        String probs = validateProcessing();
        if (probs != null) {
            String t = getMsg("dialog.problems.title");
            String m = getMsg("dialog.problems.message") + "\n\n" + probs + "\n";
            JOptionPane.showMessageDialog(null, m, t, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        _process(false);

        successText.setText("");
        descrField.setText("");
        contextCombo.setSelectedIndex(0);
        timeCombo.setSelectedItem(null);
        energyCombo.setSelectedItem(null);
        priorityCombo.setSelectedItem(null);

        // reset all status values
        dueDateField.setDate(null);
        startDateField.setDate(null);
        
        scheduledDateField.setDate(null);
        scheduledHourSpinner.setVal(ActionPrefs.getSchdTimeHr());
        scheduledMinuteSpinner.setVal(ActionPrefs.getSchdTimeMn());
        durationHourSpinner.setVal(ActionPrefs.getSchdDurHrs());
        durationMinuteSpinner.setVal(ActionPrefs.getSchdDurMns());
        
        recurrenceButton.setText(LBL_CREATE);
        delegateCombo.selectDefault();
        followupField.setDate(null);
        switch (ActionPrefs.getNewActionState()) {
            case DOASAP:
                statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                break;
            case DELEGATED:
                statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                break;
            case INACTIVE:
                statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                break;
            case SCHEDULED:
                statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                break;
        }

// Mantis #0001964 - Don't clear notes when adding actions       
//      notesField.setText("");
//      notesFieldNA.setText("");
// End Mantis #0001964 
        
        doneCheckBox.setSelected(false);
        doneDateField.setDate(null);
        createdDateField.setDate(Calendar.getInstance().getTime());
        
        if (newProjectCheck.isSelected()) {
            Project project = (Project) action.getParent();
            if (!project.isRoot()) {
                addToProject = project;
                addToProjectField.setText(project.getDescription());
            }
            newProjectCheck.setSelected(false);
            newProjectField.setText("");
        }
        
        addToProjectCheck.setSelected(null != addToProject);       
        addToProjectButton.setEnabled(addToProjectCheck.isSelected());
        addToProjectField.setEnabled(addToProjectCheck.isSelected());        

        action = new Action(data);
    }

    private enum Rows {
        One, Two
    };

    private JPanel getStatusPanel(Rows rows) {

        if (panelStatus == null) {
            panelStatus = new JPanel();
            panelStatus.setOpaque(false);
            panelStatus.setBackground(ViewUtils.COLOR_PANEL_BG);
            panelStatus.setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            panelStatus.removeAll();
        }

        if (ViewUtils.isAquaLaF()) {
            panelStatus.setLayout(rows == Rows.One ? statusLayoutA1 : statusLayoutA2);
        } else {
            panelStatus.setLayout(rows == Rows.One ? statusLayoutM1 : statusLayoutM2);
        }
        return panelStatus;
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
        panel.add(FILL_LABEL_1, "wrap");
        panel.add(delegatedToLabel, "gapleft 4, span, split 5");
        panel.add(delegateCombo, "");
        panel.add(emailButton, "gapleft 2");
        panel.add(followupLabel, "");
        panel.add(followupField, "wrap");
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
        panel.add(FILL_LABEL_2, "wrap");

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
        panel.add(FILL_LABEL_3, "wrap");

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

        panel.add(recurrenceLabel, "gapleft 4, split 2");
        panel.add(recurrenceButton, "gapleft 2, wrap");
        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private JPanel getPanelAction() {
        if (panelAction == null) {

            if (ViewUtils.isAquaLaF()) {
                panelAction = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2", "0[]2[]2[]2[]2[]2[grow]2[]2[]0"));
            } else {
                panelAction = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2", "0[top]2[top]2[]2[top]2[]2[top,grow]2[]2[top]0"));
            }

            panelAction.setBackground(ViewUtils.COLOR_PANEL_BG);

            panelAction.add(descriptionLabel, "align right");
            panelAction.add(descrField, "span, growx, wrap");

            panelAction.add(contextLabel, "align right");
            panelAction.add(contextCombo, "align left, span, split");
            if (data.getTimeCriterion().isUse()) {
                panelAction.add(timeLabel, "align right");
                panelAction.add(timeCombo, "align left");
            }
            if (data.getEnergyCriterion().isUse()) {
                panelAction.add(energyLabel, "align right");
                panelAction.add(energyCombo, "align left");
            }
            if (data.getPriorityCriterion().isUse()) {
                panelAction.add(priorityLabel, "align right");
                panelAction.add(priorityCombo, "align left");
            }
            panelAction.add(FILL_LABEL_4, "wrap");

            ViewUtils.addSeparator(panelAction);

            if (ViewUtils.isAquaLaF()) {
                panelAction.add(statusLabel, "align right top, gaptop 4");
                panelAction.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
            } else {
                panelAction.add(statusLabel, "align right top");
                panelAction.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
            }

            ViewUtils.addSeparator(panelAction);

            if (ViewUtils.isAquaLaF()) {
                panelAction.add(notesLabel, "align right top, gaptop 10, growx 0, growy 0");
                panelAction.add(notesField, "span, gapleft 3, gapright 3, grow");
            } else {
                panelAction.add(notesLabel, "align right top, growx 0, growy 0");
                panelAction.add(notesField, "span, grow");
            }

            ViewUtils.addSeparator(panelAction);

            panelAction.add(doneCheckBox, "align right, grow 0");
            panelAction.add(doneDateField, "align left, split 3, grow 0");
            panelAction.add(createdDateLabel, "align left, grow 0");
            panelAction.add(createdDateField, "align left, grow 0, wrap");
        }

        return panelAction;
    }
    
    private JPanel getPanelDelegationResponse() {
        if (panelDelegation != null) {
            return panelDelegation;
        }

        // LHS panel for new delegation response action
        JPanel panelLHS = migPanel("0[grow]0", "0[]0[grow]0");
        ViewUtils.addSeparator(panelLHS, new TRLabel("Response Action"));
        panelLHS.add(getDelegationResponseActionPanel(), "grow");        
        
        // RHS panel for original delegated action
        JPanel panelRHS = migPanel("0[grow]0", "0[]0[grow]0");            
        ViewUtils.addSeparator(panelRHS, new TRLabel("Original Action"));
        panelRHS.add(getDelegationOriginalActionPanel(), "grow");        
                
        // Project path panel
        TRLabel label = new TRLabel(getMsg("LBL_Project"));
        label.setEnabled(false);
        delegationProjectPath = new TRTextField();        
        delegationProjectPath.setEnabled(false);
        JPanel panelPath = migPanel("[][grow]0", "0[grow]0");        
        panelPath.add(label, "gapleft 30, align left");        
        panelPath.add(delegationProjectPath, "align left, grow");

        // Panel to hold LHS and RHS
        JPanel panelHold = migPanel("[grow][grow]0", "0[grow]0");
        panelHold.add(panelLHS, "grow");        
        panelHold.add(panelRHS, "grow");        
        
        panelDelegation = migPanel("[grow]", "0[][grow][]0");
        ViewUtils.addSeparator(panelDelegation, new TRLabel(getMsg("LBL_DelegationResponse")));               
        panelDelegation.add(panelHold, "grow, wrap");
        panelDelegation.add(panelPath, "grow");
        
        return panelDelegation;
    }        

    private JPanel migPanel(String cc, String rc) {
        JPanel p = new JPanel(new MigLayout(getMIGCons(), cc, rc));            
        p.setBackground(ViewUtils.COLOR_PANEL_BG);                        
        return p;
    }
    
    private ActionPanel getDelegationResponseActionPanel() {
        if (delegationResponseActionPanel == null) {
            delegationResponseActionPanel = new ActionPanel();            
        }
        return delegationResponseActionPanel;
    }
    
    private ActionPanel getDelegationOriginalActionPanel() {
        if (delegationOriginalActionPanel == null) {
            delegationOriginalActionPanel = new ActionPanel();            
        }
        return delegationOriginalActionPanel;
    }
    
    private ComboBoxModel<Value> getTimeComboBoxModel() {
        if (data != null) {
            Criterion criterion = data.getTimeCriterion();
            if (criterion != null) {
                return new CriterionComboBoxModel(criterion);
            }
        }
        return new DefaultComboBoxModel<>();
    }

    private ComboBoxModel<Value> getEnergyComboBoxModel() {
        if (data != null) {
            Criterion criterion = data.getEnergyCriterion();
            if (criterion != null) {
                return new CriterionComboBoxModel(criterion);
            }
        }
        return new DefaultComboBoxModel<>();
    }

    private ComboBoxModel<Value> getPriorityComboBoxModel() {
        if (data != null) {
            Criterion criterion = data.getPriorityCriterion();
            if (criterion != null) {
                return new CriterionComboBoxModel(criterion);
            }
        }
        return new DefaultComboBoxModel<>();
    }

    private void recurrenceButtonActionPerformed(ActionEvent e) {
        if (statusCombo.getSelectedItem() != StatusEnum.SCHEDULED) {
            return;
        }
        action.setThought(thought);
        action.setSuccess(successText.getText());
        action.setDescription(descrField.getText());
        action.setTopic((Topic) topicCombo.getSelectedItem());
        action.setContext((Context) contextCombo.getSelectedItem());
        action.setTime((Value) timeCombo.getSelectedItem());
        action.setEnergy((Value) energyCombo.getSelectedItem());
        action.setPriority((Value) priorityCombo.getSelectedItem());
        action.setNotes(notesField.getText());
        action.setDone(doneCheckBox.isSelected());
        action.setDoneDate(doneDateField.getDate());
        action.setCreated(createdDateField.getDate());
        if (!action.isStateScheduled()) {
            action.setState(new ActionStateScheduled());
        }
        ActionStateScheduled state = (ActionStateScheduled) action.getState();
//        Calendar c = Calendar.getInstance();
//        c.setTime(scheduledDateField.getDate());
//        c.set(Calendar.HOUR_OF_DAY, scheduledHourSpinner.getVal());
//        c.set(Calendar.MINUTE, scheduledMinuteSpinner.getVal());
//        state.setDate(c.getTime());
        state.setDate(scheduledDateField.getDate());
        state.setSchdHour(scheduledHourSpinner.getVal());
        state.setSchdMinute(scheduledMinuteSpinner.getVal());

        state.setDurationHours(durationHourSpinner.getVal());
        state.setDurationMins(durationMinuteSpinner.getVal());
        if (recurrenceProject == null) {
            recurrenceProject = new Project(data);
            recurrenceProject.add(action);
        }
        Recurrence recurrence = action.getRecurrence();
        if (recurrence == null) {
            NewRecurrenceWizard wizard = new NewRecurrenceWizard();
            wizard.start(action);
        } else {
            ModifyRecurrenceWizard wizard = new ModifyRecurrenceWizard();
            wizard.start(action);
        }
        // update button text if necessary
        if (action.getRecurrence() == null) {
            recurrenceButton.setText(LBL_CREATE);
        } else {
            recurrenceButton.setText(LBL_MODIFY);
        }
    }

    private void newProjectTextFocusGained(FocusEvent evt) {
        if (thought == null || newProjectField == null) {
            return;
        }
        if (newProjectField.getText().equals(thought.getDescription())) {
            newProjectField.selectAll();
        }
    }

    private void newProjectActionPerformed(ActionEvent evt) {
        newProjectField.setEnabled(newProjectCheck.isSelected());
        newProjectField.setEditable(newProjectCheck.isSelected());
    }

    
    private void addToProjectActionPerformed(ActionEvent evt) {
        addToProjectButton.setEnabled(addToProjectCheck.isSelected());
        addToProjectField.setEnabled(addToProjectCheck.isSelected());        
        addToProjectField.setEditable(false);
    }
    
    private void addToProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (projectChooser == null) {
            projectChooser = new ProjectChooser(WindowManager.getDefault().getMainWindow());
        }
        ProjectChooserDialog dialog = projectChooser.getDialog();
        Project selected = dialog.select(addToProject, addToProjectButton);
        if (!dialog.cancelled()) {
            addToProject = selected;
            if (addToProject == null) {
                addToProjectField.setText("");
            } else {
                addToProjectField.setText(addToProject.getDescription());
            }
        }
    }

    private void supergoalButtonActionPerformed(ActionEvent evt) {
        GoalChooserDialog goalChooserDialog = new GoalChooserDialog(true);
        Result result = goalChooserDialog.showDialog();
        if (result == Result.SELECT) {
            supergoal = goalChooserDialog.getSelected();
            superGoalText.setText(supergoal == null ? "" : supergoal.getDescr());
        }
    }

    @Override
    public String toString() {
        return null == thought ? "" : thought.toString();
    }

    private static String getMsg(String key) {
        return NbBundle.getMessage(CLASS, key);
    }

    private static String getMsg(String key, int n) {
        return NbBundle.getMessage(CLASS, key, n);
    }

    private static String getMsg(String key, String p1, String p2) {
        return NbBundle.getMessage(CLASS, key, p1, p2);
    }
    
    private final static Logger LOG = Logger.getLogger("tr.view.process");
    private final static Class CLASS = ProcessPanel.class;
    private final static boolean DEBUG_MIG_LAYOUT = false;
    private final static String LBL_TOPIC = getMsg("LBL_Topic");
    private final static String LBL_ACTIONABLE = getMsg("LBL_Actionable");
    private final static String LBL_YES = getMsg("LBL_Yes");
    private final static String LBL_NO = getMsg("LBL_No");
    private final static String LBL_NOT_ACTIONABLE = getMsg("LBL_NotActionable");
    private final static String LBL_DELETE = getMsg("LBL_Delete");
    private final static String LBL_SOMEDAY = getMsg("LBL_SomedayMaybe");
    private final static String LBL_REFERENCE = getMsg("LBL_Reference");
    private final static String LBL_TICKLE_DATE = getMsg("LBL_TickleDate");
    private final static String LBL_GOAL = getMsg("LBL_Goal");
    private final static String LBL_GOAL_LEVEL = getMsg("LBL_GoalLevel");
    private final static String LBL_SUPERGOAL = getMsg("LBL_SuperGoal");
    private final static String LBL_SUCCESS = getMsg("LBL_SuccessfulOutcome");
    private final static String LBL_NEXT_ACTION = getMsg("LBL_NextAction");
    private final static String LBL_DESCRIPTION = getMsg("LBL_Description");
    private final static String LBL_CONTEXT = getMsg("LBL_Context");
    private final static String LBL_TIME = getMsg("LBL_Time");
    private final static String LBL_ENERGY = getMsg("LBL_Energy");
    private final static String LBL_PRIORITY = getMsg("LBL_Priority");
    private final static String LBL_STATUS = getMsg("LBL_Status");
    private final static String LBL_DUE_DATE = getMsg("LBL_DueDate");
    private final static String LBL_START_DATE = getMsg("LBL_StartDate");
    private final static String LBL_DURATION = getMsg("LBL_Duration");
    private final static String LBL_RECURRENCE = getMsg("LBL_Recurrence");
    private final static String LBL_CREATE = getMsg("LBL_Create");
    private final static String LBL_MODIFY = getMsg("LBL_Modify");
    private final static String LBL_DELEGATE = getMsg("LBL_Delegate");
    private final static String LBL_EMAIL = getMsg("LBL_Email");
    private final static String LBL_FOLLOWUP = getMsg("LBL_Followup");
    private final static String LBL_PROJECT = getMsg("LBL_Project");
    private final static String LBL_ADD_TO_PROJECT = getMsg("LBL_AddToProject");
    private final static String LBL_NEW_PROJECT = getMsg("LBL_NewProject");
    private final static String LBL_DONE = getMsg("LBL_Done");
    private final static String LBL_CREATED = getMsg("LBL_Created");
    private final static String LBL_NOTES = getMsg("LBL_Notes");
    private final static Dimension SIZE_PROJECT_BUTTON = new Dimension(20, 20);
    private final static Dimension SIZE_NOTES_EDITOR = new Dimension(100, 62);
    private final static TRLabel FILL_LABEL_1 = new TRLabel("");
    private final static TRLabel FILL_LABEL_2 = new TRLabel("");
    private final static TRLabel FILL_LABEL_3 = new TRLabel("");
    private final static TRLabel FILL_LABEL_4 = new TRLabel("");
    private final Data data;
    private final ProcessNodeProvider processNodeProvider;
    private final ProcessNode processNode;
    private final ProcessNode processNodeDelegationResponse;
    
    private final Project projects;
    private final Project singleActions;
    private final Manager<Thought> thoughtManager;
    private final Manager<Information> referenceManager;
    private final Manager<Future> somedayManager;
    private Thought thought;
    private Action action;
    private Project addToProject;
    private Project recurrenceProject;
    private ProjectChooser projectChooser;
    private TRLabel thoughtLabel;
    private TRTextField thoughtText;
    private TRLabel topicLabel;
    private JComboBox<Topic> topicCombo;
    private ButtonGroup actionableButtonGroup;
    private TRRadioButton actionableYesRadioButton;
    private TRRadioButton actionableNoRadioButton;
    // Not Actionable
    private ButtonGroup notActionableButtonGroup;
    private TRRadioButton deleteRadioButton;
    private TRRadioButton somedayRadioButton;
    private TRRadioButton referenceRadioButton;
    private TRLabel tickleDateLabel;
    private DateField tickleDateField;
    private TRRadioButton goalRadioButton;
    private TRLabel goalLevelLabel;
    private LevelsComboBox goalLevelCombo;
    private TRLabel superGoalLabel;
    private TRButton supergoalButton;
    private TRTextField superGoalText;
    private TRLabel notesLabel;
    private GoalCtrl supergoal;
    private NotesEditorPanel notesField;
    private TRLabel notesLabelNA;
    private NotesEditorPanel notesFieldNA;
    // Actionable
    private TRLabel actionableLabel;
    private TRLabel successLabel;
    private TRLabel actionLabel;
    private TRLabel projectLabel;
    private MTextArea successText;
    private TRLabel descriptionLabel;
    private TRTextField descrField;
    private TRLabel contextLabel;
    private JComboBox<Context> contextCombo;
    private TRLabel timeLabel;
    private JComboBox<Value> timeCombo;
    private TRLabel energyLabel;
    private JComboBox<Value> energyCombo;
    private TRLabel priorityLabel;
    private JComboBox<Value> priorityCombo;
    // Status
    private JComboBox<StatusEnum> statusCombo;
    private TRLabel statusLabel;
    private TRLabel dueDateLabel;
    private DateField dueDateField;
    private TRLabel startDateLabel;
    private DateField startDateField;
    // Scheduled
    private DateField scheduledDateField;
    private HourSpinner scheduledHourSpinner;
    private MinuteSpinner scheduledMinuteSpinner;
    private TRLabel durationLabel;
    private HourSpinner durationHourSpinner;
    private MinuteSpinner durationMinuteSpinner;
    private TRButton recurrenceButton;
    private TRLabel recurrenceLabel;
    // Delegated
    private TRLabel delegatedToLabel;
    private ActorsComboBox delegateCombo;
    private TRButton emailButton;
    private TRLabel followupLabel;
    private DateField followupField;
    // done and created
    private TRCheckBox doneCheckBox;
    private DateField doneDateField;
    private TRLabel createdDateLabel;
    private DateField createdDateField;
    // project
    private TRCheckBox newProjectCheck;
    private TRTextField newProjectField;
    private TRCheckBox addToProjectCheck;
    private TRButton addToProjectButton;
    private TRTextField addToProjectField;
    
    // panels
    private JPanel panelChoice;
    private JPanel panelNotActionable;
    private JPanel panelActionable;
    private JPanel panelAction;
    private JPanel panelStatus;
    

    // New for delegation response change
    private boolean delegationResponse;
    private JPanel panelMain;
    private JPanel panelThought;
    private JPanel panelNormal;
    private JPanel panelDelegation;    
    private ActionPanel delegationResponseActionPanel;
    private ActionPanel delegationOriginalActionPanel;        
    private Action delegationResponseAction;
    private Action delegationOriginalAction;
    private Action delegationOriginalActionCopy;    
    private TRTextField delegationProjectPath;
}

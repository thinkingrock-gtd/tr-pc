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
package au.com.trgtd.tr.view.actn;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.email.prefs.EmailPrefs;
import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.runtime.Email;
import tr.model.util.delegation.DelegationData;
import au.com.trgtd.tr.swing.*;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.document.LazyDocumentListener;
import au.com.trgtd.tr.swing.mig.MTextArea;
import au.com.trgtd.tr.swing.time.HourSpinner;
import au.com.trgtd.tr.swing.time.MinuteSpinner;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.actn.recurrence.NewRecurrenceWizard;
import au.com.trgtd.tr.view.actn.recurrence.modify.ModifyRecurrenceWizard;
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
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.*;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.util.delegation.DelegationUtils;
import tr.model.util.delegation.SMTPSender;

/**
 * Action panel.
 *
 * @author Jeremy Moore (jeremy@thinkingrock.com.au)
 */
public final class ActionPanel extends JPanel implements Observer {

    /**
     * Constructs a new default instance.
     */
    public ActionPanel() {
        super(new BorderLayout());
        initPanel();
        initModel(null);
        initWidgetListeners();
        addExternalListeners();
    }

    private Data getData() {
        return DataLookup.instance().lookup(Data.class);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        addWidgetListeners();
    }

    @Override
    public void removeNotify() {
        removeWidgetListeners();
        super.removeNotify();
    }

    /**
     * Start editing the current action if possible.
     */
    public void edit() {
        descriptionText.requestFocusInWindow();
    }

    /**
     * Gets the initialized data model.
     * @return The current action or null if there is none.
     */
    public Action getModel() {
        return action;
    }

    /**
     * Initializes the action data model and view.
     * @param action The action data model.
     */
    public synchronized void initModel(Action action) {
        updating = true;
        
//      removeWidgetListeners();
        removeModelListeners();
        
        this.action = action;
        storeActionStatus();
        
        initView();
        
        addModelListeners();
//      addWidgetListeners();        
        
        updating = false;
    }

    /*
     * Initialises the view form field values for the data model.
     */
    private void initView() {
//      updating = true;

        if (action == null) {
            descriptionText.setText("");
            thoughtText.setText("");
            successText.setText("");
            topicCombo.setSelectedItem(null);
            contextCombo.setSelectedItem(null);
            delegateCombo.setSelectedItem(null);
            statusCombo.setSelectedItem(null);
            followupField.setDate(null);
            scheduledDateField.setDate(null);
            scheduledHourSpinner.setVal(0);
            scheduledMinuteSpinner.setVal(0);
            durationHourSpinner.setVal(0);
            durationMinuteSpinner.setVal(0);
            timeCombo.setSelectedItem(null);
            energyCombo.setSelectedItem(null);
            priorityCombo.setSelectedItem(null);
            notesViewLabel.setNotable(null);
            notesViewField.setNotable(null);
            createdDateField.setDate(null);
            startDateField.setDate(null);
            dueDateField.setDate(null);
            doneCheckBox.setSelected(false);
            doneDateField.setDate(null);
            setEnabled(false);
        } else {
            descriptionText.setText(action.getDescription());
            Thought thought = action.getThought();
            thoughtText.setText((thought == null) ? "" : thought.getDescription());
            successText.setText(action.getSuccess());
            topicCombo.setSelectedItem(action.getTopic());
            contextCombo.setSelectedItem(action.getContext());
            statusCombo.setSelectedItem(null);
            if (action.isStateInactive()) {
                statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                setStatusInactive();
            } else if (action.isStateASAP()) {
                statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                setStatusDoASAP();
            }
            if (action.isStateDelegated()) {
                statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                ActionStateDelegated state = (ActionStateDelegated) action.getState();
                followupField.setDate(state.getDate());
                if (state.hasDelegateValue()) {
                    delegateCombo.setSelectedItem(ActorUtils.instance().getActor(state.getActorID()));
                } else if (state.hasToValue()) {
                    delegateCombo.setSelectedItem(state.getTo());
                } else {
                    delegateCombo.selectDefault();
                }
                setStatusDelegated();
            } else { // not delegated
                followupField.setDate(null);
                delegateCombo.setSelectedItem(null);
            }

            if (action.isStateScheduled()) {
                statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                scheduledDateField.setDate(state.getDate());
                if (state.getDate() == null) {
                    setDefaultScheduledFields();
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(state.getDate());
                    scheduledHourSpinner.setVal(c.get(Calendar.HOUR_OF_DAY));
                    scheduledMinuteSpinner.setVal(c.get(Calendar.MINUTE));
                    durationHourSpinner.setVal(state.getDurationHours());
                    durationMinuteSpinner.setVal(state.getDurationMinutes());
                }
                setStatusScheduled();
            } else { // not scheduled
                scheduledDateField.setDate(null);
                setDefaultScheduledFields();
            }

            timeCombo.setSelectedItem(action.getTime());
            energyCombo.setSelectedItem(action.getEnergy());
            priorityCombo.setSelectedItem(action.getPriority());
            notesViewLabel.setNotable(action);
            notesViewField.setNotable(action);
            createdDateField.setDate(action.getCreated());
            startDateField.setDate(action.getStartDate());
            dueDateField.setDate(action.getDueDate());
            doneCheckBox.setSelected(action.isDone());
            doneDateField.setDate(action.getDoneDate());
            setEnabled(true);
        }

        // force scroll to top
        SwingUtilities.invokeLater(() -> {
            scrollpane.getViewport().setViewPosition(new Point(0, 0));
        });

//      updating = false;
    }

    private void setDefaultScheduledFields() {
        scheduledHourSpinner.setValue(ActionPrefs.getSchdTimeHr());
        scheduledMinuteSpinner.setValue(ActionPrefs.getSchdTimeMn());
        durationHourSpinner.setValue(ActionPrefs.getSchdDurHrs());
        durationMinuteSpinner.setValue(ActionPrefs.getSchdDurMns());
    }

    /**
     * Override to enable/disable fields.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        descriptionLabel.setEnabled(enabled);
        descriptionText.setEnabled(enabled);
        thoughtLabel.setEnabled(enabled);
        successLabel.setEnabled(enabled);
        successText.setEnabled(enabled);
        topicLabel.setEnabled(enabled);
        topicCombo.setEnabled(enabled);
        contextLabel.setEnabled(enabled);
        contextCombo.setEnabled(enabled);
        statusLabel.setEnabled(enabled);
        statusCombo.setEnabled(enabled);
        delegatedToLabel.setEnabled(enabled);
        delegateCombo.setEnabled(enabled);
        emailButton.setEnabled(enabled);
        followupLabel.setEnabled(enabled);
        followupField.setEnabled(enabled);
        scheduledLabel.setEnabled(enabled);
        scheduledDateField.setEnabled(enabled);
        scheduledHourSpinner.setEnabled(enabled);
        scheduledMinuteSpinner.setEnabled(enabled);
        durationLabel.setEnabled(enabled);
        durationHourSpinner.setEnabled(enabled);
        durationMinuteSpinner.setEnabled(enabled);

        if (enabled && action.isStateScheduled()) {
            recurrenceLabel.setEnabled(scheduledDateField.getDate() != null);
            recurrenceButton.setEnabled(scheduledDateField.getDate() != null);
            ActionStateScheduled state = (ActionStateScheduled) action.getState();
            if (state.getRecurrence() == null) {
                recurrenceButton.setText(getMsg("recurrence.create"));
            } else {
                recurrenceButton.setText(getMsg("recurrence.modify"));
            }
        }

        notesViewLabel.setEnabled(enabled);
        notesViewField.setEnabled(enabled);
        createdDateLabel.setEnabled(enabled);
        createdDateField.setEnabled(enabled && ActionPrefs.isEditCreateDate());
        startDateLabel.setEnabled(enabled);
        startDateField.setEnabled(enabled);
        dueDateLabel.setEnabled(enabled);
        dueDateField.setEnabled(enabled);
        doneCheckBox.setEnabled(enabled);
        doneDateField.setEnabled(enabled && action.canSetDone(true));
        timeLabel.setEnabled(enabled);
        timeCombo.setEnabled(enabled);
        energyLabel.setEnabled(enabled);
        energyCombo.setEnabled(enabled);
        priorityLabel.setEnabled(enabled);
        priorityCombo.setEnabled(enabled);
    }

    public void focus() {
        descriptionText.requestFocusInWindow();
    }

    public void refresh() {
        setEnabled(action != null);
    }

    private void addToNotes(String text) {
        if (action == null) {
            return;
        }
        String notes = action.getNotes();
        if (notes.contains(text)) {
            return;
        }
        if (notes.length() == 0) {
            action.setNotes(text);
        } else {
            action.setNotes(notes + "\n" + text);
        }
    }

    // Store current action state for restore if user changes and changes back.
    private void storeActionStatus() {
        stateDoASAP = null;
        stateInactive = null;
        stateSchedule = null;
        stateDelegate = null;
        if (action == null) {
            return;
        }
        final ActionState state = action.getState();
        switch (state.getType()) {
            case INACTIVE:
                stateInactive = (ActionStateInactive)state;
                break;
            case DOASAP:
                stateDoASAP = (ActionStateASAP)state;
                break;
            case DELEGATED:
                stateDelegate = (ActionStateDelegated)state;
                break;
            case SCHEDULED:
                stateSchedule = (ActionStateScheduled)state;
                break;
        }
    }

//  private void recurrenceActionPerformed(ActionEvent evt) {
    private void recurrenceActionPerformed() {
        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
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
            recurrenceButton.setText(getMsg("recurrence.create"));
        } else {
            recurrenceButton.setText(getMsg("recurrence.modify"));
        }

    }

    private void changedDurationMinutes() {
        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
        }

        stateSchedule = (ActionStateScheduled) action.getState();
        if (stateSchedule.getDate() == null) {
            durationMinuteSpinner.setVal(0);
        } else {
            stateSchedule.setDurationMins(durationMinuteSpinner.getVal());
        }

    }

    private void changedDurationHours() {
        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
        }

        stateSchedule = (ActionStateScheduled) action.getState();
        if (stateSchedule.getDate() == null) {
            durationHourSpinner.setVal(0);
        } else {
            stateSchedule.setDurationHours(durationHourSpinner.getVal());
        }
    }

    private void changedScheduledDate(Date date) {

        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
        }

        stateSchedule = (ActionStateScheduled) action.getState();

        if (date == null) {
            stateSchedule.setDate(null);
            scheduledHourSpinner.setVal(0);
            scheduledMinuteSpinner.setVal(0);
            durationHourSpinner.setVal(0);
            durationMinuteSpinner.setVal(0);
            recurrenceLabel.setEnabled(false);
            recurrenceButton.setEnabled(false);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, scheduledHourSpinner.getVal());
            cal.set(Calendar.MINUTE, scheduledMinuteSpinner.getVal());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            stateSchedule.setDate(cal.getTime());
            scheduledHourSpinner.setEnabled(true);
            scheduledMinuteSpinner.setEnabled(true);
            durationHourSpinner.setEnabled(true);
            durationMinuteSpinner.setEnabled(true);
            durationLabel.setEnabled(true);
            recurrenceLabel.setEnabled(true);
            recurrenceButton.setEnabled(true);
        }

    }

    private void changedFollowupDate(Date date) {
        if (updating || action == null) {
            return;
        }

        if (action.isStateDelegated()) {
            stateDelegate = (ActionStateDelegated) action.getState();
            stateDelegate.setDate(date);
        }

    }

    private void changedPriority() {
        if (updating || action == null) {
            return;
        }

        action.setPriority((Value) priorityCombo.getSelectedItem());
    }

    private void changedTime() {
        if (updating || action == null) {
            return;
        }

        action.setTime((Value) timeCombo.getSelectedItem());
    }

    private void changedEnergy() {
        if (updating || action == null) {
            return;
        }

        action.setEnergy((Value) energyCombo.getSelectedItem());
    }

    private void changedStatus() {
        if (updating || action == null) {
            return;
        }

        StatusEnum status = (StatusEnum) statusCombo.getSelectedItem();
        if (status == null) {
            if (action == null) {
                status = StatusEnum.DO_ASAP;
            } else {
                switch (action.getState().getType()) {
                    case INACTIVE:
                        status = StatusEnum.INACTIVE;
                        break;
                    case DOASAP:
                        status = StatusEnum.DO_ASAP;
                        break;
                    case SCHEDULED:
                        status = StatusEnum.SCHEDULED;
                        break;
                    case DELEGATED:
                        status = StatusEnum.DELEGATED;
                        break;
                    default:
                        status = StatusEnum.DO_ASAP;
                }
            }
        }
        switch (status) {
            case INACTIVE:
                action.setState(getStateInactive());
                setStatusInactive();
                break;
            case DO_ASAP:
                action.setState(getStateASAP());
                setStatusDoASAP();
                break;
            case SCHEDULED:
                action.setState(getStateScheduled());
                setStatusScheduled();
                break;
            case DELEGATED:
                action.setState(getStateDelegated());
                setStatusDelegated();
                break;
        }

        setEnabled(true);
    }

    private void changedDelegate() {
        if (updating || action == null || stateDelegate == null) {
            return;
        }
        Object selected = delegateCombo.getSelectedItem();
        if (selected == null) {
            stateDelegate.setTo("");
            return;
        }
        if (selected instanceof String string) {
            stateDelegate.setTo(string);
            return;
        }
        if (selected instanceof Actor actor) {
            stateDelegate.setActorID(actor.getID());
        }
    }

    private void emailActionPerformed() {
        if (action == null || stateDelegate == null) {
            return;
        }
        String to = stateDelegate.getEmail();
        if (to == null || to.trim().length() == 0) {
            return;
        }
        if (Utilities.getOperatingSystem() == Utilities.OS_LINUX && !to.contains("@")) {
            JOptionPane.showMessageDialog(null, getMsg("ERR_EmailRequired"), "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String subject = action.getDescription();
        String body = action.getNotes();

        ////        
        boolean team = stateDelegate.isTeamMember();        
        if (team) {
            DelegationData dd = DelegationUtils.getSendDelegateData(action);
            if (dd != null) {
                body += "\n\n" + dd.serialize();            
            }            
        }
        
        sendEmail(to, subject, body, team);                
        ///
        
        Date today = Calendar.getInstance().getTime();
        
        if (ActionPrefs.isNoteDelegateEmail()) {
            addToNotes(getMsg("MSG_EmailSent", Constants.DATE_TIME_FORMAT_FIXED.format(today), stateDelegate.getTo()));
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
    
    private void changedScheduledHour() {
        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
        }

        stateSchedule = (ActionStateScheduled) action.getState();

        if (stateSchedule.getDate() == null) {
            scheduledHourSpinner.setVal(0);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(stateSchedule.getDate());
            c.set(Calendar.HOUR_OF_DAY, scheduledHourSpinner.getVal());
            c.set(Calendar.SECOND, 0);

            stateSchedule.setDate(c.getTime());
            scheduledDateField.setDate(c.getTime());
        }

    }

    private void changedScheduledMinute() {
        if (updating || action == null) {
            return;
        }

        if (!action.isStateScheduled()) {
            return;
        }

        stateSchedule = (ActionStateScheduled) action.getState();

        if (stateSchedule.getDate() == null) {
            scheduledMinuteSpinner.setVal(0);
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(stateSchedule.getDate());
            c.set(Calendar.MINUTE, scheduledMinuteSpinner.getVal());
            c.set(Calendar.SECOND, 0);
            stateSchedule.setDate(c.getTime());
            scheduledDateField.setDate(c.getTime());
        }
    }

    private void changedDoneDate(Date date) {
        if (updating || action == null) {
            return;
        }
//      action.setDoneDate(doneDateField.getDate());
        action.setDoneDate(date);
    }

    private void changedDone() {
        if (updating || action == null) {
            return;
        }
        if (action.canSetDone(doneCheckBox.isSelected())) {
            action.setDone(doneCheckBox.isSelected());
            doneDateField.setDate(action.getDoneDate());
            doneDateField.setEnabled(action.canSetDone(true));
            delegateCombo.setIncludeInactive(doneCheckBox.isSelected());
        }
    }

    private void changedCreatedDate(Date date) {
        if (updating || action == null) {
            return;
        }
        action.setCreated(date);
    }

    private void changedStartDate(Date date) {
        if (updating || action == null) {
            return;
        }
        action.setStartDate(date);
    }

    private void changedDueDate(Date date) {
        if (updating || action == null) {
            return;
        }
//      action.setDueDate(dueDateField.getDate());
        action.setDueDate(date);
    }

    private void changedContext(ActionEvent evt) {
        if (updating || action == null) {
            return;
        }
        TRComboBox combo = (TRComboBox) evt.getSource();
        action.setContext((Context) combo.getSelectedItem());
    }

    private void changedTopic(ActionEvent evt) {
        if (updating || action == null) {
            return;
        }
        TRComboBox combo = (TRComboBox) evt.getSource();
        action.setTopic((Topic) combo.getSelectedItem());
    }

    private void changedPrefs(PreferenceChangeEvent e) {
        String key = e.getKey();
        if (key.equals(ActionPrefs.KEY_EDIT_CREATE_DATE)) {
            createdDateField.setEnabled(action != null && ActionPrefs.isEditCreateDate());
            return;
        }
        if (key.equals(ActionPrefs.KEY_NOTE_DELEGATE_EMAIL)) {
            // no need to do anything
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_DUE)) {
            changedStatus();
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_START)) {
            changedStatus();
            return;
        }
        if (key.equals(ActionPrefs.KEY_SHOW_SUCCESS)) {
            removeAll();
            add(createPanel(), BorderLayout.CENTER);
            if (isShowing()) {
                revalidate();
                repaint();
            }
        }
    }

    // in case one or more criteria have change use.
    private void changedCriteria() {
        removeAll();
        add(createPanel(), BorderLayout.CENTER);
        if (isShowing()) {
            revalidate();
            repaint();
        }
    }

    private void descriptionTextFocusGained(java.awt.event.FocusEvent evt) {
        if (descriptionText.getText().equals(DEFAULT_DESCRIPTION)) {
            descriptionText.setSelectionStart(0);
            descriptionText.setSelectionEnd(DEFAULT_DESCRIPTION.length());
        }
    }

    private ActionStateInactive getStateInactive() {
        if (stateInactive == null) {
            if (action.isStateInactive()) {
                stateInactive = (ActionStateInactive) action.getState();
            } else {
                stateInactive = new ActionStateInactive();
            }
        }
        return stateInactive;
    }

    private ActionStateASAP getStateASAP() {
        if (stateDoASAP == null) {
            if (action.isStateASAP()) {
                stateDoASAP = (ActionStateASAP) action.getState();
            } else {
                stateDoASAP = new ActionStateASAP();
            }
        }
        return stateDoASAP;
    }

    private ActionStateDelegated getStateDelegated() {
        if (stateDelegate == null) {
            if (action.isStateDelegated()) {
                stateDelegate = (ActionStateDelegated) action.getState();
            } else {
                stateDelegate = new ActionStateDelegated();
            }
        }
        return stateDelegate;
    }

    private ActionStateScheduled getStateScheduled() {
        if (stateSchedule == null) {
            if (action.isStateScheduled()) {
                stateSchedule = (ActionStateScheduled) action.getState();
            } else {
                stateSchedule = new ActionStateScheduled();
            }
        }
        return stateSchedule;
    }

    private void changedDescription() {
        if (updating || action == null) {
            return;
        }
        action.setDescription(descriptionText.getText());
    }

    private void changedSuccess() {
        if (updating || action == null) {
            return;
        }
        action.setSuccess(successText.getText());
    }

    private static String getMsg(String key) {
        return NbBundle.getMessage(ActionPanel.class, key);
    }

    private static String getMsg(String key, String p1, String p2) {
        return NbBundle.getMessage(ActionPanel.class, key, p1, p2);
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

    private final PropertyChangeListener descrListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (descriptionText.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!descriptionText.getText().equals(action.getDescription())) {
                descriptionText.setText(action.getDescription());
            }
        }
    };

    private final PropertyChangeListener topicListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (topicCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(action.getTopic(), topicCombo.getSelectedItem())) {
                topicCombo.setSelectedItem(action.getTopic());
            }
        }
    };

    private final PropertyChangeListener contextListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (contextCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(action.getContext(), contextCombo.getSelectedItem())) {
                contextCombo.setSelectedItem(action.getContext());
            }
        }
    };

    private final PropertyChangeListener doneListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (doneCheckBox.isSelected() != action.isDone()) {
                doneCheckBox.setSelected(action.isDone());
                doneDateField.setDate(action.getDoneDate());
                doneDateField.setEnabled(action.canSetDone(true));
            }
        }
    };

    private final PropertyChangeListener timeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (timeCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(action.getTime(), timeCombo.getSelectedItem())) {
                timeCombo.setSelectedItem(action.getTime());
            }
        }
    };

    private final PropertyChangeListener energyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (energyCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(action.getEnergy(), energyCombo.getSelectedItem())) {
                energyCombo.setSelectedItem(action.getEnergy());
            }
        }
    };

    private final PropertyChangeListener priorityListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (priorityCombo.hasFocus()) {
                return; // assume caused by user input to this panel
            }
            if (!Utils.equal(action.getPriority(), priorityCombo.getSelectedItem())) {
                priorityCombo.setSelectedItem(action.getPriority());
            }
        }
    };
    
    private final PropertyChangeListener stateListener = new PropertyChangeListener() {
        @Override
        public synchronized void propertyChange(PropertyChangeEvent evt) {
            ActionState s = (ActionState) evt.getNewValue();
            switch (s.getType()) {
                case INACTIVE: {
                    statusCombo.setSelectedItem(StatusEnum.INACTIVE);
                    setStatusInactive();
                    break;
                }
                case DOASAP: {
                    statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
                    setStatusDoASAP();
                    break;
                }
                case DELEGATED: {
                    ActionStateDelegated sd = (ActionStateDelegated) evt.getNewValue();
                    statusCombo.setSelectedItem(StatusEnum.DELEGATED);
                    if (sd.hasDelegateValue()) {
                        delegateCombo.setSelectedItem(ActorUtils.instance().getActor(sd.getActorID()));
                    } else if (sd.hasToValue()) {
                        delegateCombo.setSelectedItem(sd.getTo());
                    } else {
                        delegateCombo.selectDefault();
                    }
                    followupField.setDate(sd.getDate());
                    setStatusDelegated();
                    break;
                }
                case SCHEDULED: {
                    ActionStateScheduled ss = (ActionStateScheduled) evt.getNewValue();
                    scheduledDateField.removePropertyChangeListener("value", propertyListenerScheduledDate);
                    scheduledHourSpinner.removeChangeListener(changeListenerScheduledHour);
                    scheduledMinuteSpinner.removeChangeListener(changeListenerScheduledMinute);
                    durationHourSpinner.removeChangeListener(changeListenerDurationHour);
                    durationMinuteSpinner.removeChangeListener(changeListenerDurationMinute);
                    if (ss.getDate() == null) {
                        scheduledDateField.setDate(null);
                        setDefaultScheduledFields();
                    } else {
                        Calendar c = Calendar.getInstance();
                        c.setTime(ss.getDate());
                        scheduledHourSpinner.setVal(c.get(Calendar.HOUR_OF_DAY));
                        scheduledMinuteSpinner.setVal(c.get(Calendar.MINUTE));
                        scheduledDateField.setDate(ss.getDate());
                        durationHourSpinner.setVal(ss.getDurationHours());
                        durationMinuteSpinner.setVal(ss.getDurationMinutes());
                    }
                    statusCombo.setSelectedItem(StatusEnum.SCHEDULED);
                    setStatusScheduled();
                    scheduledDateField.addPropertyChangeListener("value", propertyListenerScheduledDate);
                    scheduledHourSpinner.addChangeListener(changeListenerScheduledHour);
                    scheduledMinuteSpinner.addChangeListener(changeListenerScheduledMinute);
                    durationHourSpinner.addChangeListener(changeListenerDurationHour);
                    durationMinuteSpinner.addChangeListener(changeListenerDurationMinute);
                    break;
                }
            }
        }
    };

    /**
     * Handle observed changes to the action.
     */
    @Override
    public void update(Observable obs, Object arg) {
        if (action == null) {
            return;
        }
        EventQueue.invokeLater(() -> {
            // for postpone action date
            dueDateField.setDate(action.getDueDate());
            startDateField.setDate(action.getStartDate());
            if (action.isStateDelegated()) {
                ActionStateDelegated s = (ActionStateDelegated) action.getState();
                followupField.setDate(s.getDate());
            } else if (action.isStateScheduled()) {
                // handle possible recurrence change to scheduled state
////                    if (!notesField.getText().equals(action.getNotes())) {
////                        notesField.setText(action.getNotes());
////                    }
//                    if (!notesViewField.getText().equals(action.getNotes())) {
//                        notesViewField.setText(action.getNotes());
//                    }
if (!successText.getText().equals(action.getSuccess())) {
    successText.setText(action.getSuccess());
}
ActionStateScheduled ass = (ActionStateScheduled) action.getState();
if (ass.getDate() == null) {
    setDefaultScheduledFields();
} else {
    Calendar c = Calendar.getInstance();
    c.setTime(ass.getDate());
    scheduledHourSpinner.setVal(c.get(Calendar.HOUR_OF_DAY));
    scheduledMinuteSpinner.setVal(c.get(Calendar.MINUTE));
    durationHourSpinner.setVal(ass.getDurationHours());
    durationMinuteSpinner.setVal(ass.getDurationMinutes());
    scheduledDateField.setDate(ass.getDate());
}
            }
        });
    }

    private void addExternalListeners() {
        prefsChangeListener = (PreferenceChangeEvent evt) -> {
            changedPrefs(evt);
        };
        ActionPrefs.getPrefs().addPreferenceChangeListener(prefsChangeListener);

        criteriaObserver = (Observable obs, Object arg) -> {
            changedCriteria();
        };
        Data data = getData();
        if (data != null) {
            getData().getTimeCriterion().addObserver(criteriaObserver);
            getData().getEnergyCriterion().addObserver(criteriaObserver);
            getData().getPriorityCriterion().addObserver(criteriaObserver);
        }
    }

    private void initPanel() {
        scheduledLabel = new TRLabel();
        scheduledHourSpinner = new HourSpinner();
        scheduledMinuteSpinner = new MinuteSpinner();
        durationLabel = new TRLabel();
        durationHourSpinner = new HourSpinner();
        durationMinuteSpinner = new MinuteSpinner();
        recurrenceLabel = new TRLabel();
        recurrenceButton = new TRButton();
        scheduledDateField = new DateField();
        delegatedToLabel = new TRLabel();
        delegateCombo = new ActorsComboBox();
        emailButton = new TRButton();
        followupLabel = new TRLabel();
        followupField = new DateField();
        descriptionLabel = new TRLabel();
        descriptionText = new TRTextField();
        thoughtLabel = new TRLabel();
        thoughtText = new TRTextField();
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

        recurrenceLabel.setText(getMsg("LBL_Recurrence")); // NOI18N
        recurrenceButton.setText(NbBundle.getMessage(ActionPanel.class, "recurrence.create")); // NOI18N

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

        descriptionLabel.setText(getMsg("LBL_Description")); // NOI18N

        thoughtLabel.setText(getMsg("LBL_Thought")); // NOI18N
        thoughtText.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        thoughtText.setEnabled(false);

        successLabel.setText(NbBundle.getMessage(ActionPanel.class, "ActionPanel.success")); // NOI18N

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

        startDateLabel.setText(NbBundle.getMessage(ActionPanel.class, "PanelAction.start")); // NOI18N

        dueDateLabel.setText(NbBundle.getMessage(ActionPanel.class, "PanelAction.due")); // NOI18N

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
                panel = new JPanel(new MigLayout(getMIGCons(),
                        "2[]4[grow]4[]4[]2",
                        "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2"));
            } else {
                panel = new JPanel(new MigLayout(getMIGCons(),
                        "2[]4[grow]4[]4[]2",
                        "2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[grow]2"));
            }
        } else {
            if (ActionPrefs.isShowSuccess()) {
                panel = new JPanel(new MigLayout(getMIGCons(),
                        "2[]4[grow]4[]4[]2",
                        "2[top]2[top]2[top]2[top]2[center]2[top]2[center]2[top]2[center]2[top]2[center]2[top, grow]2"));
            } else {
                panel = new JPanel(new MigLayout(getMIGCons(),
                        "2[]4[grow]4[]4[]2",
                        "2[top]2[top]2[top]2[center]2[top]2[center]2[top]2[center]2[top]2[center]2[top, grow]2"));
            }
        }

        panel.setBackground(ViewUtils.COLOR_PANEL_BG);

        panel.add(descriptionLabel, "align right");
        panel.add(descriptionText, "span, growx, wrap");

        panel.add(thoughtLabel, "align right");
        panel.add(thoughtText, "span, growx, wrap");

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
        Data data = getData();
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

        panel.add(recurrenceLabel, "gapleft 4, split 2");
        panel.add(recurrenceButton, "gapleft 2");

        panel.revalidate();
        panel.repaint();

        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void initWidgetListeners() {
        docListenerDescription = new LazyDocumentListener() {
            @Override
            public void update() {
                changedDescription();
            }
        };
        docListenerSuccess = new LazyDocumentListener() {
            @Override
            public void update() {
                changedSuccess();
            }
        };
        focusAdapterDescription = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                descriptionTextFocusGained(evt);
            }
        };
        actionListenerTopic = (ActionEvent evt) -> {
            changedTopic(evt);
        };
        actionListenerContext = (ActionEvent evt) -> {
            changedContext(evt);
        };
        propertyListenerScheduledDate = (PropertyChangeEvent e) -> {
            changedScheduledDate((Date) e.getNewValue());
        };
        changeListenerScheduledHour = (ChangeEvent evt) -> {
            changedScheduledHour();
        };
        changeListenerScheduledMinute = (ChangeEvent evt) -> {
            changedScheduledMinute();
        };
        changeListenerDurationHour = (ChangeEvent evt) -> {
            changedDurationHours();
        };
        changeListenerDurationMinute = (ChangeEvent evt) -> {
            changedDurationMinutes();
        };
        actionListenerRecurrence = (ActionEvent evt) -> {
            recurrenceActionPerformed();
        };
        actionListenerDelegateCombo = (ActionEvent evt) -> {
            changedDelegate();
        };
        
        propertyListenerDelegate = (PropertyChangeEvent evt) -> {
            changedDelegate();
        };
        
        actionListenerEmail = (ActionEvent evt) -> {
            emailActionPerformed();
        };
        actionListenerStatusCombo = (ActionEvent evt) -> {
            changedStatus();
        };
        actionListenerPriorityCombo = (ActionEvent evt) -> {
            changedPriority();
        };
        actionListenerEnergyCombo = (ActionEvent evt) -> {
            changedEnergy();
        };
        actionListenerTimeCombo = (ActionEvent evt) -> {
            changedTime();
        };
        propertyListenerFollowupDate = (PropertyChangeEvent e) -> {
            changedFollowupDate((Date) e.getNewValue());
        };
        propertyListenerCreatedDate = (PropertyChangeEvent e) -> {
            changedCreatedDate((Date) e.getNewValue());
        };
        propertyListenerStartDate = (PropertyChangeEvent e) -> {
            changedStartDate((Date) e.getNewValue());
        };
        propertyListenerDueDate = (PropertyChangeEvent e) -> {
            changedDueDate((Date) e.getNewValue());
        };
        doneCheckBoxActionListener = (ActionEvent evt) -> {
            changedDone();
        };
        propertyListenerDoneDate = (PropertyChangeEvent e) -> {
            changedDoneDate((Date) e.getNewValue());
        };
    }

    private void addWidgetListeners() {
        descriptionText.getDocument().addDocumentListener(docListenerDescription);
        descriptionText.addFocusListener(focusAdapterDescription);
        successText.getDocument().addDocumentListener(docListenerSuccess);
        contextCombo.addActionListener(actionListenerContext);
        topicCombo.addActionListener(actionListenerTopic);
        scheduledDateField.addPropertyChangeListener("value", propertyListenerScheduledDate);
        scheduledHourSpinner.addChangeListener(changeListenerScheduledHour);
        scheduledMinuteSpinner.addChangeListener(changeListenerScheduledMinute);
        durationHourSpinner.addChangeListener(changeListenerDurationHour);
        durationMinuteSpinner.addChangeListener(changeListenerDurationMinute);
        recurrenceButton.addActionListener(actionListenerRecurrence);
        delegateCombo.addActionListener(actionListenerDelegateCombo);
        
        
        delegateCombo.addPropertyChangeListener("selected", propertyListenerDelegate);
        
        
        emailButton.addActionListener(actionListenerEmail);
        statusCombo.addActionListener(actionListenerStatusCombo);
        priorityCombo.addActionListener(actionListenerPriorityCombo);
        energyCombo.addActionListener(actionListenerEnergyCombo);
        timeCombo.addActionListener(actionListenerTimeCombo);
        followupField.addPropertyChangeListener("value", propertyListenerFollowupDate);
        createdDateField.addPropertyChangeListener("value", propertyListenerCreatedDate);
        startDateField.addPropertyChangeListener("value", propertyListenerStartDate);
        dueDateField.addPropertyChangeListener("value", propertyListenerDueDate);
        doneCheckBox.addActionListener(doneCheckBoxActionListener);
        doneDateField.addPropertyChangeListener("value", propertyListenerDoneDate);
    }

    private void removeWidgetListeners() {
        descriptionText.getDocument().removeDocumentListener(docListenerDescription);
        descriptionText.removeFocusListener(focusAdapterDescription);
        successText.getDocument().removeDocumentListener(docListenerSuccess);
        contextCombo.removeActionListener(actionListenerContext);
        topicCombo.removeActionListener(actionListenerTopic);
        statusCombo.removeActionListener(actionListenerStatusCombo);
        scheduledDateField.removePropertyChangeListener(propertyListenerScheduledDate);
        scheduledHourSpinner.removeChangeListener(changeListenerScheduledHour);
        scheduledMinuteSpinner.removeChangeListener(changeListenerScheduledMinute);
        durationHourSpinner.removeChangeListener(changeListenerDurationHour);
        durationMinuteSpinner.removeChangeListener(changeListenerDurationMinute);
        recurrenceButton.removeActionListener(actionListenerRecurrence);
        
        delegateCombo.removeActionListener(actionListenerDelegateCombo);
        delegateCombo.removePropertyChangeListener(propertyListenerDelegate);
        
        emailButton.removeActionListener(actionListenerEmail);
        followupField.removePropertyChangeListener(propertyListenerFollowupDate);
        timeCombo.removeActionListener(actionListenerTimeCombo);
        energyCombo.removeActionListener(actionListenerEnergyCombo);
        priorityCombo.removeActionListener(actionListenerPriorityCombo);
        createdDateField.removePropertyChangeListener(propertyListenerCreatedDate);
        startDateField.removePropertyChangeListener(propertyListenerStartDate);
        dueDateField.removePropertyChangeListener(propertyListenerDueDate);
        doneCheckBox.removeActionListener(doneCheckBoxActionListener);
        doneDateField.removePropertyChangeListener(propertyListenerDoneDate);
    }
    
    private void addModelListeners() {
        if (action != null) {
            action.addObserver(this);
            action.addPropertyChangeListener(Action.PROP_DESCR, descrListener);
            action.addPropertyChangeListener(Action.PROP_TOPIC, topicListener);
            action.addPropertyChangeListener(Action.PROP_CONTEXT, contextListener);
            action.addPropertyChangeListener(Action.PROP_DONE, doneListener);
            action.addPropertyChangeListener(Action.PROP_TIME, timeListener);
            action.addPropertyChangeListener(Action.PROP_ENERGY, energyListener);
            action.addPropertyChangeListener(Action.PROP_PRIORITY, priorityListener);
            action.addPropertyChangeListener(Action.PROP_STATE, stateListener);
        }
    }
    
    private void removeModelListeners() {
        if (action != null) {
            action.removeObserver(this);
            action.removePropertyChangeListener(Action.PROP_DESCR, descrListener);
            action.removePropertyChangeListener(Action.PROP_TOPIC, topicListener);
            action.removePropertyChangeListener(Action.PROP_CONTEXT, contextListener);
            action.removePropertyChangeListener(Action.PROP_DONE, doneListener);
            action.removePropertyChangeListener(Action.PROP_TIME, timeListener);
            action.removePropertyChangeListener(Action.PROP_ENERGY, energyListener);
            action.removePropertyChangeListener(Action.PROP_PRIORITY, priorityListener);
            action.removePropertyChangeListener(Action.PROP_STATE, stateListener);
        }
    }
    
    private String getMIGCons() {
        return DEBUG_MIG_LAYOUT ? "debug" : "";
    }

    private String getMIGCons(String c) {
        assert (c != null && c.trim().length() > 0);

        return DEBUG_MIG_LAYOUT ? "debug, " + c : c;
    }
    
    private static final boolean DEBUG_MIG_LAYOUT = false;
//  private static final Logger LOG = Logger.getLogger("tr.view.actn");
    private static final String DEFAULT_DESCRIPTION = getMsg("new.action");
    private static final TRLabel fillerLabel1 = new TRLabel("");
    private static final TRLabel fillerLabel2 = new TRLabel("");
    private static final TRLabel fillerLabel3 = new TRLabel("");
    private Action action;
    private ActionStateInactive stateInactive;
    private ActionStateASAP stateDoASAP;
    private ActionStateDelegated stateDelegate;
    private ActionStateScheduled stateSchedule;
    private ComboBoxModel<Topic> topicsModel;
    private ComboBoxModel<Context> contextsModel;
    private boolean updating;
    private DocumentListener docListenerDescription;
    private FocusAdapter focusAdapterDescription;
    private DocumentListener docListenerSuccess;
    private ActionListener actionListenerTopic;
    private ActionListener actionListenerContext;
    private ActionListener actionListenerRecurrence;
    private ActionListener actionListenerEmail;
    private ActionListener actionListenerStatusCombo;
    private ActionListener actionListenerPriorityCombo;
    private ActionListener actionListenerEnergyCombo;
    private ActionListener actionListenerTimeCombo;
    private ActionListener actionListenerDelegateCombo;
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
    
    private PropertyChangeListener propertyListenerDelegate;
    
    private Observer criteriaObserver;
    private PreferenceChangeListener prefsChangeListener;
    private TRLabel descriptionLabel;
    private TRTextField descriptionText;
    private TRLabel thoughtLabel;
    private TRTextField thoughtText;
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
    private TRButton recurrenceButton;
    private TRLabel recurrenceLabel;
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

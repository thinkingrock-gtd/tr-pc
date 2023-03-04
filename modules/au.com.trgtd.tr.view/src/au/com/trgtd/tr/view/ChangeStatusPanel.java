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
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.swing.time.HourSpinner;
import au.com.trgtd.tr.swing.time.MinuteSpinner;
import au.com.trgtd.tr.view.actors.ActorsComboBox;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.actor.Actor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Change action status panel.
 * 
 * @author Jeremy Moore
 */
public class ChangeStatusPanel extends JPanel {

    /** Creates new form TickleDatePanel */
    public ChangeStatusPanel(String msg) {
        this.msg = msg;
        initView();
    }

    private void initView() {
        setLayout(new MigLayout("insets 12px", "2[600]2", "2[]12[]2"));
        JLabel messageLabel = new JLabel(msg);
        add(messageLabel, "wrap");
        add(getPanelAction(), "wrap");
        statusCombo.setSelectedItem(StatusEnum.DO_ASAP);
        setStatusDoASAP();
    }

    private enum Rows {One, Two};

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
        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "span, split");
        panel.add(delegatedToLabel, "gapleft 4, span, split 5");
        panel.add(delegateCombo);
        panel.add(followupLabel, "");
        panel.add(followupDateField, "wrap");
        panel.revalidate();
        panel.repaint();
        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusDoASAP() {
        boolean focus = statusCombo.hasFocus();
        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "wrap");
        panel.revalidate();
        panel.repaint();
        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusInactive() {
        boolean focus = statusCombo.hasFocus();
        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "wrap");
        panel.revalidate();
        panel.repaint();
        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    private void setStatusScheduled() {
        boolean focus = statusCombo.hasFocus();
        JPanel panel = getStatusPanel(Rows.One);
        panel.add(statusCombo, "span, split 7");
        panel.add(scheduledDateField, "gapleft 2");
        panel.add(scheduledHourSpinner, "gapleft 0");
        panel.add(scheduledMinuteSpinner, "gapleft 0");
        panel.add(durationLabel, "gapleft 4");
        panel.add(durationHourSpinner, "gapleft 2");
        panel.add(durationMinuteSpinner, "gapleft 0, wrap");
        panel.revalidate();
        panel.repaint();
        if (focus) {
            statusCombo.requestFocusInWindow();
        }
    }

    enum StatusEnum {
        INACTIVE,
        DO_ASAP,
        SCHEDULED,
        DELEGATED;
        @Override
        public String toString() {
            switch (this) {
                case INACTIVE:
                    return STRING_INACTIVE;
                case DO_ASAP:
                    return STRING_DO_ASAP;
                case SCHEDULED:
                    return STRING_SCHEDULED;
                case DELEGATED:
                    return STRING_DELEGATED;
                default:
                    return "";
            }
        }
        private final static String STRING_INACTIVE = NbBundle.getMessage(ChangeStatusPanel.class, "ActionStateInactive");
        private final static String STRING_DO_ASAP = NbBundle.getMessage(ChangeStatusPanel.class, "ActionStateDoASAP");
        private final static String STRING_SCHEDULED = NbBundle.getMessage(ChangeStatusPanel.class, "ActionStateScheduled");
        private final static String STRING_DELEGATED = NbBundle.getMessage(ChangeStatusPanel.class, "ActionStateDelegated");
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
                return;
        }
    }

    private class StatusComboBoxModel extends DefaultComboBoxModel<StatusEnum> {
        private List<StatusEnum> states;
        public StatusComboBoxModel() {
            super();
            states = new ArrayList<>();
            states.add(StatusEnum.INACTIVE);
            states.add(StatusEnum.DO_ASAP);
            states.add(StatusEnum.SCHEDULED);
            states.add(StatusEnum.DELEGATED);
        }
        @Override
        public StatusEnum getElementAt(int index) {
            return states.get(index);
        }
        @Override
        public int getSize() {
            return states.size();
        }
    }

    private JPanel getPanelAction() {
        if (panelAction == null) {
            statusLabel = new TRLabel(LBL_STATUS);
            statusCombo = new TRComboBox<>(new StatusComboBoxModel());
            statusCombo.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    changedStatus();
                }
            });
            scheduledHourSpinner = new HourSpinner();
            scheduledMinuteSpinner = new MinuteSpinner();
            durationLabel = new TRLabel(LBL_DURATION);
            durationHourSpinner = new HourSpinner();
            durationMinuteSpinner = new MinuteSpinner();
            scheduledDateField = new DateField();

            delegatedToLabel = new TRLabel(LBL_DELEGATE);
            delegateCombo = new ActorsComboBox();
            delegateCombo.selectDefault();
            followupLabel = new TRLabel(LBL_FOLLOWUP);
            followupDateField = new DateField();

            scheduledHourSpinner.setVal(ActionPrefs.getSchdTimeHr());
            scheduledMinuteSpinner.setVal(ActionPrefs.getSchdTimeMn());
            durationHourSpinner.setVal(ActionPrefs.getSchdDurHrs());
            durationMinuteSpinner.setVal(ActionPrefs.getSchdDurMns());

            if (ViewUtils.isAquaLaF()) {
                panelAction = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2", "0[]2[]2[]2[]2[]2[grow]2[]2[]0"));
            } else {
                panelAction = new JPanel(new MigLayout(getMIGCons(), "2[]4[grow]4[]4[]2", "0[top]2[top]2[]2[top]2[]2[top,grow]2[]2[top]0"));
            }
            if (ViewUtils.isAquaLaF()) {
                panelAction.add(statusLabel, "align right top, gaptop 4");
                panelAction.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
            } else {
                panelAction.add(statusLabel, "align right top");
                panelAction.add(getStatusPanel(Rows.One), "align left, growx 100, wrap");
            }
        }
        return panelAction;
    }

    public ActionState getStatus() {
        StatusEnum status = (StatusEnum) statusCombo.getSelectedItem();
        switch (status) {
            case DELEGATED: {
                ActionStateDelegated stateDelegated = new ActionStateDelegated();
                Object delegate = delegateCombo.getSelectedItem();
                if (delegate instanceof String str) {
                    stateDelegated.setTo(str);
                } else if (delegate instanceof Actor actor) {
                    stateDelegated.setActorID(actor.getID());
                } else {
                    stateDelegated.setTo("");
                }
                stateDelegated.setDate(followupDateField.getDate());
                return stateDelegated;
            }
            case DO_ASAP: {
                return new ActionStateASAP();
            }
            case INACTIVE: {
                return new ActionStateInactive();
            }
            case SCHEDULED: {
                Calendar cal = Calendar.getInstance();
                cal.setTime(scheduledDateField.getDate());
                cal.set(Calendar.HOUR_OF_DAY, scheduledHourSpinner.getVal());
                cal.set(Calendar.MINUTE, scheduledMinuteSpinner.getVal());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                ActionStateScheduled stateScheduled = new ActionStateScheduled();
                stateScheduled.setDate(cal.getTime());
                stateScheduled.setDurationHours(durationHourSpinner.getVal());
                stateScheduled.setDurationMins(durationMinuteSpinner.getVal());
                return stateScheduled;
            }
            default: {
                return null;
            }
        }
    }

    private String getMIGCons() {
        return DEBUG_MIG_LAYOUT ? "debug" : "";
    }

    private String getMIGCons(String c) {
        assert (c != null && c.trim().length() > 0);

        return DEBUG_MIG_LAYOUT ? "debug, " + c : c;
    }

    private static String getMsg(String key) {
        return NbBundle.getMessage(ChangeStatusPanel.class, key);
    }

    private static final boolean DEBUG_MIG_LAYOUT = false;
    private static final String LBL_STATUS = getMsg("LBL_Status");
    private static final String LBL_DURATION = getMsg("LBL_Duration");
    private static final String LBL_DELEGATE = getMsg("LBL_Delegate");
    private static final String LBL_FOLLOWUP = getMsg("LBL_Followup");
    private final MigLayout statusLayoutA1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]0");
    private final MigLayout statusLayoutA2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[]2:2:2[]0");
    private final MigLayout statusLayoutM1 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]0");
    private final MigLayout statusLayoutM2 = new MigLayout(getMIGCons("insets 0px"), "0[grow]0", "0[top]2[top]0");
    private final String msg;
    private JPanel panelAction;
    private JPanel statusPanel;
    // Status
    private JComboBox<StatusEnum> statusCombo;
    private TRLabel statusLabel;
    // Scheduled
    private DateField scheduledDateField;
    private HourSpinner scheduledHourSpinner;
    private MinuteSpinner scheduledMinuteSpinner;
    private TRLabel durationLabel;
    private HourSpinner durationHourSpinner;
    private MinuteSpinner durationMinuteSpinner;
    // Delegated
    private TRLabel delegatedToLabel;
    private ActorsComboBox delegateCombo;
    private TRLabel followupLabel;
    private DateField followupDateField;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    }

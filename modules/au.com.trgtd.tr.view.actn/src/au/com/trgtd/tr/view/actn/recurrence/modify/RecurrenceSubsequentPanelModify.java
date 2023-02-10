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
package au.com.trgtd.tr.view.actn.recurrence.modify;

import au.com.trgtd.tr.appl.Constants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.Period;
import tr.model.action.PeriodDay;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodType;
import tr.model.action.PeriodWeek;
import tr.model.action.PeriodWeekday;
import tr.model.action.PeriodYear;
import tr.model.action.Recurrence;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox;

public final class RecurrenceSubsequentPanelModify extends JPanel {

    private Recurrence modRecurrence;
    private Date actionScheduledDate;
    private boolean basisChanged;
    private long counter;
    private Map<PeriodType, Period> mapPeriods;

    public RecurrenceSubsequentPanelModify() {
        initView();
    }

    public void setModel(Recurrence recurrence, Date actionScheduledDate, boolean basisChanged) {
        this.modRecurrence = recurrence;
        this.actionScheduledDate = actionScheduledDate;
        this.basisChanged = basisChanged;
        modRecurrence.setStartDate(actionScheduledDate);
        initPeriods();
        initPanel();
    }

    private void initPeriods() {
        setPeriod(new PeriodWeekday());
        setPeriod(new PeriodDay());
        setPeriod(new PeriodWeek());
        setPeriod(new PeriodMonth());
        setPeriod(new PeriodYear());
        setPeriod(modRecurrence.getPeriod());
    }

    private Period getPeriod(PeriodType type) {
        if (mapPeriods == null) {
            mapPeriods = new HashMap<>();
        }
        return mapPeriods.get(type);
    }

    private void setPeriod(Period period) {
        if (mapPeriods == null) {
            mapPeriods = new HashMap<>();
        }
        mapPeriods.put(period.getType(), period);
    }

    public void initPanel() {
        freqSpinner.setValue(modRecurrence.getFrequency());
        periodCombo.setSelectedItem(modRecurrence.getPeriod().getType());
        endNbrRadio.setSelected(modRecurrence.getEndNbr() != null);
        endNbrSpinner.setValue(modRecurrence.getEndNbr() == null ? 2 : modRecurrence.getEndNbr());
        endDateRadio.setSelected(modRecurrence.getEndDate() != null);
        endDateField.setDate(modRecurrence.getEndDate());
        endNeverRadio.setSelected(!endNbrRadio.isSelected() && !endDateRadio.isSelected());
        modRecurrence.setStartDate(actionScheduledDate);
        changeMessage.setText(getMsg("changing.recurrence.type.from.regular.to.subsequent"));
        String dte = Constants.DATE_FORMAT_FIXED.format(actionScheduledDate);
        deleteMessage.setText(getMsg("this.will.delete.actions.dated.after.action.date", dte));
        changeMessage.setVisible(basisChanged);
        deleteMessage.setVisible(basisChanged);
    }

    @Override
    public String getName() {
        return getMsg("change.subsequent.recurrence");
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        createLabel.setEnabled(enabled);
        freqSpinner.setEnabled(enabled);
        periodCombo.setEnabled(enabled);
        endLabel.setEnabled(enabled);
        endNeverRadio.setEnabled(enabled);
        endNbrRadio.setEnabled(enabled);
        endNbrSpinner.setEnabled(enabled && endNbrRadio.isSelected());
        endDateRadio.setEnabled(enabled);
        endDateField.setEnabled(enabled && endDateRadio.isSelected());
        deleteMessage.setEnabled(enabled);
    }

    private void setEndValues() {
        if (endNbrRadio.isSelected()) {
            modRecurrence.setEndNbr((Integer) endNbrSpinner.getValue());
            modRecurrence.setEndDate(null);
        } else if (endDateRadio.isSelected()) {
            modRecurrence.setEndNbr(null);
            modRecurrence.setEndDate(this.endDateField.getDate());
        }
        stateChange();
    }

    public void stateChange() {
        putClientProperty("state.change", Long.valueOf(++counter));
    }

    public boolean isValidForm() {
        return !endDateRadio.isSelected() || endDateField.getDate() != null;
    }

    private void initView() {
        createLabel = new JLabel(getMsg("create.subsequent.action.in"));
        freqSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));
        freqSpinner.setPreferredSize(new Dimension(50, 23));
        freqSpinner.addChangeListener((ChangeEvent evt) -> {
            frequencySpinnerStateChanged();
        });
        periodCombo = new PeriodTypeComboBox();
        periodCombo.addActionListener((ActionEvent evt) -> {
            periodComboBoxActionPerformed();
        });
        endLabel = new JLabel(getMsg("terminate"));
        endNeverRadio = new JRadioButton(getMsg("never"));
        endNeverRadio.addChangeListener((ChangeEvent evt) -> {
            endNeverRadioButtonStateChanged();
        });
        endNbrRadio = new JRadioButton(getMsg("after"));
        endNbrRadio.addActionListener((ActionEvent evt) -> {
            endNbrRadioButtonActionPerformed();
        });
        endNbrSpinner = new JSpinner(new SpinnerNumberModel(2, 2, null, 1));
        endNbrSpinner.addChangeListener((ChangeEvent evt) -> {
            endNbrSpinnerStateChanged();
        });

        endDateRadio = new JRadioButton(getMsg("after"));
        endDateRadio.addChangeListener((ChangeEvent evt) -> {
            endDateRadioButtonStateChanged();
        });
        endDateField = new DateField();
        endDateField.addPropertyChangeListener(this::endDateFieldPropertyChange);
        changeMessage = new JTextArea(getMsg("changing.recurrence.type.from.regular.to.subsequent"));
        changeMessage.setFont(changeMessage.getFont().deriveFont(Font.ITALIC));
        changeMessage.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        changeMessage.setEditable(false);
        changeMessage.setWrapStyleWord(true);
        changeMessage.setLineWrap(true);
        changeMessage.setRows(2);

        deleteMessage = new JTextArea(getMsg("this.will.delete.actions.dated.after.action.date"));
        deleteMessage.setFont(changeMessage.getFont().deriveFont(Font.ITALIC));
        deleteMessage.setForeground(Color.red);
        deleteMessage.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        deleteMessage.setEditable(false);
        deleteMessage.setWrapStyleWord(true);
        deleteMessage.setLineWrap(true);
        deleteMessage.setRows(2);


        endButtonGroup = new ButtonGroup();
        endButtonGroup.add(endNeverRadio);
        endButtonGroup.add(endNbrRadio);
        endButtonGroup.add(endDateRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[]2[grow]0", "0[]2[]2[]2[]0"));

        panel.add(createLabel, "align left, span, split 3");
        panel.add(freqSpinner, "align left");
        panel.add(periodCombo, "align left, wrap");

        panel.add(endLabel,      "align left, span, split 6");
        panel.add(endNeverRadio, "align left");
        panel.add(endNbrRadio,   "align left");
        panel.add(endNbrSpinner, "align left, w 54");
        panel.add(endDateRadio,  "align left");
        panel.add(endDateField,  "align left, wrap");

        panel.add(changeMessage, "gaptop 20, span, growx, shrinkx, wrap");
        panel.add(deleteMessage, "gaptop 4,  span, growx, shrinkx, wrap");

        panel.setPreferredSize(new Dimension(800, 600));

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void periodComboBoxActionPerformed() {
        modRecurrence.setPeriod(getPeriod((PeriodType) periodCombo.getSelectedItem()));
        setEndValues();
        stateChange();
}

    private void endNbrRadioButtonActionPerformed() {
        setEndValues();
        setEnabled(isEnabled());
}

    private void frequencySpinnerStateChanged() {
        modRecurrence.setFrequency((Integer) freqSpinner.getValue());
        setEndValues();
        stateChange();
    }

    private void endNeverRadioButtonStateChanged() {
        setEndValues();
        setEnabled(isEnabled());
    }

    private void endNbrSpinnerStateChanged() {
        setEndValues();
    }

    private void endDateFieldPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("value")) {
            setEndValues();
        }
    }

    private void endDateRadioButtonStateChanged() {
        setEndValues();
        setEnabled(isEnabled());
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    public String getMsg(String key, String p1) {
        return NbBundle.getMessage(getClass(), key, p1);
    }

    private JTextArea changeMessage;
    private JTextArea deleteMessage;
    private DateField endDateField;
    private JRadioButton endDateRadio;
    private JLabel endLabel;
    private JRadioButton endNbrRadio;
    private JSpinner endNbrSpinner;
    private JRadioButton endNeverRadio;
    private JLabel createLabel;
    private JSpinner freqSpinner;
    private PeriodTypeComboBox periodCombo;
    private ButtonGroup endButtonGroup;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}


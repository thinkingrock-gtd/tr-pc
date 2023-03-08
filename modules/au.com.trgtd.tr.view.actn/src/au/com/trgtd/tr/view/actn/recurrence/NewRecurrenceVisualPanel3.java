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
package au.com.trgtd.tr.view.actn.recurrence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
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

public final class NewRecurrenceVisualPanel3 extends JPanel {

    private static final Class clazz = NewRecurrenceVisualPanel3.class;
    private static int counter;
    private final Recurrence recurrence;
    private Map<PeriodType, Period> mapPeriods;
    
    /** Creates new form NewRecurrenceVisualPanel3 */
    public NewRecurrenceVisualPanel3(Recurrence recurrence) {
        this.recurrence = recurrence;
        initPeriods();
        initView();
        loadValues();
    }

    @Override
    public String getName() {
        return getMsg("define.subsequent.recurrence");
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(clazz, key);
    }

    private void initPeriods() {
        setPeriod(new PeriodWeekday());
        setPeriod(new PeriodDay());
        setPeriod(new PeriodWeek());
        setPeriod(new PeriodMonth());
        setPeriod(new PeriodYear());
        setPeriod(recurrence.getPeriod());
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
    
    private void enableDisableComponents() {
        endDateField.setEnabled(endDateRadio.isSelected());
        endNbrSpinner.setEnabled(endNbrRadio.isSelected());
    }

    private void setEndValues() {
        if (endNbrRadio.isSelected()) {
            recurrence.setEndNbr((Integer) endNbrSpinner.getValue());
            recurrence.setEndDate(null);
            return;
        }
        if (endDateRadio.isSelected()) {
            recurrence.setEndNbr(null);
            recurrence.setEndDate(this.endDateField.getDate());
            return;
        }
    }

    public void loadValues() {
        frequencySpinner.setValue(recurrence.getFrequency());
        periodCombo.setSelectedItem(recurrence.getPeriod().getType());
        endNbrRadio.setSelected(recurrence.getEndNbr() != null);
        endNbrSpinner.setValue(recurrence.getEndNbr() == null ? 2 : recurrence.getEndNbr());
        endDateRadio.setSelected(recurrence.getEndDate() != null);
        endDateField.setDate(recurrence.getEndDate());
        endNeverRadio.setSelected(!endNbrRadio.isSelected() && !endDateRadio.isSelected());
        enableDisableComponents();        
    }
    
    public void stateChange() {
        putClientProperty("state.change", Long.valueOf(++counter));
    }        

    public boolean isValidForm() {
        if (endDateRadio.isSelected() && endDateField.getDate() == null) {
            errorText.setText(getMsg("termination.date.must.be.entered"));
            return false;
        }
        errorText.setText("");
        return true;
    }

    private void periodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        recurrence.setPeriod(getPeriod((PeriodType) periodCombo.getSelectedItem()));
        stateChange();
        setEndValues();
    }

    private void endNbrRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setEndValues();
        stateChange();
        enableDisableComponents();
    }

    private void frequencySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        recurrence.setFrequency((Integer) frequencySpinner.getValue());
        stateChange();
        setEndValues();
    }

    private void endNeverRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {
        stateChange();
        setEndValues();
        enableDisableComponents();
    }

    private void endNbrSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        stateChange();
        setEndValues();
    }

    private void endDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("value")) {
            stateChange();
            setEndValues();
        }
    }

    private void endDateRadioButtonStateChanged(javax.swing.event.ChangeEvent evt) {
        stateChange();
        setEndValues();
        enableDisableComponents();
    }

    private void initView() {

        createLabel = new JLabel(getMsg("create.subsequent.action.in"));
        frequencySpinner = new JSpinner();
        frequencySpinner.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        frequencySpinner.setPreferredSize(new Dimension(50, 23));
        frequencySpinner.addChangeListener((ChangeEvent evt) -> {
            frequencySpinnerStateChanged(evt);
        });
        periodCombo = new PeriodTypeComboBox();
        periodCombo.addActionListener((ActionEvent evt) -> {
            periodComboBoxActionPerformed(evt);
        });
        endLabel = new JLabel(getMsg("terminate"));
        endLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        endNeverRadio = new JRadioButton(getMsg("never"));
        endNeverRadio.setVerticalTextPosition(SwingConstants.TOP);
        endNeverRadio.addChangeListener((ChangeEvent evt) -> {
            endNeverRadioButtonStateChanged(evt);
        });
        endNbrRadio = new JRadioButton(getMsg("after"));
//      endNbrRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        endNbrRadio.addActionListener((ActionEvent evt) -> {
            endNbrRadioButtonActionPerformed(evt);
        });
        endNbrSpinner = new JSpinner();
        endNbrSpinner.setModel(new SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2), null, Integer.valueOf(1)));
        endNbrSpinner.addChangeListener((ChangeEvent evt) -> {
            endNbrSpinnerStateChanged(evt);
        });
        endDateRadio = new JRadioButton(getMsg("after"));
//      endDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        endDateRadio.addChangeListener((ChangeEvent evt) -> {
            endDateRadioButtonStateChanged(evt);
        });
        endDateField = new DateField();
        endDateField.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            endDateFieldPropertyChange(evt);
        });

        errorText = new JTextArea();
        errorText.setForeground(Color.red);
        errorText.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        errorText.setEditable(false);
        errorText.setWrapStyleWord(true);
        errorText.setLineWrap(true);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(endNeverRadio);
        buttonGroup.add(endNbrRadio);
        buttonGroup.add(endDateRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[]2[grow]0", "0[]2[]2[grow]0"));

        panel.add(createLabel, "align left, span, split 3");
        panel.add(frequencySpinner, "align left");
        panel.add(periodCombo, "align left, wrap");

        panel.add(endLabel,      "align left, span, split 6");
        panel.add(endNeverRadio, "align left");
        panel.add(endNbrRadio,   "align left");
        panel.add(endNbrSpinner, "align left, w 54");
        panel.add(endDateRadio,  "align left");
        panel.add(endDateField,  "align left, wrap");

        panel.add(errorText,   "gaptop 15, span, grow, shrink");

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private ButtonGroup buttonGroup;
    private DateField endDateField;
    private JRadioButton endDateRadio;
    private JLabel endLabel;
    private JRadioButton endNbrRadio;
    private JSpinner endNbrSpinner;
    private JRadioButton endNeverRadio;
    private JTextArea errorText;
    private JSpinner frequencySpinner;
    private JLabel createLabel;
    private PeriodTypeComboBox periodCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}


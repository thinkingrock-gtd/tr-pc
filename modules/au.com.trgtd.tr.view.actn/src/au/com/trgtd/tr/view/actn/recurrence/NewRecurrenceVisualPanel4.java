/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.trgtd.tr.view.actn.recurrence;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Dialog;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import tr.model.action.Intervals;
import tr.model.action.Period;
import tr.model.action.PeriodDay;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodType;
import tr.model.action.PeriodWeek;
import tr.model.action.PeriodWeekday; 
import tr.model.action.PeriodYear;
import tr.model.action.Recurrence;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.view.actn.PeriodMonthlyPanel;
import au.com.trgtd.tr.view.actn.PeriodWeeklyPanel;
import au.com.trgtd.tr.view.actn.PeriodYearlyPanel;

public final class NewRecurrenceVisualPanel4 extends JPanel {

    private static int counter;
    private final Recurrence recurrence;
    private final Date actionScheduledDate;
    private Map<PeriodType, Period> mapPeriods;
    private Map<PeriodType, Integer> mapAdvanceNbrs;
    private PeriodWeeklyPanel periodWeeklyPanel;
    private PeriodMonthlyPanel periodMonthlyPanel;
    private PeriodYearlyPanel periodYearlyPanel;

    public NewRecurrenceVisualPanel4(Recurrence recurrence, Date actionScheduledDate) {
        this.recurrence = recurrence;
        this.actionScheduledDate = actionScheduledDate;
        initComponents();
        initPeriods();
        initPanel();
    }

    private void initPeriods() {
        setPeriod(new PeriodWeekday());
        setPeriod(new PeriodDay());
        setPeriod(new PeriodWeek());
        setPeriod(new PeriodMonth());
        setPeriod(new PeriodYear());
        setPeriod(recurrence.getPeriod());
        setAdvanceNbr(recurrence.getPeriod().getType(), recurrence.getAdvanceNbr());
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

    private Integer getAdvanceNbr(PeriodType type) {
        if (mapAdvanceNbrs == null) {
            mapAdvanceNbrs = new HashMap<>();
        }
        Integer n = mapAdvanceNbrs.get(type);
        if (n == null || n < 1) {
            n = getPeriod(type).getDefaultAdvanceNbr();
        }
        return n;
    }

    private void setAdvanceNbr(PeriodType type, Integer advanceNbr) {
        if (mapAdvanceNbrs == null) {
            mapAdvanceNbrs = new HashMap<>();
        }
        mapAdvanceNbrs.put(type, advanceNbr);
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "define.regular.recurrence");
    }

    public void stateChange() {
        putClientProperty("state.change", Long.valueOf(++counter));
    }

    public void initPanel() {
        startDateField.setDate(recurrence.getStartDate());
        frequencySpinner.setValue(recurrence.getFrequency());
        periodComboBox.setSelectedItem(recurrence.getPeriod().getType());
        advanceSpinner.setValue(recurrence.getAdvanceNbr());
        endNbrRadioButton.setSelected(recurrence.getEndNbr() != null);
        endNbrSpinner.setValue(recurrence.getEndNbr() == null ? 2 : recurrence.getEndNbr());
        endDateRadioButton.setSelected(recurrence.getEndDate() != null);
        endDateField.setDate(recurrence.getEndDate());
        endNeverRadioButton.setSelected(!endNbrRadioButton.isSelected() && !endDateRadioButton.isSelected());
        enableDisableComponents();
    }

    private void enableDisableComponents() {
        endDateField.setEnabled(endDateRadioButton.isSelected());
        endNbrSpinner.setEnabled(endNbrRadioButton.isSelected());
    }

    private void setEndValues() {
        if (endNbrRadioButton.isSelected()) {
            recurrence.setEndNbr((Integer) endNbrSpinner.getValue());
            recurrence.setEndDate(null);

            // show termination date in field
            Date start = recurrence.getStartDate();
            if (start != null) {
                Period per = recurrence.getPeriod();
                int freq = recurrence.getFrequency();
                int endNbr = recurrence.getEndNbr();
                Intervals intervals = new Intervals(per, freq, start);
                Date endDate = Recurrence.getTerminationEndDate(intervals, endNbr);
                endDateField.setDate(endDate);
            }

            return;
        }
        if (endDateRadioButton.isSelected()) {
            recurrence.setEndNbr(null);
            recurrence.setEndDate(this.endDateField.getDate());
            return;
        }
    }

    public boolean isValidForm() {
        if (startDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "start.date.must.be.entered"));
            return false;
        }
        if (actionScheduledDate != null) {
            if (startDateField.getDate().before(DateUtils.clearTime(actionScheduledDate))) {
                String d = Constants.DATE_FORMAT_FIXED.format(actionScheduledDate);
                errorLabel1.setText(org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "start.date.not.before.action.date", d));
                errorLabel2.setText(org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "start.date.not.before.action.date.hint"));
                return false;
            }
        }
        if (endDateRadioButton.isSelected() && endDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "termination.date.must.be.entered"));
            return false;
        }
        errorLabel1.setText("");
        errorLabel2.setText("");
        return true;
    }

    private Dialog getDialog() {
        return (Dialog) SwingUtilities.getWindowAncestor(this);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        startLabel = new javax.swing.JLabel();
        startDateField = new au.com.trgtd.tr.swing.date.field.DateField();
        periodLabel = new javax.swing.JLabel();
        frequencySpinner = new javax.swing.JSpinner();
        periodComboBox = new au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox();
        periodPanel = new javax.swing.JPanel();
        advanceLabel1 = new javax.swing.JLabel();
        advanceSpinner = new javax.swing.JSpinner();
        advanceLabel2 = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        endNeverRadioButton = new javax.swing.JRadioButton();
        endNbrRadioButton = new javax.swing.JRadioButton();
        endNbrSpinner = new javax.swing.JSpinner();
        endNbrLabel = new javax.swing.JLabel();
        endDateRadioButton = new javax.swing.JRadioButton();
        endDateField = new au.com.trgtd.tr.swing.date.field.DateField();
        errorLabel1 = new javax.swing.JLabel();
        errorLabel2 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(480, 360));

        startLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(startLabel, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "start.date")); // NOI18N
        startLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        startLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        startLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        startDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startDateFieldPropertyChange(evt);
            }
        });

        periodLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(periodLabel, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "period")); // NOI18N
        periodLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        periodLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        periodLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        frequencySpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        frequencySpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        frequencySpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        frequencySpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        frequencySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                frequencySpinnerStateChanged(evt);
            }
        });

        periodComboBox.setMaximumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setMinimumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setPreferredSize(new java.awt.Dimension(125, 23));
        periodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                periodActionPerformed(evt);
            }
        });

        periodPanel.setMaximumSize(new java.awt.Dimension(530, 32));
        periodPanel.setMinimumSize(new java.awt.Dimension(530, 32));
        periodPanel.setPreferredSize(new java.awt.Dimension(530, 32));
        periodPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        advanceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel1, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "generate")); // NOI18N
        advanceLabel1.setMaximumSize(new java.awt.Dimension(100, 23));
        advanceLabel1.setMinimumSize(new java.awt.Dimension(100, 23));
        advanceLabel1.setPreferredSize(new java.awt.Dimension(100, 23));

        advanceSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        advanceSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        advanceSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        advanceSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        advanceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                advanceSpinnerStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel2, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "periods.in.advance")); // NOI18N
        advanceLabel2.setMaximumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setMinimumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setPreferredSize(new java.awt.Dimension(67, 23));

        endLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(endLabel, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "terminate")); // NOI18N
        endLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        endLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        endLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        buttonGroup.add(endNeverRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNeverRadioButton, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "never")); // NOI18N
        endNeverRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        endNeverRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endNeverRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup.add(endNbrRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNbrRadioButton, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "after")); // NOI18N
        endNbrRadioButton.setMaximumSize(new java.awt.Dimension(62, 23));
        endNbrRadioButton.setMinimumSize(new java.awt.Dimension(62, 23));
        endNbrRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        endNbrRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        endNbrRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endNbrRadioButtonActionPerformed(evt);
            }
        });

        endNbrSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2), null, Integer.valueOf(1)));
        endNbrSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endNbrSpinnerStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(endNbrLabel, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "periods")); // NOI18N
        endNbrLabel.setMaximumSize(new java.awt.Dimension(90, 23));
        endNbrLabel.setMinimumSize(new java.awt.Dimension(90, 23));
        endNbrLabel.setPreferredSize(new java.awt.Dimension(90, 23));

        buttonGroup.add(endDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endDateRadioButton, org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "after")); // NOI18N
        endDateRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endDateRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endDateRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        endDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        endDateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDateRadioButtonActionPerformed(evt);
            }
        });

        endDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                endDateFieldPropertyChange(evt);
            }
        });

        errorLabel1.setForeground(java.awt.Color.red);
        errorLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        errorLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        errorLabel1.setMaximumSize(new java.awt.Dimension(1024, 23));
        errorLabel1.setMinimumSize(new java.awt.Dimension(16, 23));
        errorLabel1.setOpaque(true);
        errorLabel1.setPreferredSize(new java.awt.Dimension(16, 23));

        errorLabel2.setForeground(java.awt.Color.red);
        errorLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        errorLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        errorLabel2.setMaximumSize(new java.awt.Dimension(1024, 23));
        errorLabel2.setMinimumSize(new java.awt.Dimension(16, 23));
        errorLabel2.setOpaque(true);
        errorLabel2.setPreferredSize(new java.awt.Dimension(16, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(startLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(periodLabel, 0, 0, Short.MAX_VALUE)
                                    .addComponent(endLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(advanceLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(frequencySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(periodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(advanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(advanceLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(endNeverRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(endNbrRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(endNbrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(endNbrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(errorLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(errorLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(periodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(startLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(periodLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencySpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(periodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(advanceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(advanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(advanceLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endNeverRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(endNbrLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(endNbrRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(endNbrSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(endDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(errorLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {advanceLabel1, advanceLabel2, advanceSpinner});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {frequencySpinner, periodComboBox, periodLabel});

    }// </editor-fold>//GEN-END:initComponents

    private void endNbrRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endNbrRadioButtonActionPerformed
        stateChange();
        setEndValues();
        enableDisableComponents();
    }//GEN-LAST:event_endNbrRadioButtonActionPerformed

    private void periodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_periodActionPerformed

        PeriodType periodType = (PeriodType) periodComboBox.getSelectedItem();

        Period period = getPeriod(periodType);
        Integer advanceNbr = getAdvanceNbr(periodType);

        recurrence.setPeriod(period);
        recurrence.setAdvanceNbr(advanceNbr);

        periodPanel.removeAll();

        switch (periodType) {
            case WEEK: {
                periodPanel.add(new JLabel(org.openide.util.NbBundle.getMessage(NewRecurrenceVisualPanel4.class, "on")));
                if (periodWeeklyPanel == null) {
                    periodWeeklyPanel = new PeriodWeeklyPanel();
                    periodWeeklyPanel.setPeriodWeek((PeriodWeek) period);
                }
                periodWeeklyPanel.notifyStartDate(startDateField.getDate());
                periodPanel.add(periodWeeklyPanel);
                break;
            }
            case MONTH: {
                if (periodMonthlyPanel == null) {
                    periodMonthlyPanel = new PeriodMonthlyPanel(getDialog(), (PeriodMonth) period);
                }
                periodMonthlyPanel.notifyStartDate(startDateField.getDate());
                periodPanel.add(periodMonthlyPanel);
                break;
            }
            case YEAR: {
                if (periodYearlyPanel == null) {
                    periodYearlyPanel = new PeriodYearlyPanel(getDialog(), (PeriodYear) period);
                }
                periodYearlyPanel.notifyStartDate(startDateField.getDate());
                periodPanel.add(periodYearlyPanel);
                break;
            }
        }

        advanceSpinner.setValue(advanceNbr);

        stateChange();
        setEndValues();

        periodPanel.validate();
        periodPanel.repaint();
    }//GEN-LAST:event_periodActionPerformed

    private void advanceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_advanceSpinnerStateChanged
        Integer oldValue = getAdvanceNbr(recurrence.getPeriod().getType());
        Integer newValue = (Integer) advanceSpinner.getValue();
        if (oldValue != newValue) {
            recurrence.setAdvanceNbr(newValue);
            setAdvanceNbr(recurrence.getPeriod().getType(), recurrence.getAdvanceNbr());
            stateChange();
        }
    }//GEN-LAST:event_advanceSpinnerStateChanged

    private void startDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_startDateFieldPropertyChange
        if (evt.getPropertyName().equals("value")) {
            recurrence.setStartDate(startDateField.getDate());
            putClientProperty("start", startDateField.getDate());
            stateChange();
            setEndValues();
        }
    }//GEN-LAST:event_startDateFieldPropertyChange

    private void endNeverRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endNeverRadioButtonActionPerformed
        stateChange();
        setEndValues();
        enableDisableComponents();
    }//GEN-LAST:event_endNeverRadioButtonActionPerformed

    private void endDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDateRadioButtonActionPerformed
        stateChange();
        setEndValues();
        enableDisableComponents();
    }//GEN-LAST:event_endDateRadioButtonActionPerformed

    private void endNbrSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endNbrSpinnerStateChanged
        stateChange();
        setEndValues();
    }//GEN-LAST:event_endNbrSpinnerStateChanged

    private void endDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_endDateFieldPropertyChange
        stateChange();
        setEndValues();
    }//GEN-LAST:event_endDateFieldPropertyChange

private void frequencySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_frequencySpinnerStateChanged
    recurrence.setFrequency((Integer) frequencySpinner.getValue());
    stateChange();
    setEndValues();
}//GEN-LAST:event_frequencySpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advanceLabel1;
    private javax.swing.JLabel advanceLabel2;
    private javax.swing.JSpinner advanceSpinner;
    private javax.swing.ButtonGroup buttonGroup;
    private au.com.trgtd.tr.swing.date.field.DateField endDateField;
    private javax.swing.JRadioButton endDateRadioButton;
    private javax.swing.JLabel endLabel;
    private javax.swing.JLabel endNbrLabel;
    private javax.swing.JRadioButton endNbrRadioButton;
    private javax.swing.JSpinner endNbrSpinner;
    private javax.swing.JRadioButton endNeverRadioButton;
    private javax.swing.JLabel errorLabel1;
    private javax.swing.JLabel errorLabel2;
    private javax.swing.JSpinner frequencySpinner;
    private au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox periodComboBox;
    private javax.swing.JLabel periodLabel;
    private javax.swing.JPanel periodPanel;
    private au.com.trgtd.tr.swing.date.field.DateField startDateField;
    private javax.swing.JLabel startLabel;
    // End of variables declaration//GEN-END:variables
}


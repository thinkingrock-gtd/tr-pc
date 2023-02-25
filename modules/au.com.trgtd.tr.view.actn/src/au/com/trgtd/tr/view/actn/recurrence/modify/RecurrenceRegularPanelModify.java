/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.trgtd.tr.view.actn.recurrence.modify;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
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
import au.com.trgtd.tr.view.actn.PeriodMonthlyPanel;
import au.com.trgtd.tr.view.actn.PeriodWeeklyPanel;
import au.com.trgtd.tr.view.actn.PeriodYearlyPanel;

public final class RecurrenceRegularPanelModify extends JPanel {

    private Recurrence modRecurrence;
    private Date recurrenceStartDate;
    private Date actionScheduledDate;
    private Dialog dialog;
    private long counter;

    public RecurrenceRegularPanelModify() {
        initComponents();
    }

    public void setModel(
            Recurrence modRecurrence,
            Date actionScheduledDate,
            Date recurrenceStartDate,
            boolean basisChanged) {
        this.modRecurrence = modRecurrence;
        this.recurrenceStartDate = recurrenceStartDate;
        this.actionScheduledDate = actionScheduledDate;
        initPeriods();
        initPanel(basisChanged);
        setEnabled(isEnabled());
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "change.regular.recurrence");
    }

    private void initPeriods() {
        setPeriod(new PeriodWeekday());
        setPeriod(new PeriodDay());
        setPeriod(new PeriodWeek());
        setPeriod(new PeriodMonth());
        setPeriod(new PeriodYear());
        setPeriod(modRecurrence.getPeriod());
        setAdvanceNbr(modRecurrence.getPeriod().getType(), modRecurrence.getAdvanceNbr());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        terminationButtonGroup = new javax.swing.ButtonGroup();
        startDatebuttonGroup = new javax.swing.ButtonGroup();
        periodLabel = new javax.swing.JLabel();
        frequencySpinner = new javax.swing.JSpinner();
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
        periodComboBox = new au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox();
        startLabel = new javax.swing.JLabel();
        fillerLabel = new javax.swing.JLabel();
        startStartDateRadioButton = new javax.swing.JRadioButton();
        startActionDateRadioButton = new javax.swing.JRadioButton();
        startEnterRadioButton = new javax.swing.JRadioButton();
        startEnterDateField = new au.com.trgtd.tr.swing.date.field.DateField();
        errorLabel1 = new javax.swing.JLabel();
        errorLabel2 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1024, 380));
        setMinimumSize(new java.awt.Dimension(680, 380));
        setPreferredSize(new java.awt.Dimension(680, 380));

        periodLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(periodLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods")); // NOI18N
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

        periodPanel.setMaximumSize(new java.awt.Dimension(530, 26));
        periodPanel.setMinimumSize(new java.awt.Dimension(530, 26));
        periodPanel.setPreferredSize(new java.awt.Dimension(530, 26));
        periodPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        advanceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel1, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "generate")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel2, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods.in.advance")); // NOI18N
        advanceLabel2.setMaximumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setMinimumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setPreferredSize(new java.awt.Dimension(67, 23));

        endLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(endLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "terminate")); // NOI18N
        endLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        endLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        endLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        terminationButtonGroup.add(endNeverRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNeverRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "never")); // NOI18N
        endNeverRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endNeverRadioButtonActionPerformed(evt);
            }
        });

        terminationButtonGroup.add(endNbrRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNbrRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "after")); // NOI18N
        endNbrRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endNbrRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endNbrRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
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

        org.openide.awt.Mnemonics.setLocalizedText(endNbrLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods")); // NOI18N
        endNbrLabel.setMaximumSize(new java.awt.Dimension(80, 23));
        endNbrLabel.setMinimumSize(new java.awt.Dimension(80, 23));
        endNbrLabel.setPreferredSize(new java.awt.Dimension(80, 23));

        terminationButtonGroup.add(endDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "after")); // NOI18N
        endDateRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endDateRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endDateRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
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

        periodComboBox.setMaximumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setMinimumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setPreferredSize(new java.awt.Dimension(125, 23));
        periodComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                periodActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(startLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.recurrent.actions.from")); // NOI18N
        startLabel.setMaximumSize(new java.awt.Dimension(286, 23));
        startLabel.setMinimumSize(new java.awt.Dimension(286, 23));
        startLabel.setPreferredSize(new java.awt.Dimension(286, 23));

        fillerLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        fillerLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setPreferredSize(new java.awt.Dimension(100, 23));
        fillerLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        startDatebuttonGroup.add(startStartDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startStartDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "recurrence.start.date")); // NOI18N
        startStartDateRadioButton.setMaximumSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setMinimumSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setPreferredSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startStartDateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStartDateRadioButtonActionPerformed(evt);
            }
        });

        startDatebuttonGroup.add(startActionDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startActionDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "selected.action.date")); // NOI18N
        startActionDateRadioButton.setMaximumSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setMinimumSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setPreferredSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startActionDateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionDateRadioButtonActionPerformed(evt);
            }
        });

        startDatebuttonGroup.add(startEnterRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startEnterRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "this.date")); // NOI18N
        startEnterRadioButton.setMaximumSize(new java.awt.Dimension(110, 23));
        startEnterRadioButton.setMinimumSize(new java.awt.Dimension(110, 23));
        startEnterRadioButton.setPreferredSize(new java.awt.Dimension(110, 23));
        startEnterRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startEnterRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startEnterRadioButtonActionPerformed(evt);
            }
        });

        startEnterDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startEnterDateFieldPropertyChange(evt);
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
        errorLabel2.setMaximumSize(new java.awt.Dimension(1024, 23));
        errorLabel2.setMinimumSize(new java.awt.Dimension(0, 23));
        errorLabel2.setPreferredSize(new java.awt.Dimension(0, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(endLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(advanceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(periodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(advanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(advanceLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(endNeverRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endNbrRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endNbrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endNbrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(frequencySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(periodComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(periodPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fillerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(startEnterRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(startEnterDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(startActionDateRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                                    .addComponent(startStartDateRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(startLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(errorLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(periodComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frequencySpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(periodLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(periodPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(advanceLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(advanceSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(advanceLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(endDateField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDateRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endNbrLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endNbrSpinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endNbrRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endNeverRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(startLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fillerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startStartDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(startActionDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startEnterRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startEnterDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(errorLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {advanceLabel1, advanceLabel2, advanceSpinner});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {frequencySpinner, periodLabel});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {endDateField, endDateRadioButton, endLabel, endNbrLabel, endNbrRadioButton, endNbrSpinner, endNeverRadioButton});

    }// </editor-fold>//GEN-END:initComponents
    private void endNbrRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endNbrRadioButtonActionPerformed
        setEndValues();
        setEnabled(isEnabled());
    }//GEN-LAST:event_endNbrRadioButtonActionPerformed

    private void periodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_periodActionPerformed

        PeriodType periodType = (PeriodType) periodComboBox.getSelectedItem();

        Period period = getPeriod(periodType);
        Integer advanceNbr = getAdvanceNbr(periodType);

        modRecurrence.setPeriod(period);
        modRecurrence.setAdvanceNbr(advanceNbr);

        stateChange();

        periodPanel.removeAll();

        switch (periodType) {
            case WEEK: {
                periodPanel.add(onLabel);
                if (periodWeeklyPanel == null) {
                    periodWeeklyPanel = new PeriodWeeklyPanel();
                    periodWeeklyPanel.setPeriodWeek((PeriodWeek) period);
                }
                periodWeeklyPanel.notifyStartDate(modRecurrence.getStartDate());
                periodPanel.add(periodWeeklyPanel);
                break;
            }
            case MONTH: {
                if (periodMonthlyPanel == null) {
                    periodMonthlyPanel = new PeriodMonthlyPanel(getDialog(), (PeriodMonth) period);
                }
                periodMonthlyPanel.notifyStartDate(modRecurrence.getStartDate());
                periodPanel.add(periodMonthlyPanel);
                break;
            }
            case YEAR: {
                if (periodYearlyPanel == null) {
                    periodYearlyPanel = new PeriodYearlyPanel(getDialog(), (PeriodYear) period);
                }
                periodYearlyPanel.notifyStartDate(modRecurrence.getStartDate());
                periodPanel.add(periodYearlyPanel);
                break;
            }
        }

        advanceSpinner.setValue(advanceNbr);

        setEndValues();

        periodPanel.validate();
        periodPanel.repaint();
    }//GEN-LAST:event_periodActionPerformed

    public void setDialog(Dialog dialog) {
        assert (dialog != null);
        this.dialog = dialog;
    }

    private Dialog getDialog() {
        if (dialog == null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof Dialog dlg) {
                dialog = dlg;
            } else if (window instanceof Frame frame) {
                dialog = new Dialog(frame);
            } else {
                dialog = new Dialog(new Frame());
            }
        }
        assert (dialog != null);
        return dialog;
    }

    private void advanceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_advanceSpinnerStateChanged
        Integer oldValue = getAdvanceNbr(modRecurrence.getPeriod().getType());
        Integer newValue = (Integer) advanceSpinner.getValue();
        if (oldValue != newValue) {
            modRecurrence.setAdvanceNbr(newValue);
            stateChange();
            setAdvanceNbr(modRecurrence.getPeriod().getType(), modRecurrence.getAdvanceNbr());
        }
    }//GEN-LAST:event_advanceSpinnerStateChanged

    private void endNeverRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endNeverRadioButtonActionPerformed
        setEndValues();
        setEnabled(isEnabled());
    }//GEN-LAST:event_endNeverRadioButtonActionPerformed

    private void endDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDateRadioButtonActionPerformed
        setEndValues();
        setEnabled(isEnabled());
    }//GEN-LAST:event_endDateRadioButtonActionPerformed

    private void endNbrSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endNbrSpinnerStateChanged
        setEndValues();
    }//GEN-LAST:event_endNbrSpinnerStateChanged

    private void endDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_endDateFieldPropertyChange
        setEndValues();
    }//GEN-LAST:event_endDateFieldPropertyChange

    private void startStartDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startStartDateRadioButtonActionPerformed
        modRecurrence.setStartDate(recurrenceStartDate);
        setEndValues();
        stateChange();
        setEnabled(isEnabled());

    }//GEN-LAST:event_startStartDateRadioButtonActionPerformed

    private void startActionDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionDateRadioButtonActionPerformed
        modRecurrence.setStartDate(actionScheduledDate);
        setEndValues();
        stateChange();
        setEnabled(isEnabled());
    }//GEN-LAST:event_startActionDateRadioButtonActionPerformed

    private void startEnterDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_startEnterDateFieldPropertyChange
        if (evt.getPropertyName().equals("value")) {
            modRecurrence.setStartDate(startEnterDateField.getDate());
            setEndValues();
            stateChange();
        }
    }//GEN-LAST:event_startEnterDateFieldPropertyChange

    private void startEnterRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startEnterRadioButtonActionPerformed
        modRecurrence.setStartDate(startEnterDateField.getDate());
        setEndValues();
        stateChange();
        setEnabled(isEnabled());
    }//GEN-LAST:event_startEnterRadioButtonActionPerformed

    private void frequencySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_frequencySpinnerStateChanged
        modRecurrence.setFrequency((Integer) frequencySpinner.getValue());
        setEndValues();
        stateChange();
    }//GEN-LAST:event_frequencySpinnerStateChanged

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

    public void initPanel(boolean basisChanged) {
        onLabel = new JLabel(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "on"));
        frequencySpinner.setValue(modRecurrence.getFrequency());
        periodComboBox.setSelectedItem(modRecurrence.getPeriod().getType());
        advanceSpinner.setValue(modRecurrence.getAdvanceNbr());
        endNbrRadioButton.setSelected(modRecurrence.getEndNbr() != null);
        endNbrSpinner.setValue(modRecurrence.getEndNbr() == null ? 2 : modRecurrence.getEndNbr());
        endDateRadioButton.setSelected(modRecurrence.getEndDate() != null);
        endDateField.setDate(modRecurrence.getEndDate());
        endNeverRadioButton.setSelected(!endNbrRadioButton.isSelected() && !endDateRadioButton.isSelected());
        if (basisChanged) { // i.e. was Subsequent

            startLabel.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "generate.recurrent.actions.from"));
            startStartDateRadioButton.setVisible(false);
            startStartDateRadioButton.setEnabled(false);
            startStartDateRadioButton.setSelected(false);
        } else {
            startLabel.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.recurrent.actions.from"));
            startStartDateRadioButton.setVisible(true);
            startStartDateRadioButton.setEnabled(true);
            String strStartDate = Constants.DATE_FORMAT_FIXED.format(recurrenceStartDate);
            startStartDateRadioButton.setText(startStartDateRadioButton.getText() + " (" + strStartDate + ")");
        }

        String strActionDate = Constants.DATE_FORMAT_FIXED.format(actionScheduledDate);
        startActionDateRadioButton.setText(startActionDateRadioButton.getText() + " (" + strActionDate + ")");

        startActionDateRadioButton.setSelected(true); // default   

        modRecurrence.setStartDate(actionScheduledDate);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        advanceLabel1.setEnabled(enabled);
        advanceLabel2.setEnabled(enabled);
        advanceSpinner.setEnabled(enabled);
        endDateRadioButton.setEnabled(enabled);
        endDateField.setEnabled(enabled && endDateRadioButton.isSelected());
        endLabel.setEnabled(enabled);
        endNbrLabel.setEnabled(enabled);
        endNbrRadioButton.setEnabled(enabled);
        endNbrSpinner.setEnabled(enabled && endNbrRadioButton.isSelected());
        endNeverRadioButton.setEnabled(enabled);
        frequencySpinner.setEnabled(enabled);
        periodComboBox.setEnabled(enabled);
        periodLabel.setEnabled(enabled);
        periodPanel.setEnabled(enabled);
        if (periodWeeklyPanel != null) {
            onLabel.setEnabled(enabled);
            periodWeeklyPanel.setEnabled(enabled);
        }
        if (periodMonthlyPanel != null) {
            periodMonthlyPanel.setEnabled(enabled);
        }
        if (periodYearlyPanel != null) {
            periodYearlyPanel.setEnabled(enabled);
        }
        startLabel.setEnabled(enabled);
        startStartDateRadioButton.setEnabled(enabled);
        startActionDateRadioButton.setEnabled(enabled);
        startEnterRadioButton.setEnabled(enabled);
        startEnterDateField.setEnabled(enabled && startEnterRadioButton.isSelected());
    }

    private void setEndValues() {
        if (endNbrRadioButton.isSelected()) {
            modRecurrence.setEndNbr((Integer) endNbrSpinner.getValue());
            modRecurrence.setEndDate(null);
            // show termination date in field
            Date start = modRecurrence.getStartDate();
            if (start != null) {
                Period per = modRecurrence.getPeriod();
                int freq = modRecurrence.getFrequency();
                int endNbr = modRecurrence.getEndNbr();
                Intervals intervals = new Intervals(per, freq, start);
                Date endDate = Recurrence.getTerminationEndDate(intervals, endNbr);
                endDateField.setDate(endDate);
            }
        } else if (endDateRadioButton.isSelected()) {
            modRecurrence.setEndNbr(null);
            modRecurrence.setEndDate(endDateField.getDate());
        } else if (endNeverRadioButton.isSelected()) {
            modRecurrence.setEndNbr(null);
            modRecurrence.setEndDate(null);
        }
        stateChange();
    }

    public void stateChange() {
        putClientProperty("state.change", Long.valueOf(++counter));
    }

    public boolean isValidForm() {
        if (endDateRadioButton.isSelected() && endDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "termination.date.must.be.entered"));
            errorLabel2.setText("");
            return false;
        }
        if (startEnterRadioButton.isSelected() && startEnterDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.must.be.entered"));
            errorLabel2.setText("");
            return false;
        }
        if (startEnterRadioButton.isSelected() && startEnterDateField.getDate().before(actionScheduledDate)) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.can.not.be.before.selected.action.date"));
            errorLabel2.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.can.not.be.before.selected.action.date.hint"));
            return false;
        }
        errorLabel1.setText("");
        errorLabel2.setText("");
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel advanceLabel1;
    private javax.swing.JLabel advanceLabel2;
    private javax.swing.JSpinner advanceSpinner;
    private au.com.trgtd.tr.swing.date.field.DateField endDateField;
    private javax.swing.JRadioButton endDateRadioButton;
    private javax.swing.JLabel endLabel;
    private javax.swing.JLabel endNbrLabel;
    private javax.swing.JRadioButton endNbrRadioButton;
    private javax.swing.JSpinner endNbrSpinner;
    private javax.swing.JRadioButton endNeverRadioButton;
    private javax.swing.JLabel errorLabel1;
    private javax.swing.JLabel errorLabel2;
    private javax.swing.JLabel fillerLabel;
    private javax.swing.JSpinner frequencySpinner;
    private au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox periodComboBox;
    private javax.swing.JLabel periodLabel;
    private javax.swing.JPanel periodPanel;
    private javax.swing.JRadioButton startActionDateRadioButton;
    private javax.swing.ButtonGroup startDatebuttonGroup;
    private au.com.trgtd.tr.swing.date.field.DateField startEnterDateField;
    private javax.swing.JRadioButton startEnterRadioButton;
    private javax.swing.JLabel startLabel;
    private javax.swing.JRadioButton startStartDateRadioButton;
    private javax.swing.ButtonGroup terminationButtonGroup;
    // End of variables declaration//GEN-END:variables
    private PeriodWeeklyPanel periodWeeklyPanel;
    private PeriodMonthlyPanel periodMonthlyPanel;
    private PeriodYearlyPanel periodYearlyPanel;
    private Map<PeriodType, Integer> mapAdvanceNbrs;
    private Map<PeriodType, Period> mapPeriods;
    private JLabel onLabel;
}

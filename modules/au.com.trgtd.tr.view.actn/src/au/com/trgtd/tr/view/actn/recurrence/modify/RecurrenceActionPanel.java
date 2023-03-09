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
 
import au.com.trgtd.tr.view.notes.NotesViewField;
import au.com.trgtd.tr.view.notes.NotesViewLabel;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Recurrence;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.topic.Topic;

public final class RecurrenceActionPanel extends JPanel {

    public RecurrenceActionPanel() {
        this.data = DataLookup.instance().lookup(Data.class);
        initComponents();
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
        initPanel();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RecurrenceActionPanel.class, "enter.action.details");
    }


//    private NotesEditorDialog notesEditorDialog;
//    private NotesEditorDialog getNotesEditorDialog() {
//        if (notesEditorDialog == null) {
//            Dialog owner = (Dialog)SwingUtilities.getWindowAncestor(this);
//            notesEditorDialog = NotesEditorDialog.create(owner);
//        }
//        return notesEditorDialog;
//    }

    public void initPanel() {
        schdHourSpinner.setVal(recurrence.getScheduleHours());
        schdMinSpinner.setVal(recurrence.getScheduleMins());
        durationHourSpinner.setVal(recurrence.getDurationHours());
        durationMinSpinner.setVal(recurrence.getDurationMins());
        descriptionTextField.setText(recurrence.getDescription());
        successTextArea.setText(recurrence.getSuccess());
        contextsComboBox.setSelectedItem(recurrence.getContext());
        topicsComboBox.setSelectedItem(recurrence.getTopic());
        timeComboBox.setSelectedItem(recurrence.getTime());
        energyComboBox.setSelectedItem(recurrence.getEnergy());
        priorityComboBox.setSelectedItem(recurrence.getPriority());
        notesViewLabel.setNotable(recurrence);
        notesViewField.setNotable(recurrence);
        notesViewField.setEditable(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        schdHourSpinner.setEnabled(enabled);
        schdMinSpinner.setEnabled(enabled);
        durationHourSpinner.setEnabled(enabled);
        durationMinSpinner.setEnabled(enabled);
        descriptionTextField.setEnabled(enabled);
        successTextArea.setEnabled(enabled);
        contextsComboBox.setEnabled(enabled);
        topicsComboBox.setEnabled(enabled);
        timeComboBox.setEnabled(enabled && useTime());
        energyComboBox.setEnabled(enabled && useEnergy());
        priorityComboBox.setEnabled(enabled && usePriority());
        notesViewLabel.setEnabled(enabled);
        notesViewField.setEnabled(enabled);
    }

    private boolean useTime() {
        return data == null ? true : data.getTimeCriterion().isUse();
    }

    private boolean useEnergy() {
        return data == null ? true : data.getEnergyCriterion().isUse();
    }

    private boolean usePriority() {
        return data == null ? true : data.getPriorityCriterion().isUse();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        schdTimeLabel = new javax.swing.JLabel();
        schdHourSpinner = new au.com.trgtd.tr.swing.time.HourSpinner();
        schdMinSpinner = new au.com.trgtd.tr.swing.time.MinuteSpinner();
        durationLabel = new javax.swing.JLabel();
        durationHourSpinner = new au.com.trgtd.tr.swing.time.HourSpinner();
        durationMinSpinner = new au.com.trgtd.tr.swing.time.MinuteSpinner();
        descriptionLabel = new javax.swing.JLabel();
        descriptionTextField = new javax.swing.JTextField();
        successLabel = new javax.swing.JLabel();
        successScrollPane = new javax.swing.JScrollPane();
        successTextArea = new javax.swing.JTextArea();
        contextLabel = new javax.swing.JLabel();
        contextsComboBox = new au.com.trgtd.tr.view.contexts.ContextsComboBox();
        topicLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        timeComboBox = new au.com.trgtd.tr.view.criteria.TimeComboBox();
        energyLabel = new javax.swing.JLabel();
        energyComboBox = new au.com.trgtd.tr.view.criteria.EnergyComboBox();
        priorityLabel = new javax.swing.JLabel();
        priorityComboBox = new au.com.trgtd.tr.view.criteria.PriorityComboBox();
        notesViewLabel = new NotesViewLabel();
        notesViewField = new NotesViewField(true);
        topicsComboBox = new au.com.trgtd.tr.view.topics.TopicsComboBox();

        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(680, 224));

        schdTimeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(schdTimeLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "schedule.time")); // NOI18N
        schdTimeLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        schdTimeLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        schdTimeLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        schdHourSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        schdHourSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        schdHourSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        schdHourSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                schdHourSpinnerStateChanged(evt);
            }
        });

        schdMinSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        schdMinSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        schdMinSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        schdMinSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                schdMinSpinnerStateChanged(evt);
            }
        });

        durationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(durationLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "duration")); // NOI18N
        durationLabel.setMaximumSize(new java.awt.Dimension(70, 23));
        durationLabel.setMinimumSize(new java.awt.Dimension(70, 23));
        durationLabel.setPreferredSize(new java.awt.Dimension(70, 23));

        durationHourSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        durationHourSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        durationHourSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        durationHourSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                durationHourSpinnerStateChanged(evt);
            }
        });

        durationMinSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        durationMinSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        durationMinSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        durationMinSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                durationMinSpinnerStateChanged(evt);
            }
        });

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(descriptionLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "description")); // NOI18N
        descriptionLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        descriptionLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        descriptionLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        descriptionTextField.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        descriptionTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descriptionTextFieldFocusLost(evt);
            }
        });

        successLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(successLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "success")); // NOI18N
        successLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        successLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        successLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        successTextArea.setColumns(20);
        successTextArea.setRows(2);
        successTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                successTextAreaFocusLost(evt);
            }
        });
        successTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                successTextAreaKeyPressed(evt);
            }
        });
        successScrollPane.setViewportView(successTextArea);

        contextLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(contextLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "context")); // NOI18N
        contextLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        contextLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        contextLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        contextsComboBox.setMaximumSize(new java.awt.Dimension(190, 23));
        contextsComboBox.setMinimumSize(new java.awt.Dimension(190, 23));
        contextsComboBox.setPreferredSize(new java.awt.Dimension(190, 23));
        contextsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contextsComboBoxActionPerformed(evt);
            }
        });

        topicLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(topicLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "topic")); // NOI18N
        topicLabel.setMaximumSize(new java.awt.Dimension(90, 23));
        topicLabel.setMinimumSize(new java.awt.Dimension(90, 23));
        topicLabel.setPreferredSize(new java.awt.Dimension(90, 23));

        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(timeLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "time")); // NOI18N
        timeLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        timeLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        timeLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        timeComboBox.setMaximumSize(new java.awt.Dimension(120, 23));
        timeComboBox.setMinimumSize(new java.awt.Dimension(120, 23));
        timeComboBox.setPreferredSize(new java.awt.Dimension(120, 23));
        timeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeComboBoxActionPerformed(evt);
            }
        });

        energyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(energyLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "energy")); // NOI18N
        energyLabel.setMaximumSize(new java.awt.Dimension(50, 23));
        energyLabel.setMinimumSize(new java.awt.Dimension(50, 23));
        energyLabel.setPreferredSize(new java.awt.Dimension(50, 23));

        energyComboBox.setMaximumSize(new java.awt.Dimension(120, 23));
        energyComboBox.setMinimumSize(new java.awt.Dimension(120, 23));
        energyComboBox.setPreferredSize(new java.awt.Dimension(120, 23));
        energyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                energyComboBoxActionPerformed(evt);
            }
        });

        priorityLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(priorityLabel, org.openide.util.NbBundle.getMessage(RecurrenceActionPanel.class, "priority")); // NOI18N
        priorityLabel.setMaximumSize(new java.awt.Dimension(50, 23));
        priorityLabel.setMinimumSize(new java.awt.Dimension(50, 23));
        priorityLabel.setPreferredSize(new java.awt.Dimension(50, 23));

        priorityComboBox.setMaximumSize(new java.awt.Dimension(120, 23));
        priorityComboBox.setMinimumSize(new java.awt.Dimension(120, 23));
        priorityComboBox.setPreferredSize(new java.awt.Dimension(120, 23));
        priorityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priorityComboBoxActionPerformed(evt);
            }
        });

        notesViewLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        notesViewLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        notesViewLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        notesViewLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        topicsComboBox.setMaximumSize(new java.awt.Dimension(190, 23));
        topicsComboBox.setMinimumSize(new java.awt.Dimension(190, 23));
        topicsComboBox.setPreferredSize(new java.awt.Dimension(190, 23));
        topicsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                topicsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(schdTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(successLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(notesViewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(contextsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(topicLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(topicsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(schdHourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(schdMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(durationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(durationHourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(durationMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(notesViewField, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                            .addComponent(descriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                            .addComponent(successScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(energyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(energyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(priorityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {contextLabel, descriptionLabel, notesViewLabel, schdTimeLabel, successLabel, timeLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(schdTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(schdHourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(schdMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(durationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(durationHourSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(durationMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(successLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(successScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(topicLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contextsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(topicsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energyLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priorityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priorityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(notesViewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(notesViewField, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {contextLabel, descriptionLabel, notesViewLabel, schdTimeLabel, successLabel, timeLabel});

    }// </editor-fold>//GEN-END:initComponents

    private void contextsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contextsComboBoxActionPerformed
        recurrence.setContext((Context) contextsComboBox.getSelectedItem());
    }//GEN-LAST:event_contextsComboBoxActionPerformed

    private void schdHourSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_schdHourSpinnerStateChanged
        recurrence.setScheduleHours(schdHourSpinner.getVal());
    }//GEN-LAST:event_schdHourSpinnerStateChanged

    private void schdMinSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_schdMinSpinnerStateChanged
        recurrence.setScheduleMins(schdMinSpinner.getVal());
    }//GEN-LAST:event_schdMinSpinnerStateChanged

    private void durationHourSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_durationHourSpinnerStateChanged
        recurrence.setDurationHours(durationHourSpinner.getVal());
    }//GEN-LAST:event_durationHourSpinnerStateChanged

    private void durationMinSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_durationMinSpinnerStateChanged
        recurrence.setDurationMins(durationMinSpinner.getVal());
    }//GEN-LAST:event_durationMinSpinnerStateChanged

    private void descriptionTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextFieldFocusLost
        recurrence.setDescription(descriptionTextField.getText());
    }//GEN-LAST:event_descriptionTextFieldFocusLost

    private void successTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_successTextAreaFocusLost
        recurrence.setSuccess(successTextArea.getText());
    }//GEN-LAST:event_successTextAreaFocusLost

    private void timeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeComboBoxActionPerformed
        recurrence.setTime((Value) timeComboBox.getSelectedItem());
    }//GEN-LAST:event_timeComboBoxActionPerformed

    private void energyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_energyComboBoxActionPerformed
        recurrence.setEnergy((Value) energyComboBox.getSelectedItem());
    }//GEN-LAST:event_energyComboBoxActionPerformed

    private void priorityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priorityComboBoxActionPerformed
        recurrence.setPriority((Value) priorityComboBox.getSelectedItem());
    }//GEN-LAST:event_priorityComboBoxActionPerformed

    private void topicsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topicsComboBoxActionPerformed
        recurrence.setTopic((Topic) topicsComboBox.getSelectedItem());
    }//GEN-LAST:event_topicsComboBoxActionPerformed

    private void successTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_successTextAreaKeyPressed
        recurrence.setSuccess(successTextArea.getText());
    }//GEN-LAST:event_successTextAreaKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contextLabel;
    private au.com.trgtd.tr.view.contexts.ContextsComboBox contextsComboBox;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextField descriptionTextField;
    private au.com.trgtd.tr.swing.time.HourSpinner durationHourSpinner;
    private javax.swing.JLabel durationLabel;
    private au.com.trgtd.tr.swing.time.MinuteSpinner durationMinSpinner;
    private au.com.trgtd.tr.view.criteria.EnergyComboBox energyComboBox;
    private javax.swing.JLabel energyLabel;
    private au.com.trgtd.tr.view.notes.NotesViewField notesViewField;
    private au.com.trgtd.tr.view.notes.NotesViewLabel notesViewLabel;
    private au.com.trgtd.tr.view.criteria.PriorityComboBox priorityComboBox;
    private javax.swing.JLabel priorityLabel;
    private au.com.trgtd.tr.swing.time.HourSpinner schdHourSpinner;
    private au.com.trgtd.tr.swing.time.MinuteSpinner schdMinSpinner;
    private javax.swing.JLabel schdTimeLabel;
    private javax.swing.JLabel successLabel;
    private javax.swing.JScrollPane successScrollPane;
    private javax.swing.JTextArea successTextArea;
    private au.com.trgtd.tr.view.criteria.TimeComboBox timeComboBox;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel topicLabel;
    private au.com.trgtd.tr.view.topics.TopicsComboBox topicsComboBox;
    // End of variables declaration//GEN-END:variables
    private final Data data;
    private Recurrence recurrence;
	
}


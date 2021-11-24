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
 
import javax.swing.JPanel;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

public final class ModifyRecurrenceVisualPanelChoice1 extends JPanel {

    /** Creates new form NewRecurrenceVisualPanel1 */
    public ModifyRecurrenceVisualPanelChoice1(Recurrence recurrence) {
        this.recurrence = recurrence;
        initComponents();
        initPanel();
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelChoice1.class, "choose.modification");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        actionPanel = new au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceActionPanel();
        regularPanel = new au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceRegularPanel();
        subsequentPanel = new au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceSubsequentPanel();
        modificationPanel = new javax.swing.JPanel();
        changeRecurrenceRadioButton = new javax.swing.JRadioButton();
        removeRecurrenceRadioButton = new javax.swing.JRadioButton();
        changeProjectRadioButton = new javax.swing.JRadioButton();
        changeActionRadioButton = new javax.swing.JRadioButton();

        setMinimumSize(new java.awt.Dimension(700, 600));
        setPreferredSize(new java.awt.Dimension(700, 600));

        actionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Action Details"));
        actionPanel.setEnabled(false);

        regularPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Regular Recurrence"));
        regularPanel.setEnabled(false);

        subsequentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Subsequent Recurrence"));
        subsequentPanel.setEnabled(false);

        modificationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Modification"));
        modificationPanel.setPreferredSize(new java.awt.Dimension(100, 60));
        modificationPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 8));

        buttonGroup.add(changeRecurrenceRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(changeRecurrenceRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelChoice1.class, "change.recurrence")); // NOI18N
        changeRecurrenceRadioButton.setMaximumSize(new java.awt.Dimension(160, 23));
        changeRecurrenceRadioButton.setMinimumSize(new java.awt.Dimension(160, 23));
        changeRecurrenceRadioButton.setPreferredSize(new java.awt.Dimension(160, 23));
        changeRecurrenceRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeRecurrenceRadioButtonActionPerformed(evt);
            }
        });
        modificationPanel.add(changeRecurrenceRadioButton);

        buttonGroup.add(removeRecurrenceRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(removeRecurrenceRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelChoice1.class, "remove.recurrence")); // NOI18N
        removeRecurrenceRadioButton.setMaximumSize(new java.awt.Dimension(160, 23));
        removeRecurrenceRadioButton.setMinimumSize(new java.awt.Dimension(160, 23));
        removeRecurrenceRadioButton.setPreferredSize(new java.awt.Dimension(160, 23));
        removeRecurrenceRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRecurrenceRadioButtonActionPerformed(evt);
            }
        });
        modificationPanel.add(removeRecurrenceRadioButton);

        buttonGroup.add(changeProjectRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(changeProjectRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelChoice1.class, "change.project")); // NOI18N
        changeProjectRadioButton.setMaximumSize(new java.awt.Dimension(160, 23));
        changeProjectRadioButton.setMinimumSize(new java.awt.Dimension(160, 23));
        changeProjectRadioButton.setPreferredSize(new java.awt.Dimension(160, 23));
        changeProjectRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeProjectRadioButtonActionPerformed(evt);
            }
        });
        modificationPanel.add(changeProjectRadioButton);

        buttonGroup.add(changeActionRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(changeActionRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelChoice1.class, "change.action")); // NOI18N
        changeActionRadioButton.setMaximumSize(new java.awt.Dimension(160, 23));
        changeActionRadioButton.setMinimumSize(new java.awt.Dimension(160, 23));
        changeActionRadioButton.setPreferredSize(new java.awt.Dimension(160, 23));
        changeActionRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeActionRadioButtonActionPerformed(evt);
            }
        });
        modificationPanel.add(changeActionRadioButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(modificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addComponent(actionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addComponent(regularPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addComponent(subsequentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(actionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regularPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subsequentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void changeProjectRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeProjectRadioButtonActionPerformed
        setModificationProperty();
    }//GEN-LAST:event_changeProjectRadioButtonActionPerformed

    private void changeActionRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeActionRadioButtonActionPerformed
        setModificationProperty();
    }//GEN-LAST:event_changeActionRadioButtonActionPerformed

    private void changeRecurrenceRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeRecurrenceRadioButtonActionPerformed
        setModificationProperty();
    }//GEN-LAST:event_changeRecurrenceRadioButtonActionPerformed

    private void removeRecurrenceRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRecurrenceRadioButtonActionPerformed
        setModificationProperty();
}//GEN-LAST:event_removeRecurrenceRadioButtonActionPerformed

    private void setModificationProperty() {
        if (changeProjectRadioButton.isSelected()) {
            putClientProperty(Modification.PROPERTY, Modification.CHANGE_PROJECT);
        } else if (changeActionRadioButton.isSelected()) {
            putClientProperty(Modification.PROPERTY, Modification.CHANGE_ACTION);
        } else if (changeRecurrenceRadioButton.isSelected()) {
            putClientProperty(Modification.PROPERTY, Modification.CHANGE_RECURRENCE);
        } else if (removeRecurrenceRadioButton.isSelected()) {
            putClientProperty(Modification.PROPERTY, Modification.REMOVE_RECURRENCE);
        } 
    }
    
    public boolean isModificationChosen() {
        return changeActionRadioButton.isSelected()
            || changeProjectRadioButton.isSelected()
            || changeRecurrenceRadioButton.isSelected()
            || removeRecurrenceRadioButton.isSelected();
    }
    
    public void initPanel() {
        if (recurrence.getBasis() == Basis.DONE_DATE) {
            regularPanel.setVisible(false);
            subsequentPanel.setVisible(true);
            subsequentPanel.setRecurrence(recurrence);
            subsequentPanel.setEnabled(false);
        } else {
            subsequentPanel.setVisible(false);
            regularPanel.setVisible(true);
            regularPanel.setRecurrence(recurrence);
            regularPanel.setEnabled(false);
        }
//        projectPanel.setRecurrence(recurrence);
//        projectPanel.setEnabled(false);
        actionPanel.setRecurrence(recurrence);
        actionPanel.setEnabled(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceActionPanel actionPanel;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JRadioButton changeActionRadioButton;
    private javax.swing.JRadioButton changeProjectRadioButton;
    private javax.swing.JRadioButton changeRecurrenceRadioButton;
    private javax.swing.JPanel modificationPanel;
    private au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceRegularPanel regularPanel;
    private javax.swing.JRadioButton removeRecurrenceRadioButton;
    private au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceSubsequentPanel subsequentPanel;
    // End of variables declaration//GEN-END:variables
    private final Recurrence recurrence;
}


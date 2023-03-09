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
import java.util.Date;
import javax.swing.JPanel;
import tr.model.action.Action;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Recurrence;
import tr.model.project.Project;
import au.com.trgtd.tr.util.Utils;

public final class ModifyRecurrenceVisualPanelModProject1 extends JPanel {

    private static long counter;
    private final Project currentProject;
    private final Date currentStartDate;
    private final Date currentActionDate;

    /** Creates new form NewRecurrenceVisualPanel1 */
    public ModifyRecurrenceVisualPanelModProject1(Recurrence recurrence, Action action) {
        this.currentProject = recurrence.getProject();
        this.currentStartDate = recurrence.getStartDate();
        this.currentActionDate = ((ActionStateScheduled) action.getState()).getDate();
        initComponents();
        initPanel();
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "change.project.details");
    }

    public Boolean isMoveAll() {
        return moveAllRadioButton.isSelected();
    }

    public Date getMoveDate() {
        if (isMoveAll()) {
            return null;
        }
        if (moveStartDateRadioButton.isSelected()) {
            return currentStartDate;
        }
        if (moveActionDateRadioButton.isSelected()) {
            return currentActionDate;
        }
        return null;
    }

    public Project getNewProject() {
        return projectNewComboBox.getSelectedItem();
    }

    private void moveChanged() {
        stateChange();
        enableDisableFields();
    }

    private void projectChanged() {
        stateChange();
    }

    private void stateChange() {
        putClientProperty("state.change", ++counter);
    }

    private void enableDisableFields() {
        projectCurrentComboBox.setEnabled(false);
        moveAllRadioButton.setEnabled(true);
        moveActionDateRadioButton.setEnabled(true);
        moveStartDateRadioButton.setEnabled(true);
    }

    private void initPanel() {
        projectCurrentComboBox.setSelectedItem(currentProject);
        projectNewComboBox.setSelectedItem(null);
//      moveAllRadioButton.setSelected(true);        
        moveActionDateRadioButton.setSelected(true);        
        String strActionDate = Constants.DATE_FORMAT_FIXED.format(currentActionDate);
        moveActionDateRadioButton.setText(moveActionDateRadioButton.getText() + " (" + strActionDate + ")");
        String strStartDate = Constants.DATE_FORMAT_FIXED.format(currentStartDate);
        moveStartDateRadioButton.setText(moveStartDateRadioButton.getText() + " (" + strStartDate + ")");
        moveChanged();
    }

    public boolean isFormValid() {
        if (Utils.equal(currentProject, getNewProject())) {
            return false;
        }
        return isMoveAll() || getMoveDate() != null;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        movebuttonGroup = new javax.swing.ButtonGroup();
        moveStartDateRadioButton = new javax.swing.JRadioButton();
        moveActionDateRadioButton = new javax.swing.JRadioButton();
        projectCurrentLabel = new javax.swing.JLabel();
        projectCurrentComboBox = new au.com.trgtd.tr.view.project.combo.ProjectsComboBox();
        projectNewLabel = new javax.swing.JLabel();
        projectNewComboBox = new au.com.trgtd.tr.view.project.combo.ProjectsComboBox();
        fillerLabel = new javax.swing.JLabel();
        moveAllRadioButton = new javax.swing.JRadioButton();
        moveLabel = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(480, 360));
        setPreferredSize(new java.awt.Dimension(640, 480));

        movebuttonGroup.add(moveStartDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(moveStartDateRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "dated.from.recurrence.start.date")); // NOI18N
        moveStartDateRadioButton.setMaximumSize(new java.awt.Dimension(178, 23));
        moveStartDateRadioButton.setMinimumSize(new java.awt.Dimension(178, 23));
        moveStartDateRadioButton.setPreferredSize(new java.awt.Dimension(178, 23));
        moveStartDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        moveStartDateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveStartDateRadioButtonActionPerformed(evt);
            }
        });

        movebuttonGroup.add(moveActionDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(moveActionDateRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "dated.from.selected.action.date")); // NOI18N
        moveActionDateRadioButton.setMaximumSize(new java.awt.Dimension(178, 23));
        moveActionDateRadioButton.setMinimumSize(new java.awt.Dimension(178, 23));
        moveActionDateRadioButton.setPreferredSize(new java.awt.Dimension(178, 23));
        moveActionDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        moveActionDateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveActionDateRadioButtonActionPerformed(evt);
            }
        });

        projectCurrentLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(projectCurrentLabel, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "current.project")); // NOI18N
        projectCurrentLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        projectCurrentLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        projectCurrentLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        projectCurrentComboBox.setEnabled(false);
        projectCurrentComboBox.setMaximumSize(new java.awt.Dimension(32767, 23));
        projectCurrentComboBox.setMinimumSize(new java.awt.Dimension(41, 23));
        projectCurrentComboBox.setPreferredSize(new java.awt.Dimension(41, 23));

        projectNewLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(projectNewLabel, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "new.project")); // NOI18N
        projectNewLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        projectNewLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        projectNewLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        projectNewComboBox.setMaximumSize(new java.awt.Dimension(32767, 23));
        projectNewComboBox.setMinimumSize(new java.awt.Dimension(41, 23));
        projectNewComboBox.setPreferredSize(new java.awt.Dimension(41, 23));
        projectNewComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectNewComboBoxActionPerformed(evt);
            }
        });

        fillerLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        fillerLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        movebuttonGroup.add(moveAllRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(moveAllRadioButton, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "all")); // NOI18N
        moveAllRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        moveAllRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveAllRadioButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(moveLabel, org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModProject1.class, "modify.project.move")); // NOI18N
        moveLabel.setMaximumSize(new java.awt.Dimension(150, 23));
        moveLabel.setMinimumSize(new java.awt.Dimension(150, 23));
        moveLabel.setPreferredSize(new java.awt.Dimension(150, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(moveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(projectCurrentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(projectNewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(projectNewComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                            .addComponent(projectCurrentComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fillerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(moveAllRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                            .addComponent(moveStartDateRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                            .addComponent(moveActionDateRadioButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {projectCurrentLabel, projectNewLabel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectCurrentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(projectCurrentComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectNewLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(projectNewComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(moveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fillerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(moveAllRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(moveStartDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(moveActionDateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(274, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void moveActionDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveActionDateRadioButtonActionPerformed
        moveChanged();
}//GEN-LAST:event_moveActionDateRadioButtonActionPerformed

    private void projectNewComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectNewComboBoxActionPerformed
        projectChanged();
}//GEN-LAST:event_projectNewComboBoxActionPerformed

    private void moveStartDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveStartDateRadioButtonActionPerformed
        moveChanged();
}//GEN-LAST:event_moveStartDateRadioButtonActionPerformed

    private void moveAllRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveAllRadioButtonActionPerformed
        moveChanged();
    }//GEN-LAST:event_moveAllRadioButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fillerLabel;
    private javax.swing.JRadioButton moveActionDateRadioButton;
    private javax.swing.JRadioButton moveAllRadioButton;
    private javax.swing.JLabel moveLabel;
    private javax.swing.JRadioButton moveStartDateRadioButton;
    private javax.swing.ButtonGroup movebuttonGroup;
    private au.com.trgtd.tr.view.project.combo.ProjectsComboBox projectCurrentComboBox;
    private javax.swing.JLabel projectCurrentLabel;
    private au.com.trgtd.tr.view.project.combo.ProjectsComboBox projectNewComboBox;
    private javax.swing.JLabel projectNewLabel;
    // End of variables declaration//GEN-END:variables
}


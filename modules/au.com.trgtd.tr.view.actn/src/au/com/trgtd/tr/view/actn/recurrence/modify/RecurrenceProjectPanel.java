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
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Recurrence;
import tr.model.project.Project;

public final class RecurrenceProjectPanel extends JPanel {

    public RecurrenceProjectPanel() {
        this.data = DataLookup.instance().lookup(Data.class);
        initComponents();
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
        initPanel();
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(RecurrenceProjectPanel.class, "choose.project");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectLabel = new javax.swing.JLabel();
        projectCombo = new au.com.trgtd.tr.view.project.combo.ProjectsComboBox();

        setMinimumSize(new java.awt.Dimension(250, 23));
        setPreferredSize(new java.awt.Dimension(250, 23));

        projectLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(projectLabel, org.openide.util.NbBundle.getMessage(RecurrenceProjectPanel.class, "project")); // NOI18N
        projectLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        projectLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        projectLabel.setPreferredSize(new java.awt.Dimension(100, 23));

        projectCombo.setMaximumSize(new java.awt.Dimension(32767, 23));
        projectCombo.setMinimumSize(new java.awt.Dimension(120, 23));
        projectCombo.setPreferredSize(new java.awt.Dimension(120, 23));
        projectCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                projectComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(projectCombo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(projectCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(projectLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    public void initPanel() {
        projectCombo.setSelectedItem(recurrence.getProject());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
//      projectLabel.setEnabled(enabled);
        projectCombo.setEnabled(enabled);
    }

    private void projectComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_projectComboActionPerformed
        if (projectCombo.isEnabled()) {
            recurrence.setProject(projectCombo.getSelectedItem());
        }
    }//GEN-LAST:event_projectComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private au.com.trgtd.tr.view.project.combo.ProjectsComboBox projectCombo;
    private javax.swing.JLabel projectLabel;
    // End of variables declaration//GEN-END:variables
    private final Data data;
    private Recurrence recurrence;
}


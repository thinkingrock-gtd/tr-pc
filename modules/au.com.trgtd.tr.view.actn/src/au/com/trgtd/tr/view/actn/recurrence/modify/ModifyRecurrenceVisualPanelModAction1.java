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

public final class ModifyRecurrenceVisualPanelModAction1 extends JPanel {

    /** Creates new form NewRecurrenceVisualPanel1 */
    public ModifyRecurrenceVisualPanelModAction1(Recurrence recurrence) {
        this.recurrence = recurrence;
        initComponents();
        initPanel();        
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModAction1.class, "change.action.values");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        actionPanel = new au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceActionPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(actionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(actionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 221, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void initPanel() {
        actionPanel.setRecurrence(recurrence);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private au.com.trgtd.tr.view.actn.recurrence.modify.RecurrenceActionPanel actionPanel;
    // End of variables declaration//GEN-END:variables
    private final Recurrence recurrence;
}


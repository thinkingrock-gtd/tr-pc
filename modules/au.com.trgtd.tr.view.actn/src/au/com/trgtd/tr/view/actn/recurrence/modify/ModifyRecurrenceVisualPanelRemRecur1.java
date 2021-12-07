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
import org.openide.util.NbBundle;

public final class ModifyRecurrenceVisualPanelRemRecur1 extends JPanel {

    /** Creates new form NewRecurrenceVisualPanel1 */
    public ModifyRecurrenceVisualPanelRemRecur1() {
        initComponents();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ModifyRecurrenceVisualPanelRemRecur1.class, "confirm.recurrence.removal");
    }

    public void initPanel() {
        String m = NbBundle.getMessage(ModifyRecurrenceVisualPanelRemRecur1.class, "recurrence.removal.explain");
        messageTextArea.setText(m.toString());        
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messageTextArea = new javax.swing.JTextArea();

        messageTextArea.setColumns(20);
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setBorder(null);
        messageTextArea.setFocusable(false);
        messageTextArea.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 558, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 22, Short.MAX_VALUE)
                    .addComponent(messageTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 22, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 22, Short.MAX_VALUE)
                    .addComponent(messageTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 22, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea messageTextArea;
    // End of variables declaration//GEN-END:variables
}


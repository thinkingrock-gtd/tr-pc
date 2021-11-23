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
package au.com.trgtd.tr.view.contexts.dialog;

import javax.swing.JPanel;
import org.openide.util.NbBundle;

/**
 * Context panel.
 *
 * @author Jeremy Moore
 */
public class ContextDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public ContextDialogPanel() {
        initComponents();
    }

    public void setContextName(String name) {
        contextTextField.setText(name);
    }

    public String getContextName() {
        return contextTextField.getText();
    }

    public void setDescription(String desc) {
        descTextField.setText(desc);
    }

    public String getDescription() {
        return descTextField.getText();
    }

    public void focus() {
        requestFocusInWindow();
        contextTextField.requestFocusInWindow();
    }

    // Initialise components
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contextLabel = new javax.swing.JLabel();
        contextTextField = new javax.swing.JTextField();
        descLabel = new javax.swing.JLabel();
        descTextField = new javax.swing.JTextField();

        contextLabel.setBackground(javax.swing.UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        contextLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        contextLabel.setText(org.openide.util.NbBundle.getMessage(ContextDialogPanel.class, "name")); // NOI18N

        descLabel.setBackground(javax.swing.UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        descLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        descLabel.setText(NbBundle.getMessage(ContextDialogPanel.class, "description")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(contextLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(descLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(descTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .add(contextTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(contextTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(contextLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(descLabel)
                    .add(descTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contextLabel;
    private javax.swing.JTextField contextTextField;
    private javax.swing.JLabel descLabel;
    private javax.swing.JTextField descTextField;
    // End of variables declaration//GEN-END:variables
}

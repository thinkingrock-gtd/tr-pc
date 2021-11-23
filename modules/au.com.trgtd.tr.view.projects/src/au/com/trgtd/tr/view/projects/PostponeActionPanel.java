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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.view.projects.PostponeActionAction.Periods;

/**
 * Postpone action date panel.
 * 
 * @author Jeremy Moore
 */
public class PostponeActionPanel extends javax.swing.JPanel {

//    private static Integer lastNumber;
//    private static Periods lastPeriods;
    
    /** Creates new form PostponeActionPanel */
    public PostponeActionPanel() {
        initComponents();
    }

    public int getNumber() {
        return (Integer)nSpinner.getValue();
    }
    
    public Periods getPeriods() {
        if (daysRadioButton.isSelected()) {
            return Periods.DAYS;
        } else if (weeksRadioButton.isSelected()) {
            return Periods.WEEKS;
        } else if (monthsRadioButton.isSelected()) {
            return Periods.MONTHS;
        } else if (yearsRadioButton.isSelected()) {
            return Periods.YEARS;
        } else {
            return null;            
        }
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        periodsButtonGroup = new javax.swing.ButtonGroup();
        postponeLabel = new javax.swing.JLabel();
        nSpinner = new javax.swing.JSpinner();
        daysRadioButton = new javax.swing.JRadioButton();
        weeksRadioButton = new javax.swing.JRadioButton();
        monthsRadioButton = new javax.swing.JRadioButton();
        yearsRadioButton = new javax.swing.JRadioButton();

        postponeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        postponeLabel.setText(org.openide.util.NbBundle.getMessage(PostponeActionPanel.class, "PostponeActionPanel.postponeLabel.text")); // NOI18N

        nSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        periodsButtonGroup.add(daysRadioButton);
        daysRadioButton.setText(org.openide.util.NbBundle.getMessage(PostponeActionPanel.class, "PostponeActionPanel.daysRadioButton.text")); // NOI18N
        daysRadioButton.setPreferredSize(new java.awt.Dimension(100, 24));

        periodsButtonGroup.add(weeksRadioButton);
        weeksRadioButton.setSelected(true);
        weeksRadioButton.setText(org.openide.util.NbBundle.getMessage(PostponeActionPanel.class, "PostponeActionPanel.weeksRadioButton.text")); // NOI18N
        weeksRadioButton.setPreferredSize(new java.awt.Dimension(100, 24));

        periodsButtonGroup.add(monthsRadioButton);
        monthsRadioButton.setText(org.openide.util.NbBundle.getMessage(PostponeActionPanel.class, "PostponeActionPanel.monthsRadioButton.text")); // NOI18N
        monthsRadioButton.setPreferredSize(new java.awt.Dimension(100, 24));

        periodsButtonGroup.add(yearsRadioButton);
        yearsRadioButton.setText(org.openide.util.NbBundle.getMessage(PostponeActionPanel.class, "PostponeActionPanel.yearsRadioButton.text")); // NOI18N
        yearsRadioButton.setPreferredSize(new java.awt.Dimension(100, 24));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(daysRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(weeksRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                    .add(postponeLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 174, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(nSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(monthsRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(yearsRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(postponeLabel)
                    .add(nSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(daysRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(yearsRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(weeksRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(monthsRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton daysRadioButton;
    private javax.swing.JRadioButton monthsRadioButton;
    private javax.swing.JSpinner nSpinner;
    private javax.swing.ButtonGroup periodsButtonGroup;
    private javax.swing.JLabel postponeLabel;
    private javax.swing.JRadioButton weeksRadioButton;
    private javax.swing.JRadioButton yearsRadioButton;
    // End of variables declaration//GEN-END:variables

}

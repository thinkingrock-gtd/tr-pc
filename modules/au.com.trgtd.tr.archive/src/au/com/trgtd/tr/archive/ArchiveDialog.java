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
package au.com.trgtd.tr.archive;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Archive dialog.
 *
 * @author Jeremy Moore
 */
public class ArchiveDialog extends javax.swing.JDialog {
    
    /** Creates new form ArchiveDialog */
    public ArchiveDialog(Frame parent, boolean modal, Date date, String path, boolean doneProjectsOnly) {
        super(parent, modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        dateField.setDate(date);
        pathText.setText(path);
        doneProjectsOnlyCheckBox.setSelected(doneProjectsOnly);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonsPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        archiveButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        pathLabel = new javax.swing.JLabel();
        pathText = new javax.swing.JTextField();
        pathText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent arg0) {
                enableDisableButtons();
            }
            public void insertUpdate(DocumentEvent arg0) {
                enableDisableButtons();
            }
            public void removeUpdate(DocumentEvent arg0) {
                enableDisableButtons();
            }
        });
        pathButton = new javax.swing.JButton();
        pathHintLabel = new javax.swing.JLabel();
        dateHintLabel = new javax.swing.JLabel();
        dateField = new au.com.trgtd.tr.swing.date.field.DateField();
        doneProjectsOnlyCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Resources.getText("archive.title")); // NOI18N

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        cancelButton.setText(Resources.getText("button.cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        buttonsPanel.add(cancelButton);

        archiveButton.setText(Resources.getText("button.archive")); // NOI18N
        archiveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archiveActionPerformed(evt);
            }
        });
        buttonsPanel.add(archiveButton);

        jLabel1.setText("    ");
        buttonsPanel.add(jLabel1);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.PAGE_END);

        centerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dateLabel.setText(Resources.getText("archive.date")); // NOI18N

        pathLabel.setText(Resources.getText("archive.folder")); // NOI18N

        pathButton.setText(Resources.getText("button.browse")); // NOI18N
        pathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        pathHintLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("controlDkShadow"));
        pathHintLabel.setText(NbBundle.getMessage(ArchiveDialog.class, "archive.folder.hint")); // NOI18N

        dateHintLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("controlDkShadow"));
        dateHintLabel.setText(NbBundle.getMessage(ArchiveDialog.class, "archive.date.hint")); // NOI18N

        dateField.setPreferredSize(new java.awt.Dimension(120, 28));
        dateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                datePropertyChange(evt);
            }
        });
        dateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateFocusLost(evt);
            }
        });

        doneProjectsOnlyCheckBox.setText(org.openide.util.NbBundle.getMessage(ArchiveDialog.class, "done.projects.only")); // NOI18N

        org.jdesktop.layout.GroupLayout centerPanelLayout = new org.jdesktop.layout.GroupLayout(centerPanel);
        centerPanel.setLayout(centerPanelLayout);
        centerPanelLayout.setHorizontalGroup(
            centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(centerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(centerPanelLayout.createSequentialGroup()
                        .add(doneProjectsOnlyCheckBox, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(centerPanelLayout.createSequentialGroup()
                        .add(centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(pathLabel)
                            .add(dateLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(centerPanelLayout.createSequentialGroup()
                                .add(pathText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(pathButton))
                            .add(dateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .add(centerPanelLayout.createSequentialGroup()
                        .add(pathHintLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .add(210, 210, 210))
                    .add(dateHintLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)))
        );
        centerPanelLayout.setVerticalGroup(
            centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(centerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(dateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(dateLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dateHintLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(centerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(pathLabel)
                    .add(pathText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(pathButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pathHintLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(doneProjectsOnlyCheckBox)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void datePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_datePropertyChange
        if (evt.getPropertyName().equals("value")) { // No I18N
            enableDisableButtons();
        }
    }//GEN-LAST:event_datePropertyChange
    
    private void dateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFocusLost
        enableDisableButtons();
    }//GEN-LAST:event_dateFocusLost
    
    private void enableDisableButtons() {
        archiveButton.setEnabled(isValidArchiveDate(getArchiveDate())
        && isValidArchivePath(getArchivePath()));
    }
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        archive = false;
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed
    
    private void archiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archiveActionPerformed
        archive = true;
        setVisible(false);
    }//GEN-LAST:event_archiveActionPerformed
    
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(pathText.getText()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = fc.showOpenDialog(p);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                pathText.setText(fc.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) {
                pathText.setText("");
            }
        }
    }//GEN-LAST:event_browseButtonActionPerformed
    
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            archive = false;
            super.setVisible(true);
        } else {
            super.setVisible(false);
            super.dispose();
        }
    }
    
    public boolean isValidArchiveDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(Calendar.getInstance().getTime()); // before today
    }
    
    public Date getArchiveDate() {
        return dateField.getDate();
    }
    
    public boolean isValidArchivePath(String path) {
        if (path == null || path.trim().equals("")) {
            return true;
        }
        try {
            File file = new File(path);
            return file.isDirectory() && file.canWrite();
        } catch (SecurityException ex) {
            return false;
        }
    }
    
    public String getArchivePath() {
        return pathText.getText().trim();
    }
    
    public boolean isDoneProjectsOnly() {
        return doneProjectsOnlyCheckBox.isSelected();
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton archiveButton;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centerPanel;
    private au.com.trgtd.tr.swing.date.field.DateField dateField;
    private javax.swing.JLabel dateHintLabel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JCheckBox doneProjectsOnlyCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton pathButton;
    private javax.swing.JLabel pathHintLabel;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathText;
    // End of variables declaration//GEN-END:variables
    
    public boolean archive;
    
}

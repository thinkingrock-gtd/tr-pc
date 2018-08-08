/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2008 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.appl.prefs;

import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ApplicationPanel extends javax.swing.JPanel {

    private final ApplicationOptionsPanelController controller;

    private static String loc(String key) {
        return NbBundle.getMessage(ApplicationPanel.class, key);
    }
    
    private static final String[] periods = new String[] {
        loc("CTL_Update_every_startup"),
        loc("CTL_Update_every_day"),
        loc("CTL_Update_every_week"),
        loc("CTL_Update_every_fortnight"),
        loc("CTL_Update_every_month"),
        loc("CTL_Update_never")
    };
        
    ApplicationPanel(ApplicationOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }
    
    void load() {
        checkPeriodCombo.setSelectedIndex(ApplicationPrefs.getVersionCheckPeriod());
    }

    void store() {
        ApplicationPrefs.setVersionCheckPeriod(checkPeriodCombo.getSelectedIndex());
    }

    boolean valid() {
        return true;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(ApplicationPanel.class, key);
    }

    private void initForm() {
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        JLabel checkPeriodLabel = new JLabel(getMsg("prefs.version.label"));
        checkPeriodCombo = new JComboBox(new DefaultComboBoxModel(periods));
        checkPeriodCombo.setMaximumRowCount(periods.length);
        JPanel panel = new JPanel(new MigLayout("", "0[]4[]0", "0[]0"));
        panel.add(checkPeriodLabel, "align right");
        panel.add(checkPeriodCombo, "align left, wrap");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private JComboBox checkPeriodCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

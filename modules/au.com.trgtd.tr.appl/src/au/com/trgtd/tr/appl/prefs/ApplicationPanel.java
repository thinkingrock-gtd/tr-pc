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
package au.com.trgtd.tr.appl.prefs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ApplicationPanel extends javax.swing.JPanel {

    private static String msg(String key) {
        return NbBundle.getMessage(ApplicationPanel.class, key);
    }

    private static final String[] PERIODS = new String[]{
        msg("CTL_Update_every_startup"),
        msg("CTL_Update_every_day"),
        msg("CTL_Update_every_week"),
        msg("CTL_Update_every_fortnight"),
        msg("CTL_Update_every_month"),
        msg("CTL_Update_never")
    };

    ApplicationPanel(ApplicationOptionsPanelController controller) {
        setLayout(new BorderLayout());
        add(getView(), BorderLayout.CENTER);
    }

    void load() {
        int index = ApplicationPrefs.getVersionCheckPeriod();       
        boolean used = index != ApplicationPrefs.VERSION_CHECK_PERIOD_NEVER;
        versionCheckBox.setSelected(used);
        versionCheckCombo.setSelectedIndex(index);        
        versionCheckCombo.setEnabled(used);
        
        used = ApplicationPrefs.isInactivityUsed();
        idleCheckBox.setSelected(used);
        inactiveHrsLabel.setEnabled(used);
        inactiveHrsSpinner.setValue(ApplicationPrefs.getInactivityHours());
        inactiveHrsSpinner.setEnabled(used);
        inactiveMinsLabel.setEnabled(used);
        inactiveMinsSpinner.setValue(ApplicationPrefs.getInactivityMinutes());
        inactiveMinsSpinner.setEnabled(used);
    }

    void store() {
        ApplicationPrefs.setVersionCheckPeriod(versionCheckCombo.getSelectedIndex());        
        ApplicationPrefs.setInactivity((Integer)inactiveHrsSpinner.getModel().getValue(), (Integer)inactiveMinsSpinner.getModel().getValue());
    }

    boolean valid() {
        return true;
    }

    private JComponent getView() {
        versionCheckBox = new JCheckBox(msg("prefs.version.label"));
        versionCheckBox.setHorizontalTextPosition(SwingConstants.TRAILING);
        versionCheckBox.addActionListener((ActionEvent evt) -> {
            if (versionCheckBox.isSelected()) {
                versionCheckCombo.setEnabled(true);
                versionCheckCombo.setSelectedIndex(ApplicationPrefs.VERSION_CHECK_PERIOD_STARTUP);
            } else {
                versionCheckCombo.setEnabled(false);
                versionCheckCombo.setSelectedIndex(ApplicationPrefs.VERSION_CHECK_PERIOD_NEVER);
            }
        });
        versionCheckCombo = new JComboBox<>(new DefaultComboBoxModel<>(PERIODS));
        versionCheckCombo.setMaximumRowCount(PERIODS.length);
        idleCheckBox = new JCheckBox(msg("prefs.inactive.quit.label"));
        idleCheckBox.setHorizontalTextPosition(SwingConstants.TRAILING);
        idleCheckBox.addActionListener((ActionEvent evt) -> {
            boolean selected = idleCheckBox.isSelected();
            if (!selected) {
                inactiveHrsSpinner.setValue(0);
                inactiveMinsSpinner.setValue(0);
            }
            inactiveHrsLabel.setEnabled(selected);
            inactiveHrsSpinner.setEnabled(selected);
            inactiveMinsLabel.setEnabled(selected);
            inactiveMinsSpinner.setEnabled(selected);
        });
        inactiveHrsLabel = new JLabel(msg("prefs.inactive.hrs.label"));
        inactiveMinsLabel = new JLabel(msg("prefs.inactive.mins.label"));
        inactiveHrsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        inactiveMinsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));

        JPanel idleQuitPanel = new JPanel(new MigLayout("", "0[]2[]2[]2[]0"/*cols*/, "0[]0"/*rows*/));
        idleQuitPanel.add(inactiveHrsSpinner, "align left");
        idleQuitPanel.add(inactiveHrsLabel, "align left");
        idleQuitPanel.add(inactiveMinsSpinner, "align left");
        idleQuitPanel.add(inactiveMinsLabel, "align left");

        JPanel mainPanel = new JPanel(new MigLayout("", "2[]2[]2"/*cols*/, "2[]8[]2"/*rows*/));
        mainPanel.add(versionCheckBox, "align left");
        mainPanel.add(versionCheckCombo, "align left, wrap");

        mainPanel.add(idleCheckBox, "align left");
        mainPanel.add(idleQuitPanel, "align left, wrap");

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        return scrollPane;
    }

    private JCheckBox versionCheckBox;
    private JComboBox<String> versionCheckCombo;
    private JCheckBox idleCheckBox;
    private JLabel inactiveHrsLabel;
    private JLabel inactiveMinsLabel;
    private JSpinner inactiveHrsSpinner;
    private JSpinner inactiveMinsSpinner;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

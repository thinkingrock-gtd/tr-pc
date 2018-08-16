package au.com.trgtd.tr.appl.prefs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        
        used = ApplicationPrefs.isIdleQuitUsed();
        idleCheckBox.setSelected(used);
        idleHoursLabel.setEnabled(used);
        idleHoursSpinner.setValue(ApplicationPrefs.getIdleQuitHrs());
        idleHoursSpinner.setEnabled(used);
        idleMinsLabel.setEnabled(used);
        idleMinsSpinner.setValue(ApplicationPrefs.getIdleQuitMins());
        idleMinsSpinner.setEnabled(used);
    }

    void store() {
        ApplicationPrefs.setVersionCheckPeriod(versionCheckCombo.getSelectedIndex());        
        ApplicationPrefs.setIdleQuit((Integer)idleHoursSpinner.getModel().getValue(), (Integer)idleMinsSpinner.getModel().getValue());
    }

    boolean valid() {
        return true;
    }

    private JComponent getView() {
        versionCheckBox = new JCheckBox(msg("prefs.version.label"));
        versionCheckBox.setHorizontalTextPosition(SwingConstants.TRAILING);
        versionCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (versionCheckBox.isSelected()) {
                    versionCheckCombo.setEnabled(true);
                    versionCheckCombo.setSelectedIndex(ApplicationPrefs.VERSION_CHECK_PERIOD_STARTUP);
                } else {
                    versionCheckCombo.setEnabled(false);
                    versionCheckCombo.setSelectedIndex(ApplicationPrefs.VERSION_CHECK_PERIOD_NEVER);
                }
            }
        });
        versionCheckCombo = new JComboBox(new DefaultComboBoxModel(PERIODS));
        versionCheckCombo.setMaximumRowCount(PERIODS.length);
        idleCheckBox = new JCheckBox(msg("prefs.idle.quit.label"));
        idleCheckBox.setHorizontalTextPosition(SwingConstants.TRAILING);
        idleCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                boolean selected = idleCheckBox.isSelected();
                if (!selected) {
                    idleHoursSpinner.setValue(0);
                    idleMinsSpinner.setValue(0);                    
                }
                idleHoursLabel.setEnabled(selected);
                idleHoursSpinner.setEnabled(selected);
                idleMinsLabel.setEnabled(selected);
                idleMinsSpinner.setEnabled(selected);
            }
        });
        idleHoursLabel = new JLabel(msg("prefs.idle.hours.label"));
        idleMinsLabel = new JLabel(msg("prefs.idle.mins.label"));
        idleHoursSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        idleMinsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));

        JPanel idleQuitPanel = new JPanel(new MigLayout("", "0[]2[]2[]2[]0"/*cols*/, "0[]0"/*rows*/));
        idleQuitPanel.add(idleHoursSpinner, "align left");
        idleQuitPanel.add(idleHoursLabel, "align left");
        idleQuitPanel.add(idleMinsSpinner, "align left");
        idleQuitPanel.add(idleMinsLabel, "align left");

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
    private JComboBox versionCheckCombo;
    private JCheckBox idleCheckBox;
    private JLabel idleHoursLabel;
    private JLabel idleMinsLabel;
    private JSpinner idleHoursSpinner;
    private JSpinner idleMinsSpinner;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

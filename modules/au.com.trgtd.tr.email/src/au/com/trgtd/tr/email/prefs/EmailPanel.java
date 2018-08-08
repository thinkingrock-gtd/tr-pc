package au.com.trgtd.tr.email.prefs;

import au.com.trgtd.tr.email.task.FetchEmailScheduler;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.util.DateUtils;
import javax.swing.JFileChooser;
import org.openide.windows.WindowManager;
import java.io.File;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * EmailPanel.java
 *
 * @author jmoore
 */
public class EmailPanel extends javax.swing.JPanel {

    private final EmailOptionsPanelController controller;

    EmailPanel(EmailOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }
    
    void load() {
        descrField.setText(EmailPrefs.getEmailDescription());
        addressField.setText(EmailPrefs.getEmailAddress());
        usernameField.setText(EmailPrefs.getEmailUsername());
        passwordField.setText(EmailPrefs.getEmailPassword());        
        serverField.setText(EmailPrefs.getEmailServer());
        portField.setValue(EmailPrefs.getPort());
        sslCheck.setSelected(EmailPrefs.isSSL());
        fetchAtStartupCheck.setSelected(EmailPrefs.isEmailFetchAtStartup());
        fetchScheduleCheck.setSelected(EmailPrefs.isEmailFetchSchedule());
        final long ms = EmailPrefs.getEmailFetchIntervalMS();
        daysField.setValue(DateUtils.getDays(ms));
        hoursField.setValue(DateUtils.getHrs(ms));
        minsField.setValue(DateUtils.getMins(ms));
        fetchKeepCheck.setSelected(EmailPrefs.isKeepOnServer());
        saveAttachmentsCheck.setSelected(EmailPrefs.isSaveAttachments());
        attachmentsFolderField.setText(EmailPrefs.getPathAttachments());
        enableDisableFields();
    }

    void store() {
        EmailPrefs.setEmailDescription(descrField.getText());
        EmailPrefs.setEmailAddress(addressField.getText());
        EmailPrefs.setEmailUsername(usernameField.getText());
        EmailPrefs.setEmailPassword(new String(passwordField.getPassword()));
        EmailPrefs.setEmailServer(serverField.getText());
        try {
            EmailPrefs.setPort(Integer.parseInt(portField.getText()));
        } catch (NumberFormatException ex) {
        }            
        EmailPrefs.setSSL(sslCheck.isSelected());
        EmailPrefs.setEmailFetchAtStartup(fetchAtStartupCheck.isSelected());
        EmailPrefs.setEmailFetchSchedule(fetchScheduleCheck.isSelected());
        long days = getValue(daysField);
        long hrs = getValue(hoursField);
        long mins = getValue(minsField);
        long ms = DateUtils.getMilliseconds(days, hrs, mins, 0);
        EmailPrefs.setEmailFetchIntervalMS(ms);
        FetchEmailScheduler.instance().cancel();
        if (EmailPrefs.isEmailFetchSchedule()) {
            long interval = EmailPrefs.getEmailFetchIntervalMS();
            if (interval > 0) {
                FetchEmailScheduler.instance().schedule(interval, interval);
            }
        }
        EmailPrefs.setFetchKeep(fetchKeepCheck.isSelected());

        EmailPrefs.setSaveAttachments(saveAttachmentsCheck.isSelected());

        if (isValidPath()) {
            EmailPrefs.setPathAttachments(getPath());
        }
    }

    private long getValue(JSpinner spinner) {
        return (spinner.getValue() instanceof Long) ? (Long)spinner.getValue() : 0;
    }

    boolean valid() {
        return isValidPath();
    }    

    private void enableDisableFields() {
        boolean schedule = fetchScheduleCheck.isSelected();
        daysField.setEnabled(schedule);
        hoursField.setEnabled(schedule);
        minsField.setEnabled(schedule);
        attachmentsFolderField.setEnabled(saveAttachmentsCheck.isSelected());
        attachmentsFolderLabel.setEnabled(saveAttachmentsCheck.isSelected());
    }

    private void initForm() {
        initComponents();
        initFields();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        JPanel panel = new JPanel(new MigLayout("", "0[]2[grow]0", "0[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]2[]0"));

        panel.add(descrLabel, "align right");
        panel.add(descrField, "align left, growx, wrap");

        panel.add(addressLabel, "align right");
        panel.add(addressField, "align left, growx, wrap");

        panel.add(serverLabel, "align right");
        panel.add(serverField, "align left, growx, wrap");

        panel.add(usernameLabel, "align right");
        panel.add(usernameField, "align left, growx, wrap");

        panel.add(passwordLabel, "align right");
        panel.add(passwordField, "align left, growx, wrap");

        panel.add(portLabel, "align right");
        panel.add(portField, "align left, span, split 2");
        panel.add(sslCheck,  "align right, wrap");

        panel.add(fetchAtStartupCheck, "align left, span, wrap");

        panel.add(fetchScheduleCheck,  "align left, span, wrap");

        panel.add(daysLabel,  "align right");
        panel.add(daysField,  "align left, span, split 5");
        panel.add(hoursLabel, "align left");
        panel.add(hoursField, "align left");
        panel.add(minsLabel,  "align left");
        panel.add(minsField,  "align left, wrap");

        panel.add(fetchKeepCheck,  "align right, wrap");

        panel.add(saveAttachmentsCheck,  "align left, wrap");

        panel.add(attachmentsFolderLabel,  "align right");
        panel.add(attachmentsFolderField,  "align left, split 2, growx 100");
        panel.add(browseButton, "align left, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(EmailPanel.class, key);
    }

    private void initFields() {
        descrLabel = new JLabel(getMsg("email-account-descr"));
        addressLabel = new JLabel(getMsg("email-account-address"));
        serverLabel = new JLabel(getMsg("email-account-server"));
        usernameLabel = new JLabel(getMsg("email-account-username"));
        passwordLabel = new JLabel(getMsg("email-account-password"));
        portLabel = new JLabel(getMsg("email-account-port"));

        portField = new JFormattedTextField();
        sslCheck = new JCheckBox(getMsg("email-account-ssl"));
        passwordField = new JPasswordField();
        usernameField = new JTextField();
        serverField = new JTextField();
        addressField = new JTextField();
        descrField = new JTextField();
        fetchAtStartupCheck = new JCheckBox(getMsg("fetch.at.startup"));
        fetchScheduleCheck = new JCheckBox(getMsg("fetch.schedule"));
        fetchScheduleCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDisableFields();
            }
        });
        daysField = new JSpinner();
        daysField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(99L), Long.valueOf(1L)));
        daysLabel = new JLabel(getMsg("fetch.days"));
        hoursLabel = new JLabel(getMsg("fetch.hours"));
        hoursField = new JSpinner();
        hoursField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(23L), Long.valueOf(1L)));
        minsLabel = new JLabel(getMsg("fetch.mins"));
        minsField = new JSpinner();
        minsField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        fetchKeepCheck = new JCheckBox(getMsg("fetch.keep"));

        saveAttachmentsCheck = new JCheckBox(getMsg("save.attachments"));
        saveAttachmentsCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enableDisableFields();
            }
        });
        attachmentsFolderLabel = new JLabel(getMsg("path.attachments"));
        attachmentsFolderField = new JTextField();
        attachmentsFolderField.getDocument().addDocumentListener(new FolderDocumentListener());

        browseButton = new JButton(getMsg("browse"));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                browse();
            }
        });
    }

    // Document listener for the folder text field.
    private class FolderDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            EmailPanel.this.controller.changed();
        }
        public void removeUpdate(DocumentEvent e) {
            EmailPanel.this.controller.changed();
        }
        public void changedUpdate(DocumentEvent e) {
            EmailPanel.this.controller.changed();
        }
    }

    private boolean isValidPath() {
        String path = getPath();
        return path.length() == 0 || (new File(path)).isDirectory();
    }

    private String getPath() {
        String path = attachmentsFolderField.getText().trim();
        while (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1).trim();
        }
        return path;
    }

    private void browse() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(attachmentsFolderField.getText()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = fc.showOpenDialog(p);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                attachmentsFolderField.setText(fc.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) {
                attachmentsFolderField.setText("");
            }
        }
    }

    private JLabel addressLabel;
    private JTextField addressField;
    private JLabel daysLabel;
    private JSpinner daysField;
    private JLabel descrLabel;
    private JTextField descrField;
    private JCheckBox fetchAtStartupCheck;
    private JCheckBox fetchScheduleCheck;
    private JLabel hoursLabel;
    private JSpinner hoursField;
    private JLabel minsLabel;
    private JSpinner minsField;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JFormattedTextField portField;
    private JLabel portLabel;
    private JLabel serverLabel;
    private JTextField serverField;
    private JCheckBox sslCheck;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JCheckBox fetchKeepCheck;

    private JCheckBox saveAttachmentsCheck;
    private JLabel attachmentsFolderLabel;
    private JTextField attachmentsFolderField;
    private JButton browseButton;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

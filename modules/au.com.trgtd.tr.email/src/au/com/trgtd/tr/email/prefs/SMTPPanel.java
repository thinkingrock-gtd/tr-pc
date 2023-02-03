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
package au.com.trgtd.tr.email.prefs;

import au.com.trgtd.tr.email.prefs.EmailPrefs.DelegateEmailMethod;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.NbBundle;

/**
 * SMTP preferences panel.
 */
public class SMTPPanel extends javax.swing.JPanel {

    private final SMTPOptionsPanelController controller;
    private final JLabel hostLabel;
    private final JTextField hostField;
    private final JLabel portLabel;
    private final JFormattedTextField portField;
    private final JLabel userLabel;
    private final JTextField userField;
    private final JLabel passLabel;
    private final JPasswordField passField;
    private final JCheckBox sslCheck;
    private final JPanel panel;
    private final JButton testBttn;
    private final JLabel testIcon;
    private final ImageIcon checkIcon;
    private final ImageIcon crossIcon;
    private final JLabel methodLabel;
    private final ButtonGroup methodButtonGroup;
    private final JRadioButton emailClientRadio;
    private final JRadioButton directAllRadio;
    private final JRadioButton directTeamRadio;

    SMTPPanel(SMTPOptionsPanelController controller) {
        this.controller = controller;
        this.hostLabel = new JLabel(getMsg("smtp_host"));
        this.hostField = new JTextField();
        this.userLabel = new JLabel(getMsg("smtp_user"));
        this.userField = new JTextField();
        this.passLabel = new JLabel(getMsg("smtp_pass"));
        this.passField = new JPasswordField();
        this.portLabel = new JLabel(getMsg("smtp_port"));
        this.portField = new JFormattedTextField();
        this.sslCheck = new JCheckBox(getMsg("smtp_ssl"));
        this.testBttn = new JButton(getMsg("smtp_test"));
        this.testIcon = new JLabel("");
        this.crossIcon = new ImageIcon(getClass().getResource("/au/com/trgtd/tr/email/prefs/cross.png"));
        this.checkIcon = new ImageIcon(getClass().getResource("/au/com/trgtd/tr/email/prefs/check.png"));
        this.methodLabel = new JLabel(getMsg("delegate.email.method"));
        this.emailClientRadio = new JRadioButton(getMsg("delegate.email.client"));
        this.directAllRadio = new JRadioButton(getMsg("delegate.email.direct.all"));
        this.directTeamRadio = new JRadioButton(getMsg("delegate.email.direct.team"));
        this.methodButtonGroup = new ButtonGroup();        
        this.methodButtonGroup.add(emailClientRadio);
        this.methodButtonGroup.add(directAllRadio);
        this.methodButtonGroup.add(directTeamRadio);
        this.panel = new JPanel(new MigLayout("", "0[]2[grow]0", "0[]2[]2[]2[]2[]8[]0"));
        initComponents();
    }

    private class TextDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            changed();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            changed();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            changed();
        }
    }

    private class CheckActionListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            changed();
        }
    }

    private void initComponents() {
        hostField.getDocument().addDocumentListener(new TextDocumentListener());
        userField.getDocument().addDocumentListener(new TextDocumentListener());
        passField.getDocument().addDocumentListener(new TextDocumentListener());
        portField.getDocument().addDocumentListener(new TextDocumentListener());
        sslCheck.addActionListener(new CheckActionListener());
        testBttn.addActionListener((ActionEvent e) -> {
            testConnection();
        });
        panel.setLayout(new MigLayout("", "0[]2[grow]0", "0[]2[]2[]12[]0"));
        panel.add(hostLabel, "align right");
        panel.add(hostField, "align left, growx, wrap");
        panel.add(userLabel, "align right");
        panel.add(userField, "align left, growx, wrap");
        panel.add(passLabel, "align right");
        panel.add(passField, "align left, growx, wrap");
        panel.add(portLabel, "align right");
        panel.add(portField, "align left, span, split 2");
        panel.add(sslCheck, "align right, wrap");
        panel.add(testBttn, "skip, split 2, align left");
        panel.add(testIcon, "align left, wrap");
        panel.add(methodLabel, "align left");
        panel.add(emailClientRadio, "split 3, align left");
        panel.add(directAllRadio, "align left");
        panel.add(directTeamRadio, "align left, wrap");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void changed() {
        hideTestIndicator();
        controller.changed();
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(SMTPPanel.class, key);
    }

    void load() {
        hostField.setText(EmailPrefs.getSMTPHost());
        userField.setText(EmailPrefs.getSMTPUser());
        passField.setText(EmailPrefs.getSMTPPass());
        portField.setValue(EmailPrefs.getSMTPPort());
        sslCheck.setSelected(EmailPrefs.isSMTPSSL());
        switch (EmailPrefs.getDelegateEmailMethod()) {
            case DIRECT_ALL:
                directAllRadio.setSelected(true);
                break;
            case DIRECT_TEAM:
                directTeamRadio.setSelected(true);
                break;
            default:
                emailClientRadio.setSelected(true);
        }
    }

    void store() {
        EmailPrefs.setSMTPHost(hostField.getText());
        EmailPrefs.setSMTPUser(userField.getText());
        EmailPrefs.setSMTPPass(new String(passField.getPassword()));
        EmailPrefs.setSMTPSSL(sslCheck.isSelected());
        try {
            EmailPrefs.setSMTPPort(Integer.parseInt(portField.getText()));
        } catch (NumberFormatException nfe) {
        }
        if (directAllRadio.isSelected()) {
            EmailPrefs.setDelegateEmailProcess(DelegateEmailMethod.DIRECT_ALL);
        } else if (directTeamRadio.isSelected()) {
            EmailPrefs.setDelegateEmailProcess(DelegateEmailMethod.DIRECT_TEAM);            
        } else {
            EmailPrefs.setDelegateEmailProcess(DelegateEmailMethod.EMAIL_CLIENT);            
        }
    }

    boolean valid() {
        return true;
    }

    private void testConnection() {
        String host = hostField.getText();
        if (StringUtils.isBlank(host)) {
            showTestFailure();
            return;
        }

        int port = 0;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException nfe) {
        }
        if (port < 1) {
            showTestFailure();
            return;
        }

        String user = userField.getText();
        String pass = new String(passField.getPassword());
        boolean ssl = sslCheck.isSelected();

        if (SMTPUtils.verify(host, port, user, pass, ssl)) {
            showTestSuccess();
        } else {
            showTestFailure();
        }
    }

    private void hideTestIndicator() {
        testIcon.setVisible(false);
    }

    private void showTestSuccess() {
        testIcon.setIcon(checkIcon);
        testIcon.setVisible(true);
    }

    private void showTestFailure() {
        testIcon.setIcon(crossIcon);
        testIcon.setVisible(true);
    }

}

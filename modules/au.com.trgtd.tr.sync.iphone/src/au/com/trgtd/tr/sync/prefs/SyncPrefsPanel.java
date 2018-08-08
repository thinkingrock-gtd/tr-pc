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
 * Portions Copyright 2006-2011 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.sync.prefs;

import au.com.trgtd.tr.sync.iphone.SyncPrefs;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.windows.WindowManager;

final class SyncPrefsPanel extends JPanel {

    private final SyncOptionsPanelController controller;

    SyncPrefsPanel(SyncOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    void load() {
        final int method = SyncPrefs.getMethod();
        wifiRadio.setSelected(method == SyncPrefs.SYNC_METHOD_WIFI);
        dBoxRadio.setSelected(method == SyncPrefs.SYNC_METHOD_DBOX);
        dboxFolderField.setText(SyncPrefs.getDropBoxPath());
        enableDisableFields();
    }

    void store() {
        if (valid()) {
            if (dBoxRadio.isSelected()) {
                SyncPrefs.setMethod(SyncPrefs.SYNC_METHOD_DBOX);
                SyncPrefs.setDropBoxPath(tidyPath(dboxFolderField.getText()));
            } else {
                SyncPrefs.setMethod(SyncPrefs.SYNC_METHOD_WIFI);
            }
        }
    }

    private void enableDisableFields() {
        boolean isDropBox = dBoxRadio.isSelected();
        dboxFolderLabel.setEnabled(isDropBox);
        dboxFolderField.setEnabled(isDropBox);
        dboxBrowseButton.setEnabled(isDropBox);
    }

    boolean valid() {
        if (dBoxRadio.isSelected()) {
            String path = tidyPath(dboxFolderField.getText());
            if (path.length() > 0 && !(new File(path)).isDirectory()) {
                return false;
            }
        }
        return true;
    }

    private String tidyPath(String path) {
        path = path.trim();
        while (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1).trim();
        }
        return path;
    }

    private void initForm() {
        setLayout(new BorderLayout());
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {

        methodLabel = new JLabel(Resources.getText("sync.method"));

        wifiRadio = new JRadioButton(Resources.getText("method.wifi"));
        wifiRadio.addActionListener(changeListener);

        dBoxRadio = new JRadioButton(Resources.getText("method.dbox"));
        dBoxRadio.addActionListener(changeListener);

        methodButtonGroup = new ButtonGroup();
        methodButtonGroup.add(wifiRadio);
        methodButtonGroup.add(dBoxRadio);

        dboxFolderLabel = new JLabel(Resources.getText("dbox.folder"));
        dboxFolderField = new JTextField();
        dboxFolderField.getDocument().addDocumentListener(docChangeListener);

        dboxBrowseButton = new JButton(Resources.getText("dbox.browse"));
        dboxBrowseButton.addActionListener(browseListener);

        JPanel panel = new JPanel(new MigLayout("", "6[]2[]2[grow]2[]6", "6[]6[]6[]6"));

        panel.add(methodLabel, "align left, wrap");
        panel.add(wifiRadio, "align left, wrap");

        panel.add(dBoxRadio, "align left");
        panel.add(dboxFolderLabel, "align left");
        panel.add(dboxFolderField, "align left, growx");
        panel.add(dboxBrowseButton, "align right, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        return scrollPane;
    }

    private void changed() {
        controller.changed();
        enableDisableFields();
    }

    private final ActionListener changeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            changed();
        }
    };

    private final DocumentListener docChangeListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {changed();}
        @Override
        public void removeUpdate(DocumentEvent e) {changed();}
        @Override
        public void changedUpdate(DocumentEvent e) {changed();}
    };

    private final ActionListener browseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setSelectedFile(new File(dboxFolderField.getText()));
            Component p = WindowManager.getDefault().getMainWindow();
            int returnVal = fc.showOpenDialog(p);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    dboxFolderField.setText(fc.getSelectedFile().getCanonicalPath());
                } catch (Exception ex) {
                    dboxFolderField.setText("");
                }
            }
        }
    };

    private JLabel dboxFolderLabel;
    private JTextField dboxFolderField;
    private JButton dboxBrowseButton;
    private JLabel methodLabel;
    private ButtonGroup methodButtonGroup;
    private JRadioButton wifiRadio;
    private JRadioButton dBoxRadio;

//    private void initComponents() {
//
//        setLayout(new java.awt.BorderLayout());
//    }

}

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

    private final ActionListener changeListener = (ActionEvent e) -> {
        changed();
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

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
package au.com.trgtd.tr.sync.device.prefs;

import static au.com.trgtd.tr.sync.device.SyncUtils.isEmpty;
import au.com.trgtd.tr.sync.device.usb.ADBProvided;
import au.com.trgtd.tr.sync.device.usb.ADBController;
import au.com.trgtd.tr.util.UtilsFile;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.windows.WindowManager;

final class SyncPrefsPanel extends JPanel {

    private static final int PORT_USB_DEFAULT = SyncPrefs.DEF_USB_PORT;
    private static final int PORT_WIFI_DEFAULT = SyncPrefs.DEF_WIFI_PORT;
    private static final int PORT_MIN_DYNAMIC = SyncPrefs.PORT_MIN_DYNAMIC;
    private static final int PORT_MAX_DYNAMIC = SyncPrefs.PORT_MAX_DYNAMIC;
    private static final int PORT_NUMBER_STEP = 1;
    
    private final SyncOptionsPanelController controller;

    SyncPrefsPanel(SyncOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    void load() {
        syncCheckBox.setSelected(SyncPrefs.isSyncAndroid());
        
        dbxCheckBox.setSelected(SyncPrefs.isSyncDbx());
        String dbxPath = SyncPrefs.getDbxPath();        
        dbxPathField.setText(dbxPath == null ? "" : dbxPath);
        
        wifiCheckBox.setSelected(SyncPrefs.isSyncWiFi());
        wifiPortSpinner.setValue(SyncPrefs.getWiFiPort());

        usbCheckBox.setSelected(SyncPrefs.isSyncUSB());
        usbPortSpinner.setValue(SyncPrefs.getUSBPort());
        String adbPath = SyncPrefs.getADBPath();        
        if (isEmpty(adbPath)) {
            adbPath = getProvidedADBPath();
        }
        adbField.setText(adbPath);

        enableDisableFields();
    }
    
    private String getProvidedADBPath() {
        String path = ADBProvided.getProvidedADBPath();        
        return path == null ? "" : path;
    }

    void store() {
        if (valid()) {
            SyncPrefs.setSyncAndroid(syncCheckBox.isSelected());
            SyncPrefs.setSyncDbx(dbxCheckBox.isSelected());
            SyncPrefs.setDbxPath(tidyPath(dbxPathField.getText()));
            SyncPrefs.setSyncWiFi(wifiCheckBox.isSelected());
            SyncPrefs.setWiFiPort((Integer)wifiPortSpinner.getValue());
            SyncPrefs.setSyncUSB(usbCheckBox.isSelected());
            SyncPrefs.setUSBPort((Integer)usbPortSpinner.getValue());
            SyncPrefs.setADBPath(tidyPath(adbField.getText()));
            ADBController.instance().onPrefsChange(SyncPrefsADB.getCurrent());
        }
    }

    private void enableDisableFields() {
        boolean sync = syncCheckBox.isSelected();

        dbxCheckBox.setEnabled(sync);
        boolean dbx = sync && dbxCheckBox.isSelected();
        dbxPathLabel.setEnabled(dbx);
        dbxPathField.setEnabled(dbx);
        dbxPathButton.setEnabled(dbx);            
        
        wifiCheckBox.setEnabled(sync);
        boolean wifi = sync && wifiCheckBox.isSelected();
        wifiPortLabel.setEnabled(wifi);
        wifiPortSpinner.setEnabled(wifi);

        usbCheckBox.setEnabled(sync);
        boolean usb  = sync && usbCheckBox.isSelected();
        usbPortLabel.setEnabled(usb);
        usbPortSpinner.setEnabled(usb);
        adbLabel.setEnabled(usb);
        adbField.setEnabled(usb);
        adbButton.setEnabled(usb);            
    }

    boolean valid() {

        msgLabel.setText("");
        
        if (!syncCheckBox.isSelected()) {
            return true;
        }
        
        if (dbxCheckBox.isSelected()) {
            String dbxPath = tidyPath(dbxPathField.getText());
            if (dbxPath == null || dbxPath.length() == 0) {
                msgLabel.setText("DropBox root path is not entered.");
                return false;
            } else {
                File dbxDir = new File(dbxPath);
                if (!dbxDir.isDirectory()) {
                    msgLabel.setText("DropBox root path is not a directory.");
                    return false;
                }            
            }            
        }
        
        boolean useWifi = wifiCheckBox.isSelected();
        if (useWifi) {
            Integer wifiPort = (Integer)wifiPortSpinner.getValue();
            if (wifiPort == null || wifiPort < PORT_MIN_DYNAMIC || wifiPort > PORT_MAX_DYNAMIC) {
                msgLabel.setText("WiFi port must be between " + PORT_MIN_DYNAMIC + " and " + PORT_MAX_DYNAMIC);
            }            
        }
        
        if (usbCheckBox.isSelected()) {
            Integer usbPort = (Integer)usbPortSpinner.getValue();
            if (usbPort == null || usbPort < PORT_MIN_DYNAMIC || usbPort > PORT_MAX_DYNAMIC) {
                msgLabel.setText("USB port must be between " + PORT_MIN_DYNAMIC + " and " + PORT_MAX_DYNAMIC);
            }
            String abdPath = tidyPath(adbField.getText());
            if (abdPath != null && abdPath.length() > 0) {
                File adbFile = new File(abdPath);
                if (!adbFile.isFile()) {
                    msgLabel.setText("ADB program is not a file.");
                    return false;
                }            
                // if ADB program is not executable, try to set it executable
                if (!adbFile.canExecute()) {
                    UtilsFile.setExecutable(adbFile);
                }
                if (!adbFile.canExecute()) {
                    msgLabel.setText("ADB program can not be executed. See Help.");
                    return false;
                }
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

    private SpinnerNumberModel getPortNumberModel(int value) {
        return new SpinnerNumberModel(value, PORT_MIN_DYNAMIC, PORT_MAX_DYNAMIC, PORT_NUMBER_STEP);
    }
    
    private JComponent getView() {
        syncCheckBox = new JCheckBox(Resources.getText("sync.android"));
        syncCheckBox.addItemListener((ItemEvent e) -> {
            changed();
        });        

        // DropBox
        dbxCheckBox = new JCheckBox(Resources.getText("dbx.use"));
        dbxCheckBox.addItemListener((ItemEvent e) -> {
            changed();
        });                
        dbxPathLabel = new JLabel(Resources.getText("dbx.path"));
        dbxPathField = new JTextField();
        dbxPathField.getDocument().addDocumentListener(dbxDocChangeListener);
        dbxPathButton = new JButton(Resources.getText("dbx.browse"));
        dbxPathButton.addActionListener(dbxBrowseListener);
        // END DropBox
        
        
        
        wifiCheckBox = new JCheckBox(Resources.getText("wifi.use"));
        wifiCheckBox.addItemListener((ItemEvent e) -> {
            changed();
        });                
        wifiPortLabel = new JLabel(Resources.getText("wifi.port"));
        wifiPortSpinner = new JSpinner(getPortNumberModel(PORT_WIFI_DEFAULT));
        // added for problem with Spanish format number
        wifiPortSpinner.setEditor(new JSpinner.NumberEditor(wifiPortSpinner, "#"));
        
        usbCheckBox = new JCheckBox(Resources.getText("usb.use"));
        usbCheckBox.addItemListener((ItemEvent e) -> {
            changed();
        });        
        
        usbPortLabel = new JLabel(Resources.getText("usb.port"));
        usbPortSpinner = new JSpinner(getPortNumberModel(PORT_USB_DEFAULT));        
        // added for problem with Spanish format number
        usbPortSpinner.setEditor(new JSpinner.NumberEditor(usbPortSpinner, "#"));
        
        adbLabel = new JLabel(Resources.getText("adb.path"));
        adbField = new JTextField();
        adbField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (adbField.getText().trim().length() == 0) {
                    adbField.setText(getProvidedADBPath());
                }
            }
        });
        adbField.getDocument().addDocumentListener(adbDocChangeListener);
        adbButton = new JButton(Resources.getText("adb.browse"));
        adbButton.addActionListener(adbBrowseListener);
        msgLabel = new JLabel();
        msgLabel.setForeground(Color.red);

        JPanel panel = new JPanel(new MigLayout("", "6[]4[grow]4[]6", "6[]8[]8[]8[]8[]8[]8[]4[]12[]6"));
        
        panel.add(syncCheckBox,     "align left, spanx 3, wrap");

        panel.add(dbxCheckBox,      "align left, gapleft 10, spanx 3, wrap");
        panel.add(dbxPathLabel,     "align left, gapleft 20");
        panel.add(dbxPathField,     "align left, growx");
        panel.add(dbxPathButton,    "align left, wrap");
        
        panel.add(wifiCheckBox,     "align left, gapleft 10, spanx 3, wrap");
        panel.add(wifiPortLabel,    "align left, gapleft 20");
        panel.add(wifiPortSpinner,  "align left, growx 0, wrap");
        
        panel.add(usbCheckBox,      "align left, gapleft 10, spanx 3, wrap");
        panel.add(usbPortLabel,     "align left, gapleft 20");
        panel.add(usbPortSpinner,   "align left, growx 0, wrap");
        panel.add(adbLabel,         "align left, gapleft 20");
        panel.add(adbField,         "align left, growx");
        panel.add(adbButton,        "align left, wrap");
        
        panel.add(msgLabel,         "align left, gapleft 10, spanx 3, wrap");
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        return scrollPane;
    }

    private void changed() {
        controller.changed();
        enableDisableFields();
    }
    
    private final DocumentListener adbDocChangeListener = new DocumentListener() {
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
    };

    private final DocumentListener dbxDocChangeListener = new DocumentListener() {
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
    };
    
    private final ActionListener adbBrowseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setSelectedFile(new File(adbField.getText()));
            Component p = WindowManager.getDefault().getMainWindow();
            int returnVal = fc.showOpenDialog(p);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    adbField.setText(fc.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    adbField.setText("");
                }
            }
        }
    };

    private final ActionListener dbxBrowseListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setSelectedFile(new File(dbxPathField.getText()));
            Component p = WindowManager.getDefault().getMainWindow();
            int returnVal = fc.showOpenDialog(p);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    dbxPathField.setText(fc.getSelectedFile().getCanonicalPath());
                } catch (IOException ex) {
                    dbxPathField.setText("");
                }
            }
        }
    };
    
    private JCheckBox syncCheckBox;

    private JCheckBox dbxCheckBox;
    private JLabel dbxPathLabel;
    private JTextField dbxPathField;
    private JButton dbxPathButton;    
    
    private JCheckBox wifiCheckBox;
    private JLabel wifiPortLabel;
    private JSpinner wifiPortSpinner;
    
    private JCheckBox usbCheckBox;
    private JLabel usbPortLabel;
    private JSpinner usbPortSpinner;
    private JLabel adbLabel;
    private JTextField adbField;
    private JButton adbButton;    

    private JLabel msgLabel;
}

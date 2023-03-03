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
package au.com.trgtd.tr.prefs.data;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.util.DateUtils;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JSeparator;
//import au.com.trgtd.tr.view.ViewUtils;

final class DataPrefsPanel extends JPanel implements ChangeListener {

    private final DataOptionsPanelController controller;

    DataPrefsPanel(DataOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    void load() {
        final long ms = DataPrefs.getAutoSaveIntervalMS();
        hoursField.setValue(DateUtils.getHrs(ms));
        minsField.setValue(DateUtils.getMins(ms));
        secsField.setValue(DateUtils.getSecs(ms));
        recoveryFolderText.setText(DataPrefs.getRecoveryPath());
        recoveryKeepAllRadio.setSelected(DataPrefs.isKeepAllRecoveryFiles());
        recoveryKeepNbrRadio.setSelected(!DataPrefs.isKeepAllRecoveryFiles());
        recoveryKeepNbrSpinner.setValue(DataPrefs.getKeepNbrRecoveryFiles());
        backupCombo.setSelectedItem(getInterval(DataPrefs.getBackupIntervalDays()));
        backupFolderText.setText(DataPrefs.getBackupPath());
        backupKeepAllRadio.setSelected(DataPrefs.isKeepAllBackupFiles());
        backupKeepNbrRadio.setSelected(!DataPrefs.isKeepAllBackupFiles());
        backupKeepNbrSpinner.setValue(DataPrefs.getKeepNbrBackupFiles());
        enableDisableFields();
    }

    void store() {
        if (valid()) {
            DataPrefs.setAutoSaveIntervalMS(getAutoSaveMS());
            DataPrefs.setRecoveryPath(tidyPath(recoveryFolderText.getText()));
            DataPrefs.setKeepAllRecoveryFiles(recoveryKeepAllRadio.isSelected());
            DataPrefs.setKeepNbrRecoveryFiles((Integer) recoveryKeepNbrSpinner.getValue());
            Item intervalItem = (Item) backupCombo.getSelectedItem();
            if (intervalItem != null) {
                int value = DataPrefs.getBackupIntervalDays();
                if (intervalItem.value != value) {
                    DataPrefs.setBackupIntervalDays(intervalItem.value);
                }
            }
            DataPrefs.setBackupPath(tidyPath(backupFolderText.getText()));
            DataPrefs.setKeepAllBackupFiles(backupKeepAllRadio.isSelected());
            DataPrefs.setKeepNbrBackupFiles((Integer)backupKeepNbrSpinner.getValue());
        }
    }

    private void enableDisableFields() {
        recoveryKeepNbrSpinner.setEnabled(!recoveryKeepAllRadio.isSelected());
        Item item = (Item)backupCombo.getSelectedItem();
        boolean backup = (item == null ? false : item.value != Integer.MAX_VALUE);
        backupFolderLabel.setEnabled(backup);
        backupFolderText.setEnabled(backup);
        backupBrowseButton.setEnabled(backup);
        backupKeepAllRadio.setEnabled(backup);
        backupKeepNbrRadio.setEnabled(backup);
        backupKeepNbrSpinner.setEnabled(backup && !backupKeepAllRadio.isSelected());
    }

    private long getValue(JSpinner spinner) {
        return (spinner.getValue() instanceof Long) ? (Long)spinner.getValue() : 0;
    }

    private long getAutoSaveMS() {
        long hrs = getValue(hoursField);
        long mins = getValue(minsField);
        long secs = getValue(secsField);
        return DateUtils.getMilliseconds(0, hrs, mins, secs);
    }

    boolean valid() {
        if (getAutoSaveMS() < (30 * DateUtils.MS_PER_SEC)) {
            return false;
        }
        String path = tidyPath(recoveryFolderText.getText());
        if (path.length() > 0 && !(new File(path)).isDirectory()) {
            return false;
        }
        path = tidyPath(backupFolderText.getText());
        if (path.length() > 0 && !(new File(path)).isDirectory()) {
            return false;
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
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        autosaveLabel = new JLabel(Resources.getText("autosave"));
        hoursLabel = new JLabel(Resources.getText("hours"));
        hoursField = new JSpinner();
        hoursField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(23L), Long.valueOf(1L)));
        hoursField.addChangeListener(this);
        minsLabel = new JLabel(Resources.getText("minutes"));
        minsField = new JSpinner();
        minsField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        minsField.addChangeListener(this);
        secsLabel = new JLabel(Resources.getText("seconds"));
        secsField = new JSpinner();
        secsField.setModel(new SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(59L), Long.valueOf(1L)));
        secsField.addChangeListener(this);
        DocumentListener docChangeListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {stateChanged(null);}
            public void removeUpdate(DocumentEvent e) {stateChanged(null);}
            public void changedUpdate(DocumentEvent e) {stateChanged(null);}
        };
        ActionListener actionChangeListener = (ActionEvent e) -> {
            stateChanged(null);
        };
        recoveryFolderLabel = new JLabel(Resources.getText("CTL_Folder"));
        recoveryFolderText = new JTextField();
        recoveryFolderText.getDocument().addDocumentListener(docChangeListener);
        recoveryBrowseButton = new JButton(Resources.getText("CTL_Browse"));
        recoveryBrowseButton.addActionListener((ActionEvent evt) -> {
            browse();
        });
        recoveryKeepAllRadio = new JRadioButton(Resources.getText("recovery.keep.all.files"));
        recoveryKeepAllRadio.addActionListener(actionChangeListener);
        recoveryKeepNbrRadio = new JRadioButton(Resources.getText("recovery.keep.nbr.files"));
        recoveryKeepNbrRadio.addActionListener(actionChangeListener);
        recoveryKeepNbrSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
        recoveryButtonGroup = new ButtonGroup();
        recoveryButtonGroup.add(recoveryKeepAllRadio);
        recoveryButtonGroup.add(recoveryKeepNbrRadio);

        backupLabel = new JLabel(Resources.getText("backup.period"));
        backupCombo = new TRComboBox<>(new DefaultComboBoxModel<>(intervals));
        backupCombo.addActionListener(actionChangeListener);
        backupFolderLabel = new JLabel(Resources.getText("backup.folder"));
        backupFolderText = new JTextField();
        backupFolderText.getDocument().addDocumentListener(docChangeListener);
        backupBrowseButton = new JButton(Resources.getText("CTL_Browse"));
        backupBrowseButton.addActionListener((ActionEvent evt) -> {
            backupBrowse();
        });
        backupKeepAllRadio = new JRadioButton(Resources.getText("backup.keep.all.files"));
        backupKeepAllRadio.addActionListener(actionChangeListener);
        backupKeepNbrRadio = new JRadioButton(Resources.getText("backup.keep.nbr.files"));
        backupKeepNbrRadio.addActionListener(actionChangeListener);
        backupKeepNbrSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 100, 1));
        backupButtonGroup = new ButtonGroup();
        backupButtonGroup.add(backupKeepAllRadio);
        backupButtonGroup.add(backupKeepNbrRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[]2[grow]2[]0", "4[]4[]10[]4[]2[]0[]10[]4[]2[]2[]0[]0"));
        
//      ViewUtils.addSeparator(panel, Resources.getText("autosave.title"));
        addSeparator(panel, Resources.getText("autosave.title"));
        
        panel.add(autosaveLabel, "align left, span, split 7");
        panel.add(hoursField, "align left");
        panel.add(hoursLabel, "align left");
        panel.add(minsField, "align left");
        panel.add(minsLabel, "align left");
        panel.add(secsField, "align left");
        panel.add(secsLabel, "align left, wrap");
        
        
//      ViewUtils.addSeparator(panel, Resources.getText("recovery.title"));
        addSeparator(panel, Resources.getText("recovery.title"));
        
        panel.add(recoveryFolderLabel, "align left, spanx, split 3");
        panel.add(recoveryFolderText, "align left, growx");
        panel.add(recoveryBrowseButton, "align left, wrap");
        panel.add(recoveryKeepAllRadio, "align left, wrap");
        panel.add(recoveryKeepNbrRadio, "align left, spanx, split 2");
        panel.add(recoveryKeepNbrSpinner, "align left, growx 0, wrap");
        
//      ViewUtils.addSeparator(panel, Resources.getText("backup.title"));
        addSeparator(panel, Resources.getText("backup.title"));
        
        panel.add(backupLabel, "align left, span, split 2");
        panel.add(backupCombo, "align left, wrap");
        panel.add(backupFolderLabel, "align left, spanx, split 3");
        panel.add(backupFolderText, "align left, growx");
        panel.add(backupBrowseButton, "align left, wrap");
        panel.add(backupKeepAllRadio, "align left, wrap");
        panel.add(backupKeepNbrRadio, "align left, spanx, split 2");
        panel.add(backupKeepNbrSpinner, "align left, wrap");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        return scrollPane;
    }

    private void browse() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(recoveryFolderText.getText()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = fc.showOpenDialog(p);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                recoveryFolderText.setText(fc.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) {
                recoveryFolderText.setText("");
            }
        }
    }

    
    private static final Color COLOR_SEP_TEXT = Color.BLACK;
    private static final Font FONT_BOLD = (new JLabel()).getFont().deriveFont(Font.BOLD);
    
    private static void addSeparator(JPanel panel) {
        JSeparator sep = new JSeparator();
        panel.add(sep, "gaptop 1, gapbottom 1, aligny center, span, growx, wrap");
    }
    private static void addSeparator(JPanel panel, String text) {
        addSeparator(panel, new JLabel(text));
    }
    private static void addSeparator(JPanel panel, JLabel lbl) {
        lbl.setForeground(COLOR_SEP_TEXT);
        lbl.setFont(FONT_BOLD);
        JSeparator sep = new JSeparator();
        panel.add(lbl, "span, split 2, aligny center");
        panel.add(sep, "gapleft rel, growx, wrap");
    }
    
    
    
    private void backupBrowse() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(backupFolderText.getText()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = fc.showOpenDialog(p);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                backupFolderText.setText(fc.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) {
                backupFolderText.setText("");
            }
        }
    }

    public void stateChanged(ChangeEvent evt) {
        controller.changed();
        enableDisableFields();
    }

    private JLabel autosaveLabel;
    private JLabel hoursLabel;
    private JSpinner hoursField;
    private JLabel minsLabel;
    private JSpinner minsField;
    private JLabel secsLabel;
    private JSpinner secsField;
    private JButton recoveryBrowseButton;
    private JLabel recoveryFolderLabel;
    private JTextField recoveryFolderText;
    private ButtonGroup recoveryButtonGroup;
    private JRadioButton recoveryKeepAllRadio;
    private JRadioButton recoveryKeepNbrRadio;
    private JSpinner recoveryKeepNbrSpinner;
    private JLabel backupLabel;
    private JComboBox<Item> backupCombo;
    private JLabel backupFolderLabel;
    private JTextField backupFolderText;
    private JButton backupBrowseButton;
    private ButtonGroup backupButtonGroup;
    private JRadioButton backupKeepAllRadio;
    private JRadioButton backupKeepNbrRadio;
    private JSpinner backupKeepNbrSpinner;

    private static final class Item {
        public String label;
        public int value;
        public Item(String label, int value) {
            this.label = label;
            this.value = value;
        }
        @Override
        public String toString() {
            return label;
        }
    }
    private static final Item[] intervals = {
        new Item(Resources.getText("startup"), 0),
        new Item(Resources.getText("day"), 1),
        new Item(Resources.getText("week"), 7),
        new Item(Resources.getText("2weeks"), 14),
        new Item(Resources.getText("3weeks"), 21),
        new Item(Resources.getText("4weeks"), 28),
        new Item(Resources.getText("never"), Integer.MAX_VALUE)
    };
    private static final Item getInterval(int value) {
        for (Item interval : intervals) {
            if (interval.value == value) {
                return interval;
            }
        }
        return intervals[0];
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

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
package au.com.trgtd.tr.calendar.ical4j.impl.prefs;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.calendar.prefs.TimeZones;
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizerOptions;
import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Options panel.
 *
 * @author Jeremy Moore
 */
public class OptionsPanel extends JPanel implements CalendarSynchronizerOptions {

    private boolean changed;

    public OptionsPanel() {
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        folderLabel = new TRLabel(getMsg("folderLabel"));
        folderField = new JTextField();
        folderField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                changed = true;
            }
            public void removeUpdate(DocumentEvent evt) {
                changed = true;
            }
            public void changedUpdate(DocumentEvent evt) {
                changed = true;
            }
        });
        folderButton = new JButton(getMsg("folderButton"));
        folderButton.addActionListener((ActionEvent evt) -> {
            browse();
        });
        timezoneLabel = new TRLabel(getMsg("timezoneLabel"));
        timezoneCombo = new TRComboBox<>();
        timezoneCombo.addActionListener((ActionEvent evt) -> {
            changed = true;
        });
        JPanel panel = new JPanel(new MigLayout("", "4[]2[grow]2[]4", "4[]4"));
        panel.add(folderLabel,   "align left");
        panel.add(folderField,   "align left, growx 100");
        panel.add(folderButton,  "align left, wrap");
        panel.add(timezoneLabel, "align left");
        panel.add(timezoneCombo, "align left, wrap");
        return panel;
    }

    public boolean changed() {
        return changed;
    }

    public void load() {
        initTimeZoneItems();
        timezoneCombo.setModel(new DefaultComboBoxModel<>(tzlist));
        timezoneCombo.setMaximumRowCount(Constants.COMBO_MAX_ROWS);
        timezoneCombo.setSelectedItem(Options.getTimeZoneID());
        folderField.setText(Options.getICalendarPath());
        changed = false;
    }

    private static Vector<String> tzlist;

    private void initTimeZoneItems() {
        tzlist = new Vector<>();
        tzlist.add("");
        for (String id : TimeZones.IDS) {
            tzlist.add(id);
        }
        Collections.sort(tzlist);
    }

    public void store() {
        if (isValidPath()) {
            Options.setICalendarPath(tidyPath(folderField.getText()));
        } else {
            Options.setICalendarPath("");
        }
        String tzid = (String)timezoneCombo.getSelectedItem();
        if (tzid != null) {
            Options.setTimeZoneID(tzid);
        }
        changed = false;
    }

    public JPanel getPanel() {
        return this;
    }

    public boolean valid() {
        return isValidPath();
    }

    private boolean isValidPath() {
        String path = tidyPath(folderField.getText());
        return path.length() == 0 || (new File(path)).isDirectory();
    }

    private String tidyPath(String path) {
        path = path.trim();
        while (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1).trim();
        }
        return path;
    }

    @Override
    public void enable(boolean b) {
        folderLabel.setEnabled(b);
        folderButton.setEnabled(b);
        folderField.setEnabled(b);
        timezoneLabel.setEnabled(b);
        timezoneCombo.setEnabled(b);
    }

    private void browse() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setSelectedFile(new File(folderField.getText()));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                folderField.setText(fc.getSelectedFile().getCanonicalPath());
            } catch (Exception ex) {
                folderField.setText("");
            }
        }
    }

    public static String getMsg(String key) {
        return NbBundle.getMessage(OptionsPanel.class, key);
    }

    private JButton folderButton;
    private JLabel folderLabel;
    private JTextField folderField;
    private JComboBox<String> timezoneCombo;
    private JLabel timezoneLabel;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

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
package au.com.trgtd.tr.extract.clean;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ExtractCleanPanel extends javax.swing.JPanel {
    
    private final ExtractCleanOptionsPanelController controller;
    
    ExtractCleanPanel(ExtractCleanOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        runLabel = new JLabel(getMsg("Run_Label"));
        ageLabel = new JLabel(getMsg("Age_Label"));
        runCombo = new TRComboBox<>(new DefaultComboBoxModel<>(intervals));
        runCombo.addActionListener((java.awt.event.ActionEvent evt) -> {
            controller.changed();
        });
        ageCombo = new TRComboBox<>(new DefaultComboBoxModel<>(ages));
        ageCombo.addActionListener((java.awt.event.ActionEvent evt) -> {
            controller.changed();
        });

        JPanel panel = new JPanel(new MigLayout("", "0[]2[]0", "0[]2[]0"));

        panel.add(runLabel, "align right");
        panel.add(runCombo, "align left, wrap");

        panel.add(ageLabel, "align right");
        panel.add(ageCombo, "align left, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    void load() {
        runCombo.setSelectedItem(getInterval(ExtractCleanPrefs.getCleanIntervalDays()));
        ageCombo.setSelectedItem(getAge(ExtractCleanPrefs.getCleanAgeDays()));
    }

    private Item getInterval(int value) {
        for (int i = 0; i < intervals.length; i++) {
            if (intervals[i].value == value) {
                return intervals[i];
            }
        }
        return intervals[0];
    }

    private Item getAge(int value) {
        for (int i = 0; i < ages.length; i++) {
            if (ages[i].value == value) {
                return ages[i];
            }
        }
        return ages[0];
    }

    void store() {
        Item ageItem = (Item)ageCombo.getSelectedItem();
        if (ageItem != null) {
            int value = ExtractCleanPrefs.getCleanAgeDays();
            if (ageItem.value != value) {
                ExtractCleanPrefs.setCleanAgeDays(ageItem.value);
            }
        }
        Item intervalItem = (Item)runCombo.getSelectedItem();
        if (intervalItem != null) {
            int value = ExtractCleanPrefs.getCleanIntervalDays();
            if (intervalItem.value != value) {
                ExtractCleanPrefs.setCleanIntervalDays(intervalItem.value);
            }
        }
    }

    boolean valid() {
        return true;
    }

    private static final class Item {
        public String label;
        public int value;
        public Item(String label, int value) {
            this.label = label;
            this.value = value;
        }
        public String toString() {
            return label;
        }
    }

    private final Item[] intervals = {
        new Item(getMsg("Run_Every_Startup"), 0),
        new Item(getMsg("Run_Every_Day"), 1),
        new Item(getMsg("Run_Every_Week"), 7),
        new Item(getMsg("Run_Every_2_Weeks"), 14),
        new Item(getMsg("Run_Every_3_Weeks"), 21),
        new Item(getMsg("Run_Every_4_Weeks"), 28),
        new Item(getMsg("Run_Never"), Integer.MAX_VALUE)
    };

    private final Item[] ages = {
        new Item(getMsg("Age_1_Day"), 1),
        new Item(getMsg("Age_1_Week"), 7),
        new Item(getMsg("Age_2_Weeks"), 14),
        new Item(getMsg("Age_3_Weeks"), 21),
        new Item(getMsg("Age_4_Weeks"), 28)
    };

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private javax.swing.JComboBox<Item> ageCombo;
    private javax.swing.JLabel ageLabel;
    private javax.swing.JComboBox<Item> runCombo;
    private javax.swing.JLabel runLabel;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

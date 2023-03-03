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
package au.com.trgtd.tr.view.actns.prefs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.ParamFont;
import au.com.trgtd.tr.swing.TRComboBox;

final class ActionsPrefsPanel extends JPanel {
    
    private final ActionsOptionsPanelController controller;
    
    ActionsPrefsPanel(ActionsOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    void load() {
        colourCheck.setSelected(ActionsPrefs.isReportUseColour());
        strikeCheck.setSelected(ActionsPrefs.isReportStrikeDone());
        items = (Vector<Item>) (new ParamFont(null, null)).getItems();
        fontCombo.setModel(new DefaultComboBoxModel<>(items));
        String font = ActionsPrefs.getReportFont();
        for (Item item : items) {
            if (item.value.equals(font)) {
                fontCombo.setSelectedItem(item);
                break;
            }
        }
    }

    void store() {
        ActionsPrefs.setReportUseColour(colourCheck.isSelected());
        ActionsPrefs.setReportStrikeDone(strikeCheck.isSelected());
        Item item = (Item) fontCombo.getSelectedItem();
        if (item != null) {
            ActionsPrefs.setReportFont(item.value);
        }
    }

    boolean valid() {
        return true;
    }

    private void initForm() {
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        titleLabel = new JLabel(getMsg("report.heading"));
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));        
        fontLabel = new JLabel(getMsg("report.font"));
        fontCombo = new TRComboBox<>();
        fontCombo.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });
        colourCheck = new JCheckBox(getMsg("use.color"));
        strikeCheck = new JCheckBox(getMsg("strike.done"));

        JPanel panel = new JPanel(new MigLayout("", "0[]0", "0[]4[]2[]2[]0"));
        panel.add(titleLabel,  "align left, wrap");
        panel.add(fontLabel,   "align left, split 2");
        panel.add(fontCombo,   "align left, wrap");
        panel.add(colourCheck, "align left, wrap");
        panel.add(strikeCheck, "align left, wrap");
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(ActionsPrefsPanel.class, key);
    }

    private JLabel titleLabel;
    private JLabel fontLabel;
    private JComboBox<Item> fontCombo;
    private JCheckBox colourCheck;
    private JCheckBox strikeCheck;
    private Vector<Item> items;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

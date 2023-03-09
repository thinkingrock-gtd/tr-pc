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
package au.com.trgtd.tr.prefs.recurrence;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class RecurrencePanel extends javax.swing.JPanel {
    
    private final RecurrenceOptionsPanelController controller;
    
    RecurrencePanel(RecurrenceOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        headingLabel = new JLabel(getMsg("nbr.future"));
        headingLabel.setFont(headingLabel.getFont().deriveFont(Font.BOLD));

        weekdayLabel = new JLabel(getMsg("nbr.future.weekday"));
        weekdayField = new JSpinner();
        weekdayField.addChangeListener(changeListener);

        dayLabel = new JLabel(getMsg("nbr.future.day"));
        dayField = new JSpinner();
        dayField.addChangeListener(changeListener);

        weekLabel = new JLabel(getMsg("nbr.future.week"));
        weekField = new JSpinner();
        weekField.addChangeListener(changeListener);

        monthLabel = new JLabel(getMsg("nbr.future.month"));
        monthField = new JSpinner();
        monthField.addChangeListener(changeListener);
        
        yearLabel = new JLabel(getMsg("nbr.future.year"));
        yearField = new JSpinner();
        yearField.addChangeListener(changeListener);

        JPanel panel = new JPanel(new MigLayout("", "0[]2[]2[]2[]2[]2[]0", "0[]2[]2[]0"));

        panel.add(headingLabel, "align left, span, wrap");

        panel.add(weekdayLabel, "align right, gapleft 12");
        panel.add(weekdayField, "align left, w 60!");
        panel.add(dayLabel,     "align right, gapleft 12");
        panel.add(dayField,     "align left, w 60!");
        panel.add(weekLabel,    "align right, gapleft 12");
        panel.add(weekField,    "align left, w 60!, wrap");

        panel.add(monthLabel,   "align right, gapleft 12");
        panel.add(monthField,   "align left, w 60!");
        panel.add(yearLabel,    "align right, gapleft 12");
        panel.add(yearField,    "align left, w 60!, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    void load() {
        weekdayField.setValue(Integer.valueOf(RecurrencePrefs.getNbrFutureWeekday()));
        dayField.setValue(Integer.valueOf(RecurrencePrefs.getNbrFutureDay()));
        weekField.setValue(Integer.valueOf(RecurrencePrefs.getNbrFutureWeek()));
        monthField.setValue(Integer.valueOf(RecurrencePrefs.getNbrFutureMonth()));
        yearField.setValue(Integer.valueOf(RecurrencePrefs.getNbrFutureYear()));
    }

    void store() {
        RecurrencePrefs.setNbrFutureWeekday(((Number)weekdayField.getValue()).intValue());
        RecurrencePrefs.setNbrFutureDay(((Number)dayField.getValue()).intValue());
        RecurrencePrefs.setNbrFutureWeek(((Number)weekField.getValue()).intValue());
        RecurrencePrefs.setNbrFutureMonth(((Number)monthField.getValue()).intValue());
        RecurrencePrefs.setNbrFutureYear(((Number)yearField.getValue()).intValue());
    }

    boolean valid() {
        return true;
    }

    private ChangeListener changeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent evt) {
            controller.changed();
        }
    };

    private JLabel dayLabel;
    private JSpinner dayField;
    private JLabel headingLabel;
    private JLabel monthLabel;
    private JSpinner monthField;
    private JLabel weekLabel;
    private JSpinner weekField;
    private JLabel weekdayLabel;
    private JSpinner weekdayField;
    private JLabel yearLabel;
    private JSpinner yearField;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

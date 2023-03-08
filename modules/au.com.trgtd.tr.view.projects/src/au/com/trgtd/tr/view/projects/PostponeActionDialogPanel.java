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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.view.projects.PostponeActionAction.Periods;
import java.awt.event.ActionEvent;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Postpone action date panel.
 *
 * @author Jeremy Moore
 */
public class PostponeActionDialogPanel extends JPanel {

    /** Creates new form PostponeActionPanel */
    public PostponeActionDialogPanel() {
        initComponents();
    }

    public void reset() {
        specificRadioButton.setSelected(true);
        numberSpinnerField.setValue(1);
        weeksRadioButton.setSelected(true);
        enableDisable();
    }

    public boolean isSpecific() {
        return specificRadioButton.isSelected();
    }

    public boolean isAdvance() {
        return advanceRadioButton.isSelected();
    }

    public Date getSpecificDate() {
        return specificDateField.getDate();
    }

    public int getAdvanceNumber() {
        return (Integer) numberSpinnerField.getValue();
    }

    public Periods getAdvancePeriods() {
        if (daysRadioButton.isSelected()) {
            return Periods.DAYS;
        } else if (weeksRadioButton.isSelected()) {
            return Periods.WEEKS;
        } else if (monthsRadioButton.isSelected()) {
            return Periods.MONTHS;
        } else if (yearsRadioButton.isSelected()) {
            return Periods.YEARS;
        } else {
            return null;
        }
    }

    private void enableDisable() {
        boolean b = advanceRadioButton.isSelected();
        daysRadioButton.setEnabled(b);
        monthsRadioButton.setEnabled(b);
        numberSpinnerField.setEnabled(b);
        weeksRadioButton.setEnabled(b);
        yearsRadioButton.setEnabled(b);

        specificDateField.setEnabled(!b);
    }

    private void initComponents() {
        // type choice fields
        specificRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "postpone.specific"));
        specificRadioButton.addActionListener((ActionEvent evt) -> {
            enableDisable();
        });
        advanceRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "postpone.advanced"));
        advanceRadioButton.addActionListener((ActionEvent evt) -> {
            enableDisable();
        });
        typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(specificRadioButton);
        typeButtonGroup.add(advanceRadioButton);

        // specific type fields
        specificDateField = new DateField();
        specificDateField.setDate(new Date());

        // advance type fields
        periodsButtonGroup = new ButtonGroup();
        numberSpinnerField = new JSpinner();
        daysRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "days"));
        weeksRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "weeks"));
        monthsRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "months"));
        yearsRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "years"));
        numberSpinnerField.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        periodsButtonGroup.add(daysRadioButton);
        periodsButtonGroup.add(weeksRadioButton);
        periodsButtonGroup.add(monthsRadioButton);
        periodsButtonGroup.add(yearsRadioButton);

        // layout fields
        setLayout(new MigLayout("","8[]4[]24","8[]4[]4[]4[]4[]4[]8"));
        add(specificRadioButton, "tag label");
        add(specificDateField, "align left, wrap");
        add(advanceRadioButton, "tag label");
        add(numberSpinnerField, "align left, wrap");
        add(daysRadioButton, "skip, align left, wrap");
        add(weeksRadioButton, "skip, align left, wrap");
        add(monthsRadioButton, "skip, align left, wrap");
        add(yearsRadioButton, "skip, align left, wrap");

        reset();
    }
    
    private JRadioButton daysRadioButton;
    private JRadioButton monthsRadioButton;
    private JSpinner numberSpinnerField;
    private ButtonGroup periodsButtonGroup;
    private JRadioButton weeksRadioButton;
    private JRadioButton yearsRadioButton;
    private ButtonGroup typeButtonGroup;
    private JRadioButton specificRadioButton;
    private JRadioButton advanceRadioButton;
    private DateField specificDateField;
    
}

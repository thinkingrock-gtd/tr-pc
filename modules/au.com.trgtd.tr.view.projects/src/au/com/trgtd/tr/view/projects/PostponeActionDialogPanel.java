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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.view.projects.PostponeActionAction.Periods;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        specificRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                enableDisable();
            }
        });
        advanceRadioButton = new JRadioButton(NbBundle.getMessage(PostponeActionPanel.class, "postpone.advanced"));
        advanceRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                enableDisable();
            }
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

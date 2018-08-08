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
package au.com.trgtd.tr.view.actn;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodMonth.OnTheDay;
import tr.model.action.PeriodMonth.OnTheNth;

/**
 * Month period panel.
 * 
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class PeriodMonthlyPanel extends JPanel {

    /** Creates new form PeriodMonthsPanel */
    public PeriodMonthlyPanel(Dialog parent, PeriodMonth periodMonth) {
        this.parent = parent;
        this.periodMonth = periodMonth;
        initView();
        eachRadio.setSelected(periodMonth.getOption() == PeriodMonth.Option.Each);
        onTheRadio.setSelected(periodMonth.getOption() == PeriodMonth.Option.OnThe);
        dayCombo.setModel(getDayComboModel());
        dayCombo.setSelectedItem(periodMonth.getOnTheDay());
        nthCombo.setModel(getNthComboModel());
        nthCombo.setSelectedItem(periodMonth.getOnTheNth());
        setEnabled(true);
    }

    private String getSelectedDaysText() {
        return periodMonth.getSelectedDaysText();        
    }
    
    private ComboBoxModel getDayComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheDay.values());
    }

    private ComboBoxModel getNthComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheNth.values());
    }

    /**
     * Receives notification of the start date so that a default day of the
     * month can be set if necessary (i.e. if there is none already set).
     * @startDate The start date
     */
    public void notifyStartDate(Date startDate) {
        if (startDate == null) {
            return;
        }
        if (periodMonth.getSelectedDays().size() > 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        periodMonth.select(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);        
        daysButton.setEnabled(enabled && eachRadio.isSelected());
        nthCombo.setEnabled(enabled && onTheRadio.isSelected());
        dayCombo.setEnabled(enabled && onTheRadio.isSelected());
        eachRadio.setEnabled(enabled);
        onTheRadio.setEnabled(enabled);
    }

    private void initView() {
        eachRadio = new JRadioButton(getMsg("months.each"));
        eachRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthsRadioButtonAction(evt);
            }
        });
        daysButton = new JButton(getSelectedDaysText());
        daysButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daysButtonAction(evt);
            }
        });
        onTheRadio = new JRadioButton(getMsg("months.on.the"));
        onTheRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthsRadioButtonAction(evt);
            }
        });
        nthCombo = new TRComboBox();
        nthCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onTheNthComboAction(evt);
            }
        });
        dayCombo = new TRComboBox();
        dayCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayComboAction(evt);
            }
        });

        buttonGroup = new ButtonGroup();
        buttonGroup.add(eachRadio);
        buttonGroup.add(onTheRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[grow]0", "0[]0"));
        panel.add(eachRadio, "");
        panel.add(daysButton, "");
        panel.add(onTheRadio, "");
        panel.add(nthCombo, "");
        panel.add(dayCombo, "wrap");
        
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void dayComboAction(java.awt.event.ActionEvent evt) {
        periodMonth.setOnTheDay((OnTheDay) dayCombo.getSelectedItem());
    }

    private void onTheNthComboAction(java.awt.event.ActionEvent evt) {
        periodMonth.setOnTheNth((OnTheNth) nthCombo.getSelectedItem());
    }

    private void daysButtonAction(java.awt.event.ActionEvent evt) {
        DaysOfMonthDialog dialog = new DaysOfMonthDialog(parent, daysButton);
        dialog.setPeriodMonth(periodMonth);
        dialog.setVisible(true);
        if (!dialog.cancelled()) {
            periodMonth.setSelectedDays(dialog.getSelectedDays());
            daysButton.setText(periodMonth.getSelectedDaysText());
        }
    }

    private void monthsRadioButtonAction(java.awt.event.ActionEvent evt) {
        if (eachRadio.isSelected()) {
            periodMonth.setOption(PeriodMonth.Option.Each);
        } else {
            periodMonth.setOption(PeriodMonth.Option.OnThe);
        }
        setEnabled(true);
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private final Dialog parent;
    private final PeriodMonth periodMonth;
    private ButtonGroup buttonGroup;
    private JComboBox dayCombo;
    private JButton daysButton;
    private JRadioButton eachRadio;
    private JComboBox nthCombo;
    private JRadioButton onTheRadio;
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

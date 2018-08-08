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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodMonth.OnTheDay;
import tr.model.action.PeriodMonth.OnTheNth;
import tr.model.action.PeriodYear;

/**
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class PeriodYearlyPanel extends JPanel {

    /** Creates new instance. */
    public PeriodYearlyPanel(Dialog parent, PeriodYear periodYear) {
        this.parent = parent;
        this.periodYear = periodYear;
        initView();
        onTheCheckBox.setSelected(periodYear.isOnTheSelected());
        dayComboBox.setModel(getDayComboModel());
        dayComboBox.setSelectedItem(periodYear.getOnTheDay());
        nthComboBox.setModel(getNthComboModel());
        nthComboBox.setSelectedItem(periodYear.getOnTheNth());
        setEnabled(true);
    }

    private ComboBoxModel getDayComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheDay.values());
    }

    private ComboBoxModel getNthComboModel() {
        return new DefaultComboBoxModel(PeriodMonth.OnTheNth.values());
    }

    private String getSelectedMonthsText() {
        return periodYear.getSelectedMonthsText();        
    }
    
    /**
     * Receives notification of the start date so that a default month can
     * be set if necessary (i.e. if there is none already set).
     * @startDate The start date
     */
    public void notifyStartDate(Date startDate) {
        if (startDate == null) {
            return;
        }
        if (periodYear.getSelectedMonths().size() > 0) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        periodYear.select(calendar.get(Calendar.MONTH));
    }

    private void initView() {
        inLabel = new JLabel(getMsg("YearsPanel.in"));
        monthsButton = new JButton(getSelectedMonthsText());
        monthsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                monthsButtonAction(evt);
            }
        });
        onTheCheckBox = new JCheckBox(getMsg("months.on.the"));
        onTheCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                onTheCheckBoxAction(evt);
            }
        });
        nthComboBox = new TRComboBox();
        nthComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                NthComboAction(evt);
            }
        });
        dayComboBox = new TRComboBox();
        dayComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dayComboAction(evt);
            }
        });

        JPanel panel = new JPanel(new MigLayout("", "0[grow]0", "0[]0"));
        panel.add(inLabel, "");
        panel.add(monthsButton, "");
        panel.add(onTheCheckBox, "");
        panel.add(nthComboBox, "");
        panel.add(dayComboBox, "wrap");       

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void dayComboAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheDay((OnTheDay) dayComboBox.getSelectedItem());
    }

    private void NthComboAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheNth((OnTheNth) nthComboBox.getSelectedItem());
    }

    private void onTheCheckBoxAction(java.awt.event.ActionEvent evt) {
        periodYear.setOnTheSelected(onTheCheckBox.isSelected());
        setEnabled(true);
    }

    private void monthsButtonAction(java.awt.event.ActionEvent evt) {
        MonthsOfYearDialog dialog = new MonthsOfYearDialog(parent, monthsButton);
        dialog.setPeriodYear(periodYear);
        dialog.setVisible(true);
        if (!dialog.cancelled()) {
            periodYear.setSelectedMonths(dialog.getSelectedMonths());
            monthsButton.setText(periodYear.getSelectedMonthsText());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        nthComboBox.setEnabled(enabled && onTheCheckBox.isSelected());
        dayComboBox.setEnabled(enabled && onTheCheckBox.isSelected());
        monthsButton.setEnabled(enabled);
        onTheCheckBox.setEnabled(enabled);
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private final Dialog parent;
    private final PeriodYear periodYear;
    private JComboBox dayComboBox;
    private JLabel inLabel;
    private JButton monthsButton;
    private JComboBox nthComboBox;
    private JCheckBox onTheCheckBox;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

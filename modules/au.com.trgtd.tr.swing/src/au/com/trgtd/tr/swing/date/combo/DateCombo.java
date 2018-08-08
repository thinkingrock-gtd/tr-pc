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

package au.com.trgtd.tr.swing.date.combo;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;

import au.com.trgtd.tr.swing.date.chooser.DateChooserDialog;
import au.com.trgtd.tr.swing.date.chooser.DateChooser;

/**
 * ComboBox for date selection.
 *
 * @author Jeremy Moore
 */
public class DateCombo extends TRComboBox implements ActionListener {
    
    /** Property for the date selection. */
    public static final String PROPERTY_DATE = "date";
    
    private final boolean calc;
    private final DateFormat df;
    private final DateItem chosen;
    private final DateChooser dateChooser;
    
    /**
     * Constructs a new instance for the given data model.
     * @param calc If true dates are calculated and selected, e.g. if the
     * user selects Today, today's date is put in the first item and selected.
     * If false dates are not calculated, e.g. If the user selects Today then
     * Today is selected.  Note that if the user selects the Use Date Chooser
     * option the selected date is set in the first item and it is selected.
     */
    public DateCombo(Window window, DateItem[] items, boolean calc, DateFormat df) {
        super(new DefaultComboBoxModel(items));
        this.calc = calc;
        this.df = df;
        if (window instanceof Frame) {
            dateChooser = new DateChooser((Frame)window);            
        } else if (window instanceof Dialog) {
            dateChooser = new DateChooser((Dialog)window);                        
        } else {
            dateChooser = new DateChooser(new Frame());                        
        }
        chosen = new DateItem(DateType.NONE, "", 0);
        insertItemAt(chosen, 0);
        setMaximumRowCount(items.length + 1);
        setSelectedItem(null);
        addActionListener(this);
    }
    
    public void stopChangeEvents() {
        removeActionListener(this);
    }
    
    public void startChangeEvents() {
        addActionListener(this);
    }
    
    /** Handle selection. */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        DateItem item = (DateItem)getSelectedItem();
        if (item == null) {
            return;
        }
        if (item == DateItem.DATE_CHOOSER) {
            DateChooserDialog dialog = dateChooser.getDialog();
            Date date;
            if (chosen.equals(DateItem.EARLIEST) || chosen.equals(DateItem.LATEST)) {
                date = dialog.select(null, this);
            } else {
                date = dialog.select(getDate(chosen), this);
            }
            if (dialog.cancelled()) {
                setSelectedIndex(0);
                return;
            }
            if (date == null) {
                chosen.type = DateItem.NONE.type;
                chosen.label = DateItem.NONE.label;
                chosen.value = DateItem.NONE.value;
            } else {
                chosen.type = DateType.MS;
                chosen.label = df.format(date);
                chosen.value = date.getTime();
            }
        } else { // (item != DateItem.DATE_CHOOSER)
            if (calc) {
                if (item.type == DateType.DAYS) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_YEAR, (int)item.value);
                    chosen.type = DateType.MS;
                    chosen.label = df.format(c.getTime());
                    chosen.value = c.getTimeInMillis();
                } else if (item.type == DateType.MS) {
                    chosen.type = DateType.MS;
                    chosen.label = df.format(new Date(item.value));
                    chosen.value = item.value;
                } else if (item.type == DateType.FIXED) {
                    chosen.type = item.type;
                    chosen.label = item.label;
                    chosen.value = item.value;                    
                }
            } else { // (!calc )
                chosen.type = item.type;
                chosen.label = item.label;
                chosen.value = item.value;
            }
        }
        
        setSelectedIndex(0);
        
        fireValueChange();
    }
    
    public void fireValueChange() {
        firePropertyChange(PROPERTY_DATE, null, null);
    }
    
    /**
     * Gets the date item for date item values.
     * @param type The date item type
     * @param value The date item value
     * @return A date item or null.
     */
    public DateItem getDateItem(DateType type, long value) {
        if (type == DateType.MS) {
            chosen.type = type;
            chosen.value = value;
            chosen.label = df.format(new Date(value));
            return chosen;
        }
        for (int i = 0; i < getItemCount(); i++) {
            DateItem item = (DateItem)getItemAt(i);
            if (item.type == type && item.value == value) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Gets the date for a date item.
     * @return A date or null.
     */
    public static Date getDate(DateItem item) {
        return (item == null) ? null : item.getDate();
    }
    
}

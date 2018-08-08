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

package au.com.trgtd.tr.swing.date.button;

import au.com.trgtd.tr.swing.date.chooser.DateChooser;
import au.com.trgtd.tr.swing.date.chooser.DateChooserDialog;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Custom button for entering dates. The <code>DateButton</code> class is just
 * a standard button that defines an additional bound property: "date". The
 * button displays the date property as its label. When clicked, it does not
 * generate an <code>ActionEvent</code>, but displays a {@link DateChooser}
 * dialog instead, that allows you to change the date. When the date is changed,
 * a <code>PropertyChangeEvent</code> is generated, according the contract for
 * bound properties.
 */
public class DateButton extends JButton {
    
    public static final DateFormat DATE_FORMAT_FIXED = new SimpleDateFormat("EEE, d MMM yyyy");
    
    /** Format to use to display the date property. */
    public static final DateFormat DATE_FORMAT = DATE_FORMAT_FIXED;
    
    /* DateChooser instance to use to change the date. */
    private final DateChooser dateChooser;
    
    /* Date property. */
    private Date date;
    private String noSelectionText = "";
    
    public Component component = null;
    
    /**
     * Constructs a new default instance.
     */
    public DateButton() {
//        Frame owner = WindowManager.getDefault().getMainWindow();
//        dateChooser = new DateChooser(owner, firstDay);
        dateChooser = new DateChooser(new JFrame());
    }
    
    /**
     * Constructs a new instance with the given title.
     * @param title The dialog title.
     */
    public DateButton(String title) {
//        Frame owner = WindowManager.getDefault().getMainWindow();
//        dateChooser = new DateChooser(owner, title, firstDay);
        dateChooser = new DateChooser(new JFrame(), title);
    }
    
    /**
     * Constructs a new instance with the given window owner.
     * @param owner The window owner.
     */
    public DateButton(Frame owner) {
        dateChooser = new DateChooser(owner);
    }
    
    /**
     * Constructs a new instance with the window owner and dialog title.
     * @param owner The window owner.
     * @param title The dialog title.
     */
    public DateButton(Frame owner, String title) {
        dateChooser = new DateChooser(owner, title);
    }
    
    /**
     * Constructs a new instance with the given dialog owner.
     * @param owner The dialog owner.
     */
    public DateButton(Dialog owner) {
        dateChooser = new DateChooser(owner);
    }
    
    /**
     * Constructs a new instance with the window owner and dialog title.
     * @param owner The dialog owner.
     * @param title The dialog title.
     */
    public DateButton(Dialog owner, String title) {
        dateChooser = new DateChooser(owner, title);
    }
    
    
    /**
     * Set the button text to display when no date is selected.
     * @param s The no selection text.
     */
    public void setNoSelectionText(String text) {
        this.noSelectionText = text;
        this.setDate(date);
    }
    
    /**
     * Gets the value of the date property.
     * @return The current value of the date property.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Sets the value of the date property.
     * @param date The new value of the date property
     * @return the old value of the date property
     */
    public void setDate(Date date) {
        Date old = this.date;
        this.date = date;
        setText((date == null) ? noSelectionText : DATE_FORMAT.format(date));
        firePropertyChange("date", old, date);
    }
    
    /**
     * Called when the button is clicked, in order to fire an
     * <code>ActionEvent</code>. Displays the dialog to change the date
     * instead of generating the event and updates the date property.
     * @param e <code>ActionEvent</code> to fire
     */
    protected void fireActionPerformed(ActionEvent event) {
        
        DateChooserDialog dialog = dateChooser.getDialog();
        
        if (component == null) {
            component = this;
        }
        
        Date newDate = dialog.select(date, component);
        if (dialog.cancelled()) {
            return;
        }
        
        setDate(newDate);
    }
    
}

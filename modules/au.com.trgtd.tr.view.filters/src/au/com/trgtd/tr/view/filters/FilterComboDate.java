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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.filters;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window; 
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import javax.swing.JComboBox;
import au.com.trgtd.tr.swing.date.combo.DateCombo;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.util.Utils;

/**
 * ComboBox for date selection.
 *
 * @author Jeremy Moore
 */

public class FilterComboDate extends DateCombo implements FilterCombo {
    
    private static final Frame frame = Utils.getAppFrame();
    private static final DateFormat df = Constants.DATE_FORMAT_FIXED;
    
    public FilterComboDate(DateItem[] items, boolean calc) {
        super((Window)frame, items, calc, df);
        setPreferredSize(new Dimension(FilterCombo.WIDTH, FilterCombo.HEIGHT));
    }
    
    public void addValueChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(FilterCombo.PROPERTY_VALUE, listener);
    }
    
    @Override
    public void fireValueChange() {
        firePropertyChange(FilterCombo.PROPERTY_VALUE, null, null);
    }
    
    public JComboBox getJComboBox() {
        return this;
    }
    
}

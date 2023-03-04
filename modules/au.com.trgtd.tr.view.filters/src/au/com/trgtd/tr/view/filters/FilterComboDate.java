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
    
    public JComboBox<DateItem> getJComboBox() {
        return this;
    }
    
}

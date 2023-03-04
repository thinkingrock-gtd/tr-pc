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

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * Filter values combo box.
 *
 * @author Jeremy Moore
 */
public abstract class FilterComboAbstract<E> extends TRComboBox<E> implements FilterCombo<E> {
    
    public FilterComboAbstract(ComboBoxModel<E> model) {
        super(model);
        setPreferredSize(new Dimension(FilterCombo.WIDTH, FilterCombo.HEIGHT));
//      putClientProperty("JComponent.sizeVariant", "small");
//        setPreferredSize(new Dimension(FilterCombo.WIDTH, getHeight()));
//        ((JTextField)getEditor().getEditorComponent()).putClientProperty("JComponent.sizeVariant", "small");
        
    }
    
    public JComboBox<E> getJComboBox() {
        return this;
    }
    
    public void fireValueChange() {
        firePropertyChange(FilterCombo.PROPERTY_VALUE, null, null);
    }
    
    public void addValueChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(FilterCombo.PROPERTY_VALUE, listener);
    }
    
}

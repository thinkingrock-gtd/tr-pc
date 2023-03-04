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

import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;

/**
 * Filter values combo box.
 *
 * @author Jeremy Moore
 */
public interface FilterCombo<E> {
    
    /** Property for value selection changes. */
    public static final String PROPERTY_VALUE = "value";
    
    /** Preferred width. */
    public static final int WIDTH = 140;
    
    /** Preferred height. */
//  public static final int HEIGHT = 27;
//  public static final int HEIGHT = Utilities.isMac() ? 23 : 19;
    public static final int HEIGHT = 23;
    
    public void stopChangeEvents();
    
    public void startChangeEvents();
    
    public JComboBox<E> getJComboBox();
    
    public void addValueChangeListener(PropertyChangeListener listener);
    
    public void fireValueChange();
    
    public Object getSelectedItem();
    
    public void setSelectedItem(Object object);
    
    public void setSelectedIndex(int anIndex);
    
    public E getItemAt(int index);
    
    public int getItemCount();
    
}

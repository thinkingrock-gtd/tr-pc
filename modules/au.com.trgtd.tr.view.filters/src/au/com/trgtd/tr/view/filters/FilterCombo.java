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

import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;

/**
 * Filter values combo box.
 *
 * @author Jeremy Moore
 */
public interface FilterCombo {
    
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
    
    public JComboBox getJComboBox();
    
    public void addValueChangeListener(PropertyChangeListener listener);
    
    public void fireValueChange();
    
    public Object getSelectedItem();
    
    public void setSelectedItem(Object object);
    
    public void setSelectedIndex(int anIndex);
    
    public Object getItemAt(int index);
    
    public int getItemCount();
    
}

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
public abstract class FilterComboAbstract extends TRComboBox implements FilterCombo {
    
    public FilterComboAbstract(ComboBoxModel model) {
        super(model);
        setPreferredSize(new Dimension(FilterCombo.WIDTH, FilterCombo.HEIGHT));
//      putClientProperty("JComponent.sizeVariant", "small");
//        setPreferredSize(new Dimension(FilterCombo.WIDTH, getHeight()));
//        ((JTextField)getEditor().getEditorComponent()).putClientProperty("JComponent.sizeVariant", "small");
        
    }
    
    public JComboBox getJComboBox() {
        return this;
    }
    
    public void fireValueChange() {
        firePropertyChange(FilterCombo.PROPERTY_VALUE, null, null);
    }
    
    public void addValueChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(FilterCombo.PROPERTY_VALUE, listener);
    }
    
}

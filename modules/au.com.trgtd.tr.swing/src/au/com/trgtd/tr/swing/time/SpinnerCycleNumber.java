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
package au.com.trgtd.tr.swing.time;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;

/**
 * A JSpinner that cycles through a range of numbers.
 *
 * @author Jeremy Moore
 */
public class SpinnerCycleNumber extends JSpinner {

    private static final Dimension SIZE = new Dimension(46, 23);
    
    /** Minimum value. */
    public final int min;
    /** Maximum value. */
    public final int max;
    
    private final JFormattedTextField ftf;

    /**
     * Constructs a new instance.
     * 
     * @param val The val value.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @param step The step value.
     */
    public SpinnerCycleNumber(int val, int min, int max, int step) {
        super();
        this.min = min;
        this.max = max;
                
        setModel(new SpinnerCycleNumberModel(val, min, max, step));
        
        setEditor(new JSpinner.NumberEditor(this, "00"));
        ftf = getTextField(this);
        ftf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    ftf.selectAll();
                });
            }
        });
//	The following is an attempt to get state change on user text edit changes.
//      It does not work properly but could be fixable.
//	ftf.addKeyListener(new KeyAdapter() {
//          public void keyReleased(KeyEvent arg0) {
//	    	try {
//                  int value = Integer.parseInt(ftf.getText());
//                  if (value != getLastValue() && isValidValue(value)) {
//	    		SpinnerCycleNumber.this.fireStateChanged();
//                  }
//	    	}
//	    	catch (NumberFormatException ex) {
//                  System.out.println("NumberFormatException");
//	    	}
//	    }
//      });
        setPreferredSize(SIZE);
        setMinimumSize(SIZE);
        setMaximumSize(SIZE);
    }
    
    /* Gets the underlying formatted text field */
    private JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            return defaultEditor.getTextField();
        } else {
            return null;
        }
    }
    
    /**
     * Determines whether or not a given value if it is valid.
     * @param value The value.
     * @return true if and only if the value is valid.
     */
    public boolean isValidValue(int value) {
        return (value >= min && value <= max);
    }
    
    /* Gets the last value set in the model (as apposed to the text value). */
    private int getLastValue() {
        return ((Number) super.getValue()).intValue();
    }
    
    /**
     * Sets the value if it is valid.
     * @param value The new value.
     */
    public void setVal(int value) {
        if (isValidValue(value)) {
            super.setValue(new Integer(value));
        }
    }
    
    /**
     * Gets the value.
     * @return The value.
     */
    public int getVal() {
        return getLastValue();
    }
        
}

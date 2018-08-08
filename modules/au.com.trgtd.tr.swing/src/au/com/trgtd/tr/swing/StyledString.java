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

package au.com.trgtd.tr.swing;

import java.awt.Color;

/**
 * Wraps a String value and provides some style attributes such as colour.
 *
 * @author Jeremy Moore
 */
public class StyledString implements Comparable {
    
    /** The String value. */
    private String string;
    /** The foreground colour. */
    private Color foreground;
    /** The background colour. */
    private Color background;
    /** Whether the text should be stiked out. */
    private boolean strike;
    
    /**
     * Constructs a new default instance.
     */
    public StyledString() {
        this(null);
    }
    
    /**
     * Constructs a new instance for a given string value.
     */
    public StyledString(String string) {
        this.setString(string);
        this.setForeground(Color.black);
        this.setBackground(Color.white);
    }
    
    /**
     * Implement Comparable to provide case insensitive alpabetic ordering.
     * @param o The Object to compare to.
     * @return -1, 0, 1 if this.toString() is less than, equal to or greater than
     * o.toString() respectively.
     */
    public int compareTo(Object object) {
        if (object instanceof StyledString) {
            String s1 = getString();
            String s2 = ((StyledString)object).getString();
            return s1.compareToIgnoreCase(s2);
        }
        return 0;
    }
    
    public String getString() {
        return string;
    }
    
    public void setString(String string) {
        this.string = (string == null) ? "" : string;
    }
    
    public Color getForeground() {
        return foreground;
    }
    
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }
    
    public Color getBackground() {
        return background;
    }
    
    public void setBackground(Color background) {
        this.background = background;
    }
    
    public boolean isStrike() {
        return strike;
    }
    
    public void setStrike(boolean strike) {
        this.strike = strike;
    }
    
    public String toString() {
        return string;
    }
    
}

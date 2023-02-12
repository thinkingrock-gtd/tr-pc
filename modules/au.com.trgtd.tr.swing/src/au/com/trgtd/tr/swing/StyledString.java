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
        if (object instanceof StyledString styledString) {
            String s1 = getString();
            String s2 = styledString.getString();
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

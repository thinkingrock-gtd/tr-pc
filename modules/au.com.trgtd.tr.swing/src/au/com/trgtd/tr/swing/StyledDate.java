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

import java.text.DateFormat;
import java.util.Date;

/**
 * Extends <code>StyledLabel</code> to provide styled date.
 *
 * @author Jeremy Moore
 */
public class StyledDate extends StyledString implements Comparable {
    
    private Date date;
    private boolean showTime;
    
    private final DateFormat dateformat;
    private final DateFormat datetimeformat;
    
    
    public StyledDate(DateFormat dateformat, DateFormat datetimeformat) {
        super();
        this.dateformat = dateformat;
        this.datetimeformat = datetimeformat;
    }
    
    /**
     * Sets the date.
     * @param date The date to set.
     */
    public synchronized void setDate(Date date) {
        this.date = date;
        if (date == null) {
            setString("");
            return;
        }
        try {
            setString(showTime ? datetimeformat.format(date) : dateformat.format(date));
        } catch (Exception ex) {
            setString("");
        }
    }
    
    /**
     * Gets the date.
     * @return the date value.
     */
    public synchronized Date getDate() {
        return date;
    }
    
    public void setShowTime(boolean b) {
        showTime = b;
        setDate(date);
    }
    
    /**
     * Override to provide date ordering by dates then nulls.
     * @param object The Object to compare to.
     * @return -1, 0, 1 if this.date is less than, equal to or greater than
     * o.date respectively.
     */
    @Override
    public int compareTo(Object object) {
        if ( ! (object instanceof StyledDate)) {
            return -1;
        }
        
        Date d1 = getDate();
        Date d2 = ((StyledDate)object).getDate();
        
        if (d1 == d2) return 0;
        if (d1 == null) return 1;  // d1 (null) > d2
        if (d2 == null) return -1; // d1 < d2 (null)
        
        return d1.compareTo(d2);
    }
    
}

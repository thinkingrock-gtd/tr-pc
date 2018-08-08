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

package au.com.trgtd.tr.view.actns.screens;

import java.util.Comparator;
import java.util.Date;
import org.jdesktop.swingx.calendar.DateUtils;
import au.com.trgtd.tr.swing.StyledDate;

/**
 * Comparator for due dates.
 *
 * @author Jeremy Moore
 */
public class ComparatorActionDate implements Comparator<StyledDate> {
    
    /**
     * Ordering is first by date <= today, then null, then date > today.
     */
    public int compare(StyledDate sd1, StyledDate sd2) {
        
        Date d1 = sd1.getDate();
        Date d2 = sd2.getDate();
        
        if (d1 == d2) {
            return 0;
        }
        
        if (d1 == null) {
            Date today = DateUtils.endOfDay(new Date());
            return (d2.after(today)) ? -1 : 1;
        }
        
        if (d2 == null) {
            Date today = DateUtils.endOfDay(new Date());
            return (d1.after(today)) ? 1 : -1;
        }
        
        return d1.compareTo(d2);
    }
    
}


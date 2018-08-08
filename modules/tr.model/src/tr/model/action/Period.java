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
package tr.model.action;

import java.util.Date;
import java.util.List;
import org.openide.util.NbBundle;

/**
 * Period type.
 *
 * @author Jeremy Moore
 */
public abstract class Period {

    public abstract byte getID();

    public abstract String getBundleKey();

    @Override
    public final String toString() {
        return NbBundle.getMessage(Period.class, getBundleKey());
    }

    @Override
    public abstract Period clone();

    @Override
    public abstract boolean equals(Object object);

    @Override
    public abstract int hashCode();

    /*
     * Gets the period type.
     */
    public abstract PeriodType getType();

    /*
     * Initialise the period for the given start date.
     */
    public abstract void initialise(Date startDate);

    /**
     * Gets the period interval for a date.
     * @param date The date.
     * @return The period interval.
     */
    public abstract Interval getPeriod(Date date);    
    
    /**
     * Gets the Nth next period interval.
     * @param interval The starting interval.
     * @param n The number of periods.
     * @return The interval.
     */
    public abstract Interval addPeriods(Interval interval, int n);

    /**
     * Gets the next period interval.
     * @param interval The starting period interval.
     * @return The next interval.
     */
    public final Interval getNextPeriod(Interval interval) {
        return addPeriods(interval, 1);        
    }        
    
    /**
     * Gets the previous period interval.
     * @param interval The starting interval.
     * @return The previous interval.
     */
    public final Interval getPreviousPeriod(Interval interval) {
        return addPeriods(interval, -1);
    }          
    
    /**
     * Gets the selected days for the period starting on a given date.
     */
    public abstract List<Date> getSelectedDates(Date startDate, Date templateDate);

    /**
     * Gets the default number of periods in advance.
     */
    public abstract int getDefaultAdvanceNbr();
}

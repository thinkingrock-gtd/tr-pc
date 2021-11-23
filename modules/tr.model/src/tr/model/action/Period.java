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

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

/**
 * The Intervals class has an period interval and a start date. It also provides
 * various methods for working with intervals.  A period interval is a period
 * and frequency (ie number of periods in the interval). For example:
 * an interval of 2 weeks. You would use an Intervals object to manage a series
 * of such intervals with a given start date - a series of 2 week intervals
 * starting at a given start date.
 *
 * @author Jeremy Moore
 */
public class Intervals {

    /** The period. */
    public final Period period;
    /** The frequency (ie the number of periods) in each interval. */
    public final int frequency;
    /** The start date (within the first interval). */
    public final Date startDate;
    /* The derived first interval. */
    private transient Interval firstInterval;

    /**
     * Constructs a new instance.
     * @period The interval periods.
     * @frequency The number of periods in the interval.
     * @startDate The start date within the first period of the first interval.
     */
    public Intervals(Period period, int frequency, Date startDate) {
        assert (period != null);
        assert (frequency > 0);
        assert (startDate != null);

        this.period = period;
        this.frequency = frequency;
        this.startDate = startDate;
    }

    /**
     * Gets the first interval.
     */
    public Interval getFirstInterval() {
        if (firstInterval == null) {
            Interval firstPeriod = period.getPeriod(startDate);
            Interval lastPeriod = period.addPeriods(firstPeriod, frequency - 1);
            firstInterval = new Interval(firstPeriod.start, lastPeriod.end);
        }
        return firstInterval;
    }

    /**
     * Gets the next interval.
     */
    public Interval getNextInterval(Interval interval) {
        assert (interval != null);

        Interval firstPeriod = period.getNextPeriod(period.getPeriod(interval.end));
        Interval lastPeriod = period.addPeriods(firstPeriod, frequency - 1);
        return new Interval(firstPeriod.start, lastPeriod.end);
    }

    /**
     * Gets the previous interval.
     */
    public Interval getPreviousInterval(Interval interval) {
        assert (interval != null);

        Interval lastPeriod = period.getPreviousPeriod(period.getPeriod(interval.start));
        Interval firstPeriod = period.addPeriods(lastPeriod, -(frequency - 1));
        return new Interval(firstPeriod.start, lastPeriod.end);
    }

    /**
     * Finds the interval a date is in.
     * @param date The date.
     */
    public Interval getInterval(Date date) {
        assert (date != null);

        Interval interval = getFirstInterval();

        while (date.before(interval.start)) {
            interval = getPreviousInterval(interval);
        }

        while (date.after(interval.end)) {
            interval = getNextInterval(interval);
        }

        return interval;
    }

    /**
     * Gets the dates within a specific interval that correspond to the selected
     * dates for the interval.
     *
     * Note: dates are for the first period of the interval.
     *
     * @param interval The specific interval of time.
     * @param template A date used to transfer relevant values such as the time,
     * day of the week, etc. for the derived dates.
     */
    public List<Date> getSelectedDates(Interval interval, Date templateDate) {
        return period.getSelectedDates(interval.start, templateDate);
    }
}

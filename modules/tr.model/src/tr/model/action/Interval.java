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

/**
 * A time interval.
 *
 * @author Jeremy Moore
 */
public class Interval {

    /** The start date of the interval. */
    public final Date start;
    /** The end date of the interval. */
    public final Date end;

    /** Constructs a new instance. */
    public Interval(Date start, Date end) {
        if (start == null) {
            throw new IllegalArgumentException("Start date is null.");
        }
        if (end == null) {
            throw new IllegalArgumentException("End date is null.");
        }
        if (end.before(start)) {
            throw new IllegalArgumentException("End date is before start date.");
        }
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object object) {
        try {
            Interval other = (Interval) object;
            return other.start.equals(start) && other.end.equals(end);
        } catch (Exception ex) {
            return false;
        }

    }

    public boolean after(Interval other) {
        return start.after(other.end);
    }

    public boolean before(Interval other) {
        return end.before(other.start);
    }
}

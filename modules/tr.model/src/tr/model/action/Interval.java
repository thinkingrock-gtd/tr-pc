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

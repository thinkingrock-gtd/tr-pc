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
 * Portions Copyright 2006-2008 Avente Pty Ltd. All Rights Reserved.
 */
package tr.model.action;

import org.openide.util.NbBundle;

/**
 * Period type enumeration.
 *
 * @author Jeremy Moore
 */
public enum PeriodType {

    WEEKDAY(PeriodWeekday.ID, "period.weekday"),
    DAY(PeriodDay.ID, "period.day"),
    WEEK(PeriodWeek.ID, "period.week"),
    MONTH(PeriodMonth.ID, "period.month"),
    YEAR(PeriodYear.ID, "period.year");
    
    /** The period identifier. */
    public final byte ID;
    
    /** The period name. */
    public final String NAME;

    /* Constructor */
    private PeriodType(byte id, String key) {
        ID = id;
        NAME = NbBundle.getMessage(Period.class, key);
    }

    /** 
     * Gets the period type from the given period ID.
     * @param id The period ID.
     * @return The period type.
     */
    public static final PeriodType fromID(byte id) {
        switch (id) {
            case PeriodWeekday.ID:
                return WEEKDAY;
            case PeriodDay.ID:
                return DAY;
            case PeriodWeek.ID:
                return WEEK;
            case PeriodMonth.ID:
                return MONTH;
            case PeriodYear.ID:
                return YEAR;
            default:
                return WEEK;
        }
    }
    
    @Override
    public String toString() {
        return NAME;
    }
    
}

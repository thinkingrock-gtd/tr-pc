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

package au.com.trgtd.tr.swing.date.combo;

import java.io.Serializable;

/**
 * Date type enumeration.
 *
 * @author Jeremy Moore
 */
public enum DateType implements Serializable {
    
    NONE(0), DAYS(1), MS(2), CHOOSE(3), FIXED(4), START_OF_WEEK(5), END_OF_WEEK(6);
    
    public final int id;
    
    private DateType(int id) {
        this.id = id;
    }
    
    public static DateType getDateType(int id) {
        if (id == NONE.id) return NONE;
        if (id == DAYS.id) return DAYS;
        if (id == MS.id) return MS;
        if (id == CHOOSE.id) return CHOOSE;
        if (id == FIXED.id) return FIXED;
        if (id == START_OF_WEEK.id) return START_OF_WEEK;
        if (id == END_OF_WEEK.id) return END_OF_WEEK;
        return NONE;
    }
    
    @Override
    public String toString() {
        return name();
    }
    
}

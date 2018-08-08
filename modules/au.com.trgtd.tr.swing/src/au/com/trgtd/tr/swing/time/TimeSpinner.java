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

package au.com.trgtd.tr.swing.time;

import java.util.Calendar;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Time entry spinner.
 *
 * @author Jeremy Moore
 */
public class TimeSpinner extends JSpinner {
    
    /**
     * Constructs a new instance with the current time.
     */
    public TimeSpinner() {
        this(new Date());
    }
    
    /**
     * Constructs a new instance with the given date time.
     * @param date The date and time.
     */
    public TimeSpinner(Date date) {
        super(new SpinnerDateModel(date, null, null, Calendar.MINUTE));
//      setEditor(new JSpinner.DateEditor(this, "HH:mm:ss"));
        setEditor(new JSpinner.DateEditor(this, "HH:mm"));
    }
    
}

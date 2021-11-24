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

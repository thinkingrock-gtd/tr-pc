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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.appl.Constants;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DatePropertyEditor extends PropertyEditorSupport {

    private static final DateFormat DATE_FORMAT = Constants.DATE_FORMAT_FIXED;
    
    @Override
    public String getAsText() {
        Date date = (Date)getValue();
        return date == null ? "" : DATE_FORMAT.format(date);
    }

    @Override
    public void setAsText(String string) {
        try {
//          setValue(new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));
            DATE_FORMAT.parse(string);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Could not parse date");
       }
    }
    
}

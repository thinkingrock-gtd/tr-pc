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
package au.com.trgtd.tr.util;

/**
 * Some static utility methods for email.
 *
 * @author Jeremy Moore
 */
public final class UtilsEmail {

    /** Email address regular expression */
    public static final String EMAIL_REGEXP = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    /**
     * Utility method to determine if a string contains a valid email address.
     * @param string The String value.
     * @return true iff string is empty or contains valid email address.
     */
    public static final boolean isEmailAddress(String string) {

        if (string == null) {
            return false;
        }
        
        string = string.trim().toLowerCase();
        
        return string.length() > 0 && string.matches(EMAIL_REGEXP);
    }

}

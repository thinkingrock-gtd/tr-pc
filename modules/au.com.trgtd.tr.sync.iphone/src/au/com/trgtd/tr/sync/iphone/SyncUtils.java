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
package au.com.trgtd.tr.sync.iphone;

import java.util.logging.Logger;

public class SyncUtils {

    public static final Logger LOG = Logger.getLogger("tr.iPhone.sync");
    public static final String ETX = "\u0003";
    
    public static synchronized String escape(String str) {
        return str == null ? "" : str.replace("/", "/1").replace("|", "/2");
    }

    public static synchronized String unEscape(String str) {
      return str == null ? "" : str.replace("/2", "|").replace("/1", "/");
    }

    public static synchronized String getFieldValue(String field, String string) {
        int p0 = string.indexOf("|" + field + "|");
        if (p0 > -1) {
            int p1 = p0 + field.length() + 2;
            int p2 = string.indexOf("|", p1);
            if (p2 > p1) {
                return unEscape(string.substring(p1, p2));
            }
        }
        return "";
    }

}

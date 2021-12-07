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
package au.com.trgtd.tr.sync.device;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Constants {

//  public static final String VERSION = "1.0.0";
    public static final String ENCODING = "UTF-8";
    public static final String DELIM = "|";
    public static final String ETX = "\u0003";
    public static final String NL = "\n";

    public static final int MAX_MSG_SIZE = 5000;

    public static final DateFormat DF_DATE = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat DF_TIME = new SimpleDateFormat("HHmm");

    /** Message code: handshake. */
    public static final String CODE_HANDSHAKE = "HANDSHAKE";
    /** Field name: version. */
    public static final String FIELD_VERSION = "Version";
    // Regular expression: escaped delimiter character.
    public static final String RE_DELIM = "\\|";
    // Regular expression: escaped version number.
//  public static final String RE_VERSION = VERSION.replace(".", "\\.");

}

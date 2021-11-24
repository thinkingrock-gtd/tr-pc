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
package au.com.trgtd.tr.sync.device.handshake;

import static au.com.trgtd.tr.sync.device.Constants.*;
import au.com.trgtd.tr.sync.device.exception.ParserException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handshake message parser.
 */
class SyncHSParser {

    public static final String RE_VER = "(\\d+\\.\\d+\\.\\d+)";    

    // RE for handshake message.
    private static final String RE_MSG =
            CODE_HANDSHAKE + RE_DELIM +
            FIELD_VERSION + RE_DELIM + RE_VER + RE_DELIM;

    // RE pattern for handshake message
    private static final Pattern PATTERN = Pattern.compile(RE_MSG);


    public SyncHSMsg parse(String msg) throws ParserException {

        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }

        try
        {
            String version = String.valueOf(matcher.group(1));            
            return new SyncHSMsg(version);
        }
        catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Handshake parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }

}

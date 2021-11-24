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
package au.com.trgtd.tr.sync.device.v100.message.parse;

import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.exception.ReceiveException;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Receive.Reference.*;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reference message parser.
 */
class ParserReference implements IParser {

    @Override
    public RecvMsg parse(String msg) throws ParserException, ReceiveException {

        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }

        try {
            int id = Integer.valueOf(matcher.group(1));            
            String title = SyncUtils.unEscape(matcher.group(2));
            String notes = SyncUtils.unEscape(matcher.group(3));
            int topicID = Integer.valueOf(matcher.group(4));
            String change = matcher.group(5);

            return new RecvMsgReference(id, title, notes, topicID, change);
        }
        catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Reference parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }

    private static final String CODE = RecvMsg.Type.REFERENCE.toString();

    private static final String REGEXP =
            CODE + RE_SEP +
            ID + RE_ID_GRP +
            TITLE + RE_TITLE_GRP +
            NOTES + RE_NOTES_GRP +
            TOPIC_ID + RE_TOPIC_ID_GRP + 
            CHANGE + RE_CHANGE_NOT_NULL_GRP;

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

}

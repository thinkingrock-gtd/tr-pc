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

import au.com.trgtd.tr.sync.device.Constants;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.exception.ParserException;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Receive.UpdProject.*;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgProject;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Update action message parser.
 */
class ParserUpdProject implements IParser {

    @Override
    public RecvMsg parse(String msg) throws ParserException {
        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }
        try {
            int id = Integer.valueOf(matcher.group(1));            
            String notes =      SyncUtils.unEscape(matcher.group(2));
            String purpose =    SyncUtils.unEscape(matcher.group(3));
            String vision =     SyncUtils.unEscape(matcher.group(4));
            String brainstorm = SyncUtils.unEscape(matcher.group(5));
            String organise =   SyncUtils.unEscape(matcher.group(6));
            Date dueDate = matcher.group(7) == null ? null : Constants.DF_DATE.parse(matcher.group(7));

            return new RecvMsgProject(id, notes, purpose, vision, brainstorm, organise, dueDate);

        } catch (ParseException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        } catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Project update parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }


    public static final String CODE = RecvMsg.Type.UPDPROJECT.toString();

    public static final String REGEXP =
            CODE + RE_SEP +
            ID + RE_ID_GRP +
            NOTES + RE_NOTES_GRP +
            PURPOSE + RE_ANY_GRP +
            VISION + RE_ANY_GRP +
            BRAINSTORM + RE_ANY_GRP +
            ORGANISE + RE_ANY_GRP +
            DUE + RE_DATE_GRP;

    public static final Pattern PATTERN = Pattern.compile(REGEXP);

}

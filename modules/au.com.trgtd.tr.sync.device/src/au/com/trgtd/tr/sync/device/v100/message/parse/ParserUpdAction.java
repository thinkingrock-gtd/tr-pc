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
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Receive.UpdAction.*;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgAction;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Update action message parser.
 */
class ParserUpdAction implements IParser {

    @Override
    public RecvMsg parse(String msg) throws ParserException {
        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }
        try {
            int id = Integer.valueOf(matcher.group(1));
//          boolean done = Boolean.valueOf(matcher.group(2));
            boolean done;
            Date doneDate;
            Long doneTime = matcher.group(2) == null ? null : Long.valueOf(matcher.group(2));
            if (doneTime == null || doneTime == 0) {
                done = false;
                doneDate = null;
            } else {
                done = true;
                doneDate = new Date(doneTime);
            }
            String notes = SyncUtils.unEscape(matcher.group(3));
            Date date = matcher.group(4) == null ? null : Constants.DF_DATE.parse(matcher.group(4));
            Integer startHr = matcher.group(5) == null ? null : Integer.valueOf(matcher.group(5));
            Integer startMn = matcher.group(6) == null ? null : Integer.valueOf(matcher.group(6));
            Integer lengthHrs = matcher.group(7) == null ? null : Integer.valueOf(matcher.group(7));
            Integer lengthMns = matcher.group(8) == null ? null : Integer.valueOf(matcher.group(8));
            
            return new RecvMsgAction(id, done, doneDate, notes, date, startHr, startMn, lengthHrs, lengthMns);
        } catch (ParseException | IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Action update parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }

    public static final String CODE = RecvMsg.Type.UPDACTION.toString();

    public static final String REGEXP =
            CODE + RE_SEP +
            ID + RE_ID_GRP +
//          DONE + RE_BOOL_NOT_NULL_GRP +
            DONE + RE_NUM_GRP +
            NOTES + RE_NOTES_GRP +
            DATE + RE_DATE_GRP +
            START_HR + RE_HR_GRP +
            START_MN + RE_MN_GRP +
            LENGTH_HRS + RE_HR_GRP +
            LENGTH_MNS + RE_MN_GRP;

    public static final Pattern PATTERN = Pattern.compile(REGEXP);

}

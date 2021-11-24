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

import au.com.trgtd.tr.sync.device.exception.ReceiveException;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message parser.
 *
 * @author Jeremy Moore
 */
public class Parser implements IParser {

    private final ParserSummary parserSummary;
    private final ParserNewThought parserNewThgt;
    private final ParserUpdAction parserUpdActn;
    private final ParserUpdProject parserUpdProject;
    private final ParserDefault parserDefault;
    private final ParserReference parserReferences;

    public Parser() {
        parserSummary = new ParserSummary();
        parserNewThgt = new ParserNewThought();
        parserUpdActn = new ParserUpdAction();
        parserUpdProject = new ParserUpdProject();
        parserDefault = new ParserDefault();
        parserReferences = new ParserReference();
    }

    @Override
    public RecvMsg parse(String string) throws Exception {

        RecvMsg.Type type = RecvMsg.Type.fromCode(getMsgCode(string));

        switch (type) {
            case SUMMARY:
                return parserSummary.parse(string);
            case NEWTHOUGHT:
                return parserNewThgt.parse(string);
            case UPDACTION:
                return parserUpdActn.parse(string);
            case UPDPROJECT:
                return parserUpdProject.parse(string);
            case REFERENCE:
                return parserReferences.parse(string);
            default:
                return parserDefault.getMsg(type);
        }
    }

    public static final String REGEXP_CODE = "([A-Z]+)[\\|]?";
    public static final Pattern PATTERN_CODE = Pattern.compile(REGEXP_CODE);

    /*
     * Gets the message code from a message string. The message code is either
     * terminated with the delimeter character, or is the whole string.
     * @param string The message string.
     * @return message code string.
     * @throws Exception if the code is invalid.
     */
    private static String getMsgCode(String string) throws Exception
    {
        if (string == null) {
            throw new IllegalArgumentException("Message string is null.");
        }
        Matcher matcher = PATTERN_CODE.matcher(string);
        if (!matcher.find()) {
            throw new ReceiveException("Invalid message: " + string);
        }
        try {
            return matcher.group(1);
        } catch (Exception ex) {
            throw new ReceiveException("Invalid message: " + string);
        }
    }


}

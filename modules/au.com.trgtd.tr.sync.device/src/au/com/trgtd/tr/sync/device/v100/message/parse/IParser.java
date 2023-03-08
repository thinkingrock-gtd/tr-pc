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

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;

/**
 * Received message parser.
 *
 * @author Jeremy Moore
 */
public interface IParser {

    /**
     * Parses a received message string and returns a message object.
     * @param msg The message string.
     * @return a message object.
     * @throws MsgParserException if the string can not be parsed.
     */
    public RecvMsg parse(String msg) throws Exception;

    
    // Regular Expressions
    /** RE: escaped delimiter character. */
    public static final String RE_SEP = "\\|";

    /** RE: number: null, one or more digits. */
    public static final String RE_NUM = "(\\d+)?";
    /** RE: number group e.g. |123| */
    public static final String RE_NUM_GRP = RE_SEP + RE_NUM + RE_SEP;
    
    /** RE: number: not null, one or more digits. */
    public static final String RE_NUM_NOT_NULL = "(\\d+)";
    /** RE: number one or more digits, not null group e.g. |123| */
    public static final String RE_NUM_NOT_NULL_GRP = RE_SEP + RE_NUM_NOT_NULL + RE_SEP;    

    /** RE: boolean: null, "true" or "false". */
    public static final String RE_BOOL = "(true|false)?";
    /** RE: boolean group e.g. |true| */
    public static final String RE_BOOL_GRP = RE_SEP + RE_BOOL + RE_SEP;

    /** RE: boolean: not null, "true" or "false". */
    public static final String RE_BOOL_NOT_NULL = "(true|false)";
    /** RE: boolean not null group e.g. |true| */
    public static final String RE_BOOL_NOT_NULL_GRP = RE_SEP + RE_BOOL_NOT_NULL + RE_SEP;

    /** RE: any characters except SEP: one or more or null. */
    public static final String RE_ANY = "([^\\|]+)?";
    /** RE: any characters group e.g. |blah 8797-\/fnah...%&%&%&fjka| */
    public static final String RE_ANY_GRP = RE_SEP + RE_ANY + RE_SEP;

    /** RE: any characters except SEP: one or more, not null. */
    public static final String RE_ANY_NOT_NULL = "([^\\|]+)";
    /** RE: any characters not null group e.g. |blah 8797-\/fnah...%&%&%&fjka| */
    public static final String RE_ANY_NOT_NULL_GRP = RE_SEP + RE_ANY_NOT_NULL + RE_SEP;

    /** RE: date: null or eight numeric digits. */
    public static final String RE_DATE = "(\\d{8})?";
    /** RE: date group e.g. |20101225| */
    public static final String RE_DATE_GRP = RE_SEP + RE_DATE + RE_SEP;

    /** RE: date: not null, eight numeric digits. */
    public static final String RE_DATE_NOT_NULL = "(\\d{8})";
    /** RE: date not null group e.g. |20101225| */
    public static final String RE_DATE_NOT_NULL_GRP = RE_SEP + RE_DATE_NOT_NULL + RE_SEP;

//    /** RE: escaped version number. */
//    public static final String RE_VER = Constants.VERSION.replace(".", "\\.");
//    /** RE: version group e.g. |1.0.2| */
//    public static final String RE_VER_GRP = RE_SEP + RE_VER + RE_SEP;
    /** RE: escaped version number. */
    public static final String RE_VER = "(\\d+\\.\\d+\\.\\d+)";
    /** RE: version group e.g. |1.0.2| */
    public static final String RE_VER_GRP = RE_SEP + RE_VER + RE_SEP;

    /** RE: ID number. */
    public static final String RE_ID = RE_NUM_NOT_NULL;
    /** RE: ID number group e.g. |123| */
    public static final String RE_ID_GRP = RE_NUM_NOT_NULL_GRP;

    /** RE: Topic ID number. */
    public static final String RE_TOPIC_ID = RE_NUM_NOT_NULL;
    /** RE: Topic ID number group e.g. |123| */
    public static final String RE_TOPIC_ID_GRP = RE_NUM_NOT_NULL_GRP;

    /** RE: Title, one or more of any characters. */
//  public static final String RE_TITLE = RE_ANY_NOT_NULL;
    public static final String RE_TITLE = RE_ANY;
    /** RE: Title group e.g. |Mow the lawn| */
//  public static final String RE_TITLE_GRP = RE_ANY_NOT_NULL_GRP;
    public static final String RE_TITLE_GRP = RE_ANY_GRP;

    /** RE: Notes, null or any characters. */
    public static final String RE_NOTES = RE_ANY;
    /** RE: Topic ID number group e.g. |123| */
    public static final String RE_NOTES_GRP = RE_ANY_GRP;

    /** RE: hour, null or two numeric digits. */
    public static final String RE_HR = "(\\d{1,2})?";
    /** RE: hour group e.g. |06| */
    public static final String RE_HR_GRP = RE_SEP + RE_HR + RE_SEP;

    /** RE: minute, null, one or two numeric digits. */
    public static final String RE_MN = "(\\d{1,2})?";
    /** RE: minute group e.g. |05| */
    public static final String RE_MN_GRP = RE_SEP + RE_MN + RE_SEP;

    /** RE: Change: not null, "D", "I" or "U". */
    public static final String RE_CHANGE_NOT_NULL = "(D|I|U)";
    /** RE: Change not null group e.g. |D| */
    public static final String RE_CHANGE_NOT_NULL_GRP = RE_SEP + RE_CHANGE_NOT_NULL + RE_SEP;
    
}

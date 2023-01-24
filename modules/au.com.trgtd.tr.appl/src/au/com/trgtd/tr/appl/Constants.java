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
package au.com.trgtd.tr.appl;

import java.awt.Color;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;
import org.openide.util.Utilities;

/**
 * Application constants.
 *
 * @author Jeremy Moore
 */
public class Constants {

    /**
     * Application title
     */
    public static final String TITLE = "ThinkingRock";
    /**
     * String value for the company.
     */
    public static final String COMPANY = "Avente Pty. Ltd.";
    /**
     * String value for the web site address.
     */
    public static final String WEB_SITE = "https://trgtd.com.au";
    /**
     * Application version. For all versions that are not pre-release, the
     * format is:
     *
     * major.minor.revision
     *
     * e.g. 2.0.0.
     *
     * For all pre-release versions it is in format:
     *
     * major.minor.revision.type.n
     *
     * e.g. 2.0.0.beta.1
     */
//  public static final String VERSION = "3.0.3";
//  public static final String VERSION = "3.1.2";
//  public static final String VERSION = "3.4.1";
//  public static final String VERSION = "3.4.2";
//  public static final String VERSION = "3.5.1";
//  public static final String VERSION = "3.6.0";
//  public static final String VERSION = "3.7.0";
    public static final String VERSION = "4.0.0.beta.1";

    /**
     * String URL of the file on the web site that contains the latest version.
     */
    public static final String VERSION_FILE_URL = "https://trgtd.com.au/version.php";

    /**
     * String URL of the file on the web site that contains messages.
     */
    public static final String MESSAGES_FILE_URL = "https://trgtd.com.au/messages.xml";

    /**
     * Get the full path for some preferences. Note that the preferences version
     * does not correspond the program version (i.e 3.5.1 is correct).
     * @param relativePath The relative path. No initial forward slash.
     * @return The preferences path.
     * @throws IllegalArgumentException if relative path is null, empty or has
     * an initial slash character.
     */
    private static String getPrefsPath(String relativePath) {
        final String str = relativePath == null ? "" : relativePath.trim();
        if (str.isEmpty() || str.startsWith("/")) {
            throw new IllegalArgumentException("Bad preferences path: " + relativePath);
        }
        // Don't change version
        return "ThinkingRock/3.5.1/" + str;
    }

    /**
     * Gets preferences given a relative path. 
     * @param relativePath The relative path. No initial forward slash.
     * @return Preferences for the given relative path.
     * @throws IllegalArgumentException if relative path is null, empty or has
     * an initial slash character.
     */
    public static final Preferences getPrefs(String relativePath) {
        return Preferences.userRoot().node(getPrefsPath(relativePath));
    }
    
//    /** String value of the copyright unicode character. */
//    public static final String STRING_COPYRIGHT = String.valueOf('\u00A9');
//    /** String value of the left arrow unicode character. */
//    public static final String STRING_ARROW_LEFT = String.valueOf('\u2190');
//    /** String value of the up arrow unicode character. */
//    public static final String STRING_ARROW_UP = String.valueOf('\u2191');
//    /** String value of the right arrow unicode character. */
//    public static final String STRING_ARROW_RIGHT =String.valueOf('\u2192');
//    /** String value of the down arrow unicode character. */
//    public static final String STRING_ARROW_DOWN = String.valueOf('\u2193');
    /**
     * String value of the minus sign unicode character.
     */
    public static final String STRING_MINUS = String.valueOf('\u2212');
    /**
     * String value of the plus sign unicode character.
     */
    public static final String STRING_PLUS = String.valueOf('\u002B');
//    /** String value of the semi circle arrow unicode character. */
//    public static final String STRING_REPROCESS = STRING_ARROW_LEFT;
//    /** String value of the delete right unicode character. */
//    public static final String STRING_DELETE_RIGHT = String.valueOf('\u2326');
//    /** String value of the meta unicode character. */
//    public static final String STRING_META = String.valueOf('\u2318');
//    /** String value of the control unicode character. */
//    public static final String STRING_CTRL = String.valueOf('\u2303');

    /**
     * Date format used.
     */
    public static final DateFormat DATE_FORMAT_SHORT
            = DateFormat.getDateInstance(DateFormat.SHORT);
    public static final DateFormat DATE_FORMAT_MEDIUM
            = DateFormat.getDateInstance(DateFormat.MEDIUM);
    public static final DateFormat DATE_FORMAT_LONG
            = DateFormat.getDateInstance(DateFormat.LONG);
    public static final DateFormat DATE_FORMAT_FULL
            = DateFormat.getDateInstance(DateFormat.FULL);
    public static final DateFormat DATE_FORMAT_FIXED
            = new SimpleDateFormat("EEE, d MMM yyyy");
    public static final DateFormat DATE_TIME_FORMAT_FIXED
            = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    /**
     * Date format for timestamps.
     */
    public static final DateFormat DF_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * Maximum number of rows to display for combo box lists
     */
    public static final int COMBO_MAX_ROWS = 30;
    // ID number constants
    /**
     * The special ID number for the root project.
     */
    public static final int ID_ROOT_ALL = 0;
    /**
     * The special ID number for the root project for projects.
     */
    public static final int ID_ROOT_PROJECTS = 1;
    /**
     * The special ID number for the root project for single actions.
     */
    public static final int ID_ROOT_ACTIONS = 2;
    /**
     * The special ID number for the root project for template projects.
     */
    public static final int ID_ROOT_TEMPLATES = 3;
    /**
     * The special ID number for the root project for future projects.
     */
    public static final int ID_ROOT_FUTURES = 4;
    /**
     * The special ID number for filter objects representing the All choice.
     */
    public static final int ID_FILTER_ALL = 5;
    /**
     * The special ID number for filter objects representing the Multiple
     * choice.
     */
    public static final int ID_FILTER_MULTIPLE = 6;
    /**
     * The special ID number for filter objects representing the Edit Multiple
     * choice.
     */
    public static final int ID_FILTER_MULTIPLE_EDIT = 7;
    /**
     * The special ID number for the default topic.
     */
    public static final int ID_DEFAULT_TOPIC = 8;
    /**
     * The special ID number for the default context.
     */
    public static final int ID_DEFAULT_CONTEXT = 9;

    /**
     * The special ID number for filter contexts representing the All choice.
     */
    public static final int ID_FILTER_CONTEXTS_ALL = 10;
    /**
     * The special ID number for filter contexts representing the Multiple
     * choice.
     */
    public static final int ID_FILTER_CONTEXTS_MULTIPLE = 11;
    /**
     * The special ID number for filter contexts representing the Edit Multiple
     * choice.
     */
    public static final int ID_FILTER_CONTEXTS_MULTIPLE_EDIT = 12;
    /**
     * The special ID number for filter topics representing the All choice.
     */
    public static final int ID_FILTER_TOPICS_ALL = 13;
    /**
     * The special ID number for filter topics representing the Multiple choice.
     */
    public static final int ID_FILTER_TOPICS_MULTIPLE = 14;
    /**
     * The special ID number for filter topics representing the Edit Multiple
     * choice.
     */
    public static final int ID_FILTER_TOPICS_MULTIPLE_EDIT = 15;

    /**
     * The special ID number for root of goals.
     */
    public static final int ID_ROOT_GOAL = 16;
    /**
     * The special ID number for root of levels.
     */
    public static final int ID_ROOT_LEVEL = 17;

    /**
     * The maximum special ID number. Normal ID numbers start after this number.
     */
    public static final int ID_MAX_SPECIAL = 100;
    // End of ID number constants
    /**
     * Icon button type
     */
    public static final String BUTTON_TYPE_ICON = "icon";
    /**
     * Text button type
     */
    public static final String BUTTON_TYPE_TEXT = "text";
    /**
     * Tool bar button type
     */
    public static final String BUTTON_TYPE_TOOLBAR = "toolbar";
    /**
     * Button type property *
     */
    public static final String BUTTON_TYPE = "JButton.buttonType";
    /**
     * Icon button type value *
     */
    public static final String ICON = "icon";
    /**
     * Button size for tool bar buttons. *
     */
    public static final Dimension TOOLBAR_BUTTON_SIZE
            = Utilities.isMac() ? new Dimension(28, 28) : new Dimension(24, 24);
    /**
     * Color of dates past
     */
    public static final Color COLOR_DATE_PAST = Color.red;
    /**
     * Color of dates today
     */
    public static final Color COLOR_DATE_TODAY = Color.blue;
    /**
     * Color of dates future week
     */
    public static final Color COLOR_DATE_WEEK = new Color(0, 102, 0);

}

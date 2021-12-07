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
package au.com.trgtd.tr.view.actns.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User preferences for actions screen.
 *
 * @author Jeremy Moore
 */
public class ActionsPrefs {

    private static final Logger LOG = Logger.getLogger("tr.view.actions.prefs");

    private static final Preferences PREFS = Constants.getPrefs("screens/actions");

    private static final String KEY_REPORT_USE_COLOUR = "report.colour.";
    private static final String KEY_REPORT_STRIKE_DONE = "report.strike.";
    private static final String KEY_REPORT_FONT = "report.font";
    private static final String KEY_VIEW_DELAY = "view.delay";

    private static final boolean DEF_REPORT_USE_COLOUR = true;
    private static final boolean DEF_REPORT_STRIKE_DONE = true;
    private static final String DEF_REPORT_FONT = "Helvetica";
    private static final int DEF_VIEW_DELAY = 200;
    
    /**
     * Gets the preferences.
     * @return The preferences.
     */
    public static final Preferences getPrefs() {
        return PREFS;
    }

    /**
     * Gets the value for the report use color preference.
     * @return The value.
     */
    public static final boolean isReportUseColour() {
        return PREFS.getBoolean(KEY_REPORT_USE_COLOUR, DEF_REPORT_USE_COLOUR);
    }
    /**
     * Sets the value for the report use color preference.
     * @param value The value.
     */
    public static final void setReportUseColour(boolean value) {
        PREFS.putBoolean(KEY_REPORT_USE_COLOUR, value);
        flush();
    }
    
    /**
     * Gets the value for the report strike done preference.
     * @return The value.
     */
    public static final boolean isReportStrikeDone() {
        return PREFS.getBoolean(KEY_REPORT_STRIKE_DONE, DEF_REPORT_STRIKE_DONE);
    }
    /**
     * Sets the value for the report strike done preference.
     * @param value The value.
     */
    public static final void setReportStrikeDone(boolean value) {
        PREFS.putBoolean(KEY_REPORT_STRIKE_DONE, value);
        flush();
    }
    
    /**
     * Gets the value for the report font preference.
     * @return The value.
     */
    public static final String getReportFont() {
        return PREFS.get(KEY_REPORT_FONT, DEF_REPORT_FONT);
    }
    
    /**
     * Sets the value for the report font preference.
     * @param value The value.
     */
    public static final void setReportFont(String value) {
        PREFS.put(KEY_REPORT_FONT, value);
        flush();
    }

    /**
     * Gets the value for the view delay preference.
     * @return The value.
     */
    public static final int getViewDelay() {
        return PREFS.getInt(KEY_VIEW_DELAY, DEF_VIEW_DELAY);
    }
    
    /**
     * Sets the value for the view delay preference.
     * @param value The value.
     */
    public static final void setViewDelay(int value) {
        PREFS.putInt(KEY_VIEW_DELAY, value);
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Actions preferences error. {0}", ex.getMessage());
        }
    }
    
}

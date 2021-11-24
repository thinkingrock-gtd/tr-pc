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
package au.com.trgtd.tr.prefs.ui;

import au.com.trgtd.tr.appl.Constants;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JToolBar;

/**
 * User preferences for the GUI.
 *
 * @author Jeremy Moore
 */
public class GUIPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.gui");

    private static final Preferences PREFS = Constants.getPrefs("gui");         
    
    /** No initial action. */
    public static final String INITIAL_ACTION_ID_NONE = "none";
    /* Initial action preference key. */
    private static final String KEY_INITIAL_ACTION_ID = "initial.action.id";
    /* Initial action preference default. */
    private static final String DEF_INITIAL_ACTION_ID = "overview";
    /** Position buttons at the top of windows. */
    public static final String BUTTONS_POSITION_TOP = "top";
    /** Position buttons at the bottom of windows. */
    public static final String BUTTONS_POSITION_BOTTOM = "bottom";
    /** Position buttons at the left of windows. */
    public static final String BUTTONS_POSITION_LEFT = "left";
    /** Position buttons at the right of windows. */
    public static final String BUTTONS_POSITION_RIGHT = "right";
    /* Buttions position preference key. */
    private static final String KEY_BUTTONS_POSITION = "buttons.position";
    /* Buttions position preference default. */
    private static final String DEF_BUTTONS_POSITION = BUTTONS_POSITION_TOP;

    /**
     * Gets the value for the initial action identifier preference.
     * @return The value.
     */
    public static final String getInitialActionID() {
        return PREFS.get(KEY_INITIAL_ACTION_ID, DEF_INITIAL_ACTION_ID);
    }
    
    /**
     * Sets the value for the initial action identifier preference.
     * @param value The value.
     */
    public static final void setInitialWindowID(String value) {
        PREFS.put(KEY_INITIAL_ACTION_ID, value);
        flush();
    }
    
    /**
     * Gets the value for the buttons position preference.
     * @return The value.
     */
    public static final String getButtonsPosition() {
        return PREFS.get(KEY_BUTTONS_POSITION, DEF_BUTTONS_POSITION);
    }
    
    /**
     * Gets the BorderLayout value for the buttons position preference.
     * @return The BorderLayout position value.
     */
    public static final String getBorderLayoutButtonsPosition() {
        String bp = getButtonsPosition();
        if (bp.equals(BUTTONS_POSITION_TOP)) return BorderLayout.NORTH;
        if (bp.equals(BUTTONS_POSITION_BOTTOM)) return BorderLayout.SOUTH;
        if (bp.equals(BUTTONS_POSITION_LEFT)) return BorderLayout.WEST;
        if (bp.equals(BUTTONS_POSITION_RIGHT)) return BorderLayout.EAST;
        return BorderLayout.NORTH;
    }
    
    /**
     * Gets the JToolBar orientation for the buttons position preference.
     * @return The JToolBar orientation value.
     */
    public static final int getToolBarOrientation() {
        String bp = getButtonsPosition();
        if (bp.equals(BUTTONS_POSITION_TOP)) return JToolBar.HORIZONTAL;
        if (bp.equals(BUTTONS_POSITION_BOTTOM)) return JToolBar.HORIZONTAL;
        if (bp.equals(BUTTONS_POSITION_LEFT)) return JToolBar.VERTICAL;
        if (bp.equals(BUTTONS_POSITION_RIGHT)) return JToolBar.VERTICAL;
        return JToolBar.HORIZONTAL;
    }
    
    /**
     * Sets the value for the buttons position preference.
     * @param value The value.
     */
    public static final void setButtonsPosition(String value) {
        PREFS.put(KEY_BUTTONS_POSITION, value);
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "GUI preferences error. {0}", ex.getMessage());
        }
    }
    
}

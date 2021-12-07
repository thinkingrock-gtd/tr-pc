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
package au.com.trgtd.tr.prefs.projects;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.NbBundle;

/**
 * User preferences for projects screen.
 */
public class ProjectsPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.projects");
    
    private static final Preferences PREFS = Constants.getPrefs("projects");      
    
    private static final String KEY_AUTOSEQUENCE = "auto.sequence.actions";
    private static final String KEY_AUTOSEQUENCE_SUBPROJECTS = "auto.sequence.include.subprojects";
    private static final String KEY_AUTOSEQUENCE_SCHEDULED = "auto.sequence.include.scheduled";
    private static final String KEY_AUTOSEQUENCE_DELEGATED = "auto.sequence.include.delegated";
    private static final String KEY_INCLUDE_START_DATE = "include.start.date";
    private static final String KEY_INCLUDE_DUE_DATE = "include.due.date";
    private static final String KEY_DEFAULT_ACTION_DUE_DATE = "default.action.due.date";
    private static final String KEY_EDIT_CREATE_DATE = "edit.create-date";
    private static final String KEY_NEW_ACTION_STATE = "new.action.state";
    private static final String KEY_PRINT_FORMAT = "print.format";

    private static final boolean DEF_AUTOSEQUENCE = false;
    private static final boolean DEF_AUTOSEQUENCE_SUBPROJECTS = false;
    private static final boolean DEF_AUTOSEQUENCE_SCHEDULED = false;
    private static final boolean DEF_AUTOSEQUENCE_DELEGATED = false;
    private static final boolean DEF_INCLUDE_START_DATE = true;
    private static final boolean DEF_INCLUDE_DUE_DATE = true;
    private static final boolean DEF_DEFAULT_ACTION_DUE_DATE = true;
    private static final boolean DEF_EDIT_CREATE_DATE = false;
    private static final ActionState DEF_NEW_ACTION_STATE = ActionState.INACTIVE;
    private static final String DEF_PRINT_FORMAT = PageSizeChoice.A4.name();
    
    public enum ActionState {
        INACTIVE(0, NbBundle.getMessage(ProjectsPrefs.class, "ActionStateInactive")),
        DOASAP(1, NbBundle.getMessage(ProjectsPrefs.class, "ActionStateDoASAP")),
        SCHEDULED(2, NbBundle.getMessage(ProjectsPrefs.class, "ActionStateScheduled")),
        DELEGATED(3, NbBundle.getMessage(ProjectsPrefs.class, "ActionStateDelegated"));
        public final int id;
        public final String name;
        private ActionState(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public static ActionState getActionState(int id) {
            if (id == DOASAP.id)    return DOASAP;
            if (id == SCHEDULED.id) return SCHEDULED;
            if (id == DELEGATED.id) return DELEGATED;
            return INACTIVE;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    public static Preferences getPrefs() {
        return PREFS;
    }

    /**
     * Gets the value for the automatic sequence actions preference.
     * @return The value.
     */
    public static final boolean isSequencing() {
        return PREFS.getBoolean(KEY_AUTOSEQUENCE, DEF_AUTOSEQUENCE);
    }
    
    /**
     * Sets the value for the automatic sequence actions preference.
     * @param value The value.
     */
    public static final void setSequencing(boolean value) {
        PREFS.putBoolean(KEY_AUTOSEQUENCE, value);
        flush();
    }

    /**
     * Gets the value for the auto-sequence include sub-projects preference.
     * @return The value.
     */
    public static final boolean getAutoSeqIncludeSubprojects() {
        return PREFS.getBoolean(KEY_AUTOSEQUENCE_SUBPROJECTS, DEF_AUTOSEQUENCE_SUBPROJECTS);
    }

    /**
     * Sets the value for the auto-sequence include sub-projects preference.
     * @param value The value.
     */
    public static final void setAutoSeqIncludeSubprojects(boolean value) {
        PREFS.putBoolean(KEY_AUTOSEQUENCE_SUBPROJECTS, value);
        flush();
    }

    /**
     * Gets the value for the auto-sequence include scheduled preference.
     * @return The value.
     */
    public static final boolean getAutoSeqIncludeScheduled() {
        return PREFS.getBoolean(KEY_AUTOSEQUENCE_SCHEDULED, DEF_AUTOSEQUENCE_SCHEDULED);
    }

    /**
     * Sets the value for the auto-sequence include scheduled preference.
     * @param value The value.
     */
    public static final void setAutoSeqIncludeScheduled(boolean value) {
        PREFS.putBoolean(KEY_AUTOSEQUENCE_SCHEDULED, value);
        flush();
    }

    /**
     * Gets the value for the auto-sequence include delegated preference.
     * @return The value.
     */
    public static final boolean getAutoSeqIncludeDelegated() {
        return PREFS.getBoolean(KEY_AUTOSEQUENCE_DELEGATED, DEF_AUTOSEQUENCE_DELEGATED);
    }

    /**
     * Sets the value for the auto-sequence include delegated preference.
     * @param value The value.
     */
    public static final void setAutoSeqIncludeDelegated(boolean value) {
        PREFS.putBoolean(KEY_AUTOSEQUENCE_DELEGATED, value);
        flush();
    }

    /**
     * Gets the value for the include start date field preference.
     * @return The value.
     */
    public static final boolean isIncludeStartDate() {
        return PREFS.getBoolean(KEY_INCLUDE_START_DATE, DEF_INCLUDE_START_DATE);
    }
    /**
     * Sets the value for the include start date field preference.
     * @param value The value.
     */
    public static final void setIncludeStartDate(boolean value) {
        PREFS.putBoolean(KEY_INCLUDE_START_DATE, value);
        flush();
    }
    
    /**
     * Gets the value for the include due date field preference.
     * @return The value.
     */
    public static final boolean isIncludeDueDate() {
        return PREFS.getBoolean(KEY_INCLUDE_DUE_DATE, DEF_INCLUDE_DUE_DATE);
    }
    /**
     * Sets the value for the  include due date field preference.
     * @param value The value.
     */
    public static final void setIncludeDueDate(boolean value) {
        PREFS.putBoolean(KEY_INCLUDE_DUE_DATE, value);
        flush();
    }
    
    /**
     * Gets the value for the default action due date field preference.
     * @return The value.
     */
    public static final boolean isDefaultActionDueDate() {
        return PREFS.getBoolean(KEY_DEFAULT_ACTION_DUE_DATE, DEF_DEFAULT_ACTION_DUE_DATE);
    }
    /**
     * Sets the value for the  default action due date field preference.
     * @param value The value.
     */
    public static final void setDefaultActionDueDate(boolean value) {
        PREFS.putBoolean(KEY_DEFAULT_ACTION_DUE_DATE, value);
        flush();
    }
    
    /**
     * Gets the value for the new action state preference.
     * @return The value.
     */
    public static final ActionState getNewActionState() {
        int id = PREFS.getInt(KEY_NEW_ACTION_STATE, DEF_NEW_ACTION_STATE.id);
        return ActionState.getActionState(id);
    }
    
    /**
     * Sets the value for the new action state preference.
     * @param value The value.
     */
    public static final void setNewActionState(ActionState value) {
        if (value == null) {
            value = DEF_NEW_ACTION_STATE;
        }
        PREFS.putInt(KEY_NEW_ACTION_STATE, value.id);
        flush();
    }
    
    /**
     * Gets the value for the edit create date preference.
     * @return The value.
     */
    public static final boolean isEditCreateDate() {
        return PREFS.getBoolean(KEY_EDIT_CREATE_DATE, DEF_EDIT_CREATE_DATE);
    }
    /**
     * Sets the value for the edit create date preference.
     * @param value The value.
     */
    public static final void setEditCreateDate(boolean value) {
        PREFS.putBoolean(KEY_EDIT_CREATE_DATE, value);
        flush();
    }

    /**
     * Gets the value for the print format preference.
     * @return The value.
     */
    public static final String getPageSizeChoiceName() {
        return PREFS.get(KEY_PRINT_FORMAT, DEF_PRINT_FORMAT);
    }
    /**
     * Sets the value for the print format default preference.
     * @param value The value.
     */
    public static final void setPrintFormat(String value) {
        PREFS.put(KEY_PRINT_FORMAT, value);
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Projects preferences error. {0}", ex.getMessage());
        }
    }
    
}

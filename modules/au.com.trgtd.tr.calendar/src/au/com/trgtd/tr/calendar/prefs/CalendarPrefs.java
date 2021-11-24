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
package au.com.trgtd.tr.calendar.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * ICalendar preferences.
 *
 * @author Jeremy Moore
 */
public class CalendarPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.calendar");
    
    private static final Preferences PREFS = Constants.getPrefs("calendar");
    
    private static final String KEY_REQUIRED = "icalendar.required";
    private static final String KEY_ID_SYNCHRONIZER = "id.synchronizer";
    private static final String KEY_SYNC_INACTIVE = "sync.inactive";
    private static final String KEY_TODO_INACTIVE = "todo.inactive";
    private static final String KEY_SYNC_DOASAP_DUE_DATE = "sync.doasap.due.date";
    private static final String KEY_TODO_DOASAP_DUE_DATE = "todo.doasap.due.date";
    private static final String KEY_SYNC_DOASAP_NO_DUE_DATE = "sync.doasap.no.due.date";
    private static final String KEY_SYNC_DELEGATED = "sync.delegated";
    private static final String KEY_TODO_DELEGATED = "todo.delegated";
    private static final String KEY_SYNC_DELEGATED_NO_DATE = "sync.delegated.no.due.date";
    private static final String KEY_SYNC_SCHEDULED = "sync.scheduled";
    private static final String KEY_SYNC_PROJECTS = "sync.projects";
    private static final String KEY_SYNC_PROJECTS_SEP = "sync.projects.separate";
    private static final String KEY_SYNC_PROJECTS_FUTURE = "sync.projects.future";
    private static final String KEY_SYNC_PROJECTS_FUTURE_SEP = "sync.projects.future.separate";
    private static final boolean DEF_REQUIRED = false;
    private static final boolean DEF_SYNC_INACTIVE = false;
    private static final boolean DEF_TODO_INACTIVE = true;
    private static final boolean DEF_SYNC_DOASAP_DUE_DATE = false;
    private static final boolean DEF_TODO_DOASAP_DUE_DATE = true;
    private static final boolean DEF_SYNC_DOASAP_NO_DUE_DATE = false;
    private static final boolean DEF_SYNC_DELEGATED = false;
    private static final boolean DEF_TODO_DELEGATED = true;
    private static final boolean DEF_SYNC_DELEGATED_NO_DATE = false;
    private static final boolean DEF_SYNC_SCHEDULED = false;
    private static final boolean DEF_SYNC_PROJECTS = false;
    private static final boolean DEF_SYNC_PROJECTS_SEP = true;
    private static final boolean DEF_SYNC_PROJECTS_FUTURE = false;
    private static final boolean DEF_SYNC_PROJECTS_FUTURE_SEP = true;

    /**
     * Gets the value for the iCalendar required preference.
     * @return The value.
     */
    public static final boolean isICalendarRequired() {
        return PREFS.getBoolean(KEY_REQUIRED, DEF_REQUIRED);
    }

    /**
     * Sets the value for the ICalendar required preference.
     * @param value The value.
     */
    public static final void setICalendarRequired(boolean value) {
        PREFS.putBoolean(KEY_REQUIRED, value);
        flush();
    }
    
    /**
     * Gets the value for the synchronizer identifier.
     * @return The value.
     */
    public static final String getSynchronizerID() {
        return PREFS.get(KEY_ID_SYNCHRONIZER, "");
    }

    /**
     * Sets the value for the synchronizer identifier.
     * @param value The value.
     */
    public static final void setSynchronizerID(String value) {
        PREFS.put(KEY_ID_SYNCHRONIZER, value);
        flush();
    }
    
    /**
     * Gets the value for the sync Do ASAP actions with a due date preference.
     * @return The value.
     */
    public static final boolean isSyncDoasapDueDate() {
        return PREFS.getBoolean(KEY_SYNC_DOASAP_DUE_DATE, DEF_SYNC_DOASAP_DUE_DATE);
    }

    /**
     * Sets the value for the sync Do ASAP actions with a due date preference.
     * @param value The value.
     */
    public static final void setSyncDoASAPWithDueDate(boolean value) {
        PREFS.putBoolean(KEY_SYNC_DOASAP_DUE_DATE, value);
        flush();
    }
    
    /**
     * Gets the value for the sync Do ASAP actions with no due date preference.
     * @return The value.
     */
    public static final boolean isSyncDoasapNoDueDate() {
        return PREFS.getBoolean(KEY_SYNC_DOASAP_NO_DUE_DATE, DEF_SYNC_DOASAP_NO_DUE_DATE);
    }

    /**
     * Sets the value for the sync Do ASAP actions with no due date preference.
     * @param value The value.
     */
    public static final void setSyncDoASAPWithNoDueDate(boolean value) {
        PREFS.putBoolean(KEY_SYNC_DOASAP_NO_DUE_DATE, value);
        flush();
    }
        
    /**
     * Gets the value for the sync Delegated actions preference.
     * @return The value.
     */
    public static final boolean isSyncDelegated() {
        return PREFS.getBoolean(KEY_SYNC_DELEGATED, DEF_SYNC_DELEGATED);
    }

    /**
     * Sets the value for the sync Delegated actions preference.
     * @param value The value.
     */
    public static final void setSyncDelegated(boolean value) {
        PREFS.putBoolean(KEY_SYNC_DELEGATED, value);
        flush();
    }

    /**
     * Gets the value for the sync Delegated actions with no date preference.
     * @return The value.
     */
    public static final boolean isSyncDelegatedNoDate() {
        return PREFS.getBoolean(KEY_SYNC_DELEGATED_NO_DATE, DEF_SYNC_DELEGATED_NO_DATE);
    }

    /**
     * Sets the value for the sync Delegated actions with no date preference.
     * @param value The value.
     */
    public static final void setSyncDelegatedNoDate(boolean value) {
        PREFS.putBoolean(KEY_SYNC_DELEGATED_NO_DATE, value);
        flush();
    }



    /**
     * Gets the value for the sync Scheduled actions preference.
     * @return The value.
     */
    public static final boolean isSyncScheduled() {
        return PREFS.getBoolean(KEY_SYNC_SCHEDULED, DEF_SYNC_SCHEDULED);
    }

    /**
     * Sets the value for the sync Scheduled actions preference.
     * @param value The value.
     */
    public static final void setSyncScheduled(boolean value) {
        PREFS.putBoolean(KEY_SYNC_SCHEDULED, value);
        flush();
    }
    
    /**
     * Gets the value for the sync Do ASAP actions with a due date as To Do 
     * items preference.
     * @return The value.
     */
    public static final boolean isDoasapDueAsTodo() {
        return PREFS.getBoolean(KEY_TODO_DOASAP_DUE_DATE, DEF_TODO_DOASAP_DUE_DATE);
    }

    /**
     * Sets the value for the sync Do ASAP actions with a due date as To Do 
     * items preference.
     * @param value The value.
     */
    public static final void setDoasapDueAsTodo(boolean value) {
        PREFS.putBoolean(KEY_TODO_DOASAP_DUE_DATE, value);
        flush();
    }
    
    /**
     * Gets the value for the sync Delegated actions as To Do items preference.
     * @return The value.
     */
    public static final boolean isDelegatedAsTodo() {
        return PREFS.getBoolean(KEY_TODO_DELEGATED, DEF_TODO_DELEGATED);
    }
    /**
     * Sets the value for the sync Delegated actions as To Do items preference.
     * @param value The value.
     */
    public static final void setDelegatedAsTodo(boolean value) {
        PREFS.putBoolean(KEY_TODO_DELEGATED, value);
        flush();
    }
        
    /**
     * Gets the value for the sync Inactive actions with a start date preference.
     * @return The value.
     */
    public static final boolean isSyncInactive() {
        return PREFS.getBoolean(KEY_SYNC_INACTIVE, DEF_SYNC_INACTIVE);
    }
    /**
     * Sets the value for the sync Inactive actions with a start date preference.
     * @param value The value.
     */
    public static final void setSyncInactive(boolean value) {
        PREFS.putBoolean(KEY_SYNC_INACTIVE, value);
        flush();
    }

    /**
     * Gets the value for the sync Inactive actions as To Do items preference.
     * @return The value.
     */
    public static final boolean isInactiveAsTodo() {
        return PREFS.getBoolean(KEY_TODO_INACTIVE, DEF_TODO_INACTIVE);
    }
    /**
     * Sets the value for the sync Inactive actions as To Do items preference.
     * @param value The value.
     */
    public static final void setInactiveAsTodo(boolean value) {
        PREFS.putBoolean(KEY_TODO_INACTIVE, value);
        flush();
    }

    /**
     * Gets the value for the sync projects preference.
     * @return The value.
     */
    public static final boolean isSyncProjects() {
        return PREFS.getBoolean(KEY_SYNC_PROJECTS, DEF_SYNC_PROJECTS);
    }
    /**
     * Sets the value for the sync projects preference.
     * @param value The value.
     */
    public static final void setSyncProjects(boolean value) {
        PREFS.putBoolean(KEY_SYNC_PROJECTS, value);
        flush();
    }

    /**
     * Gets the value for the sync future projects preference.
     * @return The value.
     */
    public static final boolean isSyncFutureProjects() {
        return PREFS.getBoolean(KEY_SYNC_PROJECTS_FUTURE, DEF_SYNC_PROJECTS_FUTURE);
    }
    /**
     * Sets the value for the sync future projects preference.
     * @param value The value.
     */
    public static final void setSyncFutureProjects(boolean value) {
        PREFS.putBoolean(KEY_SYNC_PROJECTS_FUTURE, value);
        flush();
    }

    /**
     * Gets the value for the sync projects in a separate file preference.
     * @return The value.
     */
    public static final boolean isSyncProjectsSeparate() {
        return PREFS.getBoolean(KEY_SYNC_PROJECTS_SEP, DEF_SYNC_PROJECTS_SEP);
    }
    /**
     * Sets the value for the sync projects in a separate file preference.
     * @param value The value.
     */
    public static final void setSyncProjectsSeparate(boolean value) {
        PREFS.putBoolean(KEY_SYNC_PROJECTS_SEP, value);
        flush();
    }

    /**
     * Gets the value for the sync future projects in a separate file preference.
     * @return The value.
     */
    public static final boolean isSyncFutureProjectsSeparate() {
        return PREFS.getBoolean(KEY_SYNC_PROJECTS_FUTURE_SEP, DEF_SYNC_PROJECTS_FUTURE_SEP);
    }
    /**
     * Sets the value for the sync future projects in a separate file preference.
     * @param value The value.
     */
    public static final void setSyncFutureProjectsSeparate(boolean value) {
        PREFS.putBoolean(KEY_SYNC_PROJECTS_FUTURE_SEP, value);
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Calendar preferences error. {0}", ex.getMessage());
        }
    }
    
}

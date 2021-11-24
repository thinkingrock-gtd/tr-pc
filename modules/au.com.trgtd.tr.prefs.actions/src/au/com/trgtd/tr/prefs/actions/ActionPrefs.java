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
package au.com.trgtd.tr.prefs.actions;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * User preferences for actions.
 *
 * @author Jeremy Moore
 */
public class ActionPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.actions");
   
    private static final Preferences PREFS = Constants.getPrefs("actn");      
    
    /** Note delegate email key. */
    public static final String KEY_NOTE_DELEGATE_EMAIL = "note.delegate.email";
    private static final boolean DEF_NOTE_DELEGATE_EMAIL = true;
    /** Include due date field key. */
    public static final String KEY_SHOW_DUE = "show.due";
    private static final boolean DEF_SHOW_DUE = true;
    /** Include start date field key. */
    public static final String KEY_SHOW_START = "show.start";
    // Include start date field default value.
    private static final boolean DEF_SHOW_START = true;
    /** Show success outcome key. */
    public static final String KEY_SHOW_SUCCESS = "show.success";
    private static final boolean DEF_SHOW_SUCCESS = true;
    /** Edit creation date key. */
    public static final String KEY_EDIT_CREATE_DATE = "edit.create-date";
    private static final boolean DEF_EDIT_CREATE_DATE = false;
    private static final String KEY_ENCODING = "email.encoding";
    private static final String DEF_ENCODING = "UTF-8";
    private static final String DEF_ENCODING_LINUX = "";
    private static final String KEY_NEW_ACTION_STATE = "new.action.state";
    private static final ActionState DEF_NEW_ACTION_STATE = ActionState.DOASAP;
    
    private static Boolean isNoteDelegateEmail;
    private static Boolean isShowSuccess;
    private static Boolean isShowDueDate;
    private static Boolean isShowStartDate;
    private static Boolean isEditCreateDate;
    
    // Preference key for the default mode of combo for the delegate field.
    private static final String KEY_DELEGATE_MODE_COMBO = "delegate.mode.combo";
    private static final boolean DEF_DELEGATE_MODE_COMBO = true;
    
    // Scheduled time hour default setting. 
    private static final String KEY_SCHD_TIME_HR = "schd.time.hr";
    // Scheduled time minute default setting. 
    private static final String KEY_SCHD_TIME_MN = "schd.time.mn";
    // Scheduled duration hours default setting. 
    private static final String KEY_SCHD_DUR_HRS = "schd.dur.hrs";
    // Scheduled duration minutes default setting. 
    private static final String KEY_SCHD_DUR_MNS = "schd.dur.mns";

    public enum ActionState {

        INACTIVE(0, NbBundle.getMessage(ActionPrefs.class, "ActionStateInactive")),
        DOASAP(1, NbBundle.getMessage(ActionPrefs.class, "ActionStateDoASAP")),
        SCHEDULED(2, NbBundle.getMessage(ActionPrefs.class, "ActionStateScheduled")),
        DELEGATED(3, NbBundle.getMessage(ActionPrefs.class, "ActionStateDelegated"));
        public final int id;
        public final String name;

        private ActionState(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static ActionState getActionState(int id) {
            if (id == DOASAP.id) {
                return DOASAP;
            }
            if (id == SCHEDULED.id) {
                return SCHEDULED;
            }
            if (id == DELEGATED.id) {
                return DELEGATED;
            }
            return INACTIVE;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Gets the preferences.
     * @return The preferences.
     */
    public static Preferences getPrefs() {
        return PREFS;
    }

    /**
     * Gets the value for the note delegate email preference.
     * @return The value.
     */
    public static boolean isNoteDelegateEmail() {
        isNoteDelegateEmail = PREFS.getBoolean(KEY_NOTE_DELEGATE_EMAIL, DEF_NOTE_DELEGATE_EMAIL);
        return isNoteDelegateEmail;
    }

    /**
     * Sets the value for the note delegate email preference.
     * @param value The value.
     */
    public static void setNoteDelegateEmail(boolean value) {
        if (value == isNoteDelegateEmail) {
            return;
        }

        PREFS.putBoolean(KEY_NOTE_DELEGATE_EMAIL, value);

        flush();

        isNoteDelegateEmail = value;
    }

    /**
     * Gets the value for the show due date field preference.
     * @return The value.
     */
    public static boolean isShowDueDate() {
        isShowDueDate = PREFS.getBoolean(KEY_SHOW_DUE, DEF_SHOW_DUE);
        return isShowDueDate;
    }

    /**
     * Sets the value for the show due date field preference.
     * @param value The value.
     */
    public static void setShowDueDate(boolean value) {
        if (value == isShowDueDate) {
            return;
        }

        PREFS.putBoolean(KEY_SHOW_DUE, value);
        flush();

        isShowDueDate = value;
    }

    /**
     * Gets the value for the show start date field preference.
     * @return The value.
     */
    public static boolean isShowStartDate() {
        isShowStartDate = PREFS.getBoolean(KEY_SHOW_START, DEF_SHOW_START);
        return isShowStartDate;
    }

    /**
     * Sets the value for the show start date field preference.
     * @param value The value.
     */
    public static void setShowStartDate(boolean value) {
        if (value == isShowStartDate) {
            return;
        }

        PREFS.putBoolean(KEY_SHOW_START, value);
        flush();

        isShowStartDate = value;
    }

    /**
     * Gets the value for the show success outcome preference.
     * @return The value.
     */
    public static boolean isShowSuccess() {
        isShowSuccess = PREFS.getBoolean(KEY_SHOW_SUCCESS, DEF_SHOW_SUCCESS);
        return isShowSuccess;
    }

    /**
     * Sets the value for the show success outcome preference.
     * @param value The value.
     */
    public static void setShowSuccess(boolean value) {
        if (value == isShowSuccess) {
            return;
        }

        PREFS.putBoolean(KEY_SHOW_SUCCESS, value);
        flush();

        isShowSuccess = value;
    }

    /**
     * Gets the value for the edit create date preference.
     * @return The value.
     */
    public static boolean isEditCreateDate() {
        isEditCreateDate = PREFS.getBoolean(KEY_EDIT_CREATE_DATE, DEF_EDIT_CREATE_DATE);
        return isEditCreateDate;
    }

    /**
     * Sets the value for the edit create date preference.
     * @param value The value.
     */
    public static void setEditCreateDate(boolean value) {
        if (value == isEditCreateDate) {
            return;
        }

        PREFS.putBoolean(KEY_EDIT_CREATE_DATE, value);
        flush();

        isEditCreateDate = value;
    }

    /**
     * Gets the value for encoding.
     * @return The value.
     */
    public static String getEmailEncoding() {
        if (Utilities.getOperatingSystem() == Utilities.OS_LINUX) {
            return PREFS.get(KEY_ENCODING, DEF_ENCODING_LINUX);
        } else {
            return PREFS.get(KEY_ENCODING, DEF_ENCODING);
        }
    }

    /**
     * Sets the value for encoding.
     * @param value The value.
     */
    public static void setEmailEncoding(String value) {
        PREFS.put(KEY_ENCODING, (value == null) ? "" : value.trim());
        flush();
    }

    /**
     * Gets the value for the new action state preference.
     * @return The value.
     */
    public static ActionState getNewActionState() {
        int id = PREFS.getInt(KEY_NEW_ACTION_STATE, DEF_NEW_ACTION_STATE.id);
        return ActionState.getActionState(id);
    }

    /**
     * Sets the value for the new action state preference.
     * @param value The value.
     */
    public static void setNewActionState(ActionState value) {
        if (value == null) {
            value = DEF_NEW_ACTION_STATE;
        }
        PREFS.putInt(KEY_NEW_ACTION_STATE, value.id);
        flush();
    }

    /**
     * Gets the value for the default delegate mode of combo box preference.
     * @return The value.
     */
    public static boolean isDelegateModeCombo() {
        return PREFS.getBoolean(KEY_DELEGATE_MODE_COMBO, DEF_DELEGATE_MODE_COMBO);
    }

    /**
     * Sets the value for the default delegate mode of combo box preference.
     * @param value The value.
     */
    public static void setDelegateModeCombo(boolean value) {
        PREFS.putBoolean(KEY_DELEGATE_MODE_COMBO, value);
        flush();
    }
    
    /**
     * Gets the scheduled time hour default setting.
     * @return The value.
     */
    public static int getSchdTimeHr() {
        return PREFS.getInt(KEY_SCHD_TIME_HR, 0);
    }
    
    /**
     * Gets the scheduled time minute default setting.
     * @return The value.
     */
    public static int getSchdTimeMn() {
        return PREFS.getInt(KEY_SCHD_TIME_MN, 0);
    }
    
    /**
     * Gets the scheduled duration hours default setting.
     * @return The value.
     */
    public static int getSchdDurHrs() {
        return PREFS.getInt(KEY_SCHD_DUR_HRS, 0);
    }
    
    /**
     * Gets the scheduled duration minutes default setting.
     * @return The value.
     */
    public static int getSchdDurMns() {
        return PREFS.getInt(KEY_SCHD_DUR_MNS, 0);
    }
    
    /**
     * Sets the scheduled time hour default setting.
     * @param value The value.
     */
    public static void setSchdTimeHr(int value) {
        if (value < 0 || value > 23) {
            return;
        }        
        PREFS.putInt(KEY_SCHD_TIME_HR, value);
        flush();
    }

    /**
     * Sets the scheduled time minute default setting.
     * @param value The value.
     */
    public static void setSchdTimeMn(int value) {
        if (value < 0 || value > 59) {
            return;
        }        
        PREFS.putInt(KEY_SCHD_TIME_MN, value);
        flush();
    }
    
    /**
     * Sets the scheduled duration hours default setting.
     * @param value The value.
     */
    public static void setSchdDurHrs(int value) {
        if (value < 0) {
            return;
        }        
        PREFS.putInt(KEY_SCHD_DUR_HRS, value);
        flush();
    }
    
    /**
     * Sets the scheduled duration minutes default setting.
     * @param value The value.
     */
    public static void setSchdDurMns(int value) {
        if (value < 0 || value > 59) {
            return;
        }        
        PREFS.putInt(KEY_SCHD_DUR_MNS, value);
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Action preferences error. {0}", ex.getMessage());
        }
    }
}

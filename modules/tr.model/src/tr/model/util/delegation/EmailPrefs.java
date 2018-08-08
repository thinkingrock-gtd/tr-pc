package tr.model.util.delegation;

import au.com.trgtd.tr.appl.Constants;
import java.util.prefs.Preferences;

/**
 * This is a hack to avoid a cyclic dependency on the email module. Just
 * provides the email address from email preferences.
 *
 * @author Jeremy Moore
 */
final class EmailPrefs {

    private static final Preferences PREFS = Constants.getPrefs("email");      

    /**
     * Gets the value for the email account address preference.
     *
     * @return The email address or null.
     */
    static final String getEmailAddress() {
        return PREFS.get("account.address", null);
    }

    /**
     * Get the SMTP preferences if they are set.
     *
     * @return the SMTP preferences or null.
     */
    static SMTP getSMTP() {
        return new SMTP(PREFS.get("smtp.host", null),
                        PREFS.getInt("smtp.port", 0),
                        PREFS.get("smtp.user", null),
                        PREFS.get("smtp.pass", null),
                        PREFS.getBoolean("smtp.ssl", false));
    }
}

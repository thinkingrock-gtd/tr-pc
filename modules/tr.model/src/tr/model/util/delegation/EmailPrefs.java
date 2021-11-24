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

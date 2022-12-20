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
package au.com.trgtd.tr.email.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Email options preferences.
 *
 * @author Jeremy Moore
 */
public class EmailPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.email");

    // EMAIL ACCOUNT (RECEIVING)
    private static final Preferences PREFS = Constants.getPrefs("email");

    private static final String KEY_EMAIL_DESC = "account.descr";
    private static final String KEY_EMAIL_ADDR = "account.address";
    private static final String KEY_EMAIL_USER = "account.username";
    private static final String KEY_EMAIL_PASS = "account.password";
    private static final String KEY_EMAIL_SRVR = "email.server";
    private static final String KEY_EMAIL_PORT = "email.port";
    private static final String KEY_EMAIL_SSL = "email.ssl";
    private static final String KEY_EMAIL_SSL_PROTOCOLS = "email.ssl.protocols";
    private static final String KEY_FETCH_AT_STARTUP = "fetch.at.startup";
    private static final String KEY_FETCH_SCHEDULE = "fetch.schedule";
    private static final String KEY_FETCH_MS_INTERVAL = "fetch.ms.interval";
    private static final String KEY_FETCH_KEEP = "fetch.keep";
    private static final String KEY_LAST_MSG_UID = "fetch.last.uid";
    private static final int DEF_EMAIL_PORT = 110;
    private static final boolean DEF_EMAIL_SSL = false;
    private static final String DEF_EMAIL_SSL_PROTOCOLS = "TLSv1.3 TLSv1.2";
    private static final boolean DEF_FETCH_AT_STARTUP = false;
    private static final boolean DEF_FETCH_SCHEDULE = false;
    private static final long DEF_FETCH_MS_INTERVAL = 0;
    private static final boolean DEF_FETCH_KEEP = false;
    private static final String DEF_LAST_MSG_UID = null;

    private static final String KEY_SAVE_ATTACHMENTS = "save.attachments";
    private static final boolean DEF_SAVE_ATTACHMENTS = false;
    private static final String KEY_PATH_ATTACHMENTS = "path.attachments";
    private static final String DEF_PATH_ATTACHMENTS = System.getProperty("user.home");

    // SMTP (SENDING)
    private static final String KEY_SMTP_HOST = "smtp.host";
    private static final String KEY_SMTP_PORT = "smtp.port";
    private static final String KEY_SMTP_USER = "smtp.user";
    private static final String KEY_SMTP_PASS = "smtp.pass";
    private static final String KEY_SMTP_SSL = "smtp.ssl";
    private static final int DEF_SMTP_PORT = 465;
    private static final boolean DEF_SMTP_SSL = true;

    public enum DelegateEmailMethod {
        EMAIL_CLIENT, DIRECT_ALL, DIRECT_TEAM
    }
    private static final String KEY_DELEGATE_EMAIL_METHOD = "delegate.email.method";
    private static final int DEF_DELEGATE_EMAIL_METHOD = DelegateEmailMethod.EMAIL_CLIENT.ordinal();

    /**
     * Gets the value for the email account description preference.
     *
     * @return The value.
     */
    public static final String getEmailDescription() {
        return PREFS.get(KEY_EMAIL_DESC, "");
    }

    /**
     * Sets the value for the email account description preference.
     *
     * @param value The value.
     */
    public static final void setEmailDescription(String value) {
        PREFS.put(KEY_EMAIL_DESC, value);
        flush();
    }

    /**
     * Gets the value for the email account address preference.
     *
     * @return The value.
     */
    public static final String getEmailAddress() {
        return PREFS.get(KEY_EMAIL_ADDR, "");
    }

    /**
     * Sets the value for the email account address preference.
     *
     * @param value The value.
     */
    public static final void setEmailAddress(String value) {
        PREFS.put(KEY_EMAIL_ADDR, value);
        flush();
    }

    /**
     * Gets the value for the email account username preference.
     *
     * @return The value.
     */
    public static final String getEmailUsername() {
        return PREFS.get(KEY_EMAIL_USER, "");
    }

    /**
     * Sets the value for the email account username preference.
     *
     * @param value The value.
     */
    public static final void setEmailUsername(String value) {
        PREFS.put(KEY_EMAIL_USER, value);
        flush();
    }

    /**
     * Gets the value for the email account password preference.
     *
     * @return The value.
     */
    public static final String getEmailPassword() {
        return PREFS.get(KEY_EMAIL_PASS, "");
    }

    /**
     * Sets the value for the email account description preference.
     *
     * @param value The value.
     */
    public static final void setEmailPassword(String value) {
        PREFS.put(KEY_EMAIL_PASS, value);
        flush();
    }

    /**
     * Gets the value for the email server preference.
     *
     * @return The value.
     */
    public static final String getEmailServer() {
        return PREFS.get(KEY_EMAIL_SRVR, "");
    }

    /**
     * Sets the value for the email server preference.
     *
     * @param value The value.
     */
    public static final void setEmailServer(String value) {
        PREFS.put(KEY_EMAIL_SRVR, value);
        flush();
    }

    /**
     * Gets the value for the email account port preference.
     *
     * @return The value.
     */
    public static final int getPort() {
        return PREFS.getInt(KEY_EMAIL_PORT, DEF_EMAIL_PORT);
    }

    /**
     * Sets the value for the email account port preference.
     *
     * @param value The value.
     */
    public static final void setPort(int value) {
        PREFS.putInt(KEY_EMAIL_PORT, value);
        flush();
    }

    /**
     * Gets the value for the email account SSL preference.
     *
     * @return The value.
     */
    public static final boolean isSSL() {
        return PREFS.getBoolean(KEY_EMAIL_SSL, DEF_EMAIL_SSL);
    }

    /**
     * Sets the value for the email account SSL preference.
     *
     * @param value The value.
     */
    public static final void setSSL(boolean value) {
        PREFS.putBoolean(KEY_EMAIL_SSL, value);
        flush();
    }

    /**
     * Gets the value for the ssl protocols
     *
     * @return The value.
     */
    public static final String getSslProtocols() {
        return PREFS.get(KEY_EMAIL_SSL_PROTOCOLS, DEF_EMAIL_SSL_PROTOCOLS);
    }

    /**
     * Sets the value for the email ssl protocols
     *
     * @param protocols The protocols, space delimited
     */
    public static final void setSslProtocols(String protocols) {
        PREFS.put(KEY_EMAIL_SSL_PROTOCOLS, protocols);
        flush();
    }

    /**
     * Gets the value for the fetch email at startup preference.
     *
     * @return The value.
     */
    public static final boolean isEmailFetchAtStartup() {
        return PREFS.getBoolean(KEY_FETCH_AT_STARTUP, DEF_FETCH_AT_STARTUP);
    }

    /**
     * Sets the value for the fetch email at startup preference.
     *
     * @param value The value.
     */
    public static final void setEmailFetchAtStartup(boolean value) {
        PREFS.putBoolean(KEY_FETCH_AT_STARTUP, value);
        flush();
    }

    /**
     * Gets the value for the schedule fetch email preference.
     *
     * @return The value.
     */
    public static final boolean isEmailFetchSchedule() {
        return PREFS.getBoolean(KEY_FETCH_SCHEDULE, DEF_FETCH_SCHEDULE);
    }

    /**
     * Sets the value for the schedule fetch email preference.
     *
     * @param value The value.
     */
    public static final void setEmailFetchSchedule(boolean value) {
        PREFS.putBoolean(KEY_FETCH_SCHEDULE, value);
        flush();
    }

    /**
     * Gets the value for the scheduled fetch email millisecond interval
     * preference.
     *
     * @return The value.
     */
    public static final long getEmailFetchIntervalMS() {
        return PREFS.getLong(KEY_FETCH_MS_INTERVAL, DEF_FETCH_MS_INTERVAL);
    }

    /**
     * Sets the value for the scheduled fetch email millisecond interval
     * preference.
     *
     * @param value The value.
     */
    public static final void setEmailFetchIntervalMS(long value) {
        PREFS.putLong(KEY_FETCH_MS_INTERVAL, value);
        flush();
    }

    /**
     * Gets the value for the keep email on server preference.
     *
     * @return The value.
     */
    public static final boolean isKeepOnServer() {
        return PREFS.getBoolean(KEY_FETCH_KEEP, DEF_FETCH_KEEP);
    }

    /**
     * Sets the value for the keep email on server preference.
     *
     * @param value The value.
     */
    public static final void setFetchKeep(boolean value) {
        PREFS.putBoolean(KEY_FETCH_KEEP, value);
        flush();
    }

    /**
     * Gets the value for the last message UID preference.
     *
     * @return The value.
     */
    public static final String getLastMsgUID() {
        return PREFS.get(KEY_LAST_MSG_UID, DEF_LAST_MSG_UID);
    }

    /**
     * Sets the value for the last message UID preference.
     *
     * @param value The value.
     */
    public static final void setLastMsgUID(String value) {
        PREFS.put(KEY_LAST_MSG_UID, value);
        flush();
    }

    /**
     * Gets the value for the save attachments preference.
     *
     * @return The value.
     */
    public static final boolean isSaveAttachments() {
        return PREFS.getBoolean(KEY_SAVE_ATTACHMENTS, DEF_SAVE_ATTACHMENTS);
    }

    /**
     * Sets the value for the save attachments preference.
     *
     * @param value The value.
     */
    public static final void setSaveAttachments(boolean value) {
        PREFS.putBoolean(KEY_SAVE_ATTACHMENTS, value);
        flush();
    }

    /**
     * Gets the value for the attachments path preference.
     *
     * @return The value.
     */
    public static final String getPathAttachments() {
        return PREFS.get(KEY_PATH_ATTACHMENTS, DEF_PATH_ATTACHMENTS);
    }

    /**
     * Sets the value for the attachments path preference.
     *
     * @param value The value.
     */
    public static final void setPathAttachments(String value) {
        PREFS.put(KEY_PATH_ATTACHMENTS, value);
        flush();
    }

    // SMTP Start
    
    /**
     * Gets the value for the SMTP host.
     *
     * @return The value.
     */
    public static final String getSMTPHost() {
        return PREFS.get(KEY_SMTP_HOST, "");
    }

    /**
     * Sets the SMTP host value.
     *
     * @param value The value.
     */
    public static final void setSMTPHost(String value) {
        PREFS.put(KEY_SMTP_HOST, value);
        flush();
    }

    /**
     * Gets the SMTP port value.
     *
     * @return The value.
     */
    public static final int getSMTPPort() {
        return PREFS.getInt(KEY_SMTP_PORT, DEF_SMTP_PORT);
    }

    /**
     * Sets the SMTP port value.
     *
     * @param value The value.
     */
    public static final void setSMTPPort(int value) {
        PREFS.putInt(KEY_SMTP_PORT, value);
        flush();
    }

    /**
     * Gets the SMTP username value.
     *
     * @return The value.
     */
    public static final String getSMTPUser() {
        return PREFS.get(KEY_SMTP_USER, "");
    }

    /**
     * Sets the SMTP username value.
     *
     * @param value The value.
     */
    public static final void setSMTPUser(String value) {
        PREFS.put(KEY_SMTP_USER, value);
        flush();
    }

    /**
     * Gets the SMTP password value.
     *
     * @return The value.
     */
    public static final String getSMTPPass() {
        return PREFS.get(KEY_SMTP_PASS, "");
    }

    /**
     * Sets the SMTP password value.
     *
     * @param value The value.
     */
    public static final void setSMTPPass(String value) {
        PREFS.put(KEY_SMTP_PASS, value);
        flush();
    }

    /**
     * Gets the SMTP SSL value.
     *
     * @return The value.
     */
    public static final boolean isSMTPSSL() {
        return PREFS.getBoolean(KEY_SMTP_SSL, DEF_SMTP_SSL);
    }

    /**
     * Sets the SMTP SSL value.
     *
     * @param value The value.
     */
    public static final void setSMTPSSL(boolean value) {
        PREFS.putBoolean(KEY_SMTP_SSL, value);
        flush();
    }
    // SMTP End

    /**
     * Gets the value for the delegate email process preference.
     *
     * @return The value.
     */
    public static final DelegateEmailMethod getDelegateEmailMethod() {
        final int option = PREFS.getInt(KEY_DELEGATE_EMAIL_METHOD, DEF_DELEGATE_EMAIL_METHOD);
        return DelegateEmailMethod.values()[option];
    }

    /**
     * Sets the value for the delegate email process preference.
     *
     * @param method The method.
     */
    public static final void setDelegateEmailProcess(DelegateEmailMethod method) {
        if (method == null) {
            PREFS.remove(KEY_DELEGATE_EMAIL_METHOD);
        } else {
            PREFS.putInt(KEY_DELEGATE_EMAIL_METHOD, method.ordinal());
        }
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Email preferences error. {0}", ex.getMessage());
        }
    }

}

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
package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.prefs.ApplicationPrefs;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.task.messages.MessageParser.Message;
import au.com.trgtd.tr.util.DateUtils;

/**
 * Version check.
 *
 * @author Jeremy Moore
 */
final class VersionChecker {

    private static final Logger LOG = Logger.getLogger("tr.version");
    private static final int TIMEOUT = 10000;

    /**
     * Determines whether it is time to check for a new version.
     * @return true if it is time to check, otherwise false.
     */
    public boolean shouldCheckVersion() {
        switch (ApplicationPrefs.getVersionCheckPeriod()) {
            case ApplicationPrefs.VERSION_CHECK_PERIOD_NEVER: {
                LOG.fine("User preference Check Period is set to NEVER");
                return false;
            }
            case ApplicationPrefs.VERSION_CHECK_PERIOD_STARTUP: {
                LOG.fine("User preference Check Period is set to STARTUP");
                return true;
            }
            default: {
                if (checkPeriodExpired()) {
                    LOG.fine("Period has expired");
                    return true;
                } else {
                    LOG.fine("Period has not expired");
                    return false;
                }
            }
        }
    }

    private boolean checkPeriodExpired() {
        LOG.fine("Checking if period has expired ... ");

        Calendar checkDate = Calendar.getInstance();
        checkDate.setTimeInMillis(ApplicationPrefs.getCheckVersionLastTime());

        switch (ApplicationPrefs.getVersionCheckPeriod()) {
            case ApplicationPrefs.VERSION_CHECK_PERIOD_DAY:
                LOG.fine("User preference Check Period is set to DAY");
                checkDate.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case ApplicationPrefs.VERSION_CHECK_PERIOD_WEEK:
                LOG.fine("User preference Check Period is set to WEEK");
                checkDate.add(Calendar.DAY_OF_YEAR, 7);
                break;
            case ApplicationPrefs.VERSION_CHECK_PERIOD_FORTNIGHT:
                LOG.fine("User preference Check Period is set to TWO WEEKS");
                checkDate.add(Calendar.DAY_OF_YEAR, 14);
                break;
            case ApplicationPrefs.VERSION_CHECK_PERIOD_MONTH:
                LOG.fine("User preference Check Period is set to MONTH");
                checkDate.add(Calendar.MONTH, 1);
                break;
            default:
                LOG.severe("User preference Check Period is not recognized.");
                return false;
        }

        Calendar now = Calendar.getInstance();

        return now.after(checkDate);
    }

    /**
     * Gets the version string if there is a new version available.
     * @return version string.
     */
    public Message getNewVersionMessage() throws Exception {
        LOG.fine("Getting latest version number from web site");

        String latestVersion = null;
        try {
            latestVersion = getVersionFromWebSite();
        } catch (Exception ex) {
            LOG.severe("Latest version was not obtained from web site");
            throw ex;
        }
        try {
            if (isNewer(latestVersion, Constants.VERSION)) {
                Date date = new Date();
                String type = "A";
                String href = "https://www.trgtd.com.au/index.php/download-versions";
                String text = NbBundle.getMessage(getClass(), "messages.version", latestVersion);
                return new Message(date, type, href, text);
            } else {
                return null;
            }
        } catch (NumberFormatException ex) {
            LOG.severe("Version is not of correct format.");
            throw ex;
        }
    }

    public void setLastCheckDate() {
        LOG.fine("Setting last time checked");
        Date startOfToday = DateUtils.getStart(Calendar.getInstance().getTime());
        ApplicationPrefs.setCheckVersionLastTime(startOfToday.getTime());
    }

    private String getVersionFromWebSite() throws Exception {
        URL url = new URL(Constants.VERSION_FILE_URL);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(TIMEOUT);
        InputStream in = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        return br.readLine();
    }

    private int[] getVersionArr(String version) throws Exception {
        StringTokenizer st = new StringTokenizer(version, ".");
        if (st.countTokens() < 3) {
            throw new NumberFormatException();
        }
        return new int[]{
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken())
                };
    }

    private boolean isNewer(String latestVersion, String currentVersion) throws Exception {
        int[] latestVersionArr = getVersionArr(latestVersion);
        int[] currentVersionArr = getVersionArr(currentVersion);
        for (int i = 0; i < 3; i++) {
            if (latestVersionArr[i] > currentVersionArr[i]) {
                return true;
            }
            if (latestVersionArr[i] < currentVersionArr[i]) {
                return false;
            }
        }
        return false;
    }
}

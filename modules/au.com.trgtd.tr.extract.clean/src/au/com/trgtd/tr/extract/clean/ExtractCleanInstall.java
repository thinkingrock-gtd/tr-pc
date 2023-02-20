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
package au.com.trgtd.tr.extract.clean;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import java.io.File;
import java.io.FileFilter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.openide.modules.ModuleInstall;
import au.com.trgtd.tr.extract.prefs.ExtractPrefs;
import au.com.trgtd.tr.util.UtilsFile;
import java.util.logging.Level;

/**
 * Extract cleanup install.
 *
 * @author Jeremy Moore
 */
public class ExtractCleanInstall extends ModuleInstall {

    private final static Logger log = Logger.getLogger("tr.extract.clean");

    /**
     * Creates a new instance of ExtractCleanInstall.
     */
    public ExtractCleanInstall() {
    }

    /**
     * On every startup check if the extract directory should be cleaned and
     * if so, clean it.
     */
    @Override
    public void restored() {
        SwingUtilities.invokeLater(() -> {
            log.finest("Begin");

            checkCleanup();

            log.finest("End");
        });
    }

    private void checkCleanup() {

        int interval = ExtractCleanPrefs.getCleanIntervalDays();

        if (interval == ExtractCleanPrefs.CLEAN_INTERVAL_NEVER) {
            log.fine("Interval set to NEVER.");
            return;
        }

        if (interval != ExtractCleanPrefs.CLEAN_INTERVAL_STARTUP) {
            // see if the interval has passed since last check
            Calendar nextCheckDate = Calendar.getInstance();
            nextCheckDate.setTimeInMillis(ExtractCleanPrefs.getCleanLastDateMS());
            nextCheckDate.add(Calendar.DAY_OF_YEAR, interval);
            Calendar now = Calendar.getInstance();
            if (now.before(nextCheckDate)) {
                log.fine("Interval has not passed.");
                return;
            }
        }

        // do the clean
        cleanExtractFolder();

        // set the last clean date
        ExtractCleanPrefs.setCleanLastDateMS(Calendar.getInstance().getTimeInMillis());
    }

    private File getExtractFolder() {
        String path = ExtractPrefs.getPath();
        if (path.trim().length() > 0) {
            // user preference extract path
            return new File(path);
        }
        // no user preference path so use path of data file
        DataStore ds = (DataStore) DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            File dataFile = new File(ds.getPath());
            if (dataFile.isFile()) {
                return dataFile.getParentFile();
            }
        }
        // data file error so give up
        return null;
    }

    private void cleanExtractFolder() {
        File folder = getExtractFolder();
        if (folder == null || !folder.isDirectory()) {
            log.severe("Extract path is not a directory.");
            return;
        }
        if (!folder.canRead() || !folder.canWrite()) {
            log.severe("Insifficient permissions on extract directory.");
            return;
        }

        final Calendar ageDate = Calendar.getInstance();
        ageDate.add(Calendar.DAY_OF_YEAR, ExtractCleanPrefs.getCleanAgeDays() * -1);

        log.log(Level.FINE, "Deleting files older than: {0}", ageDate.getTime());

        FileFilter filter = (File file) -> {
            if (file.isDirectory()) {
                return false;
            }

            String extn = UtilsFile.getExtension(file.getPath());
            if (extn == null) {
                return false;
            }

            if (!extn.equalsIgnoreCase("txt") && !extn.equalsIgnoreCase("pdf") && !extn.equalsIgnoreCase("xml")) {
                return false;
            }

            String name = UtilsFile.removeExtension(file.getName());
            if (name.length() < 15) { // timestamp is 14 digits
                return false;
            }

            try {
                String timestamp = name.substring(name.length() - 14);
                Long.parseLong(timestamp); // check that it is a number

                Date date = Constants.DF_TIMESTAMP.parse(timestamp);
                if (date.before(ageDate.getTime())) {
                    return true;
                }
            } catch (Exception ex) {
            }
            return false;
        };

        for (File file : folder.listFiles(filter)) {

            log.log(Level.FINE, "Deleting: {0}", file.getPath());

            if (!file.delete()) {
                log.log(Level.WARNING, "Could not delete: {0}", file.getPath());
            }
        }
    }
}

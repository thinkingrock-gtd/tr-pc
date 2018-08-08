/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                log.finest("Begin");

                checkCleanup();

                log.finest("End");
            }
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

        log.fine("Deleting files older than: " + ageDate.getTime());

        FileFilter filter = new FileFilter() {

            public boolean accept(File file) {
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
            }
        };

        for (File file : folder.listFiles(filter)) {

            log.fine("Deleting: " + file.getPath());

            if (!file.delete()) {
                log.warning("Could not delete: " + file.getPath());
            }
        }
    }
}

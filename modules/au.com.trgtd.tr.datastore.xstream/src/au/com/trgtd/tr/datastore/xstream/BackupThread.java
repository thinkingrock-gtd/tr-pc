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
package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.prefs.data.DataPrefs;
import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import au.com.trgtd.tr.util.UtilsFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Backup data thread.
 *
 * @author Jeremy Moore
 */
public class BackupThread extends Thread {

    private static final DateFormat DF_DATESTAMP = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat DF_TIMESTAMP = new SimpleDateFormat("HHmmssSSS");
    private static final Logger log = Logger.getLogger("tr.data.backup");

    @Override
    public void run() {

        try {            
            sleep(40000); // delay to reduce startup load
        } catch (InterruptedException x) {
        }

        log.info("Starting: data backup process ... ");

        if (shouldBackup()) {
            log.info("starting data backup ... ");
            if (backup()) {
//              DataPrefs.setBackupLastDateMS(System.currentTimeMillis());
                log.info("Data backup completed.");
            } else {
                log.info("Data backup failed.");
            }
        }
        
        log.info("Finished: data backup process.");
    }

    private boolean shouldBackup() {
        int interval = DataPrefs.getBackupIntervalDays();
        if (interval == DataPrefs.BACKUP_INTERVAL_STARTUP) {
            log.fine("Backup interval set to STARTUP.");
            return true;
        }
        if (interval == DataPrefs.BACKUP_INTERVAL_NEVER) {
            log.fine("Backup interval set to NEVER.");
            return false;
        }

        Calendar nextBackupDate = Calendar.getInstance();
        nextBackupDate.setTime(getLastBackupFileDate());
        nextBackupDate.add(Calendar.DAY_OF_YEAR, interval);
        Calendar now = Calendar.getInstance();
        if (now.before(nextBackupDate)) {
            log.fine("Backup interval has not passed.");
            return false;
        } else {
            log.fine("Backup interval has passed.");
            return true;
        }
    }

    private static Pattern timeStampPattern;
    private static final String getDateStamp(String filename) {
        if (timeStampPattern == null) {
            timeStampPattern = Pattern.compile("\\d{8}-\\d{9}");
        }
        Matcher matcher = timeStampPattern.matcher(filename);
        if (matcher.find()) {
            return filename.substring(matcher.start(), matcher.start() + 8);
        }
        return null;
    }

    private Date getLastBackupFileDate() {
        File file = getLatestBackupFile();
        if (file != null) {
            String datestamp = getDateStamp(file.getName());
            if (datestamp != null) {
                try {
                    return DF_DATESTAMP.parse(datestamp);
                } catch (ParseException ex) {
                }
            }
        }
        return new Date(0);
    }

    /* Gets the latest recovery file. */
    private File getLatestBackupFile() {
        File dir = getBackupDir();
        if (dir == null) {
            return null;
        }
        File datafile = getDataFile();
        if (datafile == null || !datafile.isFile()) {
            return null;
        }

        String prefix = datafile.getName().substring(0, datafile.getName().indexOf("."));

        final String regex = prefix.toLowerCase() + "\\.\\d{8}-\\d{9}\\.bak\\.(trx|xml)";

        FileFilter filter = (File file) -> file.isFile()
                && file.getName().toLowerCase().matches(regex);

        List<File> recoveryFiles = new ArrayList<>();
        Collections.addAll(recoveryFiles, dir.listFiles(filter));
        
        if (!recoveryFiles.isEmpty()) {
            Collections.sort(recoveryFiles, new DescendingFilenameComparator());
            return recoveryFiles.get(0);
        } else {
            return null;
        }
    }

    // Comparator to sort backup files into descending filename order.
    // Since the start of the file names should be the same up to the timestamp,
    // this should cause them to be ordered in descending timestamp order
    // (ie. the latest file will be first).
    private static class DescendingFilenameComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return f2.getName().compareTo(f1.getName());
        }
    }

    private File getBackupDir() {
        // first see if there is a valid path in data preferences
        String path = DataPrefs.getBackupPath();
        if (path != null && path.trim().length() > 0) {
            File file = new File(path);
            if (file.isDirectory()) {
                return file;
            }
        }
        // otherwise use data file dir
        File datafile = getDataFile();
        if (datafile != null) {
            return datafile.getParentFile();
        }

        return null;
    }

    private File getDataFile() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            return null;
        }
        File file = new File(ds.getPath());
        if (file.isFile()) {
            return file;
        }
        return null;
    }

    private boolean backup() {
        File dir = getBackupDir();
        if (dir == null) {
            log.severe("Could not ascertain backup directory.");
            return false;
        }
        if (!dir.canRead() || !dir.canWrite()) {
            log.log(Level.SEVERE, "Insifficient permissions on backup dir: {0}", dir.getPath());
            return false;
        }
        File datafile = getDataFile();
        if (datafile == null) {
            log.severe("Data file not found.");
            return false;
        }
        File backupfile = getNextBackupFile(dir);
        if (backupfile == null) {
            log.severe("Backup file could not be determined.");
            return false;
        }
        try {
            UtilsFile.copyFile(datafile, backupfile);
            log.log(Level.INFO, "Created backup file: {0}", backupfile.getPath());
            return true;
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Could not create backup file. \n{0}", ex.getMessage());
            return false;
        }    
    }

    private String getNextBackupFilename() {
        File datafile = getDataFile();
        if (datafile == null) {
            return null;
        }
        String name = UtilsFile.removeExtension(datafile.getName());
        String extn = UtilsFile.getExtension(datafile.getName());
        Date date = new Date();
        String ds = DF_DATESTAMP.format(date);
        String ts = DF_TIMESTAMP.format(date);
        return name + "." + ds + "-" + ts + ".bak." + extn;
    }

    private File getNextBackupFile(File dir) {
        if (dir == null) {
            return null;
        }
        String filename = getNextBackupFilename();
        if (filename == null) {
            return null;
        }
        return new File(dir, filename);
    }

}

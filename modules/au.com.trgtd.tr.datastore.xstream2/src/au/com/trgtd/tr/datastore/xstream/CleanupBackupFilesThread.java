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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.prefs.data.DataPrefs;
import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Task to remove old data backup files.
 *
 * @author Jeremy Moore
 */
public class CleanupBackupFilesThread extends Thread {

    private final static Logger log = Logger.getLogger("tr.data.cleanup.backups");

    @Override
    public void run() {

        try {
            // delay to reduce startup load
            sleep(30000);
        } catch (InterruptedException ex) {
        }

        log.info("Starting: Backup file cleanup ... ");

        if (DataPrefs.isKeepAllBackupFiles()) {
            log.info("Backup file cleanup keeping all backup files.");
        } else {
            File backupDir = getBackupDir();
            if (backupDir == null) {
                log.severe("Backup file cleanup could not determine backup directory.");
                return;
            }
            int nbrFilesToKeep = DataPrefs.getKeepNbrBackupFiles();
            if (nbrFilesToKeep < 0) {
                log.severe("Backup file cleanup could not determine number of files to keep.");
                return;
            }
            clean(backupDir, nbrFilesToKeep);
        }

        log.info("Finished: Backup file cleanup.");
    }

    private File getBackupDir() {
        // see if there is a valid backup directory preference
        String backupDirPath = DataPrefs.getBackupPath();
        if (backupDirPath != null && backupDirPath.trim().length() > 0) {
            File backupDir = new File(backupDirPath.trim());
            if (backupDir.isDirectory()) {
                return backupDir;
            }
        }
        // otherwise assume using the data file directory
        String datafilePath = XStreamDataStore.getDataFilePath();
        if (datafilePath != null) {
            File datafile = new File(datafilePath.trim());
            if (datafile.isFile()) {
                return datafile.getParentFile();
            }
        }
        // problem with data file directory preference
        return null;
    }

    // Regular expression for backup files
    private static final String regex = ".*\\d{8}-\\d{9}\\.bak\\.(trx|TRX|xml|XML)";

    private void clean(File backupDir, int nbrFilesToKeep) {
        log.info("Backup directory: " + backupDir.getPath());
        log.info("Number of files to keep: " + nbrFilesToKeep);

        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && file.getName().matches(regex);
            }
        };

        for (File file : backupDir.listFiles(filter)) {
            String str = file.getName();
            String key = str.substring(0, str.indexOf("."));
            map(key, file);
        }

        Comparator<File> comparator = new FileDateComparator();

        for (List<File> files : map.values()) {
            Collections.sort(files, comparator);

            for (File file : files) {
                log.info("Found backup file: " + file.getPath());
            }

            while (files.size() > nbrFilesToKeep) {
                File file = files.remove(0);
                log.info("Deleting backup file: " + file.getPath());                
                file.delete();
            }
        }
    }

    // Comparator to sort backup files into ascending filename order.
    // Since the start of the file names should be the same up to the timestamp,
    // this should cause them to be ordered in ascending timestamp order
    // (ie. the earliest file will be first).
    private static class FileDateComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }
    }
    private static Map<String, List<File>> map = new HashMap<String, List<File>>();

    private static void map(String key, File file) {
        List<File> fileList = map.get(key);
        if (fileList == null) {
            fileList = new Vector<File>();
            map.put(key, fileList);
        }
        fileList.add(file);
    }
    
}

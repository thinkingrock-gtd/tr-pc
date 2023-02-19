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

import au.com.trgtd.tr.prefs.data.DataPrefs;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Task to remove old data recovery files.
 *
 * @author Jeremy Moore
 */
public class CleanupRecoveryFilesThread extends Thread {

    private final static Logger log = Logger.getLogger("tr.datastore.xstream.cleanup");

    @Override
    public void run() {

        log.info("Starting: Recovery file cleanup ... ");

        if (DataPrefs.isKeepAllRecoveryFiles()) {
            log.info("Recovery file cleanup keeping all recovery files.");
        } else {
            File recoveryDir = getRecoveryDir();
            if (recoveryDir == null) {
                log.severe("Recovery file cleanup could not determine recovery directory.");
                return;
            }
            int nbrFilesToKeep = DataPrefs.getKeepNbrRecoveryFiles();
            if (nbrFilesToKeep < 0) {
                log.severe("Recovery file cleanup could not determine number of files to keep.");
                return;
            }
            clean(recoveryDir, nbrFilesToKeep);
        }

        log.info("Finished: Recovery file cleanup.");
    }

    private File getRecoveryDir() {
        // see if there is a valid recovery directory preference
        String recoveryDirPath = DataPrefs.getRecoveryPath();
        if (recoveryDirPath != null && recoveryDirPath.trim().length() > 0) {
            File recoveryDir = new File(recoveryDirPath.trim());
            if (recoveryDir.isDirectory()) {
                return recoveryDir;
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

    // Regular expression for recovery files
    private static final String regex = ".*\\d{8}-\\d{9}\\.rec\\.(trx|TRX|xml|XML)";

    private void clean(File recoveryDir, int nbrFilesToKeep) {

        log.log(Level.INFO, "Recovery directory: {0}", recoveryDir.getPath());
        log.log(Level.INFO, "Number of files to keep: {0}", nbrFilesToKeep);

        FileFilter filter = (File file) -> file.isFile()
                && file.getName().matches(regex);

        Map<String, List<File>> map = new HashMap<>();

        for (File file : recoveryDir.listFiles(filter)) {
            String str = file.getName();
            String key = str.substring(0, str.indexOf("."));
            map(map, key, file);
        }

        Comparator<File> comparator = new FileDateComparator();

        for (List<File> files : map.values()) {
            
            Collections.sort(files, comparator);

            for (File file : files) {
                log.log(Level.INFO, "Found recovery file: {0}", file.getPath());
            }

            while (files.size() > nbrFilesToKeep) {
                File file = files.remove(0);

                log.log(Level.INFO, "Deleting recovery file: {0}", file.getPath());
                
                file.delete();
            }
        }
    }

    // Comparator to sort files into ascending string order.
    // Since the start of the file names should be the same up to the timestamp,
    // this should cause them to be ordered in ascending timestamp order
    // (ie. the earliest file will be first).
    private class FileDateComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return f1.getName().compareTo(f2.getName());
        }
    }

    private void map(Map<String, List<File>> map, String key, File file) {
        List<File> fileList = map.get(key);
        if (fileList == null) {
            fileList = new ArrayList<>();
            map.put(key, fileList);
        }
        fileList.add(file);
    }
    
}

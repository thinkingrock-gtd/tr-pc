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

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.calendar.prefs.CalendarPrefs;
import au.com.trgtd.tr.datastore.AbstractDataStore;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.prefs.data.DataPrefs;
import au.com.trgtd.tr.runtime.Open;
import au.com.trgtd.tr.util.UtilsFile;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.project.Project;

/**
 * Data manager singleton that uses XStream to persist the data model.
 *
 * @author Jeremy Moore
 */
public final class XStreamDataStore extends AbstractDataStore implements PreferenceChangeListener {

    private static final Logger LOG = Logger.getLogger("tr.datastore.xstream");
    public static final String[] FILE_EXTENSIONS = {"xml", "trx"};
    private static final DateFormat DF_DATESTAMP = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat DF_TIMESTAMP = new SimpleDateFormat("HHmmssSSS");
    private static DataStore instance;
    private Thread autoSaveThread;
    private boolean autoSave;
    private File recoveryDir;

    /* Singleton constructor. */
    private XStreamDataStore() {
        super();
        DataPrefs.getPrefs().addPreferenceChangeListener(this);
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance.
     */
    public static DataStore instance() {
        if (instance == null) {
            instance = new XStreamDataStore();
        }
        return instance;
    }

    /**
     * Gets the data file path preference.
     *
     * @return The data file path.
     */
    public static String getDataFilePath() {
        return XStreamPrefs.getPath();
    }

    /**
     * Gets the data file path preference.
     *
     * @return The data file path.
     */
    @Override
    public String getPath() {
        return XStreamPrefs.getPath();
    }

    /**
     * Sets the data file path preference.
     *
     * @param path The data file path.
     */
    @Override
    public void setPath(String path) {
        XStreamPrefs.setPath(path);
    }

    /* Gets the data file. */
    private File getDataFile() {
        return new File(getPath());
    }

    /* Gets the recovery file directory. */
    private File getRecoveryDir() {
        if (recoveryDir != null) {
            return recoveryDir;
        }
        // see if there is a valid recovery directory preference
        String path = DataPrefs.getRecoveryPath();
        if (path != null && path.trim().length() > 0) {
            File dir = new File(path.trim());
            if (dir.isDirectory()) {
                recoveryDir = dir;
                return recoveryDir;
            }
        }
        // otherwise assume using the data file directory
        path = getDataFilePath();
        if (path != null) {
            File datafile = new File(path.trim());
            if (datafile.isFile()) {
                recoveryDir = datafile.getParentFile();
                return recoveryDir;
            }
        }
        // problem with data file directory preference
        return null;
    }

    private String getNextRecoveryFilename() {
        File datafile = getDataFile();
        if (datafile == null) {
            return null;
        }
        String name = UtilsFile.removeExtension(datafile.getName());
        String extn = UtilsFile.getExtension(datafile.getName());
        Date date = new Date();
        String ds = DF_DATESTAMP.format(date);
        String ts = DF_TIMESTAMP.format(date);
        return name + "." + ds + "-" + ts + ".rec." + extn;
    }

    private File getNextRecoveryFile() {
        File dir = getRecoveryDir();
        if (dir == null) {
            return null;
        }
        String filename = getNextRecoveryFilename();
        if (filename == null) {
            return null;
        }
        return new File(dir, filename);
    }

    /* Gets the latest recovery file. */
    private File getLatestRecoveryFile() {
        File dir = getRecoveryDir();
        if (dir == null) {
            return null;
        }
        File datafile = getDataFile();
        if (datafile == null || !datafile.isFile()) {
            return null;
        }

        String prefix = datafile.getName().substring(0, datafile.getName().indexOf("."));

        final String regex = prefix.toLowerCase() + "\\.\\d{8}-\\d{9}\\.rec\\.(trx|xml)";

        FileFilter filter = (File file) -> file.isFile()
                && file.getName().toLowerCase().matches(regex);

        List<File> recoveryFiles = new ArrayList<>();
        recoveryFiles.addAll(Arrays.asList(dir.listFiles(filter)));

        if (!recoveryFiles.isEmpty()) {
            Collections.sort(recoveryFiles, new FileDateComparator());
            return recoveryFiles.get(0);
        } else {
            return null;
        }
    }

    // Comparator to sort recovery files into descending string order.
    // Since the start of the file names should be the same up to the timestamp,
    // this should cause them to be ordered in descending timestamp order
    // (ie. the latest file will be first).
    private static class FileDateComparator implements Comparator<File> {

        @Override
        public int compare(File f1, File f2) {
            return f2.getName().compareTo(f1.getName());
        }
    }

    /* Gets the automatic save thread. */
    private Thread getAutoSaveThread() {
        if (autoSaveThread == null) {
            autoSaveThread = new AutoSaveThread();
            autoSaveThread.setDaemon(true);
        }
        return autoSaveThread;
    }

    /* Starts the automatic saving thread.	*/
    private void startAutoSaving() {
        Thread thread = getAutoSaveThread();

        if (thread.isAlive()) {
            return;
        }

        autoSave = true;

        thread.start();

        LOG.fine("Automatic saving started");
    }

    /* Stops the automatic saving thread. */
    private void stopAutoSaving() {
        autoSave = false;

        if (autoSaveThread != null) {
            while (autoSaveThread.isAlive()) {
                autoSaveThread.interrupt();
            }
            autoSaveThread = null;
        }

        LOG.fine("Automatic saving stopped");
    }

    // Removes the data file if possible.
    private void removeDataFile() throws Exception {
        File file = getDataFile();
        if (file.exists()) {
            file.delete();
        }
    }

    private static final String m_b = ">YDOB<>LMTH<";
//    private static final String m_1 = ">P/<.ytfif ot snoitcA dna stcejorP fo rebmun eht stcirtser noisrev lairt kcoRgniknihT>P<";
//    private static final String m2a = ">P/<.ti daol ton lliw kcoRgniknihT ,timil siht sehcaer elif atad ruoy nehW>P<";
//    private static final String m2b = ">P/<.elif atad wen a etaerc ,noitaulave ruoy eunitnoc oT>/RB<>B/<.ti daol ton lliw kcoRgniknihT dna timil siht dehcaer sah elif atad ruoY>B<>P<";
//    private static final String m_3 = ">P/<>A/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh A< tisiv kcoRgniknihT esahcrup oT>P<";
    private static final String m_e = ">YDOB/<>LMTH/<";

    private static final String de_m11 = ">P/<.treitimil 05 fua nenoitkA dnu etkejorP red lhaznA eid tsi noisreV lairT kcoRgniknihT red nI>P<";
    private static final String de_m12 = ">P/<.tenfföeg rhem thcin eiS driw ,thcierre timiL seseid ietadnetaD eid dlaboS>P<";
    private static final String de_m13 = ">P/<.nedalretnureh dnu nebrewre >a/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh a< retnu eiS nennök noisrevlloV kcoRgniknihT eiD>P<";
    private static final String de_m21 = de_m11;
    private static final String de_m22 = ">P/<>b/<.tenfföeg rhem thcin driw dnu thcierre noisreV lairT red timiL sad tah ietadnetaD erhI>b<>P<";
    private static final String de_m23 = ">P/<>a/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh a< retnu noisrevlloV eid tztej eiS nefuak redo ,nereiborpuzsua retiew kcoRgniknihT mu ietadnetaD euen enie eiS nelletsrE>P<";

    private String ma() {
//      return x(m_b) + x(m_1) + x(m2a) + x(m_3) + x(m_e);
        return x(m_b) + x(de_m11) + x(de_m12) + x(de_m13) + x(m_e);
    }

    private String mb() {
//      return x(m_b) + x(m_1) + x(m2b) + x(m_3) + x(m_e);
        return x(m_b) + x(de_m21) + x(de_m22) + x(de_m23) + x(m_e);
    }

    private void nu(String m) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(m);
        editorPane.setEditable(false);
        editorPane.setOpaque(true);
        editorPane.addHyperlinkListener((HyperlinkEvent evt) -> {
            if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (evt.getURL() != null) {
                    Open.open(evt.getURL());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        Object[] options = {scrollPane};
        JOptionPane.showOptionDialog(null, options, t(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    private long c() {
        Data data = getData();
        return null == data ? 0 : c(data.getRootAll());
    }

    private long c(Project p) {
        long m = p.size();
        for (Project sp : p.getChildren(Project.class)) {
            m += c(sp);
        }
        return m;
    }

    private String t() {
        return x("noisreV lairT kcoRgniknihT");
    }

    private void f() {
        setData(null);
        nu(mb());
    }

    private void g() {
        nu(ma());
    }

    /**
     * Attempt to load and set the data model from persistent storage using
     * XStream. Upon completion of this method, either the data will be set or
     * an exception will be thrown.
     *
     * @throws Exception if the data could not be loaded and set in some way.
     */
    @Override
    public void load() throws Exception {
        _load();
        if (z()) {
            f();
        } else {
            g();
        }
    }

    private boolean z() {
        return ((int)Math.PI << 4) + 2 < c();
    }

    private void _load() throws Exception {

        setData(null);

        File latestRecoveryFile = getLatestRecoveryFile();

        // return if no data file or recovery file - probably the first time.
        if (!getDataFile().exists() && latestRecoveryFile == null) {
            LOG.info("No data file or recovery file found.");
            return;
        }

        String error = "";

        // Normal case: read the existing data file
        if (getDataFile().exists()) {
            try {
                Data data = XStreamWrapper.instance().load(getDataFile());
                setData(data);
                return;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Failed to read data file. {0}", ex.getMessage());

                // for debugging
                ex.printStackTrace(System.out);

                error = ex.getMessage();
            }
        }

        String m;
        if (error.trim().length() > 0) {
            m = NbBundle.getMessage(getClass(), "file.read.error") + "\n\n" + error + "\n\n";
        } else {
            m = NbBundle.getMessage(getClass(), "file.read.error.short") + "\n\n";
        }

        // if no recovery file show message and return
        if (latestRecoveryFile == null) {
            Object[] options = {NbBundle.getMessage(getClass(), "ok")};
            Component p = null;
            JOptionPane.showOptionDialog(p, m, Constants.TITLE,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return;
        }

        m = m + NbBundle.getMessage(getClass(), "restore.from.recovery.file") + "\n\n";
        Object[] options = {
            NbBundle.getMessage(getClass(), "ok"),
            NbBundle.getMessage(getClass(), "cancel")
        };

        //Component p = WindowManager.getDefault().getMainWindow();
        Component p = null; // above line must be called in AWT thread

        int opt = JOptionPane.showOptionDialog(p, m, Constants.TITLE,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (opt == 1) { // CANCEL
            throw new Exception("The data file could not be read.");
        }

        try {
            Data data = XStreamWrapper.instance().load(latestRecoveryFile);
            removeDataFile();
            setData(data);
            setChanged(true);
        } catch (Exception ex) {
            throw new Exception("The data file could not be recovered.");
        }
    }

    /**
     * Stores the data to file using XStream and makes a backup copy of the
     * file.
     *
     * @throws Exception if an exception occurs while storing the data.
     */
    @Override
    public synchronized void store() throws Exception {
        if (!isLoaded()) {
            return;
        }
        if (getDataFile().exists()) {
            try {
                UtilsFile.renameFile(getDataFile(), getNextRecoveryFile());
                LOG.info("Recovery file has been made.");
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Could not make recovery file. {0}", ex.getMessage());
            }
        }
        // Set the data state to not changed before (not after) writing because
        // a data change could occur during the writing time.
        setChanged(false);

        try {
            XStreamWrapper.instance().store(getData(), getDataFile());
            LOG.fine("Data has been saved to file. ");
        } catch (Exception ex) {
            // write failed so restore (assumed) data changed state and re-throw exception
            setChanged(true);
            throw ex;
        }

        // Generate ICalendar if required.
        if (CalendarPrefs.isICalendarRequired()) {
            try {
                LOG.fine("Starting iCalendar export ... ");
                String filename = UtilsFile.removeExtension(getDataFile().getName()) + ".ics";
                au.com.trgtd.tr.calendar.Calendar.syncToCalendar(filename);
                LOG.fine("Finished iCalendar export.");
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "iCalendar export failed. {0}", ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals(DataPrefs.KEY_AUTOSAVE_MS)) {
            long newAutoSaveIntervalMS = DataPrefs.getAutoSaveIntervalMS();
            if (newAutoSaveIntervalMS != autoSaveIntervalMS) {
                LOG.log(Level.INFO, "Restarting automatic save task at {0}", new Date());
                autoSaveIntervalMS = newAutoSaveIntervalMS;
                stopAutoSaving();
                startAutoSaving();
            }
        } else if (evt.getKey().equals(DataPrefs.KEY_RECOVERY_PATH)) {
            recoveryDir = null;
        }
    }

    /* Thread to periodically save the data. */
    private class AutoSaveThread extends Thread {

        @Override
        public void run() {
            while (autoSave) {

                if (hasChanged()) {
                    LOG.log(Level.INFO, "Auto save task. Saving data. At {0}", new Date());
                    try {
                        store();
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "XStream store exception. {0}", ex.getMessage());
                    }
                } else {
                    LOG.log(Level.INFO, "Auto save task. Data has not changed. At {0}", new Date());
                }

                sleep();
            }
        }

        private void sleep() {
            try {
                Thread.sleep(getAutoSaveIntervalMS());
            } catch (InterruptedException ex) {
            }
        }

    }

    private long autoSaveIntervalMS;

    private long getAutoSaveIntervalMS() {
        if (autoSaveIntervalMS == 0) {
            autoSaveIntervalMS = DataPrefs.getAutoSaveIntervalMS();
        }
        return autoSaveIntervalMS;
    }

    private String x(String x) {
        return new StringBuilder(x).reverse().toString();
    }

    /**
     * Starts a thread to automatically save the data file periodically.
     */
    @Override
    public void startDaemon() {
        startAutoSaving();
    }

    /**
     * Stops the automatic saving thread.
     */
    @Override
    public void stopDaemon() {
        stopAutoSaving();
    }

}

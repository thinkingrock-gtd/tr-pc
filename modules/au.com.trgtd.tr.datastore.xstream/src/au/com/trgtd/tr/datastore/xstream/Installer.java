package au.com.trgtd.tr.datastore.xstream;

import java.io.File;
import org.openide.modules.ModuleInstall;

/**
 * Module installer
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {
    
    @Override
    public void restored() {

        // Set the datafile path to the last used (if possible)
        String path = System.getProperty("tr.datafile");
        if (path != null) {
            File file = new File(path);
            if (file.isFile()) {
                XStreamDataStore.instance().setPath(path);
            }
        }

        CleanupRecoveryFilesScheduler.instance().runTask(0, 0, 20);
        CleanupRecoveryFilesScheduler.instance().startScheduler();

        new CleanupBackupFilesThread().start();

        new BackupThread().start();
    }
    
}

package au.com.trgtd.tr.sync.device.dbx;

import java.io.File;

/**
 * File watcher interface.
 */
public interface FileWatcher {

    /**
     * Gets the directory to watch.
     * @return the directory, which must exist.
     */
    public File getDir();
    
    /**
     * Matches files of interest.
     * @param filename The filename of a file that may be of interest (that is, 
     * a file within the directory that has had an event fired for it).
     * @return true if the file is of interest.
     */
    public boolean match(String filename);
    
    /**
     * Called when the watched file is created.
     */
    public void onFileCreate(String filename);

    /**
     * Called when the watched file is modified.
     */
    public void onFileModify(String filename);
    
    /**
     * Called when the watched file is deleted.
     */
    public void onFileDelete(String filename);
    
}

package au.com.trgtd.tr.sync.iphone;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SyncProgress {

    public static final String PROP_PROGRESS = "progress";
    
    private int progress;

    public SyncProgress() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public synchronized void setProgress(int newProgress) {
        int oldProgress = progress;
        progress = newProgress;
        propertySupport.firePropertyChange(PROP_PROGRESS, oldProgress, newProgress);
    }


    private final PropertyChangeSupport propertySupport;

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }



}

package au.com.trgtd.tr.sync.iphone;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SyncState {

    public static final String PROP_STATE = "state";

    public static enum State {
        None, Connecting, Syncing, Cancelled, Failed, Finished, Aborted
    }

    private State state = State.None;

    public SyncState() {
        propertySupport = new PropertyChangeSupport(this);
    }

    public synchronized void setState(State newState) {
        State oldState = state;
        state = newState;
        propertySupport.firePropertyChange(PROP_STATE, oldState, newState);
    }


    private final PropertyChangeSupport propertySupport;

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }




}

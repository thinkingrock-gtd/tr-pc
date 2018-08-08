package tr.model.goals.ctrl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controller base interface.
 */
public interface Ctrl {

    public int getID();
    
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public boolean hasListeners(String propertyName);

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    public PropertyChangeListener[] getPropertyChangeListeners();

    public void firePropertyChange(PropertyChangeEvent evt);

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue);

    public void firePropertyChange(String propertyName, int oldValue, int newValue);

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) ;

}

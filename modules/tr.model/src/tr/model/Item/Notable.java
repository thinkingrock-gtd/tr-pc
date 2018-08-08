package tr.model.Item;

import java.beans.PropertyChangeListener;

/**
 * Interface for items that have notes.
 */
public interface Notable {

    public String getNotes();

    public void setNotes(String notes);

    public static final String PROP_NOTES = "notes";

    public void addPropertyChangeListener(String property, PropertyChangeListener listener);

    public void removePropertyChangeListener(String property, PropertyChangeListener listener);

}

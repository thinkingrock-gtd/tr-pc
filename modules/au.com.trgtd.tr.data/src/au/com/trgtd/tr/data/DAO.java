package au.com.trgtd.tr.data;

/**
 * Data access object (DAO).
 *
 * @author Jeremy Moore
 * @param <D> The data type.
 */
public interface DAO<D> {

    /**
     * Gets the data transfer object.
     * @return the data transfer object.
     */
    public D getData();

    /**
     * Determines whether or not data exists in the persistent storage.
     * @return true if data exists in storage. 
     */
    public boolean hasPersistantData();

    /**
     * Saves data to persistent storage.
     * @throws java.lang.Exception
     */
    public void persist() throws Exception;

    /**
     * Restores data from persistent storage if possible.
     * @throws java.lang.Exception
     */
    public void restore() throws Exception;

    /**
     * Deletes the persistent data storage.
     */
    public void delete();

    /**
     * Resets to the initial state.
     */
    public void reset();
}

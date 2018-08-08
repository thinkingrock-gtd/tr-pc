package tr.model;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Data lookup singleton.
 *
 * @author Jeremy Moore
 */
public class DataLookup extends ProxyLookup {
    
    private static final DataLookup INSTANCE = new DataLookup();

    /** Private singleton constructor. */
    private DataLookup() {
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static DataLookup instance() {
        return INSTANCE;
    }
    
    /**
     * Sets or removes the data instance.
     * @param data The data instance to set or null to remove.
     */
    public void setData(Data data) {
        if (data == null) {
            setLookups(new Lookup[] {});                        
        } else {
            setLookups(new Lookup[] {Lookups.singleton(data)});            
        }
    }
    
}

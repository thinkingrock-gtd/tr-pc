package au.com.trgtd.tr.cal.model;

/**
 * Event ID. Implementing class need to implement equals and hash code. 
 * 
 * @author Jeremy Moore
 */
public class EventIDFactory {
    
    public static EventID create(long id) {
        return new EventIDImpl(id);
    }
 
    
}

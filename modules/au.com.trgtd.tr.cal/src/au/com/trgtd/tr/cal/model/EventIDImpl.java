package au.com.trgtd.tr.cal.model;

/**
 * Event ID. Implementing classes need to implement equals and hash code. 
 * 
 * @author Jeremy Moore
 */
public class EventIDImpl implements EventID {
    
    private final long id;

    public EventIDImpl(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventIDImpl other = (EventIDImpl) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }
    
}

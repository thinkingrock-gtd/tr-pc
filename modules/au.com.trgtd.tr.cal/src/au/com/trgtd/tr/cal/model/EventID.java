package au.com.trgtd.tr.cal.model;

/**
 * Event ID. Implementing class need to implement equals and hash code. 
 * 
 * @author Jeremy Moore
 */
public interface EventID {
    
    @Override
    public boolean equals(Object object);

    @Override
    public int hashCode();
    
}

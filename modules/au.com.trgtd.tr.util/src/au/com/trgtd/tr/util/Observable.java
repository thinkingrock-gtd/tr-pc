package au.com.trgtd.tr.util;

/**
 * Observable interface.
 * This interface is part of an improved Observer design pattern implementation
 * to that of the java.util package.
 */
public interface Observable {

    /**
     * Adds an observer.
     * @param observer The observer to add.
     */
    public void addObserver(Observer observer);
    
    /**
     * Removes a observer.
     * @param observer The observer to remove.
     */
    public void removeObserver(Observer observer);
    
    /**
     * Removes all observers.
     */
    public void removeObservers();
    
    /**
     * Reset all child observers.
     */
    public void resetObservers();
    
}

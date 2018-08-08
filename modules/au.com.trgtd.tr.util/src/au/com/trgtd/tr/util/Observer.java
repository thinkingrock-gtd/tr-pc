
package au.com.trgtd.tr.util;

/**
 * Observer interface.
 * This interface is part of an improved Observer design pattern implementation
 * to that of the java.util package.
 */
public interface Observer {

    /**
     * Called by an observable to notify that it has changed.
     * @param observable The observable.
     * @param argument The argument.
     */
    public void update(Observable observable, Object argument);
    
}

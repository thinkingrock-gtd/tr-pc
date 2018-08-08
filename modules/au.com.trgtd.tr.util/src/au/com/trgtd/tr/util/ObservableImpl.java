package au.com.trgtd.tr.util;

import java.io.Serializable;
import java.util.WeakHashMap;

/**
 * Observable implementation that tries to avoid memory leaks by using weak
 * references to observers.
 *
 * This class is part of an improved Observer design pattern implementation to
 * that of the java.util package.
 *
 * @author Jeremy Moore
 */
public class ObservableImpl implements Observable, Serializable {

    private static final long serialVersionUID = 1;

    private transient WeakHashMap<Observer, Object> observers;

    /**
     * Constructs a default instance.
     */
    public ObservableImpl() {
    }

    /**
     * Adds an observer to the set of observers, provided that it is not the
     * same as some observer already in the set. The order in which
     * notifications will be delivered to multiple observers is not specified.
     *
     * @param observer The observer to add.
     */
    @Override
    public synchronized void addObserver(Observer observer) {
        if (observer == null) {
            return;
        }
        if (observers == null) {
            observers = new WeakHashMap<Observer, Object>();
        }
        observers.put(observer, null);
    }

    /**
     * Removes an observer from the set of observers of this object.
     *
     * @param observer The observer to remove.
     */
    @Override
    public synchronized void removeObserver(Observer observer) {
        if (observer == null || observers == null) {
            return;
        }
        observers.remove(observer);
    }

    /**
     * Removes all observers from the list.
     */
    @Override
    public synchronized void removeObservers() {
        if (observers == null) {
            return;
        }
        observers.clear();
    }

    /**
     * Override to reset all child observers.
     */
    @Override
    public synchronized void resetObservers() {
    }

    /**
     * Notify all observers. Each observer has its update method called with two
     * arguments: the observable and null.
     *
     * @param observable The observable.
     */
    public void notifyObservers(Observable observable) {
        notifyObservers(observable, null);
    }

    /**
     * Notify all observers. Each observer has its update method called with two
     * arguments: the observable and the object.
     *
     * @param observable The observable.
     * @param object The object or null.
     */
    public synchronized void notifyObservers(Observable observable, Object object) {
        if (observers == null) {
            return;
        }
//        for (Observer observer : observers.keySet()) {
//            observer.update(observable, object);
//        }

        Observer[] arr = observers.keySet().toArray(new Observer[0]);
        for (Observer o : arr) {
            if (o != null) {
                o.update(observable, object);                
            }
        }
    }

}

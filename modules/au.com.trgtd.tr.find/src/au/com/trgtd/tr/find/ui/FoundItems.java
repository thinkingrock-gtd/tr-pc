package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FoundItems implements Observable {

    private final ObservableImpl observable = new ObservableImpl();
    private final List<FoundItem> list = new ArrayList<FoundItem>();
    private String text = "";

    public String getText() {
        return text;
    }

    public void reset(String text) {
        synchronized(this) {
            this.text = text;
            list.clear();
            observable.notifyObservers(this);
        }
    }

    public boolean add(FoundItem item) {
        synchronized(this) {
            if (list.add(item)) {
                observable.notifyObservers(this);
                return true;
            }
            return false;
        }
    }

    public int size() {
        synchronized(this) {
            return list.size();
        }
    }

    public List<FoundItem> getItems() {
        synchronized(this) {
            return Collections.unmodifiableList(new ArrayList(list));
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observable.removeObserver(observer);
    }

    @Override
    public void removeObservers() {
        observable.removeObservers();
    }

    @Override
    public void resetObservers() {
        observable.resetObservers();
    }
}

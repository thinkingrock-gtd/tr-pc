package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.util.Collections;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

public class FoundChildren extends Children.Keys implements Observer {

    private final FoundItems items;

    public FoundChildren(FoundItems controller) {
        this.items = controller;
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof FoundItem) {
            return new Node[]{new FoundNode((FoundItem) key)};
        }
        return new Node[0];
    }

    @Override
    protected void addNotify() {
        synchronized (this) {
            if (items != null) {
                setKeys(items.getItems());
                items.addObserver(this);
            }
            super.addNotify();
        }
    }

    @Override
    protected void removeNotify() {
        synchronized (this) {
            setKeys(Collections.EMPTY_SET);
            if (items != null) {
                items.removeObserver(this);
            }
            super.removeNotify();
        }
    }

    @Override
    public void update(Observable obs, Object arg) {
        synchronized (this) {
            if (items != null) {
                setKeys(items.getItems());
            } else {
                setKeys(Collections.EMPTY_SET);
            }
        }
    }
}


package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.calendar.TrCalModel;
import au.com.trgtd.tr.view.calendar.TrCalModel.DelegatedItem;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Children for delegated actions.
 */
public class DelegatedChildren extends Children.Keys<DelegatedItem> { 

    private final DateCtlr dateCtlr;
    private final TrCalModel calModel;
    
    public DelegatedChildren(DateCtlr dateCtlr, TrCalModel calModel) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
    }

    private List<DelegatedItem> getItems() {
        return calModel.getDelegatedItems(dateCtlr.getDay());
    }
    
    @Override
    protected Node[] createNodes(DelegatedItem item) {
        return new Node[]{new DelegatedNode(item.getAction())};
    }

    @Override
    protected void addNotify() {
        setKeys(getItems());
        dateCtlr.addPropertyChangeListener(pcl);
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        Set<DelegatedItem> empty = Collections.emptySet();        
        setKeys(empty);
        dateCtlr.removePropertyChangeListener(pcl);
        super.removeNotify();
    }

    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            setKeys(getItems());
        }
    };
    
}

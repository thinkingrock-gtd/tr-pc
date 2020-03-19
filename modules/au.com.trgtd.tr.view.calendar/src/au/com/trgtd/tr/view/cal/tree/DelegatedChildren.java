package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.view.cal.CalModelImp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Children for delegated actions.
 */
public class DelegatedChildren extends Children.Keys<CalEvent> { 

    private final DateCtlr dateCtlr;
    private final CalModelImp calModel;
    
    public DelegatedChildren(DateCtlr dateCtlr, CalModelImp calModel) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
    }

    private List<CalEvent> getItems() {
        return calModel.getEventsDelegatedFollowupOn(dateCtlr.getDay());
    }
    
    @Override
    protected Node[] createNodes(CalEvent item) {
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
        this.setKeys(new CalEvent[0]);
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

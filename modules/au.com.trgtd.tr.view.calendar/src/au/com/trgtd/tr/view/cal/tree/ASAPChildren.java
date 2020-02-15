package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.view.cal.CalModelImp;
import au.com.trgtd.tr.view.cal.tree.ASAPNodeRoot.Type;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Do ASAP actions children.
 */
public class ASAPChildren extends Children.Keys<CalEvent> {

    private final DateCtlr dateCtlr;
    private final Type type;
    private final CalModelImp calModel;

    public ASAPChildren(DateCtlr dateCtlr, CalModelImp calModel, Type type) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
        this.type = type;
    }

    private List<CalEvent> getItems() {
        switch (type) {
            case Starting:
                return calModel.getEventsDoASAPStartOn(dateCtlr.getDay());
            case Overdue:
                return calModel.getEventsDoASAPOverdue(dateCtlr.getDay());
            default:
                return calModel.getEventsDoASAPDueOn(dateCtlr.getDay());
        }
    }

    @Override
    protected Node[] createNodes(CalEvent item) {
        return new Node[]{new ASAPNode(item.getAction())};
    }

    @Override
    protected void addNotify() {
        setKeys(getItems());
        dateCtlr.addPropertyChangeListener(pcl);
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        setKeys(Collections.EMPTY_LIST);
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

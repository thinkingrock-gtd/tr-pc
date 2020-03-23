package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.view.cal.CalModelImp;
import au.com.trgtd.tr.view.cal.tree.ASAPNodeRoot.Type;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.List;

import org.openide.nodes.Children;
import org.openide.nodes.Node;
import tr.model.action.Action;

/**
 * Do ASAP actions children.
 */
public class ASAPChildren extends Children.Keys<CalEvent> {

    private static final Comparator<CalEvent> COMPARATOR = new Comparator<CalEvent>() {
        @Override
        public int compare(CalEvent e1, CalEvent e2) {
            Action a1 = e1.getAction();
            Action a2 = e2.getAction();
            int c = a1.getTopic().compareTo(a2.getTopic());
            if (c == 0) {
                c = a1.getDescription().compareToIgnoreCase(a2.getDescription());
            }
            return c;
        }
    };

    private final DateCtlr dateCtlr;
    private final Type type;
    private final CalModel calModel;

    public ASAPChildren(DateCtlr dateCtlr, CalModelImp calModel, Type type) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
        this.type = type;
    }

    private List<CalEvent> getItems() {
        List<CalEvent> items;
        switch (type) {
            case Starting:
                items = calModel.getEventsDoASAPStartOn(dateCtlr.getDay());
                break;
            case Overdue:
                items = calModel.getEventsDoASAPOverdue(dateCtlr.getDay());
                break;
            default:
                items = calModel.getEventsDoASAPDueOn(dateCtlr.getDay());
                break;
        }
        items.sort(COMPARATOR);
        return items;
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
        setKeys(new CalEvent[0]);
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

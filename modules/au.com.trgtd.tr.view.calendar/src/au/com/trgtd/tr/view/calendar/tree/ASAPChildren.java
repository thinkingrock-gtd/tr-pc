package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.calendar.TrCalModel;
import au.com.trgtd.tr.view.calendar.TrCalModel.ASAPItem;
import au.com.trgtd.tr.view.calendar.tree.ASAPNodeRoot.Type;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Do ASAP actions children.
 */
public class ASAPChildren extends Children.Keys<ASAPItem> { 

    private final DateCtlr dateCtlr;
    private final Type type;
    private final TrCalModel calModel;
    
    public ASAPChildren(DateCtlr dateCtlr, TrCalModel calModel, Type type) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
        this.type = type;
    }

    private List<ASAPItem> getItems() {
        if (type == Type.Starting) {
            return calModel.getDoASAPStart(dateCtlr.getDay());
        } else {
            return calModel.getDoASAPDue(dateCtlr.getDay());
        } 
    }
    
    @Override
    protected Node[] createNodes(ASAPItem item) {
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
        Set<ASAPItem> empty = Collections.emptySet();        
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

package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.calendar.TrCalModel;
import au.com.trgtd.tr.view.calendar.tree.ASAPNodeRoot.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Children of the root node.
 */
public class RootChildren extends Children.Keys<Node> { 

    private final DateCtlr dateCtlr;
    private final TrCalModel calModel;
    
    public RootChildren(DateCtlr dateCtlr, TrCalModel calModel) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
    }

    private List<Node> getItems() {
        List<Node> items = new ArrayList<Node>();
        items.add(new ASAPNodeRoot(dateCtlr, calModel, Type.Due));
        items.add(new ASAPNodeRoot(dateCtlr, calModel, Type.Starting));
        items.add(new DelegatedNodeRoot(dateCtlr, calModel));
        return items;
    }
    
    @Override
    protected Node[] createNodes(Node node) {
        return new Node[]{node};
    }

    @Override
    protected void addNotify() {
        setKeys(getItems());
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        Set<Node> empty = Collections.emptySet();        
        setKeys(empty);
        super.removeNotify();
    }

}

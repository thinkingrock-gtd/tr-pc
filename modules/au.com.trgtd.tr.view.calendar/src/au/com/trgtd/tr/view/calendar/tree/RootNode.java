package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.calendar.TrCalModel;
import org.openide.nodes.AbstractNode;

/**
 * The root node.
 * @author Jeremy Moore
 */
public class RootNode extends AbstractNode {

    /** Constructs a new instance. */
    public RootNode(DateCtlr dateCtlr, TrCalModel model) {
        super(new RootChildren(dateCtlr, model));
    }
    
}

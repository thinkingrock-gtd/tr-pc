package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.cal.CalModelImp;
import org.openide.nodes.AbstractNode;

/**
 * The root node.
 * @author Jeremy Moore
 */
public class RootNode extends AbstractNode {

    /** Constructs a new instance. */
    public RootNode(DateCtlr dateCtlr, CalModelImp model) {
        super(new RootChildren(dateCtlr, model));
    }
    
}

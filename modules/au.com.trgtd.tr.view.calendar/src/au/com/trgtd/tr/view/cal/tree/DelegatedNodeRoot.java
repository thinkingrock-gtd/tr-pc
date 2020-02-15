package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.cal.CalModelImp;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Root node for delegated actions.
 * @author Jeremy Moore
 */
public class DelegatedNodeRoot extends AbstractNode {

    private final InstanceContent content;

    /**
     * Constructs a new instance.
     * @param dateCtlr The date controller.
     * @param calModel The calendar model.
     */
    public DelegatedNodeRoot(DateCtlr dateCtlr, CalModelImp calModel) {
        this(new DelegatedChildren(dateCtlr, calModel), new InstanceContent());
    }

    private DelegatedNodeRoot(DelegatedChildren children, InstanceContent content) {
        super(children, new AbstractLookup(content));
        this.content = content;
        this.content.add(this);
        setIconBaseWithExtension("au/com/trgtd/tr/view/calendar/resource/folder.png");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CalModelImp.class, "delegated.followup");
    }
    
}

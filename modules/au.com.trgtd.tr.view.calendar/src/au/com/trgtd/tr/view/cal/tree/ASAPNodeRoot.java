package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.cal.CalModelImp;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Root node for Do ASAP actions.
 *
 * @author Jeremy Moore
 */
public class ASAPNodeRoot extends AbstractNode {

    private final static String DOASAP_DUE = NbBundle.getMessage(CalModelImp.class, "doasap.due");
    private final static String DOASAP_STARTING = NbBundle.getMessage(CalModelImp.class, "doasap.starting");
    private final static String DOASAP_OVERDUE = NbBundle.getMessage(CalModelImp.class, "doasap.overdue");

    public enum Type {
        Starting, Due, Overdue
    }
    private final Type type;
    private final InstanceContent content;

    /**
     * Constructs a new instance.
     *
     * @param Action The action.
     */
    public ASAPNodeRoot(DateCtlr dateCtlr, CalModelImp calModel, Type type) {
        this(new ASAPChildren(dateCtlr, calModel, type), new InstanceContent(), type);
    }

    private ASAPNodeRoot(ASAPChildren children, InstanceContent content, Type type) {
        super(children, new AbstractLookup(content));
        this.content = content;
        this.content.add(this);
        this.type = type;
        setIconBaseWithExtension("au/com/trgtd/tr/view/calendar/resource/folder.png");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        switch (type) {
            case Starting:
                return DOASAP_STARTING;
            case Overdue:
                return DOASAP_OVERDUE;
            default:
                return DOASAP_DUE;
        }
    }
}

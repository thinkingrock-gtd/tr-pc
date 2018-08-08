package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.calendar.DayViewTree;
import au.com.trgtd.tr.view.calendar.TrCalModel;
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

    private final static String DOASAP_DUE = NbBundle.getMessage(DayViewTree.class, "doasap.due");
    private final static String DOASAP_STARTING = NbBundle.getMessage(DayViewTree.class, "doasap.starting");

    public enum Type {
        Starting, Due
    }
    private final Type type;
    private final InstanceContent content;

    /**
     * Constructs a new instance.
     * @param Action The action.
     */
    public ASAPNodeRoot(DateCtlr dateCtlr, TrCalModel calModel, Type type) {
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
        return type == Type.Due ? DOASAP_DUE : DOASAP_STARTING;
    }
}

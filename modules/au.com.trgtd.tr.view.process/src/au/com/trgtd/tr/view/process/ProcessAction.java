package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.resource.Resource;
import org.openide.nodes.Node;
import org.openide.util.*;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to process a thought.
 *
 * @author Jeremy Moore
 */
public class ProcessAction extends CookieAction {

    public ProcessAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }

    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    @Override
    protected String iconResource() {
        return Resource.Process;
    }

    /** Gets the display name.
     * @return  */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessAction");
    }

    /** Gets help context.
     * @return  */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[]{ProcessCookie.class};
    }

    @Override
    public int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction(Node[] nodes) {
        ProcessCookie cookie = (ProcessCookie) nodes[0].getCookie(ProcessCookie.class);
        if (cookie != null) {
            cookie.process();
        }
    }
}


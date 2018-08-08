package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.resource.Resource;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add an action for a thought and continue processing.
 *
 * @author Jeremy Moore
 */
public class ProcessAddAction extends CookieAction {

    public ProcessAddAction() {
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
        return Resource.Add;
    }

    /** Gets the display name.
     * @return  */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessAddAction");
    }

    /** Gets help context.
     * @return  */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[]{ProcessAddCookie.class};
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
    protected boolean enable(Node[] nodes) {
        if (null == nodes || nodes.length < 1) {
            return false;
        }
        ProcessAddCookie cookie = (ProcessAddCookie) nodes[0].getCookie(ProcessAddCookie.class);
        return cookie != null && cookie.canProcessAdd();
    }

    @Override
    public void performAction(Node[] nodes) {
        if (null == nodes || nodes.length < 1) {
            return;
        }
        ProcessAddCookie cookie = (ProcessAddCookie) nodes[0].getCookie(ProcessAddCookie.class);
        if (cookie != null) {
            cookie.processAdd();
        }
    }
}


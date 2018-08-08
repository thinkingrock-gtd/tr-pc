package au.com.trgtd.tr.view;

import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import au.com.trgtd.tr.resource.Icons;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.thought.Thought;

/**
 * Action to process a thought.
 *
 * @author Jeremy Moore
 */
public class ProcessAction extends CookieAction {

    public ProcessAction() {
        setIcon(Icons.ProcessThoughts);
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * Gets the cookie classes.
     * @return The Thought class.
     */
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Thought.class };
    }

    @Override
    public int mode() {
//      return MODE_EXACTLY_ONE;
        return MODE_ALL;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction(Node[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return;
        }

        WindowUtils.closeWindows();

        for (Node node : nodes) {
            ProcessCookie cookie = (ProcessCookie) node.getCookie(ProcessCookie.class);
            if (cookie != null) {
                cookie.process();
            }
        }

        ProcessThoughtsStarter s = ProcessThoughtsStarterLookup.starter;
        if (s != null) {
            s.invoke();
        }
    }
}


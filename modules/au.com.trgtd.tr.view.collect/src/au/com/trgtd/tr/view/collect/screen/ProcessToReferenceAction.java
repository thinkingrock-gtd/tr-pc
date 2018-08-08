package au.com.trgtd.tr.view.collect.screen;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.thought.Thought;

/**
 * Action to process thoughts to reference items.
 *
 * @author Jeremy Moore
 */
public class ProcessToReferenceAction extends CookieAction {

    public ProcessToReferenceAction() {
        super();
    }

    @Override
    protected String iconResource() {
        return "ProcessToReference.png";
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessToReferenceAction");
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
        return MODE_ALL;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction(Node[] nodes) {
        for (Node node : nodes) {
            ProcessToReferenceCookie cookie = (ProcessToReferenceCookie) node.getCookie(ProcessToReferenceCookie.class);
            if (cookie != null) {
                cookie.processToReference();
            }
        }
    }
}


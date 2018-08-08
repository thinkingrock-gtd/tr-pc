package au.com.trgtd.tr.view.collect.screen;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.thought.Thought;

/**
 * Action to process thoughts to someday/maybe items.
 *
 * @author Jeremy Moore
 */
public class ProcessToSomedayAction extends CookieAction {

    public ProcessToSomedayAction() {
        super();
    }

    @Override
    protected String iconResource() {
        return "ProcessToSomeday.png";
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessToSomedayAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

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
            ProcessToSomedayCookie cookie = (ProcessToSomedayCookie) node.getCookie(ProcessToSomedayCookie.class);
            if (cookie != null) {
                cookie.processToSomeday();
            }
        }
    }
}


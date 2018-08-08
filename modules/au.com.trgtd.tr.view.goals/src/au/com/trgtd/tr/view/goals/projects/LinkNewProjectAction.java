package au.com.trgtd.tr.view.goals.projects;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * Link new project action.
 */
public class LinkNewProjectAction extends CookieAction {

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "link.new.project");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class[] cookieClasses() {
        return new Class[] { LinkNewProjectCookie.class };
    }

    @Override
    protected void performAction(Node[] nodes) {
        LinkNewProjectCookie cookie = nodes[0].getCookie(LinkNewProjectCookie.class);
        if (cookie != null) {
            cookie.linkNewProject();
        }
    }

}


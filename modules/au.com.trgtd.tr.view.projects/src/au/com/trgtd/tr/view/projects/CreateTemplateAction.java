package au.com.trgtd.tr.view.projects;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.project.Project;

/**
 * Action to create a template for a selected project.
 * Mantis ID: 0001959
 *
 * @author Jeremy Moore
 */
public class CreateTemplateAction extends CookieAction {
    
    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_CreateTemplateAction");
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Project.class };
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
        CreateTemplateCookie cookie = (CreateTemplateCookie)nodes[0].getCookie(CreateTemplateCookie.class);
        if (cookie != null) {
            cookie.createTemplate();
        }
    }
    
}


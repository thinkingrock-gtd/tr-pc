package au.com.trgtd.tr.email;

import au.com.trgtd.tr.email.task.FetchEmailThread;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * Action to retrieve email.
 * 
 * @author Jeremy Moore
 */
public final class RetrieveEmailAction extends CallableSystemAction {

    @Override
    public void performAction() {
        new FetchEmailThread().start();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RetrieveEmailAction.class, "CTL_RetrieveEmailAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

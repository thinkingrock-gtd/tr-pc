package au.com.trgtd.tr.view.overview;

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;

/**
 * Action which shows the overview window.
 */
//public class OverviewAction extends AbstractAction implements InitialAction {
public class OverviewAction extends CallableSystemAction implements InitialAction {

    public OverviewAction() {
        super();
    }

    @Override
    protected String iconResource() {
        return Resource.Overview;
    }
        
    /** Gets the initial action identifier. */
    public String getID() {
        return "overview";
    }

    public String getName() {
        return NbBundle.getMessage(OverviewAction.class, "CTL_OverviewAction");
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                WindowUtils.closeWindows();

                TopComponent tc = OverviewTopComponent.findInstance();

                Mode mode = WindowManager.getDefault().findMode("overview");
                if (mode != null) {
                    mode.dockInto(tc);
                }

                tc.open();
                tc.requestActive();
            }
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.overview");
    }
}

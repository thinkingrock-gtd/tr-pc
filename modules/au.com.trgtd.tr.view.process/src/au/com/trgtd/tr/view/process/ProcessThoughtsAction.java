package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.view.ProcessThoughtsStarter;
import au.com.trgtd.tr.view.ProcessThoughtsStarterLookup;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;

/**
 * Action which shows the process thoughts window.
 */
public class ProcessThoughtsAction extends CallableSystemAction implements InitialAction, ProcessThoughtsStarter {

    public ProcessThoughtsAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
        ProcessThoughtsStarterLookup.starter = this;
    }

    @Override
    protected String iconResource() {
        return Resource.ProcessThoughts;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessThoughtsAction");
    }

    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the action identifier.
     * @return  */
    @Override
    public String getID() {
        return "process-thoughts";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {        
                Data data = (Data) DataLookup.instance().lookup(Data.class);
                if (data == null) {
                    return;
                }

                WindowUtils.closeWindows();

                TopComponent tc = ProcessTopComponent.findInstance();

                Mode mode = WindowManager.getDefault().findMode("process");
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
        return new HelpCtx("tr.view.process");
    }

    @Override
    public void invoke() {
        performAction();
    }

}



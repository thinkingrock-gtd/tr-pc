package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import java.awt.EventQueue;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action which shows the priorities screen.
 */
public class PrioritiesAction extends CallableSystemAction {
    
    public PrioritiesAction() {
        super();
        enableDisable();
        Lookup.Result<Data> r = DataLookup.instance().lookup(new Lookup.Template<Data>(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }
    
    @Override
    protected String iconResource() {
        return "au/com/trgtd/tr/view/criteria/screen/priority.png";
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_PrioritiesAction");
    }
    
    private void enableDisable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = DataLookup.instance().lookup(Data.class);
                setEnabled(data != null);
            }
        });
    }
    
    /** Gets the action identifier. */
    public String getID() {
        return "priorities";
    }
    
    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = DataLookup.instance().lookup(Data.class);
                if (data == null) return;
                
                WindowUtils.closeWindows();
                
                TopComponent tc = PrioritiesTopComponent.findInstance();
                
                Mode mode = WindowManager.getDefault().findMode("Setup");
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
        return new HelpCtx("tr.view.criteria");
    }
    
}

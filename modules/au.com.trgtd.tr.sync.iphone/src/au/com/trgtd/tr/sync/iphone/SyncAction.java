package au.com.trgtd.tr.sync.iphone;

import java.awt.EventQueue;
import java.awt.Frame;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

public final class SyncAction extends CallableSystemAction {

    /** Constructor. */
    public SyncAction() {
        super();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
        SyncPrefs.addListener(new PreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent evt) {
                enableDisable();
            }
        });
        enableDisable();
    }

    @Override
    public void performAction() {        
        if (SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_WIFI) {
            if (SyncManager.getDefault().canStartSync()) {
                Frame frame = WindowManager.getDefault().getMainWindow();
                (new SyncDialog(frame)).setVisible(true);
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(this.getClass(), "CTL_SyncAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return "au/com/trgtd/tr/sync/iphone/iphone.png";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    private void enableDisable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = DataLookup.instance().lookup(Data.class);
                setEnabled(data != null && SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_WIFI);
            }
        });
    }

}

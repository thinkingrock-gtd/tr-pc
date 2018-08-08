package au.com.trgtd.tr.prefs.ui.utils;

import au.com.trgtd.tr.appl.InitialAction;
import au.com.trgtd.tr.appl.InitialActionLookup;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;

/**
 * Utilities for TR Windows.
 *
 * @author Jeremy Moore
 */
public class WindowUtils {
    
    private static final Logger LOG = Logger.getLogger("tr.viw");    
    
    /* Disabled creation of new instances */
    private WindowUtils() {
    }
    
    private static final TopComponent[] TC_ARRAY = new TopComponent[0];
    
    /**
     * Closes all opened windows.
     */
    public static synchronized void closeWindows() {       
        TopComponent.Registry reg = WindowManager.getDefault().getRegistry();

        TopComponent[] tcs = (TopComponent[])reg.getOpened().toArray(TC_ARRAY);

        int n = 0;

        for (int i = 0; i < 10; i++) { // limit attempts
            
            for (TopComponent tc : tcs) {
                if (tc instanceof StayOpen) {
                    n++;
                    continue;
                }
                tc.close();
            }            
            tcs = (TopComponent[])reg.getOpened().toArray(TC_ARRAY);
            if (tcs.length <= n) {
                return;
            }
        }        
        LOG.severe("Windows probably not closed successfully.");        
    }
    
    /**
     * Open the initial window(s) by running the initial action defined in the
     * GUI user preferences.
     */
    public static void openInitialWindow() {
        String id = GUIPrefs.getInitialActionID();
        if (!id.equals(GUIPrefs.INITIAL_ACTION_ID_NONE)) {
            Lookup.Result r = InitialActionLookup.instance().lookup(new Lookup.Template(InitialAction.class));
            Collection c = r.allInstances();
            for (Iterator iter = c.iterator(); iter.hasNext(); ) {
                InitialAction action = (InitialAction)iter.next();
                if (action.getID().equals(id)) {
                    ((Action)action).actionPerformed(null);
                    return;
                }
            }
        }
    }
    
    /** Open the overview window. */
    public static void openOverviewWindow() {
        String id = "overview";
        Lookup.Result r = InitialActionLookup.instance().lookup(new Lookup.Template(InitialAction.class));
        Collection c = r.allInstances();
        for (Iterator iter = c.iterator(); iter.hasNext(); ) {
            InitialAction action = (InitialAction)iter.next();
            if (action.getID().equals(id)) {
                ((Action)action).actionPerformed(null);
                return;
            }
        }
    }
    
}


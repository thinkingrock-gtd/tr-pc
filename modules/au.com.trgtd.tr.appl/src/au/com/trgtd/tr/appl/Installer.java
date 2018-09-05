package au.com.trgtd.tr.appl;

import au.com.trgtd.tr.appl.prefs.ApplicationPrefs;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Module installer.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {        
        final int ms = ApplicationPrefs.getInactivityMs();        
        if (ms > 0) {
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                @Override
                public void run() {
                    InactivityDetector.start(ms);
                }
            });            
        }
    }

}

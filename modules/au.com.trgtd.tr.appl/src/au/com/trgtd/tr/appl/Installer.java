package au.com.trgtd.tr.appl;

import au.com.trgtd.tr.appl.prefs.ApplicationPrefs;
import au.com.trgtd.tr.appl.tasks.idlequit.IdleQuitTaskScheduler;
import org.openide.modules.ModuleInstall;

/**
 * Module installer.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        Long delayMs = ApplicationPrefs.getIdleQuitMs();
        if (delayMs != null) {
            IdleQuitTaskScheduler.instance().start(delayMs);
        }
    }

}

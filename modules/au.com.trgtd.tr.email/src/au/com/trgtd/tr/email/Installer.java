package au.com.trgtd.tr.email;

import au.com.trgtd.tr.email.prefs.EmailPrefs;
import au.com.trgtd.tr.email.task.FetchEmailScheduler;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        if (EmailPrefs.isEmailFetchAtStartup()) {
            FetchEmailScheduler.instance().schedule(5000);
        }
        if (EmailPrefs.isEmailFetchSchedule()) {
            long interval = EmailPrefs.getEmailFetchIntervalMS();
            if (interval > 0) {
                FetchEmailScheduler.instance().schedule(interval, interval);
            }
        }
    }

}

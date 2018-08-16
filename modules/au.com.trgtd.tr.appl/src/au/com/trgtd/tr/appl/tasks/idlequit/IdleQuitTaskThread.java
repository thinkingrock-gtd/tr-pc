package au.com.trgtd.tr.appl.tasks.idlequit;

import au.com.trgtd.tr.appl.prefs.ApplicationPrefs;
import au.com.trgtd.tr.util.UtilsOS;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.LifecycleManager;
import org.openide.util.NbBundle;

/**
 * Idle-quit task thread.
 *
 * @author Jeremy Moore
 */
final class IdleQuitTaskThread extends Thread {

    private static final Logger LOG = Logger.getLogger("tr.idle.quit");

    /**
     * Constructs a new instance.
     */
    public IdleQuitTaskThread() {
        super(NbBundle.getMessage(IdleQuitTaskThread.class, "idle.quit.task"));
    }

    @Override
    public void run() {
        LOG.info("Began idle check"); 
        process();
        LOG.info("Ended idle check");
    }

    private void process() {
        final long quitMs = ApplicationPrefs.getIdleQuitMs();
        if (quitMs == 0) {
            LOG.info("Idle quit preference is not used");          
            return;
        }
        LOG.log(Level.INFO, "Idle quit time: {0}ms", quitMs);      
        final long diffMs = quitMs - UtilsOS.getIdleMs();
        if (diffMs > 0) {
            // run task again after remaining time
            LOG.log(Level.INFO, "Idle remaining time: {0}ms", diffMs);      
            IdleQuitTaskScheduler.instance().start(diffMs);
        } else {
            // quit application            
            LOG.log(Level.INFO, "Idle quit time exceded - quitting");    
            LifecycleManager.getDefault().exit();
        }
    }

}

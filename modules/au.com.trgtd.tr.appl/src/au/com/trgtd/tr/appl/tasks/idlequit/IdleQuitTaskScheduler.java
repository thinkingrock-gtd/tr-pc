package au.com.trgtd.tr.appl.tasks.idlequit;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Scheduler for the task.
 *
 * @author Jeremy Moore
 */
public class IdleQuitTaskScheduler {

    private static final Logger LOG = Logger.getLogger("tr.idle.quit");

    // Singleton instance.
    private static IdleQuitTaskScheduler instance;

    /**
     * Gets the singleton instance.
     *
     * @return The instance.
     */
    public static IdleQuitTaskScheduler instance() {
        if (instance == null) {
            instance = new IdleQuitTaskScheduler();
        }
        return instance;
    }

    private IdleQuitTaskScheduler() {
    }

    private Timer timer;

    /**
     * Stops the scheduled task if there is one.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            LOG.info("Cancelled idle check");
        }
    }

    /**
     * Start the task after a given delay.
     *
     * @param delay The number of milliseconds delay.
     */
    public void start(long delay) {
        stop();
        timer = new Timer();
        timer.schedule(new IdleQuitTimerTask(), delay);
        LOG.info("Scheduled idle check");
    }

    /**
     * Timer task to run the idle-quit task thread.
     */
    private static class IdleQuitTimerTask extends TimerTask {

        @Override
        public void run() {
            new IdleQuitTaskThread().start();
        }
    }

}

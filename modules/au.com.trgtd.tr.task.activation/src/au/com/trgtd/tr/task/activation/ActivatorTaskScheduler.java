package au.com.trgtd.tr.task.activation;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduler for the activator task.
 *
 * @author Jeremy Moore
 */
public class ActivatorTaskScheduler {

    // Singleton instance.
    private static ActivatorTaskScheduler instance;

    /**
     * Gets the singleton instance.
     *
     * @return The instance.
     */
    public static ActivatorTaskScheduler instance() {
        if (instance == null) {
            instance = new ActivatorTaskScheduler();
        }
        return instance;
    }

    private static final long MILLISECOND = 1;
    private static final long SECOND = 1000 * MILLISECOND;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    private Timer timerSingle;
    private TimerTask taskSingle;
    private Timer timerDaily;
    private TimerTask taskDaily;

    /* Private singleton constructor. */
    private ActivatorTaskScheduler() {
    }

    /**
     * Starts the daily scheduler. Schedules the task to run daily at 00:01 AM.
     */
    public void startScheduler() {
        stopScheduler();
        taskDaily = new ActivatorTimerTask();
        timerDaily = new Timer();
        timerDaily.scheduleAtFixedRate(taskDaily, getDailyTime(), DAY);
    }

    /**
     * Gets the date of the first time the task is to be scheduled.
     *
     * @return The date of tomorrow 00:01 AM.
     */
    private Date getDailyTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Stops the daily scheduler.
     */
    public void stopScheduler() {
        if (timerDaily != null) {
            timerDaily.cancel();
            timerDaily = null;
        }
    }

    /**
     * Run the task once only after the given delay.
     *
     * @param hours The number of hours delay.
     * @param minutes The number of minutes delay.
     * @param seconds The number of seconds delay.
     */
    public void runTask(int hours, int minutes, int seconds) {
        if (timerSingle != null) {
            timerSingle.cancel();
            timerSingle = null;
        }
        long delay = (hours * HOUR) + (minutes * MINUTE) + (seconds * SECOND);
        taskSingle = new ActivatorTimerTask();
        timerSingle = new Timer();
        timerSingle.schedule(taskSingle, delay);
    }

    /**
     * TimerTask that runs the activator task thread.
     */
    private static class ActivatorTimerTask extends TimerTask {

        @Override
        public void run() {
            new ActivatorTaskThread().start();
        }
    }

}

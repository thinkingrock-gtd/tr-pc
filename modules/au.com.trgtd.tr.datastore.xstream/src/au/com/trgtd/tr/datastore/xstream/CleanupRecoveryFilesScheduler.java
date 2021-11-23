/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.datastore.xstream;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduler for the cleanup recovery files task.
 *
 * @author Jeremy Moore
 */
public class CleanupRecoveryFilesScheduler {

    // Singleton instance.
    private static CleanupRecoveryFilesScheduler instance;

    /**
     * Gets the singleton instance.
     * @return the instance.
     */
    public static CleanupRecoveryFilesScheduler instance() {
        if (instance == null) {
            instance = new CleanupRecoveryFilesScheduler();
        }
        return instance;
    }

    private static final long MILLISECOND = 1;
    private static final long SECOND = 1000 * MILLISECOND;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
//  private static final long DAY = 24 * HOUR;

    private Timer timerSingle;
    private TimerTask taskSingle;
    private Timer timerPeriodic;
    private TimerTask taskPeriodic;

    /* Private singleton constructor. */
    private CleanupRecoveryFilesScheduler() {
    }

    /**
     * Starts the daily scheduler. Schedules the task to run daily at 00:01 AM.
     */
    public void startScheduler() {
        stopScheduler();
        taskPeriodic = new CleanupTimerTask();
        timerPeriodic = new Timer();
        timerPeriodic.scheduleAtFixedRate(taskPeriodic, getFirstTime(), HOUR);
    }

    private Date getFirstTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

    /**
     * Stops the daily scheduler.
     */
    public void stopScheduler() {
        if (timerPeriodic != null) {
            timerPeriodic.cancel();
            timerPeriodic = null;
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
        taskSingle = new CleanupTimerTask();
        timerSingle = new Timer();
        timerSingle.schedule(taskSingle, delay);
    }

    /**
     * TimerTask that runs the cleanup task thread.
     */
    private static class CleanupTimerTask extends TimerTask {
        @Override
        public void run() {
            new CleanupRecoveryFilesThread().start();
        }
    }

}

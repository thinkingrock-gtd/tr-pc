/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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

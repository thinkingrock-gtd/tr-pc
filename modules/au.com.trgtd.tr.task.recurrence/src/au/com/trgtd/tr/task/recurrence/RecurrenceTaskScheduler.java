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

package au.com.trgtd.tr.task.recurrence;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduler for the recurrence task.
 *
 * @author Jeremy Moore
 */
public class RecurrenceTaskScheduler {
        
    // Singleton instance.
    private static RecurrenceTaskScheduler instance;
    
    /** Gets the singleton instance. */
    public static RecurrenceTaskScheduler instance() {
        if (instance == null) {
            instance = new RecurrenceTaskScheduler();
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
    private RecurrenceTaskScheduler() {}
    
    /** 
     * Starts the daily scheduler. Schedules the task to run daily at 00:02 AM. 
     */ 
    public void startScheduler() {
        stopScheduler();
        taskDaily = new RecurrenceTimerTask();
        timerDaily = new Timer();
        timerDaily.scheduleAtFixedRate(taskDaily, getDailyTime(), DAY);
    }
    
    /**
     * Gets the date of the first time the task is to be scheduled.
     * @return The date of tomorrow 00:02 AM.
     */
    private Date getDailyTime() {        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 2);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);                
        return calendar.getTime();
    }
    
    /** Stops the daily scheduler. */ 
    public void stopScheduler() {
        if (timerDaily != null) {
            timerDaily.cancel();            
            timerDaily = null;
        }
    }
    
    /** 
     * Run the task once only after the given delay. 
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
        taskSingle = new RecurrenceTimerTask();
        timerSingle = new Timer();
        timerSingle.schedule(taskSingle, delay);
    }
    
    /** 
     * TimerTask that runs the recurrence task thread. 
     */
    private static class RecurrenceTimerTask extends TimerTask {
        public void run() {
            new RecurrenceTaskThread().start();                
        }
    }
    
}

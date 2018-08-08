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

package au.com.trgtd.tr.email.task;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Scheduler for fetch email task.
 *
 * @author Jeremy Moore
 */
public class FetchEmailScheduler {
        
    // Singleton instance.
    private static FetchEmailScheduler instance;
    
    /** Gets the singleton instance. */
    public static FetchEmailScheduler instance() {
        if (instance == null) {
            instance = new FetchEmailScheduler();
        }
        return instance;
    }
        
    private Timer timerSingle;
    private Timer timerRepeat;

    /* Private singleton constructor. */
    private FetchEmailScheduler() {}

    /**
     * Run the task once only after the given delay.
     * @param delay The delay in milliseconds.
     */
    public void schedule(long delay) {
        cancelSingle();
        timerSingle = new Timer();
        timerSingle.schedule(new Task(), delay);
    }

    /** 
     * Starts the scheduler to run the task repeatedly after the given delay and
     * then after every time interval.
     * @param delay The delay in milliseconds.
     * @param interval The interval in milliseconds.
     */ 
    public void schedule(long delay, long interval) {
        cancelRepeat();
        timerRepeat = new Timer();
        timerRepeat.schedule(new Task(), delay, interval);
    }
    
    /** 
     * Cancel any scheduled fetch email tasks.
     */
    public void cancel() {
        cancelSingle();
        cancelRepeat();
    }

    private void cancelSingle() {
        if (timerSingle != null) {
            timerSingle.cancel();
            timerSingle = null;
        }
    }

    private void cancelRepeat() {
        if (timerRepeat != null) {
            timerRepeat.cancel();
            timerRepeat = null;
        }
    }
    
    /** 
     * TimerTask that runs the task thread. 
     */
    private static class Task extends TimerTask {
        @Override
        public void run() {
            new FetchEmailThread().start();
        }
    }
    
}

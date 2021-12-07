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

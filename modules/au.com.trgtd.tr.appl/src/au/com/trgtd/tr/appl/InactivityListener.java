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
package au.com.trgtd.tr.appl;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.Timer;

/**
 * A class that monitors inactivity in an application.
 *
 * It does this by using a Swing Timer and by listening for specified AWT
 * events. When an event is received the Timer is restarted. If no event is
 * received during the specified time interval then the timer will fire and
 * invoke the specified Action.
 *
 * When creating the listener the allowed inactivity is specified (in
 * milliseconds). However, once the listener has been created you can reset this
 * value if you need to.
 *
 * Some common event masks have be defined with the class: KEY_EVENTS - includes
 * all keyboard events MOUSE_EVENTS - includes mouse motion events
 * DEFAULT_USER_EVENTS - includes KEY_EVENTS and MOUSE_EVENT
 *
 * The allowed inactivity and event mask can be changed at any time, however,
 * they will not become effective until you stop and start the listener.
 */
public final class InactivityListener implements ActionListener, AWTEventListener {

    public final static long KEY_EVENTS = AWTEvent.KEY_EVENT_MASK;
    public final static long MOUSE_EVENTS = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
    public final static long DEFAULT_USER_EVENTS = KEY_EVENTS + MOUSE_EVENTS;
    public final static int DEFAULT_INACTIVITY_MS = 300000; // 5 minutes

    private final static Logger LOGGER = Logger.getLogger("tr.inactivity");

    private final Timer timer;
    private final Window window;
    private Action action;
    private int ms;
    private long events;

    /**
     * Use default allowed inactivity time and listen for default user events.
     *
     * @param window The main application window.
     * @param action The action to perform.
     */
    public InactivityListener(Window window, Action action) {
        this(window, action, DEFAULT_INACTIVITY_MS);
    }

    /**
     * Specify the allowed inactive time and listen for default user events.
     *
     * @param window The main application window.
     * @param action The action to perform.
     * @param ms The allowed inactivity time in milliseconds.
     */
    public InactivityListener(Window window, Action action, int ms) {
        this(window, action, ms, DEFAULT_USER_EVENTS);
    }

    /**
     * Specify the allowed inactive time and the events to listen for.
     *
     * @param window The main application window.
     * @param action The action to perform.
     * @param ms The allowed inactivity time in milliseconds.
     * @param events The event mask.
     */
    public InactivityListener(Window window, Action action, int ms, long events) {
        this.window = window;
        this.timer = new Timer(0, this);
        setAction(action);
        setInterval(ms);
        setEvents(events);
    }

    /**
     * The Action to be invoked after the specified inactivity period.
     *
     * @param action The action to perform.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * The allowed inactivity time before the Action is invoked.
     *
     * @param ms The allowed inactivity time in milliseconds.
     */
    public void setInterval(int ms) {
        this.ms = ms;
        timer.setInitialDelay(ms);
    }

    /**
     * A mask specifying the events to be passed to the AWTEventListener.
     *
     * @param eventMask The event mask.
     */
    public void setEvents(long eventMask) {
        this.events = eventMask;
    }

    /**
     * Start listening for events.
     */
    public void start() {
        timer.setInitialDelay(ms);
        timer.setRepeats(false);
        timer.start();
        Toolkit.getDefaultToolkit().addAWTEventListener(this, events);
    }

    /**
     * Stop listening for events
     */
    public void stop() {
        Toolkit.getDefaultToolkit().removeAWTEventListener(this);
        timer.stop();
    }

    // Implement ActionListener for the Timer
    @Override
    public void actionPerformed(ActionEvent e) {
        action.actionPerformed(new ActionEvent(window, ActionEvent.ACTION_PERFORMED, ""));
    }

    // Implement AWTEventListener
    @Override
    public void eventDispatched(AWTEvent e) {
        if (timer.isRunning()) {
            timer.restart();
//          LOGGER.log(Level.INFO, "{0} Restarted. User activity observed.", new Date());
        }
    }

}

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
package au.com.trgtd.tr.util;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;

/**
 * A lazy event notifier which fires one event for all events within a specified
 * time frame (if there are any).
 *
 * @author Jeremy Moore
 */
public abstract class LazyNotifier {

    /** The default delay in milliseconds. */
    public final static int DEFAULT_DELAY = 500;
    private final Timer timer;

    /** Constructs a new instance with the default delay. */
    public LazyNotifier() {
        this(DEFAULT_DELAY);
    }

    /**
     * Constructs a new instance with the given delay.
     * @param delay The delay in milliseconds.
     */
    public LazyNotifier(int delay) {
        timer = new Timer(delay, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                changed();
            }
        });
        timer.setRepeats(false);
    }

    /**
     * Fires up for an event occurrence.
     */
    public void fire() {
        timer.start();
    }

    /**
     * Override to handle the events that occur in each time frame.
     */
    public abstract void changed();
}

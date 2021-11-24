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
package au.com.trgtd.tr.swing.document;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A lazy document change listener which fires one update event after a time
 * delay, for all change events that occur within the time slot (if there are
 * any).
 *
 * @author Jeremy Moore
 */
public abstract class LazyDocumentListener implements DocumentListener {
    
    /** The default delay in milliseconds. */
    public final static int DEFAULT_DELAY = 500;
    
    private final Timer timer;
    
    /** Constructs a new instance with the default delay. */
    public LazyDocumentListener() {
        this(DEFAULT_DELAY);
    }
    
    /**
     * Constructs a new instance with the given delay.
     * @param delay The delay in milliseconds.
     */
    public LazyDocumentListener(int delay) {
        timer = new Timer(delay, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        timer.setRepeats(false);
    }
    
    /**
     * Overridden to start the timer if it is not already started.
     */
    public void insertUpdate(DocumentEvent e) {
        timer.start();
    }
    
    /**
     * Overridden to start the timer if it is not already started.
     */
    public void removeUpdate(DocumentEvent e) {
        timer.start();
    }
    
    /**
     * Overridden to start the timer if it is not already started.
     */
    public void changedUpdate(DocumentEvent e) {
        timer.start();
    }
    
    /**
     * Override to handle any insertUpdate, removeUpdate and changedUpdate
     * events.
     */
    public abstract void update();
    
}

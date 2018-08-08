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

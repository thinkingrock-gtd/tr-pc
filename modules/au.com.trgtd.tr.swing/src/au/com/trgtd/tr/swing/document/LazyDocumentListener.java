/*
 * TODO: Copyright
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

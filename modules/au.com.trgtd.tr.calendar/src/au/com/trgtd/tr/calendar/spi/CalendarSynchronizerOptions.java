/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.trgtd.tr.calendar.spi;

import javax.swing.JPanel;

/**
 * Calendar synchronizer options.
 *
 * @author Jeremy Moore
 */
public interface CalendarSynchronizerOptions {

    public void load();
    public void store();    
    public void enable(boolean b);
    public boolean valid();
    public boolean changed();
    public JPanel getPanel();
            
}

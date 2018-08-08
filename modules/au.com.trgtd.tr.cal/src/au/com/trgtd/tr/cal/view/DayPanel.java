package au.com.trgtd.tr.cal.view;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Day panel contains day time panel and day grid panel.
 * 
 * @author Jeremy Moore
 */
public class DayPanel extends JPanel {

    private final DayTimePanel dayTimePanel;
    private final DayListPanel dayListPanel;        
    private final DayGridPanel dayGridPanel;        
    
    public DayPanel(DayListPanel dayListPanel, DayTimePanel dayTimePanel, DayGridPanel dayGridPanel) {
        setOpaque(false);
        setLayout(new MigLayout("fill", "0[40!]0[grow]0", "0[grow]3[grow]0"));        
        
        this.dayListPanel = dayListPanel;
        this.dayTimePanel = dayTimePanel;
        this.dayGridPanel = dayGridPanel;
        
        add(this.dayListPanel, "skip, grow, wrap");
        add(this.dayTimePanel, "align right, growy");
        add(this.dayGridPanel, "grow");
    }    
    
    public synchronized void removeEventViewListener(PropertyChangeListener pl) {
        dayGridPanel.removePropertyChangeListener(pl);
    }

    public synchronized void addEventViewListener(PropertyChangeListener pl) {
        dayGridPanel.addPropertyChangeListener(pl);
    }
    
}

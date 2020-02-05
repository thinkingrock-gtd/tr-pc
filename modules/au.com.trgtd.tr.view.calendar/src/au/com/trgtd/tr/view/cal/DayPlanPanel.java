package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.view.DayListPanel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Day plan panel contains day time panel and day grid panel.
 * 
 * @author Jeremy Moore
 */
public class DayPlanPanel extends JPanel {

    private final DayListPanel dayListPanel;        
    
    public DayPlanPanel(DayListPanel dayListPanel) {
        this.dayListPanel = dayListPanel;
        initComponents();
    }
    
    private void initComponents() {
        setOpaque(false);
        setLayout(new MigLayout("fill", "0[40!]0[grow]0", "0[grow]3[grow]0"));        
        add(this.dayListPanel, "skip, grow, wrap");
//      add(this.dayTimePanel, "align right, growy");
//      add(this.dayGridPanel, "grow");
    }    
        
}

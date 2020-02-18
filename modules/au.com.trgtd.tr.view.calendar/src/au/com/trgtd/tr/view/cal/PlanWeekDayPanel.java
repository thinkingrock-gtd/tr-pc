package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * Week planning day panel contains actions for the given date.
 *
 * @author Jeremy Moore
 */
final class PlanWeekDayPanel extends JPanel {

    PlanWeekDayPanel(DateCtlr dateCtlr, boolean isFirst) {        
        setOpaque(false);
        setLayout(new BorderLayout());
        reset(dateCtlr, isFirst);
    }

    void reset(DateCtlr dateCtlr, boolean isFirst) {
        
    }
    
}

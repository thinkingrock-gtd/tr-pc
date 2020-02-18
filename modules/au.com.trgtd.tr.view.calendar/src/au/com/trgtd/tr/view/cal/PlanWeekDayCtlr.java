package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.view.EventLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Collections;

/**
 * Controller for day plan list panel with calendar event model.
 *
 * @author Jeremy Moore
 */
public final class PlanWeekDayCtlr {

    private final PlanWeekDayPanel dayPanel;

    private DateCtlr dateCtlr;
    private boolean isFirst;

    /**
     * Constructor.
     *
     * @param dateCtlr The date controller.
     * @param isFirst Is this the first day of the week?
     */
    public PlanWeekDayCtlr(DateCtlr dateCtlr, boolean isFirst) {
        this.isFirst = isFirst;
        this.dateCtlr = dateCtlr;
        this.dateCtlr.addPropertyChangeListener(pcl);
    }

    private final PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if (pce.getNewValue() instanceof DateCtlr) {
                reset(pce.getNewValue(), isFirst);
            }
        }
    };

    private void reset() {
        this.dateCtlr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getNewValue() instanceof DateCtlr) {
                    reset(pce.getNewValue());
                }
            }
        });
        this.dayPanel = new PlanWeekDayPanel(dateCtlr, isFirst);
    }

    private void reset() {
        dayPanel.reset((DateCtlr) pce.getNewValue(), isFirst);
    }

    public PlanWeekDayPanel getPanel() {
        return this.dayPanel;
    }

}

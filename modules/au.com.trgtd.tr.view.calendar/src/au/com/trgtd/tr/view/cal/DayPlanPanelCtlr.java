package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.view.DayListPanel;
import au.com.trgtd.tr.view.cal.DayPlanPanel;

/**
 * Controller for a day plan panel with calendar event model. 
 */
public final class DayPlanPanelCtlr {

    private final DayPlanListCtlr dayPlanListCtlr;    
    private final DayPlanPanel dayPanel;
    private final DayListPanel dayListPanel;
    
    /**
     * Constructor.
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     * @param isFirstDayOfWeek Is it the first day of the week?
     */
    public DayPlanPanelCtlr(CalModel calModel, DateCtlr dateCtlr, boolean isFirstDayOfWeek) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }                
        this.dayPlanListCtlr = new DayPlanListCtlr(calModel, dateCtlr, isFirstDayOfWeek);        
        this.dayListPanel = dayPlanListCtlr.getPanel();
        this.dayPanel = new DayPlanPanel(dayListPanel);        
    }

    public DayPlanPanel getDayPlanPanel() {
        return dayPanel;
    }
        
}

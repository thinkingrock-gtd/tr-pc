package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.view.DayListPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controller for week panel and calendar model. Updates the contained day grid
 * panels when the date controller date is changed.
 *
 * @author Jeremy Moore
 */
public final class WeekPlanPanelCtlr {

    private final CalModel calModel;
    private final DateCtlr dateCtlr;
    private final DateCtlr[] dateCtlrs;
    private final DayPlanListCtlr[] dayPlanListCtlrs;
    private final DayListPanel[] dayListPanels;
    private final WeekPlanPanel weekPlanPanel;

    /**
     * Constructor.
     *
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     */
    public WeekPlanPanelCtlr(CalModel calModel, DateCtlr dateCtlr) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
        this.dateCtlrs = new DateCtlr[7];
        this.dayListPanels = new DayListPanel[7];
        this.dayPlanListCtlrs = new DayPlanListCtlr[7];
        Day dayFirst = dateCtlr.getWeekStart();
        Day dayLast = dateCtlr.getWeekEnd();
        int index = 0;
        for (Day dd = dayFirst; !dd.after(dayLast); dd = dd.next()) {
            boolean isFirstDayOfWeek = index == 0;
            dateCtlrs[index] = new DateCtlr(dd.getDate());
            dayPlanListCtlrs[index] = new DayPlanListCtlr(calModel, dateCtlrs[index], isFirstDayOfWeek);
            dayListPanels[index] = dayPlanListCtlrs[index].getPanel();
            index++;
        }
        this.weekPlanPanel = new WeekPlanPanel(dayListPanels);
        this.dateCtlr.addPropertyChangeListener(pclDate);

        this.initDates(dayFirst, dayLast);
    }

    /**
     * Gets the week plan panel;
     *
     * @return the panel.
     */
    public WeekPlanPanel getWeekPanel() {
        return weekPlanPanel;
    }

    /**
     * Adds a new event to the calendar model.
     *
     * @param event The new event.
     */
    public void add(CalEvent event) {
        if (null != event) {
            calModel.add(event);
        }
    }

    /**
     * Removes an existing event from the calendar model.
     *
     * @param event The event to remove.
     */
    public void remove(CalEvent event) {
        if (null != event) {
            calModel.remove(event);
        }
    }

    private final PropertyChangeListener pclDate = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            // see if already showing correct week days for the date            
            final Day start = dateCtlr.getWeekStart();
            final Day end = dateCtlr.getWeekStart();
            if (DateUtils.isSameDay(start.getDate(), dateCtlrs[0].getDate())) {
                fireDateChanges();
            } else {
                initDates(start, end);
            }
        }
    };

    private void initDates(Day start, Day end) {
        int index = 0;
        for (Day day = start; !day.after(end); day = day.next()) {
            dateCtlrs[index++].setDate(day.getDate());
        }
        weekPlanPanel.updateDayHeadings(start, end);
    }

    // Fire date change events for each day, to force grids to update for changed calendar events.
    private void fireDateChanges() {
        for (DateCtlr dc : dateCtlrs) {
            dc.fireChange();
        }
    }
}

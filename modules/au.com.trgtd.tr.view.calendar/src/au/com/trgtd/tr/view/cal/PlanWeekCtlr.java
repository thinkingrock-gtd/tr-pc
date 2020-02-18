package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Controller for plan week panel and calendar model.
 *
 * @author Jeremy Moore
 */
final class PlanWeekCtlr {

    private final DateCtlr dateCtlr;
    private final DateCtlr[] dateCtlrs;
    private final PlanWeekDayCtlr[] dayCtlrs;
    private final PlanWeekDayPanel[] dayPanels;
    private final PlanWeekPanel weekPanel;

    /**
     * Constructor.
     *
     * @param dateCtlr The date controller.
     */
    public PlanWeekCtlr(DateCtlr dateCtlr) {
        this.dateCtlr = dateCtlr;
        this.dateCtlrs = new DateCtlr[7];
        this.dayPanels = new PlanWeekDayPanel[7];
        this.dayCtlrs = new PlanWeekDayCtlr[7];
        Day dayFirst = dateCtlr.getWeekStart();
        Day dayLast = dateCtlr.getWeekEnd();
        int index = 0;
        for (Day dd = dayFirst; !dd.after(dayLast); dd = dd.next()) {
            dateCtlrs[index] = new DateCtlr(dd.getDate());
            dayCtlrs[index] = new PlanWeekDayCtlr(dateCtlrs[index], index == 0);
            dayPanels[index] = dayCtlrs[index].getPanel();
            index++;
        }
        this.weekPanel = new PlanWeekPanel(dayPanels);
        this.dateCtlr.addPropertyChangeListener(pclDate);

        this.initDates(dayFirst, dayLast);
    }

    /**
     * Gets the week plan panel;
     *
     * @return the panel.
     */
    public PlanWeekPanel getWeekPanel() {
        return weekPanel;
    }

    private final PropertyChangeListener pclDate = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            // see if already showing correct week days for the date            
            final Day start = dateCtlr.getWeekStart();
            final Day end = dateCtlr.getWeekEnd();
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
        weekPanel.updateDayHeadings(start, end);
    }

    // Fire date change events for each day, to force grids to update for changed calendar events.
    private void fireDateChanges() {
        for (DateCtlr dc : dateCtlrs) {
            dc.fireChange();
        }
    }
}

/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.cal.ctlr;

import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.view.DayGridPanel;
import au.com.trgtd.tr.cal.view.AllDayPanel;
import au.com.trgtd.tr.cal.view.DayTimePanel;
import au.com.trgtd.tr.cal.view.WeekPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * Controller for week panel and calendar model. Updates the contained day grid
 * panels when the date controller date is changed.
 * 
 * @author Jeremy Moore
 */
public final class WeekPanelCtlr {
  
    private final CalModel calModel;
    private final DateCtlr dateCtlr;    
    private final DayTimePanel dayTimePanel;
    private final AllDayPanel[] dayListPanels;
    private final DayListCtlr[] dayListCtlrs;    
    private final DayGridPanel[] dayGridPanels;
    private final DayGridCtlr[] dayGridCtlrs;    
    private final DateCtlr[] dateCtlrs;
    private final WeekPanel weekPanel;
    
    /**
     * Constructor.
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     * @param firstHr
     * @param lastHr
     */
    public WeekPanelCtlr(CalModel calModel, DateCtlr dateCtlr, int firstHr, int lastHr) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }       
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;        
        this.dayTimePanel = new DayTimePanel(firstHr, lastHr);
        this.dateCtlrs = new DateCtlr[7];
        this.dayListPanels = new AllDayPanel[7];
        this.dayListCtlrs = new DayListCtlr[7];        
        this.dayGridPanels = new DayGridPanel[7];
        this.dayGridCtlrs = new DayGridCtlr[7];        
        Day dayFirst = dateCtlr.getWeekStart();
        Day dayLast = dateCtlr.getWeekEnd();        
        int index = 0;
        for (Day dd = dayFirst; !dd.after(dayLast); dd = dd.next()) {
            dateCtlrs[index] = new DateCtlr(dd.getDate());
            dayListCtlrs[index] = new DayListCtlr(calModel, dateCtlrs[index]);            
            dayListPanels[index] = dayListCtlrs[index].getPanel();
            dayGridCtlrs[index] = new DayGridCtlr(calModel, dateCtlrs[index], firstHr, lastHr);            
            dayGridPanels[index] = dayGridCtlrs[index].getPanel();
            index++;            
        }
        this.weekPanel = new WeekPanel(dayTimePanel, dayGridPanels, dayListPanels);        
        this.dateCtlr.addPropertyChangeListener(pclDate);        
        this.initDates();
    }

    /**
     * Gets the week panel;
     * @return the panel.
     */
    public WeekPanel getWeekPanel() {
        return weekPanel;
    }
    
    /**
     * Adds a new event to the calendar model.
     * @param event The new event.
     */
    public void add(CalEvent event) {
        if (null != event) {
            calModel.add(event);
        }
    }

    /**
     * Removes an existing event from the calendar model.
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
            final Date weekStartDate = dateCtlr.getWeekStart().getDate();
            if (DateUtils.isSameDay(weekStartDate, dateCtlrs[0].getDate())) {
                fireDateChanges();
            } else {
                initDates();                
            }
        }        
    };
    
    private void initDates() {
        final Day start = dateCtlr.getWeekStart();
        final Day end = dateCtlr.getWeekEnd();        
        int index = 0;
        for (Day day = start; !day.after(end); day = day.next()) {
            dateCtlrs[index++].setDate(day.getDate());
        }
        weekPanel.updateDayHeadings(start, end); 
    }

    // Fire date change events for each day to force grids to update for changed 
    // calendar events.
    private void fireDateChanges() {
        for (DateCtlr dc : dateCtlrs) {
            dc.fireChange();
        }
    }
}

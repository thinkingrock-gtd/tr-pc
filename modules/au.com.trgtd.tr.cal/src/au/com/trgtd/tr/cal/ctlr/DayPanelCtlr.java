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

import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.view.DayGridPanel;
import au.com.trgtd.tr.cal.view.AllDayPanel;
import au.com.trgtd.tr.cal.view.DayTimePanel;
import au.com.trgtd.tr.cal.view.DayPanel;

/**
 * Controller for a day panel with calendar event model. 
 * Creates and provides a DayGridCtlr and DayPanel.
 * 
 * @author Jeremy Moore
 */
public final class DayPanelCtlr {

    private final DayGridCtlr dayGridCtlr;
    private final DayListCtlr dayListCtlr;
    
    private final AllDayPanel dayListPanel;
    private final DayTimePanel dayTimePanel;
    private final DayGridPanel dayGridPanel;
    
    private final DayPanel dayPanel;
    
    /**
     * Constructor.
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     * @param firstHr The first hour of the day.
     * @param lastHr The last hour of the day.
     */
    public DayPanelCtlr(CalModel calModel, DateCtlr dateCtlr, int firstHr, int lastHr) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }        
        
        this.dayTimePanel = new DayTimePanel(firstHr, lastHr);
        this.dayGridCtlr = new DayGridCtlr(calModel, dateCtlr, firstHr, lastHr);
        this.dayGridPanel = dayGridCtlr.getPanel();
        this.dayListCtlr = new DayListCtlr(calModel, dateCtlr);        
        this.dayListPanel = dayListCtlr.getPanel();
        this.dayPanel = new DayPanel(dayListPanel, dayTimePanel, dayGridPanel);        
    }

    public DayPanel getPanel() {
        return dayPanel;
    }

    public DayGridCtlr getDayGridCtlr() {
        return dayGridCtlr;
    }
        
}

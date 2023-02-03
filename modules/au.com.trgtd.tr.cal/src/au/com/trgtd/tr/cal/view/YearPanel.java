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
package au.com.trgtd.tr.cal.view;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Year panel.
 */
public class YearPanel extends JPanel {

    private final static int HGAP = 9;
    private final static int VGAP = 9;
    private final static int ROWS = 3;
    private final static int COLS = 4;
    private final DateCtlr dateCtlr;
    private final CalModel calModel;
    private final MonthViewer monthViewer;
    private final WeekViewer weekViewer;
    private final Color bg;
    private JPanel monthsPanel;
    private Map<Date, List<CalEvent>> eventsMap;
    

    public YearPanel(DateCtlr dateCtlr, CalModel calModel, MonthViewer monthViewer, WeekViewer weekViewer, Color bg) {
        this.monthViewer = monthViewer;
        this.weekViewer = weekViewer;
        this.calModel = calModel;
        this.bg = bg;
        this.dateCtlr = dateCtlr;
        this.monthsPanel = makeMonthsPanel();
        this.dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            initGrid();
        });
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(monthsPanel, BorderLayout.CENTER);
        initGrid();
    }

    private JPanel makeMonthsPanel() {
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, HGAP, VGAP));
        panel.setBorder(BorderFactory.createEmptyBorder(9, 9, 8, 8));        
        panel.setOpaque(true);
        panel.setBackground(bg);
        return panel;
    }
    
    private void initGrid() {
        eventsMap = calModel.getEventsMap();

        monthsPanel.removeAll();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCtlr.getDate());

        for (int month = 0; month < 12; month++) {
            cal.set(Calendar.MONTH, month);
            monthsPanel.add(new YearMonthPanel(cal.getTime(), eventsMap, monthViewer, weekViewer));
        }
        
        this.removeAll();
        this.add(monthsPanel, BorderLayout.CENTER);
    }

}

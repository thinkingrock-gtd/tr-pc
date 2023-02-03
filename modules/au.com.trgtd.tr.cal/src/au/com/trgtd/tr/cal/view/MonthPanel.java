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
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Month grid panel.
 */
public class MonthPanel extends JPanel {

    private interface Colors {

        final Color AZURE = new Color(241, 255, 255);
        final Color TODAYS_BG = AZURE;
        final Color NORMAL_BG = Color.WHITE;
        final Color NORMAL_FG = Color.LIGHT_GRAY.darker();
        final Color TODAYS_FG = Color.BLUE;
    }

    public interface Fonts {

        final Font NORMAL = UIManager.getFont("Label.font");
        final Font DATE = NORMAL.deriveFont(10.0f);
        final Font TEXT = NORMAL.deriveFont(10.0f);
        final Font MORE = NORMAL.deriveFont(10.0f).deriveFont(Font.ITALIC);
    }
    private final static DateFormat DF0 = new SimpleDateFormat("EEE d");
    private final static DateFormat DF1 = new SimpleDateFormat("d");
    private final static int HGAP = 0;
    private final static int VGAP = 0;
    private final static int COLS = 7;
    private final DateCtlr dateCtlr;
    private final CalModel calModel;
    private final DayViewer dayViewer;
    private JPanel gridPanel;
    private int gridRows;
    private Map<Date, List<CalEvent>> eventsMap;

    public MonthPanel(DateCtlr dateCtlr, CalModel calModel, DayViewer dayViewer) {
        this.setLayout(new BorderLayout());
        this.dayViewer = dayViewer;
        this.calModel = calModel;
        this.dateCtlr = dateCtlr;
        this.dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            initGridDates();
        });
        initGridDates();
    }

    private void initGridDates() {
        eventsMap = calModel.getEventsMap();
        
        final int weekStartDayNbr = dateCtlr.getFirstDayOfWeek();
        final Day monthStartDay = dateCtlr.getMonthStart();

        final int monthStartDayNbr = getDayOfWeekNbr(monthStartDay);
        final int nbrDaysOffset = getNbrDaysOffset(monthStartDayNbr, weekStartDayNbr);
        final Day gridStartDay = monthStartDay.addDays(-nbrDaysOffset);

        gridRows = getNbrRowsRequired(dateCtlr.getDate(), nbrDaysOffset);
        gridPanel = new JPanel(new GridLayout(gridRows, COLS, HGAP, VGAP));

        removeAll();
        add(gridPanel, BorderLayout.CENTER);

        Day day = gridStartDay;

        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < COLS; col++) {
                gridPanel.add(makeDayPanel(day.getDate(), row, col));
                day = day.next();
            }
        }
    }

    private int getNbrDaysOffset(int monthStartDayNbr, int weekStartDayNbr) {
        int nbrDaysOffset = monthStartDayNbr - weekStartDayNbr;
        return nbrDaysOffset < 0 ? 6 : nbrDaysOffset;
        // note: less than 0 when week start is Monday and month start is Sunday.
    }

    private int getNbrRowsRequired(Date date, int nbrDaysOffset) {
        double nbrDays = nbrDaysOffset + getDaysInMonth(date);
        return (int) Math.ceil(nbrDays / 7);
    }

    private int getDaysInMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int getDayOfWeekNbr(Day day) {
        Calendar c = Calendar.getInstance();
        c.setTime(day.getDate());
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private Border getDayBorder(int row, int col) {
        int t = row == 0 ? 1 : 0; // top only if first row
        int l = col == 0 ? 1 : 0; // left only if first column
        int b = 1;
        int r = 1;
        return BorderFactory.createMatteBorder(t, l, b, r, Color.LIGHT_GRAY);
    }

    private String formatDate(Date date, int row) {
        return row == 0 ? DF0.format(date) : DF1.format(date);
    }

    private JPanel makeDayPanel(Date date, int row, int col) {
        JPanel dayPanel = new JPanel(new BorderLayout());
        dayPanel.setBorder(getDayBorder(row, col));
        dayPanel.setOpaque(true);
        if (DateUtils.isToday(date)) {
            dayPanel.setBackground(Colors.TODAYS_BG);
        } else {
            dayPanel.setBackground(Colors.NORMAL_BG);
        }
        dayPanel.add(makeDatePanel(date, row), BorderLayout.NORTH);
        dayPanel.add(new MonthDayPanel(eventsMap.get(date)), BorderLayout.CENTER);
        return dayPanel;
    }

    private JPanel makeDatePanel(final Date date, final int row) {
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.setOpaque(false);
        datePanel.add(makeDateLabel(date, row));
        return datePanel;
    }

    private JLabel makeDateLabel(final Date date, final int row) {
        JLabel dateLabel = new JLabel(formatDate(date, row));
        dateLabel.setOpaque(false);
        dateLabel.setFont(Fonts.DATE);
        dateLabel.setForeground(DateUtils.isToday(date) ? Colors.TODAYS_FG : Colors.NORMAL_FG);
        dateLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    dayViewer.showDayView(date);
                }
            }
        });
        return dateLabel;
    }
}

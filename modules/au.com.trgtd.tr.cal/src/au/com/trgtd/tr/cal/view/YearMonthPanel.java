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

import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Year month panel.
 */
public class YearMonthPanel extends JPanel {

    public interface Fonts {
        final Font NORMAL = UIManager.getFont("Label.font");
        final Font DATE = NORMAL.deriveFont(10.0f);
        final Font TEXT = NORMAL.deriveFont(10.0f);
        final Font MORE = NORMAL.deriveFont(10.0f).deriveFont(Font.ITALIC);
        final Font MONTH = NORMAL.deriveFont(12.0f).deriveFont(Font.BOLD);
    }
    private final static DateFormat DF_MONTH_FULL = new SimpleDateFormat("MMMM");
    private final static DateFormat DF_DAY_MED = new SimpleDateFormat("EEE");
    
    private final static int HGAP = 0;
    private final static int VGAP = 0;
    private final static int COLS = 7;
    private final static int ROWS = 7;

    private final Date date;
    private Map<Date, List<CalEvent>> eventsMap;

    private final MonthViewer monthViewer;
    private final WeekViewer weekViewer;


    private JPanel gridPanel;

    public YearMonthPanel(Date date, Map<Date, List<CalEvent>> eventsMap, MonthViewer monthViewer, WeekViewer weekViewer) {
        this.monthViewer = monthViewer;
        this.weekViewer = weekViewer;
        this.eventsMap = eventsMap;
        this.date = date;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        initGridDates();
    }

    private void initGridDates() {
        final int weekStartDayNbr = DatesPrefs.getFirstDayOfWeek();
        final Day monthStartDay = getMonthStartDay();
        final Day monthEndDay = getMonthEndDay();
        final int monthStartDayNbr = getDayOfWeekNbr(monthStartDay);
        final int nbrDaysOffset = getNbrDaysOffset(monthStartDayNbr, weekStartDayNbr);
        final Day gridStartDay = monthStartDay.addDays(-nbrDaysOffset);

        JPanel dayTitlesPanel = new JPanel(new GridLayout(1, COLS, HGAP, VGAP));
        dayTitlesPanel.setOpaque(true);
        dayTitlesPanel.setBackground(YearDayPanel.Colors.BG_MONTH_TITLES);
        dayTitlesPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    monthViewer.showMonthView(date);
                }
            }
        });
        
        Day day = gridStartDay;
        for (int col = 0; col < COLS; col++) {
            dayTitlesPanel.add(makeDayTitle(day.getDate()));
            day = day.next();
        }

        gridPanel = new JPanel(new GridLayout(ROWS-1, COLS, HGAP, VGAP));
        gridPanel.setBorder(BorderFactory.createEmptyBorder());
       
        day = gridStartDay;
        for (int row = 0; row < ROWS-1; row++) {
            for (int col = 0; col < COLS; col++) {
                if (day.before(monthStartDay) || day.after(monthEndDay)) {
                    gridPanel.add(makeBlankDayPanel(row, col));
                } else {
                    gridPanel.add(makeDayPanel(day.getDate(), row, col));
                }
                day = day.next();
            }
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(dayTitlesPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        removeAll();
        setOpaque(true);
        setBackground(YearDayPanel.Colors.BG_MONTH_TITLES);
        add(makeMonthTitle(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

    }

    private Day getMonthStartDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Day(c.getTime());
    }

    private Day getMonthEndDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Day(c.getTime());
    }

    private int getNbrDaysOffset(int monthStartDayNbr, int weekStartDayNbr) {
        int nbrDaysOffset = monthStartDayNbr - weekStartDayNbr;
        return nbrDaysOffset < 0 ? 6 : nbrDaysOffset;
        // note: less than 0 when week start is Monday and month start is Sunday.
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

    private JPanel makeMonthTitle() {
        JLabel label = new JLabel(" " + DF_MONTH_FULL.format(date));
        label.setOpaque(false);
        label.setForeground(Color.GRAY);
        label.setFont(Fonts.MONTH);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder());
        panel.add(label);
        panel.setOpaque(false);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    monthViewer.showMonthView(date);
                }
            }
        });
        return panel;
    }

    private JLabel makeDayTitle(Date date) {
        JLabel label = new JLabel(DF_DAY_MED.format(date), JLabel.CENTER);
        label.setOpaque(false);
        label.setForeground(Color.GRAY);
        label.setFont(Fonts.DATE);
        return label;
    }

    private JPanel makeDayPanel(final Date date, int row, int col) {
        YearDayPanel panel = new YearDayPanel(eventsMap, date);
        panel.setBorder(getDayBorder(row, col));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    weekViewer.showWeekView(date);
                }
            }
        });
        return panel;
    }

    private JPanel makeBlankDayPanel(int row, int col) {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(YearDayPanel.Colors.BG_NORMAL);
        panel.setBorder(getDayBorder(row, col));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    monthViewer.showMonthView(date);
                }
            }
        });
        return panel;
    }

}

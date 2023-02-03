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

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.PreferenceChangeEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import java.text.DateFormatSymbols;

/**
 * Day of the month selection panel.
 *
 * @author Jeremy Moore
 */
public class DayOfMonthChooserPanel extends JPanel {

    private interface Fonts {
        final Font NORMAL = UIManager.getFont("Label.font");
        final Font DAY_COL_TITLE = NORMAL.deriveFont(10.0f).deriveFont(Font.BOLD);
        final Font DAY_NBR_NORMAL = NORMAL.deriveFont(12.0f).deriveFont(Font.PLAIN);
        final Font DAY_NBR_SELECT = NORMAL.deriveFont(12.0f).deriveFont(Font.BOLD);
        final Font DAY_NBR_TODAY = NORMAL.deriveFont(12.0f).deriveFont(Font.PLAIN);
    }

    private interface Colors {
        final Color DAY_COL_TITLE = Color.GRAY;
        final Color DAY_NBR_NORMAL = Color.GRAY;
        final Color DAY_NBR_SELECT = Color.DARK_GRAY;
        final Color DAY_NBR_TODAY = Color.BLUE;
        final Color H = UIManager.getColor("controlHighlight");
        final Color DAY_HIGHLIGHT = new Color(H.getRed(), H.getGreen(), H.getBlue(), 60);
    }

    private final static int GRID_ROWS = 6;
    private final static int GRID_COLS = 7;
    private final JPanel gridPanel = new JPanel(); // grid panel
    private final JPanel ttlsPanel = new JPanel(); // grid day titles panel
    private final JPanel nbrsPanel = new JPanel(); // grid day numbers panel
    private final DayNbrPanel[][] dayPanels = new DayNbrPanel[6][7];
    private final DateCtlr dateCtlr;
    private DayNbrPanel selectedDay;

    public DayOfMonthChooserPanel(DateCtlr dateCtlr) {
        this.dateCtlr = dateCtlr;

        initGridPanel();
        initGridDays();
        initThisPanel();

        dateCtlr.setDate(new Date());
        dateChanged(null, dateCtlr.getDate());


        this.dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            dateChanged((Date) pce.getOldValue(), (Date) pce.getNewValue());
        });

        DatesPrefs.getPrefs().addPreferenceChangeListener((PreferenceChangeEvent pce) -> {
            initGridPanel();
            initGridDays();
        });
    }

    private void dateChanged(Date oldDate, Date newDate) {
        if (newDate == null) {
            return;
        }

        Calendar newCal = Calendar.getInstance();
        newCal.setTime(newDate);

        if (oldDate == null) {
            initGridDays();
            select(getDayPanel(newCal.get(Calendar.DAY_OF_MONTH)));
            return;
        }

        Calendar oldCal = Calendar.getInstance();
        oldCal.setTime(oldDate);

        if (newCal.get(Calendar.YEAR) != oldCal.get(Calendar.YEAR)
                || newCal.get(Calendar.MONTH) != oldCal.get(Calendar.MONTH)
                || newCal.get(Calendar.DAY_OF_YEAR) != oldCal.get(Calendar.DAY_OF_YEAR)) {
            initGridDays();
            select(getDayPanel(newCal.get(Calendar.DAY_OF_MONTH)));
        }
    }

    private DayNbrPanel getDayPanel(int dayOfMonth) {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                DayNbrPanel dp = dayPanels[row][col];
                if (dp.getDayOfMonth() == dayOfMonth) {
                    return dp;
                }
            }
        }
        return null;
    }

    private void initThisPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(210, 140));
        setMinimumSize(new Dimension(210, 140));
        setMaximumSize(new Dimension(210, 140));
        add(gridPanel, BorderLayout.CENTER);
    }

    private String[] getDayTitles() {
        String[] symbols = DateFormatSymbols.getInstance().getShortWeekdays();
        String[] daysSunSat = new String[7];
        for (int i = 0; i < 7; i++) {
            daysSunSat[i] = symbols[i+1];
        }
        String[] daysMonSun = new String[7];
        for (int i = 0; i < 7; i++) {
            daysMonSun[i] = daysSunSat[(i+1)%7];
        }

        printDayTitles("Symbols", symbols);
        printDayTitles("Sun2Sat", daysSunSat);
        printDayTitles("Mon2Sun", daysMonSun);

        if (dateCtlr.getFirstDayOfWeek() == Calendar.SUNDAY) {
            return daysSunSat;
        } else {
            return daysMonSun;
        }
    }


    private void printDayTitles(String label, String[] days) {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append("|");
        for (String day : days) {
            sb.append(day).append("|");
        }
    }

    private void initGridPanel() {
        ttlsPanel.removeAll();
        nbrsPanel.removeAll();
        gridPanel.removeAll();

        String[] dayTitles = getDayTitles();

        ttlsPanel.setLayout(new GridLayout(1, GRID_COLS));
        ttlsPanel.setOpaque(false);

        for (int col = 0; col < GRID_COLS; col++) {
            JLabel label = new JLabel(dayTitles[col]);
            label.setFont(Fonts.DAY_COL_TITLE);
            label.setForeground(Colors.DAY_COL_TITLE);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            ttlsPanel.add(label);
        }

        nbrsPanel.setLayout(new GridLayout(GRID_ROWS, GRID_COLS));
        nbrsPanel.setOpaque(false);

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                dayPanels[row][col] = new DayNbrPanel();
                dayPanels[row][col].addMouseListener(new RolloverListener());
                nbrsPanel.add(dayPanels[row][col]);
            }
        }

        gridPanel.setLayout(new BorderLayout());
        gridPanel.setOpaque(false);
        gridPanel.add(ttlsPanel, BorderLayout.NORTH);
        gridPanel.add(nbrsPanel, BorderLayout.CENTER);
    }

    private void initGridDays() {
        // clear all grid day numbers
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                dayPanels[row][col].setDayOfMonth(0);
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCtlr.getDate());
        cal.setFirstDayOfWeek(dateCtlr.getFirstDayOfWeek());

        // get last day number of current month: [28|29|30|31]
        int nMonthLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // get first day of week shown in first grid column: [1=Sun|2=Mon]
        int dGridFirstDay = cal.getFirstDayOfWeek();

        // get offset for grid start day: [0=grid starts Sun|1=grid start Mon].
        int dGridDayOffset = dGridFirstDay - 1;

        // get first day of current month: [1=Sun|2=Mon|...|7=Sat]. 
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int dMonthFirstDay = cal.get(Calendar.DAY_OF_WEEK);

        // get offset for grid start day: [0=grid starts Sun|1=grid start Mon].
        int dMonthDayOffset = (dMonthFirstDay < dGridFirstDay) ? dMonthFirstDay + 6 : dMonthFirstDay - 1;

        // set grid day numbers
        for (int nMonthDay = 1; nMonthDay <= nMonthLastDay; nMonthDay++) {

            // adjust index for grid first day and month first day 
            int nGridIndex = nMonthDay - dGridDayOffset + dMonthDayOffset - 1;

            // convert to row and col
            int col = nGridIndex % (GRID_COLS);

            int row = nGridIndex / (GRID_COLS);

            // set grid cell day number
            dayPanels[row][col].setDayOfMonth(nMonthDay);
        }
    }

    private void select(DayNbrPanel dayPanel) {
        if (null != selectedDay) {
            selectedDay.setSelected(false);
        }
        selectedDay = dayPanel;
        selectedDay.setSelected(true);
    }

    // Inner class to respond to mouse events for the "rollover" effect and 
    // day selection.
    private class RolloverListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            DayNbrPanel dayPanel = (DayNbrPanel) e.getComponent();
            dayPanel.setHighlight(true);
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            DayNbrPanel dayPanel = (DayNbrPanel) e.getComponent();
            dayPanel.setHighlight(false);
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            DayNbrPanel dayPanel = (DayNbrPanel) e.getComponent();
            dateCtlr.setDayOfMonth(dayPanel.getDayOfMonth());
        }
    }

    // Inner class for day number component.
    private class DayNbrPanel extends JPanel {

        private final static int ARC = 6;
        private final JLabel label = new JLabel(" ");
        private boolean highlight = false;
        private int nbr = 0;

        public DayNbrPanel() {
            super();
            initPanel();
        }

        public void setSelected(boolean selected) {
            if (selected) {
                label.setFont(Fonts.DAY_NBR_SELECT);
                if (isToday()) {
                    label.setForeground(Colors.DAY_NBR_TODAY);
                } else {
                    label.setForeground(Colors.DAY_NBR_SELECT);
                }
            } else { // not selected
                if (isToday()) {
                    label.setFont(Fonts.DAY_NBR_TODAY);
                    label.setForeground(Colors.DAY_NBR_TODAY);
                } else {
                    label.setFont(Fonts.DAY_NBR_NORMAL);
                    label.setForeground(Colors.DAY_NBR_NORMAL);
                }
            }
        }

        public void setHighlight(boolean b) {
            this.highlight = b;
        }

        public void setDayOfMonth(int n) {
            this.nbr = (n < 0 || n > 31) ? 0 : n;
            if (n == 0) {
                label.setText(" ");
            } else if (n < 10) {
                label.setText(" " + n);
            } else {
                label.setText("" + n);
            }
            setSelected(false);
        }

        public int getDayOfMonth() {
            return nbr;
        }

        private boolean isToday() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateCtlr.getDate());
            calendar.set(Calendar.DAY_OF_MONTH, nbr);
            return DateUtils.isToday(calendar);
        }

        private void initPanel() {
            setLayout(new BorderLayout());
            setFocusable(false);
            setOpaque(false);
            label.setOpaque(false);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            setSelected(false);
            add(label, BorderLayout.CENTER);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (nbr == 0 || !highlight) {
                return;
            }
            final int x = 0, y = 0;
            final int w = getWidth();
            final int h = getHeight();
            final Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Colors.DAY_HIGHLIGHT);
            g2.fillRoundRect(x + 1, y + 1, w - 2, h - 2, ARC, ARC);
            g2.dispose();
        }
    }
}

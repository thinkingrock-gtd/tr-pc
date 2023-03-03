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
package au.com.trgtd.tr.swing.date.chooser;

import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import org.openide.util.NbBundle;

/**
 * Custom dialog box to enter dates. The <code>DateChooser</code> class
 * presents a calendar and allows the user to visually select a day, month and
 * year so that it is impossible to enter an invalid date.
 */
public class DateChooserDialog extends JDialog implements ItemListener, MouseListener, KeyListener, ActionListener {

    private static final Class clazz = DateChooserDialog.class;
    private static final String JANUARY = NbBundle.getMessage(clazz, "january"); 
    private static final String FEBRUARY = NbBundle.getMessage(clazz, "february"); 
    private static final String MARCH = NbBundle.getMessage(clazz, "march"); 
    private static final String APRIL = NbBundle.getMessage(clazz, "april"); 
    private static final String MAY = NbBundle.getMessage(clazz, "may"); 
    private static final String JUNE = NbBundle.getMessage(clazz, "june"); 
    private static final String JULY = NbBundle.getMessage(clazz, "july"); 
    private static final String AUGUST = NbBundle.getMessage(clazz, "august"); 
    private static final String SEPTEMBER = NbBundle.getMessage(clazz, "september"); 
    private static final String OCTOBER = NbBundle.getMessage(clazz, "october"); 
    private static final String NOVEMBER = NbBundle.getMessage(clazz, "november"); 
    private static final String DECEMBER = NbBundle.getMessage(clazz, "december"); 
    private static final String MON = NbBundle.getMessage(clazz, "mon"); 
    private static final String TUE = NbBundle.getMessage(clazz, "tue"); 
    private static final String WED = NbBundle.getMessage(clazz, "wed"); 
    private static final String THU = NbBundle.getMessage(clazz, "thu"); 
    private static final String FRI = NbBundle.getMessage(clazz, "fri"); 
    private static final String SAT = NbBundle.getMessage(clazz, "sat"); 
    private static final String SUN = NbBundle.getMessage(clazz, "sun"); 
    private static final String DEFAULT_TITLE = NbBundle.getMessage(clazz, "date.chooser");
    private static final DateFormat DF = new SimpleDateFormat("EEE, d MMM yyyy");
    /** Names of the months. */
    private static final String[] MONTHS = new String[]{
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    };
    /* Month index for January. */
    private static final int MONTH_INDEX_JANUARY = 0;
    /* Month index for December. */
    private static final int MONTH_INDEX_DECEMBER = 11;
    private static final ImageIcon ICON_BACK = new ImageIcon(clazz.getResource("/au/com/trgtd/tr/swing/date/chooser/Back16.gif"));
    private static final ImageIcon ICON_NEXT = new ImageIcon(clazz.getResource("/au/com/trgtd/tr/swing/date/chooser/Next16.gif"));
    /* First day of the week. */
    private int FIRST_DAY;
    /* Names of the days of the week. */
    private String[] DAYS;
    /* Names of the days of the week when first day is Sunday. */
    private static final String[] DAYS_SUN_FIRST = new String[]{SUN, MON, TUE, WED, THU, FRI, SAT, SUN};
    /* Names of the days of the week when first day is Monday. */
    private static final String[] DAYS_MON_FIRST = new String[]{MON, TUE, WED, THU, FRI, SAT, SUN, MON};
    /* Text color of the selected day in the calendar. */
    private static final Color GRID_BACKGROUND = Color.lightGray;
    /* Empty border, used when the calendar does not have the focus. */
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    /* First year that can be selected. */
    private static final int FIRST_YEAR = 1900;
    /* Last year that can be selected. */
    private static final int LAST_YEAR = 2100;
    /* Auxiliary variable to compute dates. */
    private GregorianCalendar calendar;
    /* Editor pane for today hyperlink. */
    private JEditorPane todayPane;
    /* Calendar, as a matrix of labels. The first row represents the first week
     * of the month, the second row, the second week, and so on. Each column
     * represents a day of the week, the first is Sunday, and the last is
     * Saturday. The label's text is the number of the corresponding day. */
    private DayLabel[][] days;
    /* Day selection control. It is just a panel that can receive the focus. The
     * actual user interaction is driven by the <code>DateChooser</code>
     * class. */
    private FocusablePanel daysGrid;
    /* Headings grid. */
    private JPanel headGrid;
    /* Month selection control. */
    private JComboBox<String> month;
    /* Last month button. */
    private JButton monthLast;
    /* Next month button. */
    private JButton monthNext;
    /* Last year button. */
    private JButton yearLast;
    /* Next year button. */
    private JButton yearNext;
    /* Year selection control. */
    private JComboBox<String> year;
    /* "Clear" button. */
    private JButton clear;
    /* "Ok" button. */
    private JButton okButton;
    /* "Cancel" button. */
    private JButton cancelButton;
    /* Day of the week (0=Sunday) corresponding to the first day of the selected
     * month. Used to calculate the position, in the calendar ({@link #days}),
     * corresponding to a given day. */
    private int offset;
    /* Last day of the selected month. */
    private int lastDay;
    /* Selected day. */
    private DayLabel day;
    /* <code>true</code> if the "Ok" button was clicked to close the dialog
     * box, <code>false</code> otherwise. */
    private boolean okay;
    /* <code>true</code> if the "Cancel" button was clicked to close the dialog
     * box, <code>false</code> otherwise. */
    private boolean cancel;

    /*
     * Custom panel that can receive the focus. Used to implement the calendar
     * control.
     */
    private static class FocusablePanel extends JPanel {
        /**
         * Constructs a new <code>FocusablePanel</code> with the given layout
         * manager.
         * @param layout The layout manager
         */
        public FocusablePanel(LayoutManager layout) {
            super(layout);
        }
        /**
         * Always returns <code>true</code>, since
         * <code>FocusablePanel</code> can receive the focus.
         * @return <code>true</code>
         */
        @Override
        public boolean isFocusable() {
            return true;
        }
    }

    /* Initialises this <code>DateChooser</code> object. Creates the controls,
     * registers listeners and initialises the dialog box. */
    private void construct() {
        // Escape key to cancel
        ActionListener cancelListener = (ActionEvent e) -> {
            if (day != null) {
                day.deselect();
            }
            cancel = true;
            setVisible(false);
            dispose();
        };

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        calendar = new GregorianCalendar();

        month = new TRComboBox<>(MONTHS);
        month.setPreferredSize(new Dimension(110, 23));
        month.addItemListener(this);
        month.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    goLastMonth();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    goNextMonth();
                }
            }
        });
        monthLast = new JButton();
        monthLast.setFocusable(false);
        monthLast.setIcon(ICON_BACK);
        monthLast.setMargin(new Insets(0, 0, 0, 0));
        monthLast.setMaximumSize(new Dimension(18, 23));
        monthLast.setMinimumSize(new Dimension(18, 23));
        monthLast.setPreferredSize(new Dimension(18, 23));
        monthLast.addActionListener((ActionEvent e) -> {
            goLastMonth();
        });
        monthNext = new JButton();
        monthNext.setFocusable(false);
        monthNext.setIcon(ICON_NEXT);
        monthNext.setMargin(new Insets(0, 0, 0, 0));
        monthNext.setMaximumSize(new Dimension(18, 23));
        monthNext.setMinimumSize(new Dimension(18, 23));
        monthNext.setPreferredSize(new Dimension(18, 23));
        monthNext.addActionListener((ActionEvent e) -> {
            goNextMonth();
        });
        year = new TRComboBox<>();
        year.setPreferredSize(new Dimension(110, 23));
        for (int i = FIRST_YEAR; i <= LAST_YEAR; i++) {
            year.addItem(Integer.toString(i));
        }
        year.addItemListener(this);
        year.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    goLastYear();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    goNextYear();
                }
            }
        });
        yearLast = new JButton();
        yearLast.setFocusable(false);
        yearLast.setIcon(ICON_BACK);
        yearLast.setMargin(new Insets(0, 0, 0, 0));
        yearLast.setMaximumSize(new Dimension(18, 23));
        yearLast.setMinimumSize(new Dimension(18, 23));
        yearLast.setPreferredSize(new Dimension(18, 23));
        yearLast.addActionListener((ActionEvent e) -> {
            goLastYear();
        });
        yearNext = new JButton();
        yearNext.setFocusable(false);
        yearNext.setIcon(ICON_NEXT);
        yearNext.setMargin(new Insets(0, 0, 0, 0));
        yearNext.setMaximumSize(new Dimension(18, 23));
        yearNext.setMinimumSize(new Dimension(18, 23));
        yearNext.setPreferredSize(new Dimension(18, 23));
        yearNext.addActionListener((ActionEvent e) -> {
            goNextYear();
        });
        days = new DayLabel[7][7];
        for (int i = 0; i < 7; i++) {
            days[0][i] = new DayLabelHdr(DAYS[i]);
        }
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                days[i][j] = new DayLabelNbr(0);
                days[i][j].addMouseListener(this);
            }
        }
        clear = new JButton(NbBundle.getMessage(clazz, "clear"));
        clear.addActionListener(this);
        okButton = new JButton(NbBundle.getMessage(clazz, "select"));
        okButton.addActionListener(this);

        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton(NbBundle.getMessage(clazz, "cancel"));
        cancelButton.addActionListener(this);

        JPanel monthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        monthPanel.add(monthLast);
        monthPanel.add(month);
        monthPanel.add(monthNext);

        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        yearPanel.add(yearLast);
        yearPanel.add(year);
        yearPanel.add(yearNext);

        JPanel monthYear = new JPanel();
        monthYear.add(monthPanel);
        monthYear.add(yearPanel);

        headGrid = new JPanel(new GridLayout(1, 7, 1, 1));
        headGrid.addKeyListener(this);
        for (int j = 0; j < 7; j++) {
            headGrid.add(days[0][j]);
        }
        headGrid.setBorder(EMPTY_BORDER);

        daysGrid = new FocusablePanel(new GridLayout(6, 7, 1, 1));
        daysGrid.addKeyListener(this);
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                daysGrid.add(days[i][j]);
            }
        }
        daysGrid.setBackground(GRID_BACKGROUND);

        JPanel daysPanel = new JPanel(new BorderLayout());
        daysPanel.add(headGrid, BorderLayout.NORTH);
        daysPanel.add(daysGrid, BorderLayout.CENTER);

        todayPane = new JEditorPane();
        todayPane.setEditable(false);
        todayPane.setBackground(monthPanel.getBackground());
        todayPane.setContentType("text/html");
        String today = NbBundle.getMessage(clazz, "today.is");
        todayPane.setText("<html><body><p align='center' style='font-family:sans-serif'>" + today + " <a href=''>" + DF.format(new Date()) + "</a></p><br></body></html>");
        todayPane.addHyperlinkListener((HyperlinkEvent e) -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                goToday();
            }
        });

        daysPanel.add(todayPane, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.add(clear);
        buttons.add(okButton);
        buttons.add(cancelButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(monthYear, BorderLayout.NORTH);
        mainPanel.add(daysPanel, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        JPanel fillBottomPanel = new JPanel();
        fillBottomPanel.setPreferredSize(new Dimension(0, 32));
        JPanel fillWestPanel = new JPanel();
        fillWestPanel.setPreferredSize(new Dimension(32, 0));
        JPanel fillEastPanel = new JPanel();
        fillEastPanel.setPreferredSize(new Dimension(32, 0));

        Container dialog = getContentPane();
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(fillBottomPanel, BorderLayout.SOUTH);
        dialog.add(fillWestPanel, BorderLayout.WEST);
        dialog.add(fillEastPanel, BorderLayout.EAST);

        pack();

        setResizable(false);

        daysGrid.requestFocusInWindow();
    }

    private void initTodayDate() {
        String today = NbBundle.getMessage(clazz, "today.is");
        todayPane.setText("<html><body><p align='center' style='font-family:sans-serif'>" + today + " <a href=''>" + DF.format(new Date()) + "</a></p><br></body></html>");
    }
    
    /* Gets the selected day, as an <code>int</code>. Parses the text of the
     * selected label in the calendar to get the day.
     * @return the selected day or -1 if there is no day selected. */
    private int getSelectedDay() {
        synchronized(this) {
            return day == null ? -1 : day.getNbr();
        }
    }

    /* Sets the selected day. The day is specified as the number of the day, in
     * the month, to selected. The function compute the corresponding control to
     * select.
     * @param newDay The day to select. */
    private void setSelected(int newDay) {
        synchronized(this) {
            setSelected(days[(newDay + offset - 1) / 7 + 1][(newDay + offset - 1) % 7]);
        }
    }

    /* Sets the selected day. The day is specified as the label control, in the
     * calendar, corresponding to the day to select.
     * @param newDay The day to select. */
    private void setSelected(DayLabel newDay) {
        synchronized(this) {
            System.out.println();
            if (day != null) {
                day.deselect();
            }
            day = newDay;
            day.select();
            daysGrid.requestFocusInWindow();
        }
    }

    private void goToday() {
        Calendar cal = Calendar.getInstance();
        year.setSelectedIndex(cal.get(Calendar.YEAR) - FIRST_YEAR);
        month.setSelectedIndex(cal.get(Calendar.MONTH) - Calendar.JANUARY);
        setSelected(cal.get(Calendar.DAY_OF_MONTH));
    }

    private void goLastMonth() {
        int i = month.getSelectedIndex() - 1;
        if (i >= MONTH_INDEX_JANUARY) {
            month.setSelectedIndex(i);
            update();
        } else {
            month.setSelectedIndex(MONTH_INDEX_DECEMBER);
            goLastYear();
        }
    }

    private void goNextMonth() {
        int i = month.getSelectedIndex() + 1;
        if (i <= MONTH_INDEX_DECEMBER) {
            month.setSelectedIndex(i);
            update();
        } else {
            month.setSelectedIndex(MONTH_INDEX_JANUARY);
            goNextYear();
        }
    }

    private void goLastYear() {
        int i = year.getSelectedIndex() - 1;
        if (i > -1) {
            year.setSelectedIndex(i);
            update();
        }
    }

    private void goNextYear() {
        int i = year.getSelectedIndex() + 1;
        if (i < year.getItemCount()) {
            year.setSelectedIndex(i);
            update();
        }
    }

    /* Updates the calendar. This function updates the calendar panel to reflect
     * the month and year selected. It keeps the same day of the month that was
     * selected, except if it is beyond the last day of the month. In this case,
     * the last day of the month is selected. */
    private void update() {
        synchronized(this) {
            int iday = getSelectedDay();

            for (int i = 0; i < 7; i++) {
                days[1][i].setNbr(0);
                days[5][i].setNbr(0);
                days[6][i].setNbr(0);
            }

            calendar.set(Calendar.DATE, 1);
            calendar.set(Calendar.MONTH, month.getSelectedIndex() + Calendar.JANUARY);
            calendar.set(Calendar.YEAR, year.getSelectedIndex() + FIRST_YEAR);

            offset = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;

            if (FIRST_DAY == Calendar.MONDAY) {
                offset = (offset > 0) ? offset - 1 : 6;
            }

            lastDay = calendar.getActualMaximum(Calendar.DATE);
            for (int i = 0; i < lastDay; i++) {
                days[(i + offset) / 7 + 1][(i + offset) % 7].setNbr(i + 1);
            }

            if (iday != -1) {
                if (iday > lastDay) {
                    iday = lastDay;
                }
                setSelected(iday);
            }
        }
    }

    /**
     * Called when the "Ok" or "Cancel" button is pressed. Just sets a flag and
     * hides the dialog box.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            okay = true;
        } else if (e.getSource() == cancelButton) {
            if (day != null) {
                day.deselect();
            }
            cancel = true;
        }
        setVisible(false);

        dispose();
    }

    /**
     * Called when a new month or year is selected. Updates the calendar to
     * reflect the selection.
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        update();
    }

    /**
     * Called when a key is pressed and the calendar has the focus. Handles the
     * arrow keys so that the user can select a day using the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int iday = getSelectedDay();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (iday > 1) {
                    setSelected(iday - 1);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (iday < lastDay) {
                    setSelected(iday + 1);
                }
                break;
            case KeyEvent.VK_UP:
                if (iday > 7) {
                    setSelected(iday - 7);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (iday <= lastDay - 7) {
                    setSelected(iday + 7);
                }
                break;
        }
    }

    /**
     * Called when the mouse is clicked on a day in the calendar. Selects the
     * clicked day.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        DayLabel theDay = (DayLabel) e.getSource();
        if (!theDay.getText().equals(" ")) {
            setSelected(theDay);
        }
        // if double click then automatically okay the dialog
        if (e.getClickCount() > 1) {
            okay = true;
            cancel = false;
            setVisible(false);
            dispose();
        } else {
            daysGrid.requestFocus();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * Constructs a new instance for the given window owner and default title.
     * @param owner The owning window.
     * @param firstDay The first day must be Calendar.SUNDAY or Calendar.Monday.
     */
    public DateChooserDialog(Frame owner) {
        this(owner, DEFAULT_TITLE);
    }

    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning window.
     * @param title The dialog title.
     * @param firstDay The first day must be Calendar.SUNDAY or Calendar.Monday.
     */
    public DateChooserDialog(Frame owner, String title) {
        super(owner, title, true);
        initialise();
    }

    /*
     * Constructs a new instance for the given dialog owner and default title.
     * @param owner The owning dialog.
     * @param firstDay The first day must be Calendar.SUNDAY or Calendar.Monday.
     */
    public DateChooserDialog(Dialog owner) {
        this(owner, DEFAULT_TITLE);
    }

    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning dialog.
     * @param title The dialog title.
     * @param firstDay The first day must be Calendar.SUNDAY or Calendar.Monday.
     */
    public DateChooserDialog(Dialog owner, String title) {
        super(owner, title, true);
        initialise();
    }

    private void initialise() {
//      setDefaultCloseOperation(HIDE_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        FIRST_DAY = DatesPrefs.getFirstDayOfWeek();
        DAYS = (FIRST_DAY == DatesPrefs.MONDAY) ? DAYS_MON_FIRST : DAYS_SUN_FIRST;
    }

    /**
     * Selects a date. Displays the dialog box, with a given date as the
     * selected date, and allows the user select a new date.
     * @param date The initial date.
     * @param component The component to position the dialog relative to.
     * @return the new date selected or <code>null</code> if the user press
     * "Cancel" or closes the dialog box
     */
    public Date select(Date date, Component component) {

        if (calendar == null) {
            construct();
        }

        initTodayDate();
        
        int hour = 0, min = 0, sec = 0, msec = 0;

        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);
            sec = calendar.get(Calendar.SECOND);
            msec = calendar.get(Calendar.MILLISECOND);
        }

        final int _year = calendar.get(Calendar.YEAR);
        final int _month = calendar.get(Calendar.MONTH);
        final int _day = calendar.get(Calendar.DATE);

        year.setSelectedIndex(_year - FIRST_YEAR);
        month.setSelectedIndex(_month - Calendar.JANUARY);

        if (getSelectedDay() != _day) {
            SwingUtilities.invokeLater(() -> {
                setSelected(_day);
            });
        }

        if (component != null) {
            setLocationRelativeTo(component);
        }

        daysGrid.requestFocusInWindow();

        okay = false; cancel = false;

        setVisible(true);
        dispose();


        if (okay) {
            calendar.set(Calendar.DATE, getSelectedDay());
            calendar.set(Calendar.MONTH, month.getSelectedIndex() + Calendar.JANUARY);
            calendar.set(Calendar.YEAR, year.getSelectedIndex() + FIRST_YEAR);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, sec);
            calendar.set(Calendar.MILLISECOND, msec);
            return calendar.getTime();
        }

        return null;
    }

    /**
     * Determines whether the cancel button was activated.
     * @return true if the cancel button was activated.
     */
    public boolean cancelled() {
        return cancel;
    }

    /**
     * Determines whether the okay button was activated.
     * @return true if the okay button was activated.
     */
    public boolean okayed() {
        return okay;
    }
}

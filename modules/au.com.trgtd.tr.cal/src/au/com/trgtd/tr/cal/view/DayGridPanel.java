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
import au.com.trgtd.tr.cal.ctlr.DayGridCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.EventUtils;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.utils.Utils;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

/**
 * Day panel paints background lines for hours and half hours and contains event
 * panels that are positioned and sized according to start and end time and
 * whether or not other events start at the same time or overlap.
 *
 * @author Jeremy Moore
 */
public class DayGridPanel extends JLayeredPane {

    private interface Colors {
        Color AZURE_X = new Color(241, 255, 255);
        Color BRDR_LINE = new Color(8 * 26, 8 * 26, 8 * 26);
        Color HOUR_LINE = new Color(8 * 26, 8 * 26, 8 * 26);
        Color MINS_LINE = new Color(8 * 28, 8 * 28, 8 * 28);
        Color TODAYS_BG = AZURE_X;
        Color NORMAL_BG = Color.WHITE;
        Color TIME_LINE = Color.RED;
    }
    private interface Strokes {
        Stroke DASH = new BasicStroke(1.0f, // Width
                BasicStroke.CAP_SQUARE, // End cap
                BasicStroke.JOIN_MITER, // Join style
                1.0f, // Miter limit
                new float[]{1.0f, 2.0f}, // Dash pattern
                0.0f);                      // Dash phase    
        Stroke LINE = new BasicStroke(1.0f);
    }

    private interface Constants {
        int INDENT_WIDTH = 8;
    }
    
    private final List<EventPanel> events = new ArrayList<>();
    private final List<EventGroup> groups = new ArrayList<>();
    private final int begHr;
    private final int endHr;
    private final DateCtlr dateCtlr;
    private final DayGridCtlr dayGridCtlr;

    public DayGridPanel(int begHr, int endHr, final DateCtlr dateCtlr, DayGridCtlr dayGridCtlr) {
        this.begHr = begHr;
        this.endHr = endHr;
        this.dateCtlr = dateCtlr;
        this.dayGridCtlr = dayGridCtlr;
        setLayout(null);
        setPreferredSize(new Dimension(400, 800));
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Colors.BRDR_LINE));
        setOpaque(true);
        setBackground();                        
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                paintAll();
            }
            @Override
            public void componentMoved(ComponentEvent ce) {
                paintAll();
            }
            @Override
            public void componentShown(ComponentEvent ce) {
                paintAll();
            }
            @Override
            public void componentHidden(ComponentEvent ce) {
            }
        });
        dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            setBackground();
        });
    }
    
    public int getStartHr() {
        return begHr;
    }

    public int getEndHr() {
        return endHr;
    }

    private void setBackground() {
        Color oldBG = this.getBackground();
        Color newBG = DateUtils.isToday(dateCtlr.getDate()) ? Colors.TODAYS_BG : Colors.NORMAL_BG;
        if (!Utils.equal(oldBG, newBG)) {
            setBackground(newBG);            
        }
    }
    
    public void addEvent(EventPanel event) {
        events.add(event);
        regroupEvents();
        super.add(event);
        paintAll();
    }

    public void removeEvent(EventPanel event) {
        events.remove(event);
        regroupEvents();
        super.remove(event);
        paintAll();
    }

    public void resetEvents(Collection<EventPanel> eventPanels) {
        events.clear();
        events.addAll(eventPanels);
        regroupEvents();
        super.removeAll();
        for (EventPanel eventPanel : eventPanels) {
            super.add(eventPanel);
        }
        paintAll();
    }

    public void removeEvents() {
        events.clear();
        regroupEvents();
        super.removeAll();
        paintAll();
    }

    /* Recalculate event groups. Should be called when event list changes. */
    private void regroupEvents() {
        groups.clear();

        // sort events by ascending start date
        Collections.sort(events, ascEventStartComparator);

        // group events with the same start time        
        EventGroup lastGroup = null;
        EventPanel lastEvent = null;
        for (EventPanel event : events) {
            if (lastEvent != null && sameStart(event, lastEvent) && lastGroup != null) {
                lastGroup.add(event);
            } else {
                lastGroup = new EventGroup();
                lastGroup.add(event);
                groups.add(lastGroup);
            }
            lastEvent = event;
        }

        // set group indentations
        lastGroup = null;
        for (EventGroup thisGroup : groups) {
            if (lastGroup != null && thisGroup.overlaps(lastGroup)) {
                lastGroup.incrIndentRight();
                thisGroup.setIndentLeft(lastGroup.indentLeft() + 1);
            }
            lastGroup = thisGroup;
        }
    }

    /* Recalculate size and position of events according to groups. 
     * Should be called before laying out event panels. */
    private void arrangeEvents() {
        for (EventGroup group : groups) {
            int number = group.number();
            int inLeft = group.indentLeft();
            int inRght = group.indentRight();

            double groupW = getWidth();
            groupW -= (inLeft * Constants.INDENT_WIDTH);
            groupW -= (inRght * Constants.INDENT_WIDTH);

            double eventW = groupW / number;

            for (int i = 0; i < group.events.size(); i++) {
                EventPanel eventPanel = group.events.get(i);
                CalEvent event = eventPanel.getEvent();
                int x1 = (int) ((inLeft * Constants.INDENT_WIDTH) + (i * eventW));
                int y1 = eventY1(event);
                int y2 = eventY2(event);
                int h = y2 - y1;
                if (h < this.eventMinHeight()) {
                    h = this.eventMinHeight();
                } 
                int w = (int) eventW;
                eventPanel.setBounds(new Rectangle(x1, y1, w, h));
                
                this.moveToFront(eventPanel);            
            }
        }
    }

    private void hideEvents() {
        for (Component c : getComponents()) {
            c.setVisible(false);
        }
    }

    private void showEvents() {
        for (Component c : getComponents()) {
            c.setVisible(true);
        }
    }

    private Date eventStart(CalEvent event) {
        return EventUtils.getStart(event, dateCtlr.getDate()); 
    }
    
    private Date eventEnd(CalEvent event) {
        return EventUtils.getEnd(event, dateCtlr.getDate()); 
    }

    private int eventY1(CalEvent event) {
        double gridMins = getGridMins();
        if (gridMins < 1) {
            return 0;
        }
        
        Date evtStart = eventStart(event);
        Date dayStart = dayGridCtlr.getStartOfDay();

        if (evtStart.before(dayStart)) {
            return 0 - 50; //EventPanel.ARC;
        } 
        if (evtStart.after(dayStart)) {
            double startMins = DateUtils.getTimeInMins(evtStart);
            return (int) Math.round((startMins / gridMins) * getHeight());
        } 

        return 0;        
    }

    private int eventY2(CalEvent event) {
        double gridMins = getGridMins();
        if (gridMins < 1) {
            return 0;
        }
        
        Date evtEnd = eventEnd(event);
        Date dayEnd = dayGridCtlr.getEndOfDay();
        
        if (evtEnd.after(dayEnd)) {
            return getHeight() + 50; //EventPanel.ARC;
        } 
        
        double endMins = DateUtils.getTimeInMins(evtEnd);
        
        return (int) Math.round((endMins / gridMins) * getHeight());
    }
    
    private int eventMinHeight() {
        double gridMins = getGridMins();
        if (gridMins < 1) {
            return 0;
        }
        return (int) Math.round((30 / gridMins) * getHeight());
    }
    
    private int getGridHours() {
        return 1 + endHr - begHr;
    }

    private int getGridMins() {
        return getGridHours() * 60;
    }

    private boolean sameStart(EventPanel e1, EventPanel e2) {
        Date d1 = filter(eventStart(e1.getEvent()));
        Date d2 = filter(eventStart(e2.getEvent()));
        return d1.equals(d2);
    }

    private Date filter(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    private final Comparator<EventPanel> ascEventStartComparator = (EventPanel e1, EventPanel e2) -> {
        Date d1 = filter(eventStart(e1.getEvent()));
        Date d2 = filter(eventStart(e2.getEvent()));
        return d1.compareTo(d2);
    };

    private void paintGridLines(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        final int W = getWidth();
        final int H = getHeight();
        final double hours = 1 + endHr - begHr;
        final double hourHeight = H / hours;
        int y;
        for (int i = 1; i <= hours; i++) {
            y = (int) Math.round(i * hourHeight);
            g2.setColor(Colors.HOUR_LINE);
            g2.setStroke(Strokes.LINE);
            g2.drawLine(0, y, W, y);
            y -= (int) Math.round(hourHeight / 2);
            g2.setColor(Colors.MINS_LINE);
            g2.setStroke(Strokes.DASH);
            g2.drawLine(0, y, W, y);
        }

        // time line if today
        if (DateUtils.isToday(dateCtlr.getDate())) {
            final double minHeight = hourHeight / 60;
            Calendar c = Calendar.getInstance();
            int h = c.get(Calendar.HOUR_OF_DAY) - begHr;
            int m = c.get(Calendar.MINUTE);
            y = (int) Math.round(h * hourHeight);
            y+= (int) Math.round(m * minHeight);
            g2.setColor(Colors.TIME_LINE);
            g2.setStroke(Strokes.LINE);
            g2.drawLine(0, y, W, y);            
        }
    }

    private void paintAll() {
        hideEvents();
        repaint();
        arrangeEvents();
        showEvents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGridLines(g);
    }

    /* Inner class for a group of events that start at the same time. */
    private class EventGroup {

        private final List<EventPanel> events;
        private int indentLeft;
        private int indentRight;

        public EventGroup() {
            this.events = new ArrayList<>();
        }
        
        public void add(EventPanel event) {
            this.events.add(event);
            Collections.sort(events, descEndComparator);
        }

        public int number() {
            return this.events.size();
        }

        public int indentLeft() {
            return this.indentLeft;
        }

        public void setIndentLeft(int indentLeft) {
            this.indentLeft = indentLeft;
        }

        public void incrIndentLeft() {
            this.indentLeft++;
        }

        public int indentRight() {
            return this.indentRight;
        }

        public void setIndentRight(int indentRight) {
            this.indentRight = indentRight;
        }

        public void incrIndentRight() {
            this.indentRight++;
        }

        public Date starts() {
            return eventStart(events.get(0).getEvent());
        }

        public Date ends() {
            return eventEnd(events.get(0).getEvent());
        }

        private boolean overlaps(EventGroup thatGroup) {
            assert(thatGroup != null);
            Date thisBeg = starts();
            Date thisEnd = ends();
            Date thatBeg = thatGroup.starts();
            Date thatEnd = thatGroup.ends();
            if (thatEnd.before(thisBeg)) {
                return false;
            }
            return !thatBeg.after(thisEnd);
        }

        private final Comparator<EventPanel> descEndComparator = (EventPanel event1, EventPanel event2) -> {
            Date date1 = filter(eventEnd(event1.getEvent()));
            Date date2 = filter(eventEnd(event2.getEvent()));
            return date2.compareTo(date1);
        };
    }
}

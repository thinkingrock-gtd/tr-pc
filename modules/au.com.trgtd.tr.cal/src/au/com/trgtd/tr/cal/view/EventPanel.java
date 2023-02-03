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
import au.com.trgtd.tr.prefs.cal.CalendarPrefs;
import au.com.trgtd.tr.cal.utils.Fonts;
import au.com.trgtd.tr.services.Services;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 * Calendar event component.
 *
 * @author Jeremy Moore
 */
public class EventPanel extends JPanel {

    private final static int ARC = 12;    
    private final static int ALPHA_NORMAL = 150;
    private final static int ALPHA_SELECT = 200;
    private final static DateFormat DF_TIME = CalendarPrefs.isHourDisplayAMPM() 
                                            ? new SimpleDateFormat("h:mm a")
                                            : new SimpleDateFormat("H:mm");
    private final static DateFormat DF_FULL = CalendarPrefs.isHourDisplayAMPM()
                                            ? new SimpleDateFormat("h:mm a (d MMMM)")
                                            : new SimpleDateFormat("H:mm (d MMMM)");
    
    private final JLabel textLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();
    private final CalEvent event;
    private final Date date;
    private Color bgNormal;
    private Color bgSelect;
    private Color fgNormal;

    public EventPanel(Date date, CalEvent event) {
        super();
        if (null == date) {
            throw new IllegalArgumentException("Date can not be null.");
        }
        if (null == event) {
            throw new IllegalArgumentException("Event can not be null.");
        }

        this.date = date;
        this.event = event;
        
        initPanel();
        initValues();

        this.event.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            initValues();
        });

    }

    public CalEvent getEvent() {
        return event;
    }

    private void initPanel() {
        setLayout(new MigLayout("", "2[]4[]0", "0[]0"));
        setOpaque(false);
        setFocusable(true);
        // mouse click or press causes focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                EventPanel.this.requestFocusInWindow();
                if (me.getClickCount() == 2) {
                    event.edit();
                }
            }
            @Override
            public void mousePressed(MouseEvent me) {
                EventPanel.this.requestFocusInWindow();
            }
        });
        // focus gain or loss causes background transparency change 
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                moveToFront();
                validate();
                repaint();
            }
            @Override
            public void focusLost(FocusEvent fe) {
                validate();
                repaint();
            }
        });
        timeLabel.setBorder(null);
        timeLabel.setFont(Fonts.EVENT_TEXT);
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
//      timeLabel.setVerticalAlignment(SwingConstants.TOP);
        
        textLabel.setBorder(null);
        textLabel.setFont(Fonts.EVENT_TEXT.deriveFont(Font.BOLD));
        textLabel.setHorizontalAlignment(SwingConstants.LEFT);
//      textLabel.setVerticalAlignment(SwingConstants.TOP);
        
        this.add(timeLabel, "top, left, growy 0");
        this.add(textLabel, "top, left, growy 0, wrap");
        this.setToolTipText(getToolTipString());
    }

    private void moveToFront() {       
        try {
            JLayeredPane parent = (JLayeredPane)getParent();
            parent.moveToFront(this);            
        } catch (Exception e) {            
        }
    }
    
    private void initValues() {
        Color c = event.getCalEventBg();
        bgNormal = new Color(c.getRed(), c.getGreen(), c.getBlue(), ALPHA_NORMAL);
        bgSelect = new Color(c.getRed(), c.getGreen(), c.getBlue(), ALPHA_SELECT);
        fgNormal = event.getCalEventFg();
        timeLabel.setText(getTimeString());
        timeLabel.setForeground(fgNormal);
        textLabel.setText(getTextString());
        textLabel.setForeground(fgNormal);
    }

    private String getTimeString() {
        Day gridDay = new Day(date);
        Day startDay = new Day(event.getCalEventStart());
        if (startDay.before(gridDay)) {
            return " ... ";
        }
        return DF_TIME.format(event.getCalEventStart());
    }
    
    private String getToolTipString() {
        Date begDate = event.getCalEventStart();
        Date endDate = event.getCalEventEnd();        
        Day theDay = new Day(date);
        Day begDay = new Day(begDate);
        Day endDay = new Day(endDate);
        String dateStr;
        if (begDay.before(theDay)) {
            dateStr = DF_FULL.format(begDate) + " - ";
        } else {
            dateStr = DF_TIME.format(begDate) + " - ";
        }        
        if (endDay.after(theDay)) {
            dateStr += DF_FULL.format(endDate);
        } else {
            dateStr += DF_TIME.format(endDate);
        }        
        String pathStr = Services.instance.getPath(event.getAction());
        return "<html>" + 
                dateStr + "<br>" +                 
               "<b>" + event.getCalEventText() + "</b><br>" + 
               pathStr + 
               "</html>";
    }
    
    private String getTextString() {
        if (event.isDone()) {
            return "<html><s>" + event.getCalEventText() + "</s></html>";
        } else {
            return event.getCalEventText();            
        }        
    }

    @Override
    protected void paintComponent(Graphics g) {
        final int x = 0, y = 0;
        final int w = getWidth() - 1;
        final int h = getHeight() - 1;
        final Color bg = hasFocus() ? bgSelect : bgNormal;
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(x, y, w, h, ARC, ARC);        
        g2.setColor(bg.darker());        
        g2.drawRoundRect(x, y, w, h, ARC, ARC);                
        g2.dispose();
        super.paintComponent(g);
    }

}
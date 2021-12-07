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

import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * Week panel arranges day grid panels with a date heading above each day grid.
 *
 * @author Jeremy Moore
 */
public class WeekPanel extends JPanel {
    
    private interface Fonts {
        Font DATE_NORMAL = UIManager.getFont("Label.font").deriveFont(Font.PLAIN).deriveFont(12.0f);
        Font DATE_TODAY = UIManager.getFont("Label.font").deriveFont(Font.PLAIN).deriveFont(12.0f);
    }
    private interface Colors {
        Color BLUE_3 = new Color(0, 104, 139);        
        Color DATE_NORMAL = Color.GRAY;
        Color DATE_TODAY = BLUE_3;
    }    
    
    private final DateFormat DF = new SimpleDateFormat(" d EEEE");
    private final PropertyChangeSupport pcs;
    private final DateLabel[] dateLabels;
    
    public WeekPanel(DayTimePanel panelTimes, DayGridPanel[] gridPanels, AllDayPanel[] listPanels) {
        
        this.pcs = new PropertyChangeSupport(this);

        JPanel panelDates = new JPanel(new GridLayout(1,7));
        panelDates.setOpaque(false);                
        
        JPanel panelLists = new JPanel(new GridLayout(1,7));
        panelLists.setOpaque(false);        
        
        JPanel panelGrids = new JPanel(new GridLayout(1,7));
        panelGrids.setOpaque(false);        
        
        dateLabels = new DateLabel[7];        
        for (int i = 0; i < 7; i++) {
            dateLabels[i] = new DateLabel();
            dateLabels[i].addMouseListener(dateLabelMouseListener);
            panelDates.add(dateLabels[i]);
            panelLists.add(listPanels[i]);
            panelGrids.add(gridPanels[i]);            
        }

        setOpaque(false);
        setLayout(new MigLayout("fill", "0[40!]0[fill, grow]0", "0[]0[grow]3[grow]0"));        
        add(panelDates, "skip, grow, wrap");
        add(panelLists, "skip, grow, wrap");
        add(panelTimes, "align right, growy");
        add(panelGrids, "grow, wrap");
    }
    
    public void updateDayHeadings(Day firstDay, Day lastDay) {
        int i = 0;
        for (Day day = firstDay; !day.after(lastDay); day = day.next()) {
            if (DateUtils.isToday(day.getDate())) {               
                dateLabels[i].setFont(Fonts.DATE_TODAY);
                dateLabels[i].setForeground(Colors.DATE_TODAY);            
            } else {
                dateLabels[i].setFont(Fonts.DATE_NORMAL);
                dateLabels[i].setForeground(Colors.DATE_NORMAL);            
            }
            dateLabels[i++].setDate(day.getDate());
        }        
    }
    
    public void addDayListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    
    public void removeDayListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    private final MouseListener dateLabelMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
                DateLabel dateLabel = (DateLabel)me.getSource();
                pcs.firePropertyChange("day", null, dateLabel.date);
            }            
        }        
    };
    
    private class DateLabel extends JLabel {
        private Date date;
        public DateLabel() {
            super(" ");
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
            setText(null == date ? " " : DF.format(date));            
        }
        @Override
        public synchronized void addMouseListener(MouseListener ml) {
            super.addMouseListener(ml);
        }
    }
    
}

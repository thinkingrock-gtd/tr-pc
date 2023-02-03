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
import au.com.trgtd.tr.cal.utils.DateUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import javax.swing.*;

/**
 * Panel to list all-day events.
 * 
 * @author Jeremy Moore
 */
public final class AllDayPanel extends JPanel {

    private interface Colors {
        Color AZURE_X = new Color(241, 255, 255);
        Color BRDR_LINE = new Color(8 * 26, 8 * 26, 8 * 26);
        Color TODAYS_BG = AZURE_X;
        Color NORMAL_BG = Color.WHITE;        
    }  

    private final int minW = 0;
    private final int maxW = Integer.MAX_VALUE;
    private final DateCtlr dateCtlr;
    private final JPanel listPanel;    
    private final JScrollPane scrollPane;
    
    public AllDayPanel(final DateCtlr dateCtlr) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Colors.BRDR_LINE));
        setOpaque(false);
        
        this.dateCtlr = dateCtlr;

        listPanel = new JPanel();
        listPanel.setOpaque(true);
        listPanel.setBorder(null);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       
        add(scrollPane, BorderLayout.CENTER);
        
        dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            setBackground();
        });
        
        setBackground();
        
        resetSize();
    }

    private void setBackground() {
        Color bg = DateUtils.isToday(dateCtlr.getDate()) ? Colors.TODAYS_BG : Colors.NORMAL_BG;
        listPanel.setBackground(bg);            
    }

    public void addEvent(EventLabel eventLabel) {
        listPanel.add(eventLabel);        
        eventLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, eventLabel.getMaximumSize().height));                
        resetSize();
        listPanel.invalidate();
        listPanel.repaint();
    }

    public void removeEvent(EventLabel eventLabel) {
        listPanel.remove(eventLabel);
        resetSize();
        listPanel.invalidate();
        listPanel.repaint();
    }
    
    public void resetEvents(Collection<EventLabel> eventLabels) {
        listPanel.removeAll();
        for (EventLabel eventLabel : eventLabels) {
            listPanel.add(eventLabel);        
            eventLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, eventLabel.getMaximumSize().height));                
        }
        resetSize();
        listPanel.invalidate();
        listPanel.repaint();
    }

    private void resetSize() {
        int n = listPanel.getComponentCount();
        n = Math.max(n, 1);
        n = Math.min(n, 6);
        int h = EventLabel.getEventLabelHeight();
        setMinimumSize(new Dimension(minW, (n * h) + 4));
        setMaximumSize(new Dimension(maxW, (6 * h) + 4));
    }
    
}

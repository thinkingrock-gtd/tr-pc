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
import au.com.trgtd.tr.cal.utils.Fonts;
import au.com.trgtd.tr.services.Services;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Calendar event component for an all day event.
 *
 * @author Jeremy Moore
 */
public final class EventLabel extends JLabel {

    private final static int ALPHA_NORMAL = 150;
    private final static int ALPHA_SELECT = 200;
    private final static int ARC = 8;
    private final static Font FONT = Fonts.EVENT_TEXT.deriveFont(Font.BOLD);

    private static int height = 13;
    public static int getEventLabelHeight() {
        return height;
    }
    
    private final CalEvent event;
    private Color bgNormal;
    private Color bgSelect;
    private Color fgNormal;
    
    
    public EventLabel(CalEvent event) {
        super();
        
        if (null == event) {
            throw new IllegalArgumentException("Event can not be null.");
        }
        
        init();

        this.event = event;
        this.event.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            initValues();
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                int h = EventLabel.this.getHeight();
                if (h > 0 && h != height) {
                    height = h;                    
                }
            }            
        });
        
        initValues();
    }

    public CalEvent getEvent() {
        return event;
    }

    private void init() {
        setOpaque(true);
        setFocusable(false);
        
        setFont(FONT);
        setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.TOP);
        setVerticalTextPosition(SwingConstants.TOP);

        // mouse click or press causes focus
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                EventLabel.this.requestFocusInWindow();
                if (me.getClickCount() == 2) {
                    event.edit();
                }
            }
            @Override
            public void mousePressed(MouseEvent me) {
                EventLabel.this.requestFocusInWindow();
            }
        });
        // focus gain or loss causes background transparency change 
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                validate();
                repaint();
            }
            @Override
            public void focusLost(FocusEvent fe) {
                validate();
                repaint();
            }
        });
    }

    private void initValues() {
        Color c = event.getCalEventBg();
        
        bgNormal = new Color(c.getRed(), c.getGreen(), c.getBlue(), ALPHA_NORMAL);
        bgSelect = new Color(c.getRed(), c.getGreen(), c.getBlue(), ALPHA_SELECT);
        fgNormal = event.getCalEventFg();
        setForeground(fgNormal);        
        
        setBackground(bgNormal);
        
        setText(getTextString());
        setToolTipText(getToolTipString());
    }

    private String getTextString() {
        if (event.isDone()) {
            return "<html><s>" + event.getCalEventText() + "</s></html>";
        } else {
            return event.getCalEventText();            
        }        
    }

    private String getToolTipString() {
        String pathStr = Services.instance.getPath(event.getAction());
        return "<html><b>" + event.getCalEventText() + "</b><br>" + pathStr + "</html>";
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        final int x = 0, y = 1;
        final int w = getWidth() - 1;
        final int h = getHeight() - 2;
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
package au.com.trgtd.tr.cal.view;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.utils.DateUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.*;

/**
 * Day list panel lists all-day events.
 * 
 * @author Jeremy Moore
 */
public final class DayListPanel extends JPanel {

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
    
    public DayListPanel(final DateCtlr dateCtlr) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Colors.BRDR_LINE));
        setOpaque(false);
        
        this.dateCtlr = dateCtlr;
        this.dateCtlr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                setBackground();                        
            }            
        });

        listPanel = new JPanel();
        listPanel.setOpaque(true);
        listPanel.setBorder(null);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       
        add(scrollPane, BorderLayout.CENTER);
        
        
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

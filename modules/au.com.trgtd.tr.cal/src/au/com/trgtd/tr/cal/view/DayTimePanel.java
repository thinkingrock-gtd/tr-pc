package au.com.trgtd.tr.cal.view;

import au.com.trgtd.tr.prefs.cal.CalendarPrefs;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.openide.util.NbBundle;

/**
 * Day time panel shows the hours positioned to correspond to the day grid of
 * the same height and the same start and end hours.
 *
 * @author Jeremy Moore
 */
public final class DayTimePanel extends JPanel {

    private final static String MSG_NOON = NbBundle.getMessage(DayTimePanel.class, "noon");

    private interface Fonts {
        Font time = UIManager.getFont("Label.font").deriveFont(Font.PLAIN).deriveFont(10.0f);
    }
    private interface Colors {
        Color time = Color.GRAY;
    }    

    private final int begHr;
    private final int endHr;
    
    public DayTimePanel(int begHr, int endHr) {
        this.begHr = begHr;
        this.endHr = endHr;
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(50, 800));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        final FontMetrics fm = getFontMetrics(Fonts.time);
        final int W = getWidth();
        final int H = getHeight();
        final double hours = 1 + endHr - begHr;
        final double hourHeight = H / hours;         
        final Graphics2D g2 = (Graphics2D)g.create();        
        g2.setColor(Colors.time);
        g2.setFont(Fonts.time);
        for (int i = 0; i < hours; i++) {
            String s = getHourString(i);
            int x = W - fm.stringWidth(s);
            int y = (int)Math.round(i * hourHeight) + 5;
            g2.drawString(s, x, y);
        }
    }
    
    private String getHourString(int index) {
        int hour = index + begHr;
        if (hour == 0) {
            return " ";            
        }
        if (CalendarPrefs.isHourDisplay24hr()) {
            return hour + " ";
        } else {
            if (hour == 12) {
                return MSG_NOON + " ";
            }
            if (hour < 12) {
                return hour + " AM ";
            }
            return (hour-12) + " PM ";
        }
    }
    
}

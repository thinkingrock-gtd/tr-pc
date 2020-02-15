package au.com.trgtd.tr.cal.view;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Day panel contains day time panel and day grid panel.
 *
 * @author Jeremy Moore
 */
public final class DayPanel extends JPanel {

    private final DayGridPanel dayGridPanel;

    public DayPanel(AllDayPanel dayListPanel, DayTimePanel dayTimePanel, DayGridPanel dayGridPanel) {
        this.dayGridPanel = dayGridPanel;
        setOpaque(false);
        setLayout(new MigLayout("fill", "0[40!]0[grow]0", "0[grow]3[grow]0"));
        add(dayListPanel, "skip, grow, wrap");
        add(dayTimePanel, "align right, growy");
        add(dayGridPanel, "grow");
    }

    public synchronized void removeEventViewListener(PropertyChangeListener pl) {
        dayGridPanel.removePropertyChangeListener(pl);
    }

    public synchronized void addEventViewListener(PropertyChangeListener pl) {
        dayGridPanel.addPropertyChangeListener(pl);
    }

}

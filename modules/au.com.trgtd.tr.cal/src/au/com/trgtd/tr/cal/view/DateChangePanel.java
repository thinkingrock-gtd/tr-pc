package au.com.trgtd.tr.cal.view;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 * Panel for changing a date controller, with three buttons for: previous 
 * period, today, and next period.
 *
 * @author Jeremy Moore
 */
public class DateChangePanel extends JPanel {

    private final static String MSG_TODAY = NbBundle.getMessage(DateChangePanel.class, "today");
    private final DateCtlr dateCtlr;
    private Period period;

    /**
     * Constructs a new instance.
     * @param dateCtlr The date controller.
     * @param period The period for next/previous changes.
     */
    public DateChangePanel(DateCtlr dateCtlr, Period period) {
        this.period = period;
        this.dateCtlr = dateCtlr;
        initPanel();
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public void next() {
        Calendar c = Calendar.getInstance();
        c.setTime(dateCtlr.getDate());
        switch (period) {
            case Day:
                c.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case Week:
                c.add(Calendar.DAY_OF_YEAR, 7);
                break;
            case Month:
                c.add(Calendar.MONTH, 1);
                break;
            case Year:
                c.add(Calendar.YEAR, 1);
                break;
            default:
                return;
        }
        dateCtlr.setDate(c.getTime());
    }

    public void prev() {
        Calendar c = Calendar.getInstance();
        c.setTime(dateCtlr.getDate());
        switch (period) {
            case Day:
                c.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case Week:
                c.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case Month:
                c.add(Calendar.MONTH, -1);
                break;
            case Year:
                c.add(Calendar.YEAR, -1);
                break;
            default:
                return;
        }
        dateCtlr.setDate(c.getTime());
    }

    public void today() {
        dateCtlr.setDate(new Date());
    }

    private void initPanel() {
        String style = "segmented";
//      String style = "segmentedRoundRect";
//      String style = "segmentedCapsule";
//      String style = "segmentedTextured";
        
        Icon prevIcon = ImageUtilities.loadImageIcon("au/com/trgtd/tr/cal/view/prev.png", true);
        Icon nextIcon = ImageUtilities.loadImageIcon("au/com/trgtd/tr/cal/view/next.png", true);
        
        JButton prevButton = new JButton();
        prevButton.setIcon(prevIcon);
        prevButton.setFocusable(false);
        prevButton.putClientProperty("JButton.buttonType", style);
        prevButton.putClientProperty("JButton.segmentPosition", "first");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                prev();
            }
        });
        JButton todayButton = new JButton(MSG_TODAY);
        todayButton.setFocusable(false);
        todayButton.putClientProperty("JButton.buttonType", style);
        todayButton.putClientProperty("JButton.segmentPosition", "middle");
        todayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                today();
            }
        });
        JButton nextButton = new JButton();
        nextButton.setIcon(nextIcon);
        nextButton.setFocusable(false);
        nextButton.putClientProperty("JButton.buttonType", style);
        nextButton.putClientProperty("JButton.segmentPosition", "last");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                next();
            }
        });
        Box box = Box.createHorizontalBox();
        box.add(prevButton);
        box.add(todayButton);
        box.add(nextButton);        
        
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new BorderLayout());
        add(box, BorderLayout.CENTER);
    }
}

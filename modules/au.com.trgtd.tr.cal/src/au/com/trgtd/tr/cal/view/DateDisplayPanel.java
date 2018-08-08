package au.com.trgtd.tr.cal.view;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.utils.DateUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.openide.util.NbBundle;

/**
 * Date display panel.
 *
 * @author Jeremy Moore
 */
public final class DateDisplayPanel extends JPanel {

    private interface Fonts {
        final Font PLAIN = UIManager.getFont("Label.font").deriveFont(Font.PLAIN);
        final Font DATE = PLAIN;        
    }
    private interface Colors {
        final Color DATE = Color.DARK_GRAY;
    }

    private final static String MSG_WEEK = NbBundle.getMessage(DateDisplayPanel.class, "week").toLowerCase();

    private final static DateFormat DF_DAY = DateFormat.getDateInstance(DateFormat.FULL);
    private final static DateFormat DF_WEEK_SSM = new SimpleDateFormat("d - ");
    private final static DateFormat DF_WEEK_SDM = new SimpleDateFormat("d MMMM - ");    
//  private final static DateFormat DF_WEEK_END = new SimpleDateFormat("d MMMM yyyy '(" + MSG_WEEK + " '" + "w" + "')'");
    private final static DateFormat DF_WEEK_END = new SimpleDateFormat("d MMMM yyyy ");
    private final static DateFormat DF_WEEK_NBR = new SimpleDateFormat("w");
    private final static DateFormat DF_MONTH  = new SimpleDateFormat("MMMM yyyy");
    private final static DateFormat DF_YEAR  = new SimpleDateFormat("yyyy");

    private final Period period;    
    private final JLabel dateLabel = new JLabel();
    private final DateCtlr dateCtlr;

    public DateDisplayPanel(DateCtlr dateCtlr) {
        this(dateCtlr, Period.Day);
    }
        
    public DateDisplayPanel(DateCtlr dateCtlr, Period period) {
        this.period = period;
        this.dateCtlr = dateCtlr;
        this.dateCtlr.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                dateChanged(); 
            }
        });
        initThisPanel();
        dateChanged();
    }
    
    private void dateChanged() {        
        Date date = dateCtlr.getDate();
        if (date == null) {
            dateLabel.setText("");
            return;
        }        
        switch (period) {            
            case Day:
                dateLabel.setText(DF_DAY.format(date));
                break;
            case Week:
                Date beg = dateCtlr.getWeekStart().getDate();
                Date end = dateCtlr.getWeekEnd().getDate();                        
                String week = "(" + MSG_WEEK + " " + DF_WEEK_NBR.format(beg) + ")";
                if (DateUtils.isSameMonth(beg, end)) {
//                  dateLabel.setText(DF_WEEK_SSM.format(beg) + DF_WEEK_END.format(end));                    
                    dateLabel.setText(DF_WEEK_SSM.format(beg) + DF_WEEK_END.format(end) + week);                    
                } else {
//                  dateLabel.setText(DF_WEEK_SDM.format(beg) + DF_WEEK_END.format(end));
                    dateLabel.setText(DF_WEEK_SDM.format(beg) + DF_WEEK_END.format(end) + week);                                        
                }
                break;
            case Month:
                dateLabel.setText(DF_MONTH.format(date));
                break;
            case Year:
                dateLabel.setText(DF_YEAR.format(date));
        }
    }

    private void initThisPanel() {
        dateLabel.setForeground(Colors.DATE);
        dateLabel.setFont(Fonts.DATE);
        setLayout(new BorderLayout());
        setOpaque(false);
        add(dateLabel, BorderLayout.CENTER);
    }

}

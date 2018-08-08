package au.com.trgtd.tr.prefs.cal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;


final class CalendarPrefsPanel extends JPanel implements ActionListener {

    private final CalendarOptionsPanelController controller;
    private final ButtonGroup timeGroup;
    private final JRadioButton timeAMPM;
    private final JRadioButton time24HR;

    CalendarPrefsPanel(CalendarOptionsPanelController controller) {
        this.controller = controller;
        timeAMPM = new JRadioButton(NbBundle.getMessage(getClass(), "time.am.pm"));
        timeAMPM.addActionListener(this);
        time24HR = new JRadioButton(NbBundle.getMessage(getClass(), "time.24.hr"));
        time24HR.addActionListener(this);
        timeGroup = new ButtonGroup();
        timeGroup.add(timeAMPM);
        timeGroup.add(time24HR);
        setLayout(new MigLayout("", "12[]12", "12[]6[]6[]12"));
        add(timeAMPM, "wrap");
        add(time24HR, "wrap");
    }

    void load() {
        if (CalendarPrefs.getHourDisplay() == CalendarPrefs.HOUR_DISPLAY_24HR) {
            time24HR.setSelected(true);
        } else {
            timeAMPM.setSelected(true);
        }
    }

    void store() {
        if (time24HR.isSelected()) {
            CalendarPrefs.setHourDisplay(CalendarPrefs.HOUR_DISPLAY_24HR);
        } else {
            CalendarPrefs.setHourDisplay(CalendarPrefs.HOUR_DISPLAY_AMPM);
        }
    }

    boolean valid() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        controller.changed();
    }

}

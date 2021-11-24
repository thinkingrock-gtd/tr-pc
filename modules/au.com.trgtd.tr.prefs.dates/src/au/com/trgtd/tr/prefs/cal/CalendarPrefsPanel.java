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

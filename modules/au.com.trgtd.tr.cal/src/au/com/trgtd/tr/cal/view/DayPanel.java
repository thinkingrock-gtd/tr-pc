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

    public DayPanel(AllDayPanel allDayPanel, DayTimePanel dayTimePanel, DayGridPanel dayGridPanel) {
        this.dayGridPanel = dayGridPanel;
        setOpaque(false);
        setLayout(new MigLayout("fill", "0[40!]0[grow]0", "0[grow]3[grow]0"));
        add(allDayPanel, "skip, grow, wrap");
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

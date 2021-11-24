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
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Month day panel.
 */
public final class MonthDayPanel extends JPanel {

    private final static JLabel DUMMY_LABEL = new JLabel("Dummy");
    private final static int LABEL_HEIGHT = DUMMY_LABEL.getPreferredSize().height;
    private final List<CalEvent> events;

    public MonthDayPanel(List<CalEvent> events) {
        this.events = events;
        this.setOpaque(false);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
                update();
            }
            @Override
            public void componentShown(ComponentEvent ce) {
                update();
            }
        });
    }

    private synchronized void update() {
        removeAll();

        if (events == null || getHeight() == 0) {
            return;
        }

        final int rows = getHeight() / LABEL_HEIGHT;

        setLayout(new GridLayout(rows, 1, 0, 0));

        if (rows < events.size()) {
            for (int i = 0; i < rows - 1; i++) {
                if (i < events.size()) {
                    add(new EventLabel(events.get(i)));
                }
            }
            add(getMoreLabel(1 + events.size() - rows));
        } else {
            for (CalEvent event : events) {
                add(new EventLabel(event));
            }
        }

        revalidate();
        repaint();
    }

    private JLabel getMoreLabel(int n) {
        JLabel label = new JLabel("  " + n + " more...");
        label.setPreferredSize(DUMMY_LABEL.getPreferredSize());
        label.setFont(MonthPanel.Fonts.MORE);
        label.setForeground(Color.LIGHT_GRAY.darker());
        return label;
    }
}

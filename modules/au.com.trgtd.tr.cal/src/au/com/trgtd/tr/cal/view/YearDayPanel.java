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
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.utils.Fonts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Year day panel.
 */
public final class YearDayPanel extends JPanel {

    public interface Colors {
        Color TEXT_NORMAL = Color.LIGHT_GRAY.darker();
        Color TEXT_TODAY = Color.BLUE;

        Color BG_NORMAL = Color.WHITE;
        Color BG_HAS_EVENT = Color.ORANGE;
        Color BG_MONTH_TITLES = Color.LIGHT_GRAY;
    }
    private final static DateFormat DF = new SimpleDateFormat("d");
    private final Map<Date, List<CalEvent>> eventMap;
    private final Date date;
    private final JLabel label;

    public YearDayPanel(Map<Date, List<CalEvent>> eventMap, Date date) {
        this.eventMap = eventMap;
        this.date = DateUtils.clearTime(date);
        this.label = new JLabel(DF.format(date), JLabel.CENTER);
        this.label.setFont(Fonts.DAY_NUMBER);
        this.setOpaque(true);
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
//        this.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent ce) {
//                update();
//            }
//            @Override
//            public void componentShown(ComponentEvent ce) {
//                update();
//            }
//        });
        initColors();
    }

    private synchronized void initColors() {
        // background color
        if (hasEvents()) {
            setBackground(Colors.BG_HAS_EVENT);
        } else {
            setBackground(Colors.BG_NORMAL);
        }

        // text color
        if (DateUtils.isToday(date)) {
            label.setForeground(Colors.TEXT_TODAY);
        } else {
            label.setForeground(Colors.TEXT_NORMAL);
        }

//      revalidate();
//      repaint();
    }
    
//    private synchronized void update() {
//        // background color
//        if (hasEvents()) {
//            setBackground(Colors.BG_HAS_EVENT);
//        } else {
//            setBackground(Colors.BG_NORMAL);
//        }
//
//        // text color
//        if (DateUtils.isToday(date)) {
//            label.setForeground(Colors.TEXT_TODAY);
//        } else {
//            label.setForeground(Colors.TEXT_NORMAL);
//        }
//
//        revalidate();
//        repaint();
//    }

    private boolean hasEvents() {
        return eventMap.containsKey(date);
    }

}

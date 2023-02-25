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
package au.com.trgtd.tr.view.topics;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tr.model.topic.Topic;

/**
 * List cell renderer for topics combo box.
 *
 * @author Jeremy Moore
 */
public class TopicsListCellRenderer extends JLabel implements ListCellRenderer {

//    public TopicsListCellRenderer() {
//        setOpaque(true);
//    }
//
//    public Component getListCellRendererComponent(JList list, Object value,
//            int index, boolean isSelected, boolean cellHasFocus) {
//
//        if (value instanceof Topic) {
//            Topic topic = (Topic) value;
//            setText(topic.getName());
//            setFont(list.getFont());
//            if (isSelected) {
//                setBackground(list.getSelectionBackground());
//                setForeground(list.getSelectionForeground());
//            } else {
//                setBackground(topic.getBackground());
//                setForeground(topic.getForeground());
//            }
//        } else {
//            setText(value.toString());
//            setFont(list.getFont());
//            if (isSelected) {
//                setBackground(list.getSelectionBackground());
//                setForeground(list.getSelectionForeground());
//            } else {
//                setBackground(list.getBackground());
//                setForeground(list.getForeground());
//            }
//        }
//        return this;
//    }
    private final ListCellRenderer std;

    public TopicsListCellRenderer(ListCellRenderer std) {
        if (std == null) {
            throw new NullPointerException("Standard renderer is null.");
        }
        this.std = std;
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        Component c = std.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (!isSelected) {
            if (value instanceof Topic topic) {
                ((JLabel)c).setText(topic.getName());
                c.setBackground(topic.getBackground());
                c.setForeground(topic.getForeground());
            }
        }
        return c;
    }
}





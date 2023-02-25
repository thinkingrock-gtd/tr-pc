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
package au.com.trgtd.tr.view.topics.screen;

import au.com.trgtd.tr.resource.Icons;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.awt.Component;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import tr.model.topic.Topic;

/**
 * Topics table format.
 *
 * @author Jeremy Moore
 */
public class TopicsTableFormat implements AdvancedTableFormat<Topic> {
    
    private static final Class clazz = TopicsTableFormat.class;
    private static final String NAME = NbBundle.getMessage(clazz, "Topic.Name");
    private static final String DESC = NbBundle.getMessage(clazz, "Topic.Description");
    
    public int getColumnCount() {
        return 3;
    }
    
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "";
            case 1: return NAME;
            case 2: return DESC;
        }
        throw new IllegalStateException();
    }
    
    public Object getColumnValue(Topic topic, int column) {
        switch (column) {
            case 0: return Icons.Topic;
            case 1: return new TopicName(topic);
            case 2: return new TopicDesc(topic);
        }
        throw new IllegalStateException();
    }
    
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: return Icon.class;
            case 1: return TopicName.class;
            case 2: return TopicDesc.class;
        }
        throw new IllegalStateException();
    }
    
    public Comparator getColumnComparator(int column) {
        switch(column) {
            case 0: return null;
            case 1: return new TopicNameComparator();
            case 2: return new TopicDescComparator();
        }
        throw new IllegalStateException();
    }
    
    /*
     * Get the preferred column widths. Uses 10 times more than desired to force
     * proportions and not absolute widths.
     */
    public int getColumnWidth(int column) {
        switch (column) {
            case 0: return 20;
            case 1: return 2000;
            case 2: return 4000;
        }
        throw new IllegalStateException();
    }
    
    /*
     * Determines whether each column should be resizable.
     */
    public boolean isResizable(int column) {
        switch (column) {
            case 0: return false;
            case 1: return true;
            case 2: return true;
        }
        throw new IllegalStateException();
    }
    
    public static class TopicName {
        public final Topic topic;
        public TopicName(Topic topic) {
            this.topic = topic;
        }
    }
    
    public static class TopicNameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            TopicName t1 = (TopicName)o1;
            TopicName t2 = (TopicName)o2;
            return t1.topic.compareTo(t2.topic);
        }
    }
    
    public static class TopicNameRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int col) {
            
            JLabel label = new JLabel("");
            label.setOpaque(true);
            if (value instanceof TopicName td) {
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(td.topic.getBackground());
                    label.setForeground(td.topic.getForeground());
                }
                label.setText(td.topic.getName());
            }
            return label;
        }
    }
    
    public static class TopicDesc {
        public final Topic topic;
        public TopicDesc(Topic topic) {
            this.topic = topic;
        }
    }
    
    public static class TopicDescComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            TopicDesc t1 = (TopicDesc)o1;
            TopicDesc t2 = (TopicDesc)o2;
            return t1.topic.getDescription().compareToIgnoreCase(t2.topic.getDescription());
        }
    }
    
    public static class TopicDescRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus,
                int row, int col) {
            
            JLabel label = new JLabel("");
            label.setOpaque(true);
            if (value instanceof TopicDesc td) {
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(td.topic.getBackground());
                    label.setForeground(td.topic.getForeground());
                }
                label.setText(td.topic.getDescription());
            }
            return label;
        }
    }
    
}

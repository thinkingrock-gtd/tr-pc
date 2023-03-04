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
package au.com.trgtd.tr.view.reference.linker;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.awt.Color;
import java.awt.Component;
import java.util.Comparator;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import tr.model.information.Information;
import tr.model.topic.Topic;

/**
 * Reference chooser table format.
 *
 * @author Jeremy Moore
 */
class RefChooserTableFormat implements AdvancedTableFormat<Information> {
    
    private static Class clazz = RefChooserTableFormat.class;
    
    public int getColumnCount() {
        return 2;
    }
    
    public String getColumnName(int column) {
        switch (column) {
            case 0: return NbBundle.getMessage(clazz, "ref.descr");
            case 1: return NbBundle.getMessage(clazz, "ref.topic");
        }
        throw new IllegalStateException();
    }
    
    public Object getColumnValue(Information info, int column) {
        switch (column) {
            case 0: {
                Color bg = info.getTopic().getBackground();
                Color fg = info.getTopic().getForeground();
                return new ColoredString(info.getDescription(), bg, fg);
            }
            case 1: {
                return info.getTopic();
            }
        }
        throw new IllegalStateException();
    }
    
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: return ColoredString.class;
            case 1: return Topic.class;
        }
        throw new IllegalStateException();
    }
    
    public Comparator getColumnComparator(int column) {
        switch (column) {
            case 0: return new ColoredStringComparator();
            case 1: return new TopicComparator();
        }
        throw new IllegalStateException();
    }
    
    /*
     * Get the preferred column widths. Uses 10 times more than desired to force
     * proportions and not absolute widths.
     */
    public int getColumnWidth(int column) {
        switch (column) {
            case 0: return 460;
            case 1: return 140;
        }
        throw new IllegalStateException();
    }
    
    /*
     * Determines whether each column should be resizable.
     */
    public boolean isResizable(int column) {
        switch (column) {
            case 0: return true;
            case 1: return true;
        }
        throw new IllegalStateException();
    }
    
    public static class ColoredString {
        public final String string;
        public final Color bg;
        public final Color fg;
        public ColoredString(String string, Color bg, Color fg) {
            this.string = string;
            this.fg = fg;
            this.bg = bg;
        }
    }
    
    public static class ColoredStringComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = ((ColoredString)o1).string;
            String s2 = ((ColoredString)o2).string;
            return s1.compareToIgnoreCase(s2);
        }
    }
    
    public static class TopicComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Topic t1 = (Topic)o1;
            Topic t2 = (Topic)o2;
            return t1.compareTo(t2);
        }
    }
       
    public static class ColoredStringRenderer extends DefaultTableCellRenderer {        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            
            JLabel label = new JLabel("");
            label.setOpaque(true);
            
            if (value instanceof ColoredString cs) {
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(cs.bg);
                    label.setForeground(cs.fg);
                }
                label.setText(cs.string);
            }
            
            return label;
        }
    }
    
    public static class TopicRenderer extends DefaultTableCellRenderer {        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            
            JLabel label = new JLabel("");
            
            label.setOpaque(true);
            
            if (value instanceof Topic topic) {
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(topic.getBackground());
                    label.setForeground(topic.getForeground());
                }
                label.setText(topic.getName());
            }
            
            return label;
        }
    }

}

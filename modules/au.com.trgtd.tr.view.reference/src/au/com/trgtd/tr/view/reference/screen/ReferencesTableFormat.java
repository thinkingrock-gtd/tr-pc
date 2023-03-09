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
package au.com.trgtd.tr.view.reference.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.Constants;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import tr.model.information.Information;
import tr.model.topic.Topic;
import au.com.trgtd.tr.util.DateUtils;

/**
 * Reference table format.
 *
 * @author Jeremy Moore
 */
public class ReferencesTableFormat implements AdvancedTableFormat<Information> {
    
    private static Class clazz = ReferencesTableFormat.class;
    
    public int getColumnCount() {
        return 4;
    }
    
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "";
            case 1: return NbBundle.getMessage(clazz, "Reference.created"); // NOI18N;
            case 2: return NbBundle.getMessage(clazz, "Reference.description"); // NOI18N;
            case 3: return NbBundle.getMessage(clazz, "Reference.topic"); // NOI18N;
        }
        throw new IllegalStateException();
    }
    
    public Object getColumnValue(Information baseObject, int column) {
        
        Information info = baseObject;
        
        Color bg = info.getTopic().getBackground();
        Color fg = info.getTopic().getForeground();
        
        switch (column) {
            case 0: return Icons.Reference;
            case 1: return new ColoredDate(info.getCreated(), bg, fg);
            case 2: return new ColoredString(info.getDescription(), bg, fg);
            case 3: return info.getTopic();
        }
        throw new IllegalStateException();
    }
    
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: return Icon.class;
            case 1: return ColoredDate.class;
            case 2: return ColoredString.class;
            case 3: return Topic.class;
        }
        throw new IllegalStateException();
    }
    
    public Comparator getColumnComparator(int column) {
        switch (column) {
            case 0: return null;
            case 1: return new ColoredDateComparator();
            case 2: return new ColoredStringComparator();
            case 3: return new TopicComparator();
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
            case 1: return 1400;
            case 2: return 4000;
            case 3: return 1000;
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
            case 3: return true;
        }
        throw new IllegalStateException();
    }
    
    public static class ColoredDate {
        public final Date date;
        public final Color bg;
        public final Color fg;
        public ColoredDate(Date date, Color bg, Color fg) {
            this.date = date;
            this.fg = fg;
            this.bg = bg;
        }
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
    
//    public class ReferenceComparator implements Comparator {
//        public int compare(Object o1, Object o2) {
//            Information i1 = (Information)o1;
//            Information i2 = (Information)o2;
//            return i1.getDescription().compareToIgnoreCase(i2.getDescription());
//        }
//    }
    
    public static class ColoredDateComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Date date1 = DateUtils.clearTime(((ColoredDate)o1).date);
            Date date2 = DateUtils.clearTime(((ColoredDate)o2).date);
            return date1.compareTo(date2);
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
    
    public static class ColoredDateRenderer extends DefaultTableCellRenderer {
        
        private final DateFormat df = Constants.DATE_FORMAT_FIXED;
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            
            JLabel label = new JLabel("");
            label.setOpaque(true);
            
            if (value instanceof ColoredDate cd) {
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(cd.bg);
                    label.setForeground(cd.fg);
                }
                label.setText(df.format(cd.date));
            }
            
            return label;
        }
    }
    
    public static class ColoredStringRenderer extends DefaultTableCellRenderer {
        
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

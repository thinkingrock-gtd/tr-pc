/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
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
class RefChooserTableFormat implements AdvancedTableFormat {
    
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
    
    public Object getColumnValue(Object baseObject, int column) {       
        Information info = (Information)baseObject;        
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
            
            if (value instanceof ColoredString) {
                ColoredString cs = (ColoredString)value;
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
            
            if (value instanceof Topic) {
                Topic topic = (Topic)value;
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

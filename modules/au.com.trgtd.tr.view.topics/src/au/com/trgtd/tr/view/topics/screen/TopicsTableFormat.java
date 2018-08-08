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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
            if (value instanceof TopicName) {
                TopicName td = (TopicName)value;
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
            if (value instanceof TopicDesc) {
                TopicDesc td = (TopicDesc)value;
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

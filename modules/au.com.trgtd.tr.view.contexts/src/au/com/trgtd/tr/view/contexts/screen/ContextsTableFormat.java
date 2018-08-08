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

package au.com.trgtd.tr.view.contexts.screen;

import au.com.trgtd.tr.resource.Icons;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.awt.Component;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.openide.util.NbBundle;
import tr.model.context.Context;

/**
 * Contexts table format.
 *
 * @author Jeremy Moore
 */
public class ContextsTableFormat implements AdvancedTableFormat<Context> {
    
    private static final Class clazz = ContextsTableFormat.class;
    private static final String NAME = NbBundle.getMessage(clazz, "Context.Name");
    private static final String DESC = NbBundle.getMessage(clazz, "Context.Description");
    
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
    
    public Object getColumnValue(Context context, int column) {
        switch (column) {
            case 0: return Icons.Context;
            case 1: return context;
            case 2: return context.getDescription();
        }
        throw new IllegalStateException();
    }
    
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: return Icon.class;
            case 1: return Context.class;
            case 2: return String.class;
        }
        throw new IllegalStateException();
    }
    
    public Comparator getColumnComparator(int column) {
        switch (column) {
            case 0: return null;
            case 1: return new ContextComparator();
            case 2: return new StringComparator();
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
    
    public static class StringComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = (String)o1;
            String s2 = (String)o2;
            return s1.compareToIgnoreCase(s2);
        }
    }
    
    public static class ContextComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Context c1 = (Context)o1;
            Context c2 = (Context)o2;
            return c1.compareTo(c2);
        }
    }
    
    public static class ContextRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel label = new JLabel("");
            label.setOpaque(true);
            if (value instanceof Context) {
                Context context = (Context)value;
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(table.getBackground());
                    label.setForeground(table.getForeground());
                }
                label.setText(context.getName());
            }
            return label;
        }
    }
}

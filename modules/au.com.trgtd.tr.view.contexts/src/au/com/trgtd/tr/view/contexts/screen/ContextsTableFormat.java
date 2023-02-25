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
            if (value instanceof Context context) {
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

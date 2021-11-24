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
package au.com.trgtd.tr.view.actns.screens.prefs;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import tr.model.util.Manager;

/**
 * Table model for a actions views.
 */
public class ActionsScreensOptionsTableModel extends AbstractTableModel implements Observer {
    
    /**
     * Creates a new instance for the given data model.
     * @param criterion The data model.
     */
    public ActionsScreensOptionsTableModel(Manager<ActionsScreensItem> items) {
        super();
        this.items = items;
        this.items.addObserver(this);
    }
    
    /**
     * Return the number of columns in the data model.
     */
    public int getColumnCount() {
        return CLASSES.length;
    }
    
    /**
     * Return the column class.
     * @param column The table column.
     */
    public Class getColumnClass(int column) {
        return CLASSES[column];
    }
    
    /**
     * Return the name of the given column in the data model.
     * @param column The table column.
     */
    public String getColumnName(int column) {
        return HEADINGS[column];
    }
    
    /**
     * Return the number of rows in the data model.
     */
    public int getRowCount() {
        return items.size();
    }
    
    /**
     * Gets the data model value for a particular table cell.
     * @param row The table row.
     * @param column The table column.
     * @return The value for the cell.
     */
    public Object getValueAt(int row, int column) {
        return (column == 0) ? items.get(row).name : "";
    }
    
    /**
     * Determines whether a particular cell is editable.
     * @param row The table row.
     * @param column The table column.
     * @return true iff the cell is editable.
     */
    public boolean isCellEditable(int row, int column) {
        return column == 0;
    }
    
    /**
     * Sets the value for a particular cell.
     * @param row The table row.
     * @param column The table column.
     */
    public void setValueAt(Object value, int row, int column) {
        if (column == 0 && value instanceof String) {
            items.get(row).name = (String)value;
        }
    }
    
    public synchronized boolean moveDown(int row) {
        if (row < 0 || row > items.size() - 2) {
            return false;
        }
        ActionsScreensItem view = items.get(row);
        items.set(row, items.get(row + 1));
        items.set(row + 1, view);
        return true;
    }
    
    public synchronized boolean moveUp(int row) {
        if (row < 1 || row > items.size() - 1) {
            return false;
        }
        
        ActionsScreensItem view = items.get(row);
        items.set(row, items.get(row - 1));
        items.set(row - 1, view);
        return true;
    }
    
    public synchronized boolean removeRow(int row) {
        if (row < 0 || row > items.size() - 1) {
            return false;
        }
        return items.remove(items.get(row));
    }
    
    public synchronized boolean addRow() {
        return items.add(new ActionsScreensItem("...", null));
    }
    
    /** Handle criteria changes. */
    public void update(Observable observable, Object arg) {
        fireTableDataChanged();
    }
    
    /** Column headings */
    public static final String[] HEADINGS = { org.openide.util.NbBundle.getMessage(ActionsScreensOptionsTableModel.class, "views") };    
    /** Column classes */
    public static final Class[] CLASSES = { String.class };    
    /** Column cell renderers */
    public static final TableCellRenderer[] RENDERERS = { null };    
    /** Column cell editors */
    public static final TableCellEditor[] EDITORS = { null };    
    /** Column cell maximum widths */
    public static final int[] MAX_WIDTHS = { -1 };    
    /** Column cell minimum widths */
    public static final int[] MIN_WIDTHS = { -1 };    
    /** Column cell preferred widths */
    public static final int[] PREF_WIDTHS = { -1 };
    
    private final Manager<ActionsScreensItem> items;
    
}

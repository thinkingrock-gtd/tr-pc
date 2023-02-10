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
package au.com.trgtd.tr.view.actns.screens;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import java.util.logging.Level;

/**
 * Table model for the columns dialog.
 */
public class ColumnsTableModel extends AbstractTableModel implements Observer {
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /**
     * Creates a new instance for the given action screen definition.
     * @param screendefe The action screen definition.
     */
    public ColumnsTableModel(ActionsScreen actionsScreen) {
        super();
        this.actionsScreen = actionsScreen;
        this.actionsScreen.addObserver(this);
    }
    
    /**
     * Gets the table cell renderer corresponding to the used column.
     * @return The table cell renderer.
     */
    public TableCellRenderer getTableCellRenderer(int column) {
        return renderers[column];
    }
    
    /**
     * Gets the maximum width corresponding to the used column.
     * @return The maximum column width.
     */
    public int getMaximumWidth(int column) {
        return maxwidths[column];
    }
    
    /**
     * Gets the minimum width corresponding to the used column.
     * @return The minimum column width.
     */
    public int getMinimumWidth(int column) {
        return minwidths[column];
    }
    
    /**
     * Gets the preferred width corresponding to the used column.
     * @return The preferred column width.
     */
    public int getPreferredWidth(int column) {
        return prewidths[column];
    }
    
    /** Return the number of columns in the data model. */
    public int getColumnCount() {
        return classes.length;
    }
    
    /** Return the name of the given column in the data model. */
    @Override
    public String getColumnName(int column) {
        return headings[column];
    }
    
    /** Return the column class. */
    @Override
    public Class<?> getColumnClass(int column) {
        return classes[column];
    }
    
    /** Return the number of rows in the data model. */
    public int getRowCount() {
//        return actionsScreen.getColumns().size();
        return ActionsColumn.COLUMN_INDICES.length;
    }
    
    private Map<Byte, ActionsColumn> rowMap;
    
    private Map<Byte, ActionsColumn> getRowMap() {
        if (rowMap == null) {
            rowMap = new HashMap<>();
            for (ActionsColumn ac : actionsScreen.getColumns().list()) {
                rowMap.put(ac.getColumnIndex(), ac);
            }
            for (byte index : ActionsColumn.COLUMN_INDICES) {
                if (!rowMap.containsKey(index)) {
                    ActionsColumn ac = ActionsColumn.createColumn(index);
                    actionsScreen.getColumns().add(ac);
                    rowMap.put(index, ac);
                }
            }
        }
        return rowMap;
    }
    
    /** Gets the data model value at the specified table row and column. */
    public Object getValueAt(int row, int column) {
        
        byte index = ActionsColumn.COLUMN_INDICES[row];
        
        ActionsColumn ac = getRowMap().get(index);
        if (ac == null) return "";
        
        switch (column) {
            case (COLUMN_VISIBLE): return Boolean.valueOf(ac.isVisible());
            case (COLUMN_COLUMN) : return ac.toString();
            default: return "";
        }
    }
    
    /** Sets the data model value at the specified table row and column. */
    @Override
    public void setValueAt(Object object, int row, int column) {
        LOG.log(Level.FINE, "SetValueAt: row = {0} col = {1}", new Object[]{row, column});
        
        if (column == COLUMN_VISIBLE && object instanceof Boolean) {
            
            boolean visible = ((Boolean)object).booleanValue();
            byte index = ActionsColumn.COLUMN_INDICES[row];
            
            // Description column must be visible - must have one or more.
            if (index == ActionsColumn.INDEX_DESCR && !visible) {
                return;
            }
            
            ActionsColumn actionsColumn = getRowMap().get(index);
            if (actionsColumn != null) {
                actionsColumn.setVisible(visible);
//              fireTableDataChanged();
            }
            
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == COLUMN_VISIBLE;
    }
    
    public void update(Observable o, Object arg) {
        fireTableDataChanged();
    }
    
    private static String getText(String key) {
        return NbBundle.getMessage(ColumnsTableModel.class, key);
    }
    
    private static final int[] maxwidths = { -1, -1 };
    private static final int[] minwidths = { -1, 50 };
    private static final int[] prewidths = { -1, 70 };
    private static final Class[] classes = { String.class, Boolean.class };
    private static final TableCellRenderer[] renderers = { null, null };
    private static final String[] headings = {
        getText("ColumnsDialog.column.column"),
        getText("ColumnsDialog.column.visible"),
    };
    
    private static final byte COLUMN_COLUMN = 0;
    private static final byte COLUMN_VISIBLE = 1;
    
    private final ActionsScreen actionsScreen;
    
}

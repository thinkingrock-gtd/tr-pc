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
package au.com.trgtd.tr.view.collect.screen;

import au.com.trgtd.tr.resource.Icons;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.swing.StyledString;
import au.com.trgtd.tr.swing.StyledStringRenderer;

/**
 * Table model for thoughts.
 */
public class CollectThoughtsTableModel extends AbstractTableModel implements Observer {

    /** Column headings */
    public static final String[] HEADINGS = {
        "", 
        NbBundle.getMessage(CollectThoughtsTableModel.class, "description"), 
        NbBundle.getMessage(CollectThoughtsTableModel.class, "topic")
    };
    /** Column classes */
    public static final Class[] CLASSES = {Icon.class, StyledString.class, Topic.class};
    /** Column cell maximum widths */
    public static final int[] MAX_WIDTHS = {17, -1, 400};
    /** Column cell minimum widths */
    public static final int[] MIN_WIDTHS = {17, -1, -1};
    /** Column cell preferred widths */
    public static final int[] PREF_WIDTHS = {17, -1, 250};
    /** Column cell renderers */
    public TableCellRenderer[] renderers = {null, new StyledStringRenderer(), new StyledStringRenderer()};
    private final Manager<Thought> thoughtManager;
    private List<Thought> unprocessedThoughts;
    private Thought thought;
    private Topic topic;
    private StyledString ss;

    /**
     * Creates a new instance for the given data model.
     * @param data The data model.
     */
    public CollectThoughtsTableModel(Data data) {
        super();
        thoughtManager = data.getThoughtManager();
        thoughtManager.addObserver(this);
        initModel();
    }

    private void initModel() {
        unprocessedThoughts = new Vector<>();
        for (Thought t : thoughtManager.list()) {
            if (!t.isProcessed()) {
                unprocessedThoughts.add(t);
            }
        }
    }

    /**
     * Gets the thought for a given table row.
     * @param row The row.
     * @return The thought corresponding to the row.
     */
    public Thought getValueAt(int row) {
        return unprocessedThoughts.get(row);
    }

    /**
     * Return the number of columns in the data model.
     */
    public int getColumnCount() {
        return CLASSES.length;
    }

    /**
     * Return the name of the given column in the data model.
     * @param column The table column.
     */
    @Override
    public String getColumnName(int column) {
        return HEADINGS[column];
    }

    /**
     * Return the number of rows in the data model.
     */
    public int getRowCount() {
        return unprocessedThoughts.size();
    }

    /**
     * Return the column class.
     * @param column The table column.
     */
    @Override
    public Class getColumnClass(int column) {
        return CLASSES[column];
    }

    /**
     * Gets the data model value for a particular table cell.
     * @param row The table row.
     * @param column The table column.
     * @return The value for the cell.
     */
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return Icons.Thought;
            case 1:
                thought = unprocessedThoughts.get(row);
                topic = thought.getTopic();
                ss = new StyledString();
                ss.setString(thought.getDescription());
                ss.setForeground(topic.getForeground());
                ss.setBackground(topic.getBackground());
                return ss;
            case 2:
                thought = unprocessedThoughts.get(row);
                topic = thought.getTopic();
                ss = new StyledString();
                ss.setString(topic.getName());
                ss.setBackground(topic.getBackground());
                ss.setForeground(topic.getForeground());
                return ss;
            default:
                return null;
        }
    }

    /**
     * Determines whether a particular cell is editable.
     * @param row The table row.
     * @param column The table column.
     * @return false.
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void stopObserving() {
        thoughtManager.removeObserver(this);
    }

    public void startObserving() {
        thoughtManager.addObserver(this);
    }

    /**
     * Handle thought manager changes.
     */
    public void update(Observable o, Object arg) {
        initModel();
        fireTableDataChanged();
    }
}

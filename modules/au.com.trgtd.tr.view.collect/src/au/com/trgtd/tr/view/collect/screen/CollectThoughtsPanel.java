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

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.thought.Thought;
import au.com.trgtd.tr.view.collect.dialog.ThoughtDialog;
import java.util.Collection;
import java.util.Vector;

/**
 * Collect thoughts panel.
 *
 * @author Jeremy Moore
 */
public class CollectThoughtsPanel extends JPanel {
    
    /**
     * Creates a new instance for the given thought node provider.
     */
    public CollectThoughtsPanel(ThoughtNodeProvider thoughtProvider) {
        super();
        this.thoughtProvider = thoughtProvider;
        data = DataLookup.instance().lookup(Data.class);
        tableModel = new CollectThoughtsTableModel(data);
        initComponents();
    }
    
    private void initComponents() {
        initTable();
        initBodyPanel();
        initPanel();
    }
    
    private void initTable() {
        table = new JXTable(tableModel);
        table.getActionMap().remove("find");
        table.setColumnControlVisible(true);
        table.setSortable(false);
        table.setEnabled(true);
        table.setFocusable(true);
        table.setShowGrid(true);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
//      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    modify();
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume();
                    delete();
                }
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modify();
                }
            }
        });
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                rowSelectionChanged();
            }
        });
        for (int i = 0; i < CollectThoughtsTableModel.CLASSES.length; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            if (tableModel.renderers[i] != null) {
                tc.setCellRenderer(tableModel.renderers[i]);
            }
            if (CollectThoughtsTableModel.MAX_WIDTHS[i] > -1) {
                tc.setMaxWidth(CollectThoughtsTableModel.MAX_WIDTHS[i]);
            }
            if (CollectThoughtsTableModel.MIN_WIDTHS[i] > -1) {
                tc.setMinWidth(CollectThoughtsTableModel.MIN_WIDTHS[i]);
            }
            if (CollectThoughtsTableModel.PREF_WIDTHS[i] > -1) {
                tc.setPreferredWidth(CollectThoughtsTableModel.PREF_WIDTHS[i]);
            }
        }
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(null);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(5);
        tableScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
    }
    
    private void rowSelectionChanged() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow < 0) {
//            thoughtProvider.provide(null);
//            return;
//        }
//        Thought thought = tableModel.getValueAt(selectedRow);
//        if (thought == null) {
//            thoughtProvider.provide(null);
//            return;
//        }
//        thoughtProvider.provide(new ThoughtNode(data.getThoughtManager(), thought));

        Collection<ThoughtNode> thoughtNodes = new Vector<>();

        for (int row : table.getSelectedRows()) {
            Thought thought = tableModel.getValueAt(row);
            if (thought != null) {
                thoughtNodes.add(new ThoughtNode(data.getThoughtManager(), thought));
            }
        }
        thoughtProvider.provide(thoughtNodes);
    }
    
    private void initBodyPanel() {
        bodyPanel = new JPanel();
        bodyPanel.setLayout(new BorderLayout());
        bodyPanel.add(tableScrollPane, BorderLayout.CENTER);
    }
    
    private void initPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        add(bodyPanel, BorderLayout.CENTER);
    }
    
    public void create() {
        table.clearSelection();
        getThoughtDialog().showCreateDialog();
    }
    
    public void modify() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows == null || selectedRows.length != 1) {
            return;
        }
        Thought thought = tableModel.getValueAt(selectedRows[0]);
        if (thought == null) {
            return;
        }
        getThoughtDialog().showModifyDialog(thought);
        
        selectRow(selectedRows[0]);
    }
    
    private ThoughtDialog getThoughtDialog() {
// Changed since Java bug causes lost default button and no focus indication on
// GUI components after dispose() and subsequent show().
// See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6199625
//        if (thoughtDialog == null) {
//            Component location = (Component)toolbar;
//            thoughtDialog = new ThoughtDialog(null, location, data);
//        }
//        return thoughtDialog;
// Instead create a new dialog every time - probably gobbles up memory.
        thoughtDialog = new ThoughtDialog(data);
        return thoughtDialog;
    }
    
//    public void delete() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow < 0) return;
//
//        Thought thought = tableModel.getValueAt(selectedRow);
//        if (thought == null) return;
//
//        data.getThoughtManager().remove(thought);
//
//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException ex) {
//        }
//
//        int rowCount = table.getRowCount();
//        if (selectedRow < rowCount) {
//            selectRow(selectedRow);
//        } else if (rowCount > 0) {
//            selectRow(rowCount - 1);
//        }
//    }

    public void delete() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows == null || selectedRows.length == 0) {
            return;
        }
        if (selectedRows.length == 1) {
            delete(selectedRows[0]);
        } else {
            delete(selectedRows);
        }
    }

    public void delete(int selectedRow) {

        tableModel.stopObserving();

        data.getThoughtManager().remove(tableModel.getValueAt(selectedRow));

        tableModel.startObserving();

//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException ex) {
//        }

        tableModel.update(null, null);

        int rowCount = table.getRowCount();
        if (selectedRow < rowCount) {
            selectRow(selectedRow);
        } else if (rowCount > 0) {
            selectRow(rowCount - 1);
        }
    }

    public void delete(int[] selectedRows) {
        Vector<Thought> thoughts = new Vector<>();
        for (int row : table.getSelectedRows()) {
            thoughts.add(tableModel.getValueAt(row));
        }

        tableModel.stopObserving();

        for (Thought thought : thoughts) {
            data.getThoughtManager().remove(thought);
        }

        tableModel.startObserving();

        tableModel.update(null, null);
    }
    
    private void selectRow(final int row) {
        if (row < 0 || row >= table.getRowCount()) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            table.requestFocusInWindow();
            table.changeSelection(row, 0, false, false);
            table.scrollRowToVisible(row);
        });
    }

    public void select(Thought thought) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (thought.equals(tableModel.getValueAt(row))) {
                selectRow(row);
                return;
            }
        }
    }

    private final Data data;
    private final ThoughtNodeProvider thoughtProvider;
    private final CollectThoughtsTableModel tableModel;
    private JPanel bodyPanel;
    private JScrollPane tableScrollPane;
    private JXTable table;
    private ThoughtDialog thoughtDialog;

}

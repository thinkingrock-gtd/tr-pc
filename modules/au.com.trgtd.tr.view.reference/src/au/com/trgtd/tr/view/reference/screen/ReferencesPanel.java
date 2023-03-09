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

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.util.concurrent.Lock;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.information.Information;
import tr.model.topic.Topic;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.reference.ReferenceDeleteAction;
import au.com.trgtd.tr.view.reference.ReferenceEditAction;
import java.util.ArrayList;

/**
 * Panel for referenced information table.
 *
 * @author Jeremy Moore
 */
public class ReferencesPanel extends JPanel implements ListSelectionListener, Observer {
    
    /**
     * Creates a new instance for the given reference provider and filters.
     */
    public ReferencesPanel(ReferenceNodeProvider refsProvider, MatcherEditor<Information> matcherEditor) {
        super();
        this.refsProvider = refsProvider;
        this.refsMatcherEditor = matcherEditor;
        this.data = DataLookup.instance().lookup(Data.class);
        initComponents();
    }
    
    private void initComponents() {
        initTable();
        initPanel();
    }
    
    private void initTable() {
        if (data == null) return;
        
        refsList = data.getInformationManager().list();
        
        refsEventList = new BasicEventList<>();
        refsEventList.addAll(refsList);
        
        FilterList<Information> refsFilterList = new FilterList<>(refsEventList, refsMatcherEditor);
        
        data.getInformationManager().addObserver(this);
        
        refsSortedList = new SortedList<>(refsFilterList);
        refsTableFormat = new ReferencesTableFormat();
        refsTableModel = new EventTableModel<>(refsSortedList, refsTableFormat);
        
        refsTable = new JXTable(refsTableModel);
        refsTable.getTableHeader().setDefaultRenderer(new JTableHeader().getDefaultRenderer());
        refsTable.setAutoCreateRowSorter(false);
        refsTable.setRowSorter(null);
        refsTable.setSortable(false);
        refsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refsTable.setColumnControlVisible(true);
        refsTable.setColumnSelectionAllowed(false);
        refsTable.setCellSelectionEnabled(false);
        refsTable.setRowSelectionAllowed(true);
        refsTable.setShowGrid(false);
        refsTable.setShowHorizontalLines(false);
        refsTable.setShowVerticalLines(false);
//      refsTable.setGridColor(Color.lightGray);

        refsTable.getActionMap().remove("find");

        selectionModel = new EventSelectionModel<>(refsSortedList);
        selectionModel.addListSelectionListener(this);
        selectionModel.setSelectionMode(EventSelectionModel.SINGLE_SELECTION);
        selectionModel.setEnabled(true);
        refsTable.setSelectionModel(selectionModel);
        
        for (int c = 0; c < refsTableFormat.getColumnCount(); c++) {
            refsTable.getColumnModel().getColumn(c).setPreferredWidth(refsTableFormat.getColumnWidth(c));
        }
        
        refsTable.setDefaultRenderer(ReferencesTableFormat.ColoredDate.class,
                new ReferencesTableFormat.ColoredDateRenderer());
        refsTable.setDefaultRenderer(ReferencesTableFormat.ColoredString.class,
                new ReferencesTableFormat.ColoredStringRenderer());
        refsTable.setDefaultRenderer(Topic.class,
                new ReferencesTableFormat.TopicRenderer());
        
        tableSorter = new TableComparatorChooser<>(refsTable, refsSortedList, true);
        
        refsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    edit();
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume();
                    delete();
                }
            }
        });
        refsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                }
            }
        });
    }
    
    private void initPanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(refsTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /** Edit the selected item. */
    private void edit() {
        SystemAction.get(ReferenceEditAction.class).actionPerformed(null);
    }
    
    /** Delete the selected item. */
    private void delete() {
        SystemAction.get(ReferenceDeleteAction.class).actionPerformed(null);
    }
    
    /**
     * Handle table selection change by calling provider for the new selection.
     * @param e The list selection event.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        
        Collection<ReferenceNode> nodes = new ArrayList<>();
        for (Information info : selectionModel.getSelected()) {
            nodes.add(new ReferenceNode(info));
        }
        
        refsProvider.provide(nodes);
    }
    
    /** Handle information add, remove and change events. */
    @Override
    public void update(final Observable observable, final Object object) {
        if (object instanceof Manager.EventAdd event) {
            Lock lock = refsEventList.getReadWriteLock().writeLock();
            lock.lock();
            refsEventList.add((Information)event.item);
            lock.unlock();
        } else if (object instanceof Manager.EventRemove event) {
            Lock lock = refsEventList.getReadWriteLock().writeLock();
            lock.lock();
            refsEventList.remove(event.item);
            lock.unlock();
        } else if (observable instanceof Information information) {
            if (object == Information.Change.NOTES) return;
            
            Lock lock = refsEventList.getReadWriteLock().writeLock();
            lock.lock();
            Information item = information;
            int index = refsEventList.indexOf(item);
            if (index > -1) {
                refsEventList.set(index, item);
            }
            lock.unlock();
        }
        refsTable.invalidate();
        refsTable.repaint();
    }
    
    /** Preferences for the panel. */
    static class Preferences implements Serializable {
        private static final long serialVersionUID = 943043L;
        private final int[] modelIndex;
        private final int[] columnWidth;
        private final boolean[] columnVisible;
        private final int[] columnOrder;
        private final List<Integer> sortingColumns;
        private final List<Boolean> sortingReverse;
        // constructor saves preferences
        private Preferences(ReferencesPanel panel) {
            JXTable table = panel.refsTable;
            this.modelIndex = new int[table.getColumnCount(true)];
            this.columnWidth = new int[table.getColumnCount(true)];
            this.columnVisible = new boolean[table.getColumnCount(true)];
            this.columnOrder = new int[table.getColumnCount(false)];
            // save column widths and visibility:
            List<TableColumn> columns = table.getColumns(true);
            for (int i = 0; i < columns.size(); i++) {
                TableColumn c = columns.get(i);
                modelIndex[i] = c.getModelIndex();
                columnWidth[i] = c.getWidth();
                columnVisible[i] = table.getColumnExt(c.getIdentifier()).isVisible();
            }
            // save column order:
            for (int i = 0; i < table.getColumnCount(); i++) {
                columnOrder[i] = table.getColumn(i).getModelIndex();
            }
            // save column sorting:
            sortingColumns = panel.tableSorter.getSortingColumns();
            sortingReverse = new ArrayList<>();
            for (Integer column : sortingColumns) {
                sortingReverse.add(panel.tableSorter.isColumnReverse(column));
            }
        }
        
        private void restore(ReferencesPanel panel) {
            if (panel == null) return;
            if (panel.refsTable == null) return;
            if (panel.refsTableFormat == null) return;
            // restore column widths and visibility:
            for (int i = 0; i < modelIndex.length; i++) {
                String id = panel.refsTableFormat.getColumnName(modelIndex[i]);
                TableColumnExt tce = panel.refsTable.getColumnExt(id);
                if (panel.refsTableFormat.isResizable(modelIndex[i])) {
                    tce.setPreferredWidth(columnWidth[i]);
                } else {
                    int w = panel.refsTableFormat.getColumnWidth(modelIndex[i]);
                    tce.setMaxWidth(w);
                    tce.setMinWidth(w);
                }
                tce.setVisible(columnVisible[i]);
            }
            // restore column order:
            Object[] ids = new Object[columnOrder.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = panel.refsTableFormat.getColumnName(columnOrder[i]);
            }
            panel.refsTable.setColumnSequence(ids);
            // restore column sorting:
            panel.tableSorter.clearComparator();
            for (int i = 0; i < sortingColumns.size(); i++) {
                int column = sortingColumns.get(i);
                boolean reverse = sortingReverse.get(i);
                panel.tableSorter.appendComparator(column, 0, reverse);
            }
        }
    }

    public void select(Information reference) {
        selectionModel.clearSelection();
        int row = refsSortedList.indexOf(reference);
        if (row > -1) {
            selectionModel.setSelectionInterval(row, row);
            refsTable.scrollRowToVisible(row);
        }
    }

    public Preferences getPreferences() {
        return new Preferences(this);
    }

    public void setPreferences(Preferences preferences) {
        if (preferences != null) {
            preferences.restore(this);
        }
    }
    
    private final static Logger LOG = Logger.getLogger("tr.view.reference");
    
    private final ReferenceNodeProvider refsProvider;
    private final MatcherEditor<Information> refsMatcherEditor;
    private final Data data;
    
    private JXTable refsTable;
    private ReferencesTableFormat refsTableFormat;
    private EventTableModel<Information> refsTableModel;
    private Collection<Information> refsList;
    private EventList<Information> refsEventList;
    private EventSelectionModel<Information> selectionModel;
    private SortedList<Information> refsSortedList;
    private TableComparatorChooser<Information> tableSorter;
}

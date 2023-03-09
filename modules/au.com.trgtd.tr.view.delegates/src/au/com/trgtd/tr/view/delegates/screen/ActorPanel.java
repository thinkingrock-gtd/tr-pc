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
package au.com.trgtd.tr.view.delegates.screen;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.util.concurrent.Lock;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.actor.Actor;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.actors.ActorDialog;
import au.com.trgtd.tr.view.delegates.DeleteActorAction;
import au.com.trgtd.tr.view.delegates.EditActorAction;
import java.util.Comparator;

/**
 * Actor panel.
 *
 * @author Jeremy Moore
 */
public class ActorPanel extends JPanel implements ListSelectionListener, Observer {
    
    /**
     * Creates a new instance for the given actor node provider.
     * @param actorProvider
     */
    public ActorPanel(ActorNodeProvider actorProvider) {
        super();
        this.actorProvider = actorProvider;
        this.data = DataLookup.instance().lookup(Data.class);
        initTable();
        initPanel();
    }
    
    private void initTable() {
        if (data == null) return;
        
        actorList = data.getActorManager().list();
        actorEventList = new BasicEventList<>();
        actorEventList.addAll(actorList);
        
        data.getActorManager().addObserver(this);
        
        actorSortedList = new SortedList<>(actorEventList, actorComparator);
        actorSortedList.setMode(SortedList.AVOID_MOVING_ELEMENTS);
        
        actorTableFormat = new ActorsTableFormat();
        actorEventTableModel = new EventTableModel<>(actorSortedList, actorTableFormat);
        
        actorTable = new JXTable(actorEventTableModel);
        actorTable.getActionMap().remove("find");
        actorTable.getTableHeader().setDefaultRenderer(new JTableHeader().getDefaultRenderer());
        actorTable.setAutoCreateRowSorter(false);
        actorTable.setRowSorter(null);
        actorTable.setSortable(false);
        actorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actorTable.setColumnControlVisible(true);
        actorTable.setColumnSelectionAllowed(false);
        actorTable.setCellSelectionEnabled(false);
        actorTable.setRowSelectionAllowed(true);
        actorTable.setShowGrid(false);
        actorTable.setShowHorizontalLines(false);
        actorTable.setShowVerticalLines(false);
//      actorTable.setGridColor(Color.lightGray);
        
        selectionModel = new EventSelectionModel<>(actorSortedList);
        selectionModel.addListSelectionListener(this);
        selectionModel.setSelectionMode(EventSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectionModel.setEnabled(true);
        actorTable.setSelectionModel(selectionModel);
        
        for (int c = 0; c < actorTableFormat.getColumnCount(); c++) {
            actorTable.getColumnModel().getColumn(c).setPreferredWidth(actorTableFormat.getColumnWidth(c));
        }
        
        tableSorter = new TableComparatorChooser<>(actorTable, actorSortedList, true);

        actorTable.addKeyListener(new KeyAdapter() {
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
        actorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                }
            }
        });
    }
    
    /** Preferences for the actor panel. */
    static class Preferences implements Serializable {
        private static final long serialVersionUID = 6587787564L;
        private final int[] modelIndex;
        private final int[] columnWidth;
        private final boolean[] columnVisible;
        private final int[] columnOrder;
        private final List<Integer> sortingColumns;
        private final List<Boolean> sortingReverse;
        // constructor saves preferences
        private Preferences(ActorPanel panel) {
            JXTable table = panel.actorTable;
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
            sortingReverse = new Vector<>();
            for (Integer column : sortingColumns) {
                sortingReverse.add(new Boolean(panel.tableSorter.isColumnReverse(column)));
            }
        }
        
        private void restore(ActorPanel panel) {
            if (panel == null) return;
            if (panel.actorTable == null) return;
            if (panel.actorTableFormat == null) return;
            // restore column widths and visibility:
            for (int i = 0; i < modelIndex.length; i++) {
                String id = panel.actorTableFormat.getColumnName(modelIndex[i]);
                TableColumnExt tce = panel.actorTable.getColumnExt(id);
                if (panel.actorTableFormat.isResizable(modelIndex[i])) {
                    tce.setPreferredWidth(columnWidth[i]);
                } else {
                    int w = panel.actorTableFormat.getColumnWidth(modelIndex[i]);
                    tce.setMaxWidth(w);
                    tce.setMinWidth(w);
                }
                tce.setVisible(columnVisible[i]);
            }
            // restore column order:
            Object[] ids = new Object[columnOrder.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = panel.actorTableFormat.getColumnName(columnOrder[i]);
            }
            panel.actorTable.setColumnSequence(ids);
            // restore column sorting:
            panel.tableSorter.clearComparator();
            for (int i = 0; i < sortingColumns.size(); i++) {
                int column = sortingColumns.get(i);
                boolean reverse = sortingReverse.get(i);
                panel.tableSorter.appendComparator(column, 0, reverse);
            }
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
    
    private void initPanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(actorTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /** Edit the selected actor. */
    private void edit() {
        SystemAction.get(EditActorAction.class).actionPerformed(null);
    }
    
    /** Delete the selected actor. */
    private void delete() {
        SystemAction.get(DeleteActorAction.class).actionPerformed(null);
    }
    
    /** Get a actor dialog. */
    private ActorDialog getActorDialog() {
        actorDialog = new ActorDialog(data);
        return actorDialog;
    }
    
    /**
     * Handle table selection change by calling actor provider for the new
     * selection.
     * @param e The list selection event.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        
        Collection<ActorNode> nodes = new Vector<>();
        for (Actor actor : selectionModel.getSelected()) {
            nodes.add(new ActorNode(data.getActorManager(), actor));
        }
        actorProvider.provide(nodes);
    }
    
    /**
     * Handle actor manager add and remove events.
     */
    @Override
    public void update(final Observable observable, final Object object) {
        if (object instanceof Manager.EventAdd event) {
            Lock lock = actorEventList.getReadWriteLock().writeLock();
            lock.lock();
            actorEventList.add((Actor)event.item);
            lock.unlock();
        } else if (object instanceof Manager.EventRemove event) {
            Lock lock = actorEventList.getReadWriteLock().writeLock();
            lock.lock();
            actorEventList.remove(event.item);
            lock.unlock();
        } else if (observable instanceof Actor actor) {
            Lock lock = actorEventList.getReadWriteLock().writeLock();
            lock.lock();
            Actor item = actor;
            int index = actorEventList.indexOf(item);
            if (index > -1) {
                actorEventList.set(index, item);
            }
            lock.unlock();
        }
        actorTable.invalidate();
        actorTable.repaint();
    }
    
    private final Data data;
    private final ActorNodeProvider actorProvider;
    private JXTable actorTable;
    private ActorsTableFormat actorTableFormat;
    private Collection<Actor> actorList;
    private EventList<Actor> actorEventList;
    private EventSelectionModel<Actor> selectionModel;
    private EventTableModel<Actor> actorEventTableModel;
    private SortedList<Actor> actorSortedList;
    private TableComparatorChooser<Actor> tableSorter;
    private ActorDialog actorDialog;

    private final Comparator<Actor> actorComparator = (Actor a1, Actor a2) ->
            a1.getName().compareToIgnoreCase(a2.getName());

}

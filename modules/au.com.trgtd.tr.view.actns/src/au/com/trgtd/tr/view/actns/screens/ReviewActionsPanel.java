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

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.swing.StyledString;
import au.com.trgtd.tr.swing.StyledStringRenderer;
import au.com.trgtd.tr.util.LazyNotifier;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.DeleteAction;
import au.com.trgtd.tr.view.actns.RAProjectsTreeTopComponent;
import au.com.trgtd.tr.view.actns.RASingleActionsTopComponent;
import au.com.trgtd.tr.view.actns.prefs.ActionsPrefs;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDone;
import au.com.trgtd.tr.view.collect.AddThoughtsAction;
import au.com.trgtd.tr.view.projects.ProjectsTreeTopComponent;
import au.com.trgtd.tr.view.projects.actions.SingleActionsTopComponent;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.util.concurrent.Lock;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingx.JXTable;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Review actions panel.
 *
 * @author Jeremy Moore
 */
public class ReviewActionsPanel extends JPanel implements ListSelectionListener, Observer {

    /** Creates new form ReviewActionsPanel */
    public ReviewActionsPanel(ActionsScreen screen,
            ItemCountShower itemCountShower,
            ReviewActionsFilters filters,
            ReviewActionsTopComponent tcReviewActions,
            ActionsProvider actionsProvider) {

        this.screen = screen;
        this.itemCountShower = itemCountShower;
        this.filters = filters;
        this.tcReviewActions = tcReviewActions;
        this.actionsProvider = actionsProvider;
        data = DataLookup.instance().lookup(Data.class);
        tcSingleActions = RASingleActionsTopComponent.findInstance();
        tcProjectsTree = RAProjectsTreeTopComponent.findInstance();
        initComponents();
        selectionNotifier = new LazyNotifier(ActionsPrefs.getViewDelay()) {
            public void changed() {
                view();
            }
        };
    }

    public void stopObserving() {
        Services.instance.removeActionsObserver(this);
    }

    public void startObserving() {
        Services.instance.addActionsObserver(this);
    }
    

    public void takeFocus() {
        if (table != null) {
            table.requestFocusInWindow();
        }
    }

    private void initTable() {
        if (data == null) {
            return;
        }

        actionsList = Services.instance.getAllActions();

        actionsEventList = new BasicEventList<>();
        actionsEventList.addAll(actionsList);
        actionsFilterList = new FilterList<>(actionsEventList, filters.getMatcherEditor());
        actionsFilterList.addListEventListener((ListEvent<Action> e) -> {
            itemCountShower.showItemCount(e.getSourceList().size());
            // fix problem of number of selected rows increasing by clearing
            selectionModel.clearSelection();
        });

        itemCountShower.showItemCount(actionsFilterList.size());

////////        Services.instance.addActionsObserver(this);

        actionsSortedList = new SortedList<>(actionsFilterList);

        actionsTableFormat = new ReviewActionsTableFormat(screen);

        actionsTableModel = new EventTableModel<>(actionsSortedList, actionsTableFormat);

//        actionsTable = new JXTable(actionsTableModel);
//        actionsTable.getTableHeader().setDefaultRenderer(new JTableHeader().getDefaultRenderer());
        actionsTable = new JXTable();
        actionsTable.getActionMap().remove("find");
        actionsTable.setModel(actionsTableModel);

        actionsTable.setAutoCreateRowSorter(false);
        actionsTable.setRowSorter(null);
        actionsTable.setSortable(false);

//      actionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actionsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        actionsTable.setColumnControlVisible(false);
        actionsTable.setColumnSelectionAllowed(false);
        actionsTable.setCellSelectionEnabled(false);
        actionsTable.setRowSelectionAllowed(true);
        actionsTable.setShowGrid(false);
        actionsTable.setShowHorizontalLines(false);
        actionsTable.setShowVerticalLines(false);
//      actionsTable.setGridColor(Color.lightGray);

        selectionModel = new EventSelectionModel<>(actionsSortedList);

        selectionModel.addListSelectionListener(this);

//      selectionModel.setSelectionMode(EventSelectionModel.SINGLE_SELECTION);
        selectionModel.setSelectionMode(EventSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        selectionModel.setEnabled(true);
        actionsTable.setSelectionModel(selectionModel);

        // set column information
        for (int c = 0; c < actionsTableFormat.getColumnCount(); c++) {
            TableColumn tc = actionsTable.getColumnModel().getColumn(c);
            tc.setIdentifier(actionsTableFormat.getID(c));
            tc.setResizable(actionsTableFormat.isResizable(c));
            tc.setMinWidth(actionsTableFormat.getMinWidth(c));
            tc.setMaxWidth(actionsTableFormat.getMaxWidth(c));
            tc.setPreferredWidth(actionsTableFormat.getWidth(c));
        }

        // set columns visible
        for (TableColumn tc : actionsTable.getColumns(true)) {
            Byte id = (Byte) tc.getIdentifier();
            ActionsColumn column = screen.getColumns().get(tc.getModelIndex());
            actionsTable.getColumnExt(id).setVisible(column.isVisible());
        }

        actionsTable.setDefaultRenderer(StyledString.class, new StyledStringRenderer());

        tableSorter = new TableComparatorChooser<>(actionsTable, actionsSortedList, true);

        actionsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getModifiers() != 0) {
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    edit();
                } else if (e.getKeyCode() == KeyEvent.VK_F6) {
                    e.consume();
                    addThoughts();
                } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    e.consume();
                    delete();
                }
            }
        });
        actionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                } else if (e.getClickCount() == 1) {
                    int c = actionsTable.getSelectedColumn();
                    if (c > -1) {
                        int i = actionsTable.getColumn(c).getModelIndex();
                        if (i == ActionsColumn.INDEX_DONE) {
                            toggleDone();
                        }
                    }
                }
            }
        });

        orderColumns();
        sortColumns();
    }
    
    private void toggleDone() {
        List<Action> selected = selectionModel.getSelected();
        if (selected == null || selected.size() != 1) {
            return;            
        }        
        Action action = selected.get(0);
        action.setDone(!action.isDone());
    }
    
    private void delete() {        
        List<Action> selected = selectionModel.getSelected();
        if (selected == null || selected.isEmpty()) {
            return;            
        }        
        Node[] nodes = new Node[selected.size()];        
        for (int i = 0; i < selected.size(); i++) {
            nodes[i] = new ActionNode(selected.get(i));
        }        
        SystemAction.get(DeleteAction.class).performAction(nodes);
//        SystemAction.get(DeleteAction.class).performAction();
    }
    
    private void addThoughts() {
        AddThoughtsAction action = SystemAction.get(AddThoughtsAction.class);
        if (action != null) {
            action.performAction();
        }
    }

    private FilterDone getDoneFilter() {
        MatcherEditor<Action> matcherEditor = filters.getMatcherEditor();
        if (matcherEditor instanceof CompositeMatcherEditor cme) {
            for (Object filter : cme.getMatcherEditors().toArray(Object[]::new)) {
                if (filter instanceof FilterDone filterDone) {
                    return filterDone;
                }
            }
        }
        return null;
    }

    private boolean isShowDone() {
        FilterDone doneFilter = getDoneFilter();

        return (doneFilter == null) ? true : doneFilter.isShowDone();
    }

    public JTable getTable() {
        initTable();
        return actionsTable;
    }

    public TableFormat getTableFormat() {
        return actionsTableFormat;
    }

    private void edit() {
        EventQueue.invokeLater(() -> {
            EventList<Action> selected = selectionModel.getSelected();
            if (selected.size() != 1) {
                return;
            }
            Action action = selected.get(0);

            if (action.isSingleAction()) {
                if (tcSingleActions != null) {
                    tcSingleActions.setShowDone(isShowDone());
                    tcSingleActions.edit(action);
                }
            } else {
                if (tcProjectsTree != null) {
                    tcProjectsTree.setShowDone(isShowDone());
                    tcProjectsTree.edit(action);
                }
            }
        });
    }

    public void view() {
        EventList<Action> selected = selectionModel.getSelected();
        if (selected.size() != 1) {
            return;
        }               
        
        Action action = selected.get(0);        
        
        if (action.isSingleAction()) {
            if (tcSingleActions != null) {
                tcSingleActions.setShowDone(isShowDone());
                tcSingleActions.select(action);
            }
        } else {
            if (tcProjectsTree != null) {
                tcProjectsTree.setShowDone(isShowDone());
                tcProjectsTree.select(action);
            }
        }
        // get focus back on table (esp. for MacOSX)
        EventQueue.invokeLater(() -> {
            table.requestFocusInWindow();
        });
    }

    // Order the columns.
    private void orderColumns() {
        assert (actionsTable != null);

        byte[] order = screen.getColumnOrder();

        assert (order != null);

        TableColumnModel tcm = actionsTable.getColumnModel();

        for (int i = 0; i < order.length; i++) {

            Byte id = new Byte(order[i]);

            int p = tcm.getColumnIndex(id);
            if (p > -1) {
                tcm.moveColumn(p, i);
            }
        }
    }

    // Sort the columns.
    private void sortColumns() {
        byte[] columns = screen.getSortColumns();
        byte[] status = screen.getSortStates();
        if (columns == null || status == null || columns.length != status.length) {
            return;
        }
        for (int i = 0; i < columns.length; i++) {
            try {
                tableSorter.appendComparator(columns[i], 0, ((status[i] == SORT_DESCENDING) ? true : false));
            } catch (Exception ex) {
            }
        }
    }

    /** Save the window state. */
    public void save() {

        // set visible
        for (TableColumn tc : actionsTable.getColumns(true)) {
            Byte id = (Byte) tc.getIdentifier();
            ActionsColumn column = screen.getColumns().get(tc.getModelIndex());
            column.setVisible(actionsTable.getColumnExt(id).isVisible());
        }

        TableColumnModel tcm = table.getColumnModel();
        TableColumn tc;

        // save column order and widths
        byte[] order = new byte[tcm.getColumnCount()];

        for (int i = 0; i < tcm.getColumnCount(); i++) {
            tc = tcm.getColumn(i);
            order[i] = (byte) tc.getModelIndex();
            screen.getColumns().get(tc.getModelIndex()).setWidth(tc.getWidth());
        }
        screen.setColumnOrder(order);

        // save sorted columns and sort order
        List<Integer> sortColumnsList = tableSorter.getSortingColumns();
        byte[] sortColumns = new byte[sortColumnsList.size()];
        byte[] sortStatus = new byte[sortColumnsList.size()];

        for (int i = 0; i < sortColumnsList.size(); i++) {
            sortColumns[i] = ActionsColumn.COLUMN_INDICES[sortColumnsList.get(i)];
            if (tableSorter.isColumnReverse(sortColumnsList.get(i))) {
                sortStatus[i] = SORT_DESCENDING;
            } else {
                sortStatus[i] = SORT_ASCENDING;
            }
        }
        screen.setSortColumns(sortColumns);
        screen.setSortStatus(sortStatus);
    }

    /**
     * Receiving a table row selection changed event. 
     * @param e The event
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        
        actionsProvider.provide(selectionModel.getSelected());        
        
        // hack to stop selection if window is not active
        // TODO: fix selection mechanism properly
        TopComponent tc = (TopComponent)itemCountShower;
        if (!tc.isShowing()) {
            return;
        }
        
        selectionNotifier.fire();
    }

    public void clearSelection() {
        selectionModel.removeListSelectionListener(this);
        selectionModel.clearSelection();
        selectionModel.addListSelectionListener(this);
    }

    /**
     * As an observer of single actions and projects (and their children) we are 
     * receiving notification of a change.
     * @param observable The action or project that has changed.
     * @param argument The change details argument.
     */
    public void update(final Observable observable, final Object argument) {
        if (argument == Action.FIELD.Notes) {
            return;            
        }
        EventQueue.invokeLater(() -> {
            if (observable instanceof Action observedAction) {
                for (Action selectedAction : selectionModel.getSelected()) {
                    if (selectedAction.getID() != observedAction.getID()) {
                        clearSelection();
                        break;
                    }
                }
                refresh();
                return;
            }
            if (observable instanceof Project obsProject) {
                clearSelection();
                if (argument == null) {
                    // change to project itself
                    actionsTable.validate();
                    actionsTable.repaint();
                    refresh();
                } else if (argument instanceof Action action) {
                    // added or removed action
                    Lock lock = actionsEventList.getReadWriteLock().writeLock();
                    lock.lock();
                    if (obsProject.contains(action)) {
                        actionsEventList.add(action);
                        lock.unlock();
                    } else {
                        actionsEventList.remove(action);
                        lock.unlock();
                        selectionModel.clearSelection();
                        actionsProvider.provide(Collections.EMPTY_LIST);
                    }
                } else if (argument instanceof Project argProject) {
                    // added or removed project
                    List<Action> actions = Services.instance.getActionDecendants(argProject);
                    if (actions == null || actions.isEmpty()) {
                        return;
                    }
                    Lock lock = actionsEventList.getReadWriteLock().writeLock();
                    lock.lock();
                    if (obsProject.contains(argProject)) { // added argProject
                        actionsEventList.addAll(actions);
                    } else { // removed argProject

                        actionsEventList.removeAll(actions);
                    }
                    lock.unlock();
                }
            }
        });
    }

    public void refresh() {

        final ListSelectionListener lsl = this;

        EventQueue.invokeLater(() -> {
            // remove selection listener to try and avoid the selected table
            // row from being re-selected and causing view() to be called
            // (which requests table focus).
            selectionModel.removeListSelectionListener(lsl);

            actionsSortedList.setComparator(actionsSortedList.getComparator());

            // TODO Find a better way to fire update on filters
            for (ActionsFilter filter : screen.getFilters()) {
                filter.getFilterCombo().fireValueChange();
            }

            // add back selection listener
            selectionModel.addListSelectionListener(lsl);
        });
    }

    public void refreshColumns() {
        if (actionsTable == null) {
            return;
        }
        for (TableColumn tc : actionsTable.getColumns(true)) {
            Byte id = (Byte) tc.getIdentifier();
            ActionsColumn column = screen.getColumns().get(tc.getModelIndex());
            actionsTable.getColumnExt(id).setVisible(column.isVisible());
        }
    }

    public void resetModel() {
        Lock lock = actionsEventList.getReadWriteLock().writeLock();
        lock.lock();
        actionsEventList.clear();
        actionsEventList.addAll(Services.instance.getAllActions());
        lock.unlock();
    }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        tableScrollPane = new javax.swing.JScrollPane();
        table = getTable();

        setLayout(new java.awt.BorderLayout());

        tableScrollPane.setViewportView(table);

        add(tableScrollPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
    public static final byte SORT_ASCENDING = 1;
    public static final byte SORT_DESCENDING = 2;
    private static SingleActionsTopComponent tcSingleActions;
    private static ProjectsTreeTopComponent tcProjectsTree;
    private final ReviewActionsTopComponent tcReviewActions;
    private final ActionsScreen screen;
    private final Data data;
    private final ItemCountShower itemCountShower;
    private final ReviewActionsFilters filters;
    private final LazyNotifier selectionNotifier;
    private final ActionsProvider actionsProvider;
    public JXTable actionsTable;
    private ReviewActionsTableFormat actionsTableFormat;
    private EventTableModel<Action> actionsTableModel;
    private Collection<Action> actionsList;
    private EventList<Action> actionsEventList;
    private FilterList<Action> actionsFilterList;
    public SortedList<Action> actionsSortedList;
    private EventSelectionModel<Action> selectionModel;
    private TableComparatorChooser<Action> tableSorter;
}

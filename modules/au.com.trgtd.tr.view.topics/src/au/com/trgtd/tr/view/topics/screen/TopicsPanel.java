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
import tr.model.topic.Topic;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.topics.DeleteTopicAction;
import au.com.trgtd.tr.view.topics.EditTopicAction;
import au.com.trgtd.tr.view.topics.dialog.TopicDialog;

/**
 * Topic panel.
 *
 * @author Jeremy Moore
 */
public class TopicsPanel extends JPanel implements ListSelectionListener, Observer {
    
    /**
     * Creates a new instance for the given topic node provider.
     */
    public TopicsPanel(TopicNodeProvider topicProvider) {
        super();
        this.topicProvider = topicProvider;
        this.data = (Data)DataLookup.instance().lookup(Data.class);
        initTable();
        initPanel();
    }
    
    private void initTable() {
        if (data == null) return;
        
        topicList = data.getTopicManager().list();
        topicEventList = new BasicEventList<Topic>();
        topicEventList.addAll(topicList);
        
        data.getTopicManager().addObserver(this);
        
        topicSortedList = new SortedList<Topic>(topicEventList);
        topicSortedList.setMode(SortedList.AVOID_MOVING_ELEMENTS);
        
        topicTableFormat = new TopicsTableFormat();
        topicEventTableModel = new EventTableModel<Topic>(topicSortedList, topicTableFormat);
        
        topicTable = new JXTable(topicEventTableModel);
        topicTable.getActionMap().remove("find");
        topicTable.getTableHeader().setDefaultRenderer(new JTableHeader().getDefaultRenderer());
        topicTable.getSelectionMapper().setEnabled(false);
        topicTable.setSortable(false);
        topicTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        topicTable.setColumnControlVisible(true);
        topicTable.setColumnSelectionAllowed(false);
        topicTable.setCellSelectionEnabled(false);
        topicTable.setRowSelectionAllowed(true);
        topicTable.setShowGrid(false);
        topicTable.setShowHorizontalLines(false);
        topicTable.setShowVerticalLines(false);
//      topicTable.setGridColor(Color.lightGray);
        
        selectionModel = new EventSelectionModel<Topic>(topicSortedList);
        selectionModel.addListSelectionListener(this);
        selectionModel.setSelectionMode(EventSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectionModel.setEnabled(true);
        topicTable.setSelectionModel(selectionModel);
        
        for (int c = 0; c < topicTableFormat.getColumnCount(); c++) {
            topicTable.getColumnModel().getColumn(c).setPreferredWidth(topicTableFormat.getColumnWidth(c));
        }
        
        topicTable.setDefaultRenderer(TopicsTableFormat.TopicName.class, new TopicsTableFormat.TopicNameRenderer());
        topicTable.setDefaultRenderer(TopicsTableFormat.TopicDesc.class, new TopicsTableFormat.TopicDescRenderer());
        
        tableSorter = new TableComparatorChooser<Topic>(topicTable, topicSortedList, true);
        
        topicTable.addKeyListener(new KeyAdapter() {
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
        topicTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    edit();
                }
            }
        });
    }
    
    /** Preferences for the topic panel. */
    static class Preferences implements Serializable {
        private static final long serialVersionUID = 8787983453L;
        private final int[] modelIndex;
        private final int[] columnWidth;
        private final boolean[] columnVisible;
        private final int[] columnOrder;
        private final List<Integer> sortingColumns;
        private final List<Boolean> sortingReverse;
        // constructor saves preferences
        private Preferences(TopicsPanel panel) {
            JXTable table = panel.topicTable;
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
            sortingReverse = new Vector<Boolean>();
            for (Integer column : sortingColumns) {
                sortingReverse.add(new Boolean(panel.tableSorter.isColumnReverse(column)));
            }
        }
        
        private void restore(TopicsPanel panel) {
            if (panel == null) return;
            if (panel.topicTable == null) return;
            if (panel.topicTableFormat == null) return;
            // restore column widths and visibility:
            for (int i = 0; i < modelIndex.length; i++) {
                String id = panel.topicTableFormat.getColumnName(modelIndex[i]);
                TableColumnExt tce = panel.topicTable.getColumnExt(id);
                if (panel.topicTableFormat.isResizable(modelIndex[i])) {
                    tce.setPreferredWidth(columnWidth[i]);
                } else {
                    int w = panel.topicTableFormat.getColumnWidth(modelIndex[i]);
                    tce.setMaxWidth(w);
                    tce.setMinWidth(w);
                }
                tce.setVisible(columnVisible[i]);
            }
            // restore column order:
            Object[] ids = new Object[columnOrder.length];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = panel.topicTableFormat.getColumnName(columnOrder[i]);
            }
            panel.topicTable.setColumnSequence(ids);
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
        scrollPane.setViewportView(topicTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /** Edit the selected topic. */
    private void edit() {
        SystemAction.get(EditTopicAction.class).actionPerformed(null);
    }
    
    /** Delete the selected topic. */
    private void delete() {
        SystemAction.get(DeleteTopicAction.class).actionPerformed(null);
    }
    
    /** Get a topic dialog. */
    private TopicDialog getTopicDialog() {
        topicDialog = new TopicDialog(data);
        return topicDialog;
    }
    
    /**
     * Handle table selection change by calling topic provider for the new
     * selection.
     * @param e The list selection event.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        
        Collection<TopicNode> nodes = new Vector<TopicNode>();
        for (Topic topic : selectionModel.getSelected()) {
            nodes.add(new TopicNode(data.getTopicManager(), topic));
        }
        topicProvider.provide(nodes);
    }
    
    /**
     * Handle topic manager add and remove events.
     */
    public void update(final Observable observable, final Object object) {
        if (object instanceof Manager.EventAdd) {
            Manager.EventAdd event = (Manager.EventAdd)object;
            Lock lock = topicEventList.getReadWriteLock().writeLock();
            lock.lock();
            topicEventList.add((Topic)event.item);
            lock.unlock();
        } else if (object instanceof Manager.EventRemove) {
            Manager.EventRemove event = (Manager.EventRemove)object;
            Lock lock = topicEventList.getReadWriteLock().writeLock();
            lock.lock();
            topicEventList.remove(event.item);
            lock.unlock();
        } else if (observable instanceof Topic) {
            Lock lock = topicEventList.getReadWriteLock().writeLock();
            lock.lock();
            Topic item = (Topic)observable;
            int index = topicEventList.indexOf(item);
            if (index > -1) {
                topicEventList.set(index, item);
            }
            lock.unlock();
        }
        topicTable.invalidate();
        topicTable.repaint();
    }
    
    private final Data data;
    private final TopicNodeProvider topicProvider;
    private JXTable topicTable;
    private TopicsTableFormat topicTableFormat;
    private Collection<Topic> topicList;
    private EventList<Topic> topicEventList;
    private EventSelectionModel<Topic> selectionModel;
    private EventTableModel<Topic> topicEventTableModel;
    private ListSelectionModel tableSelectionModel;
    private SortedList<Topic> topicSortedList;
    private TableComparatorChooser<Topic> tableSorter;
    private TopicDialog topicDialog;
    
}

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
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.reference.linker;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
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
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import org.jdesktop.swingx.JXTable;
import org.openide.DialogDescriptor;
import org.openide.awt.Toolbar;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import tr.model.DataLookup;
import tr.model.Data;
import tr.model.information.Information;
import tr.model.topic.Topic;

/**
 * Reference item chooser panel.
 *
 * @author Jeremy Moore
 */
class RefChooserPanel extends JPanel implements Observer {

    private Data data;
    private RefChooserFilters refsFilters;
    private MatcherEditor refsMatcherEditor;
    private JXTable refsTable;
    private RefChooserTableFormat refsTableFormat;
    private EventTableModel<Information> refsTableModel;
    private Collection<Information> refsList;
    private EventList<Information> refsEventList;
    private EventSelectionModel<Information> selectionModel;
    private SortedList<Information> refsSortedList;
    private TableComparatorChooser<Information> tableSorter;
    private Information choice;
    private JTextField searchTextField;
    private DialogDescriptor descriptor;

    /**
     * Constructs a new instance.
     */
    public RefChooserPanel() {
        construct();
    }

    private void construct() {
        data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            data.getInformationManager().addObserver(this);
        }
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                dataChanged();
            }
        });
        refsFilters = new RefChooserFilters();
        refsMatcherEditor = refsFilters.getMatcherEditor();
        searchTextField = (JTextField) refsFilters.matcherEditorSearch.getComponent();
        initTable();
        initPanel();
    }

    void setDescriptor(DialogDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    private void dataChanged() {
        if (data != null) {
            data.getInformationManager().removeObserver(this);
        }
        data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            data.getInformationManager().addObserver(this);
        }
        updataRefsTable();
    }

    public void update(Observable obs, Object arg) {
        updataRefsTable();
    }

    private void updataRefsTable() {
        Lock lock = refsEventList.getReadWriteLock().writeLock();
        lock.lock();
        refsEventList.clear();
        if (data != null) {
            refsEventList.addAll(data.getInformationManager().list());
        }
        lock.unlock();
        refsTable.revalidate();
        refsTable.repaint();
    }

    private void initTable() {
        refsList = (data == null ? new Vector<Information>() : data.getInformationManager().list());
        refsEventList = new BasicEventList<Information>();
        refsEventList.addAll(refsList);
        FilterList refsFilterList = new FilterList(refsEventList, refsMatcherEditor);
        refsSortedList = new SortedList<Information>(refsFilterList);
        refsTableFormat = new RefChooserTableFormat();
        refsTableModel = new EventTableModel<Information>(refsSortedList, refsTableFormat);
        refsTable = new JXTable(refsTableModel);
        refsTable.getTableHeader().setDefaultRenderer(new JTableHeader().getDefaultRenderer());
        refsTable.getSelectionMapper().setEnabled(false);
        refsTable.setSortable(false);
        refsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refsTable.setColumnControlVisible(true);
        refsTable.setColumnSelectionAllowed(false);
        refsTable.setCellSelectionEnabled(false);
        refsTable.setRowSelectionAllowed(true);
        refsTable.setShowGrid(false);
        refsTable.setShowHorizontalLines(false);
        refsTable.setShowVerticalLines(false);
        refsTable.setColumnControlVisible(false);
        refsTable.getActionMap().remove("find");
        selectionModel = new EventSelectionModel<Information>(refsSortedList);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (descriptor == null) {
                    return;
                }
                descriptor.setValid(selectionModel.getSelected().size() > 0);
                saveSelected();
            }
        });
        selectionModel.setSelectionMode(EventSelectionModel.SINGLE_SELECTION);
        selectionModel.setEnabled(true);
        refsTable.setSelectionModel(selectionModel);
        for (int c = 0; c < refsTableFormat.getColumnCount(); c++) {
            refsTable.getColumnModel().getColumn(c).setPreferredWidth(refsTableFormat.getColumnWidth(c));
        }
        refsTable.setDefaultRenderer(RefChooserTableFormat.ColoredString.class, new RefChooserTableFormat.ColoredStringRenderer());
        refsTable.setDefaultRenderer(Topic.class, new RefChooserTableFormat.TopicRenderer());
        tableSorter = TableComparatorChooser.install(refsTable, refsSortedList, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);
        tableSorter.appendComparator(0, 0, false);
        refsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    saveSelected();
                    activateOkButton();
                }
            }
        });
        refsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    e.consume();
                    saveSelected();
                    activateOkButton();
                }
            }
        });
    }

    private void activateOkButton() {
        JRootPane rp = getRootPane();
        if (rp == null) {
            return;
        }
        saveSelected();
        rp.getDefaultButton().doClick();
    }

    private JToolBar getToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setUI((new Toolbar()).getUI());
        toolbar.setMargin(new Insets(4, 4, 4, 4));
        toolbar.setFloatable(false);
        JLabel searchLabel = new JLabel();
        searchLabel.setBorder(new EmptyBorder(0, 0, 0, 2));
        searchLabel.setIcon(new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/reference/linker/search.png")));
        toolbar.add(searchLabel);
        toolbar.add(searchTextField);
        JLabel topicLabel = new JLabel(refsFilters.matcherEditorTopics.getLabel());
        topicLabel.setBorder(new EmptyBorder(0, 4, 0, 2));
        toolbar.add(topicLabel);
        toolbar.add(refsFilters.matcherEditorTopics.getComponent());
        return toolbar;
    }

    private void initPanel() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(refsTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(getToolbar(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void saveSelected() {
        EventList<Information> selected = selectionModel.getSelected();
        choice = selected.size() > 0 ? selected.get(0) : null;
    }

    public Information getSelected() {
        return choice;
    }

    public void reset() {
        selectionModel.clearSelection();
    }
    
}

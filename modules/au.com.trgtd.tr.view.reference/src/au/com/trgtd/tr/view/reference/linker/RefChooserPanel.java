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
    private MatcherEditor<Information> refsMatcherEditor;
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
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            dataChanged();
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
        refsList = (data == null ? new Vector<>() : data.getInformationManager().list());
        refsEventList = new BasicEventList<>();
        refsEventList.addAll(refsList);
        FilterList<Information> refsFilterList = new FilterList<>(refsEventList, refsMatcherEditor);
        refsSortedList = new SortedList<>(refsFilterList);
        refsTableFormat = new RefChooserTableFormat();
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
        refsTable.setColumnControlVisible(false);
        refsTable.getActionMap().remove("find");
        selectionModel = new EventSelectionModel<>(refsSortedList);
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (descriptor == null) {
                    return;
                }
                descriptor.setValid(!selectionModel.getSelected().isEmpty());
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
        choice = !selected.isEmpty() ? selected.get(0) : null;
    }

    public Information getSelected() {
        return choice;
    }

    public void reset() {
        selectionModel.clearSelection();
    }
    
}

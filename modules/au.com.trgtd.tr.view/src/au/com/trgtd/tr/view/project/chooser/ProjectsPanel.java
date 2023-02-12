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
package au.com.trgtd.tr.view.project.chooser;

import au.com.trgtd.tr.resource.Icons;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.project.Project;

/**
 * ProjectsPanel.java
 *
 * Created on November 5, 2008, 12:32 PM
 */
public class ProjectsPanel extends JPanel {

    private static final Class CLASS = ProjectsPanel.class;
    private List<ActionListener> actionListeners;
    private Map<Integer, Boolean> mapProjectSorts;    
    private Map<Integer, Boolean> mapFutureSorts;
    private final boolean currentOnly;
    
    /** Creates new form ProjectsPanel */
    public ProjectsPanel() {
        this(false);
    }

    /** Creates new form ProjectsPanel */
    public ProjectsPanel(boolean currentOnly) {
        this.currentOnly = currentOnly;
        initComponents();
        if (currentOnly) {
            tabbedPane.remove(1);
        }
        mapProjectSorts = new HashMap<>();
        mapFutureSorts = new HashMap<>();
    }

    private JToolBar getProjectsToolbar() {
        ToolBarButton expandButton = new ToolBarButton(Icons.ZoomIn);
        expandButton.setPreferredSize(new Dimension(18, 18));
        expandButton.addActionListener((ActionEvent arg0) -> {
            expand(projectsTree);
        });
        ToolBarButton collapseButton = new ToolBarButton(Icons.ZoomOut);
        collapseButton.setPreferredSize(new Dimension(18, 18));
        collapseButton.addActionListener((ActionEvent arg0) -> {
            collapse(projectsTree);
        });
        ToolBarButton sortAZButton = new ToolBarButton(Icons.SortAZ);
        sortAZButton.setPreferredSize(new Dimension(18, 18));
        sortAZButton.addActionListener((ActionEvent arg0) -> {
            sortAlphabetic(projectsTree, true, mapProjectSorts);
        });
        ToolBarButton sortZAButton = new ToolBarButton(Icons.SortZA);
        sortZAButton.setPreferredSize(new Dimension(18, 18));
        sortZAButton.addActionListener((ActionEvent arg0) -> {
            sortAlphabetic(projectsTree, false, mapProjectSorts);
        });
        sortAZButton.setToolTipText(NbBundle.getMessage(CLASS, "sort.ascending"));
        sortZAButton.setToolTipText(NbBundle.getMessage(CLASS, "sort.descending"));
        if (Utilities.isMac()) {
            expandButton.setToolTipText(NbBundle.getMessage(CLASS, "expand.tooltip") + " (⌘E)");            
            collapseButton.setToolTipText(NbBundle.getMessage(CLASS, "collapse.tooltip") + " (⌃⌘E)");            
        } else {
            expandButton.setToolTipText(NbBundle.getMessage(CLASS, "expand.tooltip") + " (Ctrl+E)");            
            collapseButton.setToolTipText(NbBundle.getMessage(CLASS, "collapse.tooltip") + " (Shift+Ctrl+E)");  
        }
        
        JToolBar tb = new JToolBar();
        tb.add(expandButton);
        tb.add(collapseButton);
        tb.add(sortAZButton);
        tb.add(sortZAButton);
        return tb;
    }

    private JToolBar getFutureToolbar() {
        ToolBarButton expandButton = new ToolBarButton(Icons.ZoomIn);
        expandButton.setPreferredSize(new Dimension(18, 18));
        expandButton.addActionListener((ActionEvent arg0) -> {
            expand(futureTree);
        });
        ToolBarButton collapseButton = new ToolBarButton(Icons.ZoomOut);
        collapseButton.setPreferredSize(new Dimension(18, 18));
        collapseButton.addActionListener((ActionEvent arg0) -> {
            collapse(futureTree);
        });
        ToolBarButton sortAZButton = new ToolBarButton(Icons.SortAZ);
        sortAZButton.setPreferredSize(new Dimension(18, 18));
        sortAZButton.addActionListener((ActionEvent arg0) -> {
            sortAlphabetic(futureTree, true, mapFutureSorts);
        });
        ToolBarButton sortZAButton = new ToolBarButton(Icons.SortZA);
        sortZAButton.setPreferredSize(new Dimension(18, 18));
        sortZAButton.addActionListener((ActionEvent arg0) -> {
            sortAlphabetic(futureTree, false, mapFutureSorts);
        });
        sortAZButton.setToolTipText(NbBundle.getMessage(CLASS, "sort.ascending"));
        sortZAButton.setToolTipText(NbBundle.getMessage(CLASS, "sort.descending"));        
        if (Utilities.isMac()) {
            expandButton.setToolTipText(NbBundle.getMessage(CLASS, "expand.tooltip") + " (⌘E)");            
            collapseButton.setToolTipText(NbBundle.getMessage(CLASS, "collapse.tooltip") + " (⌃⌘E)");            
        } else {
            expandButton.setToolTipText(NbBundle.getMessage(CLASS, "expand.tooltip") + " (Ctrl+E)");            
            collapseButton.setToolTipText(NbBundle.getMessage(CLASS, "collapse.tooltip") + " (Shift+Ctrl+E)");  
        }        
        JToolBar tb = new JToolBar();
        tb.add(expandButton);
        tb.add(collapseButton);
        tb.add(sortAZButton);
        tb.add(sortZAButton);
        return tb;
    }
    
    private void sortAlphabetic(JTree tree, boolean ascending, Map<Integer, Boolean> sortsMap) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        
//        List<DefaultMutableTreeNode> list = new Vector<>();
//        
//        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
//            list.add((DefaultMutableTreeNode)e.nextElement());
//        }
//        
//        Collections.sort(list, new NodeComparator(ascending));
//
//        node.removeAllChildren();
//        
//        for (DefaultMutableTreeNode n : list) {
//            node.add(n);            
//        }
//        
        sortAlphabetic(tree, node, ascending);
        
        ((DefaultTreeModel)tree.getModel()).reload(node); 
        
        tree.setSelectionPath(new TreePath(node.getPath()));
        
        // save sort
        Object object = node.getUserObject();
        if (object instanceof Project project) {
            sortsMap.put(project.getID(), ascending);        
        } else { 
            // assume root node
            sortsMap.put(0, ascending);        
        }        
    }
    
    private void sortAlphabetic(JTree tree, DefaultMutableTreeNode node, boolean ascending) {
        List<DefaultMutableTreeNode> list = new Vector<>();
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            list.add((DefaultMutableTreeNode)e.nextElement());
        }        
        Collections.sort(list, new NodeComparator(ascending));
        node.removeAllChildren();        
        for (DefaultMutableTreeNode n : list) {
            node.add(n);            
        }        
    }
    
    private void expand(JTree tree) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        expand(node, tree);
        
//      tree.setSelectionPath(new TreePath(node.getPath()));        
    }
    
    private void expand(DefaultMutableTreeNode node, JTree tree) {
        tree.expandPath(new TreePath(node.getPath()));

        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            expand((DefaultMutableTreeNode)e.nextElement(), tree);
        }
    }
    
    
    private void collapse(JTree tree) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }        
        collapse(node, tree);        

//      tree.setSelectionPath(new TreePath(node.getPath()));        
    }
    
    private void collapse(DefaultMutableTreeNode node, JTree tree) {
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            collapse((DefaultMutableTreeNode)e.nextElement(), tree);
        }
        tree.collapsePath(new TreePath(node.getPath()));
    }
        
    private class NodeComparator implements Comparator<DefaultMutableTreeNode> {
        private final boolean ascending;
        public NodeComparator(boolean ascending) {
            this.ascending = ascending;
        }
        public int compare(DefaultMutableTreeNode n1, DefaultMutableTreeNode n2) {
            Project p1 = (Project)n1.getUserObject();
            Project p2 = (Project)n2.getUserObject();
            if (ascending) {
                return p1.getDescription().compareToIgnoreCase(p2.getDescription());                  
            } else {
                return p2.getDescription().compareToIgnoreCase(p1.getDescription());                                  
            }
        }        
    }
    
    public void refreshModel() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }                
        DefaultMutableTreeNode top;
        top = (DefaultMutableTreeNode)projectsTree.getModel().getRoot();
        top.removeAllChildren();                
        createNodes(top, data.getRootProjects());                
        refreshSorts(mapProjectSorts, projectsTree);        
        ((DefaultTreeModel)projectsTree.getModel()).reload(top);

        top = (DefaultMutableTreeNode)futureTree.getModel().getRoot();
        top.removeAllChildren();        
        createNodes(top, data.getRootFutures());                
        refreshSorts(mapFutureSorts, futureTree);        
        ((DefaultTreeModel)futureTree.getModel()).reload(top);             
    }
    
    private void refreshSorts(Map<Integer, Boolean> map, JTree tree) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        DefaultMutableTreeNode node;        
        for (Integer id : map.keySet()) {
            if (id == 0) {
                sortAlphabetic(tree, root, map.get(id));
            } else {
                node = findNode(id, root);
                if (node != null) {
                    sortAlphabetic(tree, node, map.get(id));
                }
            }
        }        
    }
    
    private JTree getProjectsTree() {
//        Data data = DataLookup.instance().lookup(Data.class);
//        if (data == null) {
//            return null;
//        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(NbBundle.getMessage(CLASS, "projects"));
//      createNodes(top, data.getRootProjects());
        final JTree tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ProjectTreeCellRenderer(Icons.Project, Icons.ProjectOpen));
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setToggleClickCount(-1);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if (getSelected() != null) {
                        fireSelectedAction();
                        return;
                    }
                }                 
            }            
        });
        tree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    if (Utilities.isMac()) {
                        if (!e.isMetaDown()) {    
                           return;
                        }
                    } else {
                        if (!e.isControlDown()) {
                            return;
                        }
                    }
                    if (e.isShiftDown()) {
                        collapse(tree);
                    } else {
                        expand(tree);
                    }                    
                }                                    
            }            
        });
        return tree;
    }

    private JTree getFutureProjectsTree() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return null;
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(NbBundle.getMessage(CLASS, "future.projects"));
        createNodes(top, data.getRootFutures());
        final JTree tree = new JTree(top);
        tree.setCellRenderer(new ProjectTreeCellRenderer(Icons.ProjectFuture, Icons.ProjectFutureOpen));
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);        
        tree.setToggleClickCount(-1);
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    if (getSelected() != null) {
                        fireSelectedAction();
                        return;
                    }
                }                 
            }            
        });        
        tree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    if (Utilities.isMac()) {
                        if (!e.isMetaDown()) {    
                           return;
                        }
                    } else {
                        if (!e.isControlDown()) {
                            return;
                        }
                    }
                    if (e.isShiftDown()) {
                        collapse(tree);
                    } else {
                        expand(tree);
                    }                    
                }                                    
            }            
        });
        return tree;
    }

    private void createNodes(DefaultMutableTreeNode node, Project project) {
        for (Project subProject : project.getChildren(Project.class)) {
            if (!subProject.isDone()) {
                DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subProject);
                createNodes(subNode, subProject);
                node.add(subNode);                
            }
        }
    }

    public void setSelected(Project project) {
        if (project == null) {
            tabbedPane.setSelectedIndex(0);
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)projectsTree.getModel().getRoot();
            projectsTree.setSelectionPath(new TreePath(root.getPath()));                
            projectsTree.requestFocusInWindow();
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        JTree tree;        
        if (project.isWithin(data.getRootProjects())) {
            tree = projectsTree;
            tabbedPane.setSelectedIndex(0);
        } else if (project.isWithin(data.getRootFutures()) && !currentOnly) {
            tree = futureTree;            
            tabbedPane.setSelectedIndex(1);
        } else {
            return;
        }
        DefaultMutableTreeNode node = findNode(project.getID(), (DefaultMutableTreeNode)tree.getModel().getRoot());
        if (node == null) {
            return;
        }
        
        tree.setSelectionPath(new TreePath(node.getPath()));                
    }   
    
    private DefaultMutableTreeNode findNode(int projectID, DefaultMutableTreeNode node) {               
        Object object = node.getUserObject();
        if (object instanceof Project project) {
            if (project.getID() == projectID) {
                return node;
            }
        }
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            DefaultMutableTreeNode n = findNode(projectID, (DefaultMutableTreeNode)e.nextElement());
            if (n != null) {
                return n;
            }
        }        
        return null;
    }
    
    public Project getSelected() {        
        JTree tree;        
        if (tabbedPane.getSelectedIndex() == 0) {
            tree = projectsTree;
        } else if (tabbedPane.getSelectedIndex() == 1) {
            tree = futureTree;
        } else {
            return null;
        }        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (node == null) {
            return null;
        }        
        Object object = node.getUserObject();
        if (object instanceof Project project) {
            return project;
        }        
        return null;
    }

    public void addActionListener(ActionListener listener) {
        if (actionListeners == null) {
            actionListeners = new Vector<>();
        }
        if (actionListeners.contains(listener)) {
            return;
        }
        actionListeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        if (actionListeners == null) {
            return;
        }
        actionListeners.remove(listener);
    }    
    
    public void fireSelectedAction() {
        ActionEvent event = new ActionEvent(this, 0, "selected");        
        for (ActionListener l : actionListeners) {
            l.actionPerformed(event);
        }
    }    
    
    private class ToolBarButton extends JButton {
        public ToolBarButton(Icon icon) {
            super(icon);
            setMargin(new Insets(2, 2, 2, 2));
            setVerticalTextPosition(BOTTOM);
            setHorizontalTextPosition(CENTER);
        }
        public ToolBarButton(String imageFile) {
            this(new ImageIcon(imageFile));
        }
        public ToolBarButton(String imageFile, String text) {
            this(new ImageIcon(imageFile));
            setText(text);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        projectsPanel = new javax.swing.JPanel();
        projectsToolBar = getProjectsToolbar();
        projectsScrollPane = new javax.swing.JScrollPane();
        projectsTree = this.getProjectsTree();
        futurePanel = new javax.swing.JPanel();
        futureToolBar = getFutureToolbar();
        futureScrollPane = new javax.swing.JScrollPane();
        futureTree = this.getFutureProjectsTree();

        setPreferredSize(new java.awt.Dimension(400, 600));
        setLayout(new java.awt.BorderLayout());

        tabbedPane.setName(""); // NOI18N

        projectsPanel.setLayout(new java.awt.BorderLayout());

        projectsToolBar.setFloatable(false);
        projectsToolBar.setRollover(true);
        projectsToolBar.setPreferredSize(new java.awt.Dimension(100, 32));
        projectsPanel.add(projectsToolBar, java.awt.BorderLayout.PAGE_START);

        projectsScrollPane.setViewportView(projectsTree);

        projectsPanel.add(projectsScrollPane, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(ProjectsPanel.class, "ProjectsPanel.projectsPanel.TabConstraints.tabTitle"), projectsPanel); // NOI18N

        futurePanel.setLayout(new java.awt.BorderLayout());

        futureToolBar.setFloatable(false);
        futureToolBar.setRollover(true);
        futureToolBar.setMinimumSize(new java.awt.Dimension(24, 40));
        futureToolBar.setPreferredSize(new java.awt.Dimension(100, 32));
        futurePanel.add(futureToolBar, java.awt.BorderLayout.PAGE_START);

        futureScrollPane.setViewportView(futureTree);

        futurePanel.add(futureScrollPane, java.awt.BorderLayout.CENTER);

        tabbedPane.addTab(org.openide.util.NbBundle.getMessage(ProjectsPanel.class, "ProjectsPanel.futurePanel.TabConstraints.tabTitle"), futurePanel); // NOI18N

        add(tabbedPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel futurePanel;
    private javax.swing.JScrollPane futureScrollPane;
    private javax.swing.JToolBar futureToolBar;
    private javax.swing.JTree futureTree;
    private javax.swing.JPanel projectsPanel;
    private javax.swing.JScrollPane projectsScrollPane;
    private javax.swing.JToolBar projectsToolBar;
    private javax.swing.JTree projectsTree;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}

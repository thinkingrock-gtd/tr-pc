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
package au.com.trgtd.tr.view.projects;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;
import tr.model.Item.Doable;
import tr.model.Item.Item;
import tr.model.project.Project;
import tr.model.action.Action;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.projects.goals.ProjectGoalsNode;
import java.util.logging.Level;

/**
 * Children of a project node.
 */
public class ProjectChildren extends Children.Keys implements Observer, ChangeListener {

    private static final Logger LOG = Logger.getLogger("tr.view.projects");

    public final Project project;
    public final boolean showDone;
    public final ProjectGoalsNode goalsNode;

    private Index index;
    private boolean showGoals;

    public ProjectChildren(Project project, boolean showDone) {
        this.project = project;
        this.showDone = showDone;
        this.goalsNode = new ProjectGoalsNode(project);
    }

    private Collection<Object> getItems() {
        if (project == null) {
            return Collections.EMPTY_SET;
        }

        List<Object> items = new Vector<>();

        if (showGoals && !project.isRoot()) {
            items.add(goalsNode);
        }

        for (Item item : project.getChildren()) {
            if (showDone || !(item instanceof Doable) || !((Doable) item).isDone()) {
                items.add(item);
            }
        }

        return items;
    }

    protected Node[] createNodes(Object key) {
        if (key instanceof ProjectGoalsNode) {
            return new Node[]{goalsNode};
        }
        if (key instanceof Project project1) {
            return new Node[]{new ProjectNode(project1, showDone)};
        }
        if (key instanceof Action action) {
            return new Node[]{new ActionNode(action)};
        }
        return new Node[]{};
    }

    @Override
    protected void addNotify() {
        setKeys(getItems());

        if (project != null) {
            project.addObserver(this);
        }

        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        setKeys(Collections.EMPTY_SET);

        if (project != null) {
            project.removeObserver(this);
        }

        super.removeNotify();
    }

    /**
     * Handles the case where a child has been added to or removed from the
     * project data model, by refreshing the child keys. Other relevant model
     * changes are handled by ProjectNode and ActionNode.
     */
    public void update(Observable observable, Object arg) {
        if (arg != null && project.equals(observable)) {
            setKeys(getItems());
        }
    }

    public boolean isShowGoals() {
        return showGoals;
    }

    public void setShowGoals(boolean showGoals) {
        if (showGoals == this.showGoals) {
            return;
        }
        this.showGoals = showGoals;
        setKeys(getItems());
    }

    /**
     * Getter for the index that works with this children.
     * @return the index.
     */
    public Index getIndex() {
        if (index == null) {
            index = new IndexImpl(this);
            index.addChangeListener(WeakListeners.change(this, index));
        }
        return index;
    }

    public void stateChanged(ChangeEvent changeEvent) {
        setKeys(getItems());
    }

    protected ExplorerManager getExplorerManager() {
        return ProjectsTreeLookup.lookup().getExplorerManager();
    }

    private class IndexImpl extends Index.Support {

        private final ProjectChildren children;

        public IndexImpl(ProjectChildren children) {
            super();
            this.children = children;
        }

        public Node[] getNodes() {
            return children.getNodes();
        }

        public int getNodesCount() {
            return children.getNodesCount();
        }

        @Override
        public void reorder() {
            super.reorder();
            fireChangeEvent(new ChangeEvent(IndexImpl.this));
        }

        /**
         * Reorder for the given permutation. The permutation gives for every
         * node (nodes[0,1, ... (perm.length -1)]) the position to move it to.
         * e.g. for perm = {0,2,3,1}
         *      nodes[0] -> nodes[0]
         *      nodes[1] -> nodes[2]
         *      nodes[2] -> nodes[3]
         *      nodes[3] -> nodes[1]
         * Since the nodes may be either showing or hiding done items we first
         * get the items for the nodes of both the source and destination
         * positions. The items can then be reordered in the model by finding
         * the actual index (regardless of show/hide done). Thus if done items
         * are not shown and the order of nodes is changed, the done items will
         * be skipped over. This algorithm may have have efficiency problems for
         * large numbers of sub-nodes and it may be necessary to implement
         * moveDown and moveUp (see below) and if all else fails, implement a
         * faster method.
         */
        public synchronized void reorder(int[] perms) {

            final ExplorerManager explorerManager = getExplorerManager();

            final Node[] selectedNodes = explorerManager.getSelectedNodes();

            Node[] nodes = getNodes();

            if (nodes.length != perms.length) {
                LOG.info("Error number of nodes different in permutation.");
                return;
            }

            Vector<Item> srcItems = new Vector<>();
            for (int i = 0; i < perms.length; i++) {
                if (nodes[i] instanceof ProjectNode projectNode) {
                    srcItems.add(projectNode.project);
                } else if (nodes[i] instanceof ActionNode actionNode) {
                    srcItems.add(actionNode.action);
                }
            }

            Vector<Item> dstItems = new Vector<>();
            for (int i = 0; i < perms.length; i++) {
                if (nodes[perms[i]] instanceof ProjectNode projectNode) {
                    dstItems.add(projectNode.project);
                } else if (nodes[perms[i]] instanceof ActionNode actionNode) {
                    dstItems.add(actionNode.action);
                }
            }

            project.reorder(srcItems.toArray(new Item[srcItems.size()]),
                    dstItems.toArray(new Item[dstItems.size()]));

            fireChangeEvent(new ChangeEvent(IndexImpl.this));

            // reselect previously selected nodes
            EventQueue.invokeLater(() -> {
                try {
                    explorerManager.setSelectedNodes(selectedNodes);
                } catch (Exception ex) {
                    LOG.log(Level.INFO, "Node selection failed. {0}", ex.getMessage());
                }
            });
        }
// This could be implemented for efficiency - instead of defaulting to reorder.
//        public void moveUp(int i) {
//            System.out.println("ProjectChildren.moveUp(" + i + ")");
//            super.moveUp(i);
//        }
// This could be implemented for efficiency - instead of defaulting to reorder.
//        public void moveDown(int i) {
//            System.out.println("ProjectChildren.moveDown(" + i + ")");
//            super.moveDown(i);
//        }
//
// This does not seem to be used.
//        public void move(int i, int j) {
//            System.out.println("ProjectChildren.move(" + i + ", " + j + ")");
//            super.move(i, j);
//        }
//
//        public int indexOf(Node node) {
//            System.out.println("ProjectChildren.indexOf(...) " + node.toString());
//
//            int retValue;
//            retValue = super.indexOf(node);
//            return retValue;
//        }
//
// This seems to be called only for moveUp and moveDown.
//        public void exchange(int i, int j) {
//            System.out.println("ProjectChildren.exchange(" + i + ", " + j + ")");
//            super.exchange(i, j);
//        }
    }
//    private class AlphabeticComparator implements Comparator<Project> {
//        public int compare(Project p1, Project p2) {
//            return p1.getDescription().compareToIgnoreCase(p2.getDescription());
//        }
//    }
}

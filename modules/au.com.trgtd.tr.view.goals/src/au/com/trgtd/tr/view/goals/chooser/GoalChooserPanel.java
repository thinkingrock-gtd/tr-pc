package au.com.trgtd.tr.view.goals.chooser;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.tree.TreeSelectionModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;

public class GoalChooserPanel extends JPanel implements ExplorerManager.Provider, PropertyChangeListener {

    private final static GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
    private final ExplorerManager explorerManager;
    private final BeanTreeView treeView;
    private final AbstractButton selectButton;
    private final Node rootNode;

    /**
     * Creates a new instance.
     */
    public GoalChooserPanel(AbstractButton selectButton, boolean showAll) {
        this.selectButton = selectButton;
        rootNode = new ChooserRootNode(goalsCtrl.getRootGoal(), selectButton, showAll);
        explorerManager = new ExplorerManager();
        explorerManager.setRootContext(rootNode);
        treeView = new BeanTreeView();
        treeView.setRootVisible(true);
        treeView.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setLayout(new BorderLayout());
        add(treeView, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        explorerManager.addPropertyChangeListener(this);
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        explorerManager.removePropertyChangeListener(this);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public GoalCtrl getSelected() {
        Node[] nodes = explorerManager.getSelectedNodes();
        return nodes.length == 0 ? null : ((ChooserGoalNode) nodes[0]).goal;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!evt.getPropertyName().equals(ExplorerManager.PROP_SELECTED_NODES)) {
            return;
        }
        Node[] nodes = explorerManager.getSelectedNodes();
        if (nodes.length != 1) {
            selectButton.setEnabled(false);
            treeView.requestFocusInWindow();
            return;
        }
        if (!(nodes[0] instanceof ChooserGoalNode)) {
            selectButton.setEnabled(false);
            treeView.requestFocusInWindow();
            return;
        }
        ChooserGoalNode goalNode = (ChooserGoalNode)nodes[0];
        selectButton.setEnabled(goalNode.isSelectable());
        treeView.requestFocusInWindow();

//        if (goalNode.goal.isAchieved()) {
//            selectButton.setEnabled(false);
//            treeView.requestFocusInWindow();
//            return;
//        }
//        LevelCtrl level = goalNode.goal.getLevel();
//        if (level == null || !level.isGoalsHaveProjects()) {
//            selectButton.setEnabled(false);
//            treeView.requestFocusInWindow();
//            return;
//        }
//
//        selectButton.setEnabled(true);
//        treeView.requestFocusInWindow();
    }

    public void reset() {
        try {
            selectButton.setEnabled(false);
            explorerManager.setSelectedNodes(new Node[]{rootNode});
            treeView.expandAll();
            treeView.requestFocusInWindow();
        } catch (PropertyVetoException ex) {
        }
    }
}

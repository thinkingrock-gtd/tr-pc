package au.com.trgtd.tr.view.calendar;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.NbBundle;

/**
 * Day view tree for delegated and Do ASAP items.
 * 
 * @author Jeremy Moore
 */
public final class DayViewTree {
    
    private final JTree tree;
    private final JScrollPane view;
    private final TrCalModel model;
    private final DateCtlr dateCtlr;
    private final DefaultMutableTreeNode rootNode;
    private final DefaultMutableTreeNode asapNode;
    private final DefaultMutableTreeNode deleNode;

    public DayViewTree(TrCalModel model, DateCtlr dateCtlr) {       
        this.model = model;
        this.dateCtlr = dateCtlr;
        
        //Create the top nodes.
        asapNode = new DefaultMutableTreeNode(NbBundle.getMessage(getClass(), "doasap.due"));
        deleNode = new DefaultMutableTreeNode(NbBundle.getMessage(getClass(), "deledated.followup"));
        rootNode = new DefaultMutableTreeNode("");
        rootNode.add(asapNode);
        rootNode.add(deleNode);
        
        //Create a tree and view.
        tree = new JTree(rootNode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//        tree.addTreeSelectionListener(selectionListener);
        view = new JScrollPane(tree);
        
//        resetNodes();
    }

    public JScrollPane getTreeView() {        
        return view;        
    }
    
//    private void resetNodes() {
//        DefaultMutableTreeNode node;
//        asapNode.removeAllChildren();
//        deleNode.removeAllChildren();
//        for (TrCalModel.ASAPItem item : model.getDoASAPItems(dateCtlr.getDay())) {
//            node = new DefaultMutableTreeNode(item.toString());
//            asapNode.add(node);            
//        }
//        for (TrCalModel.DelegatedItem item : model.getDelegatedItems(dateCtlr.getDay())) {
//            node = new DefaultMutableTreeNode(item.toString());
//            deleNode.add(node);            
//        }
//
//    }    
    
    
//    private final TreeSelectionListener selectionListener = new TreeSelectionListener() {
//        @Override
//        public void valueChanged(TreeSelectionEvent tse) {
//            resetNodes();
//        }        
//    };
    
}

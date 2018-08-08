
package au.com.trgtd.tr.view.project.chooser;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import tr.model.project.Project;
import tr.model.topic.Topic;

/**
 * ProjectTreeCellRenderer

 * @author Jeremy Moore
 */
public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {
    
    private final Icon iconOpened;
    private final Icon iconClosed;

    public ProjectTreeCellRenderer(Icon iconClosed, Icon iconOpened) {
        this.iconOpened = iconOpened;
        this.iconClosed = iconClosed;
    }

    @Override
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
    
        setForeground(getProjectColor(value));                
        setIcon(expanded ? iconOpened : iconClosed);

        return this;
    }
    
    protected Color getProjectColor(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        if (node.getUserObject() instanceof Project) {
            Project project = (Project)node.getUserObject();            
            Topic topic = project.getTopic();
            if (topic != null) {
                return topic.getForeground();
            }            
        }
        return Color.black;
    }
    
    

}

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
        if (node.getUserObject() instanceof Project project) {
            Topic topic = project.getTopic();
            if (topic != null) {
                return topic.getForeground();
            }            
        }
        return Color.black;
    }
    
    

}

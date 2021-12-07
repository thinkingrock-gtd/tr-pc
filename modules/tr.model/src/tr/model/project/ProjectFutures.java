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
package tr.model.project;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.resource.Icons;
import javax.swing.ImageIcon;
import tr.model.IDGenerator;
import tr.model.Item.Item;

/**
 * Root project for future projects.
 *
 * @author Jeremy Moore
 */
public class ProjectFutures extends Project {
    
    /** Default constructor. */
    public ProjectFutures() {
        super(new IDGenerator() {
            public int getNextID() {
                return Constants.ID_ROOT_FUTURES;
            }
        });
    }
    
    /**
     * Determines whether the item item is user editable.
     * @return false.
     */
    @Override
    public boolean isEditable() {
        return false;
    }    

    /**
     * Determines whether the project is a root project.
     * @return true.
     */
    @Override
    public boolean isRoot() {
        return true;
    }

    /**
     * Determines whether a potential child can be added.
     * @param child The potential child.
     * @return true iff the object's class is Project.class and is not already 
     * contained.
     */
    @Override
    public boolean canAdd(Item child) {
        return (child.getClass () == Project.class && !contains(child));
    }
    
    /** Overridden to return the description. */
    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(ProjectFutures.class, "Future");
    }
    
    /**
     * Overridden to get the icon for the templates project node.
     * @param expanded Whether or not the node is expanded.
     * @return The node icon.
     */
    public ImageIcon getIcon(boolean expanded) {
//        return (expanded) ? Resources.ICON_PROJECT_OPENED : Resources.ICON_PROJECT;
        return Icons.ProjectsFuture;
    }
    
}

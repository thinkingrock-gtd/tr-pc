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

package tr.model.project;

import tr.model.IDGenerator;
import tr.model.Item.Item;
import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.resource.Icons;
import javax.swing.ImageIcon;

/**
 * Project for projects.
 *
 * @author Jeremy Moore
 */
public class ProjectProjects extends Project {
    
    /** Default constructor. */
    public ProjectProjects() {
        super(new IDGenerator() {
            public int getNextID() {
                return Constants.ID_ROOT_PROJECTS;
            }
        });
    }
    
    /** Constructor for data conversion from project. */
    public ProjectProjects(Project project) {
        this();
        for (Item item : project.children ) {
            add(item);
        }
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
        return (child.getClass() == Project.class && !contains(child));
    }
    
    /** Overridden to return the description. */
    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(ProjectProjects.class, "Projects");
    }
    
    /**
     * Overridden to get the icon for the projects project node.
     * @param expanded Whether or not the node is expanded.
     * @return The node icon.
     */
    @Override
    public ImageIcon getIcon(boolean open) {
        return Icons.Projects;
        
    }
    
}

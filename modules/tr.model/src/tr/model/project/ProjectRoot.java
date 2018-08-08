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

import au.com.trgtd.tr.appl.Constants;
import tr.model.IDGenerator;
import tr.model.Item.Item;

/**
 * The root project which has two children: the projects project (ProjectRoot)
 * and the single actions project (ProjectSingleActions).
 * 
 * @author Jeremy Moore
 */
public class ProjectRoot extends Project {
    
    /**
     * Default constructor.
     * @param pp The project for projects.
     * @param pa The project for single actions.
     */
    public ProjectRoot(ProjectProjects pp, ProjectSingleActions pa) {
        super(new IDGenerator() {
            public int getNextID() {
                return Constants.ID_ROOT_ALL;
            }
        });
        children.add(pp);
        pp.setParent(this);
        pp.addObserver(this);
        children.add(pa);
        pa.setParent(this);
        pa.addObserver(this);
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
     * Overridden to disallow adding.
     * @param item The item.
     * @return false.
     */
    @Override
    public boolean canAdd(Item item) {
        return false;
    }
    
    /**
     * Overridden to dissallow set done.
     * @return false.
     */
    public boolean canSetDone() {
        return false;
    }
    
    /** Disabled. */
    @Override
    public Item remove(int index) {
        return null;
    }
    
    /** Disabled. */
    @Override
    public boolean remove(Item item) {
        return false;
    }
    
    /** Disabled. */
    @Override
    public void setDone(boolean done) {
    }
    
    /** Disabled. */
    public void setParent(Item item) {
    }
    
    /** Returns the root description. */
    @Override
    public String getDescription() {
        return org.openide.util.NbBundle.getMessage(ProjectRoot.class, "All_Items");
    }
    
}

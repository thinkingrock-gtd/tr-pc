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

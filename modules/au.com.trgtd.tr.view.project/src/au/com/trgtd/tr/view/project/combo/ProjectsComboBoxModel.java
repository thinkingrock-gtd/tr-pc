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
package au.com.trgtd.tr.view.project.combo;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.project.Project;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.project.combo.ProjectsComboBoxModel.ProjectItem;

/**
 * Projects combo box model.
 */
class ProjectsComboBoxModel extends DefaultComboBoxModel<ProjectItem> implements Observer {
    
    private List<ProjectItem> projectItems;

    private Lookup.Result result;
    
    /**
     * Creates a new instance.
     */
    public ProjectsComboBoxModel() {
        super();
        initialise();
    }
        
    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public ProjectItem getElementAt(int index) {
        try {
            return projectItems.get(index);            
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    /** Implement ListModel.getSize(). */
    @Override
    public int getSize() {
        return projectItems.size();
    }
    
    /** Implement Observer to re-initialise model and fire contents changed. */
    public void update(Observable o, Object arg) {
        initialise();
        fireContentsChanged(this, 0, getSize());
    }
    
    /* Initialise the list of projects. */
    private synchronized void initialise() {

        // need to re-initialise if the data model changes
        if (result == null) {
            result = DataLookup.instance().lookupResult(Data.class);
            result.addLookupListener((LookupEvent lookupEvent) -> {
                update(null, null);
            });
        }     
        
        projectItems = new Vector<>();
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
                
        projectItems.add(new ProjectItem(org.openide.util.NbBundle.getMessage(ProjectsComboBoxModel.class, "none"), null));
        
        addProjects(data.getRootProjects(), "");

        // need to re-initialise if any project changes
        data.getRootProjects().addObserver(this);
        
    }
    
    /* Recursively adds all of the sub-projects of a given project.
     * @param root The given project.
     * @param indent A string used to indent the project name in an attempt to
     * show its level in the project heirarchy. */
    private void addProjects(Project project, String indent) {
        if (project == null || project.isDone()) return;
        
        for (Iterator<Project> i = project.iterator(Project.class); i.hasNext(); ) {
            
            Project subProject = i.next();
            
            if (!subProject.isDone()) {
                
                projectItems.add(new ProjectItem(indent + subProject.getDescription(), subProject));
                
                addProjects(subProject, indent + "   ");
            }
        }
    }
        
    /**
     * A class for project items in the projects combo box.  Each item holds a
     * project and a string to represent it in the combo box list.
     */
    final class ProjectItem {
        
        public final String string;
        public final Project project;
        
        public ProjectItem(String string, Project project) {
            this.project = project;
            this.string = filter(string);
        }
        
        private final String filter(String str) {
            str = str.replaceAll("\t", " ");
            str = str.replaceAll("\n", " ");
            str = str.replaceAll("\f", "");
            str = str.replaceAll("\r", "");
            return str;
        }
        
        @Override
        public final String toString() {
            return string;
        }
    }
    
}

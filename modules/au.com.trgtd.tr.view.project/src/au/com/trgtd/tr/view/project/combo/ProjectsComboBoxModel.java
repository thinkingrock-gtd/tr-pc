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

package au.com.trgtd.tr.view.project.combo;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.project.Project;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Projects combo box model.
 */
class ProjectsComboBoxModel extends DefaultComboBoxModel implements Observer {
    
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
    public Object getElementAt(int index) {
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
            result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
            result.addLookupListener(new LookupListener() {
                public void resultChanged(LookupEvent lookupEvent) {
                    update(null, null);
                }
            });
        }     
        
        projectItems = new Vector<ProjectItem>();
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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

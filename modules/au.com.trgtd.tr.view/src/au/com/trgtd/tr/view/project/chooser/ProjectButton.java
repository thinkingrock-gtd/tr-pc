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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import tr.model.project.Project;

/**
 * Custom button for activating the project chooser dialog.
 */
public class ProjectButton extends JButton {
    
    private ProjectChooser projectChooser;
    private Project project;
    
    public Component component = null;
    
    /**
     * Constructs a new default instance.
     */
    public ProjectButton() {
//        Frame owner = WindowManager.getDefault().getMainWindow();
//        dateChooser = new DateChooser(owner, firstDay);
        projectChooser = new ProjectChooser(new JFrame());
    }
    
    /**
     * Constructs a new instance with the given title.
     * @param title The dialog title.
     */
    public ProjectButton(String title) {
//        Frame owner = WindowManager.getDefault().getMainWindow();
//        dateChooser = new DateChooser(owner, title, firstDay);
        projectChooser = new ProjectChooser(new JFrame(), title);
    }
    
    /**
     * Constructs a new instance with the given window owner.
     * @param owner The window owner.
     */
    public ProjectButton(Frame owner) {
        projectChooser = new ProjectChooser(owner);
    }
    
    /**
     * Constructs a new instance with the window owner and dialog title.
     * @param owner The window owner.
     * @param title The dialog title.
     */
    public ProjectButton(Frame owner, String title) {
        projectChooser = new ProjectChooser(owner, title);
    }
    
    /**
     * Constructs a new instance with the given dialog owner.
     * @param owner The dialog owner.
     */
    public ProjectButton(Dialog owner) {
        projectChooser = new ProjectChooser(owner);
    }
    
    /**
     * Constructs a new instance with the window owner and dialog title.
     * @param owner The dialog owner.
     * @param title The dialog title.
     */
    public ProjectButton(Dialog owner, String title) {
        projectChooser = new ProjectChooser(owner, title);
    }
    
    /**
     * Gets the value of the property.
     * @return The current value of the date property.
     */
    public Project getProject() {
        return project;
    }
    
    /**
     * Sets the value of the property.
     * @param project The new value of the property
     * @return the old value of the property
     */
    public void setProject(Project project) {
        Project old = this.project;
        this.project = project;
        setText((project == null) ? "" : project.getDescription());
//      firePropertyChange("date", old, date);
    }
    
    /**
     * Called when the button is clicked, in order to fire an
     * <code>ActionEvent</code>. Displays the dialog to change the project
     * instead of generating the event and updates the project property.
     * @param e <code>ActionEvent</code> to fire
     */
    @Override
    protected void fireActionPerformed(ActionEvent event) {
        
        ProjectChooserDialog dialog = projectChooser.getDialog();
        
        if (component == null) {
            component = this;
        }
        
        Project newProject = dialog.select(project, component);
        if (dialog.cancelled()) {
            return;
        }
        
        setProject(newProject);
    }
    
}

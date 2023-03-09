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

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.Font;

import tr.model.project.Project;
import au.com.trgtd.tr.view.project.combo.ProjectsComboBoxModel.ProjectItem;

/**
 * A combo box for projects.
 *
 * @author Jeremy Moore
 */
public class ProjectsComboBox extends TRComboBox<ProjectItem> {

    /** Constructs a new instance. */
    public ProjectsComboBox() {
        super(new ProjectsComboBoxModel());
        setOpaque(true);
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
    }

    /**
     * Select the given project if it is in the list, otherwise remove the
     * current selection.
     * @param project The project to select.
     */
    @Override
    public void setSelectedItem(Object object) {
        if (object == null) {
            setSelectedIndex(0); // None item
            return;
        }
        if (object instanceof Project project) {
            for (int i = 1; i < getItemCount(); i++) {
                ProjectItem item = super.getItemAt(i);
                if (project.equals(item.project)) {
                    setSelectedIndex(i);
                    return;
                }
            }
            setSelectedIndex(-1); // clear selection
            return;
        }
        super.setSelectedItem(object);
    }

    @Override
    public Project getSelectedItem() {
        Object item = super.getSelectedItem();
        if (item instanceof ProjectItem projectItem) {
            return projectItem.project;
        } else if (item instanceof Project project) {
            return project;
        }
        return null;
    }

    @Override
    public ProjectItem getItemAt(int index) {
        return super.getItemAt(index);
    }

}

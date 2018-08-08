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
public class ProjectsComboBox extends TRComboBox {

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
        if (object instanceof Project) {
            Project project = (Project) object;
            for (int i = 1; i < getItemCount(); i++) {
                ProjectItem item = (ProjectItem) super.getItemAt(i);
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
    public Object getSelectedItem() {
        Object item = super.getSelectedItem();
        if (item instanceof ProjectItem) {
            return ((ProjectItem) item).project;
        }
        return item;
    }

    @Override
    public Object getItemAt(int index) {
        Object item = super.getItemAt(index);
        if (item instanceof ProjectItem) {
            return ((ProjectItem) item).project;
        }
        return item;        
    }    
    
}

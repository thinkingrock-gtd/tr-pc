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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.view.projects.spi.ProjectViewerSPI;
import java.awt.EventQueue;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import tr.model.project.Project;

/**
 * Service provider for viewing a project.
 */
public class ProjectViewerSP implements ProjectViewerSPI {

    @Override
    public void view(final Project project) {

        CallableSystemAction action = SystemAction.get(ProjectsAction.class);
        if (action == null) {
            return;
        }
        action.performAction();

        EventQueue.invokeLater(() -> {
            ProjectsTreeTopComponent tc = ProjectsTreeTopComponent.findInstance();
            if (tc != null) {
                tc.setShowDone(project.isDone());
                tc.select(project);
            }
        });
    }

}

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

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProjectsTreeTopComponent tc = ProjectsTreeTopComponent.findInstance();
                if (tc != null) {
                    tc.setShowDone(project.isDone());
                    tc.select(project);
                }
            }
        });
    }

}

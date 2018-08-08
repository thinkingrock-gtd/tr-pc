package au.com.trgtd.tr.view.projects;

import java.util.Date;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionStateInactive;
import tr.model.project.Project;

/**
 * Create a project template from a given project. Deep copies the selected
 * project and all sub-projects and actions and adds the template to the project
 * templates. 
 * All copied projects will have the following fields reset:
 * - created date: current date
 * - start date: null
 * - due date: null
 * - done: false
 * - done date: null
 * All copied actions will have the following fields reset:
 * - created date: current date
 * - done: false
 * - done date: null
 * - start date: null
 * - due date: null
 * - status: inactive
 *
 * @author Jeremy Moore
 */
public final class CreateTemplate {

    private static final Logger LOG = Logger.getLogger("tr.create.template");

    public CreateTemplate() {
    }

    /**
     * Create the project template and put it in the templates project.
     * @param project the project.
     */
    public void create(Project project) {
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data not found.");            
            return;
        }

        // Deep copy project 
        Project templateProject = (Project)project.copy(data);
        
        // Recursively reset values for actions and subprojects 
        reset(templateProject, new Date());        
        
        // Copy to templates
        data.getRootTemplates().add(templateProject);        
    }

    private void reset(Project templateProject, Date createdDate) {        
        
        // reset template project fields
        templateProject.setCreated(createdDate);
        templateProject.setStartDate(null);
        templateProject.setDueDate(null);
        templateProject.setDone(false);
        templateProject.setDoneDate(null);        
        
        // reset child actions
        for (Action templateAction : templateProject.getChildren(Action.class)) {
            reset(templateAction, createdDate);
        }
        
        // reset child projects
        for (Project templateSubproject : templateProject.getChildren(Project.class)) {
            reset(templateSubproject, createdDate);
        }
    }

    private void reset(Action templateAction, Date createdDate) {
        // reset action fields
        templateAction.setCreated(createdDate);
        templateAction.setDone(false);
        templateAction.setDoneDate(null);
        templateAction.setStartDate(null);
        templateAction.setDueDate(null);
        templateAction.setState(new ActionStateInactive());
    }
    
}

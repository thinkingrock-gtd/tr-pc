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
package au.com.trgtd.tr.view.goals.projects;

//import au.com.trgtd.tr.view.DeleteAction;
//import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.projects.spi.ProjectViewerSPI;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.project.Project;

/**
 * Goal project node.
 * 
 * @author Jeremy Moore
 */
public class GoalProjectNode extends AbstractNode implements EditCookie, PropertyChangeListener {

    private final static Lookup.Result<ProjectViewerSPI> lookup
            = Lookup.getDefault().lookupResult(ProjectViewerSPI.class);
    private final static Image DottedOutlineImage
            = ImageUtilities.loadImage("au/com/trgtd/tr/view/goals/resource/DottedOutline.png", false);

    public final GoalCtrl goalCtrl;
    public final Project project;

    public GoalProjectNode(Project project, GoalCtrl goalCtrl) {
        super(Children.LEAF, Lookups.singleton(goalCtrl));
        this.project = project;
        this.goalCtrl = goalCtrl;
        this.project.addPropertyChangeListener(Project.PROP_DESCR, this);
        this.project.addPropertyChangeListener(Project.PROP_DONE, this);
        this.project.addPropertyChangeListener(Project.PROP_TOPIC, this);
    }

    @Override
    public Image getIcon(int type) {
        if (project == null) {
            return super.getIcon(type);
        }
        Image projectImage = project.getIcon(false).getImage();
        return ImageUtilities.mergeImages(projectImage, DottedOutlineImage, 0, 0);
    }

    @Override
    public Image getOpenedIcon(int type) {
        if (project == null) {
            return super.getIcon(type);
        }
        Image projectImage = project.getIcon(true).getImage();
        return ImageUtilities.mergeImages(projectImage, DottedOutlineImage, 0, 0);
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == EditCookie.class) {
            return type.cast(this);
        }
//        if (type == DeleteCookie.class) {
//            return type.cast(this);
//        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
                    SystemAction.get(EditAction.class),
                    null,
                    SystemAction.get(DeleteAction.class),
                    null,
                    SystemAction.get(MoveUpAction.class),
                    SystemAction.get(MoveDownAction.class),
                    null,
                };
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    @Override
    public boolean canEdit() {
        return true;
    }

    @Override
    public void edit() {
        Object[] projectViewers = lookup.allInstances().toArray();
        if (projectViewers != null && projectViewers.length > 0) {
            ((ProjectViewerSPI)projectViewers[0]).view(project);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        fireDisplayNameChange(null, project.getDescription());
        fireNameChange(null, project.getDescription());
        fireCookieChange();
        fireIconChange();
        fireOpenedIconChange();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return project.getDescription();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String getHtmlDisplayName() {
        if (project.isDone()) {
            return "<S>" + getDisplayName() + "</S>";
        } else {
            return getDisplayName();
        }
    }

    // CUT, COPY & PASTE
    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        goalCtrl.removeGoalProject(project.getID());
        fireNodeDestroyed();
    }

}

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
package au.com.trgtd.tr.view.projects.goals;

//import au.com.trgtd.tr.view.DeleteAction;
//import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.goals.dialog.GoalDialog;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.project.Project;

/**
 * Project goal node.
 * 
 * @author Jeremy Moore
 */
public class ProjectGoalNode extends AbstractNode implements EditCookie, PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger("tr.view.project.goals");
    public final GoalCtrl goalCtrl;
    public final Project project;

    public ProjectGoalNode(Project project, GoalCtrl goalCtrl) {
        super(Children.LEAF, Lookups.singleton(goalCtrl));
        this.project = project;
        this.goalCtrl = goalCtrl;
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_DESCR, this);
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_ACHIEVED_DATE, this);
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_LEVEL, this);
        if (getLevelCtrl() != null) {
            getLevelCtrl().addPropertyChangeListener(LevelCtrl.PROP_ICON_PATH, this);
        }
    }

    @Override
    public Image getIcon(int type) {
        if (goalCtrl == null || goalCtrl.getLevel() == null) {
            return super.getIcon(type);
        }
        return goalCtrl.getLevel().getGoalsIcon().icon.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        if (goalCtrl == null || goalCtrl.getLevel() == null) {
            return super.getIcon(type);
        }
        return goalCtrl.getLevel().getGoalsIcon().icon.getImage();
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
        new GoalDialog().showModifyDialog(goalCtrl);
        goalCtrl.update();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GoalCtrl.PROP_LEVEL)) {
            LevelCtrl oldLevelCtrl = (LevelCtrl)evt.getOldValue();
            if (oldLevelCtrl != null) {
                oldLevelCtrl.removePropertyChangeListener(LevelCtrl.PROP_ICON_PATH, this);
            }
            LevelCtrl newLevelCtrl = (LevelCtrl)evt.getNewValue();
            if (newLevelCtrl != null) {
                newLevelCtrl.addPropertyChangeListener(LevelCtrl.PROP_ICON_PATH, this);
            }
            fireIconChange();
            return;
        }
        fireDisplayNameChange(null, goalCtrl.getDescr());
        fireNameChange(null, goalCtrl.getDescr());
        fireCookieChange();
        fireIconChange();
        fireOpenedIconChange();
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        goalCtrl.removeGoalProject(project.getID());
        fireNodeDestroyed();
    }

    private LevelCtrl getLevelCtrl() {
        return goalCtrl.getLevel();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return goalCtrl.getDescr();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String getHtmlDisplayName() {
        if (goalCtrl.isAchieved()) {
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
        delete();
    }

}

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

import au.com.trgtd.tr.view.AddAction;
import au.com.trgtd.tr.view.AddCookie;
//import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.goals.chooser.GoalChooserDialog;
import au.com.trgtd.tr.view.goals.chooser.GoalChooserDialog.Result;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.actions.PasteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.project.Project;

public class ProjectGoalsNode extends AbstractNode implements AddCookie, EditCookie, PropertyChangeListener {

    private final Image iconClosed;
    private final Image iconOpened;
    private final Project project;

    public ProjectGoalsNode(Project project) {
        super(new ProjectGoalChildren(project));
        this.iconClosed = ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/projects/goals/GoalsFolder.png", false).getImage();
        this.iconOpened = ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/projects/goals/GoalsFolderOpen.png", false).getImage();
        this.project = project;
        this.project.addPropertyChangeListener(Project.PROP_GOAL_LINKS, this);
    }

    @Override
    public Image getIcon(int type) {
        return iconClosed;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return iconOpened;
    }

    @Override
    public String getName() {
        return "Goals";
    }

    @Override
    public String getHtmlDisplayName() {
        return "<I>" + escapeHTML(getName()) + "</I>";
    }

    private String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml10(str);
        return str.replace("&apos;", "'");
    }


//    @Override
//    public boolean canDelete() {
//        return false;
//    }
//
//    @Override
//    public void delete() {
//    }

    @Override
    public boolean canEdit() {
        return false;
    }

    @Override
    public void edit() {
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        sheet.put(Sheet.createPropertiesSet());
        return sheet;
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
//            SystemAction.get(ExpandAction.class),
//            SystemAction.get(CollapseAction.class),
//            null,
//            SystemAction.get(ToggleHideDoneAction.class),
            null,
            SystemAction.get(AddAction.class),
            null,
            SystemAction.get(PasteAction.class)
        };
    }

    @Override
    public final <T extends Node.Cookie> T getCookie(Class<T> type) {
//        if (type == Index.class) {
//            return type.cast(((ProjectGoalChildren)getChildren()).getIndex());
//        }
//        if (type == ToggleHideDoneCookie.class) {
//            return type.cast(this);
//        }
        if (type == AddCookie.class) {
            return type.cast(this);
        }
//        if (type == ExpandCookie.class) {
//            return type.cast(this);
//        }
//        if (type == CollapseCookie.class) {
//            return type.cast(this);
//        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(AddAction.class);
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public void add() {
        GoalChooserDialog goalChooserDialog = new GoalChooserDialog(false);
        Result result = goalChooserDialog.showDialog();
        if (result == Result.SELECT) {
            GoalCtrl goalCtrl = goalChooserDialog.getSelected();
            if (goalCtrl == null) {
                return;
            }
            GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
            if (goalsCtrl != null) {
                goalCtrl.insertGoalProject(project.getID());
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

}

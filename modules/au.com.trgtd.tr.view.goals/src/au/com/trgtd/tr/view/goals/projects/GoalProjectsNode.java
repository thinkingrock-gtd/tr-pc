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

import au.com.trgtd.tr.view.goals.projects.dialog.LinkNewProjectDialog;
import au.com.trgtd.tr.view.project.chooser.ProjectChooserDialog;
import java.awt.Frame;
import java.awt.Image;
import javax.swing.Action;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.actions.PasteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.windows.WindowManager;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.project.Project;

/**
 * Goal projects root node.
 *
 * @author Jeremy Moore
 */
public class GoalProjectsNode extends AbstractNode 
//      implements AddCookie {
        implements LinkNewProjectCookie, LinkExistingProjectCookie {

    private static ProjectChooserDialog projectChooserDialog;
    private final Image iconClosed;
    private final Image iconOpened;
    private final GoalCtrl goal;

    public GoalProjectsNode(GoalCtrl goal) {
        super(new GoalProjectChildren(goal));
        Image outlined = ImageUtilities.loadImage("au/com/trgtd/tr/view/goals/resource/DottedOutline.png", false);
        Image projects = ImageUtilities.loadImage("au/com/trgtd/tr/view/goals/resource/ProjectsLinked.png", false);
        this.iconClosed = ImageUtilities.mergeImages(projects, outlined, 0, 0);
        this.iconOpened = iconClosed;
        this.goal = goal;
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
        return NbBundle.getMessage(GoalProjectsNode.class, "projects.node");
    }

    @Override
    public String getHtmlDisplayName() {
        return "<I>" + escapeHTML(getName()) + "</I>";
    }

    private String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml10(str);
        return str.replace("&apos;", "'");
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
//            SystemAction.get(AddAction.class),
//            null,
            SystemAction.get(LinkNewProjectAction.class),
            SystemAction.get(LinkExistingProjectAction.class),
            null,
            SystemAction.get(PasteAction.class)
        };
    }

    @Override
    public final <T extends Node.Cookie> T getCookie(Class<T> type) {
//        if (type == AddCookie.class) {
//            return type.cast(this);
//        }
        if (type == LinkNewProjectCookie.class) {
            return type.cast(this);
        }
        if (type == LinkExistingProjectCookie.class) {
            return type.cast(this);
        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

    @Override
    public Action getPreferredAction() {
//        return SystemAction.get(AddAction.class);
        return SystemAction.get(LinkNewProjectAction.class);
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

//    @Override
//    public void add() {
//        if (projectDialog == null) {
//            Frame frame = WindowManager.getDefault().getMainWindow();
//            boolean currentOnly = true;
//            projectDialog = new ProjectChooserDialog(frame, currentOnly);
//        }
//        Project project = projectDialog.select(null, null);
//        if (!projectDialog.cancelled() && project != null) {
//            goal.insertGoalProject(project.getID());
//        }
//    }

    public void bump() {
        ((GoalProjectChildren)getChildren()).bump();
    }

    @Override
    public void linkNewProject() {
        LinkNewProjectDialog dialog = new LinkNewProjectDialog();
        dialog.showCreateDialog(goal);
    }

    @Override
    public void linkExistingProject() {
        if (projectChooserDialog == null) {
            Frame frame = WindowManager.getDefault().getMainWindow();
            boolean currentOnly = true;
            projectChooserDialog = new ProjectChooserDialog(frame, currentOnly);
        }
        Project project = projectChooserDialog.select(null, null);
        if (!projectChooserDialog.cancelled() && project != null) {
            goal.insertGoalProject(project.getID());
        }
    }

}

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
package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.*;
import au.com.trgtd.tr.view.goals.dialog.GoalDialog;
import au.com.trgtd.tr.view.goals.dialog.GoalDialog.Result;
import au.com.trgtd.tr.view.goals.projects.GoalProjectsNode;
import au.com.trgtd.tr.view.goals.projects.ToggleShowProjectsAction;
import au.com.trgtd.tr.view.goals.projects.ToggleShowProjectsCookie;
import au.com.trgtd.tr.view.topics.TopicChangeAction;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.actions.DeleteAction;
import org.openide.actions.*;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.*;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.project.Project;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;

/**
 * Goal node.
 * 
 * @author Jeremy Moore
 */
public class GoalNode extends AbstractNode
        implements AddCookie, EditCookie, ExpandCookie,
        CollapseCookie, TopicChangeCookie, ChangeLevelCookie,
        ToggleHideDoneCookie, ToggleShowProjectsCookie, PropertyChangeListener {

    private final static Image WarningBadge = ImageUtilities.loadImage("au/com/trgtd/tr/view/goals/resource/WarningBadge.png", false);
    public final OutlineView view;
    public final GoalCtrl supergoalCtrl;
    public final GoalCtrl goalCtrl;
    private final InstanceContent content;
    private boolean showWarning;

    public GoalNode(OutlineView view, GoalCtrl supergoal, GoalCtrl goal) {
        this(view, supergoal, goal, new InstanceContent());
    }

    public GoalNode(OutlineView view, GoalCtrl supergoalCtrl, GoalCtrl goalCtrl, InstanceContent content) {
        super(new GoalChildren(view, goalCtrl), new AbstractLookup(content));
        this.view = view;
        this.supergoalCtrl = supergoalCtrl;
        this.goalCtrl = goalCtrl;
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_DESCR, this);
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_LEVEL, this);
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_ACHIEVED_DATE, this);
        this.goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_PROJECT_LINKS, this);
        this.goalCtrl.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (getLevelCtrl() != null) {
                    getLevelCtrl().addPropertyChangeListener(LevelCtrl.PROP_ICON_PATH, this);
                }
            }
        });
        if (getLevelCtrl() != null) {
            getLevelCtrl().addPropertyChangeListener(LevelCtrl.PROP_ICON_PATH, this);
            getLevelCtrl().addPropertyChangeListener(LevelCtrl.PROP_PROJECTS, this);
        }

        setDisplayName(goalCtrl.getDescr());

        this.content = content;
        this.content.add(this);
        this.content.add(goalCtrl);

        this.showWarning = showWarning();
    }

    @Override
    public Image getIcon(int type) {
        if (goalCtrl == null || goalCtrl.getLevel() == null) {
            return super.getIcon(type);
        }
        Image image = goalCtrl.getLevel().getGoalsIcon().icon.getImage();
        if (showWarning) {
            return ImageUtilities.mergeImages(image, WarningBadge, 0, 0);
        } else {
            return image;
        }
    }

    @Override
    public Image getOpenedIcon(int type) {
        if (goalCtrl == null || goalCtrl.getLevel() == null) {
            return super.getIcon(type);
        }
        Image image = goalCtrl.getLevel().getGoalsIcon().icon.getImage();
        if (showWarning) {
            return ImageUtilities.mergeImages(image, WarningBadge, 0, 0);
        } else {
            return image;
        }
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set ss = sheet.get(Sheet.PROPERTIES);
        if (ss == null) {
            ss = Sheet.createPropertiesSet();
            ss.put(new PropertyAchieved(goalCtrl));
            ss.put(new PropertyLevel(goalCtrl));
            ss.put(new PropertyTopic(goalCtrl));
            sheet.put(ss);
        }
        return sheet;
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == Index.class) {
            return type.cast(((GoalChildren) getChildren()).getIndex());
        }
        if (type == ToggleHideDoneCookie.class) {
            return type.cast(this);
        }
        if (type == ToggleShowProjectsCookie.class) {
            return type.cast(this);
        }
        if (type == AddCookie.class) {
            return type.cast(this);
        }
        if (type == EditCookie.class) {
            return type.cast(this);
        }
        if (type == ExpandCookie.class) {
            return type.cast(this);
        }
        if (type == CollapseCookie.class) {
            return type.cast(this);
        }
        if (type == TopicChangeCookie.class) {
            return type.cast(this);
        }
        if (type == ChangeLevelCookie.class) {
            return type.cast(this);
        }
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
                    SystemAction.get(ExpandAction.class),
                    SystemAction.get(CollapseAction.class),
                    null,
                    SystemAction.get(ToggleHideDoneAction.class),
                    SystemAction.get(ToggleShowProjectsAction.class),
                    null,
                    SystemAction.get(AddAction.class),
                    null,
                    SystemAction.get(CopyAction.class),
                    SystemAction.get(CutAction.class),
                    SystemAction.get(PasteAction.class),
                    null,
                    SystemAction.get(DeleteAction.class),
                    null,
                    SystemAction.get(MoveUpAction.class),
                    SystemAction.get(MoveDownAction.class),
                    null,
                    SystemAction.get(ChangeLevelAction.class),
                    SystemAction.get(TopicChangeAction.class)
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
        Result result = new GoalDialog().showModifyDialog(goalCtrl);
        if (result == Result.OK) {
            goalCtrl.update();
        }
    }

    @Override
    public void add() {
        view.expandNode(this);
        GoalDialog dialog = new GoalDialog();
        dialog.showCreateDialog(goalCtrl);
    }


    void bump() {
        showWarning = showWarning();
        fireIconChange();
        ((GoalChildren)getChildren()).bump();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setDisplayName(goalCtrl.getDescr());
        showWarning = showWarning();
        fireIconChange();
        if (GoalCtrl.PROP_ACHIEVED_DATE.equals(evt.getPropertyName())) {
            firePropertyChange(PropertyAchieved.NAME, evt.getOldValue(), evt.getNewValue());
        }
        createSheet();
    }

    @Override
    public void expand() {
        view.expandNode(this);
        for (Node node : getChildren().getNodes()) {
            ExpandCookie c = node.getCookie(ExpandCookie.class);
            if (c != null) {
                c.expand();
            } else {
                if (node instanceof GoalProjectsNode) {
                    view.expandNode(node);
                }
            }
        }
    }

    @Override
    public void collapse() {
        for (Node node : getChildren().getNodes()) {
            CollapseCookie c = node.getCookie(CollapseCookie.class);
            if (c != null) {
                c.collapse();
            } else {
                if (node instanceof GoalProjectsNode) {
                    view.collapseNode(node);
                }
            }
        }
        view.collapseNode(this);
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        if (goalsCtrl.existGoalProjects(goalCtrl)) {
            // show confirm dialog to remove all linked projects
            String msg = NbBundle.getMessage(getClass(), "msg.confirm.remove.projects");
            String ttl = NbBundle.getMessage(getClass(), "ttl.confirm.remove.projects");
            NotifyDescriptor descriptor = new NotifyDescriptor.Confirmation(msg, ttl, NotifyDescriptor.OK_CANCEL_OPTION);
            if (NotifyDescriptor.OK_OPTION != DialogDisplayer.getDefault().notify(descriptor)) {
                return;
            }
            goalCtrl.removeGoalProjects();
        }
        goalsCtrl.delete(supergoalCtrl.getID(), goalCtrl.getID());
    }

    @Override
    public void setTopic(Topic topic) {
        goalCtrl.setTopic(topic);
    }

    private LevelCtrl getLevelCtrl() {
        return goalCtrl.getLevel();
    }

    private void setLevel(LevelCtrl levelCtrl) {
        goalCtrl.setLevel(levelCtrl);
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
            return "<s>" + getDisplayName() + "</s>";
        } else {
            return getDisplayName();
        }
    }

    // CUT, COPY & PASTE
    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return true;
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {
        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        if (!(dropNode instanceof GoalNode)) {
            return null;
        }

        final GoalNode dropGoalNode = (GoalNode)dropNode;

        if (isCopy(action)) {
            return new PasteType() {
                @Override
                public Transferable paste() throws IOException {
                    dropGoalNode.goalCtrl.copyTo(goalCtrl);
                    return null;
                }
            };
        }
        // MOVE ACTION
        final GoalCtrl dropGoalCtrl = dropGoalNode.goalCtrl;
        final GoalCtrl dropSupergoalCtrl = dropGoalNode.supergoalCtrl;
        // can not move a goal into itself
        if (goalCtrl.equals(dropGoalCtrl)) {
            return null;
        }
        // can not move a goal to its parent
        if (goalCtrl.isParentOf(dropGoalCtrl.getID())) {
            return null;
        }
        // can not move a goal into its decendant goal
        if (goalCtrl.isDecendantOf(dropGoalCtrl.getID())) {
            return null;
        }
        return new PasteType() {
            @Override
            public Transferable paste() throws IOException {
                GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
                goalsCtrl.move(dropGoalCtrl.getID(), dropSupergoalCtrl.getID(), goalCtrl.getID());
                return null;
            }
        };
    }

    private boolean isCopy(int action) {
        // return true if not a cut or move action
        return !((action & (NodeTransfer.CLIPBOARD_CUT | DnDConstants.ACTION_MOVE)) != 0);
    }

    @Override
    protected void createPasteTypes(Transferable t, List<PasteType> s) {
        super.createPasteTypes(t, s);

        PasteType pasteType = null;
        final Node cutNode = NodeTransfer.node(t, NodeTransfer.CLIPBOARD_CUT);
        if (null != cutNode) {
            pasteType = getDropType(t, NodeTransfer.CLIPBOARD_CUT, -1);
        } else {
            pasteType = getDropType(t, DnDConstants.ACTION_COPY, -1);
        }
        if (null != pasteType) {
            s.add(pasteType);
        }
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

    /**
     * Finds the node of the given goal ID if possible.
     * @param goalID The goal ID
     * @return the node of the goal or null if it can not be found.
     */
    public Node find(Integer goalID) {
        if (goalID == goalCtrl.getID()) {
            return this;
        }
        Node result = null;
        for (Node node : getChildren().getNodes()) {
            if (node instanceof GoalNode goalNode) {
                result = goalNode.find(goalID);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public void changeLevel(LevelCtrl levelCtrl) {
        setLevel(levelCtrl);
    }

    @Override
    public void toggleHideDone() {
        GoalChildren children = (GoalChildren) getChildren();
        setHideDone(!children.isHideDone());
    }

    public void setHideDone(boolean b) {
        GoalChildren children = (GoalChildren) getChildren();
        children.setHideDone(b);
        for (Node node : children.getNodes()) {
            if (node instanceof GoalNode goalNode) {
                goalNode.setHideDone(b);
            }
        }
    }

    @Override
    public void toggleShowProjects() {
        GoalChildren children = (GoalChildren) getChildren();
        setShowProjects(!children.isShowProjects());
    }

    @Override
    public void setShowProjects(boolean b) {
        GoalChildren children = (GoalChildren) getChildren();
        children.setShowProjects(b);
        for (Node node : children.getNodes()) {
            if (node instanceof GoalNode goalNode) {
                goalNode.setShowProjects(b);
            }
        }
    }

    private boolean showWarning() {
        // no warning for root goal
        if (goalCtrl.getID() == Constants.ID_ROOT_GOAL) {
            return false;
        }
        // no warning if goal is achieved
        if (goalCtrl.isAchieved()) {
            return false;
        }
        // no warning if goal should not have projects assigned
        if (goalCtrl.getLevel() != null && !goalCtrl.getLevel().isGoalsHaveProjects()) {
            return false;
        }        
        GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        if (goalsCtrl == null) {
            return false;
        }
        // no warning if goal has an active project assigned
        Map<Integer, Project> projectsMap = getProjectsMap();
        for (Integer projectID : goalsCtrl.getGoalProjects(goalCtrl.getID())) {
            Project project = projectsMap.get(projectID);
            if (project != null && project.hasActiveDecendant()) {
                return false;
            }
        }
        return true;
    }

    private Map<Integer, Project> getProjectsMap() {
        Map<Integer, Project> map = new HashMap<>();
        for (Project project : Services.instance.getProjects()) {
            map.put(project.getID(), project);
        }
        return map;
    }

}

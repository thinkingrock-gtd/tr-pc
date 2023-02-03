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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.find.ui.FoundItem.Group;
import au.com.trgtd.tr.view.collect.CollectThoughtsAction;
import au.com.trgtd.tr.view.collect.screen.CollectThoughtsTopComponent;
import au.com.trgtd.tr.view.goals.*;
import au.com.trgtd.tr.view.projects.ProjectsAction;
import au.com.trgtd.tr.view.projects.ProjectsTreeTopComponent;
import au.com.trgtd.tr.view.projects.actions.SingleActionsTopComponent;
import au.com.trgtd.tr.view.projects.future.FutureTopComponent;
import au.com.trgtd.tr.view.reference.ReferencesAction;
import au.com.trgtd.tr.view.reference.screen.ReferencesTopComponent;
import au.com.trgtd.tr.view.someday.FuturesAction;
import au.com.trgtd.tr.view.someday.screen.SomedaysTopComponent;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Sheet;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.future.Future;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;

public class FoundNode extends AbstractNode implements ViewCookie {

    private final FoundItem item;

    public FoundNode(FoundItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
    }

    @Override
    public String toString() {
        return getHtmlDisplayName();
    }

    @Override
    public String getName() {
        return getHtmlDisplayName();
    }

    @Override
    public String getDisplayName() {
        return getHtmlDisplayName();
    }

    @Override
    public String getHtmlDisplayName() {
        if (item.isDone()) {
//          return "<HTML><strike>" + item.toString() + "</strike></HTML>";
            return "<s>" + item.toString() + "</s>";
        } else {
            return item.toString();
        }
    }

//    @Override
//    protected Sheet createSheet() {
//        Sheet result = super.createSheet();
//        Sheet.Set set = Sheet.createPropertiesSet();
//        set.put(new PropertyDoneDate(item));
//        set.put(new PropertyCreatedDate(item));
//        set.put(new PropertyPathIcon(item));
//        set.put(new PropertyPathString(item));
//        result.put(set);
//        return result;
//    }
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set ss = sheet.get(Sheet.PROPERTIES);
        if (ss == null) {
            ss = Sheet.createPropertiesSet();
            ss.put(new PropertyDoneDate(item));
            ss.put(new PropertyCreatedDate(item));
            ss.put(new PropertyPathIcon(item));
            ss.put(new PropertyPathString(item));
            sheet.put(ss);
        }
        return sheet;
    }

    @Override
    public Image getIcon(int type) {
        return item.getIcon().getImage();
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ViewCookie.class) {
            return this;
        }
        Children ch = getChildren();
        if (clazz.isInstance(ch)) {
            return (Cookie) ch;
        }
        return super.getCookie(clazz);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
            SystemAction.get(ViewAction.class)
        };
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(ViewAction.class);
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

    private ProjectsAction projectsAction;

    private ProjectsAction getProjectsAction() {
        if (projectsAction == null) {
            projectsAction = SystemAction.get(ProjectsAction.class);
        }
        return projectsAction;
    }

    private void open(final Project project, final Group group) {
        if (getProjectsAction() == null) {
            return;
        }

        projectsAction.performAction();

        EventQueue.invokeLater(() -> {
            switch (group) {
                case Projects: {
                    ProjectsTreeTopComponent tc = ProjectsTreeTopComponent.findInstance();
                    if (tc != null) {
                        tc.setShowDone(project.isDone());
                        tc.select(project);
                    }
                    break;
                }
                case Futures: {
                    FutureTopComponent tc = FutureTopComponent.findInstance();
                    if (tc != null) {
                        tc.setShowDone(project.isDone());
                        tc.select(project);
                    }
                    break;
                }
            }
        });
    }

    private void open(final tr.model.action.Action action, final Group group) {
        if (getProjectsAction() == null) {
            return;
        }

        projectsAction.performAction();

        EventQueue.invokeLater(() -> {
            switch (group) {
                case Projects: {
                    ProjectsTreeTopComponent tc = ProjectsTreeTopComponent.findInstance();
                    if (tc != null) {
                        tc.setShowDone(action.isDone());
                        tc.select(action);
                    }
                    break;
                }
                case SingleActions: {
                    SingleActionsTopComponent tc = SingleActionsTopComponent.findInstance();
                    if (tc != null) {
                        tc.setShowDone(action.isDone());
                        tc.select(action);
                    }
                    break;
                }
                case Futures: {
                    FutureTopComponent tc = FutureTopComponent.findInstance();
                    if (tc != null) {
                        tc.setShowDone(action.isDone());
                        tc.select(action);
                    }
                    break;
                }
            }
        });
    }

    private CollectThoughtsAction thoughtsAction;

    private CollectThoughtsAction getThoughtsAction() {
        if (thoughtsAction == null) {
            thoughtsAction = SystemAction.get(CollectThoughtsAction.class);
        }
        return thoughtsAction;
    }

    private void open(final Thought thought) {
        if (getThoughtsAction() == null) {
            return;
        }

        thoughtsAction.performAction();

        EventQueue.invokeLater(() -> {
            CollectThoughtsTopComponent tc = CollectThoughtsTopComponent.findInstance();
            if (tc != null) {
                tc.select(thought);
            }
        });
    }

    private ReferencesAction referencesAction;

    private ReferencesAction getReferencesAction() {
        if (referencesAction == null) {
            referencesAction = SystemAction.get(ReferencesAction.class);
        }
        return referencesAction;
    }

    private void open(final Information reference) {
        if (getReferencesAction() == null) {
            return;
        }

        referencesAction.performAction();

        EventQueue.invokeLater(() -> {
            ReferencesTopComponent tc = ReferencesTopComponent.findInstance();
            if (tc != null) {
                tc.select(reference);
            }
        });
    }

    private FuturesAction futuresAction;

    private FuturesAction getFuturesAction() {
        if (futuresAction == null) {
            futuresAction = SystemAction.get(FuturesAction.class);
        }
        return futuresAction;
    }

    private void open(final Future future) {
        if (getFuturesAction() == null) {
            return;
        }

        futuresAction.performAction();

        EventQueue.invokeLater(() -> {
            SomedaysTopComponent tc = SomedaysTopComponent.findInstance();
            if (tc != null) {
                tc.select(future);
            }
        });
    }

    private GoalsAction goalsAction;

    private GoalsAction getGoalsAction() {
        if (goalsAction == null) {
            goalsAction = SystemAction.get(GoalsAction.class);
        }
        return goalsAction;
    }

    private void open(final GoalCtrl goalCtrl) {
        if (getGoalsAction() == null) {
            return;
        }

        goalsAction.performAction();

        EventQueue.invokeLater(() -> {
            GoalsTopComponent tc = GoalsTopComponent.findInstance();
            if (tc != null) {
                tc.select(goalCtrl);
            }
        });
    }

    @Override
    public void view() {
        switch (item.type) {
            case Action: {
                open(item.action, item.group);
                break;
            }
            case Project: {
                open(item.project, item.group);
                break;
            }
            case Thought: {
                open(item.thought);
                break;
            }
            case Someday: {
                open(item.someday);
                break;
            }
            case Reference: {
                open(item.reference);
                break;
            }
            case Goal: {
                open(item.goalCtrl);
                break;
            }
        }
    }

}

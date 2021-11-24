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

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.services.Services;
import java.util.Date;
import javax.swing.ImageIcon;
import tr.model.action.Action;
import tr.model.future.Future;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;

public class FoundItem {

    public enum Type {
        Action, Project, Thought, Reference, Someday, Goal;
    }
    public enum Group {
        SingleActions, Projects, Futures, Templates, Thoughts, References, Somedays, Goals;
    }

    public final Type type;
    public final Group group;
    public Action action;
    public Project project;
    public Thought thought;
    public Information reference;
    public Future someday;
    public GoalCtrl goalCtrl;

    public FoundItem(Action action, Group group) {
        this.type = Type.Action;
        this.action = action;
        this.group = group;
    }

    public FoundItem(Project project, Group group) {
        this.type = Type.Project;
        this.project = project;
        this.group = group;
    }

    public FoundItem(Thought thought) {
        this.type = Type.Thought;
        this.thought = thought;
        this.group = Group.Thoughts;
    }

    public FoundItem(Information reference) {
        this.type = Type.Reference;
        this.reference = reference;
        this.group = Group.References;
    }

    public FoundItem(Future someday) {
        this.someday = someday;
        this.type = Type.Someday;
        this.group = Group.Somedays;
    }

    public FoundItem(GoalCtrl goalCtrl) {
        this.goalCtrl = goalCtrl;
        this.type = Type.Goal;
        this.group = Group.Goals;
    }

    @Override
    public String toString() {
        switch (type) {
            case Action:
                return action.getDescription();
            case Project:
                return project.getDescription();
            case Thought:
                return thought.getDescription();
            case Reference:
                return reference.getDescription();
            case Someday:
                return someday.getDescription();
            case Goal:
                return goalCtrl.getDescr();
        }
        return "";
    }

    public boolean isDone() {
        switch (type) {
            case Action:
                return action.isDone();
            case Project:
                return project.isDone();
            case Goal:
                return goalCtrl.getAchievedDate() != null;
            default:
                return false;
        }
    }

    public ImageIcon getIcon() {
        switch (type) {
            case Action:
                return action.getIcon(false);
            case Project:
                return project.getIcon(false);
            case Thought:
                return Icons.Thought;
            case Reference:
                return Icons.Reference;
            case Someday:
                return Icons.SomedayMaybe;
            case Goal:
                return goalCtrl.getLevel().getGoalsIcon().icon;
        }
        return null;
    }

    public Date getCreatedDate() {
        switch (type) {
            case Action:
                return action.getCreated();
            case Project:
                return project.getCreated();
            case Thought:
                return thought.getCreated();
            case Reference:
                return reference.getCreated();
            case Someday:
                return someday.getCreated();
            case Goal:
                return goalCtrl.getCreatedDate();
        }
        return null;
    }

    public Date getDoneDate() {
        switch (type) {
            case Action:
                return action.getDoneDate();
            case Project:
                return project.getDoneDate();
            case Goal:
                return goalCtrl.getAchievedDate();
        }
        return null;
    }
    
    public String getPath() {
        switch (type) {
            case Action:
                return Services.instance.getPath(action);
            case Project:
                return Services.instance.getPath(project);
            case Thought:
                return "Thought";
            case Reference:
                return "Reference";
            case Someday:
                return "Someday-Maybe";
            case Goal:
                return goalCtrl.getPath();
        }
        return null;
    }

    public ImageIcon getPathIcon() {
        switch (type) {
            case Action: {
                if (action == null) return null;
                Project parent = (Project)action.getParent();
                return parent == null ? null : parent.getIcon(false);
            }
            case Project:
                if (project == null) return null;
                Project parent = (Project)project.getParent();
                return parent == null ? null : parent.getIcon(false);
            case Thought:
                return Icons.Collect;
            case Reference:
                return Icons.References;
            case Someday:
                return Icons.SomedayMaybes;
            case Goal:
                return goalCtrl.getLevel().getGoalsIcon().icon;
        }
        return null;
    }

}

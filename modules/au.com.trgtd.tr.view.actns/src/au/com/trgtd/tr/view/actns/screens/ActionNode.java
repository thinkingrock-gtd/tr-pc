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
package au.com.trgtd.tr.view.actns.screens;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.Criteria;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Recurrence;
import tr.model.context.Context;
import tr.model.context.ContextChangeCookie;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.ChangeStatusAction;
import au.com.trgtd.tr.view.ChangeStatusCookie;
import au.com.trgtd.tr.view.CriteriaChangeAction;
import au.com.trgtd.tr.view.CriteriaChangeCookie;
import au.com.trgtd.tr.view.DeleteAction;
import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.ProjectChangeAction;
import au.com.trgtd.tr.view.ProjectChangeCookie;
import au.com.trgtd.tr.view.ReprocessAction;
import au.com.trgtd.tr.view.ReprocessCookie;
import au.com.trgtd.tr.view.contexts.ContextChangeAction;
import au.com.trgtd.tr.view.projects.PostponeActionAction;
import au.com.trgtd.tr.view.projects.PostponeActionAction.Periods;
import au.com.trgtd.tr.view.projects.PostponeActionCookie;
import au.com.trgtd.tr.view.projects.SetDoneAction;
import au.com.trgtd.tr.view.projects.SetDoneCookie;
import au.com.trgtd.tr.view.topics.TopicChangeAction;

/**
 * Node for a TR action.
 *
 * @author Jeremy Moore
 */
public class ActionNode extends AbstractNode
        implements TopicChangeCookie, ContextChangeCookie, DeleteCookie,
        SetDoneCookie, ReprocessCookie, PostponeActionCookie,
        CriteriaChangeCookie, ProjectChangeCookie, ChangeStatusCookie {

    public final Action action;

    /** Constructs a new instance. */
    public ActionNode(Action action) {
        super(Children.LEAF, Lookups.singleton(action));
        this.action = action;
    }

    @Override
    public String toString() {
        return action.getDescription();
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[]{
                    SystemAction.get(ReprocessAction.class),
                    SystemAction.get(PostponeActionAction.class),
                    SystemAction.get(ContextChangeAction.class),
                    SystemAction.get(CriteriaChangeAction.class),
                    SystemAction.get(TopicChangeAction.class),
                    SystemAction.get(ChangeStatusAction.class),
                    SystemAction.get(ProjectChangeAction.class),
                    SystemAction.get(SetDoneAction.class),
                    SystemAction.get(DeleteAction.class),};
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == DeleteCookie.class) {
            return this;
        }
        if (clazz == TopicChangeCookie.class) {
            return this;
        }
        if (clazz == ContextChangeCookie.class) {
            return this;
        }
        if (clazz == SetDoneCookie.class) {
            return this;
        }
        if (clazz == ReprocessCookie.class) {
            return this;
        }
        if (clazz == PostponeActionCookie.class) {
            return this;
        }
        if (clazz == CriteriaChangeCookie.class) {
            return this;
        }
        if (clazz == ProjectChangeCookie.class) {
            return this;
        }
        if (clazz == ChangeStatusCookie.class) {
            return this;
        }
        return super.getCookie(clazz);
    }

    public Action getAction() {
        return action;
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        action.getParent().remove(action);
    }

    public void setDone() {
        action.setDone(true);
    }

    public void reprocess() {
        if (action == null) {
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        // delete the action
        action.removeFromParent();

        // add the new thought to the front of the thoughts
        Thought thought = new Thought(data.getNextID());
        thought.setDescription(action.getDescription());
        thought.setTopic(action.getTopic());
        thought.setAction(action);
        data.getThoughtManager().insert(thought, 0);
    }

    public void setTopic(Topic topic) {
        action.setTopic(topic);
    }

    public void setContext(Context context) {
        action.setContext(context);
    }

    private Date add(Date date, int n, Periods periods) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        switch (periods) {
            case DAYS: {
                calendar.add(Calendar.DAY_OF_YEAR, n);
                break;
            }
            case WEEKS: {
                calendar.add(Calendar.WEEK_OF_YEAR, n);
                break;
            }
            case MONTHS: {
                calendar.add(Calendar.MONTH, n);
                break;
            }
            case YEARS: {
                calendar.add(Calendar.YEAR, n);
                break;
            }
        }
        return calendar.getTime();
    }

    public void postponeAction(int n, Periods periods) {
        switch (action.getState().getType()) {
            case DOASAP: {
                action.setDueDate(add(action.getDueDate(), n, periods));
                break;
            }
            case INACTIVE: {
                action.setStartDate(add(action.getStartDate(), n, periods));
                break;
            }
            case SCHEDULED: {
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                state.setDate(add(state.getDate(), n, periods));
                break;
            }
            case DELEGATED: {
                ActionStateDelegated state = (ActionStateDelegated) action.getState();
                state.setDate(add(state.getDate(), n, periods));
                break;
            }
        }
    }

    public void postponeAction(Date date) {
        switch (action.getState().getType()) {
            case DOASAP: {
                action.setDueDate(date);
                break;
            }
            case INACTIVE: {
                action.setStartDate(date);
                break;
            }
            case SCHEDULED: {
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                state.setDate(date);
                break;
            }
            case DELEGATED: {
                ActionStateDelegated state = (ActionStateDelegated) action.getState();
                state.setDate(date);
                break;
            }
        }
    }

    public void changeCriteria(Criteria criteria, Value value) {
        if (criteria == Criteria.Time) {
            action.setTime(value);
        } else if (criteria == Criteria.Energy) {
            action.setEnergy(value);
        }
        if (criteria == Criteria.Priority) {
            action.setPriority(value);
        }
    }

    public void changeProject(Project project) {
        // ignore move to same project
        if (Utils.equal(action.getParent(), project)) {
            return;
        }
        // if action is recurrenct then remove recurrence definition
        Recurrence r = action.getRecurrence();
        if (r != null && !r.getProject().equals(project)) {
            ((ActionStateScheduled)action.getState()).setRecurrence(null);
        }

        if (project == null) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data != null) {
                action.removeFromParent();
                data.getRootActions().add(action);
            }
        } else {
            action.removeFromParent();
            project.add(action);
        }
    }

    public boolean isRecurrent() {
        return action.getRecurrence() != null;
    }

    public void changeStatus(ActionState state) {
        action.setState(state);
    }



    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        action.getParent().remove(action);
    }

}

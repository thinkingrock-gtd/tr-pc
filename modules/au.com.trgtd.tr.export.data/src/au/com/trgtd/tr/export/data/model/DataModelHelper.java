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
package au.com.trgtd.tr.export.data.model;

import au.com.trgtd.tr.appl.Constants;
import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_CONTEXT;
import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_TOPIC;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.actor.Actor;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

class DataModelHelper {

    public ContactBean convertContact(Actor a) {
        return new ContactBean(
                a.getID(),
                a.getName(),
                a.getEmail(),
                a.isTeam(),
                a.isInactive());
    }

    public ContextBean convertContext(Context c) {
        return new ContextBean(
                c.getID(),
                c.getName(),
                c.getDescription());
    }

    public EnergyBean convertEnergy(Value v, int order) {
        return new EnergyBean(
                v.getID(),
                v.getName(),
                order);
    }

    public PriorityBean convertPriority(Value v, int order) {
        return new PriorityBean(
                v.getID(),
                v.getName(),
                v.getMappedValue(),
                order);
    }

    public TopicBean convertTopic(Topic t) {
        return new TopicBean(
                t.getID(),
                t.getName(),
                t.getDescription(),
                t.getBackground(),
                t.getForeground());
    }

    public ReferenceBean convertReference(Information info) {
        return new ReferenceBean(
                info.getID(),
                info.getDescription(),
                info.getNotes(),
                id(info.getTopic()),
                info.getCreated());
    }

    public SomedayBean convertSomeday(Future f) {
        return new SomedayBean(
                f.getID(),
                f.getDescription(),
                f.getNotes(),
                id(f.getThought()),
                id(f.getTopic()),
                f.getCreated(),
                f.getTickle());
    }

    public ThoughtBean convertThought(Thought t) {
        return new ThoughtBean(
                t.getID(),
                t.getCreated(),
                t.getDescription(),
                t.getNotes(),
                id(t.getTopic()),
                id(t.getAction()),
                t.isProcessed(),
                t.getDelegationType(),
                t.getDelegationActionID(),
                t.getDelegationDone(),
                t.getDelegationReply());
    }

    public ItemBean convertItem(Item item, int order) {
        if (item instanceof Action action) {
            return convertAction(action, order);
        }
        if (item instanceof Project project) {
            return convertProject(project, order);
        }
        throw new IllegalArgumentException("Item is not an action or a project.");
    }

    public ActionBean convertAction(Action a, int order) {
        return new ActionBean(
                a.getID(),
                a.getDescription(),
                a.getNotes(),
                a.getSuccess(),
                id(a.getParent()),
                order,
                id(a.getThought()),
                id(a.getTopic()),
                id(a.getContext()),
                id(a.getEnergy()),
                id(a.getPriority()),
                time(a.getTime()),
                a.getCreated(),
                startDate(a),
                dueDate(a),
                a.getDoneDate(),
                a.getState().getType().ordinal(),
                delegateID(a),
                delegateName(a),
                delegateDate(a)
        );
    }

    public ProjectBean convertProject(Project p, int order) {
        return new ProjectBean(
                p.getID(),
                p.getDescription(),
                p.getNotes(),
                p.getPurpose(),
                p.getVision(),
                p.getBrainstorming(),
                p.getOrganising(),
                order,
                id(p.getParent()),
                id(p.getThought()),
                id(p.getTopic()),
                id(p.getPriority()),
                p.getCreated(),
                p.getStartDate(),
                p.getDueDate(),
                p.getDoneDate()
        );
    }

    private Integer id(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Action action) {
            return action.getID();
        }
        if (object instanceof Project project) {
            int id = project.getID();
            return id < Constants.ID_MAX_SPECIAL ? null : id;
        }
        if (object instanceof Actor actor) {
            return actor.getID();
        }
        if (object instanceof Context context) {
            int id = context.getID();
            return id == ID_DEFAULT_CONTEXT ? null : id;
        }
        if (object instanceof Topic topic) {
            int id = topic.getID();
            return id == ID_DEFAULT_TOPIC ? null : id;
        }
        if (object instanceof Thought thought) {
            return thought.getID();
        }
        if (object instanceof Value value) {
            return value.getID();
        }
        throw new IllegalArgumentException("Object not recognized.");
    }

    private Long time(Value v) {
        return v == null ? null : parseTime(v.getName());
    }

    private long parseTime(String s) {
        return Math.round(parseDays(s) * 1440) + Math.round(parseHours(s) * 60) + parseMins(s);
    }

    private float parseDays(String string) {
        Pattern p = Pattern.compile(".*?(\\d+(?:\\.\\d+)?)\\s*[Dd]+.*");
        Matcher m = p.matcher(string);
        if (m.matches() && m.groupCount() > 0) {
            try {
                return Float.parseFloat(m.group(1));
            } catch (Exception e) {
                return 0f;
            }
        }
        return 0f;
    }

    private float parseHours(String string) {
        Pattern p = Pattern.compile(".*?(\\d+(?:\\.\\d+)?)\\s*[Hh]+.*");
        Matcher m = p.matcher(string);
        if (m.matches() && m.groupCount() > 0) {
            try {
                return Float.parseFloat(m.group(1));
            } catch (Exception e) {
                return 0f;
            }
        }
        return 0f;
    }

    private int parseMins(String string) {
        Pattern p = Pattern.compile(".*?(\\d+)\\s*[Mm]+.*");
        Matcher m = p.matcher(string);
        if (m.matches() && m.groupCount() > 0) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    private Date startDate(Action a) {
        switch (a.getState().getType()) {
            case DOASAP:
            case INACTIVE:
            case DELEGATED:
                return a.getStartDate();
            case SCHEDULED:
                return ((ActionStateScheduled) a.getState()).getDate();
            default:
                throw new IllegalArgumentException("Action state not recognized.");
        }
    }

    private Date dueDate(Action a) {
        switch (a.getState().getType()) {
            case DOASAP:
            case INACTIVE:
            case DELEGATED:
                return a.getDueDate();
            case SCHEDULED:
                return null;
            default:
                throw new IllegalArgumentException("Action state not recognized.");
        }
    }

    private Integer delegateID(Action a) {
        switch (a.getState().getType()) {
            case DOASAP:
            case INACTIVE:
            case SCHEDULED:
                return null;
            case DELEGATED:
                return ((ActionStateDelegated) a.getState()).getActorID();
            default:
                throw new IllegalArgumentException("Action state not recognized.");
        }
    }

    private String delegateName(Action a) {
        switch (a.getState().getType()) {
            case DOASAP:
            case INACTIVE:
            case SCHEDULED:
                return null;
            case DELEGATED:
                return ((ActionStateDelegated) a.getState()).getTo();
            default:
                throw new IllegalArgumentException("Action state not recognized.");
        }
    }

    private Date delegateDate(Action a) {
        switch (a.getState().getType()) {
            case DOASAP:
            case INACTIVE:
            case SCHEDULED:
                return null;
            case DELEGATED:
                return ((ActionStateDelegated) a.getState()).getDate();
            default:
                throw new IllegalArgumentException("Action state not recognized.");
        }
    }
}

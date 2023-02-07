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
package au.com.trgtd.tr.sync.iphone;

import au.com.trgtd.tr.services.Services;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.actor.Actor;
import tr.model.context.Context;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.topic.Topic;

public abstract class SyncHandler {

    protected final static Logger LOG = Logger.getLogger(SyncHandler.class.getName());
    protected static final DateFormat DF = new SimpleDateFormat("yyyyMMdd");
    protected static final DateFormat DF_HHmm = new SimpleDateFormat("HHmm");
    protected final BufferedReader in;
    protected final PrintWriter out;
    protected final SyncProgress progress;
    protected boolean cancelSyncing;
    protected int nbrThoughtsToGet;
    protected int nbrActionsToGet;
    protected int nbrActionsGot;
    protected int nbrThoughtsGot;
    protected int nbrContextsToSend;
    protected int nbrContextsSent;
    protected int nbrTopicsToSend;
    protected int nbrTopicsSent;
    protected int nbrActionsToSend;
    protected int nbrActionsSent;
    protected int nbrTimesToSend;
    protected int nbrTimesSent;
    protected int nbrEnergiesToSend;
    protected int nbrEnergiesSent;
    protected int nbrPrioritiesToSend;
    protected int nbrPrioritiesSent;
    protected int nbrReferencesToSend;      // [JM 25/08/11]
    protected int nbrReferencesSent;        // [JM 25/08/11]
    protected int nbrProjectsToSend;        // [JM 25/08/11]
    protected int nbrProjectsSent;          // [JM 25/08/11]

    public SyncHandler(BufferedReader in, PrintWriter out, SyncProgress progress) {
        this.in = in;
        this.out = out;
        this.progress = progress;
    }

    protected void cancel() {
        this.cancelSyncing = true;
    }
    private Data data;

    protected Data getData() {
        if (data == null) {
            data = DataLookup.instance().lookup(Data.class);
        }
        return data;
    }
    private List<Context> contexts;

    protected List<Context> getContexts() {
        if (contexts == null) {
            if (getData() == null) {
                contexts = Collections.emptyList();
            } else {
                contexts = getData().getContextManager().list();
                Collections.sort(contexts);
            }
        }
        return contexts;
    }

    private List<Topic> topics;
    protected List<Topic> getTopics() {
        if (topics == null) {
            if (getData() == null) {
                topics = Collections.emptyList();
            } else {
                topics = getData().getTopicManager().list();
                Collections.sort(topics);
            }
        }
        return topics;
    }
    // [JM 25/08/11]
    private List<Information> references;
    protected List<Information> getReferences() {
        if (references == null) {
            if (getData() == null) {
                references = Collections.emptyList();
            } else {
                references = getData().getInformationManager().list();
                Collections.sort(references, referencesComparator);
            }
        }
        return references;
    }
    
    private final Comparator<Information> referencesComparator = (Information t1, Information t2) ->
            t1.getDescription().compareToIgnoreCase(t2.getDescription());
    
    private List<Project> projects;
    protected List<Project> getProjects() {
        if (projects == null) {
            if (getData() == null) {
                projects = Collections.emptyList();
            } else {
//              projects = Services.instance.getProjects();
                projects = Services.instance.getProjectsInPathOrder();
                for (Iterator<Project> iterator = projects.iterator(); iterator.hasNext();) {
                    Project project = iterator.next();
                    if (project.isDone()) {
                        iterator.remove();
                    }
                }
            }
        }
        return projects;
    }
    // End [JM 25/08/11]

    private Map<Integer, Project> projectsMap;
    protected Map<Integer, Project> getProjectsMap() {
        if (projectsMap == null) {
            if (getData() == null) {
                projectsMap = Collections.emptyMap();
            } else {
                projectsMap = new HashMap<>();
                for (Project p : getProjects()) {
                    projectsMap.put(p.getID(), p);                    
                }
            }
        }
        return Collections.unmodifiableMap(projectsMap);
    }

    private Map<Integer, Information> referencesMap;
    protected Map<Integer, Information> getReferencesMap() {
        if (referencesMap == null) {
            if (getData() == null) {
                referencesMap = Collections.emptyMap();
            } else {
                referencesMap = new HashMap<>();
                for (Information ref : getReferences()) {
                    referencesMap.put(ref.getID(), ref);                    
                }
            }
        }
        return Collections.unmodifiableMap(referencesMap);
    }
    
    protected List<Action> actions;
    protected List<Action> getActions() {
        if (actions == null) {
            actions = Services.instance.getAllActions();
            for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext();) {
                Action action = iterator.next();
                if (action.isDone() || !action.isStateASAP()) {
                    iterator.remove();
                }
            }
        }
        return actions;
    }

    protected void updateActionsToSend() {
        if (actions == null) {
            return;
        }
        for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext();) {
            Action action = iterator.next();
            if (action.isDone()) {
                iterator.remove();
            }
        }
    }

    protected Topic getTopic(String idString) {
        if (idString == null || idString.length() < 1) {
            return Topic.getDefault();
        }
        try {
            int id = Integer.parseInt(idString);
            for (Topic topic : data.getTopicManager().list()) {
                if (topic.getID() == id) {
                    return topic;
                }

            }
            throw new IllegalArgumentException();
        } catch (Exception ex) {
            System.out.println("ERROR Invalid Topic ID: " + idString);
        }
        return Topic.getDefault();
    }

    protected void updateProgress(int done, int todo) {
        if (todo > 0) {
            int percent = (done / todo) * 100;
            progress.setProgress(percent > 100 ? 100 : percent);
        } else {
            progress.setProgress(0);
        }
    }

    protected static synchronized String log(String x) {
        LOG.log(Level.INFO, "Sent:{0}", x);
        return x;
    }
    private Boolean usesTime;

    protected boolean isTimeUsed() {
        if (usesTime == null) {
            usesTime = getData() != null && getData().getTimeCriterion().isUse();
        }
        return usesTime;
    }
    private Boolean usesEnergy;

    protected boolean isEnergyUsed() {
        if (usesEnergy == null) {
            usesEnergy = getData() != null && getData().getEnergyCriterion().isUse();
        }
        return usesEnergy;
    }
    private Boolean usesPriority;

    protected boolean isPriorityUsed() {
        if (usesPriority == null) {
            usesPriority = getData() != null && getData().getPriorityCriterion().isUse();
        }
        return usesPriority;
    }

    protected String getTimeID(Action action) {
        return action.getTime() == null ? "" : "" + action.getTime().getID();
    }

    protected String getEnergyID(Action action) {
        return action.getEnergy() == null ? "" : "" + action.getEnergy().getID();
    }

    protected String getPriorityID(Action action) {
        return action.getPriority() == null ? "" : "" + action.getPriority().getID();
    }

    protected String getPriorityID(Project project) {
        return project.getPriority() == null ? "" : "" + project.getPriority().getID();
    }

    private Map<Integer, Actor> delegatesMap;
    private Map<Integer, Actor> getDelegatesMap() {
        if (null == delegatesMap) {
            delegatesMap = new HashMap<>();
            for (Actor actor : getData().getActorManager().list()) {
                delegatesMap.put(actor.getID(), actor);
            }
        }
        return delegatesMap;
    }

    public Actor getDelegate(int id) {
        return getDelegatesMap().get(id);
    }

}

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
package au.com.trgtd.tr.services;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.actor.Actor;
import tr.model.criteria.Value;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * TR Services.
 *
 * @author Jeremy Moore
 */
public class Services {
    
    private static final Logger LOG = Logger.getLogger("tr.services");
    
    /** Singleton instance. */
    public static final Services instance = new Services();
    
    private final Lookup.Result lookupResult;
    private final ActionsObserver actionsObserver;
    private final ObservableImpl actionsObservable;
    
    private Services() {
        actionsObserver = new ActionsObserver();
        actionsObservable = new ObservableImpl() {};
        dataChanged();
        
        lookupResult = DataLookup.instance().lookupResult(Data.class);
        lookupResult.addLookupListener((LookupEvent lookupEvent) -> {
            dataChanged();
        });
    }
    
    private void dataChanged() {
        actionsObservable.removeObservers();
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            data.getRootActions().addObserver(actionsObserver);
            data.getRootProjects().addObserver(actionsObserver);
        }
    }
    
    /**
     * Gets all single actions (i.e. actions that are not within a project).
     * @return a list containing all single actions.
     */
    public List<Action> getSingleActions() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        
        return data.getRootActions().getChildren(Action.class);
    }
    
    /**
     * Gets all actions that are in a project.
     * @return a list containing project actions.
     */
    public List<Action> getProjectActions() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        
        List<Action> actions = new ArrayList<>();
        
        for (Project project : getProjects()) {
            actions.addAll(project.getChildren(Action.class));
        }
        
        return actions;
    }
    
    
    /**
     * Gets all projects in path order.
     * @return a list containing all projects.
     */
    public List<Project> getProjectsInPathOrder() {        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        List<Project> projects = new ArrayList<>();
        addProjectsInPathOrder(data.getRootProjects(), projects);
        return projects;
    }
    
    private void addProjectsInPathOrder(Project project, List<Project> projects) {
        for (Project subproject : project.getChildren(Project.class)) {
            projects.add(subproject);
            addProjectsInPathOrder(subproject, projects);
        }
    }
    
    
    
    
    /**
     * Gets all actions.
     * @return a list containing all actions.
     */
    public List<Action> getAllActions() {
        List<Action> actions = new ArrayList<>();
        actions.addAll(getProjectActions());
        actions.addAll(getSingleActions());
        return actions;
    }
    
    /**
     * Gets all projects.
     * @return a list containing all projects.
     */
    public List<Project> getProjects() {
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        
        List<Project> projects = new ArrayList<>();
        
        addSubprojects(data.getRootProjects(), projects);
        
        return projects;
    }

    /**
     * Gets future projects.
     * @return a list containing future projects.
     */
    public List<Project> getFutureProjects() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        List<Project> projects = new ArrayList<>();
        addSubprojects(data.getRootFutures(), projects);
        return projects;
    }

    /**
     * Gets actions that are in a future project.
     * @return a list containing future project actions.
     */
    public List<Action> getFutureActions() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        List<Action> actions = new ArrayList<>();
        for (Project project : getFutureProjects()) {
            actions.addAll(project.getChildren(Action.class));
        }
        return actions;
    }

    private void addSubprojects(Project project, List<Project> projects) {       
        List<Project> subprojects = project.getChildren(Project.class);        
        projects.addAll(subprojects);        
        for (Project subproject : subprojects) {
            addSubprojects(subproject, projects);
        }
    }
    
    /**
     * Gets all descendant actions of a project.
     * @param project the given project.
     * @return a list of descendant actions.
     */
    private List<Action> decendants;
    public synchronized List<Action> getActionDecendants(Project project) {
        decendants = new ArrayList<>();
        _getActionDecendants(project);
        return decendants;
    }

    /* Gets all decendant actions for a project recursively. */
    private void _getActionDecendants(Project project) {
        if (project == null) {
            return;
        }
        decendants.addAll(project.getChildren(Action.class));
        for (Project subProject : project.getChildren(Project.class)) {
            _getActionDecendants(subProject);
        }
    }
            
    private final class ActionsObserver implements Observer {
        @Override
        public void update(Observable observable, Object argument) {
            actionsObservable.notifyObservers(observable, argument);
        }
    }
    
    public void addActionsObserver(Observer observer) {
        actionsObservable.addObserver(observer);
    }
    
    public void removeActionsObserver(Observer observer) {
        actionsObservable.removeObserver(observer);
    }

    /**
     * Gets processed thoughts.
     * @return a list containing processed thoughts.
     */
    public List<Thought> getThoughtsProcessed() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        List<Thought> thoughts = new ArrayList<>();
        for (Thought thought : data.getThoughtManager().list()) {
            if (thought.isProcessed()) {
                thoughts.add(thought);
            }
        }
        return thoughts;
    }

    /**
     * Gets unprocessed thoughts.
     * @return a list containing unprocessed thoughts.
     */
    public List<Thought> getThoughtsUnprocessed() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        List<Thought> thoughts = new ArrayList<>();
        for (Thought thought : data.getThoughtManager().list()) {
            if (!thought.isProcessed()) {
                thoughts.add(thought);
            }
        }
        return thoughts;
    }

    /**
     * Gets all thoughts.
     * @return a list containing all thoughts.
     */
    public List<Thought> getThoughtsAll() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        return data.getThoughtManager().list();
    }

    /**
     * Gets references.
     * @return a list containing all references.
     */
    public List<Information> getReferences() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        return data.getInformationManager().list();
    }

    /**
     * Gets someday/maybe items.
     * @return a list containing all someday/maybe items.
     */
    public List<Future> getSomedayMaybes() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Collections.emptyList();
        }
        return data.getFutureManager().list();
    }

    public String getPath(Action action) {
        return action == null ? "" : getPath((Project)action.getParent());
    }

    public String getPath(Project project) {
        return project == null ? "" : getPath(project, "");
    }

    private String getPath(Project project, String path) {
        if (path.length() > 0) {
            path = project.getDescription() + "/" + path;
        } else {
            path = project.getDescription();
        }
        Project parent = (Project)project.getParent();
        if (!parent.isRoot()) {
            return getPath(parent, path);
        }
        return path;
    }

    /**
     * Determines whether a given delegate is used by one or more actions. If
     * the includeDone option is true, done actions will be checked, otherwise
     * only actions that are not done will be checked.
     * @param actor The delegate.
     * @param includeDone The include done action option.
     * @return True if the delegate is in use in the specified actions.
     */
    public boolean isUsed(Actor actor, boolean includeDone) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return true; // as a precaution
        }
        return isUsed(actor, data.getRootActions(), includeDone)
            || isUsed(actor, data.getRootProjects(), includeDone)
            || isUsed(actor, data.getRootFutures(), includeDone)
            || isUsed(actor, data.getRootTemplates(), includeDone);
    }
    private boolean isUsed(Actor actor, Project project, boolean includeDone) {
        for (Action action : getActionDecendants(project)) {
            if (isUsed(actor, action, includeDone)) {
                return true;
            }
        }
        return false;
    }
    private boolean isUsed(Actor actor, Action action, boolean includeDone) {
        if (action.isDone() && !includeDone) {
            return false;
        }
        if (action.isStateDelegated()) {
            ActionStateDelegated state = (ActionStateDelegated)action.getState();
            if (state.getActorID() == actor.getID()) {
                return true;
            }
        }
        return false;
    }

    public Actor getActor(String name) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            for (Actor actor : data.getActorManager().list()) {
                if (actor.getName().equalsIgnoreCase(name)) {
                    return actor;
                }
            }
        }
        return null;
    }

    public Action getAction(Integer id) {
        if (id != null) {
            for (Action action : getAllActions()) {
                if (action.getID() == id) {
                    return action;
                }
            }
        }        
        return null;
    }

    public Information getReference(int id) {
        for (Information info : getReferences()) {
            if (info.getID() == id) {
                return info;
            }
        }
        return null;
    }
    
    /**
     * Gets the topic for the given topic ID. If the topic is not found then the
     * default topic is returned. 
     * @param id The topic ID.
     * @return The topic.
     */
    public Topic getTopic(Integer id) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null || id == null) {
            return Topic.getDefault();
        }
        for (Topic topic : data.getTopicManager().list()) {
            if (topic.getID() == id) {
                return topic;
            }
        }
        return Topic.getDefault();
    }

    /**
     * Attempts to find a topic by its name. 
     * @param name The topic name.
     * @return The topic if found, the default topic if not found.
     */
    public Topic getTopicByName(String name) {
        if (StringUtils.isBlank(name)) {
            return Topic.getDefault();
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return Topic.getDefault();
        }
        for (Topic topic : data.getTopicManager().list()) {
            if (name.equalsIgnoreCase(topic.getName())) {
                return topic;
            }
        }
        return Topic.getDefault();
    }

    /**
     * Attempts to find a time criterion value by its name. 
     * @param name The time name.
     * @return The time value if found, null if not found.
     */
    public Value getTimeByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return null;
        }
        for (Value time : data.getTimeCriterion().values.list()) {
            if (name.equalsIgnoreCase(time.getName())) {
                return time;
            }
        }
        return null;
    }

    /**
     * Attempts to find an energy criterion value by its name. 
     * @param name The energy name.
     * @return The energy value if found, null if not found.
     */
    public Value getEnergyByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return null;
        }
        for (Value energy : data.getEnergyCriterion().values.list()) {
            if (name.equalsIgnoreCase(energy.getName())) {
                return energy;
            }
        }
        return null;
    }

    /**
     * Attempts to find a priority criterion value by its name. 
     * @param name The priority name.
     * @return The priority value if found, null if not found.
     */
    public Value getPriorityByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return null;
        }
        for (Value priority : data.getPriorityCriterion().values.list()) {
            if (name.equalsIgnoreCase(priority.getName())) {
                return priority;
            }
        }
        return null;
    }
    
}


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
package tr.model;

import au.com.trgtd.tr.appl.Constants;
import org.openide.util.NbBundle;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.criteria.Criterion;
import tr.model.criteria.Value;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.project.ProjectFutures;
import tr.model.project.ProjectProjects;
import tr.model.project.ProjectRoot;
import tr.model.project.ProjectSingleActions;
import tr.model.project.ProjectTemplates;
import tr.model.thought.Thought;
import tr.model.topic.Topic; 
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import tr.model.actor.Actor;
import tr.model.goals.data.Goal;
import tr.model.goals.data.GoalRoot;
import tr.model.goals.data.Level;

/**
 * ThinkingRock application data model.
 *
 * @author Jeremy Moore
 */
public class Data extends ObservableImpl implements IDGenerator, Observer {

    /** The current data file version number. */
    public final static int CURRENT_VERSION = 5;
    int version;
    private int maxID = Constants.ID_MAX_SPECIAL;
    private Manager<Topic> topics;
    private Manager<Context> contexts;
    private Manager<Information> information;
    private Manager<Future> futures;
    private Manager<Thought> thoughts;
    /* @deprecated */
    private Manager<Action> actions;
    private Project rootProject;
    private Project rootActions;
    private Project rootTemplates;
    private Project rootFutures;
    private Criterion timeCriterion;
    private Criterion energyCriterion;
    private Criterion priorityCriterion;
    /* @since 2.2.1 */
    private long maxLogID = 0;
    /* @since 3.0.1 */
    private Manager<Actor> actors;
    // Goal levels
    private Vector<Level> levels;
    // Goals (root is the first element)
    private Vector<Goal> goals;
    // Goals - subgoals relationship
    private Map<Integer, Vector<Integer>> goalSubgoals;
    // Goals - projects relationship
    private Map<Integer, Vector<Integer>> goalProjects;
    
    private transient ProjectRoot root;
    private transient boolean changed;

    /**
     * Creates a new instance.
     */
    public Data() {
        super();
        version = CURRENT_VERSION;
        topics = new Manager<>();
        topics.add(Topic.getDefault());
        contexts = new Manager<>();
        contexts.add(Context.getDefault());
        thoughts = new Manager<>();
        futures = new Manager<>();
        information = new Manager<>();
        rootProject = new ProjectProjects();
        rootActions = new ProjectSingleActions();
        rootTemplates = new ProjectTemplates();
        rootFutures = new ProjectFutures();
        actors = new Manager<>();
        resetObservers();
    }

    /**
     * Resets this object as an observer and calls each observed object to reset
     * its observers likewise.
     */
    @Override
    public final void resetObservers() {
        thoughts.addObserver(this);
        thoughts.resetObservers();
        futures.addObserver(this);
        futures.resetObservers();
        information.addObserver(this);
        information.resetObservers();
        topics.addObserver(this);
        topics.resetObservers();
        contexts.addObserver(this);
        contexts.resetObservers();
        getTimeCriterion();
        timeCriterion.addObserver(this);
        timeCriterion.resetObservers();
        getEnergyCriterion();
        energyCriterion.addObserver(this);
        energyCriterion.resetObservers();
        getPriorityCriterion();
        priorityCriterion.addObserver(this);
        priorityCriterion.resetObservers();
        getRootAll().addObserver(this);
        getRootAll().resetObservers();        
        getRootFutures().addObserver(this);
        getRootFutures().resetObservers();        
        getRootTemplates().addObserver(this);
        getRootTemplates().resetObservers();
        getActorManager().addObserver(this);
        getActorManager().resetObservers();
    }

    @Override
    public final synchronized int getNextID() {
        return ++maxID;
    }

    public final synchronized long getNextLogID() {
        return ++maxLogID;
    }

    public final int getVersion() {
        return version;
    }

    public final void checkVersion() {
        if (version == CURRENT_VERSION) {
            return;
        }
        if (version > CURRENT_VERSION) {
            String msg = NbBundle.getMessage(this.getClass(), "msg.data.later.version");
            String ttl = NbBundle.getMessage(this.getClass(), "ttl.data.later.version");
            JOptionPane.showMessageDialog(null, msg, ttl, JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (version == 0) {
            DataUpgrade0to1.process(this);
        }
        if (version == 1) {
            DataUpgrade1to2.process(this);
        }
        if (version == 2) {
            DataUpgrade2to3.process(this);
        }
        if (version == 3) {
            DataUpgrade3to4.process(this);
        }
        if (version == 4) {
            DataUpgrade4to5.process(this);
        }
    }

    public final Manager<Thought> getThoughtManager() {
        return thoughts;
    }

    public final Manager<Topic> getTopicManager() {
        return topics;
    }

    public final Manager<Context> getContextManager() {
        return contexts;
    }

    public final Manager<Information> getInformationManager() {
        return information;
    }

    public final Manager<Future> getFutureManager() {
        return futures;
    }

    public final Manager<Actor> getActorManager() {
        if (actors == null) {
            actors = new Manager<>();
        }
        return actors;
    }

    public final Criterion getTimeCriterion() {
        if (timeCriterion == null) {
            IDGenerator idGenerator = this;
//          timeCriterion = new Criterion(NbBundle.getMessage(Data.class, "Time"));
            timeCriterion = new Criterion("Time");
            timeCriterion.setUse(false);
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "5_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "10_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "15_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "20_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "30_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "45_min"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "1_hr"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "1.5_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "2_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "2.5_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "3_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "4_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "5_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "6_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "7_hrs"), idGenerator));
            timeCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "8_hrs"), idGenerator));
        }
        timeCriterion.key = "time";
        return timeCriterion;
    }

    public final Criterion getEnergyCriterion() {
        if (energyCriterion == null) {
            IDGenerator idGenerator = this;
//          energyCriterion = new Criterion(NbBundle.getMessage(Data.class, "Energy"));
            energyCriterion = new Criterion("Energy");
            energyCriterion.setUse(false);
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "None"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Low_Mental"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Med_Mental"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "High_Mental"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Low_Physical"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Med_Physical"), idGenerator));
            energyCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "High_Physical"), idGenerator));
        }
        energyCriterion.key = "energy";
        return energyCriterion;
    }

    public final Criterion getPriorityCriterion() {
        if (priorityCriterion == null) {
            IDGenerator idGenerator = this;
//          priorityCriterion = new Criterion(NbBundle.getMessage(Data.class, "Priority"));
            priorityCriterion = new Criterion("Priority");
            priorityCriterion.setUse(false);
            priorityCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Must"), idGenerator));
            priorityCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Should"), idGenerator));
            priorityCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Would"), idGenerator));
            priorityCriterion.values.add(new Value(NbBundle.getMessage(Data.class, "Could"), idGenerator));
        }
        priorityCriterion.key = "priority";
        return priorityCriterion;
    }

    /**
     * Gets the root project for templates.
     * @return The root project for templates.
     */
    public final Project getRootTemplates() {
        if (rootTemplates == null) {
            rootTemplates = new ProjectTemplates();
        }
        return rootTemplates;
    }

    /**
     * Gets the root project for future projects.
     * @return The root project for future projects.
     */
    public final Project getRootFutures() {
        if (rootFutures == null) {
            rootFutures = new ProjectFutures();
        }
        return rootFutures;
    }

    /**
     * Gets the root project for projects.
     * @return The root project for projects.
     */
    public final Project getRootProjects() {
        if (rootProject == null) {
            rootProject = new ProjectProjects();
        } else if (!(rootProject instanceof ProjectProjects)) {
            rootProject = new ProjectProjects(rootProject); // data conversion
        }
        return rootProject;
    }

    /**
     * Gets the root project for single actions.
     * @return The root project for single actions.
     */
    public final ProjectSingleActions getRootActions() {
        if (rootActions == null) {
            rootActions = new ProjectSingleActions();
            if (actions != null) {
                // data upgrade - transfer actions from actions manager
                for (Action action : actions.list()) {
                    rootActions.add(action);
                }
                actions = null;	// don't use anymore
                // end data upgrade
            }
        }
        return (ProjectSingleActions) rootActions;
    }

    /**
     * Gets the absolute root project (the parent of projects and actions projects).
     * @return The root.
     */
    public final ProjectRoot getRootAll() {
        if (root == null) {
            root = new ProjectRoot((ProjectProjects) getRootProjects(), getRootActions());
        }
        return root;
    }

    /**
     * Gets the levels.
     * @return The levels.
     */
    public final Vector<Level> getLevels() {
        if (levels == null) {
            levels = new Vector<>();
        }
        return levels;
    }

    /**
     * Gets the goals.
     * @return The goals.
     */
    public final Vector<Goal> getGoals() {
        if (goals == null) {
            goals = new Vector<>();
            goals.add(new GoalRoot());
        }
        return goals;
    }

    /**
     * Gets the goals to subgoals map.
     * @return The goal subgoals.
     */
    public final Map<Integer, Vector<Integer>> getGoalSubgoalsMap() {
        if (goalSubgoals == null) {
            goalSubgoals = new HashMap<>();
            goalSubgoals.put(Constants.ID_ROOT_GOAL, new Vector<>());
        } 
        if (goalSubgoals.get(Constants.ID_ROOT_GOAL) == null) {
            goalSubgoals.put(Constants.ID_ROOT_GOAL, new Vector<>());
        }
        return goalSubgoals;
    }

    /**
     * Gets the goals to projects map.
     * @return The goal projects.
     */
    public final Map<Integer, Vector<Integer>> getGoalProjectsMap() {
        if (goalProjects == null) {
            goalProjects = new HashMap<>();
        }
        return goalProjects;
    }

    /**
     * Sets the data changed state.
     * @param changed The new changed state.
     */
    public final void setChanged(boolean changed) {
        if (this.changed != changed) {
            this.changed = changed;
            this.notifyObservers(this);
        }
    }

    /**
     * Gets the data changed state.
     * @return true if the data changed state is true.
     */
    public final boolean hasChanged() {
        return changed;
    }

    /* Implement Observer */
    /**
     * Sets the data changed state to true and passes on observable changes to observers.
     */
    @Override
    public final void update(Observable observable, Object argument) {
        changed = true;
        notifyObservers(observable, argument);
    }
    /* End Implement Observer */
}

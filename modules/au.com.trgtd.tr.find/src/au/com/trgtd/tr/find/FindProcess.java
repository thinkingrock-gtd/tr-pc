/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.find;

import au.com.trgtd.tr.find.ui.FoundItem;
import au.com.trgtd.tr.find.ui.FoundItems;
import au.com.trgtd.tr.services.Services;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.future.Future;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;

/**
 * Find find.
 *
 * @author Jeremy Moore
 */
public class FindProcess extends Thread {

    private final ActionMatcher actionMatcher;
    private final ProjectMatcher projectMatcher;
    private final ThoughtMatcher thoughtMatcher;
    private final ReferenceMatcher referenceMatcher;
    private final SomedayMatcher somedayMatcher;
    private final GoalMatcher goalMatcher;
    private final FoundItems items;
    private final String mText;
    private final DoneState mState;

    /** Constructor. */
    public FindProcess(FoundItems items, String text, DoneState state) {
        this.items = items;
        this.mText = (text == null ? "" : text.trim().toLowerCase());
        this.mState = state;
        this.actionMatcher = new ActionMatcher(mText, mState);
        this.projectMatcher = new ProjectMatcher(mText, mState);
        this.thoughtMatcher = new ThoughtMatcher(mText);
        this.referenceMatcher = new ReferenceMatcher(mText);
        this.somedayMatcher = new SomedayMatcher(mText);
        this.goalMatcher = new GoalMatcher(mText, mState);
    }

    @Override
    public void run() {
        items.reset(mText);
        if (mText.length() == 0) {
            return;
        }
        switch (mState) {
            case ANY:
            case NOT_DONE:
                findInSingleActions();
                findInProjectActions();
                findInProjects();
                findInGoals();
                findInFutureActions();
                findInFutureProjects();
                findInThoughts();
                findInReferences();
                findInSomedayMaybes();
                break;
            case DONE:
                findInSingleActions();
                findInProjectActions();
                findInProjects();
                findInGoals();
                findInFutureActions();
                findInFutureProjects();
                break;
        }
    }

    private void findInSingleActions() {
        for (Action action : Services.instance.getSingleActions()) {
            if (actionMatcher.matches(action)) {
                items.add(new FoundItem(action, FoundItem.Group.SingleActions));
            }
        }
    }

    private void findInProjectActions() {
        for (Action action : Services.instance.getProjectActions()) {
            if (actionMatcher.matches(action)) {
                items.add(new FoundItem(action, FoundItem.Group.Projects));
            }
        }
    }

    private void findInProjects() {
        for (Project project : Services.instance.getProjects()) {
            if (projectMatcher.matches(project)) {
                items.add(new FoundItem(project, FoundItem.Group.Projects));
            }
        }
    }
    
    private void findInFutureProjects() {
        for (Project project : Services.instance.getFutureProjects()) {
            if (projectMatcher.matches(project)) {
                items.add(new FoundItem(project, FoundItem.Group.Futures));
            }
        }
    }

    private void findInFutureActions() {
        for (Action action : Services.instance.getFutureActions()) {
            if (actionMatcher.matches(action)) {
                items.add(new FoundItem(action, FoundItem.Group.Futures));
            }
        }
    }

    private void findInThoughts() {
        for (Thought thought : Services.instance.getThoughtsUnprocessed()) {
            if (thoughtMatcher.matches(thought)) {
                items.add(new FoundItem(thought));
            }
        }
    }

    private void findInReferences() {
        for (Information reference : Services.instance.getReferences()) {
            if (referenceMatcher.matches(reference)) {
                items.add(new FoundItem(reference));
            }
        }
    }

    private void findInSomedayMaybes() {
        for (Future someday : Services.instance.getSomedayMaybes()) {
            if (somedayMatcher.matches(someday)) {
                items.add(new FoundItem(someday));
            }
        }
    }

    private void findInGoals() {
        GoalCtrl rootGoalCtrl = GoalsCtrlLookup.getGoalsCtrl().getRootGoal();
        for (GoalCtrl subgoalCtrl : rootGoalCtrl.getSubgoals()) {
            findInGoals(subgoalCtrl);
        }
    }

    private void findInGoals(GoalCtrl goalCtrl) {
        if (goalMatcher.matches(goalCtrl)) {
            items.add(new FoundItem(goalCtrl));
        }
        for (GoalCtrl subgoalCtrl : goalCtrl.getSubgoals()) {
            findInGoals(subgoalCtrl);
        }
    }

    private static class ActionMatcher {
        private final String text;
        private final DoneState state;
        public ActionMatcher(String text, DoneState state) {
            this.text = text;
            this.state = state;
        }
        public boolean matches(Action action) {
            switch (state) {
                case ANY:
                    break;
                case DONE:
                    if (!action.isDone()) {
                        return false;
                    }
                    break;
                case NOT_DONE:
                    if (action.isDone()) {
                        return false;
                    }
                    break;
            }
            if (action.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            if (action.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            if (action.isStateDelegated()) {
                ActionStateDelegated s = (ActionStateDelegated)action.getState();
                if (s != null && s.getTo() != null) {
                    if (s.getTo().toLowerCase().contains(text)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class ProjectMatcher {
        private final String text;
        private final DoneState state;
        public ProjectMatcher(String text, DoneState state) {
            this.text = text;
            this.state = state;
        }
        public boolean matches(Project project) {
            switch (state) {
                case ANY:
                    break;
                case DONE:
                    if (!project.isDone()) {
                        return false;
                    }
                    break;
                case NOT_DONE:
                    if (project.isDone()) {
                        return false;
                    }
                    break;
            }
            if (project.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            if (project.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        }
    }

    private static class ThoughtMatcher {
        private final String text;
        public ThoughtMatcher(String text) {
            this.text = text;
        }
        public boolean matches(Thought thought) {
            if (thought.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            if (thought.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        }
    }

    private static class SomedayMatcher {
        private final String text;
        public SomedayMatcher(String text) {
            this.text = text;
        }        
        public boolean matches(Future someday) {
            if (someday.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            if (someday.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        }
    }

    private static class ReferenceMatcher {
        private final String text;
        public ReferenceMatcher(String text) {
            this.text = text;
        }
        public boolean matches(Information reference) {
            if (reference.getDescription().toLowerCase().contains(text)) {
                return true;
            }
            if (reference.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        }
    }

    private static class GoalMatcher {
        private final String text;
        private final DoneState state;
        public GoalMatcher(String text, DoneState state) {
            this.text = text;
            this.state = state;
        }
        public boolean matches(GoalCtrl goal) {
            switch (state) {
                case ANY:
                    break;
                case DONE:
                    if (!goal.isAchieved()) {
                        return false;
                    }
                    break;
                case NOT_DONE:
                    if (goal.isAchieved()) {
                        return false;
                    }
                    break;
            }
            if (goal.getDescr().toLowerCase().contains(text)) {
                return true;
            }
            if (goal.getNotes().toLowerCase().contains(text)) {
                return true;
            }
            return false;
        }
    }
}


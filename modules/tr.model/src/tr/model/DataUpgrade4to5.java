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

import au.com.trgtd.tr.util.UtilsEmail;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.actor.Actor;
import tr.model.project.Project;

/**
 * Upgrade the data model from version 4 to version 5.
 * Initialises new recurrence period values.
 * 
 * @since 3.0.1
 * @author Jeremy Moore
 */
public class DataUpgrade4to5 {

    private static final Logger LOG = Logger.getLogger("tr.data.upgrade");
    private static final int OLD_VERSION = 4;
    private static final int NEW_VERSION = 5;
    private static final Map<String, Actor> actors = new HashMap<>();
    
    /**
     * Upgrade the data.
     * @param data The data.
     */
    public static void process(Data data) {
        if (data.version == OLD_VERSION) {
            upgrade(data);
            data.version = NEW_VERSION;
        }
    }

    // Upgrade the data.
    private static void upgrade(Data data) {
        LOG.log(Level.INFO, "Starting data upgrade version {0} to version {1} ... ", new Object[]{OLD_VERSION, NEW_VERSION});

        upgradeDelegate(data);

        LOG.log(Level.INFO, "Finished data upgrade.");
    }

    private static void upgradeDelegate(Data data) {
        process(data, data.getRootActions());
        process(data, data.getRootProjects());
        process(data, data.getRootFutures());
        process(data, data.getRootTemplates());
    }

    private static void process(Data data, Project project) {
        for (Action action : project.getChildren(Action.class)) {
            process(data, action);
        }
        for (Project subproject : project.getChildren(Project.class)) {
            process(data, subproject);
        }
    }

    private static void process(Data data, Action action) {
        // Create a delegate for all distinct "to" field values
        if (action.isStateDelegated()) {
            ActionStateDelegated state = (ActionStateDelegated)action.getState();
            String to = state.getTo();
            if (to == null) {
                return;
            }
            to = to.trim();
            if (to.length() == 0) {
                return;
            }
            Actor actor = actors.get(to);
            if (actor == null) {
                actor = new Actor(data.getNextID());
                actor.setName(to);
                if (UtilsEmail.isEmailAddress(to)) {
                    actor.setEmail(to.toLowerCase());
                }
                data.getActorManager().add(actor);
                actors.put(to, actor);
            }
            state.setActorID(actor.getID());
            LOG.log(Level.INFO, "upgraded delegate: {0}", to);
        }

//        // Only create delegate if "to" field is a valid email address
//        if (action.isStateDelegated()) {
//            ActionStateDelegated state = (ActionStateDelegated)action.getState();
//            String to = state.getTo();
//            if (to == null) {
//                return;
//            }
//            to = to.trim();
//            if (to.length() == 0) {
//                return;
//            }
//            if (UtilsEmail.isEmailAddress(to)) {
//                Actor actor = actors.get(to);
//                if (actor == null) {
//                    actor = new Actor(data.getNextID());
//                    actor.setName(to);
//                    actor.setEmail(to);
//                    data.getActorManager().add(actor);
//                    actors.put(to, actor);
//                }
//                state.setActorID(actor.getID());
//            }
//            LOG.log(Level.INFO, "upgraded delegate: {0}", to);
//        }

    }

}

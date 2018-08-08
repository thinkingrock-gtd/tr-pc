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
package tr.model;

import au.com.trgtd.tr.util.UtilsEmail;
import java.util.HashMap;
import java.util.Map;
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
    private static final Map<String, Actor> actors = new HashMap<String, Actor>();
    
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
        LOG.info("Starting data upgrade version " + OLD_VERSION + " to version " + NEW_VERSION + " ... ");

        upgradeDelegate(data);

        LOG.info("Finished data upgrade.");
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
            LOG.info("upgraded delegate: " + to);
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
//            LOG.info("upgraded delegate: " + to);
//        }

    }

}

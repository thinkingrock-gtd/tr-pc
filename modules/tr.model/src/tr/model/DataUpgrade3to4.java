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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package tr.model;

import tr.model.context.Context;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * Upgrade the data model from version 3 to version 4.
 * Initialises new recurrence period values.
 * @author Jeremy Moore
 */
public class DataUpgrade3to4 {

    private static int OLD_VERSION = 3;
    private static int NEW_VERSION = 4;

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
        System.out.print("Upgrading data from version " + OLD_VERSION + " to version " + NEW_VERSION + " ... ");

        upgradeThoughts(data);
        upgradeContexts(data);
        upgradeTopics(data);
        upgradeFuture(data);
        upgradeInformation(data);

        System.out.println("Done");
    }

    private static void upgradeThoughts(Data data) {
        for (Thought thought : data.getThoughtManager().list()) {
            thought.initID(data.getNextID());
        }
    }

    private static void upgradeContexts(Data data) {
        for (Context context : data.getContextManager().list()) {
            context.initID(data.getNextID());
        }
    }

    private static void upgradeTopics(Data data) {
        for (Topic topic : data.getTopicManager().list()) {
            topic.initID(data.getNextID());
        }
    }

    private static void upgradeFuture(Data data) {
        for (Future future : data.getFutureManager().list()) {
            future.initID(data.getNextID());
        }
    }

    private static void upgradeInformation(Data data) {
        for (Information info : data.getInformationManager().list()) {
            info.initID(data.getNextID());
        }
    }

}

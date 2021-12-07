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

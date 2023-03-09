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
package tr.model.topic;

import au.com.trgtd.tr.util.Observable;
import java.util.HashMap;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.util.Manager;

/**
 * Map for obtaining topic by identifier.
 *
 * @author Jeremy Moore
 */
public class TopicMap {

    // SINGLETON
    private static TopicMap instance;

    /**
     * Gets the default instance.
     * @return the default instance.
     */
    public static TopicMap getDefault() {
        if (instance == null) {
            instance = new TopicMap();
        }
        return instance;
    }
    // End of SINGLETON


    private Data data;
    private HashMap<Integer, Topic> map;

    private TopicMap() {
        Lookup.Result dlr = DataLookup.instance().lookupResult(Data.class);
        dlr.addLookupListener((LookupEvent lookupEvent) -> {
            initialise();
        });
        initialise();
    }

    private void initialise() {
        synchronized (this) {
            if (map == null) {
                map = new HashMap<>();
            } else {
                map.clear();
            }
            data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return;
            }
            for (Topic topic : data.getTopicManager().list()) {
                map.put(topic.getID(), topic);
            }
            data.getTopicManager().addObserver((Observable obs, Object arg) -> {
                // if manager changed (add, remove, etc), not topic change
                if (arg instanceof Manager.Event) {
                    initialise();
                }
            });
        }
    }

    /**
     * Gets the topic for a given topic ID.
     * @param topicID The topic ID.
     * @return The topic.
     */
    public final Topic getTopic(Integer topicID) {
        return map.get(topicID);
    }

}

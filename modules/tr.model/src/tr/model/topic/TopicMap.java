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
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */
package tr.model.topic;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.util.HashMap;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
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
        Lookup.Result dlr = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        dlr.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                initialise();
            }
        });
        initialise();
    }

    private void initialise() {
        synchronized (this) {
            if (map == null) {
                map = new HashMap<Integer, Topic>();
            } else {
                map.clear();
            }
            data = (Data) DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return;
            }
            for (Topic topic : data.getTopicManager().list()) {
                map.put(topic.getID(), topic);
            }
            data.getTopicManager().addObserver(new Observer() {
                @Override
                public void update(Observable obs, Object arg) {
                    // if manager changed (add, remove, etc), not topic change
                    if (arg instanceof Manager.Event) {
                        initialise();
                    }
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

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

package tr.model.actor;

import java.util.Comparator;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Actor utilities singleton.
 *
 * @author Jeremy Moore
 */
public class ActorUtils {
    
    private static ActorUtils instance;        
    
    /* Singleton constructor */
    private ActorUtils() {
    }
    
    /** 
     * Gets the singleton instance. 
     * @return The instance.
     */
    public static synchronized ActorUtils instance() {
        if (instance == null) {
            instance = new ActorUtils();
        }
        return instance;
    }


    /**
     * Gets the actor for the given identifier.
     * @param id The actor identifier.
     */
    public Actor getActor(int id) {
        synchronized(this) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return null;
            }
            for (Actor actor : data.getActorManager().list()) {
                if (actor.getID() == id) {
                    return actor;
                }
            }
            return null;
        }
    }

    public static final Comparator<Actor> COMPARATOR_AZ = new Comparator<Actor>() {
        @Override
        public int compare(Actor a1, Actor a2) {
            return a1.getName().compareToIgnoreCase(a2.getName());
        }
    };

}

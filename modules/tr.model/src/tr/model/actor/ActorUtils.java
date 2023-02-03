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

    public static final Comparator<Actor> COMPARATOR_AZ = (Actor a1, Actor a2) ->
            a1.getName().compareToIgnoreCase(a2.getName());

}

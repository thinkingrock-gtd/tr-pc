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

import java.util.Iterator;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;

/**
 * Topic utilities singleton.
 *
 * @author Jeremy Moore
 */
public class TopicUtils {
    
    private static final Logger LOG = Logger.getLogger("tr.model.topic");
    
    private static TopicUtils instance;    
    
    
    /* Singleton constructor */
    private TopicUtils() {
    }
    
    /** 
     * Gets the singleton instance. 
     * @return The instance.
     */
    public static synchronized TopicUtils instance() {
        if (instance == null) {
            instance = new TopicUtils();
        }
        return instance;
    }
    
    /**
     * Determines whether a topic is in use.
     * @param data The data model.
     * @param topic The topic.
     */
    public boolean isUsed(Data data, Topic topic) {
        synchronized(this) {
            if (data == null || topic == null) {
                return true; // play safe
            }
            return isUsedTopic(data, topic);
        }
    }

    private boolean isUsedTopic(Data data, Topic topic) {
        // check not default topic
        if (topic.equals(Topic.getDefault())) {
            return true;
        }        
        // check single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext(); ) {
            if (i.next().getTopic().equals(topic)) {
                return true;
            }
        }        
        // check projects and project decendants
        if (isUsedTopic(data.getRootProjects(), topic)) {
            return true;
        }        
        // check future projects and future project decendants
        if (isUsedTopic(data.getRootFutures(), topic)) {
            return true;
        }                
        // check template projects and template project decendants
        if (isUsedTopic(data.getRootTemplates(), topic)) {
            return true;
        }        
        // check thoughts
        for (Thought thought : data.getThoughtManager().list()) {
            if (thought.getTopic().equals(topic)) {
                return true;
            }
        }        
        // check future items
        for (Future future : data.getFutureManager().list()) {
            if (future.getTopic().equals(topic)) {
                return true;
            }
        }        
        // check information items
        for (Information info : data.getInformationManager().list()) {
            if (info.getTopic().equals(topic)) {
                return true;
            }
        }        
        return false;
    }
    
    /* Recursively check whether topic is in use in a project. */
    private boolean isUsedTopic(Project project, Topic topic) {
        // check project
        if (project.getTopic().equals(topic)) {
            return true;
        }
        // check project children
        for (Item child : project.getChildren()) {            
            if (child instanceof Action action) {
                if (action.getTopic().equals(topic)) {
                    return true;
                }
            } else if (child instanceof Project prj) {
                // recursive call for child project
                if (isUsedTopic(prj, topic)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * Removes a specific topic replacing it with a different topic.
     * @param topic The topic to remove.
     * @param replacement The topic to replace existing references to.
     */
    public boolean replace(Data data, Topic topic, Topic replacement) {
        synchronized(this) {
            if (data == null || topic == null || replacement == null) {
                return false;
            }
            if (topic.equals(Topic.getDefault())) {
                return false;
            }                    
            if (topic.equals(replacement)) {
                return false;                
            }            
            
            replaceTopic(data, topic, replacement);                        
            
            return true;
        }
    }

    private void replaceTopic(Data data, Topic topic, Topic replacement) {
        // replace in projects and project decendants
        replaceTopic(data.getRootProjects(), topic, replacement);

        // replace in single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext(); ) {
            Action action = i.next();
            if (action.getTopic().equals(topic)) {
                action.setTopic(replacement);
            }
        }        

        // replace in future projects and future project decendants
        replaceTopic(data.getRootFutures(), topic, replacement);

        // replace in template projects and template project decendants
        replaceTopic(data.getRootTemplates(), topic, replacement);
        
        // replace in thoughts
        for (Thought thought : data.getThoughtManager().list()) {
            if (thought.getTopic().equals(topic)) {
                thought.setTopic(replacement);
            }
        }        
        // replace in future items
        for (Future future : data.getFutureManager().list()) {
            if (future.getTopic().equals(topic)) {
                future.setTopic(replacement);
            }
        }        
        // replace in information items
        for (Information info : data.getInformationManager().list()) {
            if (info.getTopic().equals(topic)) {
                info.setTopic(replacement);
            }
        }
    }
    
    /* Recursively replace topic in a project and decendants. */
    private void replaceTopic(Project project, Topic topic, Topic replacement) {
        if (project.getTopic().equals(topic)) {
            project.setTopic(replacement);
        }
        for (Item child : project.getChildren()) {            
            if (child instanceof Action action) {
                if (action.getTopic().equals(topic)) {
                    action.setTopic(replacement);
                }
            } else if (child instanceof Project prj) {
                replaceTopic(prj, topic, replacement);
            }
        }
    }
    
}

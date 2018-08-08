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
            if (child instanceof Action) {
                if (((Action)child).getTopic().equals(topic)) {
                    return true;
                }
            } else if (child instanceof Project) {
                // recursive call for child project
                if (isUsedTopic((Project)child, topic)) {
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
            if (child instanceof Action) {
                Action action = (Action)child;
                if (action.getTopic().equals(topic)) {
                    action.setTopic(replacement);
                }
            } else if (child instanceof Project) {
                replaceTopic((Project)child, topic, replacement);
            }
        }
    }
    
}

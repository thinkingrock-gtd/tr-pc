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

package tr.model.context;

import java.util.Iterator;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Context utilities singleton.
 *
 * @author Jeremy Moore
 */
public class ContextUtils {
    
    private static final Logger LOG = Logger.getLogger("tr.model.context");
    
    private static ContextUtils instance;    
    
    
    /* Singleton constructor */
    private ContextUtils() {
    }
    
    /** 
     * Gets the singleton instance. 
     * @return The instance.
     */
    public static synchronized ContextUtils instance() {
        if (instance == null) {
            instance = new ContextUtils();
        }
        return instance;
    }
    
    /**
     * Determines whether a context is in use.
     * @param data The data model.
     * @param context The context.
     */
    public boolean isUsed(Data data, Context context) {
        synchronized(this) {
            if (data == null || context == null) {
                return true; // play safe
            }
            return isUsedContext(data, context);
        }
    }

    private boolean isUsedContext(Data data, Context context) {
        // check not default context
        if (context.equals(Context.getDefault())) {
            return true;
        }        
        // check single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext(); ) {
            if (i.next().getContext().equals(context)) {
                return true;
            }
        }        
        // check projects and project decendants
        if (isUsedContext(data.getRootProjects(), context)) {
            return true;
        }        
        // check future projects and future project decendants
        if (isUsedContext(data.getRootFutures(), context)) {
            return true;
        }                
        // check template projects and template project decendants
        if (isUsedContext(data.getRootTemplates(), context)) {
            return true;
        }        
        return false;
    }
    
    /* Recursively check whether a context is in use in a project. */
    private boolean isUsedContext(Project project, Context context) {
        // check project children
        for (Item child : project.getChildren()) {            
            if (child instanceof Action) {
                if (((Action)child).getContext().equals(context)) {
                    return true;
                }
            } else if (child instanceof Project) {
                // recursive call for child project
                if (isUsedContext((Project)child, context)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * Removes a specific context replacing it with a different one.
     * @param conext The context to remove.
     * @param replacement The context to replace existing references to.
     */
    public boolean replace(Data data, Context context, Context replacement) {
        synchronized(this) {
            if (data == null || context == null || replacement == null) {
                return false;
            }
            if (context.equals(Context.getDefault())) {
                return false;
            }                    
            if (context.equals(replacement)) {
                return false;                
            }            
            
            replaceContext(data, context, replacement);                        
            
            return true;
        }
    }

    private void replaceContext(Data data, Context context, Context replacement) {
        // replace in projects and project decendants
        replaceContext(data.getRootProjects(), context, replacement);

        // replace in single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext(); ) {
            Action action = i.next();
            if (action.getContext().equals(context)) {
                action.setContext(replacement);
            }
        }        

        // replace in future projects and future project decendants
        replaceContext(data.getRootFutures(), context, replacement);

        // replace in template projects and template project decendants
        replaceContext(data.getRootTemplates(), context, replacement);        
    }
    
    /* Recursively replace context in a project and decendants. */
    private void replaceContext(Project project, Context context, Context replacement) {
        for (Item child : project.getChildren()) {            
            if (child instanceof Action) {
                Action action = (Action)child;
                if (action.getContext().equals(context)) {
                    action.setContext(replacement);
                }
            } else if (child instanceof Project) {
                replaceContext((Project)child, context, replacement);
            }
        }
    }
    
}

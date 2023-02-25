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
            if (child instanceof Action action) {
                if (action.getContext().equals(context)) {
                    return true;
                }
            } else if (child instanceof Project prj) {
                // recursive call for child project
                if (isUsedContext(prj, context)) {
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
            if (child instanceof Action action) {
                if (action.getContext().equals(context)) {
                    action.setContext(replacement);
                }
            } else if (child instanceof Project prj) {
                replaceContext(prj, context, replacement);
            }
        }
    }
    
}

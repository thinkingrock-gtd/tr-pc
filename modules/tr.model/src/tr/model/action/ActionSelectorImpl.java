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
package tr.model.action;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import tr.model.context.Context;
import tr.model.topic.Topic;
import au.com.trgtd.tr.util.DateUtils;

/**
 * A definable action selector implementation. Selects the action if the
 * following conditions are all true:
 * - the action has a state that is in the action states list or the list is empty.
 * - the action has a context that is in the contexts list or the list is empty.
 * - the action has a topic that is in the topics list or the list is empty.
 * - the action has a done value that is in the done values list or the list is empty.
 * - the action has an active value that is in the active values list or the list is empty.
 *
 * @author Jeremy Moore
 */
public class ActionSelectorImpl implements ActionSelector {
    
    // Action state constants
    public static final Class ACTION_STATE_ASAP = ActionStateASAP.class;
    public static final Class ACTION_STATE_SCHEDULED = ActionStateScheduled.class;
    public static final Class ACTION_STATE_DELEGATED = ActionStateDelegated.class;
    public static final Class ACTION_STATE_INACTIVE = ActionStateInactive.class;
    public static final Class[] ACTION_STATES = {
        ACTION_STATE_ASAP,
        ACTION_STATE_SCHEDULED,
        ACTION_STATE_DELEGATED,
        ACTION_STATE_INACTIVE
    };
    
    // definition variables
    private String name;
    
    private final Vector<Class> actionStates;
    private final Vector<Context> contexts;
    private final Vector<Topic> topics;
    private final Vector<Boolean> doneValues;
    private final Vector<Boolean> activeValues;
    
    private boolean checkFutureDays = false;
    
    private int futureDays;
    
    private Date createdFrom = null;
    private Date createdTo = null;
    
    private Date doneFrom = null;
    private Date doneTo = null;
    
    /**
     * Creates a new instance.
     */
    public ActionSelectorImpl() {
        this("");
    }
    
    /**
     * Creates a new instance.
     */
    public ActionSelectorImpl(String name) {
        this.name = name;
        actionStates = new Vector<>();
        contexts = new Vector<>();
        topics = new Vector<>();
        doneValues = new Vector<>();
        activeValues = new Vector<>();
    }
    
    /**
     * Gets the name of the action selector.
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the action selector.
     * @param name The name.
     */
    public void setName(String name) {
        if (name == null) return;
        
        this.name = name;
    }
    
    /**
     * Clears all the values.
     */
    public void clearAll() {
        actionStates.clear();
        contexts.clear();
        topics.clear();
        doneValues.clear();
        activeValues.clear();
        createdFrom = null;
        createdTo = null;
        doneFrom = null;
        doneTo = null;
    }
    
    /**
     * Adds an action state class to the list of action state classes.
     * @param actionState The class to add.
     */
    public void addActionState(Class actionState) {
        actionStates.add(actionState);
    }
    
    /**
     * Adds an context to the list of action contexts.
     * @param context The context to add
     */
    public void addActionContext(Context context) {
        contexts.add(context);
    }
    
    /**
     * Adds a topic to the list of action topics.
     * @param topic The topic to add.
     */
    public void addActionTopic(Topic topic) {
        topics.add(topic);
    }
    
    /**
     * Adds a done value to the list of action done values.
     * @param done The done value to add.
     */
    public void addDoneValue(boolean done) {
        doneValues.add(new Boolean(done));
    }
    
    /**
     * Removes all done values from the list.
     */
    public void clearDoneValues() {
        doneValues.removeAllElements();
    }
    
    
    /**
     * Adds an active value to the list of action active values.
     * @param active The active value to add.
     */
    public void addActiveValue(boolean active) {
        this.activeValues.add(new Boolean(active));
    }
    
    /**
     * Set whether to check future days for scheduled and delegated action dates.
     * @param b The boolean value.
     */
    public void setCheckFutureDays(boolean b) {
        this.checkFutureDays = b;
    }
    
    /**
     * Set the number of future days for scheduled and delegated action dates check.
     * @param days The number of days.
     */
    public void setFutureDays(int days) {
        this.futureDays = days;
    }
    
    /**
     * Set the created from date.
     * @param date the date.
     */
    public void setCreatedFrom(Date date) {
        this.createdFrom = date;
    }
    
    /**
     * Set the created to date.
     * @param date the date.
     */
    public void setCreatedTo(Date date) {
        this.createdTo = date;
    }
    
    /**
     * Set the done from date.
     * @param date the date.
     */
    public void setDoneFrom(Date date) {
        this.doneFrom = date;
    }
    
    /**
     * Set the done to date.
     * @param date the date.
     */
    public void setDoneTo(Date date) {
        this.doneTo = date;
    }
    
    /**
     * Determines whether the given project and action should be selected or not.
     * @param action The action
     * @return true iff the action should be selected.
     */
    public boolean isSelected(Action action) {
        
        // see if the action done value is in the list
        if (!doneValues.isEmpty()) {
            boolean found = false;
            for (Iterator i = doneValues.iterator(); i.hasNext(); ) {
                Boolean b = (Boolean)i.next();
                if (action.isDone() == b.booleanValue()) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        
        // check created date
        Date created = action.getCreated();
        if (createdFrom != null) {
            if ( ! created.after(createdFrom) ) return false;
        }
        if (createdTo != null) {
            if ( ! created.before(createdTo) ) return false;
        }
        
        // check done date
        Date done = action.getDoneDate();
        if (doneFrom != null) {
            if (done == null || ! done.after(doneFrom)) return false;
        }
        if (doneTo != null) {
            if (done == null || ! done.before(doneTo)) return false;
        }
        
        // see if the action active value is in the list
        if (!activeValues.isEmpty()) {
            boolean found = false;
            for (Iterator i = activeValues.iterator(); i.hasNext(); ) {
                Boolean b = (Boolean)i.next();
                if (action.isActive() == b.booleanValue()) {
                    found = true;
                    break;
                }
            }
            if ( ! found ) return false;
        }
        
        // see if the action state class is in list
        if (actionStates.isEmpty()) {
            return false;
        } else {
            boolean found = false;
            for (Iterator i = actionStates.iterator(); i.hasNext(); ) {
                Class c = (Class)i.next();
                if (c.isInstance(action.getState())) {
                    found = true;
                    break;
                }
            }
            if ( ! found ) return false;
        }
        
        // see if the action context is in list
        if (!contexts.isEmpty()) {
            boolean found = false;
            for (Iterator i = contexts.iterator(); i.hasNext(); ) {
                Context context = (Context)i.next();
                if (action.getContext().equals(context)) {
                    found = true;
                    break;
                }
            }
            if ( ! found ) return false;
        }
        
        // see if the action topic is in list
        if (!topics.isEmpty()) {
            boolean found = false;
            for (Iterator i = topics.iterator(); i.hasNext(); ) {
                Topic topic = (Topic)i.next();
                if (action.getTopic().equals(topic)) {
                    found = true;
                    break;
                }
            }
            if ( ! found ) return false;
        }
        
        if (checkFutureDays) {
            Calendar future = Calendar.getInstance();
            future.add(Calendar.DAY_OF_YEAR, futureDays + 1);
            if (action.getState() instanceof ActionStateDelegated asd) {
                Date date = asd.getDate();
                if (date == null) return true;
                if ( ! DateUtils.isBeforeDay(date, future.getTime())) {
                    return false;
                }
            } else if (action.getState() instanceof ActionStateScheduled ass) {
                Date date = ass.getDate();
                if (date == null) return true;
                if ( ! DateUtils.isBeforeDay(date, future.getTime())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
}

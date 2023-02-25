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
package au.com.trgtd.tr.view.cal.dialog;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.DateUtils;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.*;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * Action controller.
 *
 * @author Jeremy Moore
 */
public class ActionCtlr { 

    private final Action action;
    private String descr;
    private String success;
    private String notes;
    private Topic topic;
    private Context context;
    private Value time;
    private Value energy;
    private Value priority;
    private Date createdDate;
    private Date startDate;         
    private Date dueDate;           
    private Date doneDate;
    private ActionState.Type stateType;
    private String delegateName;    
    private int delegateID;     
    private Date delegateDate;	    
    private Date schdDate;   
    private int schdHr;            
    private int schdMn;           
    private int schdDurHrs;            
    private int schdDurMns;           

    /**
     * Constructs a new instance.
     * @param action The action.
     */
    public ActionCtlr(Action action) {
        this.action = action;
        
        this.descr = action.getDescription();
        this.success = action.getSuccess();
        this.notes = action.getNotes();
        this.topic = action.getTopic();
        this.context = action.getContext();
        this.time = action.getTime();
        this.energy = action.getEnergy();
        this.priority = action.getPriority();
        this.createdDate = action.getCreated();
        this.startDate = action.getStartDate();
        this.dueDate = action.getDueDate();
        this.doneDate = action.getDoneDate();
        
        this.stateType = action.getState().getType();
        switch (stateType) {
            case DELEGATED:
                ActionStateDelegated ds = (ActionStateDelegated) action.getState();
                this.delegateDate = ds.getDate();
                this.delegateName = ds.hasDelegateValue() ? null : ds.getTo();
                this.delegateID = ds.hasDelegateValue() ? ds.getActorID() : 0;
                break;
            case SCHEDULED:
                ActionStateScheduled ss = (ActionStateScheduled) action.getState();
                this.schdDate = ss.getDate();
                this.schdHr = getHour(schdDate);
                this.schdMn = getMinute(schdDate);
                this.schdDurHrs = ss.getDurationHours();
                this.schdDurMns = ss.getDurationMinutes();
                break;
        }
    }

    // Get the hour field value from a date.
    private int getHour(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    // Get the minute field value from a date.
    private int getMinute(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }
    
    public int getID() {
        return action.getID();
    }

    public ImageIcon getIcon() {
        switch (stateType) {
            case DELEGATED:
                return Icons.ActionDelegated;
            case DOASAP:
                return Icons.ActionDoASAP;
            case INACTIVE:
                return Icons.ActionInactive;
            case SCHEDULED:
                return Icons.ActionScheduled;
            default:
                return null;
        }
    }

    public String getProjectPath() {
        return Services.instance.getPath(action);
    }

    public String getDescr() {
        return descr == null ? "" : descr;
    }

    public String getSuccess() {
        return success == null ? "" : success;
    }

    public void addToNotes(String text) {
        String oldNotes = getNotes();
        if (oldNotes.contains(text)) {
            return;
        }
        if (oldNotes.length() == 0) {
            setNotes(text);
        } else {
            setNotes(oldNotes + "\n" + text);
        }
    }
    
    public String getNotes() {
        return notes == null ? "" : notes;
    }

    public String getThought() {
        Thought thought = action.getThought();
        return thought == null ? " " : thought.getDescription();
    }

    public boolean isSingle() {
        return action.isSingleAction();
    }

    // STATE
    public ActionState.Type getStateType() {
        return stateType != null ? stateType : action.getState().getType();
    }

    public boolean isInactive() {
        return getStateType() == ActionState.Type.INACTIVE;
    }

    public boolean isDelegated() {
        return getStateType() == ActionState.Type.DELEGATED;
    }

    public boolean isScheduled() {
        return getStateType() == ActionState.Type.SCHEDULED;
    }

    public boolean isDoASAP() {
        return getStateType() == ActionState.Type.DOASAP;
    }

    public String getDelegateName() {
        return isDelegated() ? delegateName : null;
    }

    public String getDelegateEmail() {
        if (!isDelegated()) {
            return "";
        }
        int actorID = getDelegateID();
        if (actorID > 0) {
            Actor actor = ActorUtils.instance().getActor(actorID);
            if (actor != null) {
                String email = actor.getEmail();
                if (email != null && email.trim().length() > 0) {
                    return email;
                }
            }
        }
        String name = getDelegateName();
        return name == null ? "" : name;
    }

    public Date getDelegateDate() {
        return isDelegated() ? delegateDate : null;
    }

    public int getDelegateID() {
        return isDelegated() ? delegateID : 0;
    }

    public Date getSchdDate() {
        return isScheduled() ? schdDate : null;
    }

    public int getSchdHr() {
        return isScheduled() ? schdHr : 0;
    }

    public int getSchdMin() {
        return isScheduled() ? schdMn : 0;
    }
    
    public int getSchdDurHrs() {
        return isScheduled() ? schdDurHrs : 0;
    }

    public int getSchdDurMins() {
        return isScheduled() ? schdDurMns : 0;
    }
    // END STATE

    // DATES
    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public boolean isDone() {
        return doneDate != null;
    }
    // END DATES

    public Topic getTopic() {
        return topic;
    }

    // CRITERIA
    public Context getContext() {
        return context;
    }

    public Value getTime() {
        return time;
    }

    public Value getEnergy() {
        return energy;
    }

    public Value getPriority() {
        return priority;
    }
    // END CRITERIA

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTime(Value time) {
        this.time = time;
    }

    public void setEnergy(Value energy) {
        this.energy = energy;
    }

    public void setPriority(Value priority) {
        this.priority = priority;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public void setStateType(ActionState.Type type) {
        this.stateType = type;
    }

    public void setDelegateDate(Date date) {
        this.delegateDate = date;
    }

    public void setDelegateID(Integer id) {
        this.delegateID = id == null ? 0 : id;
    }

    public void setDelegateName(String name) {
        this.delegateName = name;
    }

    public void setSchdDate(Date date) {
        if (null == date) {
            this.schdDate = null;
        } else {
            this.schdDate = DateUtils.clearTime(date);
        }
    }

    public void setSchdHour(Object hour) {
        if (hour instanceof Integer h) {
            this.schdHr = h;
        }
    }

    public void setSchdMin(Object min) {
        if (min instanceof Integer m) {
            this.schdMn = m;
        }
    }

    public void setCreatedDate(Date date) {
        if (date == null) {
            return;
        }
        this.createdDate = date;
    }

    public void setSchdDurHours(int schdHrs) {
        this.schdDurHrs = schdHrs;
    }

    public void setSchdDurMins(int schdMins) {
        this.schdDurMns = schdMins;
    }

    /**
     * Commits all changes.
     */
    public void commit() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        action.setDescription(this.getDescr());
        action.setSuccess(this.getSuccess());
        action.setNotes(this.getNotes());
        action.setTopic(this.getTopic());
        action.setContext(this.getContext());
        action.setTime(this.getTime());
        action.setEnergy(this.getEnergy());
        action.setPriority(this.getPriority());
        action.setCreated(this.getCreatedDate());
        action.setStartDate(this.getStartDate());
        action.setDueDate(this.getDueDate());
        action.setDoneDate(this.getDoneDate());
        
        switch (stateType) {
            case INACTIVE:
                if (hasStateChanged()) {
                    action.setState(new ActionStateInactive());                     
                }
                break;
            case DOASAP:
                if (hasStateChanged()) {
                    action.setState(new ActionStateASAP());                     
                }
                break;
            case DELEGATED:
                ActionStateDelegated asd = hasStateChanged() 
                        ? new ActionStateDelegated()
                        : (ActionStateDelegated)action.getState();
                asd.setDate(this.getDelegateDate());
                asd.setTo(this.getDelegateName());
                asd.setActorID(this.getDelegateID());
                action.setState(asd);
                break;
            case SCHEDULED:
                ActionStateScheduled ass = hasStateChanged() 
                        ? new ActionStateScheduled()
                        : (ActionStateScheduled)action.getState();
                
                if (schdDate != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(schdDate);
                    c.set(Calendar.HOUR_OF_DAY, schdHr);
                    c.set(Calendar.MINUTE, schdMn);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    ass.setDate(c.getTime());
                }
                ass.setDurationHours(this.getSchdDurHrs());
                ass.setDurationMins(this.getSchdDurMins());
                action.setState(ass);
                break;
        }
        
        // force views to update for changes
        DateCtlr.DEFAULT.fireChange();
    }
    
    private boolean hasStateChanged() {
        return action.getState().getType() != this.stateType;        
    }
}

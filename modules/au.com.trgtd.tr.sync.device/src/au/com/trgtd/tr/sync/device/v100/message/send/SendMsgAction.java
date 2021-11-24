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
package au.com.trgtd.tr.sync.device.v100.message.send;

import au.com.trgtd.tr.services.Services;
import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import static au.com.trgtd.tr.sync.device.Constants.DF_DATE;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Action.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.criteria.Value;

/**
 * Action send message.
 */
public class SendMsgAction extends SendMsg {

    private final Action action;
    private final boolean timeUsed;
    private final boolean energyUsed;
    private final boolean priorityUsed;
//  private Boolean done;
    private Date doneDate;
    private String notes;
    private Date date;
    private Integer startHr;
    private Integer startMn;
    private Integer lengthHrs;
    private Integer lengthMns;
    
    // ADDED to enable listing of actions within project on Android app.
    // Order number of action within project (starting at 0).
    private final int ordinal;
    private String getOrdinal() {
        return String.valueOf(ordinal);
    }    
    private String getProjectIDString() {
        Integer id = getProjectID();
        return id == null ? "" : String.valueOf(id);            
    }
    private Integer getProjectID() {
        if (action.isSingleAction()) {
            return null;
        } else {
            return action.getParent().getID();            
        }
    }
    // END ADDED
    
    public SendMsgAction(Action action,
                       boolean timeUsed,
                       boolean energyUsed,
                       boolean priorityUsed,
                       Integer ordinal)
    {
        super(SendMsg.Type.ACTION);
        this.action = action;
        this.timeUsed = timeUsed;
        this.energyUsed = energyUsed;
        this.priorityUsed = priorityUsed;
        this.ordinal = ordinal;
    }

    /**
     * Get updated or actual action done value.
     * @return the done value.
     */
    public Boolean isDone() {
//      return done == null ? action.isDone() : done;
        return doneDate == null ? action.isDone() : true;
    }

//    /**
//     * Action update.
//     * @param done
//     */
//    public void setDone(Boolean done) {
//        this.done = done;
//    }
    /**
     * Action update.
     * @param doneDate
     */
    public void setDoneDate(Date date) {
        this.doneDate = date;
    }

    /**
     * Action update.
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Action update scheduled start hour.
     * @param hour
     */
    public void setStartHr(Integer hour) {
        this.startHr = hour;
    }

    /**
     * Action update scheduled start minute.
     * @param min
     */
    public void setStartMn(Integer min) {
        this.startMn = min;
    }

    /**
     * Action update scheduled length hours.
     * @param hours
     */
    public void setLengthHrs(Integer hours) {
        this.lengthHrs = hours;
    }

    /**
     * Action update scheduled length minutes.
     * @param mins
     */
    public void setLengthMns(Integer mins) {
        this.lengthMns = mins;
    }
    
    /**
     * Action update.
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static final String PATTERN =
            SendMsg.Type.ACTION.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM +
            NOTES        + DELIM + "{2}"  + DELIM +
            DATE         + DELIM + "{3}"  + DELIM +
            PATH         + DELIM + "{4}"  + DELIM +
            TOPIC_ID     + DELIM + "{5}"  + DELIM +
            CONTEXT_ID   + DELIM + "{6}"  + DELIM +
            TIME_ID      + DELIM + "{7}"  + DELIM +
            ENERGY_ID    + DELIM + "{8}"  + DELIM +
            PRIORITY_ID  + DELIM + "{9}"  + DELIM +
            STATE        + DELIM + "{10}" + DELIM +
            START_HR     + DELIM + "{11}" + DELIM +
            START_MN     + DELIM + "{12}" + DELIM +
            LENGTH_HRS   + DELIM + "{13}" + DELIM +
            LENGTH_MNS   + DELIM + "{14}" + DELIM +
            DELEGATE     + DELIM + "{15}" + DELIM +
            PARENT_ID    + DELIM + "{16}" + DELIM +
            ORDINAL      + DELIM + "{17}" + DELIM;

    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Action.ID, factory.numberNode(action.getID()));
        node.set(Fields.Send.Action.TITLE, factory.textNode(action.getDescription()));
        node.set(Fields.Send.Action.NOTES, factory.textNode(getNotes()));
        Date actionDate = getDate();
        if (actionDate == null) {
            node.set(Fields.Send.Action.DATE, factory.nullNode());            
        } else {
            node.set(Fields.Send.Action.DATE, factory.numberNode(actionDate.getTime()));            
        }
        node.set(Fields.Send.Action.PATH, factory.textNode(getPath()));

        node.set(Fields.Send.Action.TOPIC_ID, factory.numberNode(action.getTopic().getID()));            
        node.set(Fields.Send.Action.CONTEXT_ID, factory.numberNode(action.getContext().getID()));            

        Value time = action.getTime();        
        if (time == null) {
            node.set(Fields.Send.Action.TIME_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Action.TIME_ID, factory.numberNode(time.getID()));                        
        }
        Value energy = action.getEnergy();        
        if (energy == null) {
            node.set(Fields.Send.Action.ENERGY_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Action.ENERGY_ID, factory.numberNode(energy.getID()));                        
        }
        Value priority = action.getPriority();        
        if (priority == null) {
            node.set(Fields.Send.Action.PRIORITY_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Action.PRIORITY_ID, factory.numberNode(priority.getID()));                        
        }
        node.set(Fields.Send.Action.STATE, factory.textNode(getStateStr()));                        
        Integer hr = getSchdHr();
        node.set(Fields.Send.Action.START_HR, hr == null ? factory.nullNode() : factory.numberNode(hr));                        
        Integer mn = getSchdMn();
        node.set(Fields.Send.Action.START_MN, mn == null ? factory.nullNode() : factory.numberNode(mn));                        
        Integer hrs = getLengthHrs();
        node.set(Fields.Send.Action.LENGTH_HRS, hrs == null ? factory.nullNode() : factory.numberNode(hrs));                        
        Integer mns = getLengthMns();
        node.set(Fields.Send.Action.LENGTH_MNS, mns == null ? factory.nullNode() : factory.numberNode(mns));                        
        node.set(Fields.Send.Action.DELEGATE, factory.textNode(getDelegate()));
        Integer projectID = getProjectID();
        if (projectID == null) {
            node.set(Fields.Send.Action.PARENT_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Action.PARENT_ID, factory.numberNode(projectID));            
        }
        node.set(Fields.Send.Action.ORDINAL, factory.numberNode(ordinal));            
        return node;
    } 
    
    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN,
                getID(),
                SyncUtils.escape(getTitle()),
                SyncUtils.escape(getNotes()),
                getDateStr(),
                SyncUtils.escape(getPath()),
                getTopicID(),
                getContextID(),
                getTimeID(),
                getEnergyID(),
                getPriorityID(),
                getStateStr(),
                getSchdHrString(),
                getSchdMinString(),
                getLengthHrsString(),
                getLengthMnsString(),
                getDelegate(),
                getProjectIDString(),
                getOrdinal());
    }


    private String getID() {
        return String.valueOf(action.getID());
    }
    private String getTitle() {
        return SyncUtils.escape(action.getDescription());
    }
    private String getNotes() {
        return notes != null ? notes : action.getNotes();
    }
    public Date getDate() {
        return date != null ? date : action.getActionDate();
    }
    private String getDateStr() {
        Date _date = getDate();
        return _date == null ? "" : DF_DATE.format(_date);
    }
    private String getPath() {
        return Services.instance.getPath(action);
    }
    private String getTopicID() {
        return String.valueOf(action.getTopic().getID());
    }
    private String getContextID() {
        return String.valueOf(action.getContext().getID());
    }
    private String getTimeID() {
        if (!timeUsed) {
            return "";
        }
        return action.getTime() == null ? "" : "" + action.getTime().getID();
    }
    private String getEnergyID() {
        if (!energyUsed) {
            return "";
        }
        return action.getEnergy() == null ? "" : "" + action.getEnergy().getID();
    }
    private String getPriorityID() {
        if (!priorityUsed) {
            return "";
        }
        return action.getPriority() == null ? "" : "" + action.getPriority().getID();
    }

    public ActionState.Type getState() {
        return action.getState().getType();
    }
    
    private String getStateStr() {
        switch (getState()) {
            case INACTIVE:
                return "I";
            case DOASAP:
                return "A";
            case SCHEDULED:
                return "S";
            case DELEGATED:
                return "D";
            default:
                return "";
        }
    }
    private String getSchdHrString() {
        Integer hr = getSchdHr();
        return hr == null ? "" : String.valueOf(hr);
    }
    
    private Integer getSchdHr() {
        if (!action.isStateScheduled()) {
            return null;
        }
        if (startHr != null) {
            return startHr;
        }
        ActionStateScheduled schdState = (ActionStateScheduled)action.getState();
        if (schdState == null) {
            return null;
        }
        Date schdDate = schdState.getDate();
        if (schdDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(schdDate);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    
    private String getSchdMinString() {
        Integer mn = getSchdMn();
        return mn == null ? "" : String.valueOf(mn);
    }

    private Integer getSchdMn() {
        if (!action.isStateScheduled()) {
            return null;
        }
        if (startMn != null) {
            return startMn;
        }
        ActionStateScheduled schdState = (ActionStateScheduled)action.getState();
        if (schdState == null) {
            return null;
        }
        Date schdDate = schdState.getDate();
        if (schdDate == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(schdDate);
        return cal.get(Calendar.MINUTE);
    }
    
    
    private String getLengthHrsString() {
        Integer hrs = getLengthHrs();
        return hrs == null ? "" : String.valueOf(hrs);        
    }
    
    private Integer getLengthHrs() {
        if (!action.isStateScheduled()) {
            return null;
        }
        if (lengthHrs != null) {
            return lengthHrs;
        }
        ActionStateScheduled schdState = (ActionStateScheduled)action.getState();
        if (schdState == null) {
            return null;
        }
        return schdState.getDurationHours();
    }
    
    
    private String getLengthMnsString() {
        Integer mns = getLengthMns();
        return mns == null ? "" : String.valueOf(mns);        
    }
    
    private Integer getLengthMns() {
        if (!action.isStateScheduled()) {
            return null;
        }
        if (lengthMns != null) {
            return lengthMns;
        }        
        ActionStateScheduled schdState = (ActionStateScheduled)action.getState();
        if (schdState == null) {
            return null;
        }
        return schdState.getDurationMinutes();
    }
    
    private String getDelegate() {
        if (!action.isStateDelegated()) {
            return "";
        }
        ActionStateDelegated state = (ActionStateDelegated)action.getState();
        return state == null ? "" : state.getTo();
    }

}

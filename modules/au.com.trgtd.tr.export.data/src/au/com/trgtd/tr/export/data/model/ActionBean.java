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
package au.com.trgtd.tr.export.data.model;

import java.util.Date;

public final class ActionBean implements ItemBean {

    private final int id;
    private final String title;
    private final String notes;
    private final String success;
    private final int order;                 // order in project    
    private final Integer projectID;
    private final Integer thoughtID;
    private final Integer topicID;
    private final Integer contextID;
    private final Integer energyID;
    private final Integer priorityID;
    private final Long time;                 // time (in minutes)    
    private final Date created;
    private final Date startDate;
    private final Date dueDate;
    private final Date doneDate;
    private final int state; // 0=DoASAP, 1=Inactive, 2=Delegated, 3=Scheduled             
    private final Integer delegateID;        // delegate contact ID
    private final String delegateName;       // delegate name (if no delegateID)
    private final Date delegateFollow;       // delegate follow up date 

    public ActionBean(int id,
            String title, String notes, String success,
            Integer projectID, int order,
            Integer thoughtID, Integer topicID, Integer contextID,
            Integer energyID, Integer priorityID, Long time,
            Date created, Date startDate, Date dueDate, Date doneDate,
            int state, Integer delegateID, String delegateName, Date delegateFollow) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.success = success;
        this.thoughtID = thoughtID;
        this.topicID = topicID;
        this.contextID = contextID;
        this.energyID = energyID;
        this.priorityID = priorityID;
        this.projectID = projectID;
        this.order = order;
        this.time = time;
        this.created = created;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
        this.state = state;
        this.delegateID = delegateID;
        this.delegateName = delegateName;
        this.delegateFollow = delegateFollow;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public String getSuccess() {
        return success;
    }

    public int getOrder() {
        return order;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public Integer getThoughtID() {
        return thoughtID;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public Integer getContextID() {
        return contextID;
    }

    public Integer getEnergyID() {
        return energyID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public Long getTime() {
        return time;
    }

    public Date getCreated() {
        return created;
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

    public int getState() {
        return state;
    }

    public Integer getDelegateID() {
        return delegateID;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public Date getDelegateFollow() {
        return delegateFollow;
    }

}

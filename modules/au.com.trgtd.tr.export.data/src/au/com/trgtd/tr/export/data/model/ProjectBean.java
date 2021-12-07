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

public final class ProjectBean implements ItemBean {

    private final int id;
    private final String title;
    private final String notes;
    private final String purpose;
    private final String vision;
    private final String brainstorm;
    private final String organising;
    private final int order;
    private final Integer projectID;
    private final Integer thoughtID;
    private final Integer topicID;
    private final Integer priorityID;
    private final Date created;
    private final Date startDate;
    private final Date dueDate;
    private final Date doneDate;
//  public final List<MItem> children;
//  public final boolean sequence;     
//  public final Boolean seqIncludeProjects;
//  public final Boolean seqIncludeScheduled;
//  public final Boolean seqIncludeDelegated;

    public ProjectBean(int id, String title, String notes, 
                    String purpose, String vision, String brainstorm, String organising, 
                    int order, Integer projectID, 
                    Integer thoughtID, Integer topicID, Integer priorityID, 
                    Date created, Date startDate, Date dueDate, Date doneDate) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.purpose = purpose;
        this.vision = vision;
        this.brainstorm = brainstorm;
        this.organising = organising;
        this.order = order;
        this.projectID = projectID;
        this.thoughtID = thoughtID;
        this.topicID = topicID;
        this.priorityID = priorityID;
        this.created = created;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.doneDate = doneDate;
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

    public String getPurpose() {
        return purpose;
    }

    public String getVision() {
        return vision;
    }

    public String getBrainstorm() {
        return brainstorm;
    }

    public String getOrganising() {
        return organising;
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

    public Integer getPriorityID() {
        return priorityID;
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
        
}

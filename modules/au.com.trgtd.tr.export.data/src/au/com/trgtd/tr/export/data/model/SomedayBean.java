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

public final class SomedayBean {

    private final int id;
    private final String title;
    private final String notes;
    private final Integer thoughtID;
    private final Integer topicID;
    private final Date created;
    private final Date tickle;

    public SomedayBean(int id, String title, String notes, Integer thoughtID, Integer topicID, Date created, Date tickle) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.thoughtID = thoughtID;
        this.topicID = topicID;
        this.created = created;
        this.tickle = tickle;
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

    public Integer getThoughtID() {
        return thoughtID;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public Date getCreated() {
        return created;
    }

    public Date getTickle() {
        return tickle;
    }
    
}

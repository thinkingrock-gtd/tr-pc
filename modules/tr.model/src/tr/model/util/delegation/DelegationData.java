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
package tr.model.util.delegation;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * Defines an object to hold data sent via email when an action is delegated or
 * a delegation action is set done or not done.
 * All string values are either null or not blank.
 *
 * @author Jeremy Moore
 */
public final class DelegationData {

    public enum Type {
        DELEGATION, RESPONSE
    }

    public final Type type;
    public final Integer id;
    public final String reply;
    public final String success;
    public final String time;
    public final String energy;    
    public final String priority;
    public final String topic;
    public final Date start;
    public final Date due;
    public final Date done;

    DelegationData(
            Type type,
            Integer id,
            String reply,
            String success,
            String time,
            String energy,
            String priority,
            String topic,
            Date start,
            Date due,
            Date done) {
        this.type = type;
        this.id = id;
        this.reply = nullIfBlank(reply);
        this.success = nullIfBlank(success);
        this.time = nullIfBlank(time);
        this.energy = nullIfBlank(energy);        
        this.priority = nullIfBlank(priority);
        this.topic = nullIfBlank(topic);
        this.start = start;
        this.due = due;
        this.done = done;
        validate();
    }

    private void validate() {
        if (type == null) {
            throw new IllegalArgumentException("Null type.");
        }
        if (id == null) {
            throw new IllegalArgumentException("Null id.");
        }
        if (StringUtils.isBlank(reply)) {
            throw new IllegalArgumentException("Null or blank reply.");
        }        
    }
    
    private String nullIfBlank(String s) {
        return StringUtils.isBlank(s) ? null : s.trim();
    }

    public String serialize() {
        return DelegationUtils.serialize(this);
    }
    
}

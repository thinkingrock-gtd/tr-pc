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

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import tr.model.action.Action;
import tr.model.criteria.Value;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import static tr.model.util.delegation.DelegationData.Type.DELEGATION;
import static tr.model.util.delegation.DelegationData.Type.RESPONSE;

/**
 * Utilities for dealing with delegate data.
 *
 * @author Jeremy Moore
 */
public final class DelegationUtils {

    private static final Logger LOG = Logger.getLogger("tr.delegation.utils");

    /**
     * Determines whether or not an action was created for a delegation email.
     * @param action The action.
     * @return true if the thought associated with the action is marked as a
     * delegation task (i.e. it was created for a delegation email). 
     */
    public static synchronized boolean isDelegationAction(Action action) {
        if (action == null) {
            LOG.info("Action null.");
            return false;
        }
        Thought thought = action.getThought();
        if (thought == null) {
            LOG.info("Thought null.");
            return false;
        }
        return thought.isDelegation();
    }

    /**
     * Should be called when an action done state changes so that a delegation
     * response email can be sent back to the delegator.
     * @param action The action.
     */
    public static void handleDelegationResponce(final Action action) {
        if (isDelegationAction(action)) {
            DelegationResponder.process(action);
        }
    }

    /**
     * Get delegate data for an action.
     *
     * @param action The action.
     * @return a delegate data object or null.
     */
    public static DelegationData getSendDelegateData(Action action) {
        String reply = EmailPrefs.getEmailAddress();
        Value time = action.getTime();
        Value energy = action.getEnergy();
        Value priority = action.getPriority();
        Topic topic = action.getTopic();
        try {
            return new DelegationDataBuilder(DELEGATION)
                    .reply(reply)
                    .success(action.getSuccess())
                    .time(time == null ? null : time.getName())
                    .energy(energy == null ? null : energy.getName())
                    .priority(priority == null ? null : priority.getName())
                    .topic(topic == null ? null : topic.getName())
                    .start(action.getStartDate())
                    .due(action.getDueDate())
                    .id(action.getID())
                    .make();
        } catch (IllegalArgumentException x) {
            // No reply email address
            return null;
        }
    }

    /**
     * Get delegate data for a response.
     *
     * @param action The action.
     * @return a delegate data object or null if reply is blank or null.
     */
    static DelegationData getResponseDelegateData(Action action) {
        
        Thought thought = action.getThought();
        if (thought == null) {
            return null;
        }        
        Integer delegatedActionID = thought.getDelegationActionID();
        if (delegatedActionID == null) {
            return null;
        }        
        
        String reply = EmailPrefs.getEmailAddress();
        try {
            return new DelegationDataBuilder(RESPONSE)
                    .reply(reply)
                    .done(action.getDoneDate())
                    .id(delegatedActionID)
                    .make();
        } catch (IllegalArgumentException x) {
            return null;
        }
    }

    /**
     * Serialize delegate data.
     *
     * @param dd delegate data.
     * @return the serialized data as a string.
     */
    public static String serialize(DelegationData dd) {
        JsonObject job = Json.object();
        addIfNotNull(job, "reply", dd.reply);
        addIfNotNull(job, "type", dd.type.ordinal());        
        addIfNotNull(job, "success", dd.success);
        addIfNotNull(job, "time", dd.time);
        addIfNotNull(job, "energy", dd.energy);        
        addIfNotNull(job, "priority", dd.priority);
        addIfNotNull(job, "topic", dd.topic);
        addIfNotNull(job, "start", dd.start);
        addIfNotNull(job, "due", dd.due);
        addIfNotNull(job, "done", dd.done);
        addIfNotNull(job, "id", dd.id);
        return job.toString();
    }

    private static void addIfNotNull(JsonObject job, String name, String string) {
        if (string != null) {
            job.add(name, string);
        }
    }

    private static void addIfNotNull(JsonObject job, String name, Date date) {
        if (date != null) {
            job.add(name, date.getTime());
        }
    }

    private static void addIfNotNull(JsonObject job, String name, Integer integer) {
        if (integer != null) {
            job.add(name, integer);
        }
    }

    /**
     * De-serialize delegate data from a serialized string.
     *
     * @param ss The delegate data serialized string.
     * @return A de-serialized delegate data object or null if the serialized
     * string is null or can not be parsed.
     */
    public static DelegationData deserialize(String ss) {
        if (ss == null) {
            return null;
        }        
        try {
            JsonObject job = Json.parse(ss).asObject();
            
            int type = job.getInt("type", -1);            
            if (type == DELEGATION.ordinal()) {
                return new DelegationDataBuilder(DELEGATION)
                    .reply(job.getString("reply", ""))
                    .success(job.getString("success", ""))
                    .time(job.getString("time", ""))
                    .energy(job.getString("energy", ""))                        
                    .priority(job.getString("priority", ""))
                    .topic(job.getString("topic", ""))
                    .start(job.getLong("start", 0))
                    .due(job.getLong("due", 0))
                    .done(job.getLong("done", 0))
                    .id(job.getInt("id", 0))
                    .make();
            }            
            if (type == RESPONSE.ordinal()) {            
                return new DelegationDataBuilder(RESPONSE)
                    .reply(job.getString("reply", ""))
                    .done(job.getLong("done", 0))
                    .id(job.getInt("id", 0))
                    .make();
            }            
        } catch (ParseException x) {
        }

        return null;
    }
    
    /**
     * Extract a serialized string within a larger string.
     *
     * @param s The string to examine.
     * @return the serialized string or null if not found.
     */
    public static String extractSerializedString(String s) {
        final Pattern p = Pattern.compile("(\\{\"reply\":[\\s\\S]*?\"id\":\\d+\\})");
        final Matcher m = p.matcher(s);
        // get the last occurance
        String ss = null;
        while (m.find()) {
            ss = m.group();            
        } 
        return ss;
    }
    
    /**
     * Remove delegation serialized strings from a string.
     * @param s The string
     * @return The string with delegation serialized strings removed.
     */
    public static String removeSerializedStrings(String s) {
        String rs = s;        
        String ss = extractSerializedString(rs);
        while (ss != null) {
            rs = rs.replace(ss, "");
            ss = extractSerializedString(rs);
        }
        return rs;
    }

    /**
     * Deeply compares two delegate data objects for equality.
     *
     * @param dd1 The first delegate data object.
     * @param dd2 The second delegate data object.
     * @return true if all corresponding values are equal.
     */
    static boolean deepEquals(DelegationData dd1, DelegationData dd2) {
        return Objects.equals(dd1.id, dd2.id)
                && Objects.equals(dd1.start, dd2.start)
                && Objects.equals(dd1.due, dd2.due)
                && Objects.equals(dd1.time, dd2.time)
                && Objects.equals(dd1.energy, dd2.energy)                
                && Objects.equals(dd1.priority, dd2.priority)
                && Objects.equals(dd1.reply, dd2.reply)
                && Objects.equals(dd1.success, dd2.success)
                && Objects.equals(dd1.topic, dd2.topic)
                && Objects.equals(dd1.done, dd2.done);
    }

}

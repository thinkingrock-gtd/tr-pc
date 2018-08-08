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

package tr.model.action;

import au.com.trgtd.tr.util.Utils;
import java.util.Calendar;
import java.util.Date;
import tr.model.action.ActionState.Type;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;

/**
 * Delegated action state.
 *
 * @author Jeremy Moore
 */
public class ActionStateDelegated extends ActionState {
    
    private String to;      // delegated to
    private Date chase;	    // follow-up date
    private int actorID;    // delegate actor ID
    
    /**
     * Constructs a new instance.
     */
    public ActionStateDelegated() {
        super();
    }
    
    /**
     * Makes an exact copy of itself.
     * @return The copy.
     */
    @Override
    public ActionState copy() {
        ActionStateDelegated copy = new ActionStateDelegated();
        copy.created = this.created;
        copy.to = this.to;
        copy.chase = this.chase;
        copy.actorID = this.actorID;
        return copy;
    }


//    /**
//     * Gets the old "delegated to" value used for upgrade to data version 5.
//     * @return the old delegated to value.
//     * @deprecated 3.0.1 Use actor details.
//     */
//    public String getOldTo() {
//        return to;
//    }


    /**
     * Determines whether or not a delegate value is set.
     * @return true if a delegate value is set.
     * @since 3.0.3.
     */
    public boolean hasDelegateValue() {
        return actorID > 0;
    }

    /**
     * Determines whether or not a delegate "to" free text value is set.
     * @return true if a delegate "to" value is set.
     * @since 3.0.3.
     */
    public boolean hasToValue() {
        return to != null && to.trim().length() > 0;
    }

    /**
     * Gets the delegate name.
     * @return The delegate name if a delegate is assigned, otherwise the simple
     * text delegate to value.
     */
    public String getTo() {
//      return to;
//      @since 3.0.2
        Actor actor = ActorUtils.instance().getActor(actorID);
        if (actor == null) {
            return to == null ? "" : to;
        }
        return actor.getName();
    }
    
    /**
     * Sets the delegated to value.
     * @param to The delegated to value.
     */
    public void setTo(String to) {
        if (to != null) {
            to = to.trim();
        }
        if (!Utils.equal(this.to, to)) {
            this.to = to;
            this.actorID = 0;
            notifyObservers(this);
        }
    }

    /**
     * Get delegate actor identifier.
     * @return the actor ID
     */
    public int getActorID() {
        return actorID;
    }

    /**
     * Set delegate actor identifier.
     * @param actorID the actor ID to set
     */
    public void setActorID(int actorID) {
        if (actorID != this.actorID) {
            this.actorID = actorID;
            this.to = null;
            notifyObservers(this);
        }
    }

    /**
     * Gets the delegate email address if possible.
     * @return the delegate email address or delegate to value.
     * @since 3.0.2
     */
    public String getEmail() {
        Actor actor = ActorUtils.instance().getActor(actorID);
        if (actor == null) {
            return to == null ? "" : to;
        }
        return actor.getEmail().length() > 0 ? actor.getEmail() : actor.getName();
    }

    /**
     * Gets the follow-up date value.
     * @return the follow-up date value.
     */
    public Date getDate() {
        return chase;
    }
    
    /**
     * Is the delegate a team member?
     * @return True if so.
     */
    public boolean isTeamMember() {
        Actor actor = ActorUtils.instance().getActor(actorID);
        return actor == null ? false : actor.isTeam();
    }    
    
    /**
     * Sets the follow-up date value after clearing time values.
     * @param date The date to set.
     */
    public void setDate(Date date) {
        // remove time from date
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();
        }
        
        if (!Utils.equal(this.chase, date)) {
            this.chase = date;
            notifyObservers(this);
        }
    }
    
    /**
     * Overrides equals to compare this state and another object for equality.
     * @param object the object to compare with.
     * @return true if the object is an ActionStateDelegated instance, the
     * creation dates, delegated to values and chase dates are equal.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        ActionStateDelegated asd = (ActionStateDelegated)object;
        if (!Utils.equal(to, asd.to)) {
            return false;
        }
        if (!Utils.equal(chase, asd.chase)) {
            return false;
        }
        if (!Utils.equal(actorID, asd.actorID)) {
            return false;
        }
        return true;
    }
    
    @Override
    public final ActionState.Type getType() {
        return Type.DELEGATED;
    }
    
}

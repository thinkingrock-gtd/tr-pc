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

package tr.model.information;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;
import java.beans.PropertyChangeSupport;
import tr.model.Item.Notable;

/**
 * Information item.
 *
 * @author Jeremy Moore
 */
public class Information extends ObservableImpl
        implements TopicChangeCookie, Comparable<Information>, Notable {
    
    private Date created;
    private String description;
    private Topic topic;
    private String location;
    private String notes;
    /* @since 2.2.1 */
    private Integer id;

//  public static final String PROP_NOTES = Notable.PROP_NOTES;

    private transient PropertyChangeSupport propertyChangeSupport;

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(property, listener);
    }

    
    /** Observable changes. @since 2.0 */
    public static enum Change { DESCRIPTION, TOPIC, LOCATION, NOTES }
    
    /** Constructs a new instance. */
    public Information(int id) {
        this.id = id;
        this.created = Calendar.getInstance().getTime();
        setDescription("");
        setTopic(Topic.getDefault());
        setLocation("");
        setNotes("");
    }

    public void initID(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public int getID() {
        return id;
    }
    
    public Date getCreated() {
        return created;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        if (Utils.equal(this.description, description)) return;
        
        this.description = description;
        
        notifyObservers(this, Change.DESCRIPTION);
    }
    
    public Topic getTopic() {
        if (topic == null) {
            return Topic.getDefault();
        }
        if (topic.getName().equals(Topic.getDefault().getName())) {
            return Topic.getDefault();
        }
        return topic;
    }
    
    public void setTopic(Topic topic) {
        if (Utils.equal(this.topic, topic)) return;
        
        this.topic = topic;
        
        notifyObservers(this, Change.TOPIC);
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(String location) {
        if (Utils.equal(this.location, location)) return;
        
        this.location = location;
        
        notifyObservers(this, Change.LOCATION);
    }
    
    /**
     * Gets the notes.
     * @return the notes.
     */
    public String getNotes() {
        return (notes == null) ? "" : notes;
    }
    
    /**
     * Sets the notes.
     * @param notes The notes.
     */
    public void setNotes(String notes) {
        if (Utils.equal(this.notes, notes)) {
            return;
        }

        String oldValue = this.notes;

        this.notes = notes;

        notifyObservers(this, Change.NOTES);

        getPropertyChangeSupport().firePropertyChange(PROP_NOTES, oldValue, notes);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Information)) return false;
        Information information = (Information)object;
        if (!Utils.equal(created, information.created)) return false;
        if (!Utils.equal(description, information.description)) return false;
        if (!Utils.equal(topic, information.topic)) return false;
        if (!Utils.equal(location, information.location)) return false;
        if (!Utils.equal(notes, information.notes)) return false;
        return true;
    }
    
    @Override
    public String toString() {
        return description;
    }
    
    /**
     * Implement Comparable to provide ordering by topic and description.
     * @param other The other information item to compare to.
     * @return -1, 0, 1 if this is less than, equal to or greater than object respectively.
     */
    public int compareTo(Information other) {
        int rslt = topic.compareTo(other.topic);
        if (rslt != 0) { // different topics
            return rslt;
        } else { // equal topics so compare descriptions
            return description.compareToIgnoreCase(other.description);
        }
    }
    
}

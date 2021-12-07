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
package tr.model.future;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import java.beans.PropertyChangeSupport;
import tr.model.Item.Notable;

/**
 * Future item.
 *
 * @author Jeremy Moore
 */
public class Future extends ObservableImpl implements Comparable, Observer,
        TopicChangeCookie, Notable {
    
    private Date created;
    private Thought thought;	// the originating thought if any
    private String description;
    private Topic topic;
    private String notes;
    /* @since 2.0.1 */
    private Date tickle;
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
    
    
    /** Observable changes. @since 2.0.1 */
    public static enum Change { DESCRIPTION, TOPIC, NOTES, TICKLE }
    
    /**
     * Constructs a new instance.
     */
    public Future(int id) {
        this.id = id;
        this.created = Calendar.getInstance().getTime();
        setThought(null);
        setDescription("");
        setTopic(Topic.getDefault());
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
    
    public Thought getThought() {
        return thought;
    }
    
    public void setThought(Thought thought) {
        if (Utils.equal(this.thought, thought)) return;
        
        this.thought = thought;
        notifyObservers(this);
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
    
//    /**
//     * Gets the notes in text format.
//     * @return the notes as text.
//     */
//    public String getNotesText() {
//        return HTML.html2text(notes);
//    }
//
//    /**
//     * Gets the notes in the stored HTML format.
//     * @return the notes as HTML.
//     */
//    public String getNotesHTML() {
//        if (notes == null) {
//            notes = "";
//        }
//        return notes;
//    }
//
//    /**
//     * Sets the notes in the storage HTML format.
//     * @param html The notes as HTML.
//     */
//    public void setNotesHTML(String html) {
//        if (Utils.equal(this.notes, html)) return;
//
//        this.notes = html;
//
//        notifyObservers(this);
//    }
    
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
    
    /**
     * Gets the tickle date.
     * @return the tickle date.
     * @since 2.0.1
     */
    public Date getTickle() {
        return tickle;
    }
    
    /**
     * Sets the tickle date.
     * @param date The date to set.
     * @since 2.0.1
     */
    public void setTickle(Date date) {
        if (!Utils.equal(tickle, date)) {
            tickle = date;
            notifyObservers(this, Change.TICKLE);
        }
    }
    
    /**
     * Implement Comparable to provide ordering by topic and description.
     * @param object The Object to compare to.
     * @return -1, 0, 1 if this is less than, equal to or greater than object respectively.
     */
    public int compareTo(Object object) {
        if (!(object instanceof Future)) {
            return -1;
        }

        Future other = (Future)object;

        if (topic == null) {
            if (other.getTopic() == null) {
                return description.compareToIgnoreCase(other.description);
            } else {
                return 1;
            }
        }

        int result = topic.compareTo(other.topic);
        if (result != 0) { // different topics
            return result;
        }
        
        // equal topics
        return description.compareToIgnoreCase(other.description);
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof Future)) return false;
        Future future = (Future)object;
        if (!Utils.equal(created, future.created)) return false;
        if (!Utils.equal(thought, future.thought)) return false;
        if (!Utils.equal(description, future.description)) return false;
        if (!Utils.equal(topic, future.topic)) return false;
        if (!Utils.equal(notes, future.notes)) return false;
        return true;
    }
    
    public String toString() {
        return description;
    }
    
    /**
     * Implement Observer to pass on changes to Observers.
     */
    public void update(Observable observable, Object arguement) {
        notifyObservers(observable, arguement);
    }
    
}

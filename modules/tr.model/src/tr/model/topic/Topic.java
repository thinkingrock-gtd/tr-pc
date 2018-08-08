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
package tr.model.topic;

import au.com.trgtd.tr.appl.Constants;
import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_TOPIC;
import java.awt.Color;

import org.openide.util.NbBundle;
import au.com.trgtd.tr.util.ObservableImpl;

/**
 * Class that represents a topic.  A topic is a subject that an action or
 * project relates to and is used for categorization.
 *
 * @author Jeremy Moore
 */
public class Topic extends ObservableImpl implements Comparable<Topic> {

    private static final long serialVersionUID = 989435278L;
    private static final String DEFAULT_NAME = "None";
    private static Topic def;

    private String name = "";
    private String description = "";
    private Color color = Color.black;
    private Color background = Color.white;
    /* @since 2.2.1 */
    private Integer id;

    public transient String key;

    /**
     * Constructs a new instance.
     */
    public Topic(int id) {
        this.id = id;
    }

    public void initID(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public int getID() {
        // fudge to fix default id
//      return id;
        if (id == null || id == ID_DEFAULT_TOPIC) {
            return ID_DEFAULT_TOPIC;
        }        
        return DEFAULT_NAME.equals(name) ? Constants.ID_DEFAULT_TOPIC : id;
    }

    /**
     * Gets the name value.
     * @return the name value
     */
    public String getName() {
        if (name.equals(DEFAULT_NAME)) {
            return NbBundle.getMessage(Topic.class, DEFAULT_NAME);
        } else {
            return name;
        }
    }

    /**
     * Sets the name value.
     * @param name The name to set.
     */
    public void setName(String name) {
        if (name == null) {
            // can not set to null
            return;
        }
        if (name.equals(this.name)) {
            // no change
            return;
        }
        if (name.equals(DEFAULT_NAME)) {
            // can not set to default topic name
            return;
        }

        this.name = name;

        notifyObservers(this);
    }

    /**
     * Gets the description value.
     * @return the description value.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description value.
     * @param description The description value to set.
     */
    public void setDescription(String description) {
        if (description == null || description.equals(this.description)) {
            return;
        }

        this.description = description;

        notifyObservers(this);
    }

    /**
     * Gets the foreground color value.
     * @return The color value.
     */
    public Color getForeground() {
        return color;
    }

    /**
     * Gets the foreground color value.
     * @param color The color value to set.
     */
    public void setForeground(Color color) {
        if (color == null || color.equals(this.color)) {
            return;
        }

        this.color = color;
        notifyObservers(this);
    }

    /**
     * Gets the background color value.
     * @return The color value.
     */
    public Color getBackground() {
        return (background == null) ? Color.white : background;
    }

    /**
     * Gets the background color value.
     * @param color The color value to set.
     */
    public void setBackground(Color background) {
        if (background == null || background.equals(this.background)) {
            return;
        }

        this.background = background;

        notifyObservers(this);
    }

    /**
     * Gets the default topic.
     * @return the default topic.
     */
    public static Topic getDefault() {
        if (def == null) {
            def = new Topic(Constants.ID_DEFAULT_TOPIC);
//          def.setName(NbBundle.getMessage(Topic.class, "None"));
            def.name = DEFAULT_NAME;
            def.description = NbBundle.getMessage(Topic.class, "No_topic");
        }
        return def;
    }

    /**
     * Gets the string representation.
     * @return The name.
     */
    @Override
    public String toString() {
//      return name;
        return getName();
    }

    /**
     * Override equals to compare this topic with another object for equality,
     * @return true if the object is a topic with equal name, description
     * and color.
     */
    public boolean equals(Object object) {
        return (object instanceof Topic && name.equals(((Topic)object).name));
    }

    /**
     * Implement Comparable to provide case insensitive alpabetic ordering.
     * @param topic The Topic to compare to.
     * @return -1, 0, 1 if this.toString() is less than, equal to or greater
     * than topic.toString() respectively.
     */
    public int compareTo(Topic topic) {
        if (topic == null || topic.name == null) {
            return -1;
        }
        if (name.equals(getDefault().name)) {
            if (topic.name.equals(getDefault().name)) {
                return 0;
            }
            return -1;
        }
        if (topic.name.equals(getDefault().name)) {
            return 1;
        }
        return name.compareToIgnoreCase(topic.name);
    }
}

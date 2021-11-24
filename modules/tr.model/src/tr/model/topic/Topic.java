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
package tr.model.topic;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.util.ObservableImpl;
import java.awt.Color;
import java.util.Objects;
import org.openide.util.NbBundle;

/**
 * Class that represents a topic. A topic is a subject that an action or project
 * relates to and is used for categorization.
 *
 * @author Jeremy Moore
 */
public class Topic extends ObservableImpl implements Comparable<Topic> {

    private static final String DEF_NAME = "None";
    private static final String DEF_NAME_I18N = NbBundle.getMessage(Topic.class, DEF_NAME);
    private static final String DEF_DESC_I18N = NbBundle.getMessage(Topic.class, "No_topic");

    public static final Topic DEFAULT;

    static {
        DEFAULT = new Topic(Constants.ID_DEFAULT_TOPIC);
        DEFAULT.name = DEF_NAME;
        DEFAULT.description = DEF_DESC_I18N;
    }

    public static final boolean isDefault(Topic t) {
        return null == t
                || null == t.id
                || null == t.name
                || 0 == t.name.trim().length()
                || DEFAULT.id.equals(t.id)
                || DEFAULT.name.equals(t.name);
    }

    public static Topic getDefault() {
        return DEFAULT;
    }

    private static final long serialVersionUID = 989435278L;

    private String name = "";
    private String description = "";
    private Color color = Color.black;
    private Color background = Color.white;
    /* @since 2.2.1 */
    private Integer id;

    public transient String key;

    /**
     * Constructs a new instance.
     *
     * @param id
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
        return isDefault(this) ? DEFAULT.id : id;
    }

    /**
     * Gets the name value.
     *
     * @return the name value
     */
    public String getName() {
        return isDefault(this) ? DEF_NAME_I18N : name;
    }

    /**
     * Sets the name value.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        String s = (null == name) ? "" : name.trim();

        if (s.length() == 0 || s.equals(DEFAULT.name) || s.equals(this.name)) {
            return;
        }

        this.name = s;

        notifyObservers(this);
    }

    /**
     * Gets the description value.
     *
     * @return the description value.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description value.
     *
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
     *
     * @return The color value.
     */
    public Color getForeground() {
        return color;
    }

    /**
     * Gets the foreground color value.
     *
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
     *
     * @return The color value.
     */
    public Color getBackground() {
        return (background == null) ? Color.white : background;
    }

    /**
     * Gets the background color value.
     *
     * @param background
     */
    public void setBackground(Color background) {
        if (background == null || background.equals(this.background)) {
            return;
        }

        this.background = background;

        notifyObservers(this);
    }

    /**
     * Gets the string representation.
     *
     * @return The name.
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Override equals to compare this topic with another object for equality,
     *
     * @param that
     * @return true if the object is a topic with equal name.
     */
    @Override
    public boolean equals(Object that) {
        return (that instanceof Topic && name.equals(((Topic) that).name));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Implement Comparable to provide case insensitive alphabetic ordering.
     *
     * @param that The Topic to compare to.
     * @return -1, 0, 1 if this.toString() is less than, equal to or greater
     * than topic.toString() respectively.
     */
    @Override
    public int compareTo(Topic that) {
        if (isDefault(this)) {
            return isDefault(that) ? 0 : -1;
        }
        if (isDefault(that)) {
            return 1;
        }        
        return name.compareToIgnoreCase(that.name);
    }
}

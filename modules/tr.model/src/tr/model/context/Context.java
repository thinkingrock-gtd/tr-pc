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
package tr.model.context;

import au.com.trgtd.tr.appl.Constants;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.util.ObservableImpl;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action context, for example "At computer" or "Phone calls to make".
 *
 * @author Jeremy Moore
 */
public class Context extends ObservableImpl implements Comparable<Context> {
    
    private static final String DEFAULT_NAME = "None";
    private static Context def;    
    private String name = "";
    private String description = "";
    /* @since 2.2.1 */
    private Integer id;

    public transient String key;

    /**
     * Constructs a new instance.
     */
    public Context(int id) {
        this.id = id;
    }

    public void initID(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public int getID() {
//      return getName().equals(DEFAULT_NAME) ? Constants.ID_DEFAULT_CONTEXT : id;

        if (DEFAULT_NAME.equals(getName())) {
            return Constants.ID_DEFAULT_CONTEXT;
        }

        if (id == null) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return Constants.ID_DEFAULT_CONTEXT;
            }
            id = data.getNextID();
        }

        return id;
    }
    
    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        // Change to handle NPE
        if (name == null) {
            name = "";
        }
        // End change
        if (name.equals(DEFAULT_NAME)) {
            return NbBundle.getMessage(Context.class, DEFAULT_NAME);
        } 
        return name;
    }
    
    /**
     * Sets the name.
     * @param name The name.
     */
    public void setName(String name) {
        if (name == null) {
            return;
        }
        if (name.equals(this.name)) {
            return;            
        }
        if (name.equals(DEFAULT_NAME)) {
            return;
        }
        
        this.name = name;
       
        notifyObservers(this);
    }
    
    /**
     * Gets the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * @param description The description.
     */
    public void setDescription(String description) {
        if (description == null) {
            return;
        }
        if (description.equals(this.description)) {
            return;
        }
        this.description = description;
        
        notifyObservers(this);
    }
    
    /**
     * Gets the default context instance.
     * @return the default context instance.
     */
    public static Context getDefault() {
        if (def == null) {
            def = new Context(Constants.ID_DEFAULT_CONTEXT);
            def.name = DEFAULT_NAME;
            def.description = NbBundle.getMessage(Context.class, "No_context");
        }
        return def;
    }
    
    /**
     * Gets the string representation.
     * @return the name.
     */
    public String toString() {
        return getName();
    }
    
    /**
     * Overrides equals to compare with another object for equality.
     * @return true if the object is a Context with equal name and description.
     */
    public boolean equals(Object object) {
        return (object instanceof Context && name.equals(((Context)object).name));
    }
    
    /**
     * Implement Comparable to provide case insensitive alphabetic ordering
     * except for the default context which comes first.
     * @param c The context to compare to.
     * @return -1, 0, 1 if this.name is less than, equal to or greater than
     * c.toString() respectively except for the default context which is first.
     */
    public int compareTo(Context context) {
        if (context == null || context.name == null) {
            return -1;
        }        
        // if this context is the default context
        if (name.equals(getDefault().name)) {
            // if other context is also the default context
            if (context.name.equals(getDefault().name)) {
                return 0; // equal
            }
            return -1; // this before other
        }        
        // if other context is the default context
        if (context.name.equals(getDefault().name)) {
            return 1; // this after other
        }        
        // neither are the default context
        return name.compareToIgnoreCase(context.name);        
    }
    
}

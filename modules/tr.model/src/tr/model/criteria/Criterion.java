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

package tr.model.criteria;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.util.Manager;

/**
 * Criterion is a named manager of criterion values.
 *
 * @author Jeremy Moore
 */
public class Criterion extends ObservableImpl implements Observable, Observer {
    
    private final String name;
    private boolean use;
    public final Manager<Value> values;
    
    public transient String key;
    
    /**
     * Constructs a new instance with the given name.
     * @param name The name.
     */
    public Criterion(String name) {
        super();
        this.name = name;
        values = new Manager<Value>();
        values.addObserver(this);
    }
        
    public boolean isUse() {
        return use;
    }

    /**
     * Sets the use value.
     */
    public void setUse(boolean use) {
        if (this.use == use) return;
        
        this.use = use;

        notifyObservers(this);        
    }

    public String getName() {
        return NbBundle.getMessage(Data.class, name);
    }

    /**
     * Resets observation of values.
     */
    @Override
    public void resetObservers() {
        values.resetObservers();
        values.addObserver(this);
    }
    
    /**
     * Passes on notification of changes in contained values.
     */
    @Override
    public void update(Observable observable, Object object) {
        notifyObservers(observable, object);
    }

    /**
     * Gets the display string.
     * @return The name.
     */
    @Override
    public String toString() {
        return getName();
    }
    
}

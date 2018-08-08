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
package au.com.trgtd.tr.view.contexts;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.context.Context;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Contexts ComboBoxModel.
 *
 * @author Jeremy Moore
 */
public class ContextsComboBoxModel extends DefaultComboBoxModel implements Observer {

    private Manager<Context> contextManager;
    private List<Context> contexts;
    private boolean all;
    private transient Lookup.Result result;

    /**
     * Creates a new instance without the All entry.
     */
    public ContextsComboBoxModel() {
        this(false);
    }    
    
    /**
     * Creates a new instance.
     * @param all Whether or not to include an item representing all contexts.
     */
    public ContextsComboBoxModel(boolean all) {
        super();
        this.all = all;
        initialise();
    }

    private void initialise() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            contexts = new Vector<Context>();
            return;
        }

        if (contextManager != null) {
            contextManager.removeObserver(this);
        }
        contextManager = data.getContextManager();
        contextManager.addObserver(this);

        contexts = contextManager.list();
        Collections.sort(contexts);
        if (all) {
            Context contextAll = new Context(data.getNextID());
//            contextAll.setName("All");
            contextAll.setName(NbBundle.getMessage(ContextsComboBoxModel.class, "all"));
            contexts.add(0, contextAll);
        }

        // if the data model changes we need to re-initialise
        if (result == null) {
            result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
            result.addLookupListener(new LookupListener() {
                public void resultChanged(LookupEvent lookupEvent) {
                    update(null, null);
                }
            });
        }
    }

    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Object getElementAt(int index) {
        return contexts.get(index);
    }

    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return contexts.size();
    }

    /**
     * Implement Observer to fire contents changed.
     */
    public void update(Observable o, Object arg) {
        initialise();
        fireContentsChanged(this, 0, getSize());
    }
}

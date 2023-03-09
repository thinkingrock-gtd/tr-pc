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
package au.com.trgtd.tr.view.contexts;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
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
public class ContextsComboBoxModel extends DefaultComboBoxModel<Context> implements Observer {

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
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            contexts = new Vector<>();
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
            result = DataLookup.instance().lookupResult(Data.class);
            result.addLookupListener((LookupEvent lookupEvent) -> {
                update(null, null);
            });
        }
    }

    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Context getElementAt(int index) {
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

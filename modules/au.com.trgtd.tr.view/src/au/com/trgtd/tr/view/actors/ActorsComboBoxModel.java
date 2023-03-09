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
package au.com.trgtd.tr.view.actors;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.util.Manager;

/**
 * Actors ComboBoxModel.
 *
 * @author Jeremy Moore
 */
public final class ActorsComboBoxModel extends DefaultComboBoxModel<Actor> implements Observer, LookupListener {

    public static final Actor ACTOR_CLR = new Actor(0);
    public static final Actor ACTOR_ADD = new Actor(1);
//  public static final Actor ACTOR_ALL = new Actor(2);
    static {
        ACTOR_CLR.setName("");
        ACTOR_ADD.setName(NbBundle.getMessage(ActorsComboBoxModel.class, "add.actor"));
//      ACTOR_ALL.setName(NbBundle.getMessage(ActorsComboBoxModel.class, "all.actors"));
    }

    private Manager<Actor> mngr;
    private List<Actor> list;
    private boolean inactive;
    private final Lookup.Result rslt;

    /** Creates a new instance. */
    public ActorsComboBoxModel() {
        super();
        this.rslt = DataLookup.instance().lookupResult(Data.class);
        this.rslt.addLookupListener(this);
        initModel();
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setIncludeInactive(boolean inactive) {
        if (this.inactive != inactive) {
            this.inactive = inactive;
            initModel();
        }
    }

    private synchronized void initModel() {
        stopObserving();

        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            mngr = null;
            list = Collections.emptyList();
            return;
        }

        mngr = data.getActorManager();

        startObserving();

        list = mngr.list();

        if (!inactive) {
            for (Iterator<Actor> iter = list.iterator(); iter.hasNext(); ) {
                if (iter.next().isInactive()) {
                    iter.remove();
                }
            }
        }

        Collections.sort(list, ActorUtils.COMPARATOR_AZ);
        
        list.add(0, ACTOR_ADD);

        fireContentsChanged(this, 0, getSize());
    }

    void addClearItem() {
        Actor actor = list.get(0);
        if (actor != null && actor.equals(ACTOR_CLR)) {
            return;
        }
        list.add(0, ACTOR_CLR);
        fireContentsChanged(this, 0, getSize());
    }

    void removeClearItem() {
        Actor actor = list.get(0);
        if (actor != null && actor.equals(ACTOR_CLR)) {
            list.remove(ACTOR_CLR);
            fireContentsChanged(this, 0, getSize());
        }
    }

    /**
     * Implement ListModel.getElementAt(int index).
     * @param index
     * @return 
     */
    @Override
    public Actor getElementAt(int index) {
        return list.get(index);
    }

    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public void addElement(Actor anActor) {
        super.addElement(anActor);
        fireContentsChanged(this, 0, getSize());
    }

    /**
     * Implement Observer to fire contents changed.
     * @param obs
     * @param obj
     */
    @Override
    public void update(Observable obs, Object obj) {
        update();
    }

    public void update() {
        initModel();
    }

    public void stopObserving() {
        if (mngr != null) {
            mngr.removeObserver(this);
        }
    }

    public void startObserving() {
        if (mngr != null) {
            mngr.addObserver(this);
        }
    }

    /**
     * Data has changed.
     * @param evt The lookup event.
     */
    @Override
    public void resultChanged(LookupEvent evt) {
        update();
    }

}

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
package au.com.trgtd.tr.view.someday.screen;

import au.com.trgtd.tr.view.ReprocessCookie;
import au.com.trgtd.tr.view.someday.FutureDeleteCookie;
import au.com.trgtd.tr.view.someday.FutureEditCookie;
import au.com.trgtd.tr.view.someday.TickleDateCookie;
import java.util.Date;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.future.Future;
import tr.model.thought.Thought;

/**
 * Node for a future item.
 *
 * @author Jeremy Moore
 */
public class SomedayNode extends AbstractNode
        implements FutureEditCookie, FutureDeleteCookie, ReprocessCookie, TickleDateCookie {
    
    public final Future future;
    
    /** Constructs a new instance. */
    public SomedayNode(Future future) {
        super(Children.LEAF, Lookups.singleton(future));
        this.future = future;
    }
    
    @Override
    public String getName() {
        return (future == null) ? "" : future.getDescription();
    }
    
    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == FutureEditCookie.class) return this;
        if (clazz == FutureDeleteCookie.class) return this;
////    if (clazz == FutureReprocessCookie.class) return this;
        if (clazz == ReprocessCookie.class) return this;
        if (clazz == TickleDateCookie.class) return this;
        return super.getCookie(clazz);
    }
    
    @Override
    public void editFuture() {
        if (future == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        SomedayTopComponent rtc = SomedayTopComponent.findInstance();
        if (rtc == null) return;
        
        rtc.edit();
    }
    
    @Override
    public void deleteFuture() {
        if (future == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        data.getFutureManager().remove(future);
    }
    
//    public void reprocessFuture() {
//        if (future == null) return;
//
//        Data data = (Data)DataLookup.instance().lookup(Data.class);
//        if (data == null) return;
//
//        data.getFutureManager().remove(future);
//
//        // add the new thought to the front of the thoughts
//        Thought thought = new Thought(data.getNextID());
//        thought.setDescription(future.getDescription());
//        thought.setTopic(future.getTopic());
//        thought.setNotes(future.getNotes());
//        data.getThoughtManager().insert(thought, 0);
//
//        try {
//            // fix hanging - probably due to a windowing issue.
//            Thread.sleep(400);
//        } catch (InterruptedException ex) {
//        }
//
//        // got to process thoughts screen
//        ProcessThoughtsAction pta = (ProcessThoughtsAction)SystemAction.get(ProcessThoughtsAction.class);
//        pta.performAction();
//    }

    @Override
    public boolean isRecurrent() {
        return false;
    }

    @Override
    public void reprocess() {
        if (future == null) {
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

//        ProcessThoughtsAction processThoughtsAction = null;
//        if (now) {
//            processThoughtsAction = (ProcessThoughtsAction)SystemAction.get(ProcessThoughtsAction.class);
//            if (processThoughtsAction != null) {
//                WindowUtils.closeWindows();
//            }
//        }

        // delete the item
        data.getFutureManager().remove(future);

        // add the new thought to the front of the thoughts
        Thought thought = new Thought(data.getNextID());
        thought.setDescription(future.getDescription());
        thought.setTopic(future.getTopic());
        thought.setNotes(future.getNotes());
        data.getThoughtManager().insert(thought, 0);

//        if (now && processThoughtsAction != null) {
//            // got to process thoughts screen
//            processThoughtsAction.performAction();
//        }
    }

    @Override
    public void changeTickleDate(Date date) {
        if (future != null) {
            future.setTickle(date);
        }
    }
    
}

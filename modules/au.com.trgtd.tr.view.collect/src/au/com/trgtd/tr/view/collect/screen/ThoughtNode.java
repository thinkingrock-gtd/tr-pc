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
package au.com.trgtd.tr.view.collect.screen;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.information.Information;
import tr.model.thought.Thought;
import tr.model.util.Manager;
import au.com.trgtd.tr.view.ProcessCookie;
import au.com.trgtd.tr.view.collect.DeleteThoughtCookie;
import au.com.trgtd.tr.view.collect.EditThoughtCookie;
import au.com.trgtd.tr.view.collect.dialog.ThoughtDialog;
import tr.model.future.Future;

/**
 * Node for a thought.
 *
 * @author Jeremy Moore
 */
public class ThoughtNode extends AbstractNode 
        implements EditThoughtCookie, 
                   DeleteThoughtCookie, 
                   ProcessCookie, 
                   ProcessToReferenceCookie,
                   ProcessToSomedayCookie {

    public final Manager<Thought> manager;
    public final Thought thought;

    public ThoughtNode(Manager<Thought> manager, Thought thought) {
        super(Children.LEAF, Lookups.singleton(thought));
        this.manager = manager;
        this.thought = thought;
    }

    @Override
    public String getName() {
        return thought.getDescription();
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == EditThoughtCookie.class 
         || clazz == DeleteThoughtCookie.class 
         || clazz == ProcessCookie.class 
         || clazz == ProcessToReferenceCookie.class
         || clazz == ProcessToSomedayCookie.class) {
            return this;
        }
        return super.getCookie(clazz);
    }

    @Override
    public void editThought() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            new ThoughtDialog(data).showModifyDialog(thought);
        }
    }

    @Override
    public void deleteThought() {
        manager.remove(thought);
    }

    @Override
    public void process() {
        manager.remove(thought);
        manager.insert(thought, 0);
    }

    @Override
    public void processToReference() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        Information reference = new Information(data.getNextID());
        reference.setDescription(thought.getDescription());
        reference.setNotes(thought.getNotes());
        reference.setTopic(thought.getTopic());
        data.getInformationManager().add(reference);
        thought.setProcessed(true);
    }

    @Override
    public void processToSomeday() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        Future someday = new Future(data.getNextID());
        someday.setThought(thought);
        someday.setDescription(thought.getDescription());
        someday.setNotes(thought.getNotes());
        someday.setTopic(thought.getTopic());
        someday.setTickle(null);
        data.getFutureManager().add(someday);
        thought.setProcessed(true);
    }

}

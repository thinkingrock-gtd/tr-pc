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
package au.com.trgtd.tr.view.reference.screen;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.information.Information;
import tr.model.thought.Thought;
import au.com.trgtd.tr.view.process.ProcessThoughtsAction;
import au.com.trgtd.tr.view.reference.ReferenceDeleteCookie;
import au.com.trgtd.tr.view.reference.ReferenceEditCookie;
import au.com.trgtd.tr.view.reference.ReferenceReprocessCookie;

/**
 * Node for a referenced information item.
 *
 * @author Jeremy Moore
 */
public class ReferenceNode extends AbstractNode
        implements ReferenceEditCookie, ReferenceDeleteCookie, ReferenceReprocessCookie {
    
    public final Information info;
    
    /** Constructs a new instance. */
    public ReferenceNode(Information info) {
        super(Children.LEAF, Lookups.singleton(info));
        this.info = info;
    }
    
    @Override
    public String getName() {
        return (info == null) ? "" : info.getDescription();
    }
    
    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ReferenceEditCookie.class) return this;
        if (clazz == ReferenceDeleteCookie.class) return this;
        if (clazz == ReferenceReprocessCookie.class) return this;
        return super.getCookie(clazz);
    }
    
    public void editReference() {
        if (info == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        ReferenceTopComponent rtc = ReferenceTopComponent.findInstance();
        if (rtc == null) return;
        
        rtc.edit();
    }
    
    public void deleteReference() {
        if (info == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        data.getInformationManager().remove(info);
    }
    
    public void reprocessReference() {
        if (info == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        data.getInformationManager().remove(info);
        
        // add the new thought to the front of the thoughts
        Thought thought = new Thought(data.getNextID());
        thought.setDescription(info.getDescription());
        thought.setTopic(info.getTopic());
        thought.setNotes(info.getNotes());
        data.getThoughtManager().insert(thought, 0);
        
        try {
            // fix hanging - probably due to a windowing issue.
            Thread.sleep(400);
        } catch (InterruptedException ex) {
        }
        
        // got to process thoughts screen
        ProcessThoughtsAction pta = SystemAction.get(ProcessThoughtsAction.class);
        pta.performAction();
    }
    
}

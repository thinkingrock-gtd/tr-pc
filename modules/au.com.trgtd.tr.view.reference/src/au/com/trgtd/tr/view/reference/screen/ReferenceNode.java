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
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        ReferenceTopComponent rtc = ReferenceTopComponent.findInstance();
        if (rtc == null) return;
        
        rtc.edit();
    }
    
    public void deleteReference() {
        if (info == null) return;
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        data.getInformationManager().remove(info);
    }
    
    public void reprocessReference() {
        if (info == null) return;
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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
        ProcessThoughtsAction pta = (ProcessThoughtsAction)SystemAction.get(ProcessThoughtsAction.class);
        pta.performAction();
    }
    
}

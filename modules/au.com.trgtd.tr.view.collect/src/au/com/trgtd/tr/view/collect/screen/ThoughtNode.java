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
 * Portions Copyright 2006-2014 Avente Pty Ltd. All Rights Reserved.
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
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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

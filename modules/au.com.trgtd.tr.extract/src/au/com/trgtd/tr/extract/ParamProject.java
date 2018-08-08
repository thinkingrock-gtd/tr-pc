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

package au.com.trgtd.tr.extract;

import java.util.Iterator;

import java.util.List;
import java.util.Vector;

import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.Param.Type;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.project.Project;

/**
 * Project parameter.
 *
 * @author Jeremy Moore
 */
public class ParamProject extends Param {
    
    private final boolean includeDone;
    private final String allText;
    
    /** Constructs a new instance. */
    public ParamProject(String id, String text, String allText, boolean includeDone) {
        super(Type.COMBOBOX, id, text);
        this.includeDone = includeDone;
        this.allText = allText;
    }
    
    /** Gets the selection items of projects. */
    public List<Item> getItems() {
        if (items == null) {
            initItems();
        }
        return items;
    }
    
    private void initItems() {
        items = new Vector<Item>();
        items.add(new Item(allText, "all"));            
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        // process top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
            initItems(i.next(), "");
        }
    }
    
    private void initItems(Project project, String indent) {        
        if (!includeDone && project.isDone()) {
            return;
        }
        
        StringBuffer display = new StringBuffer();
        display.append("<HTML>");
        if (project.isDone()) {
            display.append("<STRIKE>");
        }
        display.append(indent + project.getDescription());
        if (project.isDone()) {
            display.append("</STRIKE>");
        }
        display.append("</HTML>");
        
        items.add(new Item(display.toString(), "P" + project.getID()));
        
        // process sub-projects
        for (Iterator<Project> i = project.iterator(Project.class); i.hasNext();) {
//            Object child = iter.next();
//            if (child instanceof Project) {
//                initItems((Project)child, indent + "&nbsp;&nbsp;");
//            }
              initItems(i.next(), indent + "&nbsp;&nbsp;");
        }
    }
    
}

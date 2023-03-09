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
        items = new Vector<>();
        items.add(new Item(allText, "all"));            
        
        Data data = DataLookup.instance().lookup(Data.class);
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
        
        StringBuilder display = new StringBuilder();
        display.append("<HTML>");
        if (project.isDone()) {
            display.append("<STRIKE>");
        }
        display.append(indent).append(project.getDescription());
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

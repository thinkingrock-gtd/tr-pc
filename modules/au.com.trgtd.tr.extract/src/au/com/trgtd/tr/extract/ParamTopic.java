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

import java.util.Collections;

import java.util.List;
import java.util.Vector;

import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.Param.Type;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.topic.Topic;

/**
 * Topic parameter.
 *
 * @author Jeremy Moore
 */
public class ParamTopic extends Param {
    
    private final FormatType formatType;
    
    /** Constructs a new instance. */
    public ParamTopic(String id, String display, FormatType formatType) {
        super(Type.COMBOBOX, id, display);
        this.formatType = formatType;        
    }
    
    /** Gets the selection items of topics. */
    @Override
    public List<Item> getItems() {
        if (items == null) {
            initItems();
        }
        return items;
    }
    
    private void initItems() {
        items = new Vector<>();
        items.add(new Item(org.openide.util.NbBundle.getMessage(ParamTopic.class, "All"),"all"));
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        Vector<Topic> topics = data.getTopicManager().list();        

        Collections.sort(topics);
        
        for (Topic topic : topics) {
//          items.add(new Item(topic.getName(), topic.getName()));
            
            String name = topic.getName();
            if (formatType == FormatType.CSV) {
                name = "\"" + name.replace("\"", "\"\"") + "\"";
            } else {
                name = formatType.escape(name);
            }
                        
            items.add(new Item(topic.getName(), name));
        }
    }
    
}

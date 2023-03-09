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

import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.Param.Item;
import au.com.trgtd.tr.extract.Param.Type;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.context.Context;

/**
 * Context parameter.
 *
 * @author Jeremy Moore
 */
public class ParamContext extends Param {
    
    private final FormatType formatType;
    private final boolean addAtSign;

//  /** Constructs a new instance. */
//  public ParamContext(String id, String display, FormatType formatType) {
//      super(Type.COMBOBOX, id, display);
//      this.formatType = formatType;
//  }
        
    /** Constructs a new instance. */
    public ParamContext(String id, String display, FormatType formatType) {
        this(id, display, formatType, true);
    }

    /** Constructs a new instance. */
    public ParamContext(String id, String display, FormatType formatType, boolean addAtSign) {
        super(Type.COMBOBOX, id, display);
        this.formatType = formatType;
        this.addAtSign = addAtSign;
    }
        
    /** Gets the selection items of contexts. */
    @Override
    public List<Item> getItems() {
        if (items == null) {
            initItems();
        }
        return items;
    }
    
    private void initItems() {
        items = new Vector<>();
        items.add(new Item(NbBundle.getMessage(ParamContext.class, "All"),"all"));
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
                
        Vector<Context> contexts = data.getContextManager().list();        
        
        Collections.sort(contexts);
        
        for (Context context : contexts) {
            String name = context.getName().trim();            
//            if (!name.startsWith("@")) {
//                name = "@" + name;
//            }                   
            if (addAtSign && !name.startsWith("@")) {
                name = "@" + name;
            }                   
            if (formatType == FormatType.CSV) {
                name = "\"" + name.replace("\"", "\"\"") + "\"";
            } else {
                name = formatType.escape(name);
            }            
            items.add(new Item(context.getName(), name));            
        }
    }
    
}

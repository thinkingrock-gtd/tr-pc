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
        items = new Vector<Item>();
        items.add(new Item(NbBundle.getMessage(ParamContext.class, "All"),"all"));
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
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

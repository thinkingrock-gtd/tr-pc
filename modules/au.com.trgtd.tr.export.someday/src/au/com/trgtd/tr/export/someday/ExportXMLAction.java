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
package au.com.trgtd.tr.export.someday;

import au.com.trgtd.tr.export.ExportAction;
import au.com.trgtd.tr.resource.Icons;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export to an XML file action.
 *
 * @author Jeremy Moore
 */
public final class ExportXMLAction extends ExportAction {
    
    /** Constructs a new instance. */
    public ExportXMLAction() {
        super();
        setIcon(Icons.XML);
    }
    
    /** Save the current datastore as another file. */
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        try {
            new ExportXML().process(data);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    /** Get the report action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ExportXMLAction");
    }
    
//  /** Get the help context. */
//  public HelpCtx getHelpCtx() {
//      return HelpCtx.DEFAULT_HELP;
//  }
    
}

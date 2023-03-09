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
package au.com.trgtd.tr.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Action for printing views.
 *
 * @author Jeremy Moore
 */
public class PrintAction extends AbstractAction implements LookupListener, ContextAwareAction {
    
    private Lookup context;
    Lookup.Result lookupResult;
    
    public PrintAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    private PrintAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(PrintAction.class, "Print_Action")); //NOI18N
        this.context = context;
    }
    
    void init() {
        if (lookupResult != null) {
            return;
        }
        Lookup.Template lookupTemplate = new Lookup.Template(Print.class);
        lookupResult = context.lookup(lookupTemplate);
        lookupResult.addLookupListener(this);
        resultChanged(null);
    }
    
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Print printable = context.lookup(Print.class);
        if (printable != null) {
            printable.print();
        }
    }
    
    @Override
    public void resultChanged(LookupEvent ev) {
        setEnabled(lookupResult.allItems().size() != 0);
    }
    
    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new PrintAction(context);
    }
    
}

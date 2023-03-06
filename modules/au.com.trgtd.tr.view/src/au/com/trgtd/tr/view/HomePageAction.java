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

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.runtime.Open;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Action for opening the TR home page.
 *
 * @author Jeremy Moore
 */
public class HomePageAction extends AbstractAction {
    
    public HomePageAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    private HomePageAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(getClass(), "HomePage_Action")); //NOI18N
        putValue(SMALL_ICON, Icons.Web);
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Open.open(new URL("https://trgtd.com.au"));
        } catch (Exception ex) {
        }
    }
    
}

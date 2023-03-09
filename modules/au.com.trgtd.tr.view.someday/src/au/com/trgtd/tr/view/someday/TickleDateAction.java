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
package au.com.trgtd.tr.view.someday;

import au.com.trgtd.tr.resource.Resource;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.future.Future;

/**
 * Action to change tickle date.
 *
 * @author Jeremy Moore
 */
public class TickleDateAction extends CookieAction {
    
    private TickleDatePanel panel;
    
    @Override
    protected String iconResource() {
        return Resource.CalendarEdit;
    }    
    
    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_TickleDateAction");
    }
    
    /** Gets help topic. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Future.class };
    }
    
    @Override
    public int mode() {
        return MODE_ALL;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public void performAction(Node[] nodes) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        Frame frame = WindowManager.getDefault().getMainWindow();
        
        final JDialog jd = new JDialog(frame, getName(), true);
        
        if (panel == null) {
            panel = new TickleDatePanel();
        }
        
        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);
        
        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, (PropertyChangeEvent evt) -> {
            jd.dispose();
        });
        
        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();
        
        Rectangle r = Utilities.findCenterBounds(jd.getSize());        
        jd.setLocation(r.x, r.y);        
        jd.setVisible(true);
        
        Object value = op.getValue() ;
        if (value instanceof Integer integer) {
            if (integer.intValue() == JOptionPane.OK_OPTION) {
                changeTickleDate(nodes, panel.getDate());
            }
        }
    }
    
    private void changeTickleDate(Node[] nodes, Date date) {
        if (nodes == null) {
            return;
        }        
        for (Node node : nodes) {
            TickleDateCookie cookie = node.getCookie(TickleDateCookie.class);
            if (cookie != null) {
                cookie.changeTickleDate(date);
            }
        }
    }
    
}


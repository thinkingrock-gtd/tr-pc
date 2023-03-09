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
package au.com.trgtd.tr.view.contexts;

import au.com.trgtd.tr.resource.Resource;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.context.Context;
import tr.model.context.ContextChangeCookie;

/**
 * Action to change context.
 *
 * @author Jeremy Moore
 */
public class ContextChangeAction extends CookieAction {
    
    @Override
    protected String iconResource() {
        return Resource.ContextEdit;
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ChangeContextAction");
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { ContextChangeCookie.class };
    }
    
    @Override
    public int mode() {
        return MODE_ANY;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public void performAction(Node[] nodes) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        final JDialog jd = new JDialog((Frame)null, getName(), true);
        
        ContextsComboBox cb = new ContextsComboBox(new ContextsComboBoxModel(false));
        cb.setSelectedIndex(0);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel(NbBundle.getMessage(getClass(), "LBL_NewContext")));
        panel.add(cb);
        
        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);
        
        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, (PropertyChangeEvent evt) -> {
            jd.dispose();
        });
        
        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();
        
//      jd.setLocationByPlatform(true);
        Rectangle r = Utilities.findCenterBounds(jd.getSize());
        jd.setLocation(r.x, r.y);
        
        jd.setVisible(true);
        
        Object value = op.getValue() ;
        if (value instanceof Integer integer) {
            if (integer.intValue() == JOptionPane.OK_OPTION) {
                change((Context)cb.getSelectedItem(), nodes);
            }
        }
    }
    
    private void change(Context context, Node[] nodes) {
        if (context == null) return;
        if (nodes == null) return;
        
        for (Node node : nodes) {
            ContextChangeCookie cookie = node.getCookie(ContextChangeCookie.class);
            if (cookie != null) {
                cookie.setContext(context);
            }
        }
    }
    
    
}


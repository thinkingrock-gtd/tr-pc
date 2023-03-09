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
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;
import org.openide.windows.WindowManager;
import tr.model.Criteria;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.criteria.Value;

/**
 * Action to change criteria.
 *
 * @author Jeremy Moore
 */
public class CriteriaChangeAction extends CookieAction {

    public CriteriaChangeAction() {
        setIcon(Icons.CriteriaEdit);
    }
    
    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_CriteriaChangeAction");
    }
    
    /** Gets help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public Class[] cookieClasses() {
        return new Class[] { Action.class };
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

        CriteriaChangePanel panel = new CriteriaChangePanel();

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
                changeCriteria(nodes, panel.getCriteria(), panel.getValue());
            }
        }
    }

    private void changeCriteria(Node[] nodes, Criteria criteria, Value value) {
        if (nodes == null) {
            return;
        }
        for (Node node : nodes) {
            CriteriaChangeCookie cookie = node.getCookie(CriteriaChangeCookie.class);
            if (cookie != null) {
                cookie.changeCriteria(criteria, value);
            }
        }
    }
}


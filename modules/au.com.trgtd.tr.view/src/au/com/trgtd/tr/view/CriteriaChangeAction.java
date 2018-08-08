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

package au.com.trgtd.tr.view;

import au.com.trgtd.tr.resource.Icons;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_CriteriaChangeAction");
    }
    
    /** Gets help context. */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    public Class[] cookieClasses() {
        return new Class[] { Action.class };
    }
    
    public int mode() {
        return MODE_ALL;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public void performAction(Node[] nodes) {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;

        Frame frame = WindowManager.getDefault().getMainWindow();

        final JDialog jd = new JDialog(frame, getName(), true);

        CriteriaChangePanel panel = new CriteriaChangePanel();

        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);

        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                jd.dispose();
            }
        });

        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();

        Rectangle r = Utilities.findCenterBounds(jd.getSize());
        jd.setLocation(r.x, r.y);
        jd.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer) {
            if (((Integer)value).intValue() == JOptionPane.OK_OPTION) {
                changeCriteria(nodes, panel.getCriteria(), panel.getValue());
            }
        }
    }

    private void changeCriteria(Node[] nodes, Criteria criteria, Value value) {
        if (nodes == null) {
            return;
        }
        for (Node node : nodes) {
            CriteriaChangeCookie cookie = (CriteriaChangeCookie)node.getCookie(CriteriaChangeCookie.class);
            if (cookie != null) {
                cookie.changeCriteria(criteria, value);
            }
        }
    }
}


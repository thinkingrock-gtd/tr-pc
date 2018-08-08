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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.resource.Resource;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import tr.model.action.Action;

/**
 * Action to postpone action date.
 *
 * @author Jeremy Moore
 */
public class PostponeActionAction extends CookieAction {
    
    public enum Periods { DAYS, WEEKS, MONTHS, YEARS };

    private PostponeActionDialogPanel panel;
    
    @Override
    protected String iconResource() {
        return Resource.CalendarEdit;
    }    
    
    /** 
     * Gets the display name.
     * @return The display name. 
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_PostponeActionAction");
    }
    
    /** Gets help context.
     * @return the default help context.
     */
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
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        
        Frame frame = WindowManager.getDefault().getMainWindow();
        
        final JDialog jd = new JDialog(frame, getName(), true);
        
        if (panel == null) {
            panel = new PostponeActionDialogPanel();
        } else {
            panel.reset();
        }
        
        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);
        
        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                jd.dispose();
            }
        });
        op.setIcon(Icons.CalendarEdit24);
        
        jd.getContentPane().add(op, "Center" );
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jd.pack();
        
        Rectangle r = Utilities.findCenterBounds(jd.getSize());        
        jd.setLocation(r.x, r.y);        
        jd.setVisible(true);
        
        Object value = op.getValue() ;
        if (value instanceof Integer) {
            if (((Integer)value).intValue() == JOptionPane.OK_OPTION) {
                if (panel.isAdvance()) {
                    postponeAdvance(nodes, panel.getAdvanceNumber(), panel.getAdvancePeriods());
                } else if (panel.isSpecific()) {
                    postponeSpecific(nodes, panel.getSpecificDate());
                }
            }
        }
    }
    
    private void postponeAdvance(Node[] nodes, int n, Periods periods) {
        if (nodes == null || periods == null || n < 1 ) {
            return;
        }        
        for (Node node : nodes) {
            PostponeActionCookie cookie = (PostponeActionCookie)node.getCookie(PostponeActionCookie.class);
            if (cookie != null) {
                cookie.postponeAction(n, periods);
            }
        }
    }

    private void postponeSpecific(Node[] nodes, Date date) {
        for (Node node : nodes) {
            PostponeActionCookie cookie = (PostponeActionCookie)node.getCookie(PostponeActionCookie.class);
            if (cookie != null) {
                cookie.postponeAction(date);
            }
        }
    }
    
}


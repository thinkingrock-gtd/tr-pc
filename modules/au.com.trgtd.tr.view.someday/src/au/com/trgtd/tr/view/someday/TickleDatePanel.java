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
package au.com.trgtd.tr.view.someday;

import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.swing.date.field.DateField;

/**
 * Postpone action date panel.
 * 
 * @author Jeremy Moore
 */
public class TickleDatePanel extends JPanel {

    
    /** Creates new form TickleDatePanel */
    public TickleDatePanel() {
        initView();
    }

    private void initView() {
        dateLabel = new JLabel(getMsg("CTL_SelectTickleDate"));
        dateField = new DateField();
        setLayout(new MigLayout("insets 12px", "2[]4[]2", "2[]2"));
        add(dateLabel, "");
        add(dateField, "wrap");
    }

    public Date getDate() {
        return dateField.getDate();
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private JLabel dateLabel;
    private DateField dateField;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

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

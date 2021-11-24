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
package au.com.trgtd.tr.find;

import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Find panel.
 * 
 * @author Jeremy Moore
 */
public class FindPanel extends JPanel {
  
    private static final Class CLASS = FindPanel.class;
    
    /** Constructor. */
    public FindPanel() {
        initFields();
        initView();
    }
    
    private JLabel findTextLabel;
    private JTextField findTextField;    
    private JLabel doneLabel;
    private ButtonGroup doneGroup;
    private JRadioButton doneRadioAll;
    private JRadioButton doneRadioNotDone;
    private JRadioButton doneRadioDone;

    private String msg(String key) {
        return NbBundle.getMessage(CLASS, key);         
        
    }
    
    private void initFields() {
        findTextLabel = new JLabel(msg("find"));
        findTextField = new JTextField();
        findTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                findTextField.selectAll();
            }
        });
        
        doneLabel = new JLabel("");
        doneRadioAll = new JRadioButton(msg("find.items.all"));
        doneRadioNotDone = new JRadioButton(msg("find.items.not.done"));
        doneRadioDone = new JRadioButton(msg("find.items.done"));
        doneGroup = new ButtonGroup();
        doneGroup.add(doneRadioAll);
        doneGroup.add(doneRadioDone);
        doneGroup.add(doneRadioNotDone);     

        // default done state
        doneRadioAll.setSelected(true);
    }

    private void initView() {
        setLayout(new MigLayout("", "8[]3[grow]8", "8[]3[]8"));
        setPreferredSize(new Dimension(500,100));
        add(findTextLabel,    "align left");
        add(findTextField,    "align left, growx, wrap");
        add(doneLabel,        "align left");
        add(doneRadioAll,     "split 3, align left");
        add(doneRadioNotDone, "align left");
        add(doneRadioDone,    "align left, wrap");
    }

    public String getFindText() {
        return findTextField.getText();
    }

    public DoneState getDoneState() {
        if (doneRadioDone.isSelected()) {
            return DoneState.DONE;            
        } else if (doneRadioNotDone.isSelected()) {
            return DoneState.NOT_DONE;            
        } else {
            return DoneState.ANY;                        
        }
    }

}

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

import au.com.trgtd.tr.find.ui.FoundTopComponent;
import java.awt.Frame;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action for global find.
 *
 * @author Jeremy Moore
 */
@ActionID(id = "au.com.trgtd.tr.find.FindAction", category = "Edit")
@ActionRegistration(lazy = false, displayName = "#CTL_FindAction")
@ActionReferences(value = {
    @ActionReference(path = "Shortcuts", name = "D-F"),
    @ActionReference(path = "Menu/Edit", position = 9000),
    @ActionReference(path = "Toolbars/TR Help", name = "FindAction", position = 999100)})
@NbBundle.Messages({"CTL_FindAction=Find ..."})
public class FindAction extends CallableSystemAction {
    
    private final Lookup.Result result;
    private final FindPanel panel;
    
    public FindAction() {
        super();
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            setEnabled(!result.allInstances().isEmpty());
        });
        panel = new FindPanel();
    }

    @Override
    protected String iconResource() {
        return "au/com/trgtd/tr/find/Find.png";
    }
    
    /** Gets the action identifier. */
    public String getID() {
        return "au.com.trgtd.tr.find.FindAction";
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /** Gets the initial action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_FindAction");
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        Frame frame = WindowManager.getDefault().getMainWindow();

        final JDialog dialog = new JDialog(frame, getName(), true);

        JOptionPane op = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION, null, null, null);

        op.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY, (PropertyChangeEvent evt) -> {
            dialog.dispose();
        });

        dialog.getContentPane().add(op, "Center" );
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();

        Rectangle r = Utilities.findCenterBounds(dialog.getSize());
        dialog.setLocation(r.x, r.y);
        dialog.setVisible(true);

        Object value = op.getValue() ;
        if (value instanceof Integer integer) {
            if (integer.intValue() == JOptionPane.OK_OPTION) {
                find(panel.getFindText().trim(), panel.getDoneState());
            }
        }
    }

    private void find(String text, DoneState state) {
        if (text.length() == 0) {
            return;
        }

        FoundTopComponent ftc = FoundTopComponent.findInstance();
        if (ftc == null) {
            return;
        }

        FindProcess fp = new FindProcess(ftc.getFoundItems(), text, state);
        fp.start();

        
        String MODE = "bottomSlidingSide";
        
        Mode mode = WindowManager.getDefault().findMode(MODE);
        if (mode != null) {
            mode.dockInto(ftc);
        }        
        
        ftc.open();
        ftc.requestVisible();
        ftc.requestActive();
    }
    
}

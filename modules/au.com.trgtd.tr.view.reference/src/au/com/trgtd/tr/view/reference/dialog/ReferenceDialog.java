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
package au.com.trgtd.tr.view.reference.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.information.Information;

/**
 * Dialog for creating and modifying reference items.
 *
 * @author Jeremy Moore
 */
public class ReferenceDialog extends JDialog {
    
    private static enum Mode { CREATE, MODIFY };
    private Mode mode;
    private Data data;
    private ReferenceDialogPanel referencePanel;
    private Information info;
    private JButton okButton;
    private JButton cancelButton;
    
    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public ReferenceDialog(Data data) {
        super(WindowManager.getDefault().getMainWindow(), true);
        this.data = data;
        construct();
    }
    
    private void construct() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rp = getRootPane();
        rp.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        referencePanel = new ReferenceDialogPanel();
        
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener((ActionEvent e) -> {
            cancel();
        });
        
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok")); // NOI18N
        okButton.addActionListener((ActionEvent e) -> {
            ok();
        });
        
        JPanel buttonsPanel = createButtonsPanel();
        
        add(referencePanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        referencePanel.setPreferredSize(new Dimension(800, 600));

        pack();
        
        setLocation(WindowUtils.getPointForCentering(this));
    }

    private JPanel createButtonsPanel() {
	JPanel p = new JPanel(new MigLayout("nogrid, fillx, aligny 100%, gapy unrel"));
	p.add(okButton, "sgx 0, tag ok");
	p.add(cancelButton, "sgx 1, tag cancel, wrap");
	return p;
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create"));
            okButton.setText(NbBundle.getMessage(getClass(), "add"));
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify"));
            okButton.setText(NbBundle.getMessage(getClass(), "ok"));
        }
    }
    
    private boolean isValid(Information info) {
        if (info.getDescription().trim().length() == 0) {
            return false;
        }
        return true;
    }
    
    /* Handle OK. */
    private void ok() {
        info.setDescription(referencePanel.getDescr());
        info.setTopic(referencePanel.getTopic());
        info.setNotes(referencePanel.getNotes());
        if (!isValid(info)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getInformationManager().add(info);
            info = new Information(data.getNextID());
            referencePanel.setDescr(info.getDescription());
//          panel.setTopic(info.getTopic());
            referencePanel.setNotes(info.getNotes());
            referencePanel.focus();
        }
    }
    
    /* Handle Cancel. */
    private void cancel() {
        setVisible(false);
        dispose();
    }
    
    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }
    
    /** Shows the dialog in create mode. */
    public void showCreateDialog() {
        info = new Information(data.getNextID());
        referencePanel.setDescr(info.getDescription());
        referencePanel.setTopic(info.getTopic());
        referencePanel.setNotes(info.getNotes());
        setMode(Mode.CREATE);
        showDialog();
    }
    
    /**
     * Shows the modify dialog for the given context.
     * @param context The context to modify.
     */
    public void showModifyDialog(Information info) {
        if (info == null) {
            return;
        }
        this.info = info;
        referencePanel.setDescr(info.getDescription());
        referencePanel.setTopic(info.getTopic());
        referencePanel.setNotes(info.getNotes());
        setMode(Mode.MODIFY);
        showDialog();
    }
    
}

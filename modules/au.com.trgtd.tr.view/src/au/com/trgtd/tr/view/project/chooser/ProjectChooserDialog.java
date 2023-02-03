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
package au.com.trgtd.tr.view.project.chooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.project.Project;

/**
 * Custom dialog box to choose a project.
 */
public class ProjectChooserDialog extends JDialog implements ActionListener {

    private static final Class CLASS = ProjectChooserDialog.class;
    private static final String TITLE = NbBundle.getMessage(CLASS, "dialog.title");
    private static final boolean MODAL = true;

    private ProjectsPanel panel;
    private JButton okayButton;
    private JButton cancelButton;
    private boolean okay;
    private boolean cancel;
    private boolean currentOnly;
    
    private void construct() {
        // Escape key to cancel
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel = true;
            setVisible(false);
            dispose();
        };

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        okayButton = new JButton(NbBundle.getMessage(ProjectChooserDialog.class, "dialog.okay"));
        okayButton.addActionListener(this);

        getRootPane().setDefaultButton(okayButton);

        cancelButton = new JButton(NbBundle.getMessage(ProjectChooserDialog.class, "dialog.cancel"));
        cancelButton.addActionListener(this);

        JPanel buttonsPanel = new JPanel();
        if (Utilities.isWindows()) {
            buttonsPanel.add(okayButton);
            buttonsPanel.add(cancelButton);            
        } else {
            buttonsPanel.add(cancelButton);            
            buttonsPanel.add(okayButton);
        }

        panel = new ProjectsPanel(currentOnly);
        panel.addActionListener(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());        
        
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        JPanel fillBottomPanel = new JPanel();
        fillBottomPanel.setPreferredSize(new Dimension(0, 32));
        JPanel fillWestPanel = new JPanel();
        fillWestPanel.setPreferredSize(new Dimension(32, 0));
        JPanel fillEastPanel = new JPanel();
        fillEastPanel.setPreferredSize(new Dimension(32, 0));

        Container dialog = getContentPane();
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(fillBottomPanel, BorderLayout.SOUTH);
        dialog.add(fillWestPanel, BorderLayout.WEST);
        dialog.add(fillEastPanel, BorderLayout.EAST);

        pack();

        setResizable(true);
    }

    /**
     * Called when the okay button is pressed. Just sets a flag and hides the
     * dialog box.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okayButton || e.getSource() == panel) {
            okay = true;
        } else if (e.getSource() == cancelButton) {
            cancel = true;
        }
        setVisible(false);
        dispose();
    }

    /*
     * Constructs a new instance for the given window owner and default title.
     * @param owner The owning window.
     */
    public ProjectChooserDialog(Frame owner) {
        this(owner, TITLE, false);
    }

    public ProjectChooserDialog(Frame owner, boolean currentOnly) {
        this(owner, TITLE, currentOnly);
    }

    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning window.
     * @param title The dialog title.
     */
    public ProjectChooserDialog(Frame owner, String title) {
        this(owner, title, false);
    }

    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning window.
     * @param title The dialog title.
     */
    public ProjectChooserDialog(Frame owner, String title, boolean currentOnly) {
        super(owner, title, MODAL);
        this.currentOnly = currentOnly;
        construct();        
        initialise();
    }

    /*
     * Constructs a new instance for the given dialog owner and default title.
     * @param owner The owning dialog.
     */
    public ProjectChooserDialog(Dialog owner) {
        this(owner, TITLE, false);
    }

    /*
     * Constructs a new instance for the given dialog owner and default title.
     * @param owner The owning dialog.
     */
    public ProjectChooserDialog(Dialog owner, boolean currentOnly) {
        this(owner, TITLE, currentOnly);
    }


    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning dialog.
     * @param title The dialog title.
     */
    public ProjectChooserDialog(Dialog owner, String title) {
        this(owner, title, false);
    }

    /*
     * Constructs a new instance for the given window owner.
     * @param owner The owning dialog.
     * @param title The dialog title.
     */
    public ProjectChooserDialog(Dialog owner, String title, boolean currentOnly) {
        super(owner, title, MODAL);
        this.currentOnly = currentOnly;
        construct();
        initialise();
    }

    private void initialise() {
//      setDefaultCloseOperation(HIDE_ON_CLOSE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Selects a project.
     */
    public Project select(Project project, Component component) {
        okay = false;
        cancel = false;
        if (component != null) {
            setLocationRelativeTo(component);
        }

        panel.refreshModel();
        panel.setSelected(project);

        setVisible(true);
        
        if (!okay) {
            return null;
        }
  
        return panel.getSelected();
    }

    /**
     * Determines whether the cancel button was activated.
     * @return true if the cancel button was activated.
     */
    public boolean cancelled() {
        return cancel;
    }

    /**
     * Determines whether the okay button was activated.
     * @return true if the okay button was activated.
     */
    public boolean okayed() {
        return okay;
    }
}

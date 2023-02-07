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
package au.com.trgtd.tr.view.notes;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.cal.CalendarPrefs;
import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.swing.editorpane.URLDialog;
import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.*;
import org.openide.awt.Toolbar;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Component for editable text with hyper-link code recognition and rendering.
 *
 * @author Jeremy Moore
 */
public final class NotesEditorPanel extends JPanel {
    
    /** Constructs a new instance. */
    public NotesEditorPanel() {
        notesField = new NotesEditorField();
        notesField.addKeyListener(new NotesKeyListener());
        dateAction = new DateAction();
        editAction = new EditAction();
        fileAction = new FileAction();
        timeAction = new TimeAction();
        urlAction = new URLAction();
        linkerActionMap = new HashMap<>();
        linkersLookup = Lookup.getDefault().lookupResult(NotesLinker.class);
        linkers = linkersLookup.allInstances();
        toolbar = getToolbar();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        add(toolbar, GUIPrefs.getBorderLayoutButtonsPosition());
        add(notesField, BorderLayout.CENTER);
        setEditable(true);
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        notesField.addFocusListener(l);
    }

    @Override
    public synchronized void removeFocusListener(FocusListener l) {
        notesField.removeFocusListener(l);
    }

    private class NotesKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isControlDown() && e.isShiftDown()) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    editAction.actionPerformed(null);
                    e.consume();
                    return;
                }
            }
            if (e.isControlDown() && e.isAltDown()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D: {
                        dateAction.actionPerformed(null);
                        e.consume();
                        return;
                    }
                    case KeyEvent.VK_T: {
                        timeAction.actionPerformed(null);
                        e.consume();
                        return;
                    }
                    case KeyEvent.VK_U: {
                        urlAction.actionPerformed(null);
                        e.consume();
                        return;
                    }
                    case KeyEvent.VK_F: {
                        fileAction.actionPerformed(null);
                        e.consume();
                        return;
                    }
//                    case KeyEvent.VK_E: {
//                        editAction.actionPerformed(null);
//                        e.consume();
//                        return;
//                    }
                }
            }
            for (NotesLinker linker : linkers) {
                if ((e.getModifiersEx() & linker.getShortcutMask()) == linker.getShortcutMask()) {
                    if (e.getKeyCode() == linker.getShortcutKey()) {
                        e.consume();
                        LinkerAction action = linkerActionMap.get(linker.getScheme());
                        if (action != null) {
                            action.actionPerformed(null);
                        }
                        return;
                    }
                }
            }
        }
    }



    private JToolBar getToolbar() {
        JToolBar tb = new JToolBar();
        tb.setUI((new Toolbar()).getUI());
        tb.setFloatable(false);
        tb.setOrientation(GUIPrefs.getToolBarOrientation());
        tb.add(editAction);
        tb.add(urlAction);
        tb.add(fileAction);
        tb.add(dateAction);
        tb.add(timeAction);
        for (NotesLinker linker : linkers) {
            LinkerAction action = new LinkerAction(linker);
            tb.add(action);
            linkerActionMap.put(linker.getScheme(), action);
        }
        for (Component component : tb.getComponents()) {
            if (component instanceof AbstractButton) {
                component.setPreferredSize(Constants.TOOLBAR_BUTTON_SIZE);
                component.setMinimumSize(Constants.TOOLBAR_BUTTON_SIZE);
                component.setMaximumSize(Constants.TOOLBAR_BUTTON_SIZE);
                component.setSize(Constants.TOOLBAR_BUTTON_SIZE);
                component.setFocusable(false);
            }
        }
        return tb;
    }

    @Override
    public boolean requestFocusInWindow() {
        super.requestFocusInWindow(true);
        return notesField.requestFocusInWindow();
    }

    private final class LinkerAction extends AbstractAction {
        private final NotesLinker linker;
        public LinkerAction(NotesLinker linker) {
            this.linker = linker;
            putValue(SMALL_ICON, linker.getIcon());
            putValue(SHORT_DESCRIPTION, linker.getToolTip());
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            final NotesLink link = linker.getUserLink();
            if (link == null) {
                return;
            }

            requestFocusInWindow();

            SwingUtilities.invokeLater(() -> {
                notesField.insert(link.encode());
            });
        }
    }

    private final class EditAction extends AbstractAction {
        public EditAction() {
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/notes/IconEdit16.gif")));
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(NotesEditorPanel.class, "EditButtonToolTip"));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (notesField.isTextMode()) {
                notesField.setHTMLMode();
            } else {
                notesField.setTextMode();
            }
            requestFocusInWindow();
        }
    }

    private final class URLAction extends AbstractAction {
        public URLAction() {
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/notes/IconURL16.gif")));
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(NotesEditorPanel.class, "URLButtonToolTip"));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            URLDialog dialog = new URLDialog(null, notesField, NbBundle.getMessage(NotesEditorPanel.class, "insert.web.url"));
            int choice = dialog.showDialog();
            if (choice == JOptionPane.CANCEL_OPTION) {
                requestFocusInWindow();
                return;
            }
            String urlText = dialog.getURL().trim();
            String urlDesc = dialog.getLinkText().trim();
            
            // default protocol to HTTPS if there isn't one
            if (!urlText.contains(":")) {
                urlText = "https://" + urlText;
            }
            
            // Changed to use URI to allow for other links e.g. notes://... 
            try {
                URI uri = new URI(urlText);
            } catch (URISyntaxException ex) {
                LOG.severe(ex.getMessage());
                requestFocusInWindow();
                return;
            }
            
            String link = (urlDesc.length() == 0) ? "[" + urlText + "]" : "[" + urlText + "|" + urlDesc + "]";
            notesField.insert(link);
            requestFocusInWindow();
        }
    }

    private final class FileAction extends AbstractAction {
        public FileAction() {
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/notes/IconFile16.gif")));
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(NotesEditorPanel.class, "FileButtonToolTip"));

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            
            // Set the last used path if possible.
            String lastPath = NotesPrefs.getLastFilePath();
            if (lastPath != null) {
                File file = new File(lastPath);
                if (file.isFile() || file.isDirectory()) {
                    chooser.setCurrentDirectory(file);
                }
            }
            
            chooser.setDialogTitle(NbBundle.getMessage(NotesEditorPanel.class, "insert.file.url"));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            
            int option = chooser.showOpenDialog(null);
            if (option != JFileChooser.APPROVE_OPTION) {
                requestFocusInWindow();
                return;
            }
            
            // Validate file: make sure the file exists.
            String path = chooser.getSelectedFile().getPath();
            File file = new File(path);
            if (!file.exists()) {
                LOG.severe("File does not exist.");
                requestFocusInWindow();
                return;
            }
            
            // determine file URL
            String url;
            try {
                url = file.toURL().toExternalForm();
            } catch (MalformedURLException ex) {
                LOG.severe(ex.getMessage());
                requestFocusInWindow();
                return;
            }
            
            notesField.insert("[" + url + "]");
            
            NotesPrefs.setLastFilePath(file.getPath());
            
            requestFocusInWindow();
        }
    }

    private final class DateAction extends AbstractAction {
        public DateAction() {
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/notes/IconDate16.png")));
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(NotesEditorPanel.class, "DateButtonToolTip"));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            notesField.insert(DatesPrefs.formatLong(new Date()));
            requestFocusInWindow();
        }
    }

    private final class TimeAction extends AbstractAction {
        public TimeAction() {
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/au/com/trgtd/tr/view/notes/IconTime16.png")));
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(NotesEditorPanel.class, "TimeButtonToolTip"));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
//          notesField.insert(DateFormat.getTimeInstance().format(new Date()));
            DateFormat df;
            if (CalendarPrefs.isHourDisplay24hr()) {
                df = new SimpleDateFormat("HH:mm");
            } else {
                df = new SimpleDateFormat("hh:mm a");
            }
            notesField.insert(df.format(new Date()));
            requestFocusInWindow();
        }
    }

    public void reset(String notes) {
        notesField.setTextMode();
        notesField.setText(notes);
        notesField.scrollToTop();
    }

    /**
     * Enables or disables the field.
     * @param enabled If true the field is enabled otherwise it is disabled.
     */
    @Override
    public void setEnabled(boolean enabled) {
        notesField.setEnabled(enabled);
        for (Component button : toolbar.getComponents()) {
            button.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    public void setEditable(boolean editable) {
        notesField.setEditable(editable);
    }

    /** Sets text mode. */
    public void setTextMode() {
        notesField.setTextMode();
    }

    /** Sets HTML mode. */
    public void setHTMLMode() {
        notesField.setHTMLMode();
    }

    /** 
     * Determines whether the notes are in text edit mode or HTML view mode.
     * @return true if in text edit mode, false if in HTML view mode
     */
    public boolean isEditable() {
        return notesField.isTextMode();
    }

    /**
     * Gets the text.
     * @return the text.
     */
    public String getText() {
        return notesField.getText();
    }

    /** 
     * Sets the text. 
     * @param text The text.
     */
    public void setText(String text) {
        notesField.setText(text);
    }

    private static final Logger LOG = Logger.getLogger("tr.view.NotesEditorPanel");
    
    private final Lookup.Result<NotesLinker> linkersLookup;
    private final Collection<? extends NotesLinker> linkers;
    private final Map<String, LinkerAction> linkerActionMap;
    private final NotesEditorField notesField;
    private final Action editAction;
    private final Action urlAction;
    private final Action fileAction;
    private final Action dateAction;
    private final Action timeAction;
    private final JToolBar toolbar;
}

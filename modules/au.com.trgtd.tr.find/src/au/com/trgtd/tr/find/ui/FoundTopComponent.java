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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.prefs.ui.utils.StayOpen;
import java.awt.BorderLayout;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.text.DefaultEditorKit;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node.Property;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays found items.
 */
@TopComponent.Description(
        preferredID = "FoundTopComponent",
        iconBase = "au/com/trgtd/tr/find/Find.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)

public class FoundTopComponent extends TopComponent implements StayOpen, ExplorerManager.Provider {

    private static FoundTopComponent instance;
    private static final String ICON_PATH = "au/com/trgtd/tr/find/Find.png";
    private static final String PREFERRED_ID = "FoundTopComponent";

    private final ExplorerManager manager;
    private final Lookup lookup;
    private final FoundOutlineView outlineView;
    private final FoundItems foundItems;

    private FoundTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(FoundTopComponent.class, "CTL_FoundTopComponent"));
        setToolTipText(NbBundle.getMessage(FoundTopComponent.class, "HINT_FoundTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        foundItems = new FoundItems();
        FoundNodeRoot rootNode = new FoundNodeRoot(foundItems);

        Property[] properties = new Property[] {
            new PropertyDoneDate(null),
            new PropertyCreatedDate(null),
            new PropertyPathIcon(null),
            new PropertyPathString(null)
        };
        
        outlineView = new FoundOutlineView(rootNode, properties);
        manager = outlineView.getExplorerManager();

        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, false));

        lookup = ExplorerUtils.createLookup(manager, map);
        associateLookup(lookup);

        setLayout(new BorderLayout());
        add(outlineView, BorderLayout.CENTER);
    }

    public FoundItems getFoundItems() {
        return foundItems;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized FoundTopComponent getDefault() {
        if (instance == null) {
            instance = new FoundTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the FoundTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FoundTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FoundTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FoundTopComponent foundTopComponent) {
            return foundTopComponent;
        }
        Logger.getLogger(FoundTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

//    @Override
//    public void componentOpened() {
//    }
//
//    @Override
//    public void componentClosed() {
//    }

//    /** replaces this in object stream */
//    @Override
//    public Object writeReplace() {
//        return new ResolvableHelper();
//    }

//    @Override
//    protected String preferredID() {
//        return PREFERRED_ID;
//    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }


//    final static class ResolvableHelper implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public Object readResolve() {
//            return FoundTopComponent.getDefault();
//        }
//    }
}

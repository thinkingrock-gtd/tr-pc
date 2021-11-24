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
package au.com.trgtd.tr.view.someday.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.Window;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.logging.Logger;
import org.openide.util.*;
import org.openide.windows.TopComponent;

/**
 * Top component for future item.
 *
 * @author Jeremy Moore
 */
public final class SomedayTopComponent extends Window implements LookupListener {
    
    private static final Logger LOG = Logger.getLogger("tr.view.future");
    
    private static final String PREFERRED_ID = "FutureTopComponent";
    
    private static SomedayTopComponent instance;
    
//    private FuturePanel panel;
    private SomedayPanel panel;
    private Lookup.Result result;
    
    private SomedayTopComponent() {
        setName(NbBundle.getMessage(SomedayTopComponent.class, "CTL_FutureTopComponent"));
        setToolTipText(NbBundle.getMessage(SomedayTopComponent.class, "TTT_FutureTopComponent"));
        setIcon(Icons.SomedayMaybe.getImage());            
        initComponents();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    @Override
    protected void componentOpened() {
        super.componentOpened();
        
        if (panel == null) {
            removeAll();
            panel = new SomedayPanel();
            add(panel, BorderLayout.CENTER);
        }
        
        panel.initModel(null);
        
        SomedaysTopComponent rtc = SomedaysTopComponent.findInstance();
        
        result = rtc.getLookup().lookup(new Lookup.Template(SomedayNode.class));
        result.addLookupListener(this);
        result.allInstances();
    }
    
    @Override
    protected void componentClosed() {
        super.componentClosed();
        result.removeLookupListener(this);
        result = null;
    }
    
    @Override
    protected void componentDeactivated() {
        panel.deactivate();
        super.componentDeactivated();
    }
    
    @Override
    public synchronized void resultChanged(LookupEvent lookupEvent) {
        LOG.info("Starting");
        
        if (panel == null) return;
        
        Collection collection = result.allInstances();
        if (collection.isEmpty()) {
            panel.initModel(null);
            
            LOG.info("null");
        } else {
            SomedayNode node = (SomedayNode)collection.iterator().next();
            panel.initModel(node.future);
            
            LOG.info("initialising model");
        }
    }
    
    /** Start editing if possible. */
    public void edit() {
        if (panel == null) return;
        
        requestActive();
        
        panel.edit();
    }
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized SomedayTopComponent getDefault() {
        if (instance == null) {
            instance = new SomedayTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the SomedayTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SomedayTopComponent findInstance() {
//        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
//        if (win == null) {
//            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find Future component. It will not be located properly in the window system.");
//            return getDefault();
//        }
//        if (win instanceof SomedayTopComponent) {
//            return (SomedayTopComponent)win;
//        }
//        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    @Override
    public int getPersistenceType() {
//        return TopComponent.PERSISTENCE_ALWAYS;
        return TopComponent.PERSISTENCE_NEVER;
    }
    
    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.future");
    }
    
//    /** replaces this in object stream */
//    public Object writeReplace() {
//        return new ResolvableHelper();
//    }
//
//    final static class ResolvableHelper implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public Object readResolve() {
//            return SomedayTopComponent.getDefault();
//        }
//    }
    
}

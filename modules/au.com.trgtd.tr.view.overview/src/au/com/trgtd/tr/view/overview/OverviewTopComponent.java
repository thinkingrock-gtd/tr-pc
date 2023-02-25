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
package au.com.trgtd.tr.view.overview;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.resource.Resource;
import java.awt.BorderLayout;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.apache.batik.swing.JSVGCanvas;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.Window;
import au.com.trgtd.tr.view.overview.Overview.Screen;
import au.com.trgtd.tr.view.overview.spi.OverviewSVGProvider;
import java.util.logging.Level;

/**
 * Top component which displays the overview screen.
 */
public final class OverviewTopComponent extends Window {
    
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger("tr.view.overview.OverviewTopComponent");
    
    public static final String PREFERRED_ID = "OverviewTopComponent";
    
    private static OverviewTopComponent instance;

    private final Lookup.Result<OverviewSVGProvider> lookup
            = Lookup.getDefault().lookupResult(OverviewSVGProvider.class);
    private final Overview overview;    
    private final JSVGCanvas canvas;
    
    private OverviewTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(OverviewTopComponent.class, "CTL_OverviewTopComponent"));
        setToolTipText(NbBundle.getMessage(OverviewTopComponent.class, "HINT_OverviewTopComponent"));        
        setIcon(Icons.Overview.getImage());            
        overview = new Overview();
        canvas = overview.getSVGCanvas();
        add(canvas, BorderLayout.CENTER);
//      canvas.setURI(Resource.getOverviewURL().toExternalForm());
        canvas.setURI(getURL().toExternalForm());
        canvas.setSize(1,1); // stops image being positioned at lower right
    }

    private URL getURL() {

        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();

        LOG.log(Level.INFO, "Language: {0}", language);
        LOG.log(Level.INFO, "Country: {0}", country);

        OverviewSVGProvider generalLanguageProvider = null;

        for (OverviewSVGProvider provider : lookup.allInstances()) {

            LOG.log(Level.INFO, "Provider, language: {0}, country: {1}", new Object[]{provider.getLanguage(), provider.getCountry()});

            if (Utils.equal(provider.getLanguage(), language)) {
                if (Utils.equal(provider.getCountry(), country)) {
                    LOG.info("Provider found.");
                    return provider.getURL();
                }
                if (provider.getCountry() == null) {
                    generalLanguageProvider = provider;
                }
            }
        }

        if (generalLanguageProvider != null) {
            LOG.info("General language provider found.");
            return generalLanguageProvider.getURL();
        }

        LOG.info("Using default overview SVG.");
        return Resource.getOverviewURL();
    }

    @Override
    public void addNotify() {        
        super.addNotify();        
        addListeners();
    }
            
    @Override
    public void removeNotify() {
        super.removeNotify();        
        removeListeners();        
    }

    private Observer observer;    
    
    private void addListeners() {        
        observer = (Observable observable, final Object screen) -> {
            if (screen instanceof Overview.Screen scr) {
                SwingUtilities.invokeLater(() -> {
                    switchScreen(scr);
                });
            }
        };
        overview.addObserver(observer);        
    }

    private void removeListeners() {        
        overview.removeObserver(observer);        
    }
    
    private void switchScreen(Screen screen) {        
        screen.action.actionPerformed(null);
    }

    /**
     * Gets the default instance. Do not use directly: reserved for *.settings
     * files only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized OverviewTopComponent getDefault() {
        if (instance == null) {
            instance = new OverviewTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the OverviewTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized OverviewTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentActivated() {
        refresh();
    }

    @Override
    public void componentOpened() {
        refresh();
    }

    /* Refreshes the window. */
    public void refresh() {
        canvas.setSize(1,1); // stops image being positioned at lower right
    }

    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.overview");
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}

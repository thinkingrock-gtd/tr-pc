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
package au.com.trgtd.tr.view.reference;

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import au.com.trgtd.tr.view.reference.screen.ReferenceTopComponent;
import au.com.trgtd.tr.view.reference.screen.ReferencesTopComponent;

/**
 * Action for the review references feature.
 *
 * @author Jeremy Moore
 */
//public class ReferencesAction extends AbstractAction implements InitialAction {
public class ReferencesAction extends CallableSystemAction implements InitialAction {

    private static final Logger LOG = Logger.getLogger("tr.view.reference");

    public ReferencesAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
    }

    /** Gets the display name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ReferenceAction");
    }

    @Override
    protected String iconResource() {
        return Resource.References;
    }

    private void enableDisable() {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    @Override
    public String getID() {
        return "reference";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(() -> {
            WindowUtils.closeWindows();

            TopComponent tcReferences = ReferencesTopComponent.findInstance();
            TopComponent tcReference = ReferenceTopComponent.findInstance();

            Mode mode = WindowManager.getDefault().findMode("references");
            if (mode == null) {
                LOG.severe("References mode was not found.");
            } else {
                mode.dockInto(tcReferences);
            }

            mode = WindowManager.getDefault().findMode("reference");
            if (mode == null) {
                LOG.severe("Reference mode was not found.");
            } else {
                mode.dockInto(tcReference);
            }

            tcReferences.open();
            tcReference.open();

            tcReferences.requestActive();
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.reference");
    }
}

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
import au.com.trgtd.tr.view.someday.screen.SomedayTopComponent;
import au.com.trgtd.tr.view.someday.screen.SomedaysTopComponent;

/**
 * Action for the review future items feature.
 *
 * @author Jeremy Moore
 */
//public class FuturesAction extends AbstractAction implements InitialAction {
public class FuturesAction extends CallableSystemAction implements InitialAction {

    private static final Logger LOG = Logger.getLogger("tr.view.future");

    public FuturesAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_FutureAction");
    }

    @Override
    protected String iconResource() {
        return Resource.SomedayMaybes;
    }

    private void enableDisable() {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    @Override
    public String getID() {
        return "future";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(() -> {
            WindowUtils.closeWindows();

            TopComponent tcFutures = SomedaysTopComponent.findInstance();
            TopComponent tcFuture = SomedayTopComponent.findInstance();

            Mode mode = WindowManager.getDefault().findMode("futures");
            if (mode == null) {
                LOG.severe("Futures mode was not found.");
            } else {
                mode.dockInto(tcFutures);
            }

            mode = WindowManager.getDefault().findMode("future");
            if (mode == null) {
                LOG.severe("Future mode was not found.");
            } else {
                mode.dockInto(tcFuture);
            }

            tcFutures.open();
            tcFuture.open();

            tcFutures.requestActive();
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.future");
    }
}

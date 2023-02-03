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
package au.com.trgtd.tr.sync.iphone;

import java.awt.EventQueue;
import java.awt.Frame;
import java.util.prefs.PreferenceChangeEvent;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

public final class SyncAction extends CallableSystemAction {

    /** Constructor. */
    public SyncAction() {
        super();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
        SyncPrefs.addListener((PreferenceChangeEvent evt) -> {
            enableDisable();
        });
        enableDisable();
    }

    @Override
    public void performAction() {        
        if (SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_WIFI) {
            if (SyncManager.getDefault().canStartSync()) {
                Frame frame = WindowManager.getDefault().getMainWindow();
                (new SyncDialog(frame)).setVisible(true);
            }
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(this.getClass(), "CTL_SyncAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        return "au/com/trgtd/tr/sync/iphone/iphone.png";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    private void enableDisable() {
        EventQueue.invokeLater(() -> {
            Data data = DataLookup.instance().lookup(Data.class);
            setEnabled(data != null && SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_WIFI);
        });
    }

}

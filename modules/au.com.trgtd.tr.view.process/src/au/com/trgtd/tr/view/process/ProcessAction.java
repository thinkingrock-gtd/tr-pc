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
package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.resource.Resource;
import org.openide.nodes.Node;
import org.openide.util.*;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to process a thought.
 *
 * @author Jeremy Moore
 */
public class ProcessAction extends CookieAction {

    public ProcessAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
    }

    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    @Override
    protected String iconResource() {
        return Resource.Process;
    }

    /** Gets the display name.
     * @return  */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProcessAction");
    }

    /** Gets help context.
     * @return  */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public Class[] cookieClasses() {
        return new Class[]{ProcessCookie.class};
    }

    @Override
    public int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public void performAction(Node[] nodes) {
        ProcessCookie cookie = (ProcessCookie) nodes[0].getCookie(ProcessCookie.class);
        if (cookie != null) {
            cookie.process();
        }
    }
}


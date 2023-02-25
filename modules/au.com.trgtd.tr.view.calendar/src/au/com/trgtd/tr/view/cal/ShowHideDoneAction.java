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
package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.resource.Icons;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToggleButton;
import org.openide.util.NbBundle;

/**
 * Action for show/hide done actions.
 *
 * Jeremy Moore
 */
public class ShowHideDoneAction extends AbstractAction {

    private final CalModel model;
    private final DateCtlr dateCtlr;

    public ShowHideDoneAction(CalModel model, DateCtlr dateCtlr) {
        this.model = model;
        this.dateCtlr = dateCtlr;
        initValues();
    }

    private void initValues() {
        putValue(Action.NAME, NbBundle.getMessage(getClass(), "show.hide.done"));
        putValue(SMALL_ICON, Icons.ShowDone);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JToggleButton tb) {
            model.setShowDone(tb.isSelected());
        } else {
            model.setShowDone(!model.isShowDone());
        }
        dateCtlr.fireChange();
    }

}

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
package au.com.trgtd.tr.data.recent;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.openide.awt.Actions;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 * Recent data files menu.
 *
 * @author Jeremy Moore
 */
public class RecentDataFileMenu extends Actions.MenuItem {

    private JComponent[] presenters;

    /** Constructs a new instance. */
    public RecentDataFileMenu() {
        super(new FakeAction(), false);
    }

    @Override
    public JComponent[] getMenuPresenters() {
        if (presenters == null) {
            initialize();
        }
        return presenters;
    }

    @Override
    public JComponent[] synchMenuPresenters(JComponent[] items) {
        return getMenuPresenters();
    }

    /** Initialize menu presenters. */
    void initialize() {
        List<String> paths = Prefs.getPaths();
        if (paths == null || paths.isEmpty()) {
            JMenuItem menuItem = new JMenuItem(NbBundle.getMessage(RecentDataFileMenu.class, "open.recent"));
            menuItem.setEnabled(false);
            presenters = new JComponent[]{menuItem};
        } else {
            JMenu menu = new JMenu(NbBundle.getMessage(RecentDataFileMenu.class, "open.recent"));
            for (String path : paths) {
                menu.add(new RecentDataFileOpenAction(path));
            }
            menu.add(new JSeparator());
            menu.add(SystemAction.get(RecentDataFilesClearAction.class));
            presenters = new JComponent[]{menu};
        }
    }

    private static class FakeAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }
}

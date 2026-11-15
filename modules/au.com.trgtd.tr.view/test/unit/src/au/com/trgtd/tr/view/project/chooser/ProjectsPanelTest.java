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
package au.com.trgtd.tr.view.project.chooser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tr.model.Data;
import tr.model.DataLookup;

public class ProjectsPanelTest {

    @Before
    public void removeData() {
        DataLookup.instance().setData(null);
    }

    @After
    public void cleanUpData() {
        DataLookup.instance().setData(null);
    }

    @Test
    public void bothTreesAreCreatedWhenNoDataIsAvailable() {
        ProjectsPanel panel = new ProjectsPanel();

        List<JTree> trees = find(panel, JTree.class);

        assertEquals("expected a projects tree and a future projects tree", 2, trees.size());
        for (JTree tree : trees) {
            assertNotNull("tree should have a root node", tree.getModel().getRoot());
        }
    }

    /** The panel may be created before a data file has been opened. */
    @Test
    public void refreshModelSucceedsWhenDataBecomesAvailableAfterPanelCreation() {
        ProjectsPanel panel = new ProjectsPanel();

        DataLookup.instance().setData(new Data());

        panel.refreshModel();
    }

    /** Finds the descendants of the given container that are of the given type. */
    private static <T extends Component> List<T> find(Container container, Class<T> type) {
        List<T> found = new ArrayList<>();
        collect(container, type, found);
        return found;
    }

    private static <T extends Component> void collect(Container container, Class<T> type, List<T> found) {
        for (Component component : container.getComponents()) {
            if (type.isInstance(component)) {
                found.add(type.cast(component));
            } else if (component instanceof Container child) {
                collect(child, type, found);
            }
        }
    }
}

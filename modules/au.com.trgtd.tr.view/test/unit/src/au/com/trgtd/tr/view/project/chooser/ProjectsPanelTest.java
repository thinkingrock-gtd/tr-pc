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
import java.util.Enumeration;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.project.Project;

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

    @Test
    public void filterFieldReducesBothTreesToTheMatchingProjects() {
        Data data = new Data();
        data.getRootProjects().add(project(data, "needle project"));
        data.getRootProjects().add(project(data, "other project"));
        data.getRootFutures().add(project(data, "needle future"));
        data.getRootFutures().add(project(data, "other future"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();

        filterField(panel).setText("needle");

        List<JTree> trees = find(panel, JTree.class);
        assertEquals(""
                + "Projects\n"
                + "  needle project\n",
                render(trees.get(0)));
        assertEquals(""
                + "Future Projects\n"
                + "  needle future\n",
                render(trees.get(1)));
    }

    private static Project project(Data data, String description) {
        Project project = new Project(data);
        project.setDescription(description);
        return project;
    }

    private static JTextField filterField(ProjectsPanel panel) {
        List<JTextField> fields = find(panel, JTextField.class);
        assertEquals("expected exactly one filter field", 1, fields.size());
        return fields.get(0);
    }

    /** Renders the nodes of the given tree as indented lines. */
    private static String render(JTree tree) {
        StringBuilder sb = new StringBuilder();
        render((DefaultMutableTreeNode) tree.getModel().getRoot(), 0, sb);
        return sb.toString();
    }

    private static void render(DefaultMutableTreeNode node, int depth, StringBuilder sb) {
        sb.append("  ".repeat(depth)).append(node.getUserObject()).append('\n');
        for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
            render((DefaultMutableTreeNode) e.nextElement(), depth + 1, sb);
        }
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

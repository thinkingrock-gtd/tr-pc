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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Utilities;
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

    @Test
    public void keepsTheSelectedProjectWhenItStillMatchesTheFilter() {
        Data data = new Data();
        Project needle = project(data, "needle project");
        data.getRootProjects().add(needle);
        data.getRootProjects().add(project(data, "other project"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        panel.setSelected(needle);

        filterField(panel).setText("needle");

        assertSame(needle, panel.getSelected());
    }

    @Test
    public void clearsTheSelectionWhenTheSelectedProjectIsFilteredOut() {
        Data data = new Data();
        Project needle = project(data, "needle project");
        data.getRootProjects().add(needle);
        data.getRootProjects().add(project(data, "other project"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        panel.setSelected(needle);

        filterField(panel).setText("other");

        assertNull("Accept must not return an invisible project", panel.getSelected());
    }

    @Test
    public void expandsTheTreeWhileFilteringSoThatDeepMatchesAreVisible() {
        Data data = new Data();
        Project alpha = project(data, "alpha");
        data.getRootProjects().add(alpha);
        Project nested = project(data, "nested");
        alpha.add(nested);
        nested.add(project(data, "needle"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();

        filterField(panel).setText("needle");

        JTree tree = find(panel, JTree.class).get(0);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        DefaultMutableTreeNode alphaNode = (DefaultMutableTreeNode) root.getFirstChild();
        DefaultMutableTreeNode nestedNode = (DefaultMutableTreeNode) alphaNode.getFirstChild();
        assertTrue("root expanded", tree.isExpanded(new TreePath(root.getPath())));
        assertTrue("alpha expanded", tree.isExpanded(new TreePath(alphaNode.getPath())));
        assertTrue("nested expanded", tree.isExpanded(new TreePath(nestedNode.getPath())));
    }

    @Test
    public void enterInTheFilterFieldSelectsTheFirstMatchWithoutAccepting() {
        Data data = new Data();
        Project alpha = project(data, "alpha");
        data.getRootProjects().add(alpha);
        alpha.add(project(data, "needle project"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        List<ActionEvent> accepted = new ArrayList<>();
        panel.addActionListener(accepted::add);

        JTextField field = filterField(panel);
        field.setText("needle");
        field.postActionEvent();

        assertNotNull("a match should be selected", panel.getSelected());
        assertEquals("needle project", panel.getSelected().getDescription());
        assertTrue("Enter must not accept the dialog", accepted.isEmpty());
    }

    @Test
    public void resetFilterClearsTheFilterAndShowsAllProjectsAgain() {
        Data data = new Data();
        data.getRootProjects().add(project(data, "needle project"));
        data.getRootProjects().add(project(data, "other project"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        filterField(panel).setText("needle");

        panel.resetFilter();

        assertEquals("", filterField(panel).getText());
        assertEquals(""
                + "Projects\n"
                + "  needle project\n"
                + "  other project\n",
                render(find(panel, JTree.class).get(0)));
    }

    @Test
    public void enterSelectsTheNarrowedChildWhenSeveralTermsAreUsed() {
        Data data = new Data();
        Project haus = project(data, "Haus");
        data.getRootProjects().add(haus);
        haus.add(project(data, "Dach"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();

        JTextField field = filterField(panel);
        field.setText("haus dach");
        field.postActionEvent();

        assertNotNull("a match should be selected", panel.getSelected());
        assertEquals("Dach", panel.getSelected().getDescription());
    }

    @Test
    public void aMnemonicAndAShortcutLeadBackToTheFilterField() {
        ProjectsPanel panel = new ProjectsPanel();
        JTextField field = filterField(panel);

        JLabel filterLabel = null;
        for (JLabel label : find(panel, JLabel.class)) {
            if (label.getLabelFor() == field) {
                filterLabel = label;
            }
        }
        assertNotNull("expected a label tied to the filter field", filterLabel);
        assertEquals("mnemonic F", KeyEvent.VK_F, filterLabel.getDisplayedMnemonic());

        int modifier = Utilities.isMac() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, modifier);
        Object actionKey = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).get(stroke);
        assertNotNull("the shortcut must be bound in the focused window", actionKey);
        assertNotNull("the bound action must be registered", panel.getActionMap().get(actionKey));
    }

    @Test
    public void theExtendedSearchIsOffByDefaultAndWidensTheSearchWhenTurnedOn() {
        Data data = new Data();
        Project alpha = project(data, "alpha");
        alpha.setNotes("mentions the needle");
        data.getRootProjects().add(alpha);
        data.getRootProjects().add(project(data, "beta"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        JTree tree = find(panel, JTree.class).get(0);

        JCheckBox extended = extendedSearchBox(panel);
        assertFalse("the extended search must be off by default", extended.isSelected());

        filterField(panel).setText("needle");
        assertEquals("the name alone does not match", "Projects\n", render(tree));

        extended.setSelected(true);

        assertEquals(""
                + "Projects\n"
                + "  alpha\n",
                render(tree));
    }

    @Test
    public void theExtendedSearchBoxHasAMnemonic() {
        ProjectsPanel panel = new ProjectsPanel();

        assertEquals("mnemonic E", KeyEvent.VK_E, extendedSearchBox(panel).getMnemonic());
    }

    /** The search mode is meant to outlast a single use of the chooser. */
    @Test
    public void resetFilterKeepsTheExtendedSearchSetting() {
        Data data = new Data();
        data.getRootProjects().add(project(data, "alpha"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        JCheckBox extended = extendedSearchBox(panel);
        extended.setSelected(true);
        filterField(panel).setText("alpha");

        panel.resetFilter();

        assertEquals("", filterField(panel).getText());
        assertTrue("the extended search stays on", extended.isSelected());
    }

    private static JCheckBox extendedSearchBox(ProjectsPanel panel) {
        List<JCheckBox> boxes = find(panel, JCheckBox.class);
        assertEquals("expected exactly one check box", 1, boxes.size());
        return boxes.get(0);
    }

    @Test
    public void aLongNameDeepInTheTreeCountsAsMuchAsOneAtTheTop() {
        int deep = widthForSizing(3);
        int top = widthForSizing(0);

        assertTrue("a collapsed branch must count too, deep=" + deep + " top=" + top,
                deep >= top);
    }

    @Test
    public void measuringLeavesTheTreeCollapsedAgain() {
        Data data = new Data();
        Project parent = project(data, "parent");
        data.getRootProjects().add(parent);
        parent.add(project(data, "child"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();
        JTree tree = find(panel, JTree.class).get(0);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) root.getFirstChild();

        panel.expandAllForSizing();
        assertTrue("expanded for measuring", tree.isExpanded(new TreePath(parentNode.getPath())));

        panel.restoreExpansion();

        assertFalse("collapsed again afterwards",
                tree.isExpanded(new TreePath(parentNode.getPath())));
    }

    /** The width the panel needs when the given nesting depth holds a long name. */
    private static int widthForSizing(int depth) {
        Data data = new Data();
        Project parent = data.getRootProjects();
        for (int i = 0; i < depth; i++) {
            Project level = project(data, "level " + i);
            parent.add(level);
            parent = level;
        }
        parent.add(project(data,
                "A very very long project name that is certainly wider than four hundred pixels"));
        DataLookup.instance().setData(data);
        ProjectsPanel panel = new ProjectsPanel();
        panel.refreshModel();

        panel.expandAllForSizing();
        int width = panel.getPreferredSize().width;
        panel.restoreExpansion();
        return width;
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

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

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.Test;
import tr.model.IDGenerator;
import tr.model.project.Project;

public class ProjectNodeBuilderTest {

    private final IDGenerator ids = new IDGenerator() {
        private int next = 1;

        @Override
        public int getNextID() {
            return next++;
        }
    };

    @Test
    public void addsAllNotDoneSubProjectsRecursivelyWhenNotFiltering() {
        Project root = project("root");
        Project alpha = project("alpha");
        root.add(alpha);
        alpha.add(project("alpha child"));
        root.add(project("beta"));

        assertEquals(""
                + "Projects\n"
                + "  alpha\n"
                + "    alpha child\n"
                + "  beta\n",
                build(root, null));
    }

    @Test
    public void skipsDoneSubProjects() {
        Project root = project("root");
        Project done = project("done project");
        root.add(done);
        done.setDone(true);
        root.add(project("open project"));

        assertEquals(""
                + "Projects\n"
                + "  open project\n",
                build(root, null));
    }

    @Test
    public void keepsAncestorsOfAMatchingSubProjectAndDropsTheRest() {
        Project root = project("root");
        Project alpha = project("alpha");
        root.add(alpha);
        Project nested = project("nested");
        alpha.add(nested);
        nested.add(project("needle"));
        root.add(project("beta"));

        assertEquals(""
                + "Projects\n"
                + "  alpha\n"
                + "    nested\n"
                + "      needle\n",
                build(root, "needle"));
    }

    @Test
    public void keepsTheWholeSubtreeOfAMatchingProject() {
        Project root = project("root");
        Project alpha = project("alpha");
        root.add(alpha);
        alpha.add(project("one"));
        Project two = project("two");
        alpha.add(two);
        two.add(project("deep"));
        root.add(project("beta"));

        assertEquals(""
                + "Projects\n"
                + "  alpha\n"
                + "    one\n"
                + "    two\n"
                + "      deep\n",
                build(root, "alpha"));
    }

    @Test
    public void matchesIgnoringCase() {
        Project root = project("root");
        root.add(project("Gartenhaus"));
        root.add(project("beta"));

        assertEquals(""
                + "Projects\n"
                + "  Gartenhaus\n",
                build(root, "garten"));
    }

    @Test
    public void ignoresSurroundingWhitespaceOfTheFilterText() {
        Project root = project("root");
        root.add(project("needle"));
        root.add(project("beta"));

        assertEquals(""
                + "Projects\n"
                + "  needle\n",
                build(root, "  needle  "));
    }

    @Test
    public void treatsAProjectWithoutDescriptionAsNoMatch() {
        Project root = project("root");
        root.add(project(null));
        root.add(project("needle"));

        assertEquals(""
                + "Projects\n"
                + "  needle\n",
                build(root, "needle"));
    }

    @Test
    public void keepsOnlyTheRootNodeWhenNothingMatches() {
        Project root = project("root");
        Project alpha = project("alpha");
        root.add(alpha);
        alpha.add(project("alpha child"));

        assertEquals("Projects\n", build(root, "no such project"));
    }

    @Test
    public void narrowsTheChildrenOfAMatchingProjectWithAFurtherTerm() {
        Project root = project("root");
        Project haus = project("Haus");
        root.add(haus);
        haus.add(project("Dach"));
        haus.add(project("Keller"));
        root.add(project("Garten"));

        assertEquals(""
                + "Projects\n"
                + "  Haus\n"
                + "    Dach\n",
                build(root, "haus dach"));
    }

    @Test
    public void isNotFilteringWithoutFilterText() {
        assertFalse("null is no filter", new ProjectNodeBuilder(null).isFiltering());
        assertFalse("empty is no filter", new ProjectNodeBuilder("").isFiltering());
        assertFalse("blank is no filter", new ProjectNodeBuilder("   ").isFiltering());
    }

    private Project project(String description) {
        Project project = new Project(ids);
        project.setDescription(description);
        return project;
    }

    /** Builds the nodes for the given root project and renders them as indented lines. */
    private static String build(Project rootProject, String filterText) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Projects");
        new ProjectNodeBuilder(filterText).addChildNodes(rootNode, rootProject);
        StringBuilder sb = new StringBuilder();
        render(rootNode, 0, sb);
        return sb.toString();
    }

    private static void render(DefaultMutableTreeNode node, int depth, StringBuilder sb) {
        sb.append("  ".repeat(depth)).append(node.getUserObject()).append('\n');
        for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
            render((DefaultMutableTreeNode) e.nextElement(), depth + 1, sb);
        }
    }
}

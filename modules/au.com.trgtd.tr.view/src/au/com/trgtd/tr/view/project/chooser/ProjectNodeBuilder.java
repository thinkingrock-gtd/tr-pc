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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import tr.model.project.Project;

/**
 * Builds the project nodes of the project chooser trees.
 */
class ProjectNodeBuilder {

    private final List<String> terms;

    /**
     * Constructs a new instance.
     * @param filterText The filter text, or null or blank for no filtering.
     * Whitespace separates the text into the distinct terms that all have to be
     * found.
     */
    ProjectNodeBuilder(String filterText) {
        this.terms = filterText == null ? List.of()
                : Arrays.stream(filterText.toLowerCase().split("\\s+"))
                        .filter(term -> !term.isEmpty())
                        .distinct()
                        .toList();
    }

    /**
     * Determines whether a filter text is in effect.
     * @return true if the nodes are reduced to the matching projects.
     */
    boolean isFiltering() {
        return !terms.isEmpty();
    }

    /**
     * Adds a node for each sub-project of the given project that is not done
     * and, if a filter text is in effect, either matches the filter itself or
     * is an ancestor of a matching project.
     * @param parentNode The node to add the sub-project nodes to.
     * @param project The project whose sub-projects are to be added.
     */
    void addChildNodes(DefaultMutableTreeNode parentNode, Project project) {
        addChildNodes(parentNode, project, "");
    }

    /**
     * @param ancestorText The names of the projects on the way down to the
     * given project, which count towards the terms of the filter as well.
     */
    private void addChildNodes(DefaultMutableTreeNode parentNode, Project project, String ancestorText) {
        for (Project subProject : project.getChildren(Project.class)) {
            if (subProject.isDone()) {
                continue;
            }
            String pathText = append(ancestorText, subProject);
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subProject);
            addChildNodes(subNode, subProject, pathText);
            if (matches(pathText) || subNode.getChildCount() > 0) {
                parentNode.add(subNode);
            }
        }
    }

    /** Appends the lower cased name of the given project to the given text. */
    private static String append(String ancestorText, Project project) {
        String description = project.getDescription();
        if (description == null) {
            return ancestorText;
        }
        if (ancestorText.isEmpty()) {
            return description.toLowerCase();
        }
        return ancestorText + ' ' + description.toLowerCase();
    }

    /**
     * Determines whether every term of the filter occurs in the given names of
     * a project and its ancestors. Any text matches when no filter is in
     * effect, because then nothing is filtered out.
     */
    private boolean matches(String pathText) {
        for (String term : terms) {
            if (!pathText.contains(term)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first node of the given tree that matches the filter, as
     * opposed to being present only to show the hierarchy of a match.
     * @param rootNode The root node of a tree built by this instance.
     * @return The node, or null if the tree holds no match.
     */
    DefaultMutableTreeNode findFirstMatch(DefaultMutableTreeNode rootNode) {
        return findFirstMatch(rootNode, "");
    }

    private DefaultMutableTreeNode findFirstMatch(DefaultMutableTreeNode node, String ancestorText) {
        String pathText = ancestorText;
        if (node.getUserObject() instanceof Project project) {
            pathText = append(ancestorText, project);
            if (matches(pathText)) {
                return node;
            }
        }
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            DefaultMutableTreeNode match = findFirstMatch((DefaultMutableTreeNode)e.nextElement(), pathText);
            if (match != null) {
                return match;
            }
        }
        return null;
    }
}

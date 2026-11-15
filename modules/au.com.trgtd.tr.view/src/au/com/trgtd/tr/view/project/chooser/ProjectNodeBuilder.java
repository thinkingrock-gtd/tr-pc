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
    private final ProjectSearchScope scope;

    /**
     * Constructs a new instance.
     * @param filterText The filter text, or null or blank for no filtering.
     * Whitespace separates the text into the distinct terms that all have to be
     * found.
     */
    ProjectNodeBuilder(String filterText) {
        this(filterText, ProjectSearchScope.NAME);
    }

    /**
     * Constructs a new instance.
     * @param filterText The filter text, or null or blank for no filtering.
     * Whitespace separates the text into the distinct terms that all have to be
     * found.
     * @param scope The project fields to search.
     */
    ProjectNodeBuilder(String filterText, ProjectSearchScope scope) {
        this.scope = scope;
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
        addChildNodes(parentNode, project, new boolean[terms.size()]);
    }

    /**
     * @param satisfied The terms that the projects on the way down to the given
     * project have already satisfied.
     */
    private void addChildNodes(DefaultMutableTreeNode parentNode, Project project, boolean[] satisfied) {
        for (Project subProject : project.getChildren(Project.class)) {
            if (subProject.isDone()) {
                continue;
            }
            boolean[] subSatisfied = satisfy(satisfied, subProject);
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subProject);
            addChildNodes(subNode, subProject, subSatisfied);
            if (allSatisfied(subSatisfied) || subNode.getChildCount() > 0) {
                parentNode.add(subNode);
            }
        }
    }

    /**
     * Adds the terms that the given project satisfies to the ones its ancestors
     * satisfy already. A term thus counts as found as soon as any project on
     * the way down holds it, which is what keeps the sub-projects of a match
     * reachable.
     * @return A new array, leaving the given one untouched.
     */
    private boolean[] satisfy(boolean[] ancestorsSatisfied, Project project) {
        boolean[] satisfied = ancestorsSatisfied.clone();
        String text = null;
        for (int i = 0; i < satisfied.length; i++) {
            if (satisfied[i]) {
                continue;
            }
            if (text == null) {
                text = scope.textOf(project);
            }
            satisfied[i] = text.contains(terms.get(i));
        }
        return satisfied;
    }

    /**
     * Determines whether all terms are satisfied. Holds trivially when no
     * filter is in effect, because then nothing is filtered out.
     */
    private static boolean allSatisfied(boolean[] satisfied) {
        for (boolean termSatisfied : satisfied) {
            if (!termSatisfied) {
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
        return findFirstMatch(rootNode, new boolean[terms.size()]);
    }

    private DefaultMutableTreeNode findFirstMatch(DefaultMutableTreeNode node, boolean[] ancestorsSatisfied) {
        boolean[] satisfied = ancestorsSatisfied;
        if (node.getUserObject() instanceof Project project) {
            satisfied = satisfy(ancestorsSatisfied, project);
            if (allSatisfied(satisfied)) {
                return node;
            }
        }
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            DefaultMutableTreeNode match = findFirstMatch((DefaultMutableTreeNode)e.nextElement(), satisfied);
            if (match != null) {
                return match;
            }
        }
        return null;
    }
}

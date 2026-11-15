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

import tr.model.project.Project;

/**
 * The project fields that the filter of the project chooser searches.
 */
enum ProjectSearchScope {

    /** Searches the project name. */
    NAME {
        @Override
        String textOf(Project project) {
            return join(project.getDescription());
        }
    },

    /** Searches the project name and all of its other text fields. */
    EXTENDED {
        @Override
        String textOf(Project project) {
            return join(project.getDescription());
        }
    };

    /**
     * Gets the text of the given project that this scope searches.
     * @return The text, lower cased, and empty if the project has none.
     */
    abstract String textOf(Project project);

    /**
     * Joins the given texts, skipping the ones that are null or empty. The
     * separator keeps a term from matching across two fields.
     */
    static String join(String... texts) {
        StringBuilder sb = new StringBuilder();
        for (String text : texts) {
            if (text == null || text.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(text);
        }
        return sb.toString().toLowerCase();
    }
}

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
package tr.model.project;

import org.openide.util.NbBundle;

/**
 * Automatic action sequencing type.
 *
 * @author Jeremy Moore
 */
public enum Sequencing {
    INTO_SUBPROJECTS,
    OVER_SUBPROJECTS;
    public String toString() {
        switch (this) {
            case INTO_SUBPROJECTS: return NbBundle.getMessage(Sequencing.class, "SEQUENCING_INTO");
            case OVER_SUBPROJECTS: return NbBundle.getMessage(Sequencing.class, "SEQUENCING_OVER");
            default: return "";
        }
    }
}

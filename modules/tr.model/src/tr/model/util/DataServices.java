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
package tr.model.util;

import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 *
 * @author Jeremy Moore
 */
public class DataServices {

    public interface Predicate<T> {
        public boolean apply(T object);
    }

    /**
     * Determines whether there exists an action where the predicate is true.
     *
     * @param p The action predicate.
     * @return true if such an action is found.
     */
    public boolean exists(Predicate<Action> p) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return false;
        }
        return exists(p, data.getRootProjects()) || exists(p, data.getRootActions());
    }

    private boolean exists(Predicate<Action> p, Project project) {
        return project.getChildren().stream().anyMatch((item) -> (exists(p, item)));
    }

    private boolean exists(Predicate<Action> p, Action action) {
        return p.apply(action);
    }

    private boolean exists(Predicate<Action> p, Item item) {
        if (item.isAction()) {
            return exists(p, (Action)item);
        } else if (item.isProject()) {
            return exists(p, (Project)item);
        } else {
            return false;
        }        
    }

}

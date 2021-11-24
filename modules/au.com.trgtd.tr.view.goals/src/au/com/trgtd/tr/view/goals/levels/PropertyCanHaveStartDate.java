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
package au.com.trgtd.tr.view.goals.levels;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

class PropertyCanHaveStartDate extends PropertySupport.ReadOnly<Boolean> {

    private final static Class CLASS = PropertyCanHaveStartDate.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.can.have.start");
    private final LevelCtrl level;

    public PropertyCanHaveStartDate(LevelCtrl level) {
        super("can.have.start", Boolean.class, NAME, NAME);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.level = level;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return level.isGoalsHaveStartDate();
    }

}

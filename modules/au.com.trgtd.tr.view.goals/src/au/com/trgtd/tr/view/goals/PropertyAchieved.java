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
package au.com.trgtd.tr.view.goals;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalCtrl;

public class PropertyAchieved extends PropertySupport.ReadOnly<Boolean> {

    public final static String NAME = "achieved";
    public final static String DISPLAY_NAME = NbBundle.getMessage(PropertyAchieved.class, "achieved");
    public final static String SHORT_DESCR = "";

    private final GoalCtrl goalCtrl;

    public PropertyAchieved(GoalCtrl goalCtrl) {
        super(NAME, Boolean.class, DISPLAY_NAME, SHORT_DESCR);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.goalCtrl = goalCtrl;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return goalCtrl.getAchievedDate() != null;
    }

}

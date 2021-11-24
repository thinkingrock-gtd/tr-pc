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
package au.com.trgtd.tr.view.criteria.screen;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.criteria.Value;

class PropertyMapped extends PropertySupport.ReadOnly<String> {

    private final static Class CLASS = PropertyMapped.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.mapped.value");
    private final Value value;

    public PropertyMapped(Value value) {
        super("mapped.value", String.class, NAME, NAME);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.value = value;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        if (null == value) {
            return "";
        }
        Integer mv = value.getMappedValue();
        if (null == mv) {
            return "";
        }
        return mv.toString();
    }

}

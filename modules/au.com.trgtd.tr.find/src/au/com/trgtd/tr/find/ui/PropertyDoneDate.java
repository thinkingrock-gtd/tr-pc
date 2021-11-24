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
package au.com.trgtd.tr.find.ui;

import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import org.openide.nodes.PropertySupport;

public class PropertyDoneDate extends PropertySupport.ReadOnly<Date> {

    private static final String LABEL = "Done Date";
    private final FoundItem item;

    public PropertyDoneDate(FoundItem item) {
        super("done.date", Date.class, LABEL, "Done");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);
        
        PropertyEditorManager.registerEditor(Date.class, DatePropertyEditor.class);
    }

    @Override
    public Date getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getDoneDate();
    }

}

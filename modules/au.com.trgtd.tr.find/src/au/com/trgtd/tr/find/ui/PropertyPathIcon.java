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
import javax.swing.ImageIcon;
import org.openide.nodes.PropertySupport;

public class PropertyPathIcon extends PropertySupport.ReadOnly<ImageIcon> {

    private static final String LABEL = "";
    private final FoundItem item;

    public PropertyPathIcon(FoundItem item) {
        super("path.icon", ImageIcon.class, LABEL, "Path Icon");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);

        PropertyEditorManager.registerEditor(ImageIcon.class, IconPropertyEditor.class);
    }

    @Override
    public ImageIcon getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getPathIcon();
    }

}

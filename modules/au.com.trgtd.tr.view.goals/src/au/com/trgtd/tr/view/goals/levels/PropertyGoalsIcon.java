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

import au.com.trgtd.tr.view.ui.IconPropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import javax.swing.ImageIcon;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

public class PropertyGoalsIcon extends PropertySupport.ReadOnly<ImageIcon> {

    private static final String LABEL = NbBundle.getMessage(PropertyGoalsIcon.class, "property.goals.icon");
    private final LevelCtrl levelCtrl;

    public PropertyGoalsIcon(LevelCtrl levelCtrl) {
        super("goals.icon", ImageIcon.class, LABEL, "");
        this.levelCtrl = levelCtrl;
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        PropertyEditorManager.registerEditor(ImageIcon.class, IconPropertyEditor.class);
    }

    @Override
    public ImageIcon getValue() throws IllegalAccessException, InvocationTargetException {
        return (levelCtrl == null || levelCtrl.getGoalsIcon() == null)
                ? null : levelCtrl.getGoalsIcon().icon;
    }
    
}

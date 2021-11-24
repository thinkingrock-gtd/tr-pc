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

import au.com.trgtd.tr.view.ui.ColoredString;
import au.com.trgtd.tr.view.ui.ColoredStringImpl;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.topic.Topic;

public class PropertyTopic extends PropertySupport.ReadOnly<ColoredString> {

    public final static String NAME = "topic";
    public final static String DISPLAY_NAME = NbBundle.getMessage(PropertyTopic.class, "topic");
    public final static String SHORT_DESCR = "";
    public final static Color TRANSPARENT = new Color(0,0,0,0);

    private final GoalCtrl goalCtrl;
    
    public PropertyTopic(GoalCtrl goalCtrl) {
        super(NAME, ColoredString.class, DISPLAY_NAME, SHORT_DESCR);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.goalCtrl = goalCtrl;
    }

    @Override
    public ColoredString getValue() throws IllegalAccessException, InvocationTargetException {
        Topic topic = goalCtrl.getTopic();
        String name = (topic == null ? "" : topic.getName());
        Color bg = (topic == null || topic.equals(Topic.getDefault()) ? TRANSPARENT : topic.getBackground());
        Color fg = (topic == null || topic.equals(Topic.getDefault()) ? Color.black : topic.getForeground());
        return new ColoredStringImpl(name, bg, fg);
    }
 
}

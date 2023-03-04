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
package au.com.trgtd.tr.view.topics;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Font;
import javax.swing.ComboBoxModel;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.swing.TRComboBox;
import tr.model.topic.Topic;

/**
 * Combo box for topics.
 *
 * @author Jeremy Moore
 */
public class TopicsComboBox extends TRComboBox<Topic> {

    /**
     * Constructs a new default instance.
     */
    public TopicsComboBox() {
        this(new TopicsComboBoxModel());
    }

    /**
     * Constructs a new instance for the given data model.
     * @param model The topics combo box model.
     */
    public TopicsComboBox(ComboBoxModel<Topic> model) {
        super(model);
        setRenderer(new TopicsListCellRenderer(this.getRenderer()));
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
        setToolTipText(NbBundle.getMessage(getClass(), "TTT_TopicsComboBox"));
    }

}

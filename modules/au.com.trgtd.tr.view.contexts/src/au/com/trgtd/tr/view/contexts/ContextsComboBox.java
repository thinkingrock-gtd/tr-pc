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
package au.com.trgtd.tr.view.contexts;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.Font;
import javax.swing.ComboBoxModel;
import org.openide.util.NbBundle;
import tr.model.context.Context;

/**
 * Combo box for contexts.
 *
 * @author Jeremy Moore
 */
public class ContextsComboBox extends TRComboBox<Context> {

    /**
     * Constructs a new instance with the default model.
     */
    public ContextsComboBox() {
        this(new ContextsComboBoxModel());
    }

    /**
     * Constructs a new instance for the given data model.
     * @param model The contexts combo box model.
     */
    public ContextsComboBox(ComboBoxModel<Context> model) {
        super(model);
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
        setToolTipText(NbBundle.getMessage(getClass(), "TTT_ContextsComboBox"));
    }

}

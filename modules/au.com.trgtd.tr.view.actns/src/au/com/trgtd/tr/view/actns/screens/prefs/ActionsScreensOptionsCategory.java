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
package au.com.trgtd.tr.view.actns.screens.prefs;

import javax.swing.Icon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public final class ActionsScreensOptionsCategory extends OptionsCategory {
    
    @Override
    public Icon getIcon() {
//      return new ImageIcon(Utilities.loadImage("au/com/trgtd/tr/view/actns/screens/prefs/ActionsScreensOptions32.png"));
        return ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/actns/screens/prefs/ActionsScreensOptions32.png", false);
    }
    
    public String getCategoryName() {
        return NbBundle.getMessage(ActionsScreensOptionsCategory.class, "OptionsCategory_Name");
    }
    
    public String getTitle() {
        return NbBundle.getMessage(ActionsScreensOptionsCategory.class, "OptionsCategory_Title");
    }
    
    public OptionsPanelController create() {
        return new ActionsScreensOptionsPanelController();
    }
    
}

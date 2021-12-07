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
package au.com.trgtd.tr.view.goals.chooser;

import java.awt.Image;
import javax.swing.AbstractButton;
import org.openide.util.ImageUtilities;
import tr.model.goals.ctrl.GoalCtrl;

public class ChooserRootNode extends ChooserGoalNode {

    private final Image icon;

    public ChooserRootNode(GoalCtrl goalCtrl, AbstractButton selectButton, boolean showAll) {
        super(goalCtrl, selectButton, showAll);
        icon = ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/goals/resource/Circle.png", false).getImage();
    }

    @Override
    public Image getIcon(int type) {
        return icon;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return icon;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<i>" + escapeHTML(getName()) + "</i>";
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

}

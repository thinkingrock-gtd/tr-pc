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

//import au.com.trgtd.tr.view.DeleteAction;
//import au.com.trgtd.tr.view.DeleteCookie;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.goals.levels.dialog.LevelDialog;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.Action;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.actions.SystemAction;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

/**
 * Level node.
 *
 * @author Jeremy Moore
 */
//class LevelNode extends AbstractNode implements DeleteCookie, EditCookie, PropertyChangeListener {
class LevelNode extends AbstractNode implements EditCookie, PropertyChangeListener {

    public final LevelCtrl levelCtrl;

    public LevelNode(LevelCtrl levelCtrl) {
        super(Children.LEAF);
        this.levelCtrl = levelCtrl;
        this.levelCtrl.addPropertyChangeListener(LevelCtrl.PROP_DESCR, this);
        setDisplayName(levelCtrl.getDescr());
        setIconBaseWithExtension("au/com/trgtd/tr/view/goals/resource/Levels.png");
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set ss = sheet.get(Sheet.PROPERTIES);
        if (ss == null) {
            ss = Sheet.createPropertiesSet();
            sheet.put(ss);
        }
        ss.put(new PropertyGoalsIcon(levelCtrl));
        ss.put(new PropertyCanHaveProjects(levelCtrl));
        ss.put(new PropertyCanHaveStartDate(levelCtrl));
        ss.put(new PropertyCanHaveEndDate(levelCtrl));
        ss.put(new PropertyCanHaveAccountability(levelCtrl));
        ss.put(new PropertyCanHaveBrainstorming(levelCtrl));
        ss.put(new PropertyCanHaveObstacles(levelCtrl));
        ss.put(new PropertyCanHaveRewards(levelCtrl));
        ss.put(new PropertyCanHaveSupport(levelCtrl));
        ss.put(new PropertyCanHaveVision(levelCtrl));
        return sheet;
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == EditCookie.class) {
            return type.cast(this);
        }
//        if (type == DeleteCookie.class) {
//            return type.cast(this);
//        }
        return super.getCookie(type);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
            SystemAction.get(EditAction.class),
            null,
            SystemAction.get(CopyAction.class),
            SystemAction.get(CutAction.class),
            null,
            SystemAction.get(DeleteAction.class),
            null,
            SystemAction.get(MoveUpAction.class),
            SystemAction.get(MoveDownAction.class),
        };
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        LevelsCtrlLookup.getLevelsCtrl().deleteLevel(levelCtrl);
    }

    public boolean canEdit() {
        return true;
    }

    public void edit() {
        new LevelDialog().showModifyDialog(levelCtrl);
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        delete();
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return true;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        setDisplayName(levelCtrl.getDescr());
    }


}

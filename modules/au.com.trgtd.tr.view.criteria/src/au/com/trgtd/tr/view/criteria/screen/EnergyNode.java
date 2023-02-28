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

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.criteria.screen.dialog.EnergyDialog;
import java.awt.Component;
import java.io.IOException;
import javax.swing.*;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.CriteriaUtils;
import tr.model.criteria.Value;

/**
 * Energy node.
 *
 * @author Jeremy Moore
 */
final class EnergyNode extends AbstractNode implements EditCookie, Observer {

    public final Value value;

    public EnergyNode(Value value) {
        super(Children.LEAF);
        this.value = value;
        this.value.addObserver(this);
        setDisplayName(value.getName());
        setIconBaseWithExtension("au/com/trgtd/tr/view/criteria/screen/energy.png");
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

//    @Override
//    protected Sheet createSheet() {
//        Sheet sheet = super.createSheet();
//        Sheet.Set ss = sheet.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            sheet.put(ss);
//        }
//        ss.put(new PropertyGoalsIcon(value));
//        ss.put(new PropertyCanHaveProjects(value));
//        ss.put(new PropertyCanHaveStartDate(value));
//        ss.put(new PropertyCanHaveEndDate(value));
//        ss.put(new PropertyCanHaveAccountability(value));
//        ss.put(new PropertyCanHaveBrainstorming(value));
//        ss.put(new PropertyCanHaveObstacles(value));
//        ss.put(new PropertyCanHaveRewards(value));
//        ss.put(new PropertyCanHaveSupport(value));
//        ss.put(new PropertyCanHaveVision(value));
//        return sheet;
//    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == EditCookie.class) {
            return type.cast(this);
        }
        return super.getCookie(type);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
            SystemAction.get(EditAction.class),
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

    @Override
    public boolean canEdit() {
        return true;
    }

    @Override
    public void edit() {
        new EnergyDialog().showModifyDialog(value);
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

    @Override
    public void update(Observable observable, Object arguement) {
        setDisplayName(value.getName());
    }

    public void delete() {
        final Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        if (CriteriaUtils.instance().isUsedEnergy(data, value)) {
            SwingUtilities.invokeLater(() -> {
                deleteWithReplace(data);
            });
        } else {
            data.getEnergyCriterion().values.remove(value);
        }
    }

    private static class NullItem {
        @Override
        public String toString() {
            return "<none>";
        }
        @Override
        public boolean equals(Object o) {
            return o instanceof NullItem;
        }
        @Override
        public int hashCode() {
            return 7;
        }
    }

    private JComboBox getReplacementCombo(Data data) {
        JComboBox cb = new JComboBox<>();
        cb.addItem(new NullItem());
        for (Value itemValue : data.getEnergyCriterion().values.list()) {
            if (!itemValue.equals(value)) {
                cb.addItem(itemValue);
            }
        }
        return cb;
    }
    
    // Show dialog with in use message and option to replace references.
    private void deleteWithReplace(Data data) {

        JComboBox cb = getReplacementCombo(data);

        JPanel panel = new JPanel();
        panel.add(new JLabel(NbBundle.getMessage(getClass(), "MSG_ReplaceEnergy")));
        panel.add(cb);

        String title = org.openide.util.NbBundle.getMessage(getClass(), "MSG_DeleteEnergy", value.getName());
        Component p = WindowManager.getDefault().getMainWindow();
        int r = JOptionPane.showConfirmDialog(p, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) {
            return;
        }

        Value replacement;
        
        Object selectedItem = cb.getSelectedItem();
        if (selectedItem instanceof NullItem) {
            replacement = null;
        } else if (selectedItem instanceof Value v) {
            replacement = v;
        } else {
            return;
        }
        
        if (!CriteriaUtils.instance().replaceEnergy(data, value, replacement)) {
            String msg = org.openide.util.NbBundle.getMessage(getClass(), "ERR_ReplaceEnergy", value.getName(), replacement.getName());
            JOptionPane.showMessageDialog(p, msg);
            return;
        }

        // value should not be in use now so remove it
        data.getEnergyCriterion().values.remove(value);
    }
    
}

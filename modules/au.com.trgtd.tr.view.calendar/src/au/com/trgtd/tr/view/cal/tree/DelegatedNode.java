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
package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.cal.dialog.ActionEditDialog;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;

/**
 * Node for a delegated action.
 *
 * @author Jeremy Moore
 */
public class DelegatedNode extends AbstractNode implements EditCookie {
    
    public final Action action;

    /** Constructs a new instance. 
     * @param action the action.
     */
    public DelegatedNode(Action action) {
        super(Children.LEAF, Lookups.singleton(action));
        this.action = action;
        this.action.addPropertyChangeListener(Action.PROP_DONE, (PropertyChangeEvent pce) -> {
            setName(DelegatedNode.this.action.getDescription());
        });        
    }
    
    @Override
    public String getName() {
        return action.getDescription();
    }

    private String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml10(str);
        return str.replace("&apos;", "'");
    }

    @Override
    public String getHtmlDisplayName() {
        String name = escapeHTML(getName());
        String color = au.com.trgtd.tr.util.HTML.format(action.getTopic().getForeground());
        if (action.isDone()) {
            return "<s><font color='" + color + "'>" + name + "</font></s>";
        } else {
            return "<font color='" + color + "'>" + name + "</font>";
        }
    }
    
    @Override
    public String getShortDescription() {
        String desc = action.getDescription();
        String path = Services.instance.getPath(action);
        String dele = ((ActionStateDelegated)action.getState()).getTo();        
        return "<html>"
                + "<b>" + desc + "</b><br>" 
                + path + "<br>" 
                + "<i>Delegated to: </i><b>" + dele + "</b>"
                + "</html>";
    }
    
    @Override
    public String toString() {
        return action.getDescription();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canCopy() {
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

    @Override
    public boolean canEdit() {
        return true;
    }

    @Override
    public void edit() {
        ActionEditDialog dialog = new ActionEditDialog(action);
        dialog.showModifyDialog();
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[]{ SystemAction.get(EditAction.class) };
    }

    @Override
    public javax.swing.Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == EditCookie.class) {
            return type.cast(this);
        }
        return super.getCookie(type);
    }

    @Override
    public Image getIcon(int type) {
        Icon icon = action.getIcon(false);
        if (icon instanceof ImageIcon imageIcon) {
            return imageIcon.getImage();
        }
        return super.getIcon(type);
    }

}

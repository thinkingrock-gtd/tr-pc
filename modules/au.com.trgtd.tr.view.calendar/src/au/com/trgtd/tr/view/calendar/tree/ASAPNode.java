package au.com.trgtd.tr.view.calendar.tree;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.EditCookie;
import au.com.trgtd.tr.view.calendar.dialog.ActionEditDialog;
import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.lang.StringEscapeUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import tr.model.action.Action;

/**
 * Node for a Do ASAP action.
 *
 * @author Jeremy Moore
 */
public final class ASAPNode extends AbstractNode implements EditCookie {

    private final Action action;

    public ASAPNode(Action action) {
        this(action, new InstanceContent());
    }

    private ASAPNode(Action action, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        this.action = action;
        this.action.addPropertyChangeListener(Action.PROP_DONE, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                setName(ASAPNode.this.action.getDescription());
            }            
        });                
        content.add(this);
        content.add(action);     
    }

    @Override
    public String getShortDescription() {
        String desc = action.getDescription();
        String path = Services.instance.getPath(action);
        return "<html><b>" + desc + "</b><br>" + path + "</html>";
    }
    
    @Override
    public String getName() {
        return action.getDescription();
    }

    private String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml(str);
        return str.replace("&apos;", "'");
    }

    @Override
    public String getHtmlDisplayName() {
        String name = escapeHTML(getName());
        String color = action.isStateInactive()
                ? au.com.trgtd.tr.util.HTML.format(Color.GRAY)
                : au.com.trgtd.tr.util.HTML.format(action.getTopic().getForeground());        
        String html = "<font color='" + color + "'>" + name + "</font>";        
        if (action.isDone()) {
            return "<s>" + html + "</s>";
        } else {
            return html;
        }
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
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[]{SystemAction.get(EditAction.class)};
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
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        }
        return super.getIcon(type);
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
}

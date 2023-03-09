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
package au.com.trgtd.tr.view.contexts.screen;

import java.awt.Component;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.context.Context;
import tr.model.context.ContextUtils;
import tr.model.util.Manager;
import au.com.trgtd.tr.view.contexts.ContextsComboBox;
import au.com.trgtd.tr.view.contexts.ContextsComboBoxModel;
import au.com.trgtd.tr.view.contexts.DeleteContextCookie;
import au.com.trgtd.tr.view.contexts.EditContextCookie;
import au.com.trgtd.tr.view.contexts.dialog.ContextDialog;

/**
 * Node for a context.
 *
 * @author Jeremy Moore
 */
public class ContextNode extends AbstractNode implements EditContextCookie, DeleteContextCookie {
    
    public final Manager<Context> manager;
    public final Context context;
    
    /** Constructs a new instance. */
    public ContextNode(Manager<Context>manager, Context context) {
        super(Children.LEAF, Lookups.singleton(context));
        this.manager = manager;
        this.context = context;
    }
    
    @Override
    public String getName() {
        return (context == null) ? "" : context.getName();
    }
    
    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == EditContextCookie.class) return this;
        if (clazz == DeleteContextCookie.class) return this;
        return super.getCookie(clazz);
    }
    
    public void editContext() {
        if (context == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        // The default context can not be edited.
        if (context.equals(Context.getDefault())) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        new ContextDialog(data).showModifyDialog(context);
    }
    
    public void deleteContext() {
        if (context == null) return;
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        // The default context can not be deleted.
        if (context.equals(Context.getDefault())) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        if (ContextUtils.instance().isUsed(data, context)) {
            // dialog for in use message and option to replace
            ContextsComboBox ccb = new ContextsComboBox(new ContextsComboBoxModel(false));
            ccb.setSelectedIndex(0);
            
            JPanel panel = new JPanel();
            panel.add(new JLabel(NbBundle.getMessage(getClass(), "MSG_ReplaceContext")));
            panel.add(ccb);
            
            String title = org.openide.util.NbBundle.getMessage(ContextNode.class, "confirm.delete.context", context.getName());
            Component p = WindowManager.getDefault().getMainWindow();
            int r = JOptionPane.showConfirmDialog(p, panel, title, JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.CANCEL_OPTION) {
                return;
            }
            Context replacement = (Context)ccb.getSelectedItem();
            if (!ContextUtils.instance().replace(data, context, replacement)) {
//                String msg = "Can not replace context: " + context.getName() + "\n"
//                        + " with context: " + replacement.getName() + "\n\n";
                String msg = org.openide.util.NbBundle.getMessage(ContextNode.class, "error.replace.context", context.getName(), replacement.getName());
                JOptionPane.showMessageDialog(p, msg);
                return;
            }
        }
        
        // context should not be in use now so remove it
        data.getContextManager().remove(context);
    }
    
}

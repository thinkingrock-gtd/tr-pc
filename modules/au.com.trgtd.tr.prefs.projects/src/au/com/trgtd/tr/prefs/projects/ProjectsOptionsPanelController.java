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
package au.com.trgtd.tr.prefs.projects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

final class ProjectsOptionsPanelController extends OptionsPanelController {
     
    private ProjectsOptionsPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;
    
    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }
    
    @Override
    public void applyChanges() {
        getPanel().store();
        changed = false;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public boolean isValid() {
        return getPanel().valid();
    }
    
    @Override
    public boolean isChanged() {
        return changed;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.prefs.projects");
    }
    
    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    
    private ProjectsOptionsPanel getPanel() {
        if (panel == null) {
            panel = new ProjectsOptionsPanel(this);
        }
        return panel;
    }
    
    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
    
}

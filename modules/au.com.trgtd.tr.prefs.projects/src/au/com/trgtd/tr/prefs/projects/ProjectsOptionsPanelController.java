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

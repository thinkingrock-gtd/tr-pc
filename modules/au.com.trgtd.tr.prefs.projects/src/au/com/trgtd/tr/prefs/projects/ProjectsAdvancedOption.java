package au.com.trgtd.tr.prefs.projects;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class ProjectsAdvancedOption extends AdvancedOption {
    
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ProjectsAdvancedOption.class, "AdvancedOption_DisplayName");
    }
    
    @Override
    public String getTooltip() {
        return NbBundle.getMessage(ProjectsAdvancedOption.class, "AdvancedOption_Tooltip");
    }
    
    @Override
    public OptionsPanelController create() {
        return new ProjectsOptionsPanelController();
    }
    
}

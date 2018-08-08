package au.com.trgtd.tr.imports.thoughts.prefs;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class ImportThoughtsAdvancedOption extends AdvancedOption {
    
    public String getDisplayName() {
        return NbBundle.getMessage(getClass(), "AdvancedOption_DisplayName");
    }
    
    public String getTooltip() {
        return NbBundle.getMessage(getClass(), "AdvancedOption_Tooltip");
    }
    
    public OptionsPanelController create() {
        return new ImportThoughtsPrefsPanelController();
    }
    
}

package au.com.trgtd.tr.prefs.dates;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class DatesAdvancedOption extends AdvancedOption {
    
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(DatesAdvancedOption.class, "AdvancedOption_DisplayName");
    }
    
    @Override
    public String getTooltip() {
        return NbBundle.getMessage(DatesAdvancedOption.class, "AdvancedOption_Tooltip");
    }
    
    @Override
    public OptionsPanelController create() {
        return new DatesOptionsPanelController();
    }
    
}

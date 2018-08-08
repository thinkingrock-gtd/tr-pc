package au.com.trgtd.tr.sync.device.prefs;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

public final class SyncAdvancedOption extends AdvancedOption {
    
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SyncAdvancedOption.class, "AdvancedOption_Display");
    }
    
    @Override
    public String getTooltip() {
        return NbBundle.getMessage(SyncAdvancedOption.class, "AdvancedOption_Tooltip");
    }
    
    @Override
    public OptionsPanelController create() {
        return new SyncOptionsPanelController();
    }
    
}

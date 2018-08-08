package au.com.trgtd.tr.email.prefs;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

/**
 * Email advanced option.
 *  
 * @author Jeremy Moore
 */
public final class EmailAdvancedOption extends AdvancedOption {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(EmailAdvancedOption.class, "AdvancedOption_DisplayName_Email");
    }

    @Override
    public String getTooltip() {
        return NbBundle.getMessage(EmailAdvancedOption.class, "AdvancedOption_Tooltip_Email");
    }

    @Override
    public OptionsPanelController create() {
        return new EmailOptionsPanelController();
    }
}

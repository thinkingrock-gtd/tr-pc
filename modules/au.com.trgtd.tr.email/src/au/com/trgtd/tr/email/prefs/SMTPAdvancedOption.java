package au.com.trgtd.tr.email.prefs;

import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.NbBundle;

/**
 * SMTP advanced option.
 *  
 * @author Jeremy Moore
 */
public final class SMTPAdvancedOption extends AdvancedOption {

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SMTPAdvancedOption.class, "AdvancedOption_DisplayName_SMTP");
    }

    @Override
    public String getTooltip() {
        return NbBundle.getMessage(SMTPAdvancedOption.class, "AdvancedOption_Tooltip_SMTP");
    }

    @Override
    public OptionsPanelController create() {
        return new SMTPOptionsPanelController();
    }
}

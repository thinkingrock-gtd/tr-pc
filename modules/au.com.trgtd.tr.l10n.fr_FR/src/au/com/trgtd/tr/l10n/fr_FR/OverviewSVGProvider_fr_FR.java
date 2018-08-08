package au.com.trgtd.tr.l10n.fr_FR;

import au.com.trgtd.tr.view.overview.spi.OverviewSVGProvider;
import java.net.URL;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileUtil;

public class OverviewSVGProvider_fr_FR implements OverviewSVGProvider {

    @Override
    public String getLanguage() {
        return "fr";
    }
 
    @Override
    public String getCountry() {
        return "FR";
    }
 
    @Override
    public URL getURL() {
        try {
            return FileUtil.getConfigFile("Overview/overview_fr_FR.svg").getURL();
        } catch (FileStateInvalidException ex) {
            return null;
        }
    }
 
}

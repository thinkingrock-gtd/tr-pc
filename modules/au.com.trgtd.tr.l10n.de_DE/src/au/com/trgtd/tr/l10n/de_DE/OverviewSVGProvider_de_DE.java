package au.com.trgtd.tr.l10n.de_DE;

import au.com.trgtd.tr.view.overview.spi.OverviewSVGProvider;
import java.net.URL;
import org.openide.filesystems.FileUtil;

public class OverviewSVGProvider_de_DE implements OverviewSVGProvider {
 
    @Override
    public String getLanguage() {
        return "de";
    }

    @Override
    public String getCountry() { 
        return "DE";
    }

    @Override
    public URL getURL() {
        try {
            return FileUtil.getConfigFile("Overview/overview_de_DE.svg").toURL();
        } catch (Exception ex) {
            return null;
        }
    }

}
 
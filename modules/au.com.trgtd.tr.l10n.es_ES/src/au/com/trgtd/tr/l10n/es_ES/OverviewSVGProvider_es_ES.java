package au.com.trgtd.tr.l10n.es_ES;

import java.net.URL;
import org.openide.filesystems.FileUtil;
import au.com.trgtd.tr.view.overview.spi.OverviewSVGProvider;

public class OverviewSVGProvider_es_ES implements OverviewSVGProvider {

    @Override
    public String getLanguage() {
        return "es";
    }

    @Override
    public String getCountry() {
        return "ES";
    }

    @Override
    public URL getURL() {
        try {
            return FileUtil.getConfigFile("Overview/overview_es_ES.svg").getURL();
        } catch (Exception ex) {
            return null;
        }
    }

}

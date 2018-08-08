package au.com.trgtd.tr.l10n.en_US;
 
import java.net.URL;
import org.openide.filesystems.FileUtil;
import au.com.trgtd.tr.view.overview.spi.OverviewSVGProvider;

public class OverviewSVGProvider_en_US implements OverviewSVGProvider {

    @Override
    public String getLanguage() {
        return "en";
    } 

    @Override
    public String getCountry() {
        return "US";
    }

    @Override
    public URL getURL() {
        return FileUtil.getConfigFile("Overview/overview_en_US.svg").toURL();
    }

}

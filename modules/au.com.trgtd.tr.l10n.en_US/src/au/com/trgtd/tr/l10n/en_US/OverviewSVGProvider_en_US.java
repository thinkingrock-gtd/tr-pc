/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

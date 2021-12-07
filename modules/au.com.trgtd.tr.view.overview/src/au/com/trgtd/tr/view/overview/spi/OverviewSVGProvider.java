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
package au.com.trgtd.tr.view.overview.spi;

import java.net.URL;

/**
 * Service provider interface for modules that provide an overview SVG file
 * for a locale.
 * 
 * @author Jeremy Moore
 */
public interface OverviewSVGProvider {

    /**
     * Gets the locale language.
     * @return the language code (e.g. "fr" for French).
     */
    public String getLanguage();

    /**
     * Gets the locale country.
     * @return the country code (e.g. "FR" for France).
     */
    public String getCountry();
    
    /**
     * Gets the URL of the overview SVG file.
     * @return the URL.
     */
    public URL getURL();

}

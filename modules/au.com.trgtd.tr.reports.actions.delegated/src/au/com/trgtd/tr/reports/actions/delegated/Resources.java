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
package au.com.trgtd.tr.reports.actions.delegated;

import java.io.File;
import org.openide.modules.InstalledFileLocator;

/**
 * Resources.
 *
 * @author Jeremy Moore
 */
public class Resources {

    public static final File FILE_RPT_DELEGATED = getInstalledFile("resource/reports/ActionsDelegated.jasper");
    public static final File FILE_RPT_DELEGATED_GROUPED = getInstalledFile("resource/reports/ActionsDelegatedGrouped.jasper");

    public static final File FILE_RPT_DELEGATED_LTR = getInstalledFile("resource/reports/ActionsDelegatedLTR.jasper");
    public static final File FILE_RPT_DELEGATED_GROUPED_LTR = getInstalledFile("resource/reports/ActionsDelegatedGroupedLTR.jasper");
    
    private static final String CODE_NAME_BASE= "au.com.trgtd.tr.reports.actions.delegated";
    
    private static File getInstalledFile(String path) {
        File file = InstalledFileLocator.getDefault().locate(path, CODE_NAME_BASE, false);
        if (file != null && file.isFile()) {
            return file;
        }
        return null;
    }
}

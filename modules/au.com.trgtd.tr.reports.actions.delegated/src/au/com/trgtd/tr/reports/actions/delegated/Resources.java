/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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

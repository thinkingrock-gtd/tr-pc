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
package au.com.trgtd.tr.reports.actions.doasap;

import java.io.File;
import org.openide.modules.InstalledFileLocator;
import tr.extract.reports.PaperSize;

/**
 * Resources.
 *
 * @author Jeremy Moore
 */
public class Resources {

    public static final File getFileDoASAPByContextOnly(PaperSize size) {
        if (size == PaperSize.Letter) {
            return getInstalledFile("resource/reports/DoASAPbyContextOnlyLTR.jasper");
        } else {
            return getInstalledFile("resource/reports/DoASAPbyContextOnly.jasper");
        }
    }

    public static final File getFileDoASAPByContextEnergy(PaperSize size) {
        if (size == PaperSize.Letter) {
            return getInstalledFile("resource/reports/DoASAPbyContextEnergyLTR.jasper");
        } else {
            return getInstalledFile("resource/reports/DoASAPbyContextEnergy.jasper");
        }
    }

    public static final File getFileDoASAPByContextPriority(PaperSize size) {
        if (size == PaperSize.Letter) {
            return getInstalledFile("resource/reports/DoASAPbyContextPriorityLTR.jasper");
        } else {
            return getInstalledFile("resource/reports/DoASAPbyContextPriority.jasper");
        }
    }

    public static final File getFileDoASAPByContextTime(PaperSize size) {
        if (size == PaperSize.Letter) {
            return getInstalledFile("resource/reports/DoASAPbyContextTimeLTR.jasper");
        } else {
            return getInstalledFile("resource/reports/DoASAPbyContextTime.jasper");
        }
    }

    private static final String CODE_NAME_BASE= "au.com.trgtd.tr.reports.actions.doasap";
    
    private static File getInstalledFile(String path) {
        File file = InstalledFileLocator.getDefault().locate(path, CODE_NAME_BASE, false);
        if (file != null && file.isFile()) {
            return file;
        }
        return null;
    }
}

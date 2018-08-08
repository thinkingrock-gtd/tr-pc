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

package au.com.trgtd.tr.datastore.xstream;

import java.io.File;
import org.openide.modules.ModuleInstall;

/**
 * Module installer
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {
    
    @Override
    public void restored() {

        // Set the datafile path to the last used (if possible)
        String path = System.getProperty("tr.datafile");
        if (path != null) {
            File file = new File(path);
            if (file.isFile()) {
                XStreamDataStore.instance().setPath(path);
            }
        }

        CleanupRecoveryFilesScheduler.instance().runTask(0, 0, 20);
        CleanupRecoveryFilesScheduler.instance().startScheduler();

        new CleanupBackupFilesThread().start();

        new BackupThread().start();
    }
    
}

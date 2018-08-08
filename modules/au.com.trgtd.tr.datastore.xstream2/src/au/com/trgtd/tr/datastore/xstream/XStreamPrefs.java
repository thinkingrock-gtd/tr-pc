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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.UtilsPrefs;
import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;

/**
 * Data manager singleton that uses XStream (http://xstream.codehaus.org/) to
 * persist the data model.
 *
 * @author Jeremy Moore
 */
public final class XStreamPrefs {

    private static final Logger LOG = Logger.getLogger("tr.xstream");
    private static final String PATHNAME = "xstream";

    private static final String KEY_DATAFILE = "data.file";
    private static final String DEF_DATAFILE = "";

    private static final XStreamPrefs instance = new XStreamPrefs();

    private Preferences prefs;

    private XStreamPrefs() {
        try {
            Preferences newroot = Preferences.userRoot().node(Constants.PREFS_PATH_NEW);
            if (newroot.nodeExists(PATHNAME)) {
                // use existing
                prefs = newroot.node(PATHNAME);
            } else {
                // create new
                prefs = newroot.node(PATHNAME);
                // copy old preferences if they exist
                Preferences oldroot = Preferences.userRoot().node(Constants.PREFS_PATH_OLD);
                if (oldroot.nodeExists(PATHNAME)) {
                    Preferences oldprefs = oldroot.node(PATHNAME);
                    UtilsPrefs.copy(oldprefs, prefs, KEY_DATAFILE, DEF_DATAFILE);
                    prefs.flush();
                }
            }
        } catch (BackingStoreException ex) {
            LOG.severe("XStream preferences error. " + ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Gets the data file path preference.
     * @return The data file path.
     */
    public static final String getPath() {
        return instance.prefs.get(KEY_DATAFILE, DEF_DATAFILE);
    }

    /**
     * Sets the data file path preference.
     * @param path The data file path.
     */
    public static final void setPath(String path) {
        if (path == null || path.trim().equals("")) {
            instance.prefs.remove(KEY_DATAFILE);
        } else {
            // if the file is in the user directory then just store the file name
            File file = new File(path);
            String userdir = System.getProperty("user.dir");
            if (userdir.equals(file.getParent())) {
                instance.prefs.put(KEY_DATAFILE, file.getName());
            } else {
                instance.prefs.put(KEY_DATAFILE, path);
            }
        }
        flush();
    }

    private static final void flush() {
        try {
            instance.prefs.flush();
        } catch (BackingStoreException ex) {
            LOG.severe("XStream preferences exception. " + ex.getMessage());
        }
    }

}

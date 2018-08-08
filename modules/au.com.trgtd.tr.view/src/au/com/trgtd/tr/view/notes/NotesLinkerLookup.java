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
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.notes;

import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 * Linker lookup singleton.
 *
 * @author Jeremy Moore
 */
public class NotesLinkerLookup extends ProxyLookup {

    private static final NotesLinkerLookup instance = new NotesLinkerLookup();
    private Map<String, NotesLinker> map;


    /* Private singleton constructor. */
    private NotesLinkerLookup() {
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static NotesLinkerLookup instance() {
        return instance;
    }

    /**
     * Gets a notes linker for the given URL scheme if possible.
     * @param scheme The URL scheme.
     */
    public NotesLinker getHandler(String url) {
        if (url == null) {
            return null;
        }
        int p = url.indexOf(":");
        if (p == -1) {
            return null;
        }
        String scheme = url.substring(0, p + 1);
        if (map == null) {
            map = new HashMap<String, NotesLinker>();
            for (NotesLinker linker : Lookup.getDefault().lookupResult(NotesLinker.class).allInstances()) {
                map.put(linker.getScheme(), linker);
            }
        }
        return map.get(scheme);
    }
}

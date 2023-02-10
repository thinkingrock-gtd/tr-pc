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
            map = new HashMap<>();
            for (NotesLinker linker : Lookup.getDefault().lookupResult(NotesLinker.class).allInstances()) {
                map.put(linker.getScheme(), linker);
            }
        }
        return map.get(scheme);
    }
}

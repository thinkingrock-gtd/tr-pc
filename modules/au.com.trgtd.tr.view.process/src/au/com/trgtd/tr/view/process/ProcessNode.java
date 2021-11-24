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
package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.view.DeleteCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.Lookups;

/**
 * Node for processing.
 *
 * @author Jeremy Moore
 */
public class ProcessNode extends AbstractNode {
    
    public final ProcessCookie processCookie;
    public final ProcessAddCookie processAddCookie;
    public final DeleteCookie deleteCookie;

    /** Constructs a new instance.
     * @param deleteCookie
     * @param processCookie */
    public ProcessNode(DeleteCookie deleteCookie, ProcessCookie processCookie) {
        super(Children.LEAF, Lookups.fixed(deleteCookie, processCookie));
        this.processCookie = processCookie;
        this.processAddCookie = null;
        this.deleteCookie = deleteCookie;        
    }
    
    /** Constructs a new instance.
     * @param deleteCookie
     * @param processAddCookie
     * @param processCookie */
    public ProcessNode(DeleteCookie deleteCookie, ProcessAddCookie processAddCookie, ProcessCookie processCookie) {
        super(Children.LEAF, Lookups.fixed(deleteCookie, processAddCookie, processCookie));
        this.processCookie = processCookie;
        this.processAddCookie = processAddCookie;
        this.deleteCookie = deleteCookie;
    }
    
    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ProcessCookie.class) {
            return processCookie;
        }
        if (clazz == ProcessAddCookie.class && processAddCookie != null) {
            return processAddCookie;
        }
        if (clazz == DeleteCookie.class) {
            return deleteCookie;
        }
        return super.getCookie(clazz);
    }

}

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
package au.com.trgtd.tr.view;

import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 * TopComponent window that can not be closed by the user but can be closed
 * programmatically.
 *
 * @author Jeremy Moore
 */
public class Window extends TopComponent {
    
//  private boolean isCloseAllowed;
    
    /**
     * Constructs a new default instance.
     */
    public Window() {
        super();
    }
    
    /**
     * Constructs a new instance for the given lookup.
     */
    public Window(Lookup lookup) {
        super(lookup);
    }
    
//    /**
//     * Overridden to not allowed except when forcing closure.
//     */
//    @Override
//    public boolean canClose() {
//
//        synchronized(this) {
//
//            return isCloseAllowed;
//
//        }
//
//    }
    
//    /**
//     * Provides the ability to force closure of the window.
//     */
//    public boolean forceClose() {
//
//        synchronized(this) {
//
//            boolean result = true;
//
//            if (isOpened()) {
//                isCloseAllowed = true;
//                result = close();
//                isCloseAllowed = false;
//            }
//
//            return result;
//        }
//
//    }
    
    /**
     * Override to take focus when offered.
     */
    public void takeFocus() {
    }
    
}

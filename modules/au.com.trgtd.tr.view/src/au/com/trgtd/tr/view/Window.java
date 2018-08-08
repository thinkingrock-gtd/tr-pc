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

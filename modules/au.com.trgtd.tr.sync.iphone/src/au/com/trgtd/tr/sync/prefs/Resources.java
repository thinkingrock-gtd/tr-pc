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

package au.com.trgtd.tr.sync.prefs;

import org.openide.util.NbBundle;

/**
 * Resources.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class Resources {
    
    /**
     * Gets the text for the given key from the resource Bundle.properties file.
     * @param key The key value.
     * @return the text value if the key exists, otherwise the key.
     */
    public static String getText(String key) {
        try {
            return NbBundle.getMessage(Resources.class, key);
        } catch (Exception ex) {
            return key;
        }
    }
    
}

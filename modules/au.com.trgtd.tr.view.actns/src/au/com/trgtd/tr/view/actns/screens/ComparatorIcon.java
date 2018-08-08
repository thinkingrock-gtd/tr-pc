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

package au.com.trgtd.tr.view.actns.screens;

import java.util.Comparator;
import javax.swing.Icon;

/**
 * Comparator for due dates.
 *
 * @author Jeremy Moore
 */
public class ComparatorIcon implements Comparator<Icon> {
    
    public int compare(Icon icon1, Icon icon2) {
        if (icon1 == icon2) return 0;
        if (icon1 == null) return 1;
        if (icon2 == null) return -1;
        return icon1.toString().compareTo(icon2.toString());
    }
    
}


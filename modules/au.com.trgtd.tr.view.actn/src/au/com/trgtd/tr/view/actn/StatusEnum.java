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

package au.com.trgtd.tr.view.actn;

import org.openide.util.NbBundle;

/**
 * Enumeration for action states.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public enum StatusEnum {
    
    INACTIVE,
    DO_ASAP,
    SCHEDULED,
    DELEGATED;
    
    @Override
    public String toString() {
        switch (this) {
            case INACTIVE:  return STRING_INACTIVE;
            case DO_ASAP:   return STRING_DO_ASAP;
            case SCHEDULED: return STRING_SCHEDULED;
            case DELEGATED: return STRING_DELEGATED;
            default: return "";
        }
    }

    private final static String STRING_INACTIVE = NbBundle.getMessage(StatusEnum.class, "ActionStateInactive");
    private final static String STRING_DO_ASAP = NbBundle.getMessage(StatusEnum.class, "ActionStateDoASAP");
    private final static String STRING_SCHEDULED = NbBundle.getMessage(StatusEnum.class, "ActionStateScheduled");
    private final static String STRING_DELEGATED = NbBundle.getMessage(StatusEnum.class, "ActionStateDelegated");
}

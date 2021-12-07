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

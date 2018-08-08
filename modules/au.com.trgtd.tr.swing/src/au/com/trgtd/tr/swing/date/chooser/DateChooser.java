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

package au.com.trgtd.tr.swing.date.chooser;

import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.JFrame;

/**
 * Singleton provider of the date chooser.
 *
 * @author Jeremy Moore
 */
public final class DateChooser implements PreferenceChangeListener {
    
    private final Window owner;
    private final String title;
    private DateChooserDialog dialog;
    
    public DateChooser(Frame owner) {
        this(owner, null);
    }
    
    public DateChooser(Frame owner, String title) {
        this.owner = owner;
        this.title = title;
        DatesPrefs.getPrefs().addPreferenceChangeListener(this);
    }
    
    public DateChooser(Dialog owner) {
        this(owner, null);
    }
    
    public DateChooser(Dialog owner, String title) {
        this.owner = owner;
        this.title = title;
        DatesPrefs.getPrefs().addPreferenceChangeListener(this);
    }
    
    
    public DateChooserDialog getDialog() {
        synchronized(this) {
            if (dialog == null) {
                if (owner instanceof Frame) {
                    dialog = (title == null) ? new DateChooserDialog((Frame)owner) : new DateChooserDialog((Frame)owner, title);
                } else if (owner instanceof Dialog) {
                    dialog = (title == null) ? new DateChooserDialog((Dialog)owner) : new DateChooserDialog((Dialog)owner, title);
                } else {
                    dialog = (title == null) ? new DateChooserDialog(new JFrame()) : new DateChooserDialog(new JFrame(), title);
                }
            }
            return dialog;
        }
    }
    
    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey().equals(DatesPrefs.KEY_WEEK_FIRST_DAY)) {
            synchronized(this) {
                dialog = null;
            }
        }
    }
    
}

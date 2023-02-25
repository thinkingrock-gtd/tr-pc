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
                if (owner instanceof Frame frame) {
                    dialog = (title == null) ? new DateChooserDialog(frame) : new DateChooserDialog(frame, title);
                } else if (owner instanceof Dialog dlg) {
                    dialog = (title == null) ? new DateChooserDialog(dlg) : new DateChooserDialog(dlg, title);
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

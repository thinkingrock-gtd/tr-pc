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

package au.com.trgtd.tr.view.project.chooser;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;

/**
 * Project chooser.
 *
 * @author Jeremy Moore
 */
public final class ProjectChooser {
    
    private final Window owner;
    private final String title;
    private ProjectChooserDialog dialog;
    
    public ProjectChooser(Frame owner) {
        this(owner, null);
    }
    
    public ProjectChooser(Frame owner, String title) {
        this.owner = owner;
        this.title = title;
    }
    
    public ProjectChooser(Dialog owner) {
        this(owner, null);
    }
    
    public ProjectChooser(Dialog owner, String title) {
        this.owner = owner;
        this.title = title;
    }
    
    public ProjectChooserDialog getDialog() {
        synchronized(this) {
            if (dialog == null) {
                if (owner instanceof Frame) {
                    dialog = (title == null) ? new ProjectChooserDialog((Frame)owner) : new ProjectChooserDialog((Frame)owner, title);
                } else if (owner instanceof Dialog) {
                    dialog = (title == null) ? new ProjectChooserDialog((Dialog)owner) : new ProjectChooserDialog((Dialog)owner, title);
                } else {
                    dialog = (title == null) ? new ProjectChooserDialog(new JFrame()) : new ProjectChooserDialog(new JFrame(), title);
                }
            }
            return dialog;
        }
    }
    
}

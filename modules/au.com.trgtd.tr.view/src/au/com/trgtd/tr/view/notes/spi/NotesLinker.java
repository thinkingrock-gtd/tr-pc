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
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.notes.spi;

import au.com.trgtd.tr.view.notes.NotesLink;
import javax.swing.ImageIcon;

/**
 * Service provider interface for creating and viewing links in notes fields.
 * 
 * @author Jeremy Moore
 */
public interface NotesLinker {

    public String getScheme();

    public ImageIcon getIcon();

    public int getShortcutMask();

    public int getShortcutKey();

    public String getToolTip();

    public NotesLink getUserLink();

    public void openUserLink(String url);

}

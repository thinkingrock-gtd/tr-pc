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

package au.com.trgtd.tr.view.actns.screens.prefs;

import javax.swing.Icon;
import org.netbeans.spi.options.OptionsCategory;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public final class ActionsScreensOptionsCategory extends OptionsCategory {
    
    @Override
    public Icon getIcon() {
//      return new ImageIcon(Utilities.loadImage("au/com/trgtd/tr/view/actns/screens/prefs/ActionsScreensOptions32.png"));
        return ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/actns/screens/prefs/ActionsScreensOptions32.png", false);
    }
    
    public String getCategoryName() {
        return NbBundle.getMessage(ActionsScreensOptionsCategory.class, "OptionsCategory_Name");
    }
    
    public String getTitle() {
        return NbBundle.getMessage(ActionsScreensOptionsCategory.class, "OptionsCategory_Title");
    }
    
    public OptionsPanelController create() {
        return new ActionsScreensOptionsPanelController();
    }
    
}

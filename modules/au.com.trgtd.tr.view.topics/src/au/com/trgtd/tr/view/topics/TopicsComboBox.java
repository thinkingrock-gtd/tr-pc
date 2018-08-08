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
package au.com.trgtd.tr.view.topics;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Font;
import javax.swing.ComboBoxModel;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.swing.TRComboBox;

/**
 * Combo box for topics.
 *
 * @author Jeremy Moore
 */
public class TopicsComboBox extends TRComboBox {

    /**
     * Constructs a new default instance.
     */
    public TopicsComboBox() {
        this(new TopicsComboBoxModel());
    }

    /**
     * Constructs a new instance for the given data model.
     * @param model The topics combo box model.
     */
    public TopicsComboBox(ComboBoxModel model) {
        super(model);
        setRenderer(new TopicsListCellRenderer(this.getRenderer()));
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
        setToolTipText(NbBundle.getMessage(getClass(), "TTT_TopicsComboBox"));
    }

}

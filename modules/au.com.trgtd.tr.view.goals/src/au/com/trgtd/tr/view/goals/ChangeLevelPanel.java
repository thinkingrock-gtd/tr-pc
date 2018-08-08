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
package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.view.goals.levels.combo.LevelsComboBox;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

/**
 * Change level panel.
 *
 * @author Jeremy Moore
 */
public class ChangeLevelPanel extends JPanel {

    /** Creates new form TickleDatePanel */
    public ChangeLevelPanel(String msg) {
        this.msg = msg;
        initView();
    }

    private void initView() {
        msgLabel = new TRLabel(msg);
        levelsLabel = new TRLabel(NbBundle.getMessage(ChangeLevelPanel.class, "level"));
        levelsCombo = new LevelsComboBox();

        setLayout(new MigLayout("insets 12px", "2[600]2", "2[]12[]2"));
        add(msgLabel, "wrap");
        add(levelsLabel, "align left, split 2");
        add(levelsCombo, "align left, wrap");
    }

    public LevelCtrl getLevel() {
        return (LevelCtrl)levelsCombo.getSelectedItem();
    }

    private final String msg;
    private TRLabel msgLabel;
    private LevelsComboBox levelsCombo;
    private TRLabel levelsLabel;

}

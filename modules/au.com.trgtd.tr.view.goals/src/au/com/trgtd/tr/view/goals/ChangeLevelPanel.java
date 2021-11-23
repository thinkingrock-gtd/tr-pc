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

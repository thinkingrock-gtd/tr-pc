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
package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.ctlr.WeekPanelCtlr;
import au.com.trgtd.tr.cal.view.DateChangePanel;
import au.com.trgtd.tr.cal.view.DatePanel;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.cal.view.WeekPanel;
import au.com.trgtd.tr.view.ViewUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class WeekTopComponent extends TopComponent {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/cal/resource/week.png";
    private final DateCtlr dateCtlr = DateCtlr.DEFAULT;
    private ShowHideDoneAction showDoneAction;
    
    /**
     * Creates a new instance.
     */
    public WeekTopComponent() {
        setName(NbBundle.getMessage(WeekTopComponent.class, "CTL_WeekTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        this.initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(ViewUtils.COLOR_PANEL_BG);

        CalModelImp calModel = new CalModelImp();
        
        showDoneAction = new ShowHideDoneAction(calModel, dateCtlr);

        WeekPanelCtlr weekPanelCtlr = new WeekPanelCtlr(calModel, dateCtlr, 0, 23);
        WeekPanel weekPanel = weekPanelCtlr.getWeekPanel();
        weekPanel.addDayListener((PropertyChangeEvent pce) -> {
            Object newValue = pce.getNewValue();
            if (newValue instanceof Date date) {
                dateCtlr.setDate(date);
                activateDayView();
            }            
        });
        
        weekPanel.setOpaque(true);
        weekPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DatePanel dateDisplayPanel = new DatePanel(dateCtlr, Period.Week);
        dateDisplayPanel.setOpaque(true);
        dateDisplayPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DateChangePanel dateChangerPanel = new DateChangePanel(dateCtlr, Period.Week);
        dateChangerPanel.setOpaque(true);
        dateChangerPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel northPanel = new JPanel(new MigLayout("fill", "6[grow]6[]6[]6", "0[]0"));
        northPanel.add(dateDisplayPanel, "align left");
        northPanel.add(getShowDoneButton(), "align right");
        northPanel.add(dateChangerPanel, "align right, wrap");
        northPanel.setOpaque(true);
        northPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        add(northPanel, BorderLayout.NORTH);
        add(weekPanel, BorderLayout.CENTER);
    }


    private Component getShowDoneButton() {
        JToggleButton button = new JToggleButton(showDoneAction);
        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setText("");
        button.setFocusable(false);
        return button;
    }

    private void activateDayView() {
        TopComponent tc = WindowManager.getDefault().findTopComponent("DayTopComponent");
        if (null == tc) {
            tc = new DayTopComponent();
        }
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode != null) {
            mode.dockInto(tc);
        }
        tc.open();
        tc.requestActive();
    }

    @Override
    protected void componentOpened() {
        dateCtlr.fireChange();
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return "WeekTopComponent";
    }

}

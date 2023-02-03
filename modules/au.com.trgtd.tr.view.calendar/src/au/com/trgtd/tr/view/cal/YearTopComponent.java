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
import au.com.trgtd.tr.cal.view.DateChangePanel;
import au.com.trgtd.tr.cal.view.DatePanel;
import au.com.trgtd.tr.cal.view.DayViewer;
import au.com.trgtd.tr.cal.view.MonthViewer;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.cal.view.WeekViewer;
import au.com.trgtd.tr.cal.view.YearPanel;
import au.com.trgtd.tr.view.ViewUtils;
import java.awt.BorderLayout;
import java.awt.Color;
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

/**
 * Calendar year view top component.
 */
public final class YearTopComponent extends TopComponent implements MonthViewer, WeekViewer, DayViewer {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/cal/resource/year.png";
    private final static Color BG = ViewUtils.COLOR_PANEL_BG;

    private final DateCtlr dateCtlr;
    private final CalModelImp calModel;
    private final JPanel yearPanel;
    private final ShowHideDoneAction showDoneAction;

    /**
     * Creates a new instance.
     */
    public YearTopComponent() {
        setName(NbBundle.getMessage(YearTopComponent.class, "CTL_YearTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setLayout(new BorderLayout());
        setOpaque(false);
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        this.dateCtlr = DateCtlr.DEFAULT;
        this.calModel = new CalModelImp();
        this.yearPanel = new YearPanel(dateCtlr, calModel, this, this, BG);
        this.showDoneAction = new ShowHideDoneAction(calModel, dateCtlr);
        this.initComponents();
    }

    private void initComponents() {
        dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            revalidate();
            repaint();
        });

        DatePanel dateDisplayPanel = new DatePanel(dateCtlr, Period.Year);
        dateDisplayPanel.setOpaque(true);
        dateDisplayPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DateChangePanel dateChangerPanel = new DateChangePanel(dateCtlr, Period.Year);
        dateChangerPanel.setOpaque(true);
        dateChangerPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel northPanel = new JPanel(new MigLayout("fill", "6[grow]6[]6[]6", "0[]0"));
        northPanel.add(dateDisplayPanel, "align left");
        northPanel.add(getShowDoneButton(), "align right");
        northPanel.add(dateChangerPanel, "align right, wrap");
        northPanel.setOpaque(true);
        northPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        add(northPanel, BorderLayout.NORTH);
        add(yearPanel, BorderLayout.CENTER);
    }

    private Component getShowDoneButton() {
        JToggleButton button = new JToggleButton(showDoneAction);
        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setText("");
        button.setFocusable(false);
//      button.doClick();
        return button;
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
        return "YearTopComponent";
    }

    @Override
    public void showDayView(Date date) {
        activateDayView();
        dateCtlr.setDate(date);
    }

    @Override
    public void showWeekView(Date date) {
        activateWeekView();
        dateCtlr.setDate(date);
    }

    @Override
    public void showMonthView(Date date) {
        activateMonthView();
        dateCtlr.setDate(date);
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
    private void activateWeekView() {
        TopComponent tc = WindowManager.getDefault().findTopComponent("WeekTopComponent");
        if (null == tc) {
            tc = new WeekTopComponent();
        }
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode != null) {
            mode.dockInto(tc);
        }
        tc.open();
        tc.requestActive();
    }
    private void activateMonthView() {
        TopComponent tc = WindowManager.getDefault().findTopComponent("MonthTopComponent");
        if (null == tc) {
            tc = new MonthTopComponent();
        }
        Mode mode = WindowManager.getDefault().findMode("editor");
        if (mode != null) {
            mode.dockInto(tc);
        }
        tc.open();
        tc.requestActive();
    }
    
}

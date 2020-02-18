<<<<<<< HEAD:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/wp/WeekPlanTopComponent.java
package au.com.trgtd.tr.view.calendar.wp;
=======
package au.com.trgtd.tr.view.cal;
>>>>>>> fdaf0cd2bc3b300181fca18eac6d6db3159d8fbd:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/PlanWeekTopComponent.java

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.view.DateChangePanel;
import au.com.trgtd.tr.cal.view.DatePanel;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.actns.screens.ActionsScreen;
import au.com.trgtd.tr.view.actns.screens.ReviewActionsTopComponent;
import au.com.trgtd.tr.view.calendar.DayTopComponent;
import au.com.trgtd.tr.view.calendar.ShowHideDoneAction;
import au.com.trgtd.tr.view.calendar.Singleton;
import au.com.trgtd.tr.view.calendar.TrCalModel;
import java.awt.BorderLayout;
<<<<<<< HEAD:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/wp/WeekPlanTopComponent.java
import java.awt.Component;
=======
>>>>>>> fdaf0cd2bc3b300181fca18eac6d6db3159d8fbd:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/PlanWeekTopComponent.java
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class PlanWeekTopComponent extends TopComponent {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/cal/resource/weekplan.png";

<<<<<<< HEAD:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/wp/WeekPlanTopComponent.java
    private final TrCalModel calModel = new TrCalModel();
    private final DateCtlr dateCtlr = Singleton.dateCtlr;
    private final WeekPlanPanelCtlr weekPlanPanelCtlr = new WeekPlanPanelCtlr(calModel, dateCtlr);
    private final ShowHideDoneAction showDoneAction = new ShowHideDoneAction(calModel, dateCtlr);

    private final DateDisplayPanel dateDisplayPanel;
    private final DateChangerPanel dateChangerPanel;
    private final JPanel weekDatePanel;
    private final WeekPlanPanel weekPlanPanel;
    
    public WeekPlanTopComponent() {
=======
    public PlanWeekTopComponent() {
        setName(NbBundle.getMessage(PlanWeekAction.class, "CTL_WeekPlanTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setBackground(ViewUtils.COLOR_PANEL_BG);
        setLayout(new BorderLayout());
        setOpaque(true);

>>>>>>> fdaf0cd2bc3b300181fca18eac6d6db3159d8fbd:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/PlanWeekTopComponent.java
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

<<<<<<< HEAD:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/wp/WeekPlanTopComponent.java
        setName(NbBundle.getMessage(WeekPlanTopComponent.class, "CTL_WeekPlanTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setBackground(ViewUtils.COLOR_PANEL_BG);
        setOpaque(true);

        dateDisplayPanel = new DateDisplayPanel(dateCtlr, Period.Week);
        dateDisplayPanel.setOpaque(true);
        dateDisplayPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        dateChangerPanel = new DateChangerPanel(dateCtlr, Period.Week);
        dateChangerPanel.setOpaque(true);
        dateChangerPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        weekDatePanel = new JPanel(new MigLayout("fill", "6[grow]6[]6[]6", "0[]0"));
        weekDatePanel.add(dateDisplayPanel, "align left");
        weekDatePanel.add(getShowDoneButton(), "align right");
        weekDatePanel.add(dateChangerPanel, "align right, wrap");
        weekDatePanel.setOpaque(true);
        weekDatePanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        weekPlanPanel = weekPlanPanelCtlr.getWeekPanel();
        weekPlanPanel.setOpaque(true);
        weekPlanPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        weekPlanPanel.addDayListener(new PropertyChangeListener() {
=======
        CalModel calModel = new CalModelImp();

        ShowHideDoneAction showDoneAction = new ShowHideDoneAction(calModel, DateCtlr.DEFAULT);

        JToggleButton showDoneButton = new JToggleButton(showDoneAction);
        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
        showDoneButton.setPreferredSize(buttonSize);
        showDoneButton.setMinimumSize(buttonSize);
        showDoneButton.setMaximumSize(buttonSize);
        showDoneButton.setText("");
        showDoneButton.setFocusable(false);

        DatePanel datePanel = new DatePanel(DateCtlr.DEFAULT, Period.Week);
        datePanel.setOpaque(true);
        datePanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DateChangePanel dateChangePanel = new DateChangePanel(DateCtlr.DEFAULT, Period.Week);
        dateChangePanel.setOpaque(true);
        dateChangePanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel northPanel = new JPanel(new MigLayout("fill", "6[grow]6[]6[]6", "0[]0"));
        northPanel.add(datePanel, "align left");
        northPanel.add(showDoneButton, "align right");
        northPanel.add(dateChangePanel, "align right, wrap");
        northPanel.setOpaque(true);
        northPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        PlanWeekPanel planPanel = new PlanWeekPanelCtlr(calModel, DateCtlr.DEFAULT).getWeekPanel();
        planPanel.setOpaque(true);
        planPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        planPanel.addDayListener(new PropertyChangeListener() {
>>>>>>> fdaf0cd2bc3b300181fca18eac6d6db3159d8fbd:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/PlanWeekTopComponent.java
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Object newValue = pce.getNewValue();
                if (newValue instanceof Date) {
                    DateCtlr.DEFAULT.setDate((Date) newValue);
                    activateDayView();
                }
            }
        });

<<<<<<< HEAD:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/wp/WeekPlanTopComponent.java
        setLayout(new BorderLayout());
        add(weekDatePanel, BorderLayout.NORTH);
        add(weekPlanPanel, BorderLayout.CENTER);
    }

    private TopComponent[] getDayComponents() {
        final TopComponent[] cs = new TopComponent[7];
        
        for (int i = 0; i < cs.length; i++) {
            cs[i] = ReviewActionsTopComponent.createInstance(ActionsScreen.create("day:" + i));            
        }
        
        return cs;
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
=======
        add(northPanel, BorderLayout.NORTH);
        add(planPanel, BorderLayout.CENTER);
>>>>>>> fdaf0cd2bc3b300181fca18eac6d6db3159d8fbd:modules/au.com.trgtd.tr.view.calendar/src/au/com/trgtd/tr/view/cal/PlanWeekTopComponent.java
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
        DateCtlr.DEFAULT.fireChange();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return "PlanWeekTopComponent";
    }

}

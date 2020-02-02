package au.com.trgtd.tr.view.calendar;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.ctlr.WeekPlanPanelCtlr;
import au.com.trgtd.tr.cal.view.DateChangerPanel;
import au.com.trgtd.tr.cal.view.DateDisplayPanel;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.cal.view.WeekPlanPanel;
import au.com.trgtd.tr.view.ViewUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

public final class WeekPlanTopComponent extends TopComponent {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/calendar/resource/weekplan.png";

    private final TrCalModel calModel = new TrCalModel();
    private final DateCtlr dateCtlr = Singleton.dateCtlr;
    private final WeekPlanPanelCtlr weekPlanPanelCtlr = new WeekPlanPanelCtlr(calModel, dateCtlr);

    private final ShowHideDoneAction showDoneAction = new ShowHideDoneAction(calModel, dateCtlr);

    public WeekPlanTopComponent() {
        setName(NbBundle.getMessage(WeekPlanTopComponent.class, "CTL_WeekPlanTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setBackground(ViewUtils.COLOR_PANEL_BG);
        setLayout(new BorderLayout());
        setOpaque(true);
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        this.initComponents();
    }

    private void initComponents() {
        DateDisplayPanel dateDisplayPanel = new DateDisplayPanel(dateCtlr, Period.Week);
        dateDisplayPanel.setOpaque(true);
        dateDisplayPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DateChangerPanel dateChangerPanel = new DateChangerPanel(dateCtlr, Period.Week);
        dateChangerPanel.setOpaque(true);
        dateChangerPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel northPanel = new JPanel(new MigLayout("fill", "6[grow]6[]6[]6", "0[]0"));
        northPanel.add(dateDisplayPanel, "align left");
        northPanel.add(getShowDoneButton(), "align right");
        northPanel.add(dateChangerPanel, "align right, wrap");
        northPanel.setOpaque(true);
        northPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        WeekPlanPanel weekPlanPanel = weekPlanPanelCtlr.getWeekPanel();
        weekPlanPanel.setOpaque(true);
        weekPlanPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        weekPlanPanel.addDayListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Object newValue = pce.getNewValue();
                if (newValue instanceof Date) {
                    dateCtlr.setDate((Date) newValue);
                    activateDayView();
                }
            }
        });

        add(northPanel, BorderLayout.NORTH);
        add(weekPlanPanel, BorderLayout.CENTER);
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
        return "WeekPlanTopComponent";
    }

}

package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.view.DateChangePanel;
import au.com.trgtd.tr.cal.view.DatePanel;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.view.ViewUtils;
import java.awt.BorderLayout;
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

    public PlanWeekTopComponent() {
        setName(NbBundle.getMessage(PlanWeekAction.class, "CTL_WeekPlanTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setBackground(ViewUtils.COLOR_PANEL_BG);
        setLayout(new BorderLayout());
        setOpaque(true);

        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);

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

        PlanWeekPanel planPanel = new PlanWeekCtlr(DateCtlr.DEFAULT).getWeekPanel();
        planPanel.setOpaque(true);
        planPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        planPanel.addDayListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Object newValue = pce.getNewValue();
                if (newValue instanceof Date) {
                    DateCtlr.DEFAULT.setDate((Date) newValue);
                    activateDayView();
                }
            }
        });

        add(northPanel, BorderLayout.NORTH);
        add(planPanel, BorderLayout.CENTER);
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

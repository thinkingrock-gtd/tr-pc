package au.com.trgtd.tr.view.calendar;

import au.com.trgtd.tr.appl.InitialActionLookup;
import org.openide.modules.ModuleInstall;

/**
 * Module instalation life cycle methods.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        InitialActionLookup.instance().add(new DayViewAction());
        InitialActionLookup.instance().add(new WeekViewAction());
        InitialActionLookup.instance().add(new WeekPlanViewAction());
        InitialActionLookup.instance().add(new MonthViewAction());
        InitialActionLookup.instance().add(new YearViewAction());
    }
}

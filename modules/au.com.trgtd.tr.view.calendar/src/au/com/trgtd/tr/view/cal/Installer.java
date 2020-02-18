package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.appl.InitialActionLookup;
import org.openide.modules.ModuleInstall;

/**
 * Module instalation life cycle methods.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        InitialActionLookup.instance().add(new DayAction());
        InitialActionLookup.instance().add(new WeekAction());
        InitialActionLookup.instance().add(new MonthAction());
        InitialActionLookup.instance().add(new YearAction());
    }
}

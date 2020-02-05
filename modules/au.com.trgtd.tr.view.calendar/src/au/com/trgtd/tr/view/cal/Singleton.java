package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;

/**
 * Singleton to hold a single dateCtlr for the day and week views.
 */
public class Singleton {
    
    public static final DateCtlr dateCtlr = new DateCtlr();
    
    private Singleton() {        
    }
    
}

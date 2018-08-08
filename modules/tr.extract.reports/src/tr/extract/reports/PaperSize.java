package tr.extract.reports;

import org.openide.util.NbBundle;

/**
 * Report paper size.
 * 
 * @author Jeremy Moore
 */
public enum PaperSize {

    A4,
    Letter;

    @Override
    public String toString() {
        return NbBundle.getMessage(PaperSize.class, name());
    }

    public final static String getLabel() {
        return NbBundle.getMessage(PaperSize.class, "PaperSize");
    }
    
}

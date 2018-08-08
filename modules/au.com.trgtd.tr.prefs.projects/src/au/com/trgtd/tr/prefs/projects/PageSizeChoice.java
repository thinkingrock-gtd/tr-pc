package au.com.trgtd.tr.prefs.projects;

import org.openide.util.NbBundle;

/**
 * Page size options.
 */
public enum PageSizeChoice {

    Prompt, A4, Letter;

    @Override
    public String toString() {
        return NbBundle.getMessage(PageSizeChoice.class, name());
    }
}

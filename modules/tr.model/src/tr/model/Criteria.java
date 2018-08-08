
package tr.model;

import org.openide.util.NbBundle;

public enum Criteria {
    
    Time, Energy, Priority;

    @Override
    public String toString() {
        return NbBundle.getMessage(Criteria.class, name());
    }
    
}

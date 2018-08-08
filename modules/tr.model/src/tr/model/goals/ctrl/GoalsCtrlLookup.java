package tr.model.goals.ctrl;

import tr.model.goals.ctrl.impl.GoalsCtrlImpl;

/**
 * Goals controller lookup.
 *
 * @author Jeremy Moore
 */
public class GoalsCtrlLookup {

    /**
     * Gets the goals controller object.
     * @return the goals controller.
     */
    public static GoalsCtrl getGoalsCtrl() {
        return GoalsCtrlImpl.getDefault();
    }

}

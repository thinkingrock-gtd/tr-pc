package tr.model.goals.ctrl;

import tr.model.goals.ctrl.impl.LevelsCtrlImpl;

/**
 * Goal levels controller lookup.
 *
 * @author Jeremy Moore
 */
public class LevelsCtrlLookup {

    /**
     * Gets the goal levels controller object.
     * @return the goal levels controller.
     */
    public static LevelsCtrl getLevelsCtrl() {
        return LevelsCtrlImpl.getDefault();
    }

}

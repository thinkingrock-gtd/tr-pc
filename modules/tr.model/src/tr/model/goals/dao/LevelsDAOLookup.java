package tr.model.goals.dao;

import tr.model.goals.data.LevelsDAOImpl;

/**
 * Levels DAO lookup.
 *
 * @author Jeremy Moore
 */
public class LevelsDAOLookup {

    /**
     * Gets the Levels data access object.
     * @return the levels DAO.
     */
    public static LevelsDAO getLevelsDAO() {
        return LevelsDAOImpl.getDefault();
    }

}

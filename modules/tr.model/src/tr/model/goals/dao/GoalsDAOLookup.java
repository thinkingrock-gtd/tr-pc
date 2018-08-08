package tr.model.goals.dao;

import tr.model.goals.data.GoalsDAOImpl;

/**
 * Goals DAO lookup.
 *
 * @author Jeremy Moore
 */
public class GoalsDAOLookup {

    /**
     * Gets the Goals data access object.
     * @return the goals DAO.
     */
    public static GoalsDAO getGoalsDAO() {
        return GoalsDAOImpl.getDefault();
    }

}

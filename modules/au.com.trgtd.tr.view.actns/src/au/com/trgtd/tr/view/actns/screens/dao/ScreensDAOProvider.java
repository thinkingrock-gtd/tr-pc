package au.com.trgtd.tr.view.actns.screens.dao;

import au.com.trgtd.tr.data.DAOProvider;
import au.com.trgtd.tr.data.DAOProviderLookup;
import au.com.trgtd.tr.view.actns.screens.ActionsScreens;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Actions screens DAO provider.
 *
 * @author Jeremy Moore
 */
public class ScreensDAOProvider implements DAOProvider<ScreensDAO> {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    private static final ScreensDAOProvider instance = new ScreensDAOProvider();
    private ScreensDAO screensDAO;

    /**
     * Gets the singleton instance.
     * @return the instance.
     */
    public static ScreensDAOProvider instance() {
        return instance;
    }

    /* Private singleton constructor. */
    private ScreensDAOProvider() {
        DAOProviderLookup.instance().setDAOProvider(this);
    }

    /**
     * Provide the actions screens DAO.
     * @return the actions screens DAO.
     */
    public ScreensDAO provide() {
        if (screensDAO == null) {
            screensDAO = initialiseScreensDAO();
        }
        return screensDAO;
    }

    private ScreensDAO initialiseScreensDAO() {
        LOG.info("initialiseScreensDAO()");

        ScreensDAO dao = ScreensDAOJAXB.getInstance();

        // use JAXB action screens data if it exists
        if (dao.hasPersistantData()) {
            try {
                dao.restore();                
                return dao;
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Failed to restore review actions settings. {0}", ex.getMessage());                
            }
        }

//        // No JAXB screens meta-data so try to get it from properties.
//        ScreensDAO screensDAOProps = ScreensDAOProperties.getInstance();
//        if (screensDAOProps.hasPersistantData()) {
//            try {
//                dao.getData().setScreens(screensDAOProps.getData().getScreens());
//                return dao;
//            } catch (Exception ex) {
//                LOG.log(Level.SEVERE, "Failed to get review actions settings. {0}", ex.getMessage());
//            }
//        }

        // No JAXB or properties screens meta-data so create default screens meta-data.
        dao.getData().setScreens(ActionsScreens.createDefaultScreens());
        return dao;
    }

    public boolean isInitialised() {
        return screensDAO != null;
    }

    public void reset() {
        screensDAO = null;
    }
}

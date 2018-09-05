package au.com.trgtd.tr.appl;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.LifecycleManager;
import org.openide.windows.WindowManager;

/**
 * Detects user inactivity (in this application) and quits the application if
 * the user is inactive for a specified period of time.
 *
 * @author Jeremy Moore
 */
public final class InactivityDetector {

    private static final Logger LOGGER = Logger.getLogger("tr.inactivity");

    private static Window sWindow;
    private static Action sAction;
    private static InactivityListener sListener;
    private static int sMinutes;

    private static void initListener() {
        if (sAction == null) {
            sAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LOGGER.log(Level.INFO, "{0} Quitting. User inactive for {1} minutes.", new Object[]{new Date(), sMinutes});
                    LifecycleManager.getDefault().exit();
                }
            };
        }
        if (sWindow == null) {
            sWindow = WindowManager.getDefault().getMainWindow();
        }
        sListener = new InactivityListener(sWindow, sAction);
    }

    /**
     * Stop inactivity detecting (if detecting).
     */
    public static void stop() {
        if (sListener != null) {
            sListener.stop();
            sListener = null;
            LOGGER.log(Level.INFO, "{0} Stopped.", new Date());
        }
    }

    /**
     * Start inactivity detection.
     *
     * @param ms The allowed user inactivity time (in milliseconds).
     */
    public static void start(int ms) {
        stop();
        initListener();
        sListener.setInterval(ms);
        sListener.start();
        sMinutes = ms / 60000;
        LOGGER.log(Level.INFO, "{0} Started. User allowed inactivity for {1} minutes.", new Object[]{new Date(), sMinutes});
    }

}

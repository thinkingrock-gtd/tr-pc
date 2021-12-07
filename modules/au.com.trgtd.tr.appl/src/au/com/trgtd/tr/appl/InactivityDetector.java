/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

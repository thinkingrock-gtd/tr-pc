package au.com.trgtd.tr.util.os.time;

import java.awt.Window;

import com.sun.jna.*;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.*;
import java.util.List;

/**
 * Instances of this class provide the computer idle time on a Linux system with
 * X11.
 *
 * @author Laurent Cohen
 */
public class X11LinuxIdleTimeDetector implements IdleTimeDetector {

    /**
     * Structure providing info on the XScreensaver.
     */
    public class XScreenSaverInfo extends Structure {

        /**
         * screen saver window
         */
        public Window window;
        /**
         * ScreenSaver{Off,On,Disabled}
         */
        public int state;
        /**
         * ScreenSaver{Blanked,Internal,External}
         */
        public int kind;
        /**
         * milliseconds
         */
        public NativeLong til_or_since;
        /**
         * milliseconds
         */
        public NativeLong idle;
        /**
         * events
         */
        public NativeLong event_mask;

        @Override
        protected List getFieldOrder() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Definition (incomplete) of the Xext library.
     */
    public interface Xss extends Library {

        /**
         * Instance of the Xext library bindings.
         */
        Xss INSTANCE = (Xss) Native.loadLibrary("Xss", Xss.class);

        /**
         * Allocate a XScreensaver information structure.
         *
         * @return a {@link XScreenSaverInfo} instance.
         */
        XScreenSaverInfo XScreenSaverAllocInfo();

        /**
         * Query the XScreensaver.
         *
         * @param display the display.
         * @param drawable a {@link Drawable} structure.
         * @param saver_info a previously allocated {@link XScreenSaverInfo}
         * instance.
         * @return an integer return code.
         */
        int XScreenSaverQueryInfo(Display display, Drawable drawable, XScreenSaverInfo saver_info);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSystemIdleTime() {
        X11.Window window = null;
        XScreenSaverInfo info = null;
        Display display = null;

        long idleMillis = 0L;
        try {
            display = X11.INSTANCE.XOpenDisplay(null);
            window = X11.INSTANCE.XDefaultRootWindow(display);
            // info = Xss.INSTANCE.XScreenSaverAllocInfo();
            info = new XScreenSaverInfo();
            Xss.INSTANCE.XScreenSaverQueryInfo(display, window, info);
            idleMillis = info.idle.longValue();
        } finally {
            // if (info != null) X11.INSTANCE.XFree(info.getPointer());
            info = null;

            if (display != null) {
                X11.INSTANCE.XCloseDisplay(display);
            }
            display = null;
        }
        return idleMillis;
    }
}

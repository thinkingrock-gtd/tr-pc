package au.com.trgtd.tr.util;

import au.com.trgtd.tr.util.os.time.IdleTimeDetector;
import au.com.trgtd.tr.util.os.time.MacOSXIdleTimeDetector;
import au.com.trgtd.tr.util.os.time.WindowsIdleTimeDetector;
import au.com.trgtd.tr.util.os.time.X11LinuxIdleTimeDetector;
import org.openide.util.Utilities;

/**
 * Operating system utilities.
 *
 * @author Jeremy Moore
 */
public final class UtilsOS {

    private UtilsOS() {
    }

    private static IdleTimeDetector sIdleTimeDetector;

    private static IdleTimeDetector getIdleTimeDetector() {
        if (sIdleTimeDetector == null) {
            if (isLinux()) {
                sIdleTimeDetector = new X11LinuxIdleTimeDetector();
            } else if (isMac()) {
                sIdleTimeDetector = new MacOSXIdleTimeDetector();
            } else if (isMac()) {
                sIdleTimeDetector = new WindowsIdleTimeDetector();                                
            }
        }
        return sIdleTimeDetector;
    }
    
    public static long getIdleMs() {
        IdleTimeDetector detector = getIdleTimeDetector();
        return detector == null ? 0 : detector.getSystemIdleTime();
    }

    public static boolean isLinux() {
        return Utilities.getOperatingSystem() == Utilities.OS_LINUX;
    }

    public static boolean isMac() {
        return Utilities.isMac();
    }

    public static boolean isWindows() {
        return Utilities.isWindows();
    }


}

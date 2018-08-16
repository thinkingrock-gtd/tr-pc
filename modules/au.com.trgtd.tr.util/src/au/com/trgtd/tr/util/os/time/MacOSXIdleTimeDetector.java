package au.com.trgtd.tr.util.os.time;

import com.sun.jna.*;

public class MacOSXIdleTimeDetector implements IdleTimeDetector {

    public interface ApplicationServices extends Library {

        ApplicationServices INSTANCE = (ApplicationServices) Native.loadLibrary("ApplicationServices", ApplicationServices.class);

        int kCGAnyInputEventType = ~0;
        int kCGEventSourceStatePrivate = -1;
        int kCGEventSourceStateCombinedSessionState = 0;
        int kCGEventSourceStateHIDSystemState = 1;

        public double CGEventSourceSecondsSinceLastEventType(int sourceStateId, int eventType);
    }

    // Returns the system idle time in milliseconds
    @Override
    public long getSystemIdleTime() {
        double idleTimeSeconds = ApplicationServices.INSTANCE.CGEventSourceSecondsSinceLastEventType(ApplicationServices.kCGEventSourceStateCombinedSessionState, ApplicationServices.kCGAnyInputEventType);
        return (long) (idleTimeSeconds * 1000);
    }
}

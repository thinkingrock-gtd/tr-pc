package au.com.trgtd.tr.runtime;

import java.io.IOException;
import org.openide.util.Utilities;

/**
 * Runtime.exec wrapper.
 */
public class Runtime {
    
    private static final String OS_NAME = System.getProperty("os.name" ).toLowerCase();
    
    /** Determines whether or not the system is Linux. */
    public static final boolean LINUX = Utilities.getOperatingSystem() == Utilities.OS_LINUX;
    /** Determines whether or not the system is Mac OS X. */
    public static final boolean MAC = Utilities.getOperatingSystem() == Utilities.OS_MAC;
    /** Determines whether or not the system is Windows 95. */
    public static final boolean WIN95 = Utilities.getOperatingSystem() == Utilities.OS_WIN95;
    /** Determines whether or not the system is Windows 95. */
    public static final boolean WIN98 = Utilities.getOperatingSystem() == Utilities.OS_WIN98;
    /** Determines whether or not the system is any Windows. */
    public static final boolean WINDOWS = Utilities.isWindows();
    /** Determines whether or not the system is any Unix. */
    public static final boolean UNIX = Utilities.isUnix();    
    
    /**
     * Execute a runtime command.
     * @param cmd The command.
     */
    public static void exec(String[] cmd) {
        try {
            Process process = java.lang.Runtime.getRuntime().exec(cmd);
            
            StreamConsumer stderrConsumer = new StreamConsumer(process.getErrorStream());
            StreamConsumer stdoutConsumer = new StreamConsumer(process.getInputStream());
            
            stderrConsumer.start();
            stdoutConsumer.start();
            
//          int exitValue = process.waitFor();
            process.waitFor();
            
            stderrConsumer.terminate();
            stdoutConsumer.terminate();

        } catch (IOException throwable) {
            throwable.printStackTrace(System.err);
        } catch (InterruptedException throwable) {
            throwable.printStackTrace(System.err);
        }
    }
    
}

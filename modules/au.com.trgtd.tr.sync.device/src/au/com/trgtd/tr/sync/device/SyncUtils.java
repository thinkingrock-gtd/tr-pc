package au.com.trgtd.tr.sync.device;

public class SyncUtils {

    public static synchronized String escape(String str) {
        return str == null ? "" : str.replace("/", "/1").replace("|", "/2");
    }

    public static synchronized String unEscape(String str) {
        return str == null ? "" : str.replace("/2", "|").replace("/1", "/");
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }
    
}

package au.com.trgtd.tr.sync.iphone;

import java.util.logging.Logger;

public class SyncUtils {

    public static final Logger LOG = Logger.getLogger("tr.iPhone.sync");
    public static final String ETX = "\u0003";
    
    public static synchronized String escape(String str) {
        return str == null ? "" : str.replace("/", "/1").replace("|", "/2");
    }

    public static synchronized String unEscape(String str) {
      return str == null ? "" : str.replace("/2", "|").replace("/1", "/");
    }

    public static synchronized String getFieldValue(String field, String string) {
        int p0 = string.indexOf("|" + field + "|");
        if (p0 > -1) {
            int p1 = p0 + field.length() + 2;
            int p2 = string.indexOf("|", p1);
            if (p2 > p1) {
                return unEscape(string.substring(p1, p2));
            }
        }
        return "";
    }

}

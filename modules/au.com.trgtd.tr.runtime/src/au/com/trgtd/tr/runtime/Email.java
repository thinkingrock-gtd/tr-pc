package au.com.trgtd.tr.runtime;

import java.net.URLEncoder;

/**
 * Runtime command for sending an email.
 *
 * @author Jeremy Moore
 */
public class Email {

    /* Private constructor to prevent instantiation. */
    private Email() {
    }

    /**
     * Send an email using the runtime exec facility.
     * @param to The email address to send to.
     * @param subject The email subject.
     * @param body The email body.
     * @param encoding The encoding.
     */
    public static final void email(String to, String subject, String body, String encoding) {

        final String encTo = urlEncodeAndFilter(to, encoding);
        final String encSubject = urlEncodeAndFilter(subject, encoding);
        final String encBody = urlEncodeAndFilter(body, encoding);
        
        if (Runtime.MAC) {
//          Runtime.exec(new String[]{"open", "mailto:" + encTo + "?subject=" + encSubject + "&body=" + encBody});
            Runtime.exec(new String[]{"open", "mailto:" + to + "?subject=" + subject + "&body=" + body});
        } else if (Runtime.LINUX || Runtime.UNIX) {
            String[] args = new String[]{"xdg-email", "--utf8", "--subject", subject, "--body", body, "mailto:" + to};
            Runtime.exec(args);
        } else if (Runtime.WIN95 || Runtime.WIN98) {
            Runtime.exec(new String[]{"command.com", "/C", "start", "mailto:" + encTo + "?subject=" + encSubject + "\"&\"body=" + encBody});
        } else if (Runtime.WINDOWS) {
            Runtime.exec(new String[]{"cmd.exe", "/C", "start", "mailto:" + encTo + "?subject=" + encSubject + "\"&\"body=" + encBody});
        } else {
            Runtime.exec(new String[]{"email", "mailto:" + encTo + "?subject=" + encSubject + "&body=" + encBody});
        }
    }

    private static String urlEncodeAndFilter(final String str, String encoding) {
        try {
            String out = URLEncoder.encode(str, encoding);
            return out.replaceAll("\\+", "%20").replaceAll("\\%0A", "%0D%0A");
        } catch (Exception x) {
            return str.replaceAll("\\+", "%20").replaceAll("\\%0A", "%0D%0A");
        }
    }

}

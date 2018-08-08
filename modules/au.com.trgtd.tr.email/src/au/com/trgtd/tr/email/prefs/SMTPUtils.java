package au.com.trgtd.tr.email.prefs;

import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

public class SMTPUtils {
    /**
     * Verify SMTP connection.
     * @param host The SMTP host.
     * @param port The SMTP port.
     * @param user The login username.
     * @param pass The login password.
     * @param ssl Whether or not the use SSL.
     * @return true if successful, false otherwise.
     */
    public static boolean verify(String host, int port, String user, String pass, boolean ssl) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", ssl);
            props.put("mail.smtp.starttls.enable", ssl);            
            props.put("mail.smtp.connectiontimeout", "20000");
            props.put("mail.smtp.timeout", "20000");            
            if (ssl) {
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            } 
            Session session = Session.getInstance(props, null);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, user, pass);
            transport.close();
            return true;
        } catch (AuthenticationFailedException e) {
            return false;
        } catch (MessagingException e) {
            return false;
        }
    }

}

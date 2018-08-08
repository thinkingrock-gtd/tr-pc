package tr.model.util.delegation;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.netbeans.api.progress.*;
import org.openide.util.NbBundle;

/**
 * Class to send an email directly to the SMTP server.
 *
 * @author Jeremy Moore
 */
public class SMTPSender {

    private final static Logger LOG = Logger.getLogger("tr.model.util");

    /**
     * Determine whether SMTP preferences are valid.
     *
     * @return true if SMTP host, user and password preferences are set.
     */
    public static boolean isValidSMTPPreferences() {
        SMTP smtp = EmailPrefs.getSMTP();
        return smtp != null && smtp.valid();
    }

    /**
     * Send an email to the SMTP server.
     *
     * @param to The email to address
     * @param subject The email subject.
     * @param body The email body.
     */
    public static synchronized void send(String to, String subject, String body) {
        Thread thread = new Thread("SMTP sender") {
            @Override
            public void run() {
                new SMTPSender(to, subject, body).process();
            }
        };
        thread.start();
    }

    private final String to;
    private final String subject;
    private final String body;
    private ProgressHandle progressHandle;
    
    /**
     * Create a new instance for a delegation action.
     *
     * @param delegationAction The delegation action.
     */
    private SMTPSender(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    private void process() {

        SMTP smtp = EmailPrefs.getSMTP();
        if (smtp == null) {
            LOG.info("No valid Email SMTP preferences.");
            return;
        }
        if (!smtp.valid()) {
            LOG.info("No valid Email SMTP preferences.");
            return;
        }
        String from = EmailPrefs.getEmailAddress();
        if (StringUtils.isBlank(from)) {
            LOG.info("No valid Email preferences.");
            return;
        }

        try {
            progressStart(5);
            progress(1);
            progress(2);
            progress(3);
            SimpleEmail email = new SimpleEmail();
            email.setHostName(smtp.host);
            email.setSmtpPort(smtp.port);
            email.setAuthenticator(new DefaultAuthenticator(smtp.user, smtp.pass));
            email.setSSLOnConnect(smtp.ssl);
            email.setFrom(from);
            email.setSubject(subject);
            email.setMsg(body);
            email.addTo(to);
            email.send();
            progress(4);
            progress(5);
            progressFinish();
            LOG.log(Level.INFO, "Sent email directly to SMTP server.");
        } catch (EmailException e) {
            LOG.log(Level.SEVERE, "Failed to send email. Check Email SMTP settings.");
        }
    }

    private void progressStart(int units) {
        progressHandle = ProgressHandle.createHandle(NbBundle.getMessage(SMTPSender.class, "sending.email"));        
        progressHandle.start(units);
        sleep(400);        
    }
    
    private void progress(int units) {
        progressHandle.progress(units);
        sleep(400);        
    }
    
    private void progressFinish() {
        progressHandle.finish();
        sleep(500);
    }

    private void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

}

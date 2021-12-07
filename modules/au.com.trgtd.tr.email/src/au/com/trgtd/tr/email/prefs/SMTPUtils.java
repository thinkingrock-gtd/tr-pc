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

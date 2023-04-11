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
package au.com.trgtd.tr.email;

import au.com.trgtd.tr.email.prefs.EmailPrefs;
import au.com.trgtd.tr.email.utils.Attachment;
import au.com.trgtd.tr.email.utils.Renderable;
import au.com.trgtd.tr.email.utils.RenderableFactory;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.UtilsFile;
import com.sun.mail.pop3.POP3Folder;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import org.apache.commons.lang3.StringUtils;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.thought.Thought;
import tr.model.util.delegation.DelegationData;
import static tr.model.util.delegation.DelegationData.Type.DELEGATION;
import static tr.model.util.delegation.DelegationData.Type.RESPONSE;
import tr.model.util.delegation.DelegationUtils;

/**
 * Email.
 *
 * @author Jeremy Moore
 */
public class Email {

    private static final Logger LOG = Logger.getLogger("tr.email");

    // Suppress default constructor for noninstantiability
    private Email() {
        throw new AssertionError();
    }

    public static void retrieve() {

        final Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        final EmailUtils email = new EmailUtils();

        if (EmailPrefs.isKeepOnServer()) {
            fetchEmailKeepOnServer(data, email);
        } else {
            fetchMailDeleteFromServer(data, email);
        }
    }

    private static void fetchMailDeleteFromServer(Data data, EmailUtils email) {

        try {
            LOG.log(Level.INFO, "Starting email fetch at: {0}", new Date());

            String username = EmailPrefs.getEmailUsername();
            String password = EmailPrefs.getEmailPassword();
            String popserver = EmailPrefs.getEmailServer();
            int port = EmailPrefs.getPort();
            boolean ssl = EmailPrefs.isSSL();
            String sslProtocols = EmailPrefs.getSslProtocols();

            LOG.log(Level.INFO, "Username:{0}", username);
            LOG.log(Level.INFO, "POP Server:{0}", popserver);
            LOG.log(Level.INFO, "Port:{0}", port);
            LOG.log(Level.INFO, "SSL:{0}", ssl);
            LOG.log(Level.INFO, "Protocols:{0}", sslProtocols);

            email.setUserPass(username, password);
            email.setServer(popserver);
            email.setPort(port);
            email.setUseSSL(ssl, sslProtocols);
            email.connect();
            email.openFolder("INBOX", true);

            LOG.log(Level.INFO, "Total messages: {0}", email.getMessageCount());

            for (Message msg : email.getNewMessages()) {
                Renderable r = RenderableFactory.create(msg);
                if (r != null) {
                    String descr = r.getSubject() == null ? "" : r.getSubject().trim();
                    String notes = r.getBodytext() == null ? "" : r.getBodytext().trim();
                    if (EmailPrefs.isSaveAttachments()) {
                        notes = saveAttachments(notes, r);
                    }
                    createThought(data, descr, notes);
                }
                LOG.info("Deleting message from server.");
                email.delete(msg);
            }

            email.closeFolder(true);

            LOG.log(Level.INFO, "Finished email fetch at: {0}", new Date());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                email.disconnect();
            } catch (Exception e) {
            }
        }

    }

    private static void fetchEmailKeepOnServer(Data data, EmailUtils email) {
        try {
            LOG.log(Level.INFO, "Starting email fetch at: {0}", new Date());

            String username = EmailPrefs.getEmailUsername();
            String password = EmailPrefs.getEmailPassword();
            String popserver = EmailPrefs.getEmailServer();
            int port = EmailPrefs.getPort();
            boolean ssl = EmailPrefs.isSSL();
            String sslProtocols = EmailPrefs.getSslProtocols();

            LOG.log(Level.INFO, "Username:{0}", username);
            LOG.log(Level.INFO, "POP Server:{0}", popserver);
            LOG.log(Level.INFO, "Port:{0}", port);
            LOG.log(Level.INFO, "SSL:{0}", ssl);
            LOG.log(Level.INFO, "Protocols:{0}", sslProtocols);

            email.setUserPass(username, password);
            email.setServer(popserver);
            email.setPort(port);
            email.setUseSSL(ssl, sslProtocols);
            email.connect();

            Folder folder = email.openFolder("INBOX", false);
            if (!folder.isOpen()) {
                LOG.info("Could not open INBOX folder.");
                return;
            }

            LOG.log(Level.INFO, "Total messages: {0}", email.getMessageCount());

            POP3Folder pop3folder = null;
            if (folder instanceof POP3Folder p3f) {
                pop3folder = p3f;
                LOG.info("POP3 Folder");
            } else {
                LOG.info("Not POP3 Folder");
            }

            List<Message> msgs = new ArrayList<>(email.getMessageCount());
            String lastUID = EmailPrefs.getLastMsgUID();
            boolean lookForLast = (lastUID != null && pop3folder != null);
            int lastUIDIndex = -1;

            for (Message msg : email.getNewMessages()) {
                if (msg == null) {
                    continue;
                }

                msgs.add(msg);
                if (lookForLast) {
                    @SuppressWarnings("null")
                    String msgUID = pop3folder.getUID(msg);
                    if (msgUID != null && msgUID.equals(lastUID)) {
                        lastUIDIndex = msgs.size() - 1;
                        lookForLast = false;
                        LOG.log(Level.INFO, "Found last message previously read at index: {0}", lastUIDIndex);
                    }
                }
            }

            int startIndex = (lastUIDIndex == -1 ? 0 : lastUIDIndex + 1);
            for (int i = startIndex; i < msgs.size(); i++) {

                Renderable r = RenderableFactory.create(msgs.get(i));
                if (r != null) {
                    String descr = (r.getSubject() == null ? "" : r.getSubject().trim());
                    String notes = (r.getBodytext() == null ? "" : r.getBodytext().trim());
                    if (EmailPrefs.isSaveAttachments()) {
                        notes = saveAttachments(notes, r);
                    }
                    createThought(data, descr, notes);
                }
            }

            if (!msgs.isEmpty() && pop3folder != null) {
                String newLastUID = pop3folder.getUID(msgs.get(msgs.size() - 1));
                EmailPrefs.setLastMsgUID(newLastUID);
            }

            email.closeFolder(false);

            LOG.log(Level.INFO, "Finished email fetch at: {0}", new Date());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                email.disconnect();
            } catch (Exception e) {
            }
        }
    }

    // mantis #1489 - save attachments
    private static String saveAttachments(String notes, Renderable renderable) throws Exception {
        if (renderable.getAttachmentCount() < 1) {
            return notes;
        }
        File attachDir = new File(EmailPrefs.getPathAttachments());
        if (!attachDir.isDirectory()) {
            LOG.warning("Email attachments folder is not valid.");
            return notes;
        }
        StringBuilder sbNotes = new StringBuilder(notes);
        if (notes.length() > 0) {
            sbNotes.append("\n");
        }
        for (int i = 0; i < renderable.getAttachmentCount(); i++) {
            Attachment attach = renderable.getAttachment(i);

            String filename = attach.getFilename();
            File file = new File(attachDir, filename);

            String name = UtilsFile.removeExtension(filename);
            String extn = UtilsFile.getExtension(filename);
            int n = 1;
            while (file.exists()) {
                file = new File(attachDir, name + "-" + (n++) + "." + extn);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(attach.getContent());
            }

            String url = file.toURL().toExternalForm();
            sbNotes.append("[").append(url).append("|").append(file.getName()).append("]\n");
        }
        return sbNotes.toString();
    }

    final static String HAND = "\u261E";
    final static String TODO = "\u00D7";
    final static String DONE = "\u2713";

    private static void createThought(Data data, String subject, String body) {

        DelegationData dd = parseDelegatedData(body);
        if (dd == null) {
            LOG.info("Normal email.");
            Thought thought = new Thought(data.getNextID());
            thought.setDescription(subject);
            thought.setNotes(body);
            data.getThoughtManager().add(thought);
            return;
        }

        body = DelegationUtils.removeSerializedStrings(body);

        if (dd.type == DELEGATION) {
            LOG.info("Delegation email.");
            // Create and attach a new action to store delegated action details.
            // Note that the delegated action ID is stored in the thought.
            Action action = new Action(data);
            action.setDescription(subject);
            action.setNotes(dd.reply + "\n\n" + body);
            action.setSuccess(dd.success);
            action.setDueDate(dd.due);
            action.setStartDate(dd.start);
            action.setTopic(Services.instance.getTopicByName(dd.topic));
            action.setTime(Services.instance.getTimeByName(dd.time));
            action.setEnergy(Services.instance.getEnergyByName(dd.energy));
            action.setPriority(Services.instance.getPriorityByName(dd.priority));
            Thought thought = new Thought(data.getNextID());
            thought.setDescription(HAND + " " + subject);
            thought.setNotes(dd.reply + "\n\n" + body);
            thought.setTopic(action.getTopic());
            thought.setDelegationType(DELEGATION);
            thought.setDelegationReply(dd.reply);
            thought.setDelegationActionID(dd.id);
            thought.setAction(action);
            data.getThoughtManager().add(thought);
        } else if (dd.done != null) {
            LOG.info("Delegation done response email.");
            Thought thought = new Thought(data.getNextID());
            thought.setDescription(HAND + " " + DONE + " " + subject);
            thought.setNotes(body);
            thought.setDelegationType(RESPONSE);
            thought.setDelegationDone(dd.done);
            thought.setDelegationReply(dd.reply);
            thought.setDelegationActionID(dd.id);
            data.getThoughtManager().add(thought);
        } else {
            LOG.info("Delegation not done response email.");
            Thought thought = new Thought(data.getNextID());
            thought.setDescription(HAND + " " + TODO + " " + subject);
            thought.setNotes(body);
            thought.setDelegationType(RESPONSE);
            thought.setDelegationDone(null);
            thought.setDelegationReply(dd.reply);
            thought.setDelegationActionID(dd.id);
            data.getThoughtManager().add(thought);
        }
    }

    private static DelegationData parseDelegatedData(String body) {
        String ss = DelegationUtils.extractSerializedString(body);
        return ss == null
                ? null
                : DelegationUtils.deserialize(stripNonPrintableChars(ss));
    }

    private static String stripNonPrintableChars(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int offset = 0; offset < s.length();) {
            int codePoint = s.codePointAt(offset);
            // Replace invisible control characters and unused code points
            switch (Character.getType(codePoint)) {
                case Character.CONTROL:     // \p{Cc}
                case Character.FORMAT:      // \p{Cf}
                case Character.PRIVATE_USE: // \p{Co}
                case Character.SURROGATE:   // \p{Cs}
                case Character.UNASSIGNED:  // \p{Cn}
                    sb.append(' ');
                    break;
                default:
                    sb.append(Character.toChars(codePoint));
                    break;
            }
            offset += Character.charCount(codePoint);
        }
        return StringUtils.normalizeSpace(sb.toString());
    }

}

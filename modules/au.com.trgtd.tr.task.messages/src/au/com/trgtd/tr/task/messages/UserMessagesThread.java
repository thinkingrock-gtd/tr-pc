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
package au.com.trgtd.tr.task.messages;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.task.messages.MessageParser.Message;
import au.com.trgtd.tr.runtime.Open;
import java.util.ArrayList;

public class UserMessagesThread extends Thread {

    private static final Logger LOG = Logger.getLogger("tr.messages");

    private final VersionChecker versionChecker;
    private final MessageChecker messageChecker;
    private final List<Message> messages;

    public UserMessagesThread() {
        versionChecker = new VersionChecker();
        messageChecker = new MessageChecker();
        messages = new ArrayList<>();
    }

    /**
     * Runs the version check thread.
     */
    @Override
    public void run() {
        LOG.info("Starting");
        process();
        LOG.info("Finished");
    }

    private void process() {

        if (versionChecker.shouldCheckVersion()) {
            try {
                Message message = versionChecker.getNewVersionMessage();
                if (message != null) {
                    messages.add(message);
                }
                versionChecker.setLastCheckDate();
            } catch (Exception ex) {
            }
        }

        if (messageChecker.shouldCheckMessages()) {
            try {
                messages.addAll(messageChecker.getMessages());
                messageChecker.setLastCheckDate();
            } catch (Exception ex) {
            }
        }

        if (!messages.isEmpty()) {
            notifyUser(messages);
        }

    }
    
    private void notifyUser(List<Message> msgs) {
        if (msgs == null || msgs.isEmpty()) {
            return;
        }
        Collections.sort(msgs);
        StringBuilder sb = new StringBuilder();
        sb.append("<HTML><BODY>");
        for (Message msg : msgs) {
            sb.append("<P>");
            sb.append(msg.text);
            if (msg.href.length() > 0) {
                sb.append(" ");
                sb.append("<A href='").append(msg.href).append("'>");
                sb.append(NbBundle.getMessage(getClass(), "messages.details"));
                sb.append("</A>");
            }
            sb.append("</P>");
        }
        sb.append("</BODY></HTML>");

        String t = NbBundle.getMessage(getClass(), "messages.heading");
        String m = sb.toString();

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(m);
        editorPane.setEditable(false);
        editorPane.setOpaque(true);
        editorPane.addHyperlinkListener((HyperlinkEvent evt) -> {
            if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                if (evt.getURL() != null) {
                    Open.open(evt.getURL());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        Object[] options = {scrollPane};

        JOptionPane.showOptionDialog(null, options, t, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

}

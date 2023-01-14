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

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.prefs.ApplicationPrefs;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.io.IOException;
import java.util.logging.Level;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Message check thread.
 *
 * @author Jeremy Moore
 */
final class MessageChecker {
    
    private static final Logger LOG = Logger.getLogger("tr.messages");
    private static final int TIMEOUT = 10000;

    /**
     * Determines whether it is time to check for new messages.
     * @return true if it is time to check, otherwise false.
     */
    public boolean shouldCheckMessages() {
        return getLastCheckDate().before(getOneWeekAgoDate());
    }

    public List<Message> getMessages() throws Exception {
        MessageParser parser = new MessageParser();
        return parser.parse(getMessagesFromWebSite(), getLastCheckDate(), isMember());
    }

    public void setLastCheckDate() {
        ApplicationPrefs.setMessageCheckDate(new Date());
        LOG.info("Updated last message check date");
    }

    private Date getLastCheckDate() {
        Date date = ApplicationPrefs.getLastMessageCheckDate();
        LOG.log(Level.INFO, "Last message check date: {0}", date);
        return date;
    }

    private Date getOneWeekAgoDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return calendar.getTime();
    }

    private boolean isMember() {
//        return MemberPrefs.getMemberUsername().length() > 0
//            && MemberPrefs.getMemberPassword().length() > 0;
        return true; // assume all 3.0.x users are members
    }

    @NonNull
    private String getMessagesFromWebSite() throws Exception {
        try {
            URL url = new URL(Constants.MESSAGES_FILE_URL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(TIMEOUT);
            InputStream in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));            
            StringBuilder sb = new StringBuilder();
            String s = br.readLine();
            while (s != null) {
                sb.append(s);
                s = br.readLine();
            }
            return sb.toString();
        } catch (IOException ex) {
            LOG.log(Level.INFO, "Exception getting messages. \n{0}", ex.getMessage());
            throw ex;
        }
    }
    
}

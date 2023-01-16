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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.netbeans.api.annotations.common.NonNull;
import org.w3c.dom.*;

public class MessageParser {

    private static final DateFormat DF = new SimpleDateFormat("yyyy/MM/dd");

    public static class Message implements Comparable<Message> {

        public final Date date;
        public final String type;
        public final String href;
        public final String text;

        public Message(Date date, String type, String href, String text) {
            this.date = date;
            this.type = type.toLowerCase();
            this.href = href;
            this.text = text;
        }

        @Override
        public int compareTo(Message other) {
            int c = date.compareTo(other.date);
            return c != 0 ? c : type.compareTo(other.type);
        }
    }

    @NonNull
    public List<Message> parse(@NonNull String str, @NonNull Date from, boolean member) throws Exception {
        List<Message> messages = new ArrayList<>();

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        InputStream input = new ByteArrayInputStream(str.getBytes("UTF-8"));
        Document doc = docBuilder.parse(input);
        doc.getDocumentElement().normalize();

        NodeList msgNodes = doc.getElementsByTagName("msg");
        for (int i = 0; i < msgNodes.getLength(); i++) {

            Node msgNode = msgNodes.item(i);
            if (msgNode.getNodeType() == Node.ELEMENT_NODE) {

                Element msgElement = (Element) msgNode;
                Date date = DF.parse(msgElement.getAttribute("date"));
                if (date.before(from)) {
                    continue;
                }
                String type = msgElement.getAttribute("type").toLowerCase();
                if (type.equalsIgnoreCase("N") && member) {
                    continue;
                }
                if (type.equalsIgnoreCase("M") && !member) {
                    continue;
                }
                String href = msgElement.getAttribute("href").trim();
                String text = msgElement.getTextContent().trim();

//              System.out.println("Message: date=" + date + " type=" + type + " href=" + href + " text=" + text);
                messages.add(new Message(date, type, href, text));
            }
        }

        return messages;
    }

}

package au.com.trgtd.tr.task.messages;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public class MessageParser {

    private static final DateFormat DF = new SimpleDateFormat("yyyy/MM/dd");

    public static class Message implements Comparable<Message>{
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

    public List<Message> parse(String str, Date from, boolean member) throws Exception {
        List<Message> messages = new ArrayList<Message>();

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

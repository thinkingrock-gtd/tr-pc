package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class MessageParserTest {

    private MessageParser parser = new MessageParser();
    private LocalDate localDate = LocalDate.of(2014, Month.MARCH, 26);

    final String xml1 = "<msgs>"
            + "<msg date='2014/03/27' type='A'>message1</msg>"
            + "<msg date='2014/03/30' type='B'>message2</msg>"
            + "</msgs>";

    final String xml2 = "<msgs>"
            + "<msg date='2014/03/27' type='N'>nonMemberMessage</msg>"
            + "<msg date='2014/03/30' type='M'>memberMessage</msg>"
            + "</msgs>";

    @Test
    public void parsingValidButEmptyXml_returnsNoMessages() throws Exception {
        assertTrue(parse("<msgs/>", localDate).isEmpty());
    }

    @Test
    public void parsingXml1_withDateBeforeBoth_returnsBothMessages() throws Exception {
        List<Message> messages = parse(xml1, localDate);
        assertTrue(messages.size() == 2);
        assertMessage(messages.get(0), LocalDate.of(2014, Month.MARCH, 27), "a", "message1");
        assertMessage(messages.get(1), LocalDate.of(2014, Month.MARCH, 30), "b", "message2");
    }

    @Test
    public void parsingXml1_withDateAfterFirstButBeforeSecond_returnsNewMessage() throws Exception {
        List<Message> messages = parse(xml1, localDate.plusDays(2));
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), LocalDate.of(2014, Month.MARCH, 30), "b", "message2");
    }

    @Test
    public void parsingXml2asMember_returnsMemberMsg() throws Exception {
        Boolean isMember = true;
        List<Message> messages = parse(xml2, localDate, isMember);
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), LocalDate.of(2014, Month.MARCH, 30), "m", "memberMessage");
    }

    @Test
    public void parsingXml2asNonMember_returnsNonMemberMsg() throws Exception {
        Boolean isMember = false;
        List<Message> messages = parse(xml2, localDate, isMember);
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), LocalDate.of(2014, Month.MARCH, 27), "n", "nonMemberMessage");
    }

    @Test
    public void parseFile() throws Exception {
        Path path = Paths.get(getClass().getResource("messages.xml").getPath());
        String xml = new String(Files.readAllBytes(path));
        List<Message> messages = parse(xml, localDate);
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), LocalDate.of(2014, Month.MARCH, 27), "a", null);
        String text = messages.get(0).text;
        assertEquals(431, text.length());
        assertTrue(text.contains("ThinkingRock"));
    }

    @Test
    public void parsingNull_throws() throws Exception {
        assertThrows(null);
    }

    @Test
    public void parsingInvalidEmptyXml_throws() throws Exception {
        assertThrows("");
    }

    @Test
    public void parsingInvalidXml1_throws() throws Exception {
        assertThrows("<msgs>");
    }

    @Test
    public void parsingInvalidXml2_throws() throws Exception {
        assertThrows("<msgs><msg xx</msgs>");
    }

    private void assertThrows(String xml) {
        try {
            parse(xml, localDate);
            fail("Should have thrown");
        } catch (Exception ex) {
            // Exception successfully thrown
        }
    }

    private void assertMessage(Message m, LocalDate localDate, String type, String msg) {
        assertMessage(m, localDate, type, msg, "");
    }

    private void assertMessage(Message m, LocalDate localDate, String type, String msg, String href) {
        assertEquals(Date.valueOf(localDate), m.date);
        assertEquals(type, m.type);
        if (msg != null) {
            assertEquals(msg, m.text);
        }
        assertEquals(href, m.href);
    }

    private List<Message> parse(String xml, LocalDate localDate) throws Exception {
        return parse(xml, localDate, true);
    }

    private List<Message> parse(String xml, LocalDate localDate, Boolean isMember) throws Exception {
        return parser.parse(xml, Date.valueOf(localDate), isMember);
    }
}

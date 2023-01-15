package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.io.IOException;
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

    // The message text stored in the sample messages.xml (pulled from the
    // https://trgtd.com.au/messages.xml on 2023-01-14) is too long and
    // cumbersome to validate verbatim.Simply verifying the length instead.
    private static final int LENGTH_OF_SAMPLE_MESSAGE_TEXT = 431;

    private final MessageParser parser = new MessageParser();

    private final LocalDate dateMessage1 = LocalDate.of(2014, Month.MARCH, 27);
    private final LocalDate dateMessage2 = LocalDate.of(2014, Month.MARCH, 30);
    private final LocalDate dateBeforeMessages = LocalDate.of(2014, Month.MARCH, 26);

    // valid XML with two member-independent messages of different dates
    private final String sampleMessagesA = "<msgs>"
            + "<msg date='2014/03/27' type='A'>message1</msg>"
            + "<msg date='2014/03/30' type='B'>message2</msg>"
            + "</msgs>";

    // valid XML with two messages - each intended for either members or non-members.
    // the first for non-members (type='N'), the second for members (type = 'M')
    private final String sampleMessagesB = "<msgs>"
            + "<msg date='2014/03/27' type='N'>nonMemberMessage</msg>"
            + "<msg date='2014/03/30' type='M'>memberMessage</msg>"
            + "</msgs>";

    @Test
    public void parsingValidButEmptyXml_returnsNoMessages() throws Exception {
        assertTrue(parse("<msgs/>", dateBeforeMessages).isEmpty());
    }

    @Test
    public void parsingXml1_withDateBeforeBoth_returnsBothMessages() throws Exception {
        List<Message> messages = parse(sampleMessagesA, dateBeforeMessages);
        assertTrue(messages.size() == 2);
        assertMessageA1(messages.get(0));
        assertMessageA2(messages.get(1));
    }

    @Test
    public void parsingXml1_withDateAfterFirstButBeforeSecond_returnsNewMessage() throws Exception {
        List<Message> messages = parse(sampleMessagesA, dateBeforeMessages.plusDays(2));
        assertTrue(messages.size() == 1);
        assertMessageA2(messages.get(0));
    }

    @Test
    public void parsingXml2asMember_returnsMemberMsg() throws Exception {
        Boolean isMember = true;
        List<Message> messages = parse(sampleMessagesB, dateBeforeMessages, isMember);
        assertTrue(messages.size() == 1);
        assertMessageB2(messages.get(0));
    }

    @Test
    public void parsingXml2asNonMember_returnsNonMemberMsg() throws Exception {
        Boolean isMember = false;
        List<Message> messages = parse(sampleMessagesB, dateBeforeMessages, isMember);
        assertTrue(messages.size() == 1);
        assertMessageB1(messages.get(0));
    }

    @Test
    public void parseFile() throws Exception {
        String xml = readFromResourceAsString("messages.xml");
        List<Message> messages = parse(xml, dateBeforeMessages);
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), dateMessage1, "a", null);
        String text = messages.get(0).text;
        assertEquals(LENGTH_OF_SAMPLE_MESSAGE_TEXT, text.length());
        assertTrue(text.contains("ThinkingRock"));
    }

    private String readFromResourceAsString(String fileName) throws IOException {
        Path path = Paths.get(getClass().getResource(fileName).getPath());
        return new String(Files.readAllBytes(path));
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
            parse(xml, dateBeforeMessages);
            fail("Should have thrown");
        } catch (Exception ex) {
            // Exception successfully thrown
        }
    }

    private void assertMessageA1(Message m) {
        assertMessage(m, dateMessage1, "a", "message1");
    }

    private void assertMessageA2(Message m) {
        assertMessage(m, dateMessage2, "b", "message2");
    }

    private void assertMessageB1(Message m) {
        assertMessage(m, dateMessage1, "n", "nonMemberMessage");
    }

    private void assertMessageB2(Message m) {
        assertMessage(m, dateMessage2, "m", "memberMessage");
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

package au.com.trgtd.tr.sync.device.v100.message.parse;

import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.exception.ReceiveException;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Receive.NewThought.*;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgThought;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * New thought message parser.
 */
class ParserNewThought implements IParser {

    @Override
    public RecvMsg parse(String msg) throws ParserException, ReceiveException {

        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }

        try {
            String title = SyncUtils.unEscape(matcher.group(1));
            String notes = SyncUtils.unEscape(matcher.group(2));
            int topicID = Integer.valueOf(matcher.group(3));

            return new RecvMsgThought(title, notes, topicID);
        }
        catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "New thought parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }



    private static final String CODE = RecvMsg.Type.NEWTHOUGHT.toString();

    private static final String REGEXP =
            CODE + RE_SEP +
            TITLE + RE_TITLE_GRP +
            NOTES + RE_NOTES_GRP +
            TOPIC_ID + RE_TOPIC_ID_GRP;

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

}

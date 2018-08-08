package au.com.trgtd.tr.sync.device.v100.message.parse;

import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.exception.ReceiveException;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Receive.Summary.*;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgSummary;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Summary message parser.
 */
class ParserSummary implements IParser {

    @Override
    public RecvMsg parse(String msg) throws ParserException, ReceiveException {

        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }

        try
        {
            String dataID = matcher.group(1);
            int nNewThgts = Integer.valueOf(matcher.group(2));
            int nUpdActns = Integer.valueOf(matcher.group(3));
            int nUpdPrjts = Integer.valueOf(matcher.group(4));
            int nReferences = Integer.valueOf(matcher.group(5));

            return new RecvMsgSummary(dataID, nNewThgts, nUpdActns, nUpdPrjts, nReferences);
        }
        catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Summary message parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }

    private static final String CODE = RecvMsg.Type.SUMMARY.toString();

    private static final String REGEXP =
            CODE + RE_SEP + DATA_ID + RE_ANY_GRP +
            THOUGHTS  + RE_NUM_NOT_NULL_GRP +
            ACTIONS  + RE_NUM_NOT_NULL_GRP + 
            PROJECTS  + RE_NUM_NOT_NULL_GRP + 
            REFERENCES + RE_NUM_NOT_NULL_GRP;

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

}

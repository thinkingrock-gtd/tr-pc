package au.com.trgtd.tr.sync.device.handshake;

import static au.com.trgtd.tr.sync.device.Constants.*;
import au.com.trgtd.tr.sync.device.exception.ParserException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handshake message parser.
 */
class SyncHSParser {

    public static final String RE_VER = "(\\d+\\.\\d+\\.\\d+)";    

    // RE for handshake message.
    private static final String RE_MSG =
            CODE_HANDSHAKE + RE_DELIM +
            FIELD_VERSION + RE_DELIM + RE_VER + RE_DELIM;

    // RE pattern for handshake message
    private static final Pattern PATTERN = Pattern.compile(RE_MSG);


    public SyncHSMsg parse(String msg) throws ParserException {

        Matcher matcher = PATTERN.matcher(msg);
        if (!matcher.find()) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)));
        }

        try
        {
            String version = String.valueOf(matcher.group(1));            
            return new SyncHSMsg(version);
        }
        catch (IllegalStateException ex) {
            throw new ParserException(getExceptionMessage(msg, matcher.group(0)), ex);
        }
    }

    private String getExceptionMessage(String messge, String regexp) {
        return "Handshake parse exception.\n"
                + "messge: " + messge + "\n"
                + "regexp: " + regexp + "\n";
    }

}

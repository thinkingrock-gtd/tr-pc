package au.com.trgtd.tr.sync.device.exception;

/**
 * Message parser exception.
 *
 * @author Jeremy Moore
 */
public class ParserException extends Exception {

    public ParserException(String msg) {
        super(msg);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

}

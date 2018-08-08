package au.com.trgtd.tr.sync.device.exception;

/**
 * Exception for message problems.
 *
 * @author Jeremy Moore
 */
public class ReceiveException extends Exception {

    public ReceiveException(String msg) {
        super(msg);
    }

    public ReceiveException(String message, Throwable cause) {
        super(message, cause);
    }


}

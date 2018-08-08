package au.com.trgtd.tr.sync.device.exception;

/**
 * Message processor exception.
 *
 * @author Jeremy Moore
 */
public class ProcessException extends Exception {

    public ProcessException(String msg) {
        super(msg);
    }

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
